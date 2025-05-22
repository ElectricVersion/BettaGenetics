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
        return List.of(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.matchesTag(DIRT, new BlockPos(0, -1, 0)), BlockPredicate.matchesBlock(Blocks.WATER, BlockPos.ZERO))), BiomeFilter.biome());
//        return List.of(RarityFilter.onAverageOnceEvery(16), InSquarePlacement.spread(), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.matchesTag(DIRT, new BlockPos(0, -1, 0)), BlockPredicate.matchesBlock(Blocks.WATER, BlockPos.ZERO))), BiomeFilter.biome());
    }

    public static final Holder<PlacedFeature> PATCH_DUCKWEED = PlacementUtils.register("patch_duckweed", AddonConfiguredFeatures.PATCH_DUCKWEED,
            RarityFilter.onAverageOnceEvery(16), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

    public static final Holder<PlacedFeature> PATCH_E_CORDIFOLIUS = PlacementUtils.register("patch_e_cordifolius", AddonConfiguredFeatures.PATCH_E_CORDIFOLIUS, aquaticPlantPlacement(32));
}
