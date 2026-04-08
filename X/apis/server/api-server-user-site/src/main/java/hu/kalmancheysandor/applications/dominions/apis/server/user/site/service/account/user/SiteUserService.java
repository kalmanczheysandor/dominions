package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.user;


import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.dto.account.user.*;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUser;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUserAuthorisationGroup;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUserGroup;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account.SiteUserAuthorisationGroupRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account.SiteUserGroupRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account.SiteUserRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.user.exception.*;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.authentication.SiteAuthenticationService;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.dto.account.user.option.SiteUserPermissionGroupOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.SitePermissionGroupService;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.exception.SitePermissionGroupAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.exception.SitePermissionGroupNotFoundByUuidException;
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
public class SiteUserService {

    @Autowired
    private SiteUserRepository siteUserRepository;

    @Autowired
    private SiteUserGroupRepository siteUserGroupRepository;

    @Autowired
    private SiteAuthenticationService siteAuthenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private SiteUserAuthorisationGroupRepository siteUserAuthorisationGroupRepository;


    @Autowired
    private SitePermissionGroupService sitePermissionGroupService;

    @Autowired
    private EntityManager entityManager;


    @Autowired
    private ApplicationConfig applicationConfig;

    public SiteUserAccessResponse accessUser(String uuid) {

        // Access entity via repository
        SiteUser siteUser = siteUserRepository.findByUuid(uuid);
        if (siteUser == null) {
            throw new SiteUserNotFoundByUuidException(uuid);
        }

        // Generate response
        List<String> groupUuidList = siteUser.getSiteUserGroups().stream()
                .map(siteUserGroup -> siteUserGroup.getGroupDetails().getUuid())
                .collect(Collectors.toList());
        SiteUserAccessResponse response = modelMapper.map(siteUser, SiteUserAccessResponse.class);
        response.setPermissionGroups(groupUuidList);

        return response;
    }

    public List<SiteUserItemResponse> listAllUser() {

        // Access entity via repository
        List<SiteUser> siteUserList = siteUserRepository.findAll();

        // Generate response
        List<SiteUserItemResponse> response = new ArrayList<>();
        for (SiteUser siteUser : siteUserList) {
            List<String> groupIdList = siteUser.getSiteUserGroups().stream()
                    .map(siteUserGroup -> siteUserGroup.getGroupDetails().getUuid())
                    .collect(Collectors.toList());

            SiteUserItemResponse item = modelMapper.map(siteUser, SiteUserItemResponse.class);
            item.setPermissionGroups(groupIdList);
            response.add(item);
        }

        return response;
    }

    public SiteUserSaveResponse saveUser(SiteUserSaveRequest request) {

        // Checking: Whether the confirmation is matching
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new SiteUserConfirmPasswordMismatchException();
        }

        // Checking: Whether the new identifier is reserved
        if (siteUserRepository.isIdentifierReserved(request.getIdentifier())) {
            throw new SiteUserIdentifierIsReservedException(request.getIdentifier());
        }

        // Checking: Whether the record is not finalised but enabled
        if (!request.isFinalised() && request.isEnabled()) {
            throw new SiteUserEnabledButNotFinalisedException();
        }

        // Checking: Whether each permission-group is enabled
        List<Integer> permissionGroupIdListToSave = new ArrayList<>();
        for (String permissionGroupUuid : request.getPermissionGroups()) {

            // Access entity via repository
            SiteUserAuthorisationGroup permissionGroupToAttach = siteUserAuthorisationGroupRepository.findByUuid(permissionGroupUuid);
            if (permissionGroupToAttach == null) {
                throw new SitePermissionGroupNotFoundByUuidException(permissionGroupUuid);
            }

            // Checking
            if (!permissionGroupToAttach.isEnabled()) {
                throw new SitePermissionGroupAssociationRestrictedException(permissionGroupToAttach.getId(), permissionGroupToAttach.getName());
            }

            permissionGroupIdListToSave.add(permissionGroupToAttach.getId());
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Save user
        SiteUser siteUserToSave = SiteUser.builder()
                .identifier(request.getIdentifier())
                .password(encodedPassword)
                .name(request.getName())
                .enabled(request.isEnabled())
                .finalised(request.isFinalised())
                .build();
        SiteUser siteUserSaved = uuidGenerator.saveWithRetry(siteUserRepository, siteUserToSave);

        // Save belonging permission-groups
        for (Integer permissionGroupId : permissionGroupIdListToSave) {
            siteUserGroupRepository.save(new SiteUserGroup(siteUserSaved.getId(), permissionGroupId));
        }

        // Reload
        entityManager.flush();
        entityManager.refresh(siteUserSaved);

        // Save image
        String filename = getUserPhotosFolder() + "/" + siteUserSaved.getId() + "/photo.jpg";
        FileHandler.updateBase64Image(filename, request.getImageBase64());

        // Generate response
        List<String> groupUuidList = siteUserSaved.getSiteUserGroups().stream()
                .map(siteUserGroup -> {
                    entityManager.flush();
                    entityManager.refresh(siteUserGroup);
                    return siteUserGroup.getGroupDetails().getUuid();
                })
                .collect(Collectors.toList());
        SiteUserSaveResponse response = modelMapper.map(siteUserSaved, SiteUserSaveResponse.class);
        response.setPermissionGroups(groupUuidList);

        return response;
    }

    public SiteUserUpdateResponse updateUser(String uuid, SiteUserUpdateRequest request) {
        // Checking: Whether the confirmation is matching (even it is empty)
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new SiteUserConfirmPasswordMismatchException();
        }

        // Access entity via repository
        SiteUser siteUserToModify = siteUserRepository.findByUuid(uuid);
        if (siteUserToModify == null) {
            throw new SiteUserNotFoundByUuidException(uuid);
        }
        int userId = siteUserToModify.getId();

        // Checking: Whether the new identifier is reserved
        if (siteUserRepository.isIdentifierReserved(request.getIdentifier(), userId)) {
            throw new SiteUserIdentifierIsReservedException(request.getIdentifier());
        }

        // Checking: Whether the record is not finalised but enabled
        if (!request.isFinalised() && request.isEnabled()) {
            throw new SiteUserEnabledButNotFinalisedException();
        }

        // Checking: Whether each permission-group is enabled
        List<Integer> permissionGroupIdListToModify = new ArrayList<>();
        for (String permissionGroupUuid : request.getPermissionGroups()) {
            // Access entity via repository
            SiteUserAuthorisationGroup permissionGroupToAttach = siteUserAuthorisationGroupRepository.findByUuid(permissionGroupUuid);
            if (permissionGroupToAttach == null) {
                throw new SitePermissionGroupNotFoundByUuidException(permissionGroupUuid);
            }

            // Checking: Whether the permission group enabled or already associated with the primary record
            if (!siteUserToModify.existsUserGroupWithGroupId(permissionGroupToAttach.getId()) && !permissionGroupToAttach.isEnabled()) {
                throw new SitePermissionGroupAssociationRestrictedException(permissionGroupToAttach.getId(), permissionGroupToAttach.getName());
            }

            permissionGroupIdListToModify.add(permissionGroupToAttach.getId());
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Modify user
        siteUserToModify.setIdentifier(request.getIdentifier());
        if (request.getPassword().length() > 0) {      // Keep former one if empty
            siteUserToModify.setPassword(encodedPassword);
        }
        siteUserToModify.setName(request.getName());
        siteUserToModify.setEnabled(request.isEnabled());
        siteUserToModify.setFinalised(request.isFinalised());
        SiteUser siteUserModified = uuidGenerator.saveWithRetry(siteUserRepository, siteUserToModify);

        // Modify belonging permission-groups
        siteUserGroupRepository.deleteAllGroupsOfUser(userId);
        entityManager.flush();
        entityManager.clear();
        siteUserModified = siteUserRepository.findByUuid(uuid);
        for (Integer permissionGroupId : permissionGroupIdListToModify) {
            siteUserGroupRepository.save(new SiteUserGroup(siteUserModified.getId(), permissionGroupId));
        }

        // Reload
        entityManager.flush();
        entityManager.refresh(siteUserModified);

        // Modify image
        String filename = getUserPhotosFolder() + "/" + siteUserToModify.getId() + "/photo.jpg";
        FileHandler.updateBase64Image(filename, request.getImageBase64());

        // Generate response
        List<String> groupUuidList = siteUserModified.getSiteUserGroups().stream()
                .map(siteUserGroup -> {
                    entityManager.flush();
                    entityManager.refresh(siteUserGroup);
                    return  siteUserGroup.getGroupDetails().getUuid();
                })
                .collect(Collectors.toList());
        SiteUserUpdateResponse response = modelMapper.map(siteUserModified, SiteUserUpdateResponse.class);
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

    public void deleteMultipleUser(SiteUserDeleteRequest request) {

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
        SiteUser siteUserToDelete = siteUserRepository.findByUuid(uuid);
        if (siteUserToDelete == null) {
            throw new SiteUserNotFoundByUuidException(uuid);
        }
        int userId = siteUserToDelete.getId();

        // Checking: Whether the current user attempting to delete himself/herself
        if (siteAuthenticationService.isCurrentUserAuthenticated() && siteAuthenticationService.getCurrentAuthenticatedUserId() == userId) {
            throw new SiteUserSelfDeleteException(userId);
        }

        // Check: whether any foreign key still referencing this record
        if (siteUserRepository.isReferencedElsewhere(userId)) {
            throw new SiteUserReferencedElsewhereException(userId, siteUserToDelete.getIdentifier());
        }


        // Execution
        try {
            siteUserGroupRepository.deleteAllGroupsOfUser(userId);
            siteUserRepository.deleteById(userId);
        } catch (EntityNotFoundException e) {
            throw new SiteUserNotFoundException(userId);
        }

        // Delete belonging files
        String folderPath = getUserPhotosFolder() + "/" + userId;
        FileHandler.deleteDirectoryRecursivelyIfExists(folderPath);
    }


    private String getUserPhotosFolder() {
        return applicationConfig.getAdminServer().getAccount().getSite().getUser().getPhotoPath();
    }

    public FileHandler.Result accessUserPhoto(@NotBlank String uuid) {

        // Access entity via repository
        SiteUser siteUserToModify = siteUserRepository.findByUuid(uuid);
        if (siteUserToModify == null) {
            throw new SiteUserNotFoundByUuidException(uuid);
        }
        int userId = siteUserToModify.getId();
        String fullPath = getUserPhotosFolder() + "/" + userId + "/photo.jpg";
        System.out.println("PHOTO fullPath: " + fullPath);
        return FileHandler.accessFileContentIfExists(fullPath);
    }


    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////// OPTIONS /////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<SiteUserPermissionGroupOptionResponse> atSaveListPermissionGroup() {

        List<SiteUserAuthorisationGroup> permissionGroupList = siteUserAuthorisationGroupRepository.listAllEnabled();
        return permissionGroupList.stream()
                .map(item -> modelMapper.map(item, SiteUserPermissionGroupOptionResponse.class))
                .collect(Collectors.toList());
    }

    public List<SiteUserPermissionGroupOptionResponse> atUpdateListPermissionGroup(@NotBlank String userUuid) {

        // Access entity via repository
        SiteUser siteUserToFind = siteUserRepository.findByUuid(userUuid);
        if (siteUserToFind == null) {
            throw new SiteUserNotFoundByUuidException(userUuid);
        }
        List<Integer> breedIdListToExclude = siteUserToFind.getSiteUserGroups().stream().map(item -> item.getGroupId()).collect(Collectors.toList());

        // Generate response
        List<SiteUserAuthorisationGroup> permissionGroupList = siteUserAuthorisationGroupRepository.listAllEnabledWithMultipleExcludedId(breedIdListToExclude);
        return permissionGroupList.stream()
                .map(item -> modelMapper.map(item, SiteUserPermissionGroupOptionResponse.class))
                .collect(Collectors.toList());
    }
}