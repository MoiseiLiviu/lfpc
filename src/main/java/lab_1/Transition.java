package lab_1;

public class Transition {
    char rule;
    private State dest;

    public Transition(char rule, State dest) {
        this.rule = rule;
        this.dest = dest;
    }

    public char getRule() {
        return rule;
    }

    public State getDest() {
        return dest;
    }
}
