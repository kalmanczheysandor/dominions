package hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiDecisionResponse {
    private Integer targetCellKey;
    private Integer troopSize;
}
