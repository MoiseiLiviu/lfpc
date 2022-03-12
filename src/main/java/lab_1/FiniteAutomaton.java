package lab_1;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FiniteAutomaton {


    private static List<String> Vn;
    public static List<State> states = new ArrayList<>();

    public static void initStates(String vn) {
        int a = vn.indexOf("Vn={");
        int b = vn.indexOf("},");

        Vn = Arrays
                .asList(vn.substring(a + 4, b)
                        .replaceAll("\\s+", "")
                        .trim().split(","));

        for (String c : Vn) {
            states.add(new State(c));
        }

        states.add(new State("Fin"));
    }

    public static void initAutomata(String str) {
        initStates(str);

        int a = str.indexOf("P= {") + 4;
        int b = str.lastIndexOf("}");

        str = str.substring(a, b).trim();
        String[] elements = str.split("\n");

        for (String c : elements) {
            String[] el = c.replaceAll("\\s+", "").split("->");
            State node = states
                    .stream()
                    .filter(f -> el[0].equals(f.getName()))
                    .collect(Collectors.toList()).get(0);
            String destName;
            String w;
            if (!el[1].matches(".*[A-Z].*")) {
                destName = "Fin";
                w = el[1];
            } else {
                destName = el[1].replaceAll("[^A-Z]", "");
                w = el[1].replaceAll(destName, "");
            }

            State dest = states
                    .stream().filter(f -> destName.equals(f.getName()))
                    .collect(Collectors.toList()).get(0);
            node.addTransition(w.toCharArray()[0], dest);
        }
    }

    public static List<State> getStates() {
        return states;
    }
}
