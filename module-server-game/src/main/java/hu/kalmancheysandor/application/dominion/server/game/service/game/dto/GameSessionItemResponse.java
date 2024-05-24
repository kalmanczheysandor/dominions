package hu.kalmancheysandor.application.dominion.server.game.service.game.dto;

import hu.kalmancheysandor.application.dominion.api.game.common.session.SessionStatusCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameSessionItemResponse {
    private String sessionKey;
    private String title;
    private String mapName;
    private SessionStatusCode status;
}
