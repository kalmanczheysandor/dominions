package hu.kalmancheysandor.applications.dominions.servers.site.repository.account;


import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import hu.kalmancheysandor.applications.dominions.servers.site.entity.account.SiteAccountRecoveryVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface SiteAccountRecoveryVerificationRepository extends JpaRepository<SiteAccountRecoveryVerification, String>, UUIDRepository<SiteAccountRecoveryVerification, String> {
    public SiteAccountRecoveryVerification findByUuid(String siteAccountRecoveryVerificationUuid);
    public List<SiteAccountRecoveryVerification> findAll();
    public void deleteByUuid(String siteAccountRecoveryVerificationUuid);
    public boolean existsByUuid(String siteAccountRecoveryVerificationUuid);
    public SiteAccountRecoveryVerification save(SiteAccountRecoveryVerification siteAccountRecoveryVerification);

    public SiteAccountRecoveryVerification findByUserUuid(String userUuid);


    @Query("SELECT t FROM SiteAccountRecoveryVerification t")
    public List<SiteAccountRecoveryVerification> listAll();
    
    @Modifying
    @Query("DELETE FROM SiteAccountRecoveryVerification t WHERE t.dateExpiration <= :timeNow")
    public void deleteAllExpired(@Param("timeNow") LocalDateTime timeNow);

    @Query("SELECT t FROM SiteAccountRecoveryVerification t WHERE t.dateExpiration <= :timeNow")
    public List<SiteAccountRecoveryVerification> selectAllExpired(@Param("timeNow") LocalDateTime timeNow);

    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM SiteAccountRecoveryVerification t WHERE t.userUuid = :userUuid")
    public boolean isUserUuidAlreadyUsed(@Param("userUuid") String userUuid);
}


