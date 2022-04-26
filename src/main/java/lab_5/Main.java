package lab_5;

import java.io.IOException;

public class Main {

    private static final String inputPath = "src/main/java/lab_5/input.txt";

    public static void main(String[] args) throws IOException {

        Grammar grammar = new Grammar(inputPath);
        String input = "<ab-a*ab*->";
        System.out.println(new PrecedenceParser(grammar).parseInput(input));
    }
}
