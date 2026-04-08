package hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class GameSessionStillRecruitingException extends CustomException {
    private String uuid;

    public GameSessionStillRecruitingException(String uuid) {
        super("GameSessionStillRecruiting");
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
