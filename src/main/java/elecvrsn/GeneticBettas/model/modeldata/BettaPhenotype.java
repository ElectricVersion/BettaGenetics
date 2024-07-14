package elecvrsn.GeneticBettas.model.modeldata;

import mokiyoki.enhancedanimals.model.modeldata.Phenotype;

public class BettaPhenotype implements Phenotype {
    public boolean dumbo = false;
    public BettaPhenotype(int[] gene, char uuid) {
        if (gene[56] == 2 && gene[57] == 2) {
            dumbo = true;
        }
    }
}

