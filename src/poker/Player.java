package poker;

public class Player {
    private PlayerHand hand;
    private String name;
    private int chips;
    private int currentBet;
    private boolean folded;


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

    public Hand getHand(){
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

    public void resetPlayer(){
        this.folded = false;
        this.currentBet = 0;
        this.hand = null;
    }
}
