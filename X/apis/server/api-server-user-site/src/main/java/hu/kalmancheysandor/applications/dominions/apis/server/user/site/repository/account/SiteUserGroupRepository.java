package hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account;

import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteUserGroupRepository extends JpaRepository<SiteUserGroup, SiteUserGroup.PrimaryKey> {
    public SiteUserGroup findByGroupIdAndUserId(int groupId, int userId);
    public List<SiteUserGroup> findAll();
    public List<SiteUserGroup> findAllByUserId(int userId);

    public void deleteByGroupIdAndUserId(int groupId,int userId);
    public boolean existsByGroupIdAndUserId(int groupId,int userId);
    public SiteUserGroup save(SiteUserGroup siteUserGroup);

    @Modifying
    @Query("DELETE FROM SiteUserGroup g WHERE g.userId=:paramUserId")
    public void deleteAllGroupsOfUser(@Param("paramUserId") int userId);

    @Modifying
    @Query("DELETE FROM SiteUserGroup g WHERE g.userId=:paramUserId and g.groupId IN (SELECT a.id FROM SiteUserAuthorisationGroup a WHERE a.enabled = true)")
    public void deleteAllEnabledGroupsOfUser(@Param("paramUserId") int userId);



}
