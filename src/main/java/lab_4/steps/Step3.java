package lab_4.steps;

import lab_4.CFG;

import java.util.*;

public class Step3 {
    public static void removeNonProductive(CFG cfg){

        HashMap<String, List<String>> productionRules = cfg.getProductionRules();
        List<String> productive = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : productionRules.entrySet()) {
            if(!productive.contains(entry.getKey())) {
                checkProductive(cfg, productive, new ArrayList<>(), entry.getKey());
            }
        }
        Iterator<String> iterator = cfg.getNonTerminalSet().iterator();
        while (iterator.hasNext()) {
            String symbol = iterator.next();
            if (!productive.contains(symbol)) {
                cfg.removeSymbol(symbol);
                iterator.remove();
            }
        }
    }

    public static boolean checkProductive(CFG grammar, List<String> productive, List<String> checked, String symbol){

        List<String> symbolDerivations = grammar.getProductionRules().get(symbol);
        checked.add(symbol);
        derivations:
        for (String symbolDerivation : symbolDerivations) {
            for (int j = 0; j < symbolDerivation.length(); j++) {
                String currChar = Character.toString(symbolDerivation.charAt(j));
                if (grammar.isNonTerminal(currChar)) {
                    if (!(productive.contains(currChar) ||
                            (!checked.contains(currChar) && checkProductive(grammar, productive, checked, currChar))))
                        continue derivations;
                }
            }
            productive.add(symbol);
            return true;
        }
        return false;
    }
}
