package lab_5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrecedenceParser {

    private final Grammar grammar;
    private final Map<String, List<String>> firstMap = new HashMap<>();
    private final Map<String, List<String>> lastMap = new HashMap<>();
    private final Map<String, HashMap<String, String>> precedenceMap = new HashMap<>();

    private static final String EQUAL = "=";
    private static final String LESS = "<";
    private static final String GREATER = ">";

    PrecedenceParser(Grammar grammar) {
        this.grammar = grammar;
        populateFirstMap();
        populateLastMap();
        generatePrecedenceMap();
    }

    private void populateFirstMap() {

        for (String symbol : grammar.getNonTerminalSet()) {
            List<String> firsts = new ArrayList<>();
            getFirstSymbols(symbol, firsts);
            firstMap.put(symbol, firsts);
        }
    }

    private void populateLastMap() {

        for (String symbol : grammar.getNonTerminalSet()) {
            List<String> lasts = new ArrayList<>();
            getLastSymbols(symbol, lasts);
            lastMap.put(symbol, lasts);
        }
    }

    private void getFirstSymbols(String symbol, List<String> firsts) {

        for (String derivation : grammar.getProductionRules().get(symbol)) {
            String firstSymbol = derivation.substring(0, 1);
            if (grammar.isNonTerminal(firstSymbol) && !firsts.contains(firstSymbol)) {
                firsts.add(firstSymbol);
                getFirstSymbols(firstSymbol, firsts);
            } else if (!firsts.contains(firstSymbol)) {
                firsts.add(firstSymbol);
            }
        }
    }

    private void getLastSymbols(String symbol, List<String> lasts) {

        for (String derivation : grammar.getProductionRules().get(symbol)) {
            String lastSymbol = derivation.substring(derivation.length() - 1);
            if (grammar.isNonTerminal(lastSymbol) && !lasts.contains(lastSymbol)) {
                lasts.add(lastSymbol);
                getLastSymbols(lastSymbol, lasts);
            } else if (!lasts.contains(lastSymbol)) {
                lasts.add(lastSymbol);
            }
        }
    }

    private void generatePrecedenceMap() {

        for (Map.Entry<String, List<String>> rule : grammar.getProductionRules().entrySet()) {
            for (String derivation : rule.getValue()) {
                for (int i = 0; i < derivation.length() - 1; i++) {
                    String c1 = derivation.substring(i, i + 1);
                    String c2 = derivation.substring(i + 1, i + 2);
                    precedenceMap.putIfAbsent(c1, new HashMap<>());
                    precedenceMap.get(c1).putIfAbsent(c2, EQUAL);
                    if (grammar.isNonTerminal(c2)) {
                        for (String first : firstMap.get(c2)) {
                            precedenceMap.get(c1).put(first, LESS);
                        }
                    }
                    if (grammar.isTerminal(c2) && grammar.isNonTerminal(c1)) {
                        for (String last : lastMap.get(c1)) {
                            precedenceMap.putIfAbsent(last, new HashMap<>());
                            precedenceMap.get(last).put(c2, GREATER);
                        }
                    }
                    if (grammar.isNonTerminal(c1) && grammar.isNonTerminal(c2)) {
                        for (String last : lastMap.get(c1)) {
                            for (String first : firstMap.get(c2)) {
                                if (grammar.isTerminal(first)) {
                                    precedenceMap.putIfAbsent(last, new HashMap<>());
                                    precedenceMap.get(last).put(first, GREATER);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private String getInputWithPrecedenceOperators(String input) {

        StringBuilder sb = new StringBuilder(input);
        for (int i = 1; i < sb.length() - 2; i += 2) {
            String c1 = sb.substring(i, i + 1);
            String c2 = sb.substring(i + 1, i + 2);
            sb.insert(i + 1, precedenceMap.get(c1).get(c2));
        }

        System.out.println("Input with precedence operators: " + sb.toString());
        return sb.toString();
    }

    private List<String> findProductionRuleByDerivation(String value) {

        List<String> arr = new ArrayList<>();
        for (Map.Entry<String, List<String>> rule : grammar.getProductionRules().entrySet()) {
            for (String derivation : rule.getValue()) {
                if (derivation.equals(value))
                    arr.add(rule.getKey());
            }
        }
        return arr;
    }

    public boolean parseInput(String input) {
        String inputWithPrecedenceOperators = getInputWithPrecedenceOperators(input);
        return checkString(inputWithPrecedenceOperators);
    }

    private boolean checkString(String str) {

        System.out.println(str);
        if (str.equals("<S>")) return true;

        try {
            List<String> list = new ArrayList<>();
            substituteSymbols(str, list);
            boolean shouldContinue = false;
            for (String s : list) {
                List<String> list1 = new ArrayList<>();
                shouldContinue = shouldContinue || substituteEquals(s, list1);
                if (shouldContinue) {
                    for (String s1 : list1) {
                        if (checkString(s1)) return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    private void substituteSymbols(String input, List<String> toBeProcessed) {

        StringBuilder sb = new StringBuilder(input);

        Pattern pattern = Pattern.compile("<.>");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            int index = matcher.start();
            List<String> symbolsOfTheProduction = findProductionRuleByDerivation(sb.substring(index + 1, index + 2));
            for (String symbolOfTheProduction : symbolsOfTheProduction) {
                try {
                    StringBuilder sb1 = new StringBuilder(sb.toString());
                    sb1.replace(index + 1, index + 2, symbolOfTheProduction);
                    if (index > 1) {
                        sb1.replace(index, index + 1, precedenceMap.get(sb1.substring(index - 1, index)).get(symbolOfTheProduction));
                    }
                    if (index < sb1.length() - 3) {
                        sb1.replace(index + 2, index + 3, precedenceMap.get(symbolOfTheProduction).get(sb1.substring(index + 3, index + 4)));
                    }
                    substituteSymbols(sb1.toString(), toBeProcessed);
                } catch (Exception e) {
                    //ignored
                }
            }
        } else {
            toBeProcessed.add(input);
        }
    }

    private boolean substituteEquals(String str, List<String> l) {

        try {
            StringBuilder sb = new StringBuilder(str);
            int d = 0;
            while (true) {
                int equalIndex = str.substring(d).indexOf("=") + d;
                int offset = equalIndex+2;
                StringBuilder toBeReplaced = new StringBuilder();
                if (equalIndex != -1) {
                    d = equalIndex + 1;
                    toBeReplaced.append(str,equalIndex+1,equalIndex+2);
                    while (str.substring(offset).indexOf("=") == 0) {
                        toBeReplaced.append(str, offset + 1, offset + 2);
                        offset += 2;
                    }
                    String left = str.substring(equalIndex - 1, equalIndex);
                    List<String> symbolsOfTheProduction;
                    if (toBeReplaced.toString().isEmpty()) {
                        d = offset -1;
                        String right = str.substring(equalIndex + 1, equalIndex + 2);
                        symbolsOfTheProduction = findProductionRuleByDerivation(left + right);
                    } else {
                        symbolsOfTheProduction = findProductionRuleByDerivation(left + toBeReplaced);
                    }

                    if (symbolsOfTheProduction.isEmpty()) {
                        continue;
                    }
                    for (String symbolOfTheProduction : symbolsOfTheProduction) {
                        try {
                            StringBuilder sb1 = new StringBuilder(sb.toString());
                            sb1.replace(equalIndex - 1, offset , symbolOfTheProduction);
                            equalIndex -= 2;
                            if (equalIndex > 1) {
                                sb1.replace(equalIndex, equalIndex + 1, precedenceMap.get(sb1.substring(equalIndex - 1, equalIndex)).get(symbolOfTheProduction));
                            }
                            if (equalIndex < sb1.length() - 3) {
                                sb1.replace(equalIndex + 2, equalIndex + 3, precedenceMap.get(symbolOfTheProduction).get(sb1.substring(equalIndex + 3, equalIndex + 4)));
                            }
                            substituteEquals(sb1.toString(), l);
                        } catch (Exception e) {
                            //ignored
                        }
                    }
                    return true;
                } else {
                    l.add(str);
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}
