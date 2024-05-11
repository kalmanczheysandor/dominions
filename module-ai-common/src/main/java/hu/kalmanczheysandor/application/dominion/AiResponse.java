package hu.kalmanczheysandor.application.dominion;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AiResponse {
    private Integer cellKey;
    private Integer cellValue;
}
