package hu.kalmancheysandor.applications.dominions.service.account.profile;

import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.profile.ProfileCredentialPasswordAccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.profile.ProfileCredentialPasswordUpdateRequest;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.profile.ProfileDetailsAccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.profile.ProfileDetailsUpdateRequest;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.user.*;
import hu.kalmancheysandor.applications.dominions.service.account.permission.PermissionGroupService;
import hu.kalmancheysandor.applications.dominions.service.account.permission.exception.PermissionGroupAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.service.account.user.exception.*;
import hu.kalmancheysandor.applications.dominions.service.authentication.AuthenticationService;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityAuthorisation;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityPermission;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.user.option.UserPermissionGroupOptionResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.entity.account.User;
import hu.kalmancheysandor.applications.dominions.servers.admin.entity.account.UserAuthorisationGroup;
import hu.kalmancheysandor.applications.dominions.servers.admin.entity.account.UserGroup;
import hu.kalmancheysandor.applications.dominions.service.account.permission.exception.PermissionGroupNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.account.profile.exception.ProfileConfirmPasswordMismatchException;
import hu.kalmancheysandor.applications.dominions.service.account.profile.exception.ProfileCurrentPasswordMismatchException;
import hu.kalmancheysandor.applications.dominions.service.account.profile.exception.ProfileNotFoundException;
import hu.kalmancheysandor.applications.dominions.service.dog.exception.DogNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.utils.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.utils.uuid.UUIDGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    UserAuthorisationGroupRepository userAuthorisationGroupRepository;


    @Autowired
    private PermissionGroupService permissionGroupService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FileHandler fileHandler;


    public ProfileDetailsAccessResponse accessProfileDetails() {

        // Access entity via repository
        int profileId = authenticationService.getCurrentAuthenticatedUserId();
        User profileToAccess = userRepository.findById(profileId);
        if (profileToAccess == null) {
            throw new ProfileNotFoundException(profileId);
        }

        // Generate response
        ProfileDetailsAccessResponse response = modelMapper.map(profileToAccess, ProfileDetailsAccessResponse.class);
        return response;
    }

    public void updateProfileDetails(ProfileDetailsUpdateRequest request) {

        // Access entity via repository
        int profileId = authenticationService.getCurrentAuthenticatedUserId();
        User profileToModify = userRepository.findById(profileId);
        if (profileToModify == null) {
            throw new ProfileNotFoundException(profileId);
        }

        // Modify profile
        profileToModify.setName(request.getName());
        uuidGenerator.saveWithRetry(userRepository, profileToModify);

        // Modify image
        String filename = "d:/user/" + profileToModify.getId() + "/main.jpg";
        fileHandler.updateBase64Image(filename, request.getImageBase64());
    }

    public FileHandler.Result image() {


        // Access entity via repository
        int profileId = authenticationService.getCurrentAuthenticatedUserId();
        User profileToModify = userRepository.findById(profileId);
        if (profileToModify == null) {
            throw new ProfileNotFoundException(profileId);
        }

        // Access image file
        String fullPath = "d:/user/" + profileId + "/main.jpg";
        return fileHandler.findFile(fullPath);
    }











    public ProfileCredentialPasswordAccessResponse accessProfileCredentialPassword() {

        // Access entity via repository
        int profileId = authenticationService.getCurrentAuthenticatedUserId();
        User profileToAccess = userRepository.findById(profileId);
        if (profileToAccess == null) {
            throw new ProfileNotFoundException(profileId);
        }

        // Generate response
        ProfileCredentialPasswordAccessResponse response = modelMapper.map(profileToAccess, ProfileCredentialPasswordAccessResponse.class);
        return response;
    }


    public void updateProfileCredentialPassword(ProfileCredentialPasswordUpdateRequest request) {



        // Access entity via repository
        int profileId = authenticationService.getCurrentAuthenticatedUserId();
        User profileToModify = userRepository.findById(profileId);
        if (profileToModify == null) {
            throw new ProfileNotFoundException(profileId);
        }

        // Check: whether the given password is matching with the current one
        if (!passwordEncoder.matches(request.getCurrentPassword(),profileToModify.getPassword())) {
            throw new ProfileCurrentPasswordMismatchException();
        }

        // Checking: Whether the password confirmation is matching
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new ProfileConfirmPasswordMismatchException();
        }

        // Encode password
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());

        // Modify profile
        profileToModify.setPassword(encodedNewPassword);
        uuidGenerator.saveWithRetry(userRepository, profileToModify);
    }


    public List<UserResponse> listAllUser() {

        // Access entity via repository
        List<User> userList = userRepository.findAll();

        // Generate response
        List<UserResponse> response = new ArrayList<>();
        for (User user : userList) {
            List<String> groupIdList = user.getUserGroups().stream()
                .map(userGroup -> userGroup.getGroupDetails().getUuid())
                .collect(Collectors.toList());

            UserResponse item = modelMapper.map(user, UserResponse.class);
            item.setPermissionGroups(groupIdList);
            response.add(item);
        }

        return response;
    }

    public UserCreateResponse saveUser(UserCreateRequest request) {

        // Checking: Whether the confirmation is matching
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new UserConfirmPasswordMismatchException();
        }

        // Checking: Whether the new identifier is reserved
        if (userRepository.isIdentifierReserved(request.getIdentifier())) {
            throw new UserIdentifierIsReservedException(request.getIdentifier());
        }

        // Checking: Whether each permission-group is enabled
        List<Integer> permissionGroupIdListToSave = new ArrayList<>();
        for (String permissionGroupUuid : request.getPermissionGroups()) {

            // Access entity via repository
            UserAuthorisationGroup permissionGroupToAttach = userAuthorisationGroupRepository.findByUuid(permissionGroupUuid);
            if (permissionGroupToAttach == null) {
                throw new PermissionGroupNotFoundByUuidException(permissionGroupUuid);
            }

            // Checking
            if (!permissionGroupToAttach.isEnabled()) {
                throw new PermissionGroupAssociationRestrictedException(permissionGroupToAttach.getId(), permissionGroupToAttach.getName());
            }

            permissionGroupIdListToSave.add(permissionGroupToAttach.getId());
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Save user
        User userToSave = new User();
        userToSave.setIdentifier(request.getIdentifier());
        userToSave.setPassword(encodedPassword);
        userToSave.setName(request.getName());
        userToSave.setEnabled(request.isEnabled());
        User userSaved = uuidGenerator.saveWithRetry(userRepository, userToSave);

        // Save belonging permission-groups
        for (Integer permissionGroupId : permissionGroupIdListToSave) {
            userGroupRepository.save(new UserGroup(userSaved.getId(), permissionGroupId));
        }

        // Generate response
        entityManager.flush();
        entityManager.refresh(userSaved);
        List<String> groupUuidList = userSaved.getUserGroups().stream()
            .map(userGroup -> {
                entityManager.flush();
                entityManager.refresh(userGroup);
                return userGroup.getGroupDetails().getUuid();
            })
            .collect(Collectors.toList());
        UserCreateResponse response = modelMapper.map(userSaved, UserCreateResponse.class);
        response.setPermissionGroups(groupUuidList);

        return response;
    }

    public UserUpdateResponse updateUser(String uuid, UserUpdateRequest request) {

        // Checking: Whether the confirmation is matching (even it is empty)
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new UserConfirmPasswordMismatchException();
        }

        // Access entity via repository
        User userToModify = userRepository.findByUuid(uuid);
        if (userToModify == null) {
            throw new UserNotFoundByUuidException(uuid);
        }
        int userId = userToModify.getId();

        // Checking: Whether the new identifier is reserved
        if (userRepository.isIdentifierReserved(request.getIdentifier(), userId)) {
            throw new UserIdentifierIsReservedException(request.getIdentifier());
        }

        // Checking: Whether each permission-group is enabled
        List<Integer> permissionGroupIdListToModify = new ArrayList<>();
        for (String permissionGroupUuid : request.getPermissionGroups()) {
            // Access entity via repository
            UserAuthorisationGroup permissionGroupToAttach = userAuthorisationGroupRepository.findByUuid(permissionGroupUuid);
            if (permissionGroupToAttach == null) {
                throw new PermissionGroupNotFoundByUuidException(permissionGroupUuid);
            }

            // Checking: Whether the permission group enabled or already associated with the primary record
            if (!userToModify.existsUserGroupWithGroupId(permissionGroupToAttach.getId()) && !permissionGroupToAttach.isEnabled()) {
                throw new PermissionGroupAssociationRestrictedException(permissionGroupToAttach.getId(), permissionGroupToAttach.getName());
            }

            permissionGroupIdListToModify.add(permissionGroupToAttach.getId());
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Modify user
        userToModify.setIdentifier(request.getIdentifier());
        if (request.getPassword().length() > 0) {      // Keep former one if empty
            userToModify.setPassword(encodedPassword);
        }
        userToModify.setName(request.getName());
        userToModify.setEnabled(request.isEnabled());
        User userModified = uuidGenerator.saveWithRetry(userRepository, userToModify);

        // Modify belonging permission-groups
        userGroupRepository.deleteAllGroupsOfUser(userId);       // Disabled ones must be kept
        for (Integer permissionGroupId : permissionGroupIdListToModify) {
            userGroupRepository.save(new UserGroup(userModified.getId(), permissionGroupId));
        }

        // Generate response
        entityManager.flush();
        entityManager.refresh(userModified);
        List<String> groupUuidList = userModified.getUserGroups().stream()
            .map(userGroup -> {
                entityManager.flush();
                entityManager.refresh(userGroup);
                return userGroup.getGroupDetails().getUuid();
            })
            .collect(Collectors.toList());
        UserUpdateResponse response = modelMapper.map(userModified, UserUpdateResponse.class);
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

    public void deleteMultipleUser(UserDeleteRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasDeletePermission(SecurityPermission.generator().account().user());

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
        User userToDelete = userRepository.findByUuid(uuid);
        if (userToDelete == null) {
            throw new UserNotFoundByUuidException(uuid);
        }
        int userId = userToDelete.getId();

        // Checking: Whether current user attempting to delete himself/herself
        if (authenticationService.getCurrentAuthenticatedUserId() == userId) {
            throw new UserSelfDeleteException(userId);
        }

        // Check: whether any foreign key still referencing this record
        if (userRepository.isReferencedElsewhere(userId)) {
            throw new UserReferencedElsewhereException(userId, userToDelete.getIdentifier());
        }


        // Execution
        try {
            userGroupRepository.deleteAllGroupsOfUser(userId);
            userRepository.deleteById(userId);
        } catch (EntityNotFoundException e) {
            throw new UserNotFoundException(userId);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// OPTIONS /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<UserPermissionGroupOptionResponse> atSaveListPermissionGroup() {

        List<UserAuthorisationGroup> permissionGroupList = userAuthorisationGroupRepository.listAllEnabled();
        return permissionGroupList.stream()
            .map(item -> modelMapper.map(item, UserPermissionGroupOptionResponse.class))
            .collect(Collectors.toList());
    }

    public List<UserPermissionGroupOptionResponse> atUpdateListPermissionGroup(@NotBlank String userUuid) {

        // Access entity via repository
        User userToFind = userRepository.findByUuid(userUuid);
        if (userToFind == null) {
            throw new DogNotFoundByUuidException(userUuid);
        }
        List<Integer> breedIdListToExclude = userToFind.getUserGroups().stream().map(item -> item.getGroupId()).collect(Collectors.toList());

        // Generate response
        List<UserAuthorisationGroup> permissionGroupList = userAuthorisationGroupRepository.listAllEnabledWithMultipleExcludedId(breedIdListToExclude);
        return permissionGroupList.stream()
            .map(item -> modelMapper.map(item, UserPermissionGroupOptionResponse.class))
            .collect(Collectors.toList());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// ??????? /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    public List<UserPermissionGroupData> listAllPermissionGroup() {
//        // Check permission
//        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().account().user());
//
//        // Generate response
//        return userAuthorisationGroupRepository.findAll().stream()
//            .map(g -> new UserPermissionGroupData(g.getId(), g.getName(), false))
//            .collect(Collectors.toList());
//    }
//
//    public List<UserPermissionGroupData> listAllPermissionGroup(boolean isEnabled) {
//        // Check permission
//        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().account().user());
//
//        // Generate response
//        return userAuthorisationGroupRepository.findAllByEnabled(isEnabled).stream()
//            .map(g -> new UserPermissionGroupData(g.getId(), g.getName(), false))
//            .collect(Collectors.toList());
//    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPermissionGroupData {
        private int id;
        private String name;
        private boolean selected;
    }


}