package elecvrsn.GeneticBettas.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static net.minecraft.data.worldgen.placement.VegetationPlacements.worldSurfaceSquaredWithCount;

public class AddonPlacedFeatures {
    public static final Holder<PlacedFeature> PATCH_DUCKWEED = PlacementUtils.register("patch_duckweed", AddonConfiguredFeatures.PATCH_DUCKWEED,
            worldSurfaceSquaredWithCount(1));
}
