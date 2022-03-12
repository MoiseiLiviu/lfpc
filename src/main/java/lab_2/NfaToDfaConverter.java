package lab_2;

import java.util.*;

public class NfaToDfaConverter {

    public static FiniteAutomaton convertNfaToDfa(FiniteAutomaton nfa) {

        List<String> newElements = new ArrayList<>();
        newElements.add(nfa.getStates()[0]);
        String[][] newTable = getNewTransitionTable(newElements, nfa);
        FiniteAutomaton dfa = new FiniteAutomaton();
        dfa.setTransVariables(nfa.getTransVariables());
        dfa.setStates(newElements.toArray(new String[0]));
        dfa.setTransTable(newTable);
        dfa.setFinalStates(getNewFinalStates(newElements, nfa));

        return dfa;
    }

    private static Set<String> getNewFinalStates(List<String> newElements, FiniteAutomaton nfa) {
        HashSet<String> finalStates = new HashSet<>();
        for (String state : newElements) {
            for (String fs : nfa.getFinalStates()) {
                if (state.contains(fs)) {
                    finalStates.add(state);
                }
            }
        }

        return finalStates;
    }

    private static String[][] getNewTransitionTable(List<String> newElements, FiniteAutomaton fa) {

        List<String[]> newTable = new ArrayList<>();

        int size = newElements.size();
        for (int i = 0 ; i < size; i++) {
            String[] product = findStates(newElements.get(i), fa);
            for (String pr : product) {
                if (!newElements.contains(pr)
                        && !pr.isEmpty()) {
                    newElements.add(pr);
                    size++;
                }
            }
            newTable.add(new String[fa.getTransVariables().length]);
            System.arraycopy(product, 0, newTable.get(i), 0, fa.getTransVariables().length);
        }

        String[][] res = new String[newTable.size()][fa.getTransVariables().length];
        for(int i = 0;i<res.length;i++){
            res[i] = newTable.get(i);
        }

        return res;
    }

    private static String[] findStates(String state, FiniteAutomaton fa) {

        String[] units = state.split("\\s+");
        String[] eachTrans = new String[fa.getTransVariables().length];
        Arrays.fill(eachTrans, "");
        for (String unit : units) {
            int x = Arrays.asList(fa.getStates()).indexOf(unit);
            for (int i = 0; i < eachTrans.length; i++) {
                eachTrans[i] += fa.getTransTable()[x][i] + " ";
            }
        }
        for (int i = 0; i < eachTrans.length; i++) {
            eachTrans[i] = eachTrans[i].trim();
            String[] divided = eachTrans[i].split("\\s+");
            divided = Arrays.stream(divided)
                    .distinct()
                    .toArray(String[]::new);
            eachTrans[i] = String.join(" ", divided);
        }
        return eachTrans;
    }
}
