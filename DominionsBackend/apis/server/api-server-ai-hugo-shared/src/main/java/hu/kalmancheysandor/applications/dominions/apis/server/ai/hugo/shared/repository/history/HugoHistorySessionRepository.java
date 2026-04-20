package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.repository.history;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.history.HugoHistorySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HugoHistorySessionRepository extends JpaRepository<HugoHistorySession, Integer> {
    public HugoHistorySession findBySessionUuid(String sessionUuid);

    public HugoHistorySession findById(int id);

    public List<HugoHistorySession> findAll();

    public void deleteById(int id);

    public boolean existsById(int id);

    public HugoHistorySession save(HugoHistorySession hugoHistorySession);
}
