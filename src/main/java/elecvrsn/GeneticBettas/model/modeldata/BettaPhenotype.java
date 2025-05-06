package elecvrsn.GeneticBettas.model.modeldata;

import mokiyoki.enhancedanimals.model.modeldata.Phenotype;
import mokiyoki.enhancedanimals.model.util.ModelHelper;

import java.util.List;

public class BettaPhenotype implements Phenotype {
    public boolean isFemale;
    public boolean dumbo = false;
    public float bodyScaleY = 1.0F;
    public boolean doubleTail = false;
    public float dorsalWidth = 1.0F;
    public float size = 1.0F;
    public List<Float> pectoralFinScalings = null;
    public List<Float> dorsalFinScalings = null;
    public List<Float> ventralFinScalings = null;
    public List<Float> bottomFinFrontScalings = null;
    public List<Float> bottomFinBackScalings = null;
    public List<Float> tailFinScalings;
    public List<Float> bodyFrontScalings;

    public BettaPhenotype(int[] gene, char uuid, boolean isFemale) {
        this.isFemale = isFemale;

        bodyScaleY = isFemale ? 1F : 0.95F;

        float pectoralFinHeight = 0.75F;
        float pectoralFinWidth = 0.75F;
        if (gene[56] == 2 && gene[57] == 2) {
            dumbo = true;
            float dumboFinMult = (gene[240]+gene[241]-2)*0.25F;
            pectoralFinHeight = 0.8F+dumboFinMult;
            pectoralFinWidth = 0.75F+(dumboFinMult*0.75F);
        }

        if (gene[62] == 2 || gene[63] == 2) {
            dorsalWidth = 1.09375F;
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

        float finScaleMult = isFemale ? 0.75F : 1F;

        tailFinScalings = ModelHelper.createScalings(1F, 1F, 1F, 0F, 0F, (isFemale && !doubleTail) ? -1F/16F : 0F);
        pectoralFinScalings = ModelHelper.createScalings(1F, pectoralFinHeight, pectoralFinWidth, 0F, 0F, 0F);
        dorsalFinScalings = ModelHelper.createScalings(1F, isFemale ? 0.725F : 1F, finScaleMult*dorsalWidth, 0F, 0F, 0F);
        ventralFinScalings = ModelHelper.createScalings(1F, isFemale ? 0.725F : 1F, finScaleMult, 0F, 0F, 0F);
        bottomFinFrontScalings = ModelHelper.createScalings(1F, finScaleMult, isFemale ? 0.875F : 1F, 0F, 0F, 0F);
        bottomFinBackScalings = ModelHelper.createScalings(1F, isFemale ? 0.8F : 1F, isFemale ? 0.85F : 1F, 0F, 0F, 0F);
        bodyFrontScalings = ModelHelper.createScalings(1F, isFemale ? 1F : 0.925F, 1F, 0F, 0F, 0F);
    }
}

