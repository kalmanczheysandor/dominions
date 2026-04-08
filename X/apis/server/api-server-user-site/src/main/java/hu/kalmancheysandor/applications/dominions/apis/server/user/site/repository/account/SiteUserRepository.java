package hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account;


import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUser;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteUserRepository extends JpaRepository<SiteUser, Integer>, UUIDRepository<SiteUser,Integer> {
    public SiteUser findByIdentifier(String username);
    public SiteUser findById(int userId);
    public SiteUser findByUuid(String uuid);

    public List<SiteUser> findAll();
    public void deleteById(int userId);
    public boolean existsById(int userId);
    public SiteUser save(SiteUser siteUser);

    @Query("SELECT u.enabled FROM SiteUser u WHERE u.identifier = :paramIdentifier")
    public boolean isEnabled(@Param("paramIdentifier") String identifier);

    @Query("SELECT CASE WHEN count(u) > 0 THEN true ELSE false END FROM SiteUser u WHERE u.identifier = :paramIdentifier")
    public boolean isIdentifierReserved(@Param("paramIdentifier") String identifier);

    @Query("SELECT CASE WHEN count(u) > 0 THEN true ELSE false END FROM SiteUser u WHERE u.identifier = :paramIdentifier and u.id<> :paramExcludedId")
    public boolean isIdentifierReserved(@Param("paramIdentifier") String identifier, @Param("paramExcludedId")  int excludedId);


    @Query("SELECT FALSE")
    boolean isReferencedElsewhere(@Param("paramUserId") int userId);

}
