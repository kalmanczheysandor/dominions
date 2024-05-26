package hu.kalmancheysandor.application.dominion.api.game.common.session;

public class HumanPlayer extends PlayerData{

    public HumanPlayer(int id,String name) {
        super(id, name,PlayerType.HUMAN);
    }
}
