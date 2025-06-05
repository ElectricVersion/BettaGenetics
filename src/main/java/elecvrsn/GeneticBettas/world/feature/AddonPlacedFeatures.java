package elecvrsn.GeneticBettas.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class AddonPlacedFeatures {
    public static List<PlacementModifier> aquaticPlantPlacement(int rarity) {
        return List.of(CountPlacement.of(2), RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome());
    }

    public static final Holder<PlacedFeature> PATCH_DUCKWEED = PlacementUtils.register("patch_duckweed", AddonConfiguredFeatures.PATCH_DUCKWEED,
            RarityFilter.onAverageOnceEvery(16), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

    public static final Holder<PlacedFeature> PATCH_E_CORDIFOLIUS = PlacementUtils.register("feature_e_cordifolius", AddonConfiguredFeatures.FEATURE_E_CORDIFOLIUS, aquaticPlantPlacement(4));
    public static final Holder<PlacedFeature> PATCH_TALL_E_CORDIFOLIUS = PlacementUtils.register("feature_tall_e_cordifolius", AddonConfiguredFeatures.FEATURE_TALL_E_CORDIFOLIUS, aquaticPlantPlacement(6));
    public static final Holder<PlacedFeature> PATCH_A_BARTERI = PlacementUtils.register("feature_a_barteri", AddonConfiguredFeatures.FEATURE_A_BARTERI, aquaticPlantPlacement(4));
    public static final Holder<PlacedFeature> PATCH_VARIEGATED_A_BARTERI = PlacementUtils.register("feature_variegated_a_barteri", AddonConfiguredFeatures.FEATURE_VARIEGATED_A_BARTERI, aquaticPlantPlacement(48));
    public static final Holder<PlacedFeature> PATCH_V_AMERICANA = PlacementUtils.register("feature_v_americana", AddonConfiguredFeatures.FEATURE_V_AMERICANA, aquaticPlantPlacement(6));
}
