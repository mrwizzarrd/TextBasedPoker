package poker;

import java.util.ArrayList;

public class Testing {
    public static void main(String[] args) {
    
        Player Eli = new Player("Eli", 250);
        Player Stanley = new Player("Stanley", 100000);
        Player Max = new Player("Max", 50);

        ArrayList<Player> GamePlayers = new ArrayList<>();
        GamePlayers.add(Stanley);
        GamePlayers.add(Eli);
        GamePlayers.add(Max);

        PokerGame game = new PokerGame(GamePlayers);

        System.out.println("Preflop");

        game.PreFlop();

        for(Player p : GamePlayers){
            System.out.println(p.toString());
        }

    }
}
