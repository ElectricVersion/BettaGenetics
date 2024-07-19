package elecvrsn.GeneticBettas.model.modeldata;

import mokiyoki.enhancedanimals.model.modeldata.Phenotype;

public class BettaPhenotype implements Phenotype {
    public boolean dumbo = false;
    public boolean doubleTail = false;
    public float dorsalWidth = 1.0F;
    public BettaPhenotype(int[] gene, char uuid) {
        if (gene[56] == 2 && gene[57] == 2) {
            dumbo = true;
        }
        if (gene[62] == 2 || gene[63] == 2) {
            dorsalWidth = 1.25F;
            if (gene[62] == gene[63]) {
                doubleTail = true;
            }
        }
    }
}

