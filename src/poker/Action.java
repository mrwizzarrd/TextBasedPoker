package poker;

public class Action {
    private final Player actor;
    private final PlayerAction action;
    private final int betAmount;

    public Action(Player pl, PlayerAction act, int betAmount){
        this.actor = pl;
        this.action = act;
        this.betAmount = betAmount;
    }

    //==========Getters/Setters=============

    public int getBetAmount(){
        return this.betAmount;
    }

    public PlayerAction getAction(){
        return this.action;
    }

    public Player getActor(){
        return this.actor;
    }
}
