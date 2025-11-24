package poker;

public class Dealer {
    Deck deck;

    public Dealer(){
        this.deck = new Deck();
        this.deck.shuffleDeck();
    }


    public void dealFlop(CommunityHand board){
        board.addCommunityCard(this.deck.drawCard());
        board.addCommunityCard(this.deck.drawCard());
        board.addCommunityCard(this.deck.drawCard());

    }

    public void dealTurn(CommunityHand board){
        board.addCommunityCard(this.deck.drawCard());

    }

    public void dealRiver(CommunityHand board){
        board.addCommunityCard(this.deck.drawCard());
    }




    
}
