package elecvrsn.BettaGenetics.entity.genetics;

import elecvrsn.BettaGenetics.init.breeds.BettaBreeds;
import mokiyoki.enhancedanimals.entity.genetics.AbstractGeneticsInitialiser;
import mokiyoki.enhancedanimals.util.Breed;
import mokiyoki.enhancedanimals.util.Genes;
import elecvrsn.BettaGenetics.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class BettaGeneticsInitialiser extends AbstractGeneticsInitialiser {

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
    public Genes generateLocalWildGenetics(Holder<Biome> biomeHolder, boolean isFlat) {
        int[] sexlinkedGenes = new int[Reference.BETTA_SEXLINKED_GENES_LENGTH];
        int[] autosomalGenes = new int[Reference.BETTA_AUTOSOMAL_GENES_LENGTH];
        Biome biome = biomeHolder.value();

        return new Genes(sexlinkedGenes, autosomalGenes);
    }
}
