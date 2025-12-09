package poker;

import java.util.ArrayList;

public class Testing {
    public static void main(String[] args) {
        int gameStatus = GameIO.initScreen();

        if(gameStatus == 0){
            return;
        }
        ArrayList<Player> players = GameIO.getPlayers();
        PokerGame game = new PokerGame(players);
        while(true){
            game.resetGame();
            game.PreFlop();
            game.FlopRound();
            game.TurnRound();
            game.RiverRound();
            game.determineWinner();
            GameIO.outputAllHands(players, game.getBoard());

            GameIO.pressAnyKeyToContinue();

            if(!GameIO.playAgain()){
                break;
            }
        }
    }
}
