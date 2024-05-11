package hu.kalmanczheysandor.application.dominion.ai.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AiResponse {
    private Integer cellKey;
    private Integer cellValue;
}
