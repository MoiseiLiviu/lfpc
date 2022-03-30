package lab_4;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Step2 {

    public static void removeRenamings(CFG cfg) {

        HashMap<String, List<String>> productionRules = cfg.getProductionRules();
        for (Map.Entry<String, List<String>> entry : productionRules.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                String derivesInto = entry.getValue().get(i);
                if (derivesInto.length() == 1 && cfg.isNonTerminal(derivesInto)) {
                    productionRules.get(derivesInto).stream().filter(e -> !entry.getValue().contains(e)).forEach(e -> entry.getValue().add(e));
                    productionRules.get(entry.getKey()).remove(i);
                }
            }
        }
    }
}
