package hu.kalmancheysandor.application.dominion.api.game.common.session;

public class ArtificialPlayer extends PlayerData {

    public ArtificialPlayer(int id, String name) {
        super(id, name, PlayerType.ARTIFICIAL);
    }

}
