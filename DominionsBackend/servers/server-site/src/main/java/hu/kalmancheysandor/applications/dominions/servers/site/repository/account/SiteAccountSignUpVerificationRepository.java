package hu.kalmancheysandor.applications.dominions.servers.site.repository.account;


import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import hu.kalmancheysandor.applications.dominions.servers.site.entity.account.SiteAccountSignUpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface SiteAccountSignUpVerificationRepository extends JpaRepository<SiteAccountSignUpVerification, String>, UUIDRepository<SiteAccountSignUpVerification, String> {
    public SiteAccountSignUpVerification findByUuid(String siteAccountRecoveryVerificationUuid);
    public List<SiteAccountSignUpVerification> findAll();
    public void deleteByUuid(String siteAccountRecoveryVerificationUuid);
    public boolean existsByUuid(String siteAccountRecoveryVerificationUuid);
    public SiteAccountSignUpVerification save(SiteAccountSignUpVerification siteAccountSignUpVerification);

    @Query("SELECT t FROM SiteAccountSignUpVerification t")
    public List<SiteAccountSignUpVerification> listAll();
    
    @Modifying
    @Query("DELETE FROM SiteAccountSignUpVerification t WHERE t.dateExpiration <= :timeNow")
    public void deleteAllExpired(@Param("timeNow") LocalDateTime timeNow);

    @Query("SELECT t FROM SiteAccountSignUpVerification t WHERE t.dateExpiration <= :timeNow")
    public List<SiteAccountSignUpVerification> selectAllExpired(@Param("timeNow") LocalDateTime timeNow);


    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM SiteAccountSignUpVerification t WHERE t.userUuid = :userUuid")
    public boolean isUserUuidAlreadyUsed(@Param("userUuid") String userUuid);
}


