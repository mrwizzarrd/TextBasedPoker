package poker;

public class EvaluatedHand implements Comparable<EvaluatedHand> {

    private HandCategory category;
    private int[] tie_breakers;

    public EvaluatedHand(HandCategory cat, int... tie_br){
        this.category = cat;
        this.tie_breakers = tie_br;
    }

    public HandCategory getCategory(){
        return this.category;
    }

    public int[] getTieBreakers(){
        return this.tie_breakers;
    }

    @Override
    public int compareTo(EvaluatedHand other){
        int categoryDifference = this.category.ordinal() - other.category.ordinal();
        if(categoryDifference != 0){
            return categoryDifference;
        }

        for(int i = 0; i < 4; i++){
            int diff = this.tie_breakers[i] - other.tie_breakers[i];

            if(diff != 0){
                return diff;
            }
        }

        return 0;
    }
    
    @Override

    public String toString(){
        String handString = this.category.toString();
        return handString;
    }
}
