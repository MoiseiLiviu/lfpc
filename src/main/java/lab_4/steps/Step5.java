package lab_4.steps;

import lab_4.CFG;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Step5 {

    private static final HashMap<String, String> substitutionMap = new HashMap<>();;
    private static char substitutionCharacter = 'F';

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
                if (!cfg.isNonTerminal(c)) {
                    substituteOneTerminalWithNewSymbol(c, cfg, stringBuilder, i);
                } else if (i + 1 < stringBuilder.length()) {
                    String d = String.valueOf(stringBuilder.charAt(i+1));
                    if (cfg.isNonTerminal(d)) {
                        i++;
                        substituteTwoNonTerminalsWithNewSymbol(c, d, cfg, stringBuilder, i);
                    }
                }
            }
            return normalizeRule(stringBuilder.toString(), cfg);
        } else return derivation;
    }

    private static void substituteOneTerminalWithNewSymbol(String c, CFG cfg, StringBuilder stringBuilder, int i) {

        addNewProductionRule(c,cfg);
        stringBuilder.replace(i, i + 1, substitutionMap.get(c));
    }

    private static void substituteTwoNonTerminalsWithNewSymbol(String c, String d, CFG cfg, StringBuilder stringBuilder, int i) {

        addNewProductionRule(c+d,cfg);
        stringBuilder.replace(i, i + 2, substitutionMap.get(c + d));
    }

    private static void addNewProductionRule(String newSymbol, CFG cfg){
        substitutionMap.putIfAbsent(newSymbol, String.valueOf(++substitutionCharacter));
        cfg.addSubstitutionRule(substitutionMap.get(newSymbol), newSymbol);
    }

    private static boolean derivationConsistsOfOneTerminal(String derivation, CFG cfg) {
        return (derivation.length() == 1 && !cfg.isNonTerminal(derivation.charAt(0)));
    }

    private static boolean derivationConsistsOfTwoNonTerminals(String derivation, CFG cfg) {
        return (derivation.length() == 2 && cfg.isNonTerminal(derivation.charAt(0)) && cfg.isNonTerminal(derivation.charAt(1)));
    }

    private static boolean shouldBeNormalized(String derivation, CFG cfg) {
        return !(derivationConsistsOfOneTerminal(derivation, cfg) || derivationConsistsOfTwoNonTerminals(derivation, cfg));
    }
}
