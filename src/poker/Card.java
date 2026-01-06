package poker;

/**
 * @class Card Object Class
 * 
 * Class Properties:
 * 
 * 
 * value: integer that stores the value of one of the 13 cards
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
        String rnk;
        String sut;

        switch (this.rank){
            case ACE:
                rnk = "A";
                break;
            case TWO:
                rnk = "2";
                break;
            case THREE:
                rnk = "3";
                break;
            case FOUR:
                rnk = "4";
                break;
            case FIVE:
                rnk = "5";
                break;
            case SIX:
                rnk = "6";
                break;
            case SEVEN:
                rnk = "7";
                break;
            case EIGHT:
                rnk = "8";
                break;
            case NINE:
                rnk = "9";
                break;
            case TEN:
                rnk = "10";
                break;
            case JACK:
                rnk = "J";
                break;
            case QUEEN:
                rnk = "Q";
                break;
            case KING:
                rnk = "K";
                break;
            default:
                rnk = "";
                break;
        }

        switch (this.suit){
            case CLUBS:
                sut = "♣";
                break;
            case SPADES:
                sut = "♠";
                break;
            case DIAMONDS:
                sut = "♦";
                break;
            case HEARTS:
                sut = "♥";
                break;
            default:
                sut = "";
        }
        return rnk + sut;
    }
}