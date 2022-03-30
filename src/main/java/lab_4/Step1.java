package lab_4;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Step1 {


    public static void removeEpsilons(CFG cfg) {

        HashMap<String, List<String>> productionRules = cfg.getProductionRules();
        List<String> emptySymbols;
        do {
            emptySymbols = removeEmptySymbols(productionRules);
            addNewDerivations(emptySymbols, productionRules);
        } while (!emptySymbols.isEmpty());
    }

    private static void addNewDerivations(List<String> emptySymbols, HashMap<String, List<String>> productionRules){

        for (String emptySymbol : emptySymbols) {
            HashSet<String> newDerivations = new HashSet<>();
            for (Map.Entry<String, List<String>> entry : productionRules.entrySet()) {
                for (String derivation : entry.getValue()) {
                    getNewDerivationsWithoutEmptySymbol(derivation, newDerivations, emptySymbol.charAt(0));
                }
                for (String s : newDerivations) {
                    if (!productionRules.get(entry.getKey()).contains(s))
                        productionRules.get(entry.getKey()).add(s);
                }
                newDerivations = new HashSet<>();
            }
        }
    }

    private static List<String> removeEmptySymbols(HashMap<String, List<String>> productionRules) {

        List<String> emptySymbols = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : productionRules.entrySet()) {
            Iterator<String> iterator = entry.getValue().listIterator();
            while (iterator.hasNext()) {
                String derivation = iterator.next();
                if (derivation.equals("ε") || derivation.isEmpty()) {
                    emptySymbols.add(entry.getKey());
                    iterator.remove();
                }
            }
        }
        return emptySymbols;
    }

    private static void getNewDerivationsWithoutEmptySymbol(String current, Set<String> derivations, char c) {

        derivations.add(current);
        for (int i = 0; i < current.length(); i++) {
            if (current.charAt(i) == c)
                getNewDerivationsWithoutEmptySymbol(current.substring(0, i) + current.substring(i + 1), derivations, c);
        }
    }
}
