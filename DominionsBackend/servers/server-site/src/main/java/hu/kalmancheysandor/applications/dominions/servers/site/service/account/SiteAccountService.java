package hu.kalmancheysandor.applications.dominions.servers.site.service.account;


import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.dto.SiteAccountRecoveryVerificationMailEnqueueRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.dto.SiteAccountSignUpVerificationMailEnqueueRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.etc.MailDeliveryData;
import hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.proxy.MailingServerProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.dto.account.user.SiteUserSaveRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.dto.account.user.SiteUserSaveResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUser;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUserAuthorisationGroup;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account.SiteUserAuthorisationGroupRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account.SiteUserRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.user.SiteUserService;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.recovery.*;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.signup.SiteAccountSignUpRequest;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.signup.SiteAccountSignUpResponse;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.signup.SiteAccountSignUpVerificationRequest;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.signup.SiteAccountSignUpVerificationResponse;
import hu.kalmancheysandor.applications.dominions.servers.site.entity.account.SiteAccountRecoveryVerification;
import hu.kalmancheysandor.applications.dominions.servers.site.entity.account.SiteAccountSignUpVerification;
import hu.kalmancheysandor.applications.dominions.servers.site.repository.account.SiteAccountRecoveryVerificationRepository;
import hu.kalmancheysandor.applications.dominions.servers.site.repository.account.SiteAccountSignUpVerificationRepository;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.recovery.*;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.signup.SiteAccountSignUpReservedUserIdentifierException;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.signup.SiteAccountSignUpUserDetailsNotFoundException;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.signup.SiteAccountSignUpUserUuidDuplicationException;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.signup.SiteAccountSignUpVerificationTokenNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SiteAccountService {

    @Autowired
    private SiteUserService siteUserService;

    @Autowired
    private SiteUserRepository siteUserRepository;

    @Autowired
    private SiteUserAuthorisationGroupRepository siteUserAuthorisationGroupRepository;

    @Autowired
    private SiteAccountSignUpVerificationRepository siteAccountSignUpVerificationRepository;

    @Autowired
    private SiteAccountRecoveryVerificationRepository siteAccountRecoveryVerificationRepository;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private MailingServerProxy mailingServerProxy;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public SiteAccountSignUpResponse signUp(@Valid @NotNull SiteAccountSignUpRequest request) {

        // Delete all non-verified and expired record
        deleteAllExpiredAccountSignUpVerification(LocalDateTime.now());

        // Find default permission group
        SiteUserAuthorisationGroup defaultAuthorisationGroup = siteUserAuthorisationGroupRepository.findByName("SiteUser");
        if (defaultAuthorisationGroup == null) {
            throw new RuntimeException("No default permission group was found!");
        }

        //
        if(siteUserRepository.isIdentifierReserved(request.getIdentifier())) {
            throw new SiteAccountSignUpReservedUserIdentifierException(request.getIdentifier());
        }


        // Save user record
        SiteUserSaveResponse userSaveResponse = siteUserService.saveUser(SiteUserSaveRequest.builder()
                .identifier(request.getIdentifier())
                .name(request.getName())
                .password(request.getPassword())
                .confirmPassword(request.getConfirmPassword())
                .enabled(false)
                .finalised(false)
                .permissionGroups(List.of(defaultAuthorisationGroup.getUuid()))
                .build()
        );

        // Avoid multiplication of user uuid
        if (siteAccountSignUpVerificationRepository.isUserUuidAlreadyUsed(userSaveResponse.getUuid())) {
            throw new SiteAccountSignUpUserUuidDuplicationException(userSaveResponse.getUuid());
        }

        // Generate validation entry
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime expirationTime = nowTime.plusMinutes(applicationConfig.getSiteServer().getAccount().getSignUp().getVerificationExpirationMinutes());
        SiteAccountSignUpVerification verificationToSave = SiteAccountSignUpVerification.builder()
                .userUuid(userSaveResponse.getUuid())
                .dateCreated(nowTime)
                .dateExpiration(expirationTime)
                .build();
        SiteAccountSignUpVerification verificationSaved = uuidGenerator.saveWithRetry(siteAccountSignUpVerificationRepository, verificationToSave);


        // Communication to mail delivery sub-system via proxy
        mailingServerProxy.enqueueSiteAccountSignUpVerificationMail(SiteAccountSignUpVerificationMailEnqueueRequest.builder()
                .mailDeliveryData(MailDeliveryData.builder()
                        .senderEmail("noreply@game.dominions.hu")
                        .recipientEmail("kalmanczheysandor@gmail.com")
                        .subject("SignUp verification")
                        .build()
                )
                .verificationToken(verificationSaved.getUuid())
                .expirationTime(expirationTime.toString())
                .userName(userSaveResponse.getName())
                .build()
        );

        return SiteAccountSignUpResponse.builder().build();
    }

    public SiteAccountSignUpVerificationResponse signUpVerification(@Valid @NotNull SiteAccountSignUpVerificationRequest request) {

        // Delete all non-verified and expired record
        deleteAllExpiredAccountSignUpVerification(LocalDateTime.now());

        // Attempt to access the validation record
        SiteAccountSignUpVerification verificationRecord = siteAccountSignUpVerificationRepository.findByUuid(request.getVerificationToken());
        if (verificationRecord == null) {
            throw new SiteAccountSignUpVerificationTokenNotFoundException(request.getVerificationToken());
        }

        // Find and finalise the specified user record
        SiteUser userToVerify = siteUserRepository.findByUuid(verificationRecord.getUserUuid());
        if (userToVerify == null) {
            throw new SiteAccountSignUpUserDetailsNotFoundException();
        }
        userToVerify.setEnabled(true);
        userToVerify.setFinalised(true);

        // Removal of registry record
        siteAccountSignUpVerificationRepository.deleteByUuid(request.getVerificationToken());
        entityManager.flush();

        return SiteAccountSignUpVerificationResponse.builder().build();
    }


    public SiteAccountSignUpResponse recovery(@Valid @NotNull SiteAccountRecoveryRequest request) {
        // Delete all non-verified and expired record
        deleteAllExpiredAccountRecoveryVerification(LocalDateTime.now());
        System.out.println("---recovery");
        // Attempting to find user details by its identifier
        SiteUser user = siteUserRepository.findByIdentifier(request.getIdentifier());
        if (user == null) {
            throw new SiteAccountRecoveryIdentifierNotFoundException(request.getIdentifier());
        }
        if (!user.isFinalised()) {
            throw new SiteAccountRecoveryUserNotFinalisedException();
        }
        if (!user.isEnabled()) {
            throw new SiteAccountRecoveryUserNotEnabledException();
        }

        // Delete previous but still not expired record of this user
        if (siteAccountRecoveryVerificationRepository.isUserUuidAlreadyUsed(user.getUuid())) {
            SiteAccountRecoveryVerification verificationRecordToRemove = siteAccountRecoveryVerificationRepository.findByUserUuid(user.getUuid());
            siteAccountRecoveryVerificationRepository.delete(verificationRecordToRemove);
        }

        // Generate validation entry
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime expirationTime = nowTime.plusMinutes(applicationConfig.getSiteServer().getAccount().getRecovery().getVerificationExpirationMinutes());
        SiteAccountRecoveryVerification verificationToSave = SiteAccountRecoveryVerification.builder()
                .userUuid(user.getUuid())
                .dateCreated(nowTime)
                .dateExpiration(expirationTime)
                .build();
        SiteAccountRecoveryVerification verificationSaved = uuidGenerator.saveWithRetry(siteAccountRecoveryVerificationRepository, verificationToSave);


        // Communication to mail delivery sub-system via proxy
        mailingServerProxy.enqueueSiteAccountRecoveryVerificationMail(SiteAccountRecoveryVerificationMailEnqueueRequest.builder()
                .mailDeliveryData(MailDeliveryData.builder()
                        .senderEmail("noreply@game.dominions.hu")
                        .recipientEmail("kalmanczheysandor@gmail.com")
                        .subject("Recovery verification")
                        .build()
                )
                .verificationToken(verificationSaved.getUuid())
                .expirationTime(expirationTime.toString())
                .userName(user.getName())
                .build()
        );

        return SiteAccountSignUpResponse.builder().build();
    }

//
//    public SiteAccountRecoveryVerificationResponse recoveryVerification(@Valid @NotNull SiteAccountRecoveryVerificationRequest request) {
//
//        // Delete all non-verified and expired record
//        deleteAllExpiredAccountRecoveryVerification(LocalDateTime.now());
//
//        // Attempt to access the validation record
//        SiteAccountRecoveryVerification verificationRecord = siteAccountRecoveryVerificationRepository.findByUuid(request.getVerificationToken());
//        if (verificationRecord == null) {
//            throw new SiteAccountRecoveryVerificationTokenNotFoundException(request.getVerificationToken());
//        }
//
//        // Attempting to find user details
//        SiteUser user = siteUserRepository.findByIdentifier(request.getVerificationToken());
//        if (user == null) {
//            throw new SiteAccountSignUpUserDetailsNotFoundException();
//        }
//        if (!user.isFinalised()) {
//            throw new SiteAccountRecoveryUserNotFinalisedFoundException();
//        }
//        if (!user.isEnabled()) {
//            throw new SiteAccountRecoveryUserNotEnabledFoundException();
//        }
//
//        return SiteAccountRecoveryVerificationResponse.builder().build();
//    }


    public SiteAccountRecoverySubmitResponse recoverySubmit(@Valid @NotNull SiteAccountRecoverySubmitRequest request) {

        // Delete all non-verified and expired record
        deleteAllExpiredAccountRecoveryVerification(LocalDateTime.now());
        System.out.println("---recoverySubmit");
        // Checking: Whether the confirmation is matching (even it is empty)
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new SiteAccountRecoveryConfirmPasswordMismatchException();
        }

        // Attempt to access the validation record
        SiteAccountRecoveryVerification verificationRecord = siteAccountRecoveryVerificationRepository.findByUuid(request.getVerificationToken());
        if (verificationRecord == null) {
            throw new SiteAccountRecoveryVerificationTokenNotFoundException(request.getVerificationToken());
        }

        // Find and finalise the specified user record
        SiteUser userToVerify = siteUserRepository.findByUuid(verificationRecord.getUserUuid());
        if (userToVerify == null) {
            throw new SiteAccountRecoveryUserDetailsNotFoundException();
        }
        if (!userToVerify.isFinalised()) {
            throw new SiteAccountRecoveryUserNotFinalisedException();
        }
        if (!userToVerify.isEnabled()) {
            throw new SiteAccountRecoveryUserNotEnabledException();
        }

        // Encode password
        userToVerify.setPassword(passwordEncoder.encode(request.getPassword()));


        // Removal of registry record
        siteAccountRecoveryVerificationRepository.deleteByUuid(request.getVerificationToken());
        entityManager.flush();

        return SiteAccountRecoverySubmitResponse.builder().build();
    }

    private void deleteAllExpiredAccountSignUpVerification(LocalDateTime timeNow) {
        List<SiteAccountSignUpVerification> expiredVerificationsList = siteAccountSignUpVerificationRepository.selectAllExpired(timeNow);
        for (SiteAccountSignUpVerification verificationRecord : expiredVerificationsList) {
            SiteUser userToRemove = siteUserRepository.findByUuid(verificationRecord.getUserUuid());
            if (userToRemove != null && !userToRemove.isFinalised()) {
                siteUserService.deleteOneUser(userToRemove.getUuid());
            }
            siteAccountSignUpVerificationRepository.delete(verificationRecord);
        }
    }

    private void deleteAllExpiredAccountRecoveryVerification(LocalDateTime timeNow) {
        siteAccountRecoveryVerificationRepository.deleteAllExpired(timeNow);
    }
}
