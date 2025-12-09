package poker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;


public class GameIO {

    private static Scanner sc = new Scanner(System.in);

    //=============================================================================
    //=========================Helpers=============================================
    //=============================================================================


    private static void clearScreen(){
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    private static String promptInput(String inputMessage){
        System.out.printf("%s\n>", inputMessage);
        String userInput = sc.nextLine();
        return userInput;
    }

    private static void bar(){
        System.out.println("=============================");
    }

    public static void pressAnyKeyToContinue() {
        System.out.println("[Press Enter To Continue...]");
        sc.nextLine();
    }





    //=============================================================================
    //=========================Screens=============================================
    //=============================================================================


    /**
     * Return Codes:
     * 1- Start Game
     * 0- Exit Game
     **/
    public static int initScreen(){
        clearScreen();
        System.out.println("================Welcome To Poker====================");
        int statusCode = -256;
        while (true) {
            System.out.println("    ======Options======");
            System.out.println("    A: Start Game");
            System.out.println("    B: Quit");
            System.out.print(">");

            String input = sc.nextLine();

            switch (input.toUpperCase()) {
                case "A":
                    statusCode = 1;
                    break;
                
                case "B":
                    statusCode = 0;
                    break;
                default:
                    System.out.println("    Invalid Input!");
                    continue;
            }
            return statusCode;
        }
    }

    public static ArrayList<Player> getPlayers(){
        clearScreen();
        ArrayList<Player> players = new ArrayList<>();

        int playerAmount = 0;
        while (true) {
            try{
                playerAmount = Integer.parseInt(promptInput("How many players would you like in this game?"));
                if(playerAmount != -256 && playerAmount < 2){
                    System.out.println("Not Enough Players");
                    continue;
                }

            } catch(NumberFormatException e){
                System.out.println("Invalid Input- Must Be Integer");
                continue;
            }
            System.out.printf("%d Players, Is This Correct? [Y/N] (Any other input will be taken as N)", playerAmount);
            String conf = promptInput("");

            if(conf.equals("Y") || conf.equals("y")){
                break;
            }
        }
        if(playerAmount == -256){ //DEBUG MODE I'M TIRED OF MANUALLY ADDING EACH PLAYER
            System.out.println("++++++++++++++++++++++++++++++++++DEBUG MODE ENTERED++++++++++++++++++++++++++++++");
            players.add(new Player("Roland", 10000));
            players.add(new Player("Stanley", 10000));
            players.add(new Player("Eli", 10000));
            players.add(new Player("Max", 10000));
        }
        for(int i = 0; i < playerAmount; i++){
            while (true) {
                clearScreen();
                System.out.printf("========Player #%d=========\n", i + 1);
                String name;
                int chips;

                name = promptInput("Enter Player Name");
                try{
                    chips = Integer.parseInt(promptInput("Starting Chips"));
                } catch(NumberFormatException e){
                    System.out.println("Error: Input value must be an integer, try again");
                    continue;
                }

                String conf;

                System.out.printf("    Name: %s\n    Starting Chips: %d\n\nIs This Correct [Y/N] (Any other input will be taken as N)\n", name, chips);
                conf = promptInput("");
                if( conf.equals("Y")||  conf.equals("y")){
                    players.add(new Player(name, chips));
                    break;
                }
            }
        }
        return players;
    }
    
    public static void displayBoard(CommunityHand board, Pot pot, String round){
        clearScreen();
        System.out.println("========== CURRENT  BOARD ==========");
        bar();
        System.out.printf("Round: %s\nChips in Pot: %d\n", round, pot.getPot());
        bar();

        System.out.println("Cards:");
        if(board.cards.isEmpty()){
            System.out.println("[No Cards]");
        }else{
            System.out.println();
            for (Card c : board.cards) {
                System.out.printf("[%s] ", c.toString());
            }
            System.out.println();
        }
        bar();
    }

    public static void displayPlayerTurn(Player p,
                                         ArrayList<Player> players,
                                         CommunityHand board,
                                         Pot pot,
                                         String round,
                                         int DealerIndex,
                                         int bb,
                                         int sb){

        clearScreen();

        displayBoard(board, pot, round);

        System.out.println("======= PLAYER TURN =======");
        System.out.printf("Player: %s\n", p.getName());
        System.out.printf("Hand: %s\n", p.getHand().toString());
        bar();
        System.out.println("===== Table =====");
        for(Player tp : players){
            String currentTurn = tp == p ? "->" : "";
            String otherTag = "";
            if(players.indexOf(tp) == DealerIndex){
                otherTag = "(Dealer)";
            }
            if(round.equals("Pre Flop") && players.size() != 2){
                if(players.indexOf(tp) == sb){
                    otherTag = "(Small Blind)";
                } else if (players.indexOf(tp) == bb) {
                    otherTag = "(Big Blind)";
                }
            } else if(round.equals("Pre Flop") && players.size() == 2){
                if(players.indexOf(tp) != DealerIndex){
                    otherTag = "(Big Blind)";
                }
            }
            System.out.printf("%s%s%s | Chips: %d | Bet: %d |%s%s\n",
                    currentTurn,
                    otherTag,
                    tp.getName(),
                    tp.getChips(),
                    tp.getRoundContribution(),
                    tp.hasFolded() ? " (FOLDED)" : "",
                    tp.getChips() == 0 && !tp.hasFolded() ? " (ALL-IN)" : "");
        }

        bar();
    }
    public static void outputWinner(Player winner,
                                    ArrayList<Player> winners,
                                    Pot pot,
                                    EvaluatedHand evaledHand,
                                    HashMap<Player, Integer> payouts){
        if(winners.isEmpty()){
            System.out.printf("---------Winner----------\n");
            System.out.printf("Winning Hand: %s\n", evaledHand.toString());
            System.out.printf("Player: %s\nHand: %s\nChips Won: %s\n-------------------------", winner.getName(), winner.getHand(), pot.getPot());
        } else{
            System.out.printf("---------Winners---------\n");
            System.out.printf("Winning Hand: %s\n", evaledHand.toString());
            for (Player player : winners) {
                int payout = payouts.get(player);
                System.out.printf("Player: %s\nHand: %s\nChips Won: %s\n-------------------------", player.getName(), player.getHand().toString(), payout);
            }
        }
    }

    public static void outputAllHands(ArrayList<Player> players, CommunityHand board){
        System.out.println("------------------All Hands-------------------");
        for(Player p : players){
            EvaluatedHand hd = HandEvaluator.evaluateHand(p.getHand(), board);
            System.out.printf("  %s\n    [%s] [%s]\n", p.getName(), p.getHand().toString(), hd.toString());
        }
    }


    public static void outputSPWinner(Player winner,
                                      int sidePotNum,
                                      SidePot sp,
                                      ArrayList<Player> winners,
                                      EvaluatedHand evaledHand,
                                      HashMap<Player, Integer> payouts) {
        System.out.printf("----------Side Pot #%d----------\n", sidePotNum);
        System.out.printf("    Amount: %d chips\n", sp.getAmount());
        System.out.printf("    Involved Players:\n");
        for (Player p : sp.getEligiblePlayers()) {
            System.out.printf("        -%s\n", p.getName());
        }
        if (winners.isEmpty()) {
            System.out.printf("---Winner----\n");
            System.out.printf("Winning Hand: %s\n", evaledHand.toString());
            System.out.printf("Player: %s\nHand: %s\nChips Won: %s\n-------------------------", winner.getName(), winner.getHand(), sp.getAmount());
        } else {
            System.out.printf("---Winners---\n");
            System.out.printf("Winning Hand: %s\n", evaledHand.toString());
            for (Player player : winners) {
                int payout = payouts.get(player);
                System.out.printf("Player: %s\nHand: %s\nChips Won: %s\n-------------------------", player.getName(), player.getHand().toString(), payout);
            }
            bar();
        }
    }

    public static boolean playAgain(){
        clearScreen();


        while (true) {
            System.out.println("Play Again With Same Players? [Y/N]");
            System.out.print(">");

            String respo = sc.nextLine().toLowerCase();

            if (respo.equals("y")) {
                return true;
            } else if (respo.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid Input, Try Again!");
                continue;
            }
        }

    }
}
