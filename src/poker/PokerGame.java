package poker;
import java.util.ArrayList;


/*
    All game logic in this class
    This is the game engine class that handles the round
*/
public class PokerGame {
    private Pot pot;
    private Dealer dealer;
    private ArrayList<Player> players;
    private CommunityHand board;
    private int dealerIndex = 0;
    private int lastAggressorIndex;


    public PokerGame(ArrayList<Player> players){
        this.pot = new Pot();
        this.dealer = new Dealer();
        this.players = new ArrayList<Player>(players);
        this.board = new CommunityHand();
    }

    /* in poker, the dealer rotates every round
    small blind bet = 1 chip
    big blind bet = 2 chips
    */

    private void rotateDealer(){
        this.dealerIndex = (this.dealerIndex + 1) % this.players.size();
    }

    private int getSmallBlindIndex(){
        return (this.dealerIndex + 1) % this.players.size();
    }

    private int getBigBlindIndex(){
        return (this.dealerIndex + 2) % this.players.size();
    }

    private int getFTAIndex(){

        if(players.size() == 3){
            return getSmallBlindIndex();
        }
        return (this.dealerIndex + 3) % this.players.size();
    }


//------Betting Round Helper Utilities-----------
    private int getHighestBet(){
        int max = 0;
        for(Player p : players){
            if(p.getRoundContribution() > max){
                max = p.getRoundContribution();
            }
        }
        return max;
    }

    //betting round stopping condition #1
    private boolean allPlayersMatchedHighest(){
        int highestBet = getHighestBet();
        for(Player p : players){
            if(p.hasFolded()){
                continue;
            }
            if(p.getRoundContribution() != highestBet){
                return false;
            }
        }
        return true;
    }

    //betting round stopping condition #2
    private boolean lastAgressorReached(int currentIndex){
        return currentIndex == lastAggressorIndex;
    }

//=============Betting Round Method for Game==============
    public void runBettingRound(int startingPlayerIndex){

        int currentIndex = startingPlayerIndex;

        while(true){
            PlayerAction bettingAction;
            Player p = players.get(currentIndex);
            if(p.hasFolded() || p.getChips() == 0){
                currentIndex = (currentIndex + 1) % players.size();
                continue;
            }


            bettingAction = PlayerIO.getPlayerAction(p, (p.getRoundContribution() == getHighestBet()));

            switch (bettingAction) {
                case CHECK:
                    break;
                case CALL:
                    int need = getHighestBet() -p.getRoundContribution();
                    p.addToRoundContribution(need);
                    this.pot.addToPot(need);
                    break;
                case RAISE:
                    int raiseAmount = PlayerIO.getBetAmount(p, getHighestBet() + 1);
                    lastAggressorIndex = currentIndex;

                    int diff = raiseAmount - p.getRoundContribution();
                    this.pot.addToPot(diff);
                    p.addToRoundContribution(diff);
                    break;
                case FOLD:
                    p.fold();
                    break;
                default:
                    break;
            }

            if(((allPlayersMatchedHighest() && lastAgressorReached(currentIndex)))){
                break;
            }
            currentIndex = (currentIndex + 1) % players.size(); 
        }  
    
    }

    public void PreFlop(){
        //reset everything for new hand
        this.pot = new Pot();
        this.board = new CommunityHand();
        this.dealer = new Dealer();

        //reset all players
        for(Player p : players){
            p.resetPlayer();
        }

        //removes any players who are broke
        for(int i = players.size(); i >= 0; i--){
            Player p = players.get(i);
            if(p.getChips() == 0){
                players.remove(p);
                continue;
            }
        }


        rotateDealer();

        int sbPlayer = getSmallBlindIndex();
        int bbPlayer = getBigBlindIndex();

        this.players.get(sbPlayer).addToRoundContribution(1);
        this.players.get(bbPlayer).addToRoundContribution(2);
        //TODO LATER: Handle a scenario where player doesn't have enough chips

        this.pot.addToPot(3);

        dealer.dealHands(players);

        int startingPlayer = getFTAIndex();
        lastAggressorIndex = startingPlayer;

        runBettingRound(startingPlayer);

    }

public void FlopRound(){
    for(Player p : players){
            p.resetRoundContribution();
    }
    dealer.dealFlop(this.board);
    lastAggressorIndex = getSmallBlindIndex();
    runBettingRound(getSmallBlindIndex());
}

public void TurnRound(){
    for(Player p : players){
            p.resetRoundContribution();
    }
    dealer.dealTurn(board);
    lastAggressorIndex = getSmallBlindIndex();
    runBettingRound(getSmallBlindIndex());
}

public void RiverRound(){
    for(Player p : players){
            p.resetRoundContribution();
    }
    dealer.dealRiver(board);
    lastAggressorIndex = getSmallBlindIndex();
    runBettingRound(getSmallBlindIndex());
}

    public void resetGame(){
        this.pot = new Pot();
        this.dealer = new Dealer();
    }
}
