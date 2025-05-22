package elecvrsn.GeneticBettas.world.feature;

import elecvrsn.GeneticBettas.init.AddonBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.material.Fluids;

public class AddonConfiguredFeatures {
    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_DUCKWEED =
            FeatureUtils.register("patch_duckweed", Feature.RANDOM_PATCH,
                    new RandomPatchConfiguration(9, 2, 0, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                            new SimpleBlockConfiguration(BlockStateProvider.simple(AddonBlocks.DUCKWEED.get())))));
    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_E_CORDIFOLIUS =
            FeatureUtils.register("patch_e_cordifolius", Feature.RANDOM_PATCH,
                    new RandomPatchConfiguration(4, 2, 0, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                            new SimpleBlockConfiguration(BlockStateProvider.simple(AddonBlocks.E_CORDIFOLIUS.get())))));

}
