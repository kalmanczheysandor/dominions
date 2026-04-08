package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account;

import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUserAuthorisationGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminUserAuthorisationGroupPermissionRepository extends JpaRepository<AdminUserAuthorisationGroupPermission, AdminUserAuthorisationGroupPermission.PrimaryKey> {
    public AdminUserAuthorisationGroupPermission findByGroupIdAndTarget(int authorisationGroupId, String target);
    public AdminUserAuthorisationGroupPermission findByGroupId(int authorisationGroupId);

    public List<AdminUserAuthorisationGroupPermission> findAll();
    public void deleteByGroupIdAndTarget(int authorisationGroupId, String target);
    public boolean existsByGroupIdAndTarget(int authorisationGroupId, String target);
    public AdminUserAuthorisationGroupPermission save(AdminUserAuthorisationGroupPermission permission);


    @Modifying
    @Query("DELETE FROM AdminUserAuthorisationGroupPermission p WHERE p.groupId=:paramGroupId")
    public void deleteAllPermissionsOfPermissionGroup(@Param("paramGroupId") int groupId);



}
