package poker;

public class Player {
    private PlayerHand hand;
    private String name;
    private int chips;
    private int currentBet;
    private boolean folded;
    private int roundContribution = 0;


    public Player(String name, int chips){
        this.name = name;
        this.chips = chips;
        this.hand = null;
        this.currentBet = 0;
    }

    public String getName(){
        return this.name;
    }


    //methods that deal with player's hand
    public void setHand(PlayerHand hand){
        this.hand = hand;
    }

    public PlayerHand getHand(){
        return this.hand;
    }

    //methods that deal with player's chips

    public int getChips(){
        return this.chips;
    }

    public void addChips(int chips){
        this.chips += chips;
    }

    public void removeChips(int chips){
        this.chips -= chips;
    }

    public void setChips(int chips){
        this.chips = chips;
    }

    //methods that deal with folding
    public void fold(){
        this.folded = true;
    }

    public boolean hasFolded(){
        return this.folded;
    }

    //methods that deal with betting

    public int getCurrentBet(){
        return this.currentBet;
    }

    public void setCurrentBet(int betAmount){
        this.currentBet = betAmount;
    }

    public void bet(int betAmount){
        if(betAmount > chips) betAmount = chips; //all in bet

        chips -= betAmount;
        currentBet += betAmount;
    }

    public int getRoundContribution(){
        return this.roundContribution;
    }

    public void setRoundContribution(int RC){
        this.roundContribution = RC;
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
        String PlayerDetails = new String("Player Name: %name%\nHand: %hand%\nChips: %chips%");
        PlayerDetails = PlayerDetails.replace("%name%", this.name);
        PlayerDetails = PlayerDetails.replace("%hand%", this.hand.toString());
        PlayerDetails = PlayerDetails.replace("%chips%", String.valueOf(this.chips));
        return PlayerDetails;
    }
}
