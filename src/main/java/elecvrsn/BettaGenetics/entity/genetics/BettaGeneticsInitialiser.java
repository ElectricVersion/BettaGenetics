package elecvrsn.BettaGenetics.entity.genetics;

import elecvrsn.BettaGenetics.init.breeds.BettaBreeds;
import elecvrsn.BettaGenetics.util.AddonReference;
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
        //Iridescence [Turquoise <> Royal Blue > Steel Blue]
        autosomalGenes[0] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(3) + 1) : 1;
        autosomalGenes[1] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(3) + 1) : 1;

        //Spread Iridescence [wildtype < Spread]
        autosomalGenes[2] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[3] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        return new Genes(sexlinkedGenes, autosomalGenes);
    }
}
