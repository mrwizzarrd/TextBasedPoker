package poker;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerIO {
    private static Scanner sc = new Scanner(System.in);
    public static int getBetAmount(Player p, int minBetAmount){
        System.out.printf("Player %s, Choose Amount To Bet\n>", p.getName());
        
        while (true) {
            int amount;
            try{
                amount = Integer.parseInt(sc.nextLine());
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
            System.out.printf("Player %s, Choose Your Action\n", p.getName());
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
}
