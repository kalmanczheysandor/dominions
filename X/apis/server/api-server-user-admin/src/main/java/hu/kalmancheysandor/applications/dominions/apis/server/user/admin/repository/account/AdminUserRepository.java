package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account;


import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUser;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Integer>, UUIDRepository<AdminUser,Integer> {
    public AdminUser findByIdentifier(String username);
    public AdminUser findById(int userId);
    public AdminUser findByUuid(String uuid);

    public List<AdminUser> findAll();
    public void deleteById(int userId);
    public boolean existsById(int userId);
    public AdminUser save(AdminUser adminUser);

    @Query("SELECT u.enabled FROM AdminUser u WHERE u.identifier = :paramIdentifier")
    public boolean isEnabled(@Param("paramIdentifier") String identifier);

    @Query("SELECT CASE WHEN count(u) > 0 THEN true ELSE false END FROM AdminUser u WHERE u.identifier = :paramIdentifier")
    public boolean isIdentifierReserved(@Param("paramIdentifier") String identifier);

    @Query("SELECT CASE WHEN count(u) > 0 THEN true ELSE false END FROM AdminUser u WHERE u.identifier = :paramIdentifier and u.id<> :paramExcludedId")
    public boolean isIdentifierReserved(@Param("paramIdentifier") String identifier, @Param("paramExcludedId")  int excludedId);


    @Query("SELECT FALSE")
    boolean isReferencedElsewhere(@Param("paramUserId") int userId);

}
