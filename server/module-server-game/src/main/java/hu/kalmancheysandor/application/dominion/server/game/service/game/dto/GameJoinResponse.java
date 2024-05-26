package hu.kalmancheysandor.application.dominion.server.game.service.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameJoinResponse {
    private Integer playerIndex;
    private String secretKey;

}
