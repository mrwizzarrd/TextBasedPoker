package poker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;


public class GameIO {

    private static Scanner sc = new Scanner(System.in);

    private static String ShortBar = "=============================";
    private static String LongBar = "==========================================================";

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
        System.out.println(ShortBar);
    }

    private static void divider(){
        System.out.print("======================================================\n");
    }


    public static void pressAnyKeyToContinue() {
        System.out.println("[Press Enter To Continue...]");
        sc.nextLine();
    }

    public static String center(String text, int width){
        if(text == null){
            return " ".repeat(width);
        }
        if(text.length() >= width){
            return text;
        }
        int padding = width - text.length();
        int left = padding / 2;
        int right = padding - left;

        return " ".repeat(left) + text + " ".repeat(right);

    }





    //=============================================================================
    //=========================Screens=============================================
    //=============================================================================


    /**
     * Return Codes:
     * 1- Start Game
     * 0- Exit Game
     **/
    public static int initScreen(String vers){
        clearScreen();
        System.out.println("♥♦♠♣================Welcome To Poker====================♥♦♠♣ (vers " + vers +")");
        int statusCode = -256;
        while (true) {
            System.out.println("                    A: Start Game");
            System.out.println("                    B: Quit");
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
                if(playerAmount != -256 && playerAmount < 2){ //-256 is debug mode
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
            System.out.println("++++++++++++++++++++++++++++++++++DEBUG MODE ENTERED++++++++++++++++++++++++++++++++++++++");
            players.add(new Player("Roland", 10000));
            players.add(new BotPlayer(2500, 2));
            players.add(new BotPlayer(2500, 8));
            players.add(new BotPlayer(2500, 4));
            players.add(new BotPlayer(2500, 10));
            return players;
        }
        for(int i = 0; i < playerAmount; i++){
            while (true) {
                clearScreen();
                System.out.printf("========Player #%d=========\n", i + 1);
                String name;
                int chips;

                //---Logic to Check if Player Being Added is A Bot---
                String bot = promptInput("Is This Player A Bot? [Y/N] (Any other input will be taken as N)").toLowerCase();
                if(bot.equals("y")){
                    int aggressionLevel = 0;
                    try{
                        chips = Integer.parseInt(promptInput("Enter Starting Chips"));
                    } catch (NumberFormatException e){
                        System.out.println("Error: Input value must be an integer, try again");
                        continue;
                    }
                    try{
                        aggressionLevel = Integer.parseInt(promptInput("Enter Aggression Level For Bot Player"));
                    } catch (NumberFormatException e){
                        System.out.println("Error: Input value must be an integer, try again");
                        continue;
                    }

                    if (chips <= 0) {
                        System.out.println("Error: Chips must be positive");
                        continue;
                    }

                    if (aggressionLevel < 0 || aggressionLevel > 60) {
                        System.out.println("Error: Aggression must be between 0 and 60");
                        continue;
                    }
                    Player botPlayer = new BotPlayer(chips, aggressionLevel);

                    String conf;

                    System.out.printf("    Name: %s\n    Starting Chips: %d\n    Aggression Level: %d\nIs This Correct [Y/N] (Any other input will be taken as N)\n", botPlayer.getName(), chips, aggressionLevel);
                    conf = promptInput("");
                    if (conf.equals("Y") || conf.equals("y")) {
                        players.add(botPlayer);
                        break;
                    }


                } //-----------------Human Player Logic----------------------
                else {
                    name = promptInput("Enter Human Player Name");
                    try {
                        chips = Integer.parseInt(promptInput("Starting Chips"));
                        if(chips <= 0){
                            System.out.println("Error: Chip Amount Must Be Greater Than Zero");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Input value must be an integer, try again");
                        continue;
                    }


                    String conf;

                    System.out.printf("    Name: %s\n    Starting Chips: %d\n\nIs This Correct [Y/N] (Any other input will be taken as N)\n", name, chips);
                    conf = promptInput("");
                    if (conf.equals("Y") || conf.equals("y")) {
                        players.add(new Player(name, chips));
                        break;
                    }
                }
            }
        }
        return players;
    }

    public static void displayTable(ArrayList<Player> players,
                                    int DealerIndex,
                                    int bb,
                                    int sb,
                                    PokerGame game,
                                    Player p
                                    ){
        System.out.printf("%s%s%s%n%n", "==================== ", center("TABLE", 12), " ====================");
        for(Player tp : players){
            Action lastAction = null;
            for(Action a : game.getActionLog().getRecentActions()){
                if(a.getActor() == tp){
                    lastAction = a;
                    break;
                }
            }

            String actionTag = "-";
            if(lastAction != null) {
               if(lastAction.getBetAmount() != 0){
                   actionTag = String.valueOf(lastAction.getBetAmount());
               } else if (lastAction.getAction() == PlayerAction.FOLD || tp.hasFolded()) {
                   actionTag = "(FOLDED)";
               } else {
                   actionTag = "C";
               }
            }
            String currentTurn = tp == p ? "-> " : "";
            String otherTag = "";
            if(players.indexOf(tp) == DealerIndex){
                otherTag = "(Dealer)";
            }
            if(players.size() != 2){
                if(players.indexOf(tp) == sb){
                    otherTag = "(Small Blind)";
                } else if (players.indexOf(tp) == bb) {
                    otherTag = "(Big Blind)";
                }
            } else{
                if(players.indexOf(tp) != DealerIndex){
                    otherTag = "(Big Blind)";
                }
            }

            String nameTag = currentTurn + otherTag + tp.getName();
            System.out.printf("%-40s%-10d%s%s\n",
                    nameTag,
                    tp.getChips(),
                    actionTag,
                    tp.getChips() == 0 && !tp.hasFolded() ? " (ALL-IN)" : "");
        }

        divider();
    }
    public static void displayBoard(CommunityHand board, Pot pot, String round, Player p){
        clearScreen();
        System.out.printf("%s%s%s%n%n", "==================== ", center(round.toUpperCase(), 12), " ====================");
        System.out.printf("Pot: %d | ", pot.getPot());

        System.out.print("Board: ");
        if(board.cards.isEmpty()){
            System.out.print("----");
        }else{
            for (Card c : board.cards) {
                System.out.printf("[%s] ", c.toString());
            }
            System.out.println("\n");
        }


        System.out.printf("\nYour Hand: %s \n", p.getHand().toString());
    }

    public static void displayPlayerTurn(Player p,
                                         ArrayList<Player> players,
                                         CommunityHand board,
                                         Pot pot,
                                         String round,
                                         int DealerIndex,
                                         int bb,
                                         int sb,
                                         PokerGame game){

        clearScreen();
        displayBoard(board, pot, round, p);
        displayTable(players, DealerIndex, bb, sb, game, p);
    }
    public static void outputWinner(Player winner,
                                    ArrayList<Player> winners,
                                    Pot pot,
                                    EvaluatedHand evaledHand,
                                    HashMap<Player, Integer> payouts){
        clearScreen();
        if(winners.isEmpty()){
            System.out.printf("---------Winner----------\n");
            System.out.printf("Winning Hand: %s\n", evaledHand.toString());
            System.out.printf("Player: %s\nHand: %s\nChips Won: %s\n%s\n", winner.getName(), winner.getHand(), pot.getPot(), ShortBar);
        } else{
            System.out.printf("---------Winners---------\n");
            System.out.printf("Winning Hand: %s\n", evaledHand.toString());
            for (Player player : winners) {
                int payout = payouts.get(player);
                System.out.printf("Player: %s\nHand: %s\nChips Won: %s\n%s\n", player.getName(), player.getHand().toString(), payout, ShortBar);
            }
        }
    }

    public static void outputAllHands(ArrayList<Player> players, CommunityHand board){
        System.out.println("====================All Hands====================");
        for(Player p : players){
            EvaluatedHand hd = HandEvaluator.evaluateHand(p.getHand(), board);
            String handType = "(" + hd + ")";
            System.out.printf("%-25s%-10s%s%-20s\n", p.getName(), p.getHand().toString(), "   ", handType);
        }
    }


    public static void outputSPWinner(Player winner,
                                      int sidePotNum,
                                      SidePot sp,
                                      ArrayList<Player> winners,
                                      EvaluatedHand evaledHand,
                                      HashMap<Player, Integer> payouts) {
        System.out.printf("========= Side Pot #%d =========\n", sidePotNum);
        System.out.printf("    Amount: %d chips\n", sp.getAmount());
        System.out.printf("    Involved Players:\n");
        for (Player p : sp.getEligiblePlayers()) {
            System.out.printf("        -%s\n", p.getName());
        }
        if (winners.isEmpty()) {
            System.out.printf("    ----Winner----\n");
            System.out.printf("        Winning Hand: %s\n", evaledHand.toString());
            System.out.printf("        Player: %s\n        Hand: %s\n        Chips Won: %s\n", winner.getName(), winner.getHand(), sp.getAmount());
        } else {
            System.out.printf("    ----Winners----\n");
            System.out.printf("        Winning Hand: %s\n", evaledHand.toString());
            for (Player player : winners) {
                int payout = payouts.get(player);
                System.out.printf("        Player: %s\n        Hand: %s\n        Chips Won: %d\n        %s\n", player.getName(), player.getHand().toString(), payout, ShortBar);
            }
        }
    }

    public static void outputFoldedWinner(Player p, int chips){
        System.out.printf("========Winner by Fold========\n");
        System.out.printf("Player: %s\nHand: %s\nChips Won: %s\n", p.getName(), p.getHand(), chips);
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
