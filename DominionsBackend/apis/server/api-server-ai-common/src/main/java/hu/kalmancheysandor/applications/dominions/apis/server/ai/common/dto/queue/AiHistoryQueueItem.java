package hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.queue;


import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.PlayState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiHistoryQueueItem {
    private PlayState playState;
    private String sessionUuid;
    private int scenarioId;
}
