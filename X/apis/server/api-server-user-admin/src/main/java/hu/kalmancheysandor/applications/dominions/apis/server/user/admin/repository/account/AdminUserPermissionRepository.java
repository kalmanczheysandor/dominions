package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.repository.account;


import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.entity.account.AdminUserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface AdminUserPermissionRepository extends JpaRepository<AdminUserPermission, AdminUserPermission.PrimaryKey> {

    @Query("SELECT p FROM AdminUserPermission p ")
    List<AdminUserPermission> findAll();

    @Query("SELECT p FROM AdminUserPermission p WHERE p.userId = :userId ")
    List<AdminUserPermission> listAllAtUserId(@Param("userId") int userId);
}
