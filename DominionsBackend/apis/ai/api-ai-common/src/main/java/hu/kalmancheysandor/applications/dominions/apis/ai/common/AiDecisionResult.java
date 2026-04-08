package hu.kalmancheysandor.applications.dominions.apis.ai.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiDecisionResult {
    private Integer targetCellKey;
    private Integer troopSize;
}
