package poker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.lang.Math;

public class BotPlayer extends Player{

    private final int aggression; /*will be scaled as follows:
                                             0: always fold
                                             0 < x <= 3: not very aggressive (Low)
                                             3 < x <= 6: somewhat aggressive (Med)
                                             6 < x <= 8: Very Aggressive (High)
                                             8 < x <= 10: Most Aggressive (Extreme)*/

    private float handStrength = 1.0f; //affects confidence

    private float confidence = 1.0f; /* scaled as follows:
                                        0: always fold
                                        .1-5.0: Low Confidence
                                        5.1-10.0: Medium Confidence
                                        10.1-15.0: High confidence*/


    //----------Helper Funcs---------------
    private float calculateDecisionPercent(){
        float normConf = Math.clamp(confidence / 15f, 0f, 1f);
        float normAggro = Math.clamp(this.aggression / 10f, 0f, 1f);
        return (float) Math.min(100f, 100 *(.70*(normConf) + .30*(normAggro)));
    }

    private int randomSubtractor(int min){
        int inverseAggression = 100 - (int) this.calculateDecisionPercent();

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

    private void calculateConfidence(ActionLog al, PokerGame game){
        evaluateHand(this.getHand(), game.getBoard());
        float conf = 7.5f + this.handStrength;
        List<Action> lastActions = al.getRecentActions();

        for(Action a : lastActions){
            switch (a.getAction()){
                case PlayerAction.FOLD:
                    conf += aggression / 3f;
                    break;
                case PlayerAction.RAISE:
                    if(a.getBetAmount() > this.getChips() / aggression){
                        conf -= 2.75;
                    } else{
                        conf -= 4.75;
                    }
                    break;
                default:
                    if(this.handStrength < .8f){
                        conf += .4f;
                    } else{
                        conf -= .4f;
                    }
            }
        }
        this.confidence = Math.clamp(conf, 0f, 15f);
    }

    @Override
    public PlayerAction getPlayerAction(boolean canCheck, PokerGame game){
        int roll = (int) (Math.random() * 100);
        calculateConfidence(game.getActionLog(), game);
        float decisionPercent = calculateDecisionPercent();

        PlayerAction callCheck = canCheck ? PlayerAction.CHECK : PlayerAction.CALL;

        //No Aggression
        if(this.aggression == 0) {
            return PlayerAction.FOLD;
        }

        //Low Aggression
        if(aggression <= 3){
            return roll > decisionPercent ? PlayerAction.FOLD : callCheck;
        }

        //Medium Aggression
        if(aggression <= 6){
            return roll < decisionPercent ? callCheck : PlayerAction.FOLD;
        }

        //High Aggression
        if(aggression <= 8){
            return roll < decisionPercent  ? PlayerAction.RAISE : callCheck;
        }

        //Extreme Aggression
        if(aggression <= 10){
            return roll < decisionPercent ? PlayerAction.RAISE : callCheck;
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
