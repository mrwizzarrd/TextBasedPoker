package poker;

import java.util.ArrayList;

/*
The Dealer Class Handles anything to do with the movement of cards
dealing hands to players during pre flop, and dealing the Flop, Turn, and River
 */

public class Dealer {
    Deck deck;

    public Dealer(){
        this.deck = new Deck();
        this.deck.shuffleDeck();
    }

    public void burnCard(){
        this.deck.drawCard();
    }


    public void dealFlop(CommunityHand board){
        burnCard();
        board.addCommunityCard(this.deck.drawCard());
        board.addCommunityCard(this.deck.drawCard());
        board.addCommunityCard(this.deck.drawCard());

    }

    public void dealTurn(CommunityHand board){
        burnCard();
        board.addCommunityCard(this.deck.drawCard());

    }

    public void dealRiver(CommunityHand board){
        burnCard();
        board.addCommunityCard(this.deck.drawCard());
    }

    public void dealHands(ArrayList<Player> players){
        for(Player p : players){
            PlayerHand hand = new PlayerHand();
            p.setHand(hand);
        }

        for(int i = 0; i < 2; i++){
            for(Player p : players){
                p.getHand().addCard(this.deck.drawCard());
            }
        }
    }

    
}
