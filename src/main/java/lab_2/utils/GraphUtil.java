package lab_2.utils;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import lab_2.FiniteAutomaton;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.*;
import static guru.nidi.graphviz.model.Factory.node;

public class GraphUtil {

    public static void drawGraph(FiniteAutomaton fa, String path, boolean nfa) throws IOException {

        Graph g = graph(path).directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("arial"))
                .linkAttr().with("class", "link-class")
                .with(node(fa.getStates()[0]).with(Color.GREEN));

        String[][] transTable = fa.getTransTable();
        String[] states = fa.getStates();
        for (int i = 0; i < transTable.length; i++) {
            for (int j = 0; j < transTable[i].length; j++) {
                if (!transTable[i][j].isEmpty())
                    if(nfa){
                        for(String unit : transTable[i][j].split(" ")){
                            g = g.with(
                                    node(states[i])
                                            .link(to(node(unit))
                                                    .with(Label.of(String.valueOf(fa.getTransVariables()[j]))))
                            );
                        }
                    } else {
                        g = g.with(
                                node(states[i])
                                        .link(to(node(transTable[i][j]))
                                                .with(Label.of(String.valueOf(fa.getTransVariables()[j]))))
                        );
                    }
            }
        }

        for (String s : fa.getFinalStates()) {
            g = g.with(
                    node(s).with(Shape.DOUBLE_CIRCLE)
            );
        }

        Graphviz.fromGraph(g).height(400).render(Format.PNG).toFile(new File(path));
    }
}
