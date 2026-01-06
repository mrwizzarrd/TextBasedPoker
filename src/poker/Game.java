package poker;

import java.util.ArrayList;

public class Game {
    private static String version = "1.0.6";
    public static void main(String[] args) {

        while (true) {
            int gameStatus = GameIO.initScreen(version);

            if (gameStatus == 0) {
                return;
            }
            ArrayList<Player> players = GameIO.getPlayers();
            PokerGame game = new PokerGame(players);
            while (true) {
                game.resetGame();
                game.PreFlop();
                game.FlopRound();
                game.TurnRound();
                game.RiverRound();
                game.determineWinner();
                GameIO.outputAllHands(players, game.getBoard());

                GameIO.pressAnyKeyToContinue();

                if (!GameIO.playAgain()) {
                    break;
                } else{
                    game.resetGame();
                }
            }
        }
    }
}
