package hu.kalmancheysandor.application.dominion.server.game.repository.game;

import hu.kalmancheysandor.application.dominion.server.game.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HistoryRepository extends JpaRepository<History, Long> {
//    public History findByIdentifier(String username);
//    public History findById(long userId);
//    public List<History> findAll();
//    public void deleteById(long userId);
//    public boolean existsById(long userId);
    public History save(History history);
}
