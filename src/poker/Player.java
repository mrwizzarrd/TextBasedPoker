package poker;

public class Player {
    private PlayerHand hand;
    private final String name;
    private int chips;
    private int currentBet;
    private boolean folded;
    private int roundContribution = 0;
    public boolean isBot = false;


    public Player(String name, int chips){
        this.name = name;
        this.chips = chips;
        this.hand = null;
        this.currentBet = 0;
    }

    //=========Getters/Setters====================
    public void setHand(PlayerHand hand){
        this.hand = hand;
    }

    public PlayerHand getHand(){
        return this.hand;
    }


    public String getName(){
        return this.name;
    }

    public int getChips(){
        return this.chips;
    }

    public void setChips(int chips){
        this.chips = chips;
    }

    public void addChips(int chips){
        this.chips += chips;
    }

    public void removeChips(int chips){
        this.chips -= chips;
    }

    public boolean hasFolded(){
        return this.folded;
    }

    public int getCurrentBet(){
        return this.currentBet;
    }

    public void setCurrentBet(int betAmount){
        this.currentBet = betAmount;
    }

    public int getMaxAffordableBet(int agressionPercent){
        return chips * (agressionPercent/100);
    }

    public boolean isBot() { return this.isBot; }

    public int getRoundContribution(){
        return this.roundContribution;
    }

    public void setRoundContribution(int RC){
        this.roundContribution = RC;
    }

    //========Action Methods===============

    public void fold(){
        this.folded = true;
    }

    public PlayerAction getPlayerAction(boolean canCheck, PokerGame game){
        return PlayerIO.getPlayerAction(this, canCheck);
    }

    public int getRaiseAmount(int minLegalRaise){
        return PlayerIO.getBetAmount(this, minLegalRaise);
    }

    public void addToRoundContribution(int amount){
        this.roundContribution += amount;
        if(amount > this.chips){
            amount = this.chips;
        }
        this.chips -= amount;
    }

    public void resetRoundContribution(){
        this.roundContribution = 0;
    }

    public void resetPlayer(){
        this.folded = false;
        this.currentBet = 0;
        this.hand = null;
        this.roundContribution = 0;
    }

    @Override
    public String toString(){
        String PlayerDetails = "Player Name: %name%\nHand: %hand%\nChips: %chips%";
        PlayerDetails = PlayerDetails.replace("%name%", this.name);
        PlayerDetails = PlayerDetails.replace("%hand%", this.hand.toString());
        PlayerDetails = PlayerDetails.replace("%chips%", String.valueOf(this.chips));
        return PlayerDetails;
    }
}
