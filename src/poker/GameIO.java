package poker;

import java.util.Scanner;
import java.util.ArrayList;


public class GameIO {

    private static Scanner sc = new Scanner(System.in);

    //helpers
    private static void clearScreen(){
        System.out.print("\033[2J\033[H");
    }

    private static String promptInput(String inputMessage){
        System.out.printf("%s\n>", inputMessage);
        String userInput = sc.nextLine();
        return userInput;
    }

    private static void bar(){
        System.out.println("=============================");
    }


    //screens
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

            switch (input) {
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

            } catch(NumberFormatException e){
                System.out.println("Invalid Input- Must Be Integer");
                continue;
            }
            System.out.printf("%d Players, Is This Correct? [Y/N] (Any other input will be taken as N)");
            String conf = promptInput("");

            if(conf.equals("Y") || conf.equals("y")){
                break;
            }
        }
        for(int i = 0; i < playerAmount; i++){
            while (true) {
                clearScreen();
                System.out.printf("========Player #%d=========", i + 1);
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
        System.out.println("=======CURRENT  BOARD=======");
        bar();
        System.out.printf("Round: %s\nChips in Pot: %d\n", round, pot.getPot());
        bar();

        System.out.println("Cards:\n");
        if(board.cards.isEmpty()){
            System.out.println("[No Cards]\n");
        }else{
            System.out.println();
            for (Card c : board.cards) {
                System.out.printf("[%s] ", c.toString());
            }
            System.out.println();
        }
        bar();
    }
}
