package poker;
import java.util.ArrayList;

public abstract class Hand {

    protected ArrayList<Card> cards = new ArrayList<Card>();

    public void addCard(Card c){
        cards.add(c);
    }

    public int size(){
        return cards.size();
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    @Override
    public String toString() {
        return cards.toString();
    }

}

