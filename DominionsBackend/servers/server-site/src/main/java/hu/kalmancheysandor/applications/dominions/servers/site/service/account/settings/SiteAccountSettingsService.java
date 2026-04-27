package hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings;

import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUser;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account.SiteUserRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.authentication.SiteAuthenticationService;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.settings.SiteAccountSettingsCredentialPasswordAccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.settings.SiteAccountSettingsCredentialPasswordUpdateRequest;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.settings.SiteAccountSettingsProfileDetailsAccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.settings.SiteAccountSettingsProfileDetailsUpdateRequest;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings.exception.SiteAccountSettingsCredentialConfirmPasswordMismatchException;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings.exception.SiteAccountSettingsCredentialCurrentPasswordMismatchException;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings.exception.SiteAccountSettingsUserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SiteAccountSettingsService {

    @Autowired
    private SiteUserRepository siteUserRepository;

    @Autowired
    private SiteAuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private ApplicationConfig applicationConfig;

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////  PROFILE  ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String getProfilePhotoFolder() {
        return applicationConfig.getSiteServer().getAccount().getSettings().getProfile().getPhotoPath();
    }
    public SiteAccountSettingsProfileDetailsAccessResponse accessAccountProfileDetails() {
        System.out.println("XX-B1");
        // Access entity via repository
        int userId = authenticationService.getCurrentAuthenticatedUserId();
        SiteUser userToAccess = siteUserRepository.findById(userId);
        System.out.println("XX-B2");
        if (userToAccess == null) {
            throw new SiteAccountSettingsUserNotFoundException(userId);
        }
        System.out.println("XX-B3");
        // Generate response
        SiteAccountSettingsProfileDetailsAccessResponse response = modelMapper.map(userToAccess, SiteAccountSettingsProfileDetailsAccessResponse.class);
        System.out.println("XX-B4");
        return response;
    }

    public void updateAccountProfileDetails(SiteAccountSettingsProfileDetailsUpdateRequest request) {

        // Access entity via repository
        int userId = authenticationService.getCurrentAuthenticatedUserId();
        SiteUser userToModify = siteUserRepository.findById(userId);
        if (userToModify == null) {
            throw new SiteAccountSettingsUserNotFoundException(userId);
        }

        // Modify settings
        userToModify.setName(request.getName());
        uuidGenerator.saveWithRetry(siteUserRepository, userToModify);

        // Modify image
        String filename = getProfilePhotoFolder() +"/" + userToModify.getId() + "/photo.jpg";

        FileHandler.updateBase64Image(filename, request.getImageBase64());
    }

    public FileHandler.Result accessAccountProfilePhoto() {

        // Access entity via repository
        int userId = authenticationService.getCurrentAuthenticatedUserId();
        SiteUser userToModify = siteUserRepository.findById(userId);
        if (userToModify == null) {
            throw new SiteAccountSettingsUserNotFoundException(userId);
        }

        // Access image file
        String fullPath = getProfilePhotoFolder() +"/" + userId + "/photo.jpg";
        return FileHandler.accessFileContentIfExists(fullPath);
    }

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////  CREDENTIAL  /////////////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public SiteAccountSettingsCredentialPasswordAccessResponse accessAccountCredentialPassword() {

        // Access entity via repository
        int userId = authenticationService.getCurrentAuthenticatedUserId();
        SiteUser userToAccess = siteUserRepository.findById(userId);
        if (userToAccess == null) {
            throw new SiteAccountSettingsUserNotFoundException(userId);
        }

        // Generate response
        SiteAccountSettingsCredentialPasswordAccessResponse response = modelMapper.map(userToAccess, SiteAccountSettingsCredentialPasswordAccessResponse.class);
        return response;
    }


    public void updateAccountCredentialPassword(SiteAccountSettingsCredentialPasswordUpdateRequest request) {

        // Access entity via repository
        int userId = authenticationService.getCurrentAuthenticatedUserId();
        SiteUser userToModify = siteUserRepository.findById(userId);
        if (userToModify == null) {
            throw new SiteAccountSettingsUserNotFoundException(userId);
        }

        // Check: whether the given password is matching with the current one
        if (!passwordEncoder.matches(request.getCurrentPassword(),userToModify.getPassword())) {
            throw new SiteAccountSettingsCredentialCurrentPasswordMismatchException();
        }

        // Checking: Whether the password confirmation is matching
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new SiteAccountSettingsCredentialConfirmPasswordMismatchException();
        }

        // Encode password
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());

        // Modify settings
        userToModify.setPassword(encodedNewPassword);
        uuidGenerator.saveWithRetry(siteUserRepository, userToModify);
    }
}