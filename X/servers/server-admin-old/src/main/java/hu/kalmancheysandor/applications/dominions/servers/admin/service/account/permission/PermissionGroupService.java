package hu.kalmancheysandor.applications.dominions.service.account.permission;

import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.permission.*;
import hu.kalmancheysandor.applications.dominions.service.account.permission.exception.PermissionGroupNameIsReservedException;
import hu.kalmancheysandor.applications.dominions.service.account.permission.exception.PermissionGroupNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.account.permission.exception.PermissionGroupNotFoundException;
import hu.kalmancheysandor.applications.dominions.service.account.permission.exception.PermissionGroupReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.servers.admin.entity.account.UserAuthorisationGroup;
import hu.kalmancheysandor.applications.dominions.servers.admin.entity.account.UserAuthorisationGroupPermission;
import hu.kalmancheysandor.applications.dominions.service.account.permission.exception.*;
import hu.kalmancheysandor.applications.dominions.utils.uuid.UUIDGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PermissionGroupService {

    @Autowired
    private UserAuthorisationGroupRepository userAuthorisationGroupRepository;

    @Autowired
    private UserAuthorisationGroupPermissionRepository userAuthorisationGroupPermissionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UUIDGenerator uuidGenerator;

    public PermissionGroupResponse accessPermissionGroup(@NotBlank String uuid) {

        // Access entity via repository
        UserAuthorisationGroup permissionGroup = userAuthorisationGroupRepository.findByUuid(uuid);
        if (permissionGroup == null) {
            throw new PermissionGroupNotFoundByUuidException(uuid);
        }

        // Generate response
        PermissionGroupResponse response = modelMapper.map(permissionGroup, PermissionGroupResponse.class);
        response.setPermissions(convert(permissionGroup.getPermissionList()));

        return response;
    }

    public List<PermissionGroupResponse> listAllPermissionGroup() {

        // Access entity via repository
        List<UserAuthorisationGroup> userAuthorisationGroupList = userAuthorisationGroupRepository.findAll();

        // Generate response
        return userAuthorisationGroupList.stream()
            .map(item -> modelMapper.map(item, PermissionGroupResponse.class))
            .collect(Collectors.toList());
    }

    public PermissionGroupCreateResponse savePermissionGroup(@NotNull PermissionGroupCreateRequest request) {

        // Checking: Whether the new name is reserved
        if (userAuthorisationGroupRepository.isNameReserved(request.getName())) {
            throw new PermissionGroupNameIsReservedException(request.getName());
        }

        // Save the group
        UserAuthorisationGroup groupToSave = new UserAuthorisationGroup();
        groupToSave.setName(request.getName());
        groupToSave.setEnabled(request.isEnabled());
        UserAuthorisationGroup groupSaved = uuidGenerator.saveWithRetry(userAuthorisationGroupRepository, groupToSave);

        // Store belonging child records
        flushAndStorePermissions(groupSaved.getId(), request.getPermissions());

        // Generate response
        PermissionGroupCreateResponse response = new PermissionGroupCreateResponse();
        response.setUuid(groupToSave.getUuid());
        response.setName(groupToSave.getName());
        response.setEnabled(groupToSave.isEnabled());
        if(groupSaved.getPermissionList()!=null) {
            response.setPermissions(convert(groupSaved.getPermissionList()));
        }
        return response;
    }

    public PermissionGroupUpdateResponse updatePermissionGroup(@NotBlank String uuid, @NotNull PermissionGroupUpdateRequest request) {

        // Access entity via repository
        UserAuthorisationGroup groupToModify = userAuthorisationGroupRepository.findByUuid(uuid);
        if (groupToModify == null) {
            throw new PermissionGroupNotFoundByUuidException(uuid);
        }
        int permissionGroupId = groupToModify.getId();

        // Checking: Whether the new name is reserved
        if (userAuthorisationGroupRepository.isNameReserved(request.getName(), permissionGroupId)) {
            throw new PermissionGroupNameIsReservedException(request.getName());
        }

        // Modify the main record
        groupToModify.setName(request.getName());
        groupToModify.setEnabled(request.isEnabled());
        UserAuthorisationGroup groupModified = uuidGenerator.saveWithRetry(userAuthorisationGroupRepository, groupToModify);

        // Store belonging child records
        flushAndStorePermissions(groupModified.getId(), request.getPermissions());

        // Generate response
        PermissionGroupUpdateResponse response = new PermissionGroupUpdateResponse();
        response.setUuid(groupModified.getUuid());
        response.setName(groupModified.getName());
        response.setEnabled(groupModified.isEnabled());
        if(groupModified.getPermissionList()!=null) {
            response.setPermissions(convert(groupModified.getPermissionList()));
        }

        return response;
    }

    public void deleteOnePermissionGroup(@NotBlank String uuid) {

        // Execution
        try {
            this.deleteOneRow(uuid);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    public void deleteMultiplePermissionGroup(@NotNull PermissionGroupDeleteRequest request) {

        // Execution
        try {
            for (String uuid : request.getItems()) {
                this.deleteOneRow(uuid);
            }
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }

    private void deleteOneRow(@NotBlank String uuid) {

        // Access entity via repository
        UserAuthorisationGroup permissionGroupToDelete = userAuthorisationGroupRepository.findByUuid(uuid);
        if (permissionGroupToDelete == null) {
            throw new PermissionGroupNotFoundByUuidException(uuid);
        }
        int permissionGroupId = permissionGroupToDelete.getId();

        // Check: whether any foreign key still referencing this record
        if (userAuthorisationGroupRepository.isReferencedElsewhere(permissionGroupId)) {
            throw new PermissionGroupReferencedElsewhereException(permissionGroupId,permissionGroupToDelete.getName());
        }

        // Delete item from db
        try {
            userAuthorisationGroupPermissionRepository.deleteAllPermissionsOfPermissionGroup(permissionGroupId);
            userAuthorisationGroupRepository.deleteById(permissionGroupId);
        } catch (EntityNotFoundException e) {
            throw new PermissionGroupNotFoundException(permissionGroupId);
        }
    }

    private List<String> convert(@NotNull List<UserAuthorisationGroupPermission> list) {
        List<String> permissions = new ArrayList<>();
        for (UserAuthorisationGroupPermission permission : list) {

            if (permission.isPermissionIsView()) {
                permissions.add(permission.getTarget() + ":VIEW");
            }

            if (permission.isPermissionIsAdd()) {
                permissions.add(permission.getTarget() + ":ADD");
            }

            if (permission.isPermissionIsEdit()) {
                permissions.add(permission.getTarget() + ":EDIT");
            }

            if (permission.isPermissionIsDelete()) {
                permissions.add(permission.getTarget() + ":DELETE");
            }
        }
        return permissions;
    }

    private void flushAndStorePermissions(int permissionGroupId,@NotNull  List<String> permissionList) {
        // Remove all belonging old records
        userAuthorisationGroupPermissionRepository.deleteAllPermissionsOfPermissionGroup(permissionGroupId);

        // Save belonging permissions
        HashMap<String, UserAuthorisationGroupPermission> permissions = new HashMap<>();
        long count = 0;
        String namespace;
        String action;
        for (String permissionPath : permissionList) {
            count = permissionPath.chars().filter(c -> c == ':').count();

            if (count != 1) {
                throw new RuntimeException();
            }
            String[] parts = permissionPath.split(":", 2); // Limit to 2 parts
            if (parts.length == 2) {
                namespace = parts[0];
                action = parts[1];
            } else {
                throw new RuntimeException();
            }

            UserAuthorisationGroupPermission groupPermission;
            if (permissions.containsKey(namespace)) {
                groupPermission = permissions.get(namespace);
            } else {
                groupPermission = new UserAuthorisationGroupPermission();
                groupPermission.setTarget(namespace);
                groupPermission.setGroupId(permissionGroupId);
            }

            if ("VIEW".equals(action)) {
                groupPermission.setPermissionIsView(true);
            } else if ("ADD".equals(action)) {
                groupPermission.setPermissionIsAdd(true);
            } else if ("EDIT".equals(action)) {
                groupPermission.setPermissionIsEdit(true);
            } else if ("DELETE".equals(action)) {
                groupPermission.setPermissionIsDelete(true);
            }

            permissions.put(namespace, groupPermission);
        }
        // Add a fix ones
        permissions.put("Main", new UserAuthorisationGroupPermission("Main",permissionGroupId,true,false,false,false));
        permissions.put("Account.Profile", new UserAuthorisationGroupPermission("Account.Profile",permissionGroupId,true,true,true,true));

        for (UserAuthorisationGroupPermission aGroupPermission : permissions.values()) {
            userAuthorisationGroupPermissionRepository.save(aGroupPermission);
        }
    }

}