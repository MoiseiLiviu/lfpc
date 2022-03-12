package lab_2;


import lab_2.utils.GraphUtil;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        FiniteAutomaton nfa = new FiniteAutomaton("src/main/java/lab_2/input.txt");
        GraphUtil.drawGraph(nfa, "src/main/resources/lab_2_NFA.png",true);
        FiniteAutomaton dfa = NfaToDfaConverter.convertNfaToDfa(nfa);
        GraphUtil.drawGraph(dfa, "src/main/resources/lab_2_DFA.png",false);
    }
}