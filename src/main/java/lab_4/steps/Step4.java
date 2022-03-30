package lab_4.steps;

import lab_4.CFG;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Step4 {

    public static void removeInaccessibleSymbols(CFG cfg) {

        List<String> accessible = new ArrayList<>();
        accessible.add("S");
        checkAccessibility(cfg, new ArrayList<>(), accessible, "S");
        Iterator<String> iterator = cfg.getNonTerminalSet().iterator();
        while (iterator.hasNext()) {
            String symbol = iterator.next();
            if (!accessible.contains(symbol)) {
                cfg.removeSymbol(symbol);
                iterator.remove();
            }
        }
    }

    public static void checkAccessibility(CFG cfg, List<String> checked, List<String> accessible, String symbol) {

        checked.add(symbol);
        for (String s : cfg.getProductionRules().get(symbol)) {
            for (int i = 0; i < s.length(); i++) {
                String currChar = Character.toString(s.charAt(i));
                if (!accessible.contains(currChar))
                    accessible.add(currChar);
                if (cfg.isNonTerminal(currChar) && !checked.contains(currChar)) {
                    checkAccessibility(cfg, checked, accessible, currChar);
                }
            }
        }
    }
}
