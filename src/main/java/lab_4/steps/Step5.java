package lab_4.steps;

import lab_4.CFG;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Step5 {

    private static final HashMap<String, String> substitutionMap = new HashMap<>();
    ;
    private static int terminalsIndex = 1;
    private static int nonTerminalsIndex = 1;

    public static void chomskyNormalization(CFG cfg) {

        ConcurrentHashMap<String, List<String>> productionRules = new ConcurrentHashMap<>(cfg.getProductionRules());
        for (Map.Entry<String, List<String>> entry : productionRules.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                String normalizedRule = normalizeRule(entry.getValue().get(i), cfg);
                entry.getValue().set(i, normalizedRule);
            }
        }
    }

    private static String normalizeRule(String derivation, CFG cfg) {

        if (shouldBeNormalized(derivation, cfg)) {
            StringBuilder stringBuilder = new StringBuilder(derivation);
            for (int i = 0; i < stringBuilder.length(); i++) {
                String c = String.valueOf(stringBuilder.charAt(i));
                if (cfg.isTerminal(c)) {
                    substituteOneTerminalWithNewSymbol(c, cfg, stringBuilder, i);
                    i++;
                } else {
                    int cnt = 1;
                    if (c.equals("X") || c.equals("Y")) {
                        c += String.valueOf(stringBuilder.charAt(i + cnt++));
                    }
                    if (i + cnt < stringBuilder.length()) {
                        String d = String.valueOf(stringBuilder.charAt(i + cnt++));
                        if (d.equals("X") || d.equals("Y")) {
                            d += String.valueOf(stringBuilder.charAt(i + cnt));
                            substituteTwoNonTerminalsWithNewSymbol(c, d, cfg, stringBuilder, i);
                            i++;
                        } else if (cfg.isNonTerminal(d)) {
                            substituteTwoNonTerminalsWithNewSymbol(c, d, cfg, stringBuilder, i);
                            i++;
                        }
                    }
                }
            }
            return normalizeRule(stringBuilder.toString(), cfg);
        } else return derivation;
    }

    private static void substituteOneTerminalWithNewSymbol(String c, CFG cfg, StringBuilder stringBuilder, int i) {

        addNewProductionRule(c, "X" + nonTerminalsIndex++, cfg);
        stringBuilder.replace(i, i + 1, substitutionMap.get(c));
    }

    private static void substituteTwoNonTerminalsWithNewSymbol(String c, String d, CFG cfg, StringBuilder stringBuilder, int i) {

        addNewProductionRule(c + d, "Y" + terminalsIndex++, cfg);
        stringBuilder.replace(i, i + (c+d).length(), substitutionMap.get(c + d));
    }

    private static void addNewProductionRule(String newSymbol, String substitutionSymbol, CFG cfg) {
        substitutionMap.putIfAbsent(newSymbol, substitutionSymbol);
        cfg.addSubstitutionRule(substitutionMap.get(newSymbol), newSymbol);
    }

    private static boolean derivationConsistsOfOneTerminal(String derivation, CFG cfg) {
        return (derivation.length() == 1 && !cfg.isNonTerminal(derivation.charAt(0)));
    }

    private static boolean derivationConsistsOfTwoNonTerminals(String derivation, CFG cfg) {

        int cnt = 0;
        for(int i = 0;i<derivation.length();i++){

            if(derivation.charAt(i)=='X' || derivation.charAt(i)=='Y'){
                i++;
                cnt++;
            } else if(cfg.isNonTerminal(derivation.charAt(i))){
                cnt++;
            } else return false;
            if(cnt > 2) return false;
        }
        return true;
    }

    private static boolean shouldBeNormalized(String derivation, CFG cfg) {
        return !(derivationConsistsOfOneTerminal(derivation, cfg) || derivationConsistsOfTwoNonTerminals(derivation, cfg));
    }
}
