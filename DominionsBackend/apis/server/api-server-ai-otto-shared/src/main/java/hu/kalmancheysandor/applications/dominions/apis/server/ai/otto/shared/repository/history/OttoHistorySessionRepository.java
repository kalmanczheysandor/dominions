package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.history;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.history.OttoHistorySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OttoHistorySessionRepository extends JpaRepository<OttoHistorySession, Integer> {
    public OttoHistorySession findBySessionUuid(String sessionUuid);

    public OttoHistorySession findById(int id);

    public List<OttoHistorySession> findAll();

    public void deleteById(int id);

    public boolean existsById(int id);

    public OttoHistorySession save(OttoHistorySession historySession);
}
