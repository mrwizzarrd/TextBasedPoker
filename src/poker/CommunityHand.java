package poker;


//instance variables will be called 'board'
public class CommunityHand extends Hand {

    public CommunityHand(){}

    public void addCommunityCard(Card card){
        if(this.cards.size() < 5){
            addCard(card);
        }
    }
    
}
