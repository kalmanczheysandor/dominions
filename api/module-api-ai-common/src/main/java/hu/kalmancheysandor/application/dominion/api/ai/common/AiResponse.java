package hu.kalmancheysandor.application.dominion.api.ai.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiResponse {
    private Integer targetCellKey;
    private Integer troopSize;
}
