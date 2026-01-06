package poker;

import java.util.Scanner;

/**
 * All input output functions for prompting players
 * might combine into the GameIO class
 **/
public class PlayerIO {
    private static final Scanner sc = new Scanner(System.in);
    public static int getBetAmount(Player p, int minBetAmount){
        System.out.printf("Player %s, Choose Amount To Bet\n>", p.getName());
        
        while (true) {
            int amount;
            try{
                String input = sc.nextLine().toLowerCase();
                if(input.equals("b") || input.equals("back")){
                    return -456;
                }
                amount = Integer.parseInt(input);
            } catch(NumberFormatException e){
                System.out.println("Invalid Input! Try Again");
                continue;
            }
            if(amount > p.getChips()){
                System.out.println("Cannot Bet More Chips Than You Have! Try Again");
                continue;
            }
            if(amount < minBetAmount){
                System.out.println("Cannot Bet Less Than The Minimum Bet Amount! Try Again");
                continue;
            }
            return amount;
        }
    }


    public static PlayerAction getPlayerAction(Player p, boolean CanCheck){        
        while(true){
            System.out.printf("%s, Choose Your Action\n", p.getName());
            if(!CanCheck){
                System.out.println("C: Call");
            } else{
                System.out.println("C: Check");
            }
            System.out.println("R: Raise");
            System.out.println("F: Fold");
            System.out.print(">");

            String input = sc.nextLine();
            input = input.toUpperCase();
            switch(input){
                case "C":
                    if(!CanCheck){
                        return PlayerAction.CALL;
                    } else{
                        return PlayerAction.CHECK;
                    }
                case "R":
                    return PlayerAction.RAISE;
                case "F":
                    return PlayerAction.FOLD;
                default:
                    System.out.println("Invalid Input! Try Again");
            }
        }
    }
}
