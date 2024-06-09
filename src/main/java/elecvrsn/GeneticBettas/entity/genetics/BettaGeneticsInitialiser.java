package elecvrsn.GeneticBettas.entity.genetics;

import elecvrsn.GeneticBettas.init.breeds.BettaBreeds;
import elecvrsn.GeneticBettas.util.AddonReference;
import mokiyoki.enhancedanimals.config.EanimodCommonConfig;
import mokiyoki.enhancedanimals.entity.genetics.AbstractGeneticsInitialiser;
import mokiyoki.enhancedanimals.util.Breed;
import mokiyoki.enhancedanimals.util.Genes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BettaGeneticsInitialiser extends AbstractGeneticsInitialiser {
    //Have to redeclare WTC for some reason, probably because addon?
    int WTC = EanimodCommonConfig.COMMON.wildTypeChance.get();
    public BettaGeneticsInitialiser() {
        this.breeds.add(BettaBreeds.GENERIC);
    }

    List<Breed> breeds = new ArrayList<>();

    public Genes generateNewGenetics(LevelAccessor world, BlockPos pos, boolean generateBreed) {
        return super.generateNewGenetics(world, pos, generateBreed, this.breeds);
    }

    public Genes generateWithBreed(LevelAccessor world, BlockPos pos, String breed) {
        return super.generateWithBreed(world, pos, this.breeds, breed);
    }
    @Override
    public Genes generateLocalWildGenetics(Holder<Biome> biomeHolder, boolean isFlat) {
        int[] sexlinkedGenes = new int[AddonReference.BETTA_SEXLINKED_GENES_LENGTH];
        int[] autosomalGenes = new int[AddonReference.BETTA_AUTOSOMAL_GENES_LENGTH];
        Biome biome = biomeHolder.value();
        //Temporary Filler WTs
        for (int i = 0; i < AddonReference.BETTA_AUTOSOMAL_GENES_LENGTH; i++) {
            autosomalGenes[i] = 1;
        }
        //Iridescence [Turquoise <> Steel Blue]
        autosomalGenes[0] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[1] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Spread Iridescence [wildtype < Spread]
        autosomalGenes[2] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[3] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Masked Iridescence (modifies spread) [Wildtype <> Masked]
        autosomalGenes[4] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[5] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Melano Black [Wildtype > melano]
        //Homozygous melano causes infertility on females
        autosomalGenes[6] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[7] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Laced Black [Wildtype > laced]
        //Supposedly prevents the infertility when paired with melano. somehow.
        autosomalGenes[8] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[9] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Cambodian [Wildtype > cambodian]
        autosomalGenes[10] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[11] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Extended Red [wildtype < Extended]
        autosomalGenes[12] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[13] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Blonde [Wildtype > blonde]
        autosomalGenes[14] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[15] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Red Mask [Wildtype <> Mask]
        autosomalGenes[16] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[17] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Butterfly [wildtype < Butterfly]
        autosomalGenes[18] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[19] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Butterfly Level 1 [Wildtype <> Higher]
        autosomalGenes[20] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[21] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Butterfly Level 2 [wildtype < Higher]
        autosomalGenes[22] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[23] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Body Iridescence Level [Wildtype > higher]
        autosomalGenes[24] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[25] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[26] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[27] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Fin Iridescence Level [Wildtype > higher]
        autosomalGenes[28] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[29] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[30] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[31] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        return new Genes(sexlinkedGenes, autosomalGenes);
    }
}
