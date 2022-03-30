package lab_4;

import java.io.*;

public class Main {

    private static final String INPUT_PATH = "src/main/resources/input.txt";

    public static void main(String[] args) throws IOException {

        CFG cfg = new CFG(INPUT_PATH);
        System.out.println(cfg);
        Step1.removeEpsilons(cfg);
        System.out.println(cfg);
        Step2.removeRenamings(cfg);
        System.out.println(cfg);
        Step3.removeNonProductive(cfg);
        System.out.println(cfg);
        Step4.removeInaccessibleSymbols(cfg);
        System.out.println(cfg);
        Step5.chomskyNormalization(cfg);
        System.out.println(cfg);
    }
}
