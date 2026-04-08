package hu.kalmancheysandor.applications.dominions.servers.game.queue;


import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.PlayState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryQueueItem {
    private PlayState playState;
}
