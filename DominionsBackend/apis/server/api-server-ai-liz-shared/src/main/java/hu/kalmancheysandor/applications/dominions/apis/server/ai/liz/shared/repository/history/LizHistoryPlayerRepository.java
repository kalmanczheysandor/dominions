package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface LizHistoryPlayerRepository extends JpaRepository<LizHistoryPlayer, Integer> {
    public LizHistoryPlayer findByUserUuid(String userUuid);

    public LizHistoryPlayer findById(int id);

    public List<LizHistoryPlayer> findAll();

    @Query("SELECT COUNT(t) FROM LizHistoryPlayer t")
    int countAll();

    @Query("SELECT t FROM LizHistoryPlayer t")
    List<LizHistoryPlayer> listAll();

    @Query("SELECT t FROM LizHistoryPlayer t")
    Stream<LizHistoryPlayer> streamAll();

    public void deleteById(int id);

//    public boolean existsByUserUuid(String userUuid);

    public LizHistoryPlayer save(LizHistoryPlayer lizHistoryPlayer);
}
