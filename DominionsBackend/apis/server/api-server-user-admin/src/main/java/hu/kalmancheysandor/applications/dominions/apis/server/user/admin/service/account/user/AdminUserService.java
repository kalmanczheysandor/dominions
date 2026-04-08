package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.user;


import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.dto.account.user.*;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.user.exception.*;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUser;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUserAuthorisationGroup;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUserGroup;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account.AdminUserAuthorisationGroupRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account.AdminUserGroupRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account.AdminUserRepository;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.dto.account.user.option.AdminUserPermissionGroupOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.AdminPermissionGroupService;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception.AdminPermissionGroupAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception.AdminPermissionGroupNotFoundByUuidException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private AdminUserGroupRepository adminUserGroupRepository;

    @Autowired
    private AdminAuthenticationService adminAuthenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private AdminUserAuthorisationGroupRepository adminUserAuthorisationGroupRepository;


    @Autowired
    private AdminPermissionGroupService adminPermissionGroupService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ApplicationConfig applicationConfig;

    public AdminUserAccessResponse accessUser(String uuid) {

        // Access entity via repository
        AdminUser adminUser = adminUserRepository.findByUuid(uuid);
        if (adminUser == null) {
            throw new AdminUserNotFoundByUuidException(uuid);
        }

        // Generate response
        List<String> groupUuidList = adminUser.getAdminUserGroups().stream()
                .map(adminUserGroup -> adminUserGroup.getGroupDetails().getUuid())
                .collect(Collectors.toList());
        AdminUserAccessResponse response = modelMapper.map(adminUser, AdminUserAccessResponse.class);
        response.setPermissionGroups(groupUuidList);

        return response;
    }

    public List<AdminUserItemResponse> listAllUser() {

        // Access entity via repository
        List<AdminUser> adminUserList = adminUserRepository.findAll();

        // Generate response
        List<AdminUserItemResponse> response = new ArrayList<>();
        for (AdminUser adminUser : adminUserList) {
            List<String> groupIdList = adminUser.getAdminUserGroups().stream()
                    .map(adminUserGroup -> adminUserGroup.getGroupDetails().getUuid())
                    .collect(Collectors.toList());

            AdminUserItemResponse item = modelMapper.map(adminUser, AdminUserItemResponse.class);
            item.setPermissionGroups(groupIdList);
            response.add(item);
        }

        return response;
    }

    public AdminUserSaveResponse saveUser(AdminUserSaveRequest request) {

        // Checking: Whether the confirmation is matching
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AdminUserConfirmPasswordMismatchException();
        }

        // Checking: Whether the new identifier is reserved
        if (adminUserRepository.isIdentifierReserved(request.getIdentifier())) {
            throw new AdminUserIdentifierIsReservedException(request.getIdentifier());
        }

        // Checking: Whether the record is not finalised but enabled
        if (!request.isFinalised() && request.isEnabled()) {
            throw new AdminUserEnabledButNotFinalisedException();
        }

        // Checking: Whether each permission-group is enabled
        List<Integer> permissionGroupIdListToSave = new ArrayList<>();
        for (String permissionGroupUuid : request.getPermissionGroups()) {

            // Access entity via repository
            AdminUserAuthorisationGroup permissionGroupToAttach = adminUserAuthorisationGroupRepository.findByUuid(permissionGroupUuid);
            if (permissionGroupToAttach == null) {
                throw new AdminPermissionGroupNotFoundByUuidException(permissionGroupUuid);
            }

            // Checking
            if (!permissionGroupToAttach.isEnabled()) {
                throw new AdminPermissionGroupAssociationRestrictedException(permissionGroupToAttach.getId(), permissionGroupToAttach.getName());
            }

            permissionGroupIdListToSave.add(permissionGroupToAttach.getId());
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Save user
        AdminUser adminUserToSave = AdminUser.builder()
                .identifier(request.getIdentifier())
                .password(encodedPassword)
                .name(request.getName())
                .enabled(request.isEnabled())
                .finalised(request.isFinalised())
                .build();
        AdminUser adminUserSaved = uuidGenerator.saveWithRetry(adminUserRepository, adminUserToSave);

        // Save belonging permission-groups
        for (Integer permissionGroupId : permissionGroupIdListToSave) {
            adminUserGroupRepository.save(new AdminUserGroup(adminUserSaved.getId(), permissionGroupId));
        }

        // Reload
        entityManager.flush();
        entityManager.refresh(adminUserSaved);

        // Save image
        String filename = getUserPhotosFolder() + "/" + adminUserSaved.getId() + "/photo.jpg";
        FileHandler.updateBase64Image(filename, request.getImageBase64());

        // Generate response
        List<String> groupUuidList = adminUserSaved.getAdminUserGroups().stream()
                .map(adminUserGroup -> {
                    entityManager.flush();
                    entityManager.refresh(adminUserGroup);
                    return adminUserGroup.getGroupDetails().getUuid();
                })
                .collect(Collectors.toList());
        AdminUserSaveResponse response = modelMapper.map(adminUserSaved, AdminUserSaveResponse.class);
        response.setPermissionGroups(groupUuidList);

        return response;
    }

    public AdminUserUpdateResponse updateUser(String uuid, AdminUserUpdateRequest request) {

        // Checking: Whether the confirmation is matching (even it is empty)
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AdminUserConfirmPasswordMismatchException();
        }

        // Access entity via repository
        AdminUser adminUserToModify = adminUserRepository.findByUuid(uuid);
        if (adminUserToModify == null) {
            throw new AdminUserNotFoundByUuidException(uuid);
        }
        int userId = adminUserToModify.getId();

        // Checking: Whether the new identifier is reserved
        if (adminUserRepository.isIdentifierReserved(request.getIdentifier(), userId)) {
            throw new AdminUserIdentifierIsReservedException(request.getIdentifier());
        }

        // Checking: Whether the record is not finalised but enabled
        if (!request.isFinalised() && request.isEnabled()) {
            throw new AdminUserEnabledButNotFinalisedException();
        }

        // Checking: Whether each permission-group is enabled
        List<Integer> permissionGroupIdListToModify = new ArrayList<>();
        for (String permissionGroupUuid : request.getPermissionGroups()) {
            // Access entity via repository
            AdminUserAuthorisationGroup permissionGroupToAttach = adminUserAuthorisationGroupRepository.findByUuid(permissionGroupUuid);
            if (permissionGroupToAttach == null) {
                throw new AdminPermissionGroupNotFoundByUuidException(permissionGroupUuid);
            }

            // Checking: Whether the permission group enabled or already associated with the primary record
            if (!adminUserToModify.existsUserGroupWithGroupId(permissionGroupToAttach.getId()) && !permissionGroupToAttach.isEnabled()) {
                throw new AdminPermissionGroupAssociationRestrictedException(permissionGroupToAttach.getId(), permissionGroupToAttach.getName());
            }

            permissionGroupIdListToModify.add(permissionGroupToAttach.getId());
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Modify user
        adminUserToModify.setIdentifier(request.getIdentifier());
        if (request.getPassword().length() > 0) {      // Keep former one if empty
            adminUserToModify.setPassword(encodedPassword);
        }
        adminUserToModify.setName(request.getName());
        adminUserToModify.setEnabled(request.isEnabled());
        adminUserToModify.setFinalised(request.isFinalised());
        AdminUser adminUserModified = uuidGenerator.saveWithRetry(adminUserRepository, adminUserToModify);

        // Modify belonging permission-groups
        adminUserGroupRepository.deleteAllGroupsOfUser(userId);
        entityManager.flush();
        entityManager.clear();
        adminUserModified = adminUserRepository.findByUuid(uuid);
        for (Integer permissionGroupId : permissionGroupIdListToModify) {
            adminUserGroupRepository.save(new AdminUserGroup(adminUserModified.getId(), permissionGroupId));
        }

        // Reload
        entityManager.flush();
        entityManager.refresh(adminUserModified);

        // Modify image
        String filename = getUserPhotosFolder() + "/" + adminUserModified.getId() + "/photo.jpg";
        FileHandler.updateBase64Image(filename, request.getImageBase64());

        // Generate response
        List<String> groupUuidList = adminUserModified.getAdminUserGroups().stream()
                .map(adminUserGroup -> {
                    entityManager.flush();
                    entityManager.refresh(adminUserGroup);
                    return adminUserGroup.getGroupDetails().getUuid();
                })
                .collect(Collectors.toList());
        AdminUserUpdateResponse response = modelMapper.map(adminUserModified, AdminUserUpdateResponse.class);
        response.setPermissionGroups(groupUuidList);

        return response;
    }

    public void deleteOneUser(String uuid) {

        // Execution
        try {
            this.deleteOneRow(uuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultipleUser(AdminUserDeleteRequest request) {
        // Execution
        try {
            for (String uuid : request.getItems()) {
                this.deleteOneRow(uuid);
            }
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    private void deleteOneRow(String uuid) {

        // Access entity via repository
        AdminUser adminUserToDelete = adminUserRepository.findByUuid(uuid);
        if (adminUserToDelete == null) {
            throw new AdminUserNotFoundByUuidException(uuid);
        }
        int userId = adminUserToDelete.getId();

        // Checking: Whether current user attempting to delete himself/herself
        if (adminAuthenticationService.isCurrentUserAuthenticated() && adminAuthenticationService.getCurrentAuthenticatedUserId() == userId) {
            throw new AdminUserSelfDeleteException(userId);
        }

        // Check: whether any foreign key still referencing this record
        if (adminUserRepository.isReferencedElsewhere(userId)) {
            throw new AdminUserReferencedElsewhereException(userId, adminUserToDelete.getIdentifier());
        }


        // Execution
        try {
            adminUserGroupRepository.deleteAllGroupsOfUser(userId);
            adminUserRepository.deleteById(userId);
        } catch (EntityNotFoundException e) {
            throw new AdminUserNotFoundException(userId);
        }
    }


    private String getUserPhotosFolder() {
        return applicationConfig.getAdminServer().getAccount().getAdmin().getUser().getPhotoPath();
    }

    public FileHandler.Result accessUserPhoto(@NotBlank String uuid) {

        // Access entity via repository
        AdminUser siteUserToModify = adminUserRepository.findByUuid(uuid);
        if (siteUserToModify == null) {
            throw new AdminUserNotFoundByUuidException(uuid);
        }
        int userId = siteUserToModify.getId();
        String fullPath = getUserPhotosFolder() + "/" + userId + "/photo.jpg";
        System.out.println("PHOTO fullPath: " + fullPath);
        return FileHandler.accessFileContentIfExists(fullPath);
    }


    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////// OPTIONS /////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<AdminUserPermissionGroupOptionResponse> atSaveListPermissionGroup() {

        List<AdminUserAuthorisationGroup> permissionGroupList = adminUserAuthorisationGroupRepository.listAllEnabled();
        return permissionGroupList.stream()
                .map(item -> modelMapper.map(item, AdminUserPermissionGroupOptionResponse.class))
                .collect(Collectors.toList());
    }

    public List<AdminUserPermissionGroupOptionResponse> atUpdateListPermissionGroup(@NotBlank String userUuid) {

        // Access entity via repository
        AdminUser adminUserToFind = adminUserRepository.findByUuid(userUuid);
        if (adminUserToFind == null) {
            throw new AdminUserNotFoundByUuidException(userUuid);
        }
        List<Integer> breedIdListToExclude = adminUserToFind.getAdminUserGroups().stream().map(item -> item.getGroupId()).collect(Collectors.toList());

        // Generate response
        List<AdminUserAuthorisationGroup> permissionGroupList = adminUserAuthorisationGroupRepository.listAllEnabledWithMultipleExcludedId(breedIdListToExclude);
        return permissionGroupList.stream()
                .map(item -> modelMapper.map(item, AdminUserPermissionGroupOptionResponse.class))
                .collect(Collectors.toList());
    }


}