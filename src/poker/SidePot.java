package poker;

import java.util.*;


/**
 * The object SidePot is used when the betting round requires a Side Pot for all in bets
 * Contains properties: amount (The pot amount) and eligible players
**/

public class SidePot {
    private int amount;
    private final List<Player> eligiblePlayers;

    public SidePot(){
        this.amount = 0;
        this.eligiblePlayers = new ArrayList<>();
    }

    public void addAmount(int amount){
        this.amount += amount;
    }

    public void addPlayer(Player p){
        this.eligiblePlayers.add(p);
    }

    public List<Player> getEligiblePlayers() {
        return Collections.unmodifiableList(this.eligiblePlayers);
    }

    public int getAmount(){
        return this.amount;
    }
}
