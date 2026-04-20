package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.repository.history;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.history.HugoHistoryPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface HugoHistoryPlayerRepository extends JpaRepository<HugoHistoryPlayer, Integer> {
    public HugoHistoryPlayer findByUserUuid(String userUuid);

    public HugoHistoryPlayer findById(int id);

    public List<HugoHistoryPlayer> findAll();

    @Query("SELECT COUNT(t) FROM HugoHistoryPlayer t")
    int countAll();

    @Query("SELECT t FROM HugoHistoryPlayer t")
    List<HugoHistoryPlayer> listAll();

    @Query("SELECT t FROM HugoHistoryPlayer t")
    Stream<HugoHistoryPlayer> streamAll();

    public void deleteById(int id);

//    public boolean existsByUserUuid(String userUuid);

    public HugoHistoryPlayer save(HugoHistoryPlayer lizHistoryPlayer);
}
