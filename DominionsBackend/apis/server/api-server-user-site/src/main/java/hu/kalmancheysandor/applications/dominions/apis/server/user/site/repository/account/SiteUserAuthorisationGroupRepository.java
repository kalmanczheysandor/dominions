package hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account;


import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUserAuthorisationGroup;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteUserAuthorisationGroupRepository extends JpaRepository<SiteUserAuthorisationGroup, Integer>, UUIDRepository<SiteUserAuthorisationGroup,Integer> {
    public SiteUserAuthorisationGroup findById(int authorisationGroupId);
    public SiteUserAuthorisationGroup findByName(String authorisationGroupName);
    public List<SiteUserAuthorisationGroup> findAll();
    public List<SiteUserAuthorisationGroup> findAllByEnabled(boolean enabled);
    public void deleteById(int authorisationGroupId);
    public boolean existsById(int authorisationGroupId);
    public SiteUserAuthorisationGroup save(SiteUserAuthorisationGroup userGroup);

    @Query("SELECT CASE WHEN count(g) > 0 THEN true ELSE false END FROM SiteUserAuthorisationGroup g WHERE g.name = :paramName")
    public boolean isNameReserved(@Param("paramName") String name);

    @Query("SELECT CASE WHEN count(g) > 0 THEN true ELSE false END FROM SiteUserAuthorisationGroup g WHERE g.name = :paramName and g.id<> :paramExcludedId")
    public boolean isNameReserved(@Param("paramName") String name, @Param("paramExcludedId")  int excludedId);




    @Query("SELECT g.enabled FROM SiteUserAuthorisationGroup g WHERE g.id=:paramId")
    boolean isEnabled(@Param("paramId") int id);

    @Query("SELECT g FROM SiteUserAuthorisationGroup g WHERE g.enabled=true")
    List<SiteUserAuthorisationGroup> listAllEnabled();

    @Query("SELECT g FROM SiteUserAuthorisationGroup g WHERE g.id = :paramExcludedId OR g.enabled=true")
    List<SiteUserAuthorisationGroup> listAllEnabledWithOneExcludedId(@Param("paramExcludedId") int excludedId);

    @Query("SELECT g FROM SiteUserAuthorisationGroup g WHERE g.id IN :excludedIdList OR g.enabled=true")
    List<SiteUserAuthorisationGroup> listAllEnabledWithMultipleExcludedId(@Param("excludedIdList") List<Integer> excludedIdList);

    @Query("SELECT g FROM SiteUserAuthorisationGroup g WHERE g.uuid = :paramExcludedUuid OR g.enabled=true")
    List<SiteUserAuthorisationGroup> listAllEnabledWithOneExcludedUuid(@Param("paramExcludedUuid") String excludedUuid);

    @Query("SELECT g FROM SiteUserAuthorisationGroup g WHERE g.uuid IN :excludedUuidList OR g.enabled=true")
    List<SiteUserAuthorisationGroup> listAllEnabledWithMultipleExcludedUuid(@Param("excludedUuidList") List<String> excludedUuidList);









    @Query("SELECT CASE WHEN " +
        "EXISTS (SELECT 1 FROM SiteUserGroup g WHERE g.groupId = :paramGroupId) OR " +
        "EXISTS (SELECT 1 FROM SiteUserGroup g WHERE g.groupId = :paramGroupId) " +
        "THEN TRUE ELSE FALSE END")
    boolean isReferencedElsewhere(@Param("paramGroupId") int groupId);


}
