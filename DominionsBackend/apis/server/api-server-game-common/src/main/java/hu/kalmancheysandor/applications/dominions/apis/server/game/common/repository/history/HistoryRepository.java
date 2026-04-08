package hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.history;

import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.history.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    //    public History findByIdentifier(String username);
    //    public History findById(long userId);
    public List<History> findAll();

    //    public void deleteById(long userId);
    //    public boolean existsById(long userId);
    public History save(History history);
}
