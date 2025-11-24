package poker;

public class Player {
    private PlayerHand hand;
    private String name;
    private int chips;
    public boolean folded;


    public Player(String name, int chips){
        this.name = name;
        this.chips = chips;
    }
}
