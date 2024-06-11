package elecvrsn.GeneticBettas.model.modeldata;

import mokiyoki.enhancedanimals.model.modeldata.AnimalModelData;

public class BettaModelData extends AnimalModelData {
    public BettaPhenotype getPhenotype() {
        return (BettaPhenotype) this.phenotype;
    }
    public boolean isBubbling = false;
}
