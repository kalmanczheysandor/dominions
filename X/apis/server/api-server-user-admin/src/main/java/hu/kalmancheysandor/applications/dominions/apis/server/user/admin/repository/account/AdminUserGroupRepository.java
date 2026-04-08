package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account;


import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminUserGroupRepository extends JpaRepository<AdminUserGroup, AdminUserGroup.PrimaryKey> {
    public AdminUserGroup findByGroupIdAndUserId(int groupId, int userId);
    public List<AdminUserGroup> findAll();
    public List<AdminUserGroup> findAllByUserId(int userId);

    public void deleteByGroupIdAndUserId(int groupId,int userId);
    public boolean existsByGroupIdAndUserId(int groupId,int userId);
    public AdminUserGroup save(AdminUserGroup siteUserGroup);

    @Modifying
    @Query("DELETE FROM AdminUserGroup g WHERE g.userId=:paramUserId")
    public void deleteAllGroupsOfUser(@Param("paramUserId") int userId);

    @Modifying
    @Query("DELETE FROM AdminUserGroup g WHERE g.userId=:paramUserId and g.groupId IN (SELECT a.id FROM AdminUserAuthorisationGroup a WHERE a.enabled = true)")
    public void deleteAllEnabledGroupsOfUser(@Param("paramUserId") int userId);



}
