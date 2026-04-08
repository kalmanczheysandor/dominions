package hu.kalmancheysandor.applications.dominions.servers.test.service;


import hu.kalmancheysandor.applications.dominions.apis.server.common.entity.account.User;
import hu.kalmancheysandor.applications.dominions.apis.server.common.entity.account.UserPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.common.repository.account.UserPermissionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.common.repository.account.UserRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.common.security.SecurityUserDetails;
import hu.kalmancheysandor.applications.dominions.apis.server.common.utils.security.SecurityPermission;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;

    public CustomUserDetailsService(UserRepository userRepository, UserPermissionRepository userPermissionRepository) {
        this.userRepository = userRepository;
        this.userPermissionRepository = userPermissionRepository;
    }

    @Override
    public SecurityUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByIdentifier(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Load user permissions
        Set<SecurityPermission> authorities = new HashSet<>();

        // Adding some permissions which have to even when no enabled permission group is attached.
        authorities.add(new SecurityPermission("Main:VIEW"));
        authorities.add(new SecurityPermission("Account.Profile:VIEW"));
        authorities.add(new SecurityPermission("Account.Profile:ADD"));
        authorities.add(new SecurityPermission("Account.Profile:EDIT"));
        authorities.add(new SecurityPermission("Account.Profile:DELETE"));


        List<UserPermission> permissionList = userPermissionRepository.listAllAtUserId(user.getId());
        for (UserPermission permission : permissionList) {
            if (permission.isPermissionIsView()) {
                authorities.add(new SecurityPermission(permission.getPermissionTarget() + ":" + "VIEW"));
            }

            if (permission.isPermissionIsAdd()) {
                authorities.add(new SecurityPermission(permission.getPermissionTarget() + ":" + "ADD"));
            }

            if (permission.isPermissionIsEdit()) {
                authorities.add(new SecurityPermission(permission.getPermissionTarget() + ":" + "EDIT"));
            }

            if (permission.isPermissionIsDelete()) {
                authorities.add(new SecurityPermission(permission.getPermissionTarget() + ":" + "DELETE"));
            }
        }

        //
        SecurityUserDetails securityUserDetails = new SecurityUserDetails();

        securityUserDetails.setId(user.getId());
        securityUserDetails.setUsername(user.getIdentifier());
        securityUserDetails.setPassword(user.getPassword());
        securityUserDetails.setAuthorities(authorities);
        securityUserDetails.setEnabled(user.isEnabled());
        return securityUserDetails;
    }

    public boolean isEnabled(String username) {
        return userRepository.isEnabled(username);
    }



}
