package poker;

public class Pot {

    private int pot;

    public Pot(){
        this.pot = 0;
    }

    public int getPot(){
        return this.pot;
    }

    public void addToPot(int amount){
        this.pot += amount;
    }

    public void resetPot(){
        this.pot = 0;
    }
    
}
