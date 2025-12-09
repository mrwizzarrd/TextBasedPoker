package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pot {

    private int mainPot;

    private final List<SidePot> sidePots = new ArrayList<>();

    public Pot(){
        this.mainPot = 0;
    }

    public int getPot(){
        return this.mainPot;
    }

    public void addToPot(int amount){
        this.mainPot += amount;
    }


    public void resetPot(){
        this.mainPot = 0;
    }

    public List<SidePot> getSidePots() {
        return Collections.unmodifiableList(this.sidePots);
    }

    public void addSidePot(SidePot pot) {
        this.sidePots.add(pot);
    }
    
}
