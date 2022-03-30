package lab_4;

import lab_4.steps.*;

public class CNFConverter {

    public static void convert(CFG cfg){

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
