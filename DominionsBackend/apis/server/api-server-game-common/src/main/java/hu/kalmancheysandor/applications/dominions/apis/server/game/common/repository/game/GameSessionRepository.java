package hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.game;


import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.game.GameSession;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, String>, UUIDRepository<GameSession, String> {
    public GameSession findByUuid(String gameSessionUuid);
    public List<GameSession> findAll();
    public void deleteByUuid(String gameSessionUuid);
    public boolean existsByUuid(String gameSessionUuid);
    public GameSession save(GameSession gameSession);


    @Query("SELECT t FROM GameSession t WHERE t.enabled=true")
    List<GameSession> listAllEnabled();

    @Query("SELECT t FROM GameSession t WHERE t.recruiting=true and t.enabled=true ORDER BY t.freeSlotCount DESC")
    List<GameSession> listAllRecruiting();

    @Query("SELECT t FROM GameSession t WHERE t.visible=true and t.enabled=true")
    List<GameSession> listAllVisible();

    @Query("SELECT t FROM GameSession t")
    List<GameSession> listAll();

    @Query("SELECT t.recruiting FROM GameSession t WHERE t.uuid=:uuid")
    boolean isRecruiting(@Param("uuid") String uuid);

    @Modifying
    @Query("DELETE FROM GameSession t WHERE t.dateExpiration <= :timeNow")
    void deleteAllExpired(@Param("timeNow") LocalDateTime timeNow);
}


