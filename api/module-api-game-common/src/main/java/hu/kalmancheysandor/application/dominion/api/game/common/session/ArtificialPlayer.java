package hu.kalmancheysandor.application.dominion.api.game.common.session;

public class ArtificialPlayer extends PlayerData {
    private EngineType engineType;

    public ArtificialPlayer(int id, String name, EngineType engineType) {
        super(id, name, PlayerType.ARTIFICIAL);
        this.engineType = engineType;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public enum EngineType {
        OTTO,
        LIZ
    }
}
