package hu.kalmancheysandor.application.dominion.api.ai.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AiResponse {
    private Integer targetCellKey;
    private Integer troopSize;
}
