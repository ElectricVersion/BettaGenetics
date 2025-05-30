package elecvrsn.GeneticBettas.world.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

import static net.minecraft.data.worldgen.placement.VegetationPlacements.worldSurfaceSquaredWithCount;
import static net.minecraft.tags.BlockTags.DIRT;

public class AddonPlacedFeatures {
    public static List<PlacementModifier> aquaticPlantPlacement(int rarity) {
        return List.of(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());
    }

    public static final Holder<PlacedFeature> PATCH_DUCKWEED = PlacementUtils.register("patch_duckweed", AddonConfiguredFeatures.PATCH_DUCKWEED,
            RarityFilter.onAverageOnceEvery(16), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

    public static final Holder<PlacedFeature> PATCH_E_CORDIFOLIUS = PlacementUtils.register("patch_e_cordifolius", AddonConfiguredFeatures.PATCH_E_CORDIFOLIUS, aquaticPlantPlacement(12));
    public static final Holder<PlacedFeature> PATCH_TALL_E_CORDIFOLIUS = PlacementUtils.register("patch_tall_e_cordifolius", AddonConfiguredFeatures.PATCH_TALL_E_CORDIFOLIUS, aquaticPlantPlacement(12));
    public static final Holder<PlacedFeature> PATCH_A_BARTERI = PlacementUtils.register("patch_a_barteri", AddonConfiguredFeatures.PATCH_A_BARTERI, aquaticPlantPlacement(12));
    public static final Holder<PlacedFeature> PATCH_VARIEGATED_A_BARTERI = PlacementUtils.register("patch_variegated_a_barteri", AddonConfiguredFeatures.PATCH_VARIEGATED_A_BARTERI, aquaticPlantPlacement(48));
    public static final Holder<PlacedFeature> PATCH_V_AMERICANA = PlacementUtils.register("patch_v_americana", AddonConfiguredFeatures.PATCH_V_AMERICANA, aquaticPlantPlacement(12));
}
