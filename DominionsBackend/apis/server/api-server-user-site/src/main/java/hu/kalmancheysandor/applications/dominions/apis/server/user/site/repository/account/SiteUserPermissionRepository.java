package hu.kalmancheysandor.applications.dominions.apis.server.user.site.repository.account;

import hu.kalmancheysandor.applications.dominions.apis.server.user.site.entity.account.SiteUserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface SiteUserPermissionRepository extends JpaRepository<SiteUserPermission, SiteUserPermission.PrimaryKey> {

    @Query("SELECT p FROM SiteUserPermission p ")
    List<SiteUserPermission> findAll();

    @Query("SELECT p FROM SiteUserPermission p WHERE p.userId = :userId ")
    List<SiteUserPermission> listAllAtUserId(@Param("userId") int userId);
}
