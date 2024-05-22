package hu.kalmancheysandor.application.dominion.server.game.service.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStateResponse {
    private int turn;
    private int pendingCount;
    private int playerCount;
}
