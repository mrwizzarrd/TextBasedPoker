package poker;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/*
    All game logic in this class
    This is the game engine class that handles the round
*/
public class PokerGame {

    //=============================================================================
    //================Class Properties And Constructor=============================
    //=============================================================================
    private Pot pot;
    private Dealer dealer;
    private final ArrayList<Player> players;
    private CommunityHand board;
    private int dealerIndex = 0;
    private int lastAggressorIndex;
    private String currentRound;
    private int sbPosition;
    private int bbPosition;

    public PokerGame(ArrayList<Player> players){
        this.pot = new Pot();
        this.dealer = new Dealer();
        this.players = new ArrayList<>(players);
        this.board = new CommunityHand();
    }

    /* in poker, the dealer rotates every round
    small blind bet = 1 chip
    big blind bet = 2 chips
    */


    //=============================================================================
    //=========================Getters/Setters=====================================
    //=============================================================================

    public CommunityHand getBoard(){
        return this.board;
    }

    //=============================================================================
    //=========================Helpers=============================================
    //=============================================================================


    private void rotateDealer(){

        this.dealerIndex = (this.dealerIndex + 1) % this.players.size();
    }

    private int getSmallBlindIndex(){

        if(this.players.size() == 2){
            return this.dealerIndex;
        }

        return (this.dealerIndex + 1) % this.players.size();
    }

    private int getBigBlindIndex(){

        if(this.players.size() == 2){
            return (this.dealerIndex + 1) % 2;
        }
        return (this.dealerIndex + 2) % this.players.size();
    }

    private int getPostflopSB(int oldSB) {
        int idx = oldSB;
        while (players.get(idx).hasFolded()) {
            idx = (idx + 1) % players.size();
        }
        return idx;
    }

    private int getFTAIndex(){

        if(players.size() == 3){
            return getSmallBlindIndex();
        } if(players.size() == 2){
            return  this.dealerIndex;
        }
        return (this.dealerIndex + 3) % this.players.size();
    }

    private int getNextActivePlayer(int startIndex) {
        int index = startIndex;
        while (players.get(index).hasFolded()) {
            index = (index + 1) % players.size();
        }
        return index;
    }

    private int getActivePlayerCount(){
        int count = 0;
        for(Player p : players){
            if(!p.hasFolded()){
                count++;
            }
        }
        return count;
    }


    public static HashMap<Player, Integer> splitPot(Pot pot, ArrayList<Player> winners){
        HashMap<Player, Integer> payouts = new HashMap<>();
        int total = pot.getPot();
        int numOfWinners = winners.size();

        int base = total / numOfWinners;
        int leftover = total % numOfWinners;

        for(int i = 0; i < winners.size(); i++){
            Player p = winners.get(i);
            int payout = base;

            if(i < leftover){
                payout++;
            }

            payouts.put(p, payout);
        }
        return payouts;
    }
    public static HashMap<Player, Integer> splitSidePot(SidePot pot, ArrayList<Player> winners){
        HashMap<Player, Integer> payouts = new HashMap<>();
        int total = pot.getAmount();
        int numOfWinners = winners.size();

        int base = total / numOfWinners;
        int leftover = total % numOfWinners;

        for(int i = 0; i < winners.size(); i++){
            Player p = winners.get(i);
            int payout = base;

            if(i < leftover){
                payout++;
            }

            payouts.put(p, payout);
        }
        return payouts;
    }




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

    private  boolean endOfRound(int currentIndex, int startingPlayerIndex){
        if(!allPlayersMatchedHighest()){
            return false;
        }
        if(lastAggressorIndex != -1){
            int nextIndex = (currentIndex + 1) % players.size();
            nextIndex = getNextActivePlayer(nextIndex);
            return nextIndex == this.lastAggressorIndex;
        }
        int nextActive = (currentIndex + 1) % players.size();
        nextActive = getNextActivePlayer(nextActive);
        return nextActive == startingPlayerIndex;
    }



    //================================================================================
    //=========================Game Logic=============================================
    //================================================================================

    //=============Side Pot Handler=============================
    private void buildSidePots(){
        List<Player> activePlayers = new ArrayList<>();

        for(Player p : players){
            if(!p.hasFolded()){
                activePlayers.add(p);
            }
        }

        activePlayers.sort(Comparator.comparingInt(Player::getRoundContribution));

        int previous = 0;

        for(int i = 0; i < activePlayers.size(); i++){
            Player p = activePlayers.get(i);
            int contrib = p.getRoundContribution();

            if(contrib > previous){
                SidePot side = new SidePot();
                int level = contrib - previous;

                for(Player pl : activePlayers){
                    int amount = Math.min(pl.getRoundContribution() - previous, level);

                    if(amount > 0){
                        side.addAmount(amount);
                        side.addPlayer(pl);
                    }
                }

                pot.addSidePot(side);

                previous = contrib;
            }
        }
    }


    //=============Betting Round Method for Game==============
    public void runBettingRound(int startingPlayerIndex){

        int currentIndex = startingPlayerIndex;

        while(true){
            PlayerAction bettingAction;
            Player p = players.get(currentIndex);
            if(p.hasFolded() || p.getChips() == 0){

                if(endOfRound(currentIndex, startingPlayerIndex)){
                    break;
                }
                currentIndex = (currentIndex + 1) % players.size();
                continue;
            }

            GameIO.displayPlayerTurn(p, this.players, this.board, this.pot, this.currentRound, this.dealerIndex, this.bbPosition, this.sbPosition);
            bettingAction = PlayerIO.getPlayerAction(p, (p.getRoundContribution() == getHighestBet()));

            switch (bettingAction) {
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
                    if(getActivePlayerCount() == 1){
                        return;
                    }
                    break;
                default:
                    break;
            }


            if((endOfRound(currentIndex, startingPlayerIndex))){
                break;
            }


            currentIndex = (currentIndex + 1) % players.size();
            currentIndex = getNextActivePlayer(currentIndex);
            System.out.println("Next Player To Act: " + players.get(currentIndex).getName());
            GameIO.pressAnyKeyToContinue();
        }  
    
    }

    public void PreFlop(){
        if(getActivePlayerCount() == 1){
            return;
        }
        this.currentRound = "Pre Flop";
        //reset everything for new hand
        this.pot = new Pot();
        this.board = new CommunityHand();
        this.dealer = new Dealer();

        //reset all players
        for(Player p : players){
            p.resetPlayer();
        }

        //removes any players who are broke
        for(int i = players.size() - 1; i >= 0; i--){
            Player p = players.get(i);
            if(p.getChips() == 0){
                players.remove(p);
            }
        }


        rotateDealer();

        sbPosition = getSmallBlindIndex();
        bbPosition = getBigBlindIndex();

        this.players.get(sbPosition).addToRoundContribution(1);
        this.players.get(bbPosition).addToRoundContribution(2);
        //TODO LATER: Handle a scenario where player doesn't have enough chips

        this.pot.addToPot(3);

        dealer.dealHands(players);

        int startingPlayer = getFTAIndex();
        lastAggressorIndex = startingPlayer;

        runBettingRound(startingPlayer);

    }

    public void FlopRound(){
        if(getActivePlayerCount() == 1){
            return;
        }
        this.currentRound = "Flop";
        for(Player p : players){
                p.resetRoundContribution();
        }
        lastAggressorIndex = -1;
        this.sbPosition = getPostflopSB(this.sbPosition);
        int startingPlayer = getNextActivePlayer(this.sbPosition);
        dealer.dealFlop(this.board);

        runBettingRound(startingPlayer);
    }

    public void TurnRound(){
        if(getActivePlayerCount() == 1){
            return;
        }
        this.currentRound = "Turn";
        for(Player p : players){
                p.resetRoundContribution();
        }
        lastAggressorIndex = -1;
        this.sbPosition = getPostflopSB(this.sbPosition);
        int startingPlayer = getNextActivePlayer(this.sbPosition);
        dealer.dealTurn(board);

        runBettingRound(startingPlayer);
    }

    public void RiverRound(){
        if(getActivePlayerCount() == 1){
            return;
        }
        this.currentRound = "River";
        for(Player p : players){
                p.resetRoundContribution();
        }
        lastAggressorIndex = -1;
        this.sbPosition = getPostflopSB(this.sbPosition);
        int startingPlayer = getNextActivePlayer(this.sbPosition);
        dealer.dealRiver(board);
        runBettingRound(startingPlayer);
        buildSidePots();
    }


    public void determineWinner() {

        int count = getActivePlayerCount();
        if(count == 0){
            System.err.println("ERROR: Winner Cannot Be Determined.");
        }
        Player winningPlayer = null;
        EvaluatedHand winnersHand = null;
        ArrayList<Player> tiedPlayers = new ArrayList<>();
        HashMap<Player, Integer> split = null;

        if(getActivePlayerCount() != 1){
            for (Player player : players){
                if(player.hasFolded()){
                    continue;
                }
                if (winningPlayer == null) {
                    winningPlayer = player;
                    winnersHand = HandEvaluator.evaluateHand(player.getHand(), board);
                    continue;
                }

            EvaluatedHand currentHand = HandEvaluator.evaluateHand(player.getHand(), board);

            if (currentHand.compareTo(winnersHand) > 0) {
                winnersHand = currentHand;
                winningPlayer = player;
                tiedPlayers.clear();
            } else if (currentHand.compareTo(winnersHand) == 0) {
                if (!tiedPlayers.isEmpty()) {
                    tiedPlayers.add(player);
                } else {
                    tiedPlayers.add(winningPlayer);
                    tiedPlayers.add(player);
                    }
                }
            }

        } else{
            for(Player p : players) {
                if (!p.hasFolded()) {
                    winningPlayer = p;
                    break;
                }
            }
        }

        if (tiedPlayers.isEmpty()) {
            assert winningPlayer != null;
            winningPlayer.addChips(pot.getPot());
        } else {
            split = splitPot(this.pot, tiedPlayers);

            for (Player p : tiedPlayers) {
                int payout = split.get(p);
                p.addChips(payout);
            }
        }
        GameIO.outputWinner(winningPlayer, tiedPlayers, pot, winnersHand, split);

        int potNumber = 1;
        for(SidePot sidePot : pot.getSidePots()){

            Player spWinner = null;
            EvaluatedHand bestHand = null;
            ArrayList<Player> tiedSP = new ArrayList<>();
            HashMap<Player, Integer> payoutMap = null;

            for(Player pl : sidePot.getEligiblePlayers()){
                EvaluatedHand h = HandEvaluator.evaluateHand(pl.getHand(), this.board);

                if(spWinner == null){
                    spWinner = pl;
                    bestHand = h;
                    continue;
                }
                int cmp = h.compareTo(bestHand);
                if(cmp > 0){
                    spWinner = pl;
                    bestHand = h;
                    tiedSP.clear();
                } else if (h.compareTo(bestHand) == 0){
                    tiedSP.add(pl);
                }
            }
            if(!tiedSP.isEmpty()){
                tiedSP.add(spWinner);
                payoutMap = splitSidePot(sidePot, tiedSP);
                for(Player p : tiedSP){
                    int payout = payoutMap.get(p);
                    p.addChips(payout);
                }
            } else{
                spWinner.addChips(sidePot.getAmount());
            }
            GameIO.outputSPWinner(spWinner, potNumber, sidePot, tiedSP, bestHand, payoutMap);
            potNumber++;
        }
    }
    public void resetGame(){
        this.pot = new Pot();
        this.dealer = new Dealer();
    }
}
