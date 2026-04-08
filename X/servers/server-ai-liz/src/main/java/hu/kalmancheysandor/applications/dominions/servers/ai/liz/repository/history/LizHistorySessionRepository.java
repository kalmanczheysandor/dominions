package hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.history;

import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.history.LizHistorySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LizHistorySessionRepository extends JpaRepository<LizHistorySession, Integer> {
    public LizHistorySession findBySessionUuid(String sessionUuid);

    public LizHistorySession findById(int id);

    public List<LizHistorySession> findAll();

    public void deleteById(int id);

    public boolean existsById(int id);

    public LizHistorySession save(LizHistorySession lizHistorySession);
}
