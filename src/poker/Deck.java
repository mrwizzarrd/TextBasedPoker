package poker;

import java.util.ArrayList;
import java.util.Set;

public class Deck {

    //Standard Deck Order (top to bottom): Jokers, Spades (A-K), Diamonds (A-K), Clubs (K-A), Hearts (K-A)  
    //Data structure type: Stack
    private final ArrayList<Card> deck = new ArrayList<>();

    private static final Suit[] suits = {Suit.HEARTS, Suit.CLUBS, Suit.DIAMONDS, Suit.SPADES};
    private static final Rank[] ranks = {Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX, Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING};
    private static final Set<Suit> FRONT_ORDER = Set.of(Suit.HEARTS, Suit.CLUBS);
    /* Creates a new deck in New Deck Order (no jokers)*/
    public Deck(){
        //int outerLoop = 4;
        //int innerLoop = 13;

        for (Suit suit : suits) {
            //starting with Hearts and Clubs
            
            boolean startFromFront = FRONT_ORDER.contains(suit); 
            
            if(!startFromFront){
                for(int i = 12; i >= 0; i--){
                    this.deck.add(new Card(ranks[i], suit));
                }
            } else{
                for (Rank rank : ranks) {
                    this.deck.add(new Card(rank, suit));
                }
            }

        }

    }

    @Override
    public String toString(){
        final StringBuilder finalString = new StringBuilder("Deck: \n");

        for (Card card : deck) {
            finalString.append(card).append("\n");
        }
        return finalString.toString();
    }

    
}
