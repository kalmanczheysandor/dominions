package hu.kalmancheysandor.application.dominion.server.game.service.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameStepRequest {
    private Integer gameId;
    private Integer playerId;
    private String value;
}
