package elecvrsn.GeneticBettas.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

public class AquaticPlantFeature extends Feature<SimpleBlockConfiguration> {
    public AquaticPlantFeature(Codec<SimpleBlockConfiguration> p_66768_) {
        super(p_66768_);
    }

    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
        SimpleBlockConfiguration simpleblockconfiguration = context.config();
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();
        BlockState blockstate = simpleblockconfiguration.toPlace().getState(context.random(), blockpos);
        if (blockstate.canSurvive(worldgenlevel, blockpos)) {
            if (blockstate.getBlock() instanceof DoublePlantBlock) {
                if (!worldgenlevel.isWaterAt(blockpos.above())) {
                    return false;
                }

                DoublePlantBlock.placeAt(worldgenlevel, blockstate, blockpos, 2);
            } else {
                worldgenlevel.setBlock(blockpos, blockstate, 2);
            }

            return true;
        } else {
            return false;
        }
    }
}