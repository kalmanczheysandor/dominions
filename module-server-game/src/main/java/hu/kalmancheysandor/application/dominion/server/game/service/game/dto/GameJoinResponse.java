package hu.kalmancheysandor.application.dominion.server.game.service.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameJoinResponse {
    private StatusCode statusCode;
    private String joinKey;
    public enum StatusCode {
        ACCEPTED,
        REJECTED
    }
}
