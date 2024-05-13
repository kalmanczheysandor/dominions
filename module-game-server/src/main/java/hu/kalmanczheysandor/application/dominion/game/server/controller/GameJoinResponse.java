package hu.kalmanczheysandor.application.dominion.game.server.controller;

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
