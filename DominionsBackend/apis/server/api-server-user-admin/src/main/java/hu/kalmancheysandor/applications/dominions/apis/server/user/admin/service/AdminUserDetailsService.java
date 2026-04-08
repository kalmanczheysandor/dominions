package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service;


import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUser;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUserPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account.AdminUserPermissionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account.AdminUserRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.security.AdminUserSecurityDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;
    private final AdminUserPermissionRepository adminUserPermissionRepository;

    public AdminUserDetailsService(AdminUserRepository adminUserRepository, AdminUserPermissionRepository adminUserPermissionRepository) {
        this.adminUserRepository = adminUserRepository;
        this.adminUserPermissionRepository = adminUserPermissionRepository;
    }

    @Override
    public AdminUserSecurityDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByIdentifier(username);
        if (adminUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Load user permissions
        Set<AdminPermission> authorities = new HashSet<>();

        // Adding some permissions which have to even when no enabled permission group is attached.
        authorities.add(new AdminPermission("Main:VIEW"));
        authorities.add(new AdminPermission("Account.Profile:VIEW"));
        authorities.add(new AdminPermission("Account.Profile:ADD"));
        authorities.add(new AdminPermission("Account.Profile:EDIT"));
        authorities.add(new AdminPermission("Account.Profile:DELETE"));


        List<AdminUserPermission> permissionList = adminUserPermissionRepository.listAllAtUserId(adminUser.getId());
        for (AdminUserPermission permission : permissionList) {
            if (permission.isPermissionIsView()) {
                authorities.add(new AdminPermission(permission.getPermissionTarget() + ":" + "VIEW"));
            }

            if (permission.isPermissionIsAdd()) {
                authorities.add(new AdminPermission(permission.getPermissionTarget() + ":" + "ADD"));
            }

            if (permission.isPermissionIsEdit()) {
                authorities.add(new AdminPermission(permission.getPermissionTarget() + ":" + "EDIT"));
            }

            if (permission.isPermissionIsDelete()) {
                authorities.add(new AdminPermission(permission.getPermissionTarget() + ":" + "DELETE"));
            }
        }

        //
        AdminUserSecurityDetails securityUserDetails = new AdminUserSecurityDetails();

        securityUserDetails.setId(adminUser.getId());
        securityUserDetails.setUsername(adminUser.getIdentifier());
        securityUserDetails.setPassword(adminUser.getPassword());
        securityUserDetails.setAuthorities(authorities);
        securityUserDetails.setEnabled(adminUser.isEnabled());
        return securityUserDetails;
    }

    public boolean isEnabled(String username) {
        return adminUserRepository.isEnabled(username);
    }

    public AdminUser findUserById(int userId) {
        return adminUserRepository.findById(userId);
    }


}
