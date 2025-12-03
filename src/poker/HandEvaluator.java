package poker;

import java.util.ArrayList;

public class HandEvaluator {
    public static EvaluatedHand evaluateHand(PlayerHand hand, CommunityHand board){

        HandCategory best = null;

        boolean pair = false;
        boolean trips = false;
        boolean quads = false;

        boolean isRoyalFlush = false;
        boolean isStraightFlush = false;
        boolean isFourKind = false;
        boolean isFullHouse = false;
        boolean isStraight = false;
        boolean isFlush = false;
        boolean isThreeKind = false;
        boolean isTwoPair = false;
        boolean isOnePair = false;

        int pairCount = 0;
        int tripsCount = 0;

        ArrayList<HandCategory> possibleCategories = new ArrayList<>();

        possibleCategories.add(HandCategory.ROYAL_FLUSH);
        possibleCategories.add(HandCategory.STRAIGHT_FLUSH);
        possibleCategories.add(HandCategory.FOUR_KIND);
        possibleCategories.add(HandCategory.FULL_HOUSE);
        possibleCategories.add(HandCategory.FLUSH);
        possibleCategories.add(HandCategory.STRAIGHT);
        possibleCategories.add(HandCategory.THREE_KIND);
        possibleCategories.add(HandCategory.TWO_PAIR);
        possibleCategories.add(HandCategory.ONE_PAIR);
        possibleCategories.add(HandCategory.HIGH);

        ArrayList<Card> cards = new ArrayList<>();
        
        //Add players hand
        for(Card c : hand.cards){
            cards.add(c);
        }

        //Add community hand
        for(Card c : board.cards){
            cards.add(c);
        }

        //Count Suit Freq
        int[] suitFreq = new int[4];
        for(Card c : cards){
            suitFreq[c.getSuit().ordinal()]++;
        }
        //Count Ranks
        int[] rankFreq = new int[13];
        for(Card c : cards){
            rankFreq[c.getValue().ordinal()]++;
        }

        for (int i : rankFreq) {
            if(i == 2){
                pair = true;
                pairCount++;
            }
            if(i == 3){
                trips = true;
                tripsCount++;
            }
            if(i == 4){
                quads = true;
            }
        }

        for (int i : suitFreq) {
            if(i >= 5){
                isFlush = true;
            }
        }

        int straightHigh = -1;
        int consecutive = 0;
        for(int r = 0; r < 13; r++){
            if(rankFreq[r] > 0){
                consecutive++;
                if(consecutive >= 5){
                    isStraight = true;
                    straightHigh = r;
                }
            } else{
                consecutive = 0;
            }
        }
        if(rankFreq[12] > 0 && 
           rankFreq[0] > 0 && 
           rankFreq[1] > 0 && 
           rankFreq[2] > 0 && 
           rankFreq[3] > 0){
            straightHigh = 3;
            isStraight = true;
           }

        int sfHigh = -1;
        if(isStraight && isFlush){
            int flushSuit = -1;
            for(int s = 0; s < 4; s++){
                if(suitFreq[s] >= 5){
                    flushSuit = s;
                    break;
                }
            }
        

            if(flushSuit != -1){
                ArrayList<Card> suited = new ArrayList<>();

                for (Card c : cards) {
                    if(c.getSuit().ordinal() == flushSuit){
                        suited.add(c);
                    }
                }

                int[] flushRankFreq = new int[13];

                for (Card c : suited) {
                    flushRankFreq[(c.getValue().ordinal())]++;
                }

                boolean sf = false;
                int cons = 0;

                for(int r = 0; r < 13; r++){
                    if(flushRankFreq[r] > 0){
                        cons++;

                        if(cons >= 5){
                            sf = true;
                            sfHigh = r;
                        }
                    } else{
                        cons = 0;
                    }
                }   
                if(flushRankFreq[12] > 0 &&
                    flushRankFreq[0] > 0 &&
                    flushRankFreq[1] > 0 &&
                    flushRankFreq[2] > 0 &&
                    flushRankFreq[3] > 0
                ){
                    sfHigh = 3;
                    sf = true;
                }
                isStraightFlush = sf;
            }
        }

        if(pair){
            isOnePair = true;
        }
        if(pairCount >= 2){
            isTwoPair = true;
        }
        if(trips){
            isThreeKind = true;
        }
        if(pair && trips || tripsCount >= 2){
            isFullHouse = true;
        }
        if(quads){
            isFourKind = true;
        }


//possibility (RARE) of a Royal Straight Flush
        if(sfHigh == 12 && isStraightFlush){
            isRoyalFlush = true;
        }


        if(!isRoyalFlush){
            possibleCategories.remove(HandCategory.ROYAL_FLUSH);
        }
        if(!isStraightFlush){
            possibleCategories.remove(HandCategory.STRAIGHT_FLUSH);
        }
        if(!isFourKind){
            possibleCategories.remove(HandCategory.FOUR_KIND);
        }
        if(!isFullHouse){
            possibleCategories.remove(HandCategory.FULL_HOUSE);
        }
        if(!isFlush){
            possibleCategories.remove(HandCategory.FLUSH);
        }
        if(!isStraight){
            possibleCategories.remove(HandCategory.STRAIGHT);
        }
        if(!isThreeKind){
            possibleCategories.remove(HandCategory.THREE_KIND);
        }
        if(!isTwoPair){
            possibleCategories.remove(HandCategory.TWO_PAIR);
        }
        if(!isOnePair){
            possibleCategories.remove(HandCategory.ONE_PAIR);
        }

        for(HandCategory cat : HandCategory.values()){
            if(possibleCategories.contains(cat)){
                best = cat;
                break;
            }
        }

        return new EvaluatedHand(best, 0);

    }
}
