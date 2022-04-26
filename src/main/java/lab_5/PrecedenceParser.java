package lab_5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrecedenceParser {

    private Grammar grammar;
    private Map<String, List<String>> firstMap = new HashMap<>();
    private Map<String, List<String>> lastMap = new HashMap<>();
    private Map<String, HashMap<String, String>> precedenceMap = new HashMap<>();

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
            } else if(!firsts.contains(firstSymbol)){
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
            } else if(!lasts.contains(lastSymbol)){
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
                            precedenceMap.get(c1).put(last, GREATER);
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
            sb.insert(i+1, precedenceMap.get(c1).get(c2));
        }

        return sb.toString();
    }

    private String findProductionRuleByDerivation(String value) {

        for (Map.Entry<String, List<String>> rule : grammar.getProductionRules().entrySet()) {
            for (String derivation : rule.getValue()) {
                if (derivation.equals(value))
                    return rule.getKey();
            }
        }

        throw new RuntimeException("No rule was found for derivation : " + value);
    }

    public boolean parseInput(String input) {
        String inputWithPrecedenceOperators = getInputWithPrecedenceOperators(input);
        return checkString(inputWithPrecedenceOperators);
    }

    private boolean checkString(String str) {

        if (str.equals("<S>")) return true;

        StringBuilder sb = new StringBuilder(str);
        int equalIndex = str.indexOf("=");
        if (equalIndex != -1) {
            String left = str.substring(equalIndex - 1, equalIndex);
            String right = str.substring(equalIndex + 1, equalIndex + 2);
            String symbolOfTheProduction = findProductionRuleByDerivation(left + right);
            sb.replace(equalIndex-1, equalIndex + 2, symbolOfTheProduction);

            equalIndex-=2;
            if(equalIndex>1){
                sb.replace(equalIndex,equalIndex+1,precedenceMap.get(sb.substring(equalIndex-1,equalIndex)).get(symbolOfTheProduction));
            }
            if(equalIndex<sb.length()-3){
                sb.replace(equalIndex+2,equalIndex+3,precedenceMap.get(symbolOfTheProduction).get(sb.substring(equalIndex+3,equalIndex+4)));
            }
            return checkString(sb.toString());
        }

        Pattern pattern = Pattern.compile("<.>");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            int index = matcher.start();
            String symbolOfTheProduction = findProductionRuleByDerivation(sb.substring(index+1,index+2));
            sb.replace(index+1, index + 2, symbolOfTheProduction);
            if(index>1){
                sb.replace(index,index+1,precedenceMap.get(sb.substring(index-1,index)).get(symbolOfTheProduction));
            }
            if(index<sb.length()-3){
                sb.replace(index+2,index+3,precedenceMap.get(symbolOfTheProduction).get(sb.substring(index+3,index+4)));
            }
            return checkString(sb.toString());
        }

        return false;
    }

    public static void main(String[] args) {

        String str = "aaaa<b>";
        Pattern pattern = Pattern.compile("<[a-zA-Z]>");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            int index = matcher.start();
        }
    }
}