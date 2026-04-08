package hu.kalmancheysandor.applications.dominions.servers.admin.repository.site;


import hu.kalmancheysandor.applications.dominions.servers.admin.entity.site.Site;
import hu.kalmancheysandor.applications.dominions.servers.admin.utils.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteRepository extends JpaRepository<Site, Integer>, UUIDRepository<Site, Integer> {
    public Site findById(int siteId);

    public Site findByUuid(String uuid);

    public List<Site> findAll();

    public List<Site> findByEnabled(boolean enabled);

    public void deleteById(int siteId);

    public boolean existsById(int siteId);

    public Site save(Site site);

    @Query("SELECT CASE WHEN count(b) > 0 THEN true ELSE false END FROM Site b WHERE b.name = :paramName")
    public boolean isNameReserved(@Param("paramName") String name);

    @Query("SELECT CASE WHEN count(b) > 0 THEN true ELSE false END FROM Site b WHERE b.name = :paramName and b.id<> :paramExcludedId")
    public boolean isNameReserved(@Param("paramName") String name, @Param("paramExcludedId") int excludedId);



    @Query("SELECT b.enabled FROM Site b WHERE b.id=:paramId")
    boolean isEnabled(@Param("paramId") int id);

    @Query("SELECT b FROM Site b WHERE b.enabled=true")
    List<Site> listAllEnabled();

    @Query("SELECT b FROM Site b WHERE b.id = :paramExcludedId OR b.enabled=true")
    List<Site> listAllEnabledWithOneExcludedId(@Param("paramExcludedId") int excludedId);

    @Query("SELECT b FROM Site b WHERE b.id IN :excludedIdList OR b.enabled=true")
    List<Site> listAllEnabledWithMultipleExcludedId(@Param("excludedIdList") List<Integer> excludedIdList);

    @Query("SELECT b FROM Site b WHERE b.uuid = :paramExcludedUuid OR b.enabled=true")
    List<Site> listAllEnabledWithOneExcludedUuid(@Param("paramExcludedUuid") String excludedUuid);

    @Query("SELECT b FROM Site b WHERE b.id IN :excludedUuidList OR b.enabled=true")
    List<Site> listAllEnabledWithMultipleExcludedUuid(@Param("excludedUuidList") List<String> excludedUuidList);

//    @Query("SELECT FALSE")
    @Query("SELECT CASE WHEN " +
        "EXISTS (SELECT 1 FROM Dog d WHERE d.site.id = :paramSiteId) " +
        "THEN TRUE ELSE FALSE END")
    boolean isReferencedElsewhere(@Param("paramSiteId") int siteId);
}
