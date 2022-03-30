package lab_4;

import java.io.*;

public class Main {

    private static final String INPUT_PATH = "src/main/resources/input.txt";

    public static void main(String[] args) throws IOException {

        CFG cfg = new CFG(INPUT_PATH);
        CNFConverter.convert(cfg);
    }
}
