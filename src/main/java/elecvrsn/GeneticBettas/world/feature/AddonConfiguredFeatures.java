package elecvrsn.GeneticBettas.world.feature;

import elecvrsn.GeneticBettas.init.AddonBlocks;
import elecvrsn.GeneticBettas.util.AddonReference;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class AddonConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> DUCKWEED_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.tryBuild(AddonReference.MODID, "duckweed"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> E_CORDIFOLIUS_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.tryBuild(AddonReference.MODID, "e_cordifolius"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_E_CORDIFOLIUS_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.tryBuild(AddonReference.MODID, "tall_e_cordifolius"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> A_BARTERI_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.tryBuild(AddonReference.MODID, "a_barteri"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> VARIEGATED_A_BARTERI_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.tryBuild(AddonReference.MODID, "variegated_a_barteri"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> V_AMERICANA_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.tryBuild(AddonReference.MODID, "v_americana"));

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        context.register(DUCKWEED_KEY, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(9, 2, 0, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(AddonBlocks.DUCKWEED.get()))))));
        aquaticPlantFeature(context, E_CORDIFOLIUS_KEY, AddonBlocks.E_CORDIFOLIUS.get());
        aquaticPlantFeature(context, TALL_E_CORDIFOLIUS_KEY, AddonBlocks.TALL_E_CORDIFOLIUS.get());
        aquaticPlantFeature(context, A_BARTERI_KEY, AddonBlocks.A_BARTERI.get());
        aquaticPlantFeature(context, VARIEGATED_A_BARTERI_KEY, AddonBlocks.VARIEGATED_A_BARTERI.get());
        aquaticPlantFeature(context, V_AMERICANA_KEY, AddonBlocks.V_AMERICANA.get());
    }

    private static void aquaticPlantFeature(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, Block block) {
        context.register(key, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block))));
    }


}
