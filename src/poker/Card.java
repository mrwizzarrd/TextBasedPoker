package poker;

/**
 * @class Card Object Class
 * 
 * Class Properties:
 * 
 * 
 * value- integer that stores the value of one of the 13 cards
 * A- 14
 * 2-9- their Face Values
 * J- 10
 * Q- 11
 * K- 12
 * 
 * suit
 * one of 4 types from a enum definition:
 * 
 */

public class Card{
    Rank rank;
    Suit suit;


    public Card(Rank rank, Suit suit){
        this.rank = rank;
        this.suit = suit;
    }

    public Suit getSuit(){
        return this.suit;
    }

    public Rank getValue(){
        return this.rank;
    }

    @Override
    public String toString(){
        return rank + " of " + suit;
    }
}