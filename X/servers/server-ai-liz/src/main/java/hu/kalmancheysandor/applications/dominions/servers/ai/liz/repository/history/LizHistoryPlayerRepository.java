package hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.history;

import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.history.LizHistoryPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LizHistoryPlayerRepository extends JpaRepository<LizHistoryPlayer, Integer> {
    public LizHistoryPlayer findByUserUuid(String userUuid);

    public LizHistoryPlayer findById(int id);

    public List<LizHistoryPlayer> findAll();

    public void deleteById(int id);

//    public boolean existsByUserUuid(String userUuid);

    public LizHistoryPlayer save(LizHistoryPlayer lizHistoryPlayer);
}
