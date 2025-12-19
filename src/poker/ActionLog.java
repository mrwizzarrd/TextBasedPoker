package poker;

import java.util.ArrayList;
import java.util.List;

public class ActionLog {
    private List<Action> actions = new ArrayList<>();
    private List<Action> recentActions = new ArrayList<>(); //latest action per player

    //========Constructor=============
    public ActionLog(){

    }

    //========Getters/Setters==========

    public List<Action> getActions() {
        return List.copyOf(this.actions);
    }

    public List<Action> getRecentActions(){
        return List.copyOf(this.recentActions);
    }

    //=======Add-Action=================
    public void addAction(Player p, PlayerAction action, int betAmount){
        Action act = new Action(p, action, betAmount);
        this.actions.add(act);
        this.recentActions.removeIf(a -> a.getActor() == p);
        this.recentActions.add(act);
    }
}
