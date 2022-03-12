package lab_1;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

public class Main {

    public static void main(String[] args) throws IOException {

        String filePath = new File("src/main/java/lab_1/input.txt").getAbsolutePath();
        FiniteAutomaton.initAutomata(readLineByLine(filePath));
        Scanner sc = new Scanner(System.in);
        String word;
        word = sc.nextLine();

        State currentState = FiniteAutomaton.states.get(0);

        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (currentState.isValid(ch) != null) {
                currentState = currentState.isValid(ch);
            } else {
                break;
            }
            if (currentState == FiniteAutomaton.states.get(FiniteAutomaton.states.size() - 1)) {

                drawGraph();
                System.out.println("Accepted!");
                return;
            }
        }
        System.out.println("Rejected!");
    }


    public static void drawGraph() throws IOException {
        String name = "src/main/resources/lab_1.png";

        Graph g = graph(name).directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("arial"))
                .linkAttr().with("class", "link-class")
                .with(node(FiniteAutomaton.getStates().get(0).getName()).with(Color.GREEN));

        for (State state : FiniteAutomaton.getStates()) {
            if(state.getName().equals("Fin")) continue;
            for (Transition transition : state.getTransitions()){
                Node node = node(state.getName());
                if(!transition.getDest().getName().equals("Fin")){
                    node = node.link(to(node(transition.getDest().getName())).with(Label.of(String.valueOf(transition.getRule()))));}
                else node = node.with(Shape.DOUBLE_CIRCLE);
                g = g.with(
                        node
                );
            }
        }

        Graphviz.fromGraph(g).height(400).render(Format.PNG).toFile(new File(name));
    }

    private static String readLineByLine(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }
}
