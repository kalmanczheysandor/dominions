package hu.kalmancheysandor.application.dominion.server.game.service.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStateResponse {
    private int currentTurn;
    private int pendingCount;
    private int playerCount;
    private StatusCode statusCode;


    public enum StatusCode {
        RECRUITING,
        PLAYING,
        ENDED
    }
}
