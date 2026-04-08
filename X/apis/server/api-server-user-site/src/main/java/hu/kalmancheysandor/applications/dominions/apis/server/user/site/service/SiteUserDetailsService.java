package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service;


import hu.kalmancheysandor.applications.dominions.apis.server.user.site.SitePermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUser;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUserPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account.SiteUserPermissionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account.SiteUserRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.security.SiteUserSecurityDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SiteUserDetailsService implements UserDetailsService {

    private final SiteUserRepository siteUserRepository;
    private final SiteUserPermissionRepository siteUserPermissionRepository;

    public SiteUserDetailsService(SiteUserRepository siteUserRepository, SiteUserPermissionRepository siteUserPermissionRepository) {
        this.siteUserRepository = siteUserRepository;
        this.siteUserPermissionRepository = siteUserPermissionRepository;
    }

    @Override
    public SiteUserSecurityDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SiteUser siteUser = siteUserRepository.findByIdentifier(username);
        if (siteUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Load user permissions
        Set<SitePermission> authorities = new HashSet<>();

        // Adding some permissions which have to even when no enabled permission group is attached.
        authorities.add(new SitePermission("Main:VIEW"));
        authorities.add(new SitePermission("Account.Profile:VIEW"));
        authorities.add(new SitePermission("Account.Profile:ADD"));
        authorities.add(new SitePermission("Account.Profile:EDIT"));
        authorities.add(new SitePermission("Account.Profile:DELETE"));


        List<SiteUserPermission> permissionList = siteUserPermissionRepository.listAllAtUserId(siteUser.getId());
        for (SiteUserPermission permission : permissionList) {
            if (permission.isPermissionIsView()) {
                authorities.add(new SitePermission(permission.getPermissionTarget() + ":" + "VIEW"));
            }

            if (permission.isPermissionIsAdd()) {
                authorities.add(new SitePermission(permission.getPermissionTarget() + ":" + "ADD"));
            }

            if (permission.isPermissionIsEdit()) {
                authorities.add(new SitePermission(permission.getPermissionTarget() + ":" + "EDIT"));
            }

            if (permission.isPermissionIsDelete()) {
                authorities.add(new SitePermission(permission.getPermissionTarget() + ":" + "DELETE"));
            }
        }

        //
        SiteUserSecurityDetails securityUserDetails = new SiteUserSecurityDetails();

        securityUserDetails.setId(siteUser.getId());
        securityUserDetails.setUsername(siteUser.getIdentifier());
        securityUserDetails.setPassword(siteUser.getPassword());
        securityUserDetails.setAuthorities(authorities);
        securityUserDetails.setEnabled(siteUser.isEnabled());
        return securityUserDetails;
    }

    public boolean isEnabled(String username) {
        return siteUserRepository.isEnabled(username);
    }

    public SiteUser findUserById(int userId) {
        return siteUserRepository.findById(userId);
    }


}
