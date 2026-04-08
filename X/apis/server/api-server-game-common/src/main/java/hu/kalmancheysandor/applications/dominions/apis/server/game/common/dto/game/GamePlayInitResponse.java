package hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game;

import hu.kalmancheysandor.applications.dominions.apis.game.common.map.configuration.GameMapConfiguration;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.state.GameMapState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayInitResponse {
    private GameMapState gameMapState;
    private GameMapConfiguration gameMapConfiguration;
    private int maxHumanPlayerCount;
    private int currentHumanPlayerCount;
}
