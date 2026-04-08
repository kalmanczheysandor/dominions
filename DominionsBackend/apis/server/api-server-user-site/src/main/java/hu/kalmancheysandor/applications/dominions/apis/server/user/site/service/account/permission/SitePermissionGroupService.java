package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission;


import hu.kalmancheysandor.applications.dominions.apis.server.user.site.dto.account.permission.*;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUserAuthorisationGroup;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUserAuthorisationGroupPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account.SiteUserAuthorisationGroupPermissionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account.SiteUserAuthorisationGroupRepository;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.exception.SitePermissionGroupNameIsReservedException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.exception.SitePermissionGroupNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.exception.SitePermissionGroupNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.exception.SitePermissionGroupReferencedElsewhereException;
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
public class SitePermissionGroupService {

    @Autowired
    private SiteUserAuthorisationGroupRepository userAuthorisationGroupRepository;

    @Autowired
    private SiteUserAuthorisationGroupPermissionRepository userAuthorisationGroupPermissionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UUIDGenerator uuidGenerator;

    public SitePermissionGroupAccessResponse accessPermissionGroup(@NotBlank String uuid) {

        // Access entity via repository
        SiteUserAuthorisationGroup permissionGroup = userAuthorisationGroupRepository.findByUuid(uuid);
        if (permissionGroup == null) {
            throw new SitePermissionGroupNotFoundByUuidException(uuid);
        }

        // Generate response
        SitePermissionGroupAccessResponse response = modelMapper.map(permissionGroup, SitePermissionGroupAccessResponse.class);
        response.setPermissions(convert(permissionGroup.getPermissionList()));

        return response;
    }

    public List<SitePermissionGroupItemResponse> listAllPermissionGroup() {

        // Access entity via repository
        List<SiteUserAuthorisationGroup> siteUserAuthorisationGroupList = userAuthorisationGroupRepository.findAll();

        // Generate response
        return siteUserAuthorisationGroupList.stream()
            .map(item -> modelMapper.map(item, SitePermissionGroupItemResponse.class))
            .collect(Collectors.toList());
    }

    public SitePermissionGroupCreateResponse savePermissionGroup(@NotNull SitePermissionGroupCreateRequest request) {

        // Checking: Whether the new name is reserved
        if (userAuthorisationGroupRepository.isNameReserved(request.getName())) {
            throw new SitePermissionGroupNameIsReservedException(request.getName());
        }

        // Save the group
        SiteUserAuthorisationGroup groupToSave = new SiteUserAuthorisationGroup();
        groupToSave.setName(request.getName());
        groupToSave.setEnabled(request.isEnabled());
        SiteUserAuthorisationGroup groupSaved = uuidGenerator.saveWithRetry(userAuthorisationGroupRepository, groupToSave);

        // Store belonging child records
        flushAndStorePermissions(groupSaved.getId(), request.getPermissions());

        // Generate response
        SitePermissionGroupCreateResponse response = new SitePermissionGroupCreateResponse();
        response.setUuid(groupToSave.getUuid());
        response.setName(groupToSave.getName());
        response.setEnabled(groupToSave.isEnabled());
        if(groupSaved.getPermissionList()!=null) {
            response.setPermissions(convert(groupSaved.getPermissionList()));
        }
        return response;
    }

    public SitePermissionGroupUpdateResponse updatePermissionGroup(@NotBlank String uuid, @NotNull SitePermissionGroupUpdateRequest request) {

        // Access entity via repository
        SiteUserAuthorisationGroup groupToModify = userAuthorisationGroupRepository.findByUuid(uuid);
        if (groupToModify == null) {
            throw new SitePermissionGroupNotFoundByUuidException(uuid);
        }
        int permissionGroupId = groupToModify.getId();

        // Checking: Whether the new name is reserved
        if (userAuthorisationGroupRepository.isNameReserved(request.getName(), permissionGroupId)) {
            throw new SitePermissionGroupNameIsReservedException(request.getName());
        }

        // Modify the main record
        groupToModify.setName(request.getName());
        groupToModify.setEnabled(request.isEnabled());
        SiteUserAuthorisationGroup groupModified = uuidGenerator.saveWithRetry(userAuthorisationGroupRepository, groupToModify);

        // Store belonging child records
        flushAndStorePermissions(groupModified.getId(), request.getPermissions());

        // Generate response
        SitePermissionGroupUpdateResponse response = new SitePermissionGroupUpdateResponse();
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

    public void deleteMultiplePermissionGroup(@NotNull SitePermissionGroupDeleteRequest request) {

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
        SiteUserAuthorisationGroup permissionGroupToDelete = userAuthorisationGroupRepository.findByUuid(uuid);
        if (permissionGroupToDelete == null) {
            throw new SitePermissionGroupNotFoundByUuidException(uuid);
        }
        int permissionGroupId = permissionGroupToDelete.getId();

        // Check: whether any foreign key still referencing this record
        if (userAuthorisationGroupRepository.isReferencedElsewhere(permissionGroupId)) {
            throw new SitePermissionGroupReferencedElsewhereException(permissionGroupId,permissionGroupToDelete.getName());
        }

        // Delete item from db
        try {
            userAuthorisationGroupPermissionRepository.deleteAllPermissionsOfPermissionGroup(permissionGroupId);
            userAuthorisationGroupRepository.deleteById(permissionGroupId);
        } catch (EntityNotFoundException e) {
            throw new SitePermissionGroupNotFoundException(permissionGroupId);
        }
    }

    private List<String> convert(@NotNull List<SiteUserAuthorisationGroupPermission> list) {
        List<String> permissions;
        permissions = new ArrayList<>();
        list.forEach(permission -> {
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
        });
        return permissions;
    }

    private void flushAndStorePermissions(int permissionGroupId,@NotNull  List<String> permissionList) {
        // Remove all belonging old records
        userAuthorisationGroupPermissionRepository.deleteAllPermissionsOfPermissionGroup(permissionGroupId);

        // Save belonging permissions
        HashMap<String, SiteUserAuthorisationGroupPermission> permissions = new HashMap<>();
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

            SiteUserAuthorisationGroupPermission groupPermission;
            if (permissions.containsKey(namespace)) {
                groupPermission = permissions.get(namespace);
            } else {
                groupPermission = new SiteUserAuthorisationGroupPermission();
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
        permissions.put("Main", new SiteUserAuthorisationGroupPermission("Main",permissionGroupId,true,false,false,false));
        permissions.put("Account.Profile", new SiteUserAuthorisationGroupPermission("Account.Profile",permissionGroupId,true,true,true,true));

        for (SiteUserAuthorisationGroupPermission aGroupPermission : permissions.values()) {
            userAuthorisationGroupPermissionRepository.save(aGroupPermission);
        }
    }

}