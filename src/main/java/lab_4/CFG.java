package lab_4;

import lombok.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Data
public class CFG {

    private String startingSymbol;
    private HashSet<String> nonTerminalSet = new HashSet<>();
    private HashSet<String> terminalSet = new HashSet<>();
    private HashMap<String, List<String>> productionRules = new HashMap<>();

    public CFG(String filePath) throws IOException {
        init(filePath);
    }

    public void removeSymbol(String symbol) {

        Iterator<Map.Entry<String, List<String>>> it = productionRules.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            if (entry.getKey().equals(symbol)) {
                it.remove();
                continue;
            }
            entry.getValue().removeIf(derivation -> derivation.contains(symbol));
        }
    }

    public void init(String pathName) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(pathName))) {
            String line;
            while ((line = br.readLine()) != null) {
                switch (line.charAt(0)) {
                    case 'G':
                        startingSymbol = Character.toString(line.charAt(line.indexOf(')') - 1));
                        break;
                    case 'V':
                        populateTerminalAndNonTerminalSymbols(line);
                        break;
                    default:
                        populateProductionRules(line);
                        break;
                }
            }
        }
    }

    private void populateTerminalAndNonTerminalSymbols(String line) {

        String[] splitSymbols = line.substring(line.indexOf('{') + 1, line.length() - 1).split("[\\s,]+");
        if (line.charAt(1) == 'n' || line.charAt(1) == 'N') {
            for (String s : splitSymbols) {
                nonTerminalSet.add(s.trim());
            }
        } else if (line.charAt(1) == 't' || line.charAt(1) == 'T') {
            for (String s : splitSymbols) {
                terminalSet.add(s.trim());
            }
        }
    }

    private void populateProductionRules(String line) {

        if (line.contains("->")) {
            String[] transitionSymbols;
            if (Character.isDigit(line.charAt(0)))
                transitionSymbols = line.substring(line.indexOf('.') + 1).split("[->]+");
            else transitionSymbols = line.split("[->]+");

            String first = transitionSymbols[0].trim();
            String second = transitionSymbols[1].trim();

            if (!productionRules.containsKey(first)) {
                List<String> tempList = new ArrayList<>();
                tempList.add(second);
                productionRules.put(first, tempList);
            } else {
                productionRules.get(first).add(second);
            }
        }
    }

    public void addSubstitutionRule(String symbol, String derivation){

        nonTerminalSet.add(symbol);
        productionRules.putIfAbsent(symbol, Collections.singletonList(derivation));
    }

    public boolean isNonTerminal(String checkSymbol) {
        return nonTerminalSet.contains(checkSymbol);
    }

    public boolean isNonTerminal(char c){
        return isNonTerminal(String.valueOf(c));
    }

    @Override
    public String toString() {
        return "Grammar{" +
                "startingSymbol='" + startingSymbol + '\'' +
                ", nonTerminalSet=" + nonTerminalSet +
                ", terminalSet=" + terminalSet +
                ", productionRules=" + productionRules +
                '}';
    }
}
