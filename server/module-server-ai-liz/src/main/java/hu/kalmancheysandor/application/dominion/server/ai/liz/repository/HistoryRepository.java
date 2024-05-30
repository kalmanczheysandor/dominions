package hu.kalmancheysandor.application.dominion.server.ai.liz.repository;


import hu.kalmancheysandor.application.dominion.server.ai.liz.repository.domain.HistoryRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryRow, Long> {
    //    public History findByIdentifier(String username);
    //    public History findById(long userId);
    public List<HistoryRow> findAll();

    //    public void deleteById(long userId);
    //    public boolean existsById(long userId);
    public HistoryRow save(HistoryRow history);
}
