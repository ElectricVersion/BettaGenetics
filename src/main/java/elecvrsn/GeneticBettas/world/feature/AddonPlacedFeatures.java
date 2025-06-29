package elecvrsn.GeneticBettas.world.feature;

import elecvrsn.GeneticBettas.util.AddonReference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class AddonPlacedFeatures {
    public static final ResourceKey<PlacedFeature> PLACED_DUCKWEED_KEY = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.tryBuild(AddonReference.MODID, "placed_duckweed"));
    public static final ResourceKey<PlacedFeature> PLACED_E_CORDIFOLIUS_KEY = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.tryBuild(AddonReference.MODID, "placed_e_cordifolius"));
    public static final ResourceKey<PlacedFeature> PLACED_TALL_E_CORDIFOLIUS_KEY = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.tryBuild(AddonReference.MODID, "placed_tall_e_cordifolius"));
    public static final ResourceKey<PlacedFeature> PLACED_A_BARTERI_KEY = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.tryBuild(AddonReference.MODID, "placed_a_barteri"));
    public static final ResourceKey<PlacedFeature> PLACED_VARIEGATED_A_BARTERI_KEY = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.tryBuild(AddonReference.MODID, "placed_variegated_a_barteri"));
    public static final ResourceKey<PlacedFeature> PLACED_V_AMERICANA_KEY = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.tryBuild(AddonReference.MODID, "placed_v_americana"));


    public static List<PlacementModifier> aquaticPlantPlacement(int rarity) {
        return List.of(CountPlacement.of(2), RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome());
    }

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(PLACED_DUCKWEED_KEY, new PlacedFeature(configuredFeatures.getOrThrow(AddonConfiguredFeatures.DUCKWEED_KEY),
                List.of(RarityFilter.onAverageOnceEvery(16), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
        context.register(PLACED_E_CORDIFOLIUS_KEY, new PlacedFeature(configuredFeatures.getOrThrow(AddonConfiguredFeatures.E_CORDIFOLIUS_KEY), aquaticPlantPlacement(4)));
        context.register(PLACED_TALL_E_CORDIFOLIUS_KEY, new PlacedFeature(configuredFeatures.getOrThrow(AddonConfiguredFeatures.TALL_E_CORDIFOLIUS_KEY), aquaticPlantPlacement(6)));
        context.register(PLACED_A_BARTERI_KEY, new PlacedFeature(configuredFeatures.getOrThrow(AddonConfiguredFeatures.A_BARTERI_KEY), aquaticPlantPlacement(4)));
        context.register(PLACED_VARIEGATED_A_BARTERI_KEY, new PlacedFeature(configuredFeatures.getOrThrow(AddonConfiguredFeatures.VARIEGATED_A_BARTERI_KEY), aquaticPlantPlacement(48)));
        context.register(PLACED_V_AMERICANA_KEY, new PlacedFeature(configuredFeatures.getOrThrow(AddonConfiguredFeatures.V_AMERICANA_KEY), aquaticPlantPlacement(6)));
    }
}
