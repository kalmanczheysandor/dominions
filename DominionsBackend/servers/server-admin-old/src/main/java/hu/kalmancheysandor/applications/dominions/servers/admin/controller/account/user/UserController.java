package hu.kalmancheysandor.applications.dominions.servers.admin.controller.account.user;

import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.user.*;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.user.option.UserPermissionGroupOptionResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.user.UserService;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityAuthorisation;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityPermission;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/account/user")
public class UserController {

    @Autowired
    private UserService userService;

    //////////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////


    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().account().user());

        return userService.accessUser(uuid);
    }


    //////////////////////////////////////// LIST //////////////////////////////////////////////////////////////


    @GetMapping("/list")
    public List<UserResponse> listAllUser() {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().account().user());

        return userService.listAllUser();
    }

    //////////////////////////////////////// ADD ///////////////////////////////////////////////////////////////

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreateResponse addUser(@Valid @RequestBody UserCreateRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasAddPermission(SecurityPermission.generator().account().user());

        return userService.saveUser(request);
    }
    @GetMapping("/add/options/permission")
    public List<UserPermissionGroupOptionResponse> atAddListBreed() {
        // Check permission
        SecurityAuthorisation.assertHasAddPermission(SecurityPermission.generator().account().user());

        return userService.atSaveListPermissionGroup();
    }

    //////////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public UserUpdateResponse updateUser(@PathVariable("uuid") String uuid, @Valid @RequestBody UserUpdateRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasEditPermission(SecurityPermission.generator().account().user());

        return userService.updateUser(uuid,request);

    }


    @GetMapping("/{uuid}/edit/options/permission")
    public List<UserPermissionGroupOptionResponse> atEditListBreed(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasEditPermission(SecurityPermission.generator().account().user());

        return userService.atUpdateListPermissionGroup(uuid);
    }

    //////////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasDeletePermission(SecurityPermission.generator().account().user());

        userService.deleteOneUser(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody UserDeleteRequest request) {
        userService.deleteMultipleUser(request);
    }

}

