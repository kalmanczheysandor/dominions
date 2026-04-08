package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account;


import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUserAuthorisationGroup;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminUserAuthorisationGroupRepository extends JpaRepository<AdminUserAuthorisationGroup, Integer>, UUIDRepository<AdminUserAuthorisationGroup,Integer> {
    public AdminUserAuthorisationGroup findById(int authorisationGroupId);
    public AdminUserAuthorisationGroup findByName(String authorisationGroupName);
    public List<AdminUserAuthorisationGroup> findAll();
    public List<AdminUserAuthorisationGroup> findAllByEnabled(boolean enabled);
    public void deleteById(int authorisationGroupId);
    public boolean existsById(int authorisationGroupId);
    public AdminUserAuthorisationGroup save(AdminUserAuthorisationGroup userGroup);

    @Query("SELECT CASE WHEN count(g) > 0 THEN true ELSE false END FROM AdminUserAuthorisationGroup g WHERE g.name = :paramName")
    public boolean isNameReserved(@Param("paramName") String name);

    @Query("SELECT CASE WHEN count(g) > 0 THEN true ELSE false END FROM AdminUserAuthorisationGroup g WHERE g.name = :paramName and g.id<> :paramExcludedId")
    public boolean isNameReserved(@Param("paramName") String name, @Param("paramExcludedId")  int excludedId);


    @Query("SELECT g.enabled FROM AdminUserAuthorisationGroup g WHERE g.id=:paramId")
    boolean isEnabled(@Param("paramId") int id);

    @Query("SELECT g FROM AdminUserAuthorisationGroup g WHERE g.enabled=true")
    List<AdminUserAuthorisationGroup> listAllEnabled();

    @Query("SELECT g FROM AdminUserAuthorisationGroup g WHERE g.id = :paramExcludedId OR g.enabled=true")
    List<AdminUserAuthorisationGroup> listAllEnabledWithOneExcludedId(@Param("paramExcludedId") int excludedId);

    @Query("SELECT g FROM AdminUserAuthorisationGroup g WHERE g.id IN :excludedIdList OR g.enabled=true")
    List<AdminUserAuthorisationGroup> listAllEnabledWithMultipleExcludedId(@Param("excludedIdList") List<Integer> excludedIdList);

    @Query("SELECT g FROM AdminUserAuthorisationGroup g WHERE g.uuid = :paramExcludedUuid OR g.enabled=true")
    List<AdminUserAuthorisationGroup> listAllEnabledWithOneExcludedUuid(@Param("paramExcludedUuid") String excludedUuid);

    @Query("SELECT g FROM AdminUserAuthorisationGroup g WHERE g.uuid IN :excludedUuidList OR g.enabled=true")
    List<AdminUserAuthorisationGroup> listAllEnabledWithMultipleExcludedUuid(@Param("excludedUuidList") List<String> excludedUuidList);









    @Query("SELECT CASE WHEN " +
        "EXISTS (SELECT 1 FROM AdminUserGroup g WHERE g.groupId = :paramGroupId) OR " +
        "EXISTS (SELECT 1 FROM AdminUserGroup g WHERE g.groupId = :paramGroupId) " +
        "THEN TRUE ELSE FALSE END")
    boolean isReferencedElsewhere(@Param("paramGroupId") int groupId);


}
