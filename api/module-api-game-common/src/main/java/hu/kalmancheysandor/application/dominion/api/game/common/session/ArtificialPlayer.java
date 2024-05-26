package hu.kalmancheysandor.application.dominion.api.game.common.session;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiEngine;
import lombok.AllArgsConstructor;
import lombok.Data;

public class ArtificialPlayer extends PlayerData {

    public ArtificialPlayer(int id, String name) {
        super(id, name, PlayerType.ARTIFICIAL);
    }

}
