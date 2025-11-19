package poker;

import java.util.ArrayList;
import java.util.Set;
import java.util.Random;

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

    /*Uses the Fisher-Yates Shuffle Algorithm*/
    public void shuffleDeck(){
        Random rnd = new Random();
        for(int j = 51; j >= 0; j--){
            Card currentCard = this.deck.get(j);
            int deckIndex = rnd.nextInt(j+1);
            Card deckIndexCard = this.deck.get(deckIndex);
            this.deck.set(deckIndex, currentCard);
            this.deck.set(j, deckIndexCard);
        }
    }

    public Card drawCard(){
        Card drawnCard = this.deck.get(0);
        this.deck.remove(0);
        return drawnCard;
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
