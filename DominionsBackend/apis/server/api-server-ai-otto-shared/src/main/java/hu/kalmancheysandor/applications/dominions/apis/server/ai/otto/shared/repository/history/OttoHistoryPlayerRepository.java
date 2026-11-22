package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.history;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.history.OttoHistoryPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface OttoHistoryPlayerRepository extends JpaRepository<OttoHistoryPlayer, Integer> {
    public OttoHistoryPlayer findByUserUuid(String userUuid);

    public OttoHistoryPlayer findById(int id);

    public List<OttoHistoryPlayer> findAll();

    @Query("SELECT COUNT(t) FROM OttoHistoryPlayer t")
    int countAll();

    @Query("SELECT t FROM OttoHistoryPlayer t")
    List<OttoHistoryPlayer> listAll();

    @Query("SELECT t FROM OttoHistoryPlayer t")
    Stream<OttoHistoryPlayer> streamAll();

    public void deleteById(int id);

    public OttoHistoryPlayer save(OttoHistoryPlayer historyPlayer);
}
