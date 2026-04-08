package hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account;


import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUserAuthorisationGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteUserAuthorisationGroupPermissionRepository extends JpaRepository<SiteUserAuthorisationGroupPermission, SiteUserAuthorisationGroupPermission.PrimaryKey> {
    public SiteUserAuthorisationGroupPermission findByGroupIdAndTarget(int authorisationGroupId, String target);
    public SiteUserAuthorisationGroupPermission findByGroupId(int authorisationGroupId);

    public List<SiteUserAuthorisationGroupPermission> findAll();
    public void deleteByGroupIdAndTarget(int authorisationGroupId, String target);
    public boolean existsByGroupIdAndTarget(int authorisationGroupId, String target);
    public SiteUserAuthorisationGroupPermission save(SiteUserAuthorisationGroupPermission permission);


    @Modifying
    @Query("DELETE FROM SiteUserAuthorisationGroupPermission p WHERE p.groupId=:paramGroupId")
    public void deleteAllPermissionsOfPermissionGroup(@Param("paramGroupId") int groupId);



}
