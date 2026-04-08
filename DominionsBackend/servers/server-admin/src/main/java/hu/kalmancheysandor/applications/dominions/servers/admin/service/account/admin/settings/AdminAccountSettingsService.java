package hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings;

import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUser;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account.AdminUserAuthorisationGroupRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account.AdminUserGroupRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account.AdminUserRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;

import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.account.admin.settings.AdminAccountSettingsCredentialPasswordAccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.account.admin.settings.AdminAccountSettingsCredentialPasswordUpdateRequest;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.account.admin.settings.AdminAccountSettingsProfileDetailsAccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.account.admin.settings.AdminAccountSettingsProfileDetailsUpdateRequest;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings.exception.AdminAccountSettingsCredentialConfirmPasswordMismatchException;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings.exception.AdminAccountSettingsCredentialCurrentPasswordMismatchException;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings.exception.AdminAccountSettingsUserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminAccountSettingsService {

    @Autowired
    private AdminUserRepository adminUserRepository;


    @Autowired
    private AdminAuthenticationService adminAuthenticationService;

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
        return applicationConfig.getAdminServer().getAccount().getAdmin().getSettings().getProfile().getPhotoPath();
    }
    
    public AdminAccountSettingsProfileDetailsAccessResponse accessAccountProfileDetails() {

        // Access entity via repository
        int userId = adminAuthenticationService.getCurrentAuthenticatedUserId();
        AdminUser userToAccess = adminUserRepository.findById(userId);
        if (userToAccess == null) {
            throw new AdminAccountSettingsUserNotFoundException(userId);
        }

        // Generate response
        AdminAccountSettingsProfileDetailsAccessResponse response = modelMapper.map(userToAccess, AdminAccountSettingsProfileDetailsAccessResponse.class);
        return response;
    }

    public void updateAccountProfileDetails(AdminAccountSettingsProfileDetailsUpdateRequest request) {

        // Access entity via repository
        int userId = adminAuthenticationService.getCurrentAuthenticatedUserId();
        AdminUser userToModify = adminUserRepository.findById(userId);
        if (userToModify == null) {
            throw new AdminAccountSettingsUserNotFoundException(userId);
        }

        // Modify settings
        userToModify.setName(request.getName());
        uuidGenerator.saveWithRetry(adminUserRepository, userToModify);

        // Modify image
        String filename = getProfilePhotoFolder() +"/" + userToModify.getId() + "/photo.jpg";
        FileHandler.updateBase64Image(filename, request.getImageBase64());
    }

    public FileHandler.Result accessAccountProfilePhoto() {

        // Access entity via repository
        int userId = adminAuthenticationService.getCurrentAuthenticatedUserId();
        AdminUser userToModify = adminUserRepository.findById(userId);
        if (userToModify == null) {
            throw new AdminAccountSettingsUserNotFoundException(userId);
        }

        // Access image file
        String fullPath = getProfilePhotoFolder() +"/" + userId + "/photo.jpg";
        return FileHandler.accessFileContentIfExists(fullPath);
    }

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////  CREDENTIAL  /////////////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AdminAccountSettingsCredentialPasswordAccessResponse accessAccountCredentialPassword() {

        // Access entity via repository
        int userId = adminAuthenticationService.getCurrentAuthenticatedUserId();
        AdminUser userToAccess = adminUserRepository.findById(userId);
        if (userToAccess == null) {
            throw new AdminAccountSettingsUserNotFoundException(userId);
        }

        // Generate response
        AdminAccountSettingsCredentialPasswordAccessResponse response = modelMapper.map(userToAccess, AdminAccountSettingsCredentialPasswordAccessResponse.class);
        return response;
    }


    public void updateAccountCredentialPassword(AdminAccountSettingsCredentialPasswordUpdateRequest request) {

        // Access entity via repository
        int userId = adminAuthenticationService.getCurrentAuthenticatedUserId();
        AdminUser userToModify = adminUserRepository.findById(userId);
        if (userToModify == null) {
            throw new AdminAccountSettingsUserNotFoundException(userId);
        }

        // Check: whether the given password is matching with the current one
        if (!passwordEncoder.matches(request.getCurrentPassword(),userToModify.getPassword())) {
            throw new AdminAccountSettingsCredentialCurrentPasswordMismatchException();
        }

        // Checking: Whether the password confirmation is matching
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new AdminAccountSettingsCredentialConfirmPasswordMismatchException();
        }

        // Encode password
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());

        // Modify settings
        userToModify.setPassword(encodedNewPassword);
        uuidGenerator.saveWithRetry(adminUserRepository, userToModify);
    }
}