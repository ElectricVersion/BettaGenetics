package elecvrsn.GeneticBettas.model.modeldata;

import mokiyoki.enhancedanimals.model.modeldata.Phenotype;

public class BettaPhenotype implements Phenotype {
    public boolean isFemale;
    public boolean dumbo = false;
    public float bodyScaleY = 1.0F;
    public boolean doubleTail = false;
    public float dorsalWidth = 1.0F;
    public float size = 1.0F;
    public BettaPhenotype(int[] gene, char uuid, boolean isFemale) {
        this.isFemale = isFemale;

        bodyScaleY = isFemale ? 1F : 0.95F;

        if (gene[56] == 2 && gene[57] == 2) {
            dumbo = true;
        }
        if (gene[62] == 2 || gene[63] == 2) {
            dorsalWidth = 1.0625F;
            if (gene[62] == gene[63]) {
                doubleTail = true;
                bodyScaleY *= 0.975F;
            }
        }
        if (gene[166] == 2 || gene[167] == 2) {
            size += gene[166] == gene[167] ? 0.375F : 0.25F;
        }

        size += (gene[168] - 1)*0.05F;
        size += (gene[169] - 1)*0.05F;

        size -= (gene[170] - 1)*0.05F;
        size -= (gene[171] - 1)*0.05F;
    }
}

