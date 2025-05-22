package elecvrsn.GeneticBettas.world.feature;

import elecvrsn.GeneticBettas.init.AddonBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.material.Fluids;

public class AddonConfiguredFeatures {
    private static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> aquaticPlantFeature(String name, Block block) {
        return FeatureUtils.register("patch_"+name, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(5, 2, 0, PlacementUtils.filtered(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(block)), BlockPredicate.matchesFluid(Fluids.WATER, BlockPos.ZERO))));
    }

    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_DUCKWEED =
            FeatureUtils.register("patch_duckweed", Feature.RANDOM_PATCH,
                    new RandomPatchConfiguration(9, 2, 0, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                            new SimpleBlockConfiguration(BlockStateProvider.simple(AddonBlocks.DUCKWEED.get())))));

    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_E_CORDIFOLIUS = aquaticPlantFeature("e_cordifolius", AddonBlocks.E_CORDIFOLIUS.get());
    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_TALL_E_CORDIFOLIUS = aquaticPlantFeature("tall_e_cordifolius", AddonBlocks.TALL_E_CORDIFOLIUS.get());
    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_A_BARTERI = aquaticPlantFeature("a_barteri", AddonBlocks.A_BARTERI.get());
    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_VARIEGATED_A_BARTERI = aquaticPlantFeature("variegated_a_barteri", AddonBlocks.VARIEGATED_A_BARTERI.get());
    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_V_AMERICANA = aquaticPlantFeature("v_americana", AddonBlocks.V_AMERICANA.get());

}
