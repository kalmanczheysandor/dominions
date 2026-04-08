package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.option;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LizNeuralConceptResultHistoryPlayerOptionResponse {
    private String uuid;
    private String caption;
    private long snapshotCount;

}
