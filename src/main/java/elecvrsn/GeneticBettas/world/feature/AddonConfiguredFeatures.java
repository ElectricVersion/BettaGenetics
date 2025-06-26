//package elecvrsn.GeneticBettas.world.feature;
//
//import elecvrsn.GeneticBettas.init.AddonBlocks;
//import net.minecraft.core.Holder;
//import net.minecraft.data.worldgen.features.FeatureUtils;
//import net.minecraft.data.worldgen.placement.PlacementUtils;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
//import net.minecraft.world.level.levelgen.feature.Feature;
//import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
//import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
//import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
//
//public class AddonConfiguredFeatures {
//    private static Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> aquaticPlantFeature(String name, Block block) {
//        return FeatureUtils.register("feature_"+name, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)));
//    }
//
//    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_DUCKWEED =
//            FeatureUtils.register("patch_duckweed", Feature.RANDOM_PATCH,
//                    new RandomPatchConfiguration(9, 2, 0, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
//                            new SimpleBlockConfiguration(BlockStateProvider.simple(AddonBlocks.DUCKWEED.get())))));
//
//    public static final Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> FEATURE_E_CORDIFOLIUS = aquaticPlantFeature("e_cordifolius", AddonBlocks.E_CORDIFOLIUS.get());
//    public static final Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> FEATURE_TALL_E_CORDIFOLIUS = aquaticPlantFeature("tall_e_cordifolius", AddonBlocks.TALL_E_CORDIFOLIUS.get());
//    public static final Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> FEATURE_A_BARTERI = aquaticPlantFeature("a_barteri", AddonBlocks.A_BARTERI.get());
//    public static final Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> FEATURE_VARIEGATED_A_BARTERI = aquaticPlantFeature("variegated_a_barteri", AddonBlocks.VARIEGATED_A_BARTERI.get());
//    public static final Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> FEATURE_V_AMERICANA = aquaticPlantFeature("v_americana", AddonBlocks.V_AMERICANA.get());
//
//}
