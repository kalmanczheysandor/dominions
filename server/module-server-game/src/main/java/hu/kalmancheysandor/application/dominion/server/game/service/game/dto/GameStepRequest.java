package hu.kalmancheysandor.application.dominion.server.game.service.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStepRequest {
    private Integer targetCellKey;
    private Integer attackingTroopSize;
}
