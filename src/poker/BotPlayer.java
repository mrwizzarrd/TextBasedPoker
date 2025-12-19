package poker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BotPlayer extends Player{

    private final int aggression; /*will be scaled as follows:
                                             0: always fold
                                             0 < x <= 15: not very aggressive (Low)
                                             15 < x <= 30: somewhat aggressive (Med)
                                             30 < x <= 45: Very Aggressive (High)
                                             45 < x <= 60: Most Aggressive (Extreme)*/

    private float handStrength = (float) 1.0;

    //----------Helper Funcs---------------
    private float calculateAggressionPercentage(){
        return Math.min(((100.0f * this.aggression)/60.0f) * this.handStrength, 100.0f);
    }

    private int randomSubtractor(int min){
        int inverseAggression = 100 - (int) this.calculateAggressionPercentage();

        return (int) (Math.random() * (min * inverseAggression) / 100);
    }


    //----------Getters/Setters------------
    public int getAggression(){
        return this.aggression;
    }

    public float getHandStrength(){
        return this.handStrength;
    }

    public void setHandStrength(float hs){
        this.handStrength = hs;
    }

    //----------Constructor----------------

    public BotPlayer(int chips, int aggression) {
        super(BotPlayerNames.getName(), chips);
        this.aggression = aggression;
        this.isBot = true;
    }



    //---------------------------Action Decider (Main Class Logic)-----------------------------------------------
    public void evaluateHand(Hand hand, CommunityHand board){
        float handStrength = 0.75f;
        List<Card> Cards = new ArrayList<>(board.cards);
        Card c1 = hand.getCard(0);
        Card c2 = hand.getCard(1);
        Cards.add(c1);
        Cards.add(c2);

        int rankDiff = Math.abs(c1.rank.ordinal() - c2.rank.ordinal());


        //if the community hand is empty (pre-flop) only decide based on the personal hand
        if(board.cards.isEmpty()){
            if(rankDiff == 0){
                handStrength += .3f;
            } else if(handStrength <= 3){
                handStrength += .075f;
            } else{
                handStrength -= .3f;
            }

            if(c1.suit == c2.suit){
                handStrength += .2f;
            }
        } else{
            int maxSequenceLength = 0;
            int maxSuitFreq = 0;
            int[] suitFreq = {0, 0, 0, 0};
            int[] rankFreq = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            for(Card c : Cards){
                int cardRank = c.rank.ordinal();
                int cardSuit = c.getSuit().ordinal();
                suitFreq[cardSuit]++;
                rankFreq[cardRank]++;
            }
            int SequenceLength = 0;
            for(int i = 0; i < 13; i++){
                if(rankFreq[i] == 2){
                    handStrength += .2f;
                    continue;
                }
                if(rankFreq[i] == 3){
                    handStrength += .3f;
                }
                if(rankFreq[i] == 4){
                    handStrength += .35f;
                }
                if(SequenceLength == 0 || rankFreq[(i+1) % 13] != 0) {
                    SequenceLength++;
                    maxSequenceLength = Math.max(SequenceLength, maxSequenceLength);
                } else{
                    SequenceLength = 0;
                }
            }
            for(Integer i : suitFreq){
                if(i > maxSuitFreq){
                    maxSuitFreq = i;
                }
            }
            handStrength += (maxSuitFreq / 20.0f);
        }

        handStrength = Math.min(handStrength, 1.6f);

        this.handStrength = handStrength;
    }

    @Override
    public PlayerAction getPlayerAction(boolean canCheck, CommunityHand board){
        int roll = (int) (Math.random() * 100);
        evaluateHand(this.getHand(), board);
        float aggressionPercent = calculateAggressionPercentage();

        PlayerAction callCheck = canCheck ? PlayerAction.CHECK : PlayerAction.CALL;

        //No Aggression
        if(this.aggression == 0) {
            return PlayerAction.FOLD;
        }

        //Low Aggression
        if(aggression <= 15){
            return roll > aggressionPercent ? PlayerAction.FOLD : callCheck;
        }

        //Medium Aggression
        if(aggression <= 30){
            return roll < aggressionPercent ? callCheck : PlayerAction.FOLD;
        }

        //High Aggression
        if(aggression <= 45){
            return roll < aggressionPercent  ? PlayerAction.RAISE : callCheck;
        }

        //Extreme Aggression
        if(aggression <= 60){
            return roll < aggressionPercent ? PlayerAction.RAISE : callCheck;
        }

        return PlayerAction.FOLD;
    }

    @Override
    public int getRaiseAmount(int minLegalRaise){

        int max = this.getMaxAffordableBet(this.aggression);

        int raise = max - this.randomSubtractor(minLegalRaise);

        return Math.max(minLegalRaise, raise);
    }
}
