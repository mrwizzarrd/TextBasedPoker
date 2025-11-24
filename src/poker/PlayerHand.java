package poker;

public class PlayerHand extends Hand {
    
    public PlayerHand(Deck deck){
        addCard(deck.drawCard());
        addCard(deck.drawCard());
    }

    public void displayHand(){
        System.out.printf("Hand: \n%s\n%s\n", this.cards.get(0), this.cards.get(0));
    }

}
