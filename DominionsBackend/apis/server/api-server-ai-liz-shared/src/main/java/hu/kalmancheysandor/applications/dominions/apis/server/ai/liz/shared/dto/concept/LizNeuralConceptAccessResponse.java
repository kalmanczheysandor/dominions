package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LizNeuralConceptAccessResponse {
    private String uuid;
    private String name;
    private int confMaxIterationsPerTurn;
    private int confMaxTurn;
    private BigDecimal confLearningRate;

    private boolean enabled;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateModified;
}
