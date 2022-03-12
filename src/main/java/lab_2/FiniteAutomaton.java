package lab_2;

import lab_2.utils.IOUtil;
import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
public class FiniteAutomaton {

    private String[] states;
    private String[] transVariables;
    private String[][] transTable;
    private Set<String> finalStates;

    public FiniteAutomaton(String filePath){
        String content = IOUtil.readLineByLine(filePath);
        initFA(content);
    }

    public FiniteAutomaton(){}

    public void initFA(String input) {

        int a = input.indexOf("Q={");
        int b = input.indexOf("};\nA");
        states = input
                .substring(a + 3, b)
                .split(",");

        a = input.indexOf("F={");
        b = input.indexOf("}");
        String finalState = input.substring(a + 3, b);
        finalStates = new HashSet<>();
        finalStates.addAll(Arrays.asList(finalState.split(",")));

        a = input.indexOf("A={");
        b = input.indexOf("};\nsigma");
        transVariables = input
                .substring(a + 3, b)
                .split(",");

        transTable = new String[states.length][transVariables.length];
        Arrays.stream(transTable).forEach(x -> Arrays.fill(x, ""));
        a = input.indexOf(".");
        String[] elements = input.substring(b + 3, a).split(";");
        initTransTable(elements);
    }

    private void initTransTable(String[] rules) {

        for (String el : rules) {
            int a;
            a = el.indexOf("q");
            String state1 = el.substring(a, a + 2);
            String transVar = el.substring(a + 3, a + 4);
            a = el.indexOf("=");
            String state2 = el.substring(a + 1, a + 3);

            int state = Arrays.asList(states).indexOf(state1);
            int trans = Arrays.asList(transVariables).indexOf(transVar);

            if (transTable[state][trans].equals("")) {
                transTable[state][trans] = transTable[state][trans].concat(state2);
            } else {
                transTable[state][trans] = transTable[state][trans].concat(" " + state2);
            }
        }
    }
}
