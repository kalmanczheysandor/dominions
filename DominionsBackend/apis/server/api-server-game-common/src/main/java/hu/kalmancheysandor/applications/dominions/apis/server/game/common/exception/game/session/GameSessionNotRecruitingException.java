package hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class GameSessionNotRecruitingException extends CustomException {
    private String uuid;

    public GameSessionNotRecruitingException(String uuid) {
        super("GameSessionNotRecruiting");
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
