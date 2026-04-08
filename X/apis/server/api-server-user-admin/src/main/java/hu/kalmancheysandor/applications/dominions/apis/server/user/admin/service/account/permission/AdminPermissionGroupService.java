package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission;

import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.dto.account.permission.*;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUserAuthorisationGroup;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUserAuthorisationGroupPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account.AdminUserAuthorisationGroupPermissionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account.AdminUserAuthorisationGroupRepository;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception.AdminPermissionGroupNameIsReservedException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception.AdminPermissionGroupNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception.AdminPermissionGroupNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception.AdminPermissionGroupReferencedElsewhereException;
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
public class AdminPermissionGroupService {

    @Autowired
    private AdminUserAuthorisationGroupRepository userAuthorisationGroupRepository;

    @Autowired
    private AdminUserAuthorisationGroupPermissionRepository userAuthorisationGroupPermissionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UUIDGenerator uuidGenerator;

    public AdminPermissionGroupAccessResponse accessPermissionGroup(@NotBlank String uuid) {

        // Access entity via repository
        AdminUserAuthorisationGroup permissionGroup = userAuthorisationGroupRepository.findByUuid(uuid);
        if (permissionGroup == null) {
            throw new AdminPermissionGroupNotFoundByUuidException(uuid);
        }

        // Generate response
        AdminPermissionGroupAccessResponse response = modelMapper.map(permissionGroup, AdminPermissionGroupAccessResponse.class);
        response.setPermissions(convert(permissionGroup.getPermissionList()));

        return response;
    }

    public List<AdminPermissionGroupItemResponse> listAllPermissionGroup() {

        // Access entity via repository
        List<AdminUserAuthorisationGroup> siteUserAuthorisationGroupList = userAuthorisationGroupRepository.findAll();

        // Generate response
        return siteUserAuthorisationGroupList.stream()
            .map(item -> modelMapper.map(item, AdminPermissionGroupItemResponse.class))
            .collect(Collectors.toList());
    }

    public AdminPermissionGroupCreateResponse savePermissionGroup(@NotNull AdminPermissionGroupCreateRequest request) {

        // Checking: Whether the new name is reserved
        if (userAuthorisationGroupRepository.isNameReserved(request.getName())) {
            throw new AdminPermissionGroupNameIsReservedException(request.getName());
        }

        // Save the group
        AdminUserAuthorisationGroup groupToSave = new AdminUserAuthorisationGroup();
        groupToSave.setName(request.getName());
        groupToSave.setEnabled(request.isEnabled());
        AdminUserAuthorisationGroup groupSaved = uuidGenerator.saveWithRetry(userAuthorisationGroupRepository, groupToSave);

        // Store belonging child records
        flushAndStorePermissions(groupSaved.getId(), request.getPermissions());

        // Generate response
        AdminPermissionGroupCreateResponse response = new AdminPermissionGroupCreateResponse();
        response.setUuid(groupToSave.getUuid());
        response.setName(groupToSave.getName());
        response.setEnabled(groupToSave.isEnabled());
        if(groupSaved.getPermissionList()!=null) {
            response.setPermissions(convert(groupSaved.getPermissionList()));
        }
        return response;
    }

    public AdminPermissionGroupUpdateResponse updatePermissionGroup(@NotBlank String uuid, @NotNull AdminPermissionGroupUpdateRequest request) {

        // Access entity via repository
        AdminUserAuthorisationGroup groupToModify = userAuthorisationGroupRepository.findByUuid(uuid);
        if (groupToModify == null) {
            throw new AdminPermissionGroupNotFoundByUuidException(uuid);
        }
        int permissionGroupId = groupToModify.getId();

        // Checking: Whether the new name is reserved
        if (userAuthorisationGroupRepository.isNameReserved(request.getName(), permissionGroupId)) {
            throw new AdminPermissionGroupNameIsReservedException(request.getName());
        }

        // Modify the main record
        groupToModify.setName(request.getName());
        groupToModify.setEnabled(request.isEnabled());
        AdminUserAuthorisationGroup groupModified = uuidGenerator.saveWithRetry(userAuthorisationGroupRepository, groupToModify);

        // Store belonging child records
        flushAndStorePermissions(groupModified.getId(), request.getPermissions());

        // Generate response
        AdminPermissionGroupUpdateResponse response = new AdminPermissionGroupUpdateResponse();
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

    public void deleteMultiplePermissionGroup(@NotNull AdminPermissionGroupDeleteRequest request) {

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
        AdminUserAuthorisationGroup permissionGroupToDelete = userAuthorisationGroupRepository.findByUuid(uuid);
        if (permissionGroupToDelete == null) {
            throw new AdminPermissionGroupNotFoundByUuidException(uuid);
        }
        int permissionGroupId = permissionGroupToDelete.getId();

        // Check: whether any foreign key still referencing this record
        if (userAuthorisationGroupRepository.isReferencedElsewhere(permissionGroupId)) {
            throw new AdminPermissionGroupReferencedElsewhereException(permissionGroupId,permissionGroupToDelete.getName());
        }

        // Delete item from db
        try {
            userAuthorisationGroupPermissionRepository.deleteAllPermissionsOfPermissionGroup(permissionGroupId);
            userAuthorisationGroupRepository.deleteById(permissionGroupId);
        } catch (EntityNotFoundException e) {
            throw new AdminPermissionGroupNotFoundException(permissionGroupId);
        }
    }

    private List<String> convert(@NotNull List<AdminUserAuthorisationGroupPermission> list) {
        List<String> permissions;
        permissions = new ArrayList<>();
        for (AdminUserAuthorisationGroupPermission permission : list) {

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
        HashMap<String, AdminUserAuthorisationGroupPermission> permissions = new HashMap<>();
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

            AdminUserAuthorisationGroupPermission groupPermission;
            if (permissions.containsKey(namespace)) {
                groupPermission = permissions.get(namespace);
            } else {
                groupPermission = new AdminUserAuthorisationGroupPermission();
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
        permissions.put("Main", new AdminUserAuthorisationGroupPermission("Main",permissionGroupId,true,false,false,false));
        permissions.put("Account.Profile", new AdminUserAuthorisationGroupPermission("Account.Profile",permissionGroupId,true,true,true,true));

        for (AdminUserAuthorisationGroupPermission aGroupPermission : permissions.values()) {
            userAuthorisationGroupPermissionRepository.save(aGroupPermission);
        }
    }

}