package lab_1;

import java.util.ArrayList;
import java.util.List;

public class State {
    private String name;
    private List<Transition> transitions;

    public State(String name) {
        this.name = name;
        transitions = new ArrayList<>();
    }

    public State isValid(char weight){
        for (Transition ed: transitions){
            if (ed.getRule()==weight) return ed.getDest();
        }
        return null;
    }

    public void addTransition(char weight, State vert){
        transitions.add(new Transition(weight, vert));
    }

    public String getName() {
        return name;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }
}
