package poker;

public class PlayerHand extends Hand {
    
    public PlayerHand(){
        
    }

    public void displayHand(){
        System.out.printf("Hand: \n%s\n%s\n", this.cards.get(0), this.cards.get(1));
    }

    @Override
    public String toString(){
        String HandString = "[%card1% %card2%]";
        HandString = HandString.replace("%card1%", this.cards.get(0).toString());
        HandString = HandString.replace("%card2%", this.cards.get(1).toString());
        return HandString;
    }

}
