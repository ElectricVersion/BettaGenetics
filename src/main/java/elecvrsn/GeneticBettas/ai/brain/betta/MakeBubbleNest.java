package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import mokiyoki.enhancedanimals.init.ModMemoryModuleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;


public class MakeBubbleNest extends Behavior<EnhancedBetta> {

    boolean nestMade = false;
    public MakeBubbleNest() {
        //Condition, Min Duration, Max Duration
        super(ImmutableMap.of(
                ModMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                ModMemoryModuleTypes.FOCUS_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                AddonMemoryModuleTypes.MAKING_NEST.get(), MemoryStatus.VALUE_PRESENT
        ), 5, 100);
    }

    public void tick(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long gameTime) {
        BlockPos nestPos = enhancedBetta.getNestPos();
        if (nestPos.closerToCenterThan(enhancedBetta.position(), 1F) && serverLevel.isWaterAt(nestPos)) {
            serverLevel.setBlock(nestPos, AddonBlocks.BUBBLE_NEST.get().defaultBlockState(), 2);
            enhancedBetta.setNestPos(BlockPos.ZERO);
            enhancedBetta.getBrain().eraseMemory(AddonMemoryModuleTypes.MAKING_NEST.get());
        }
    }

    protected boolean canStillUse(ServerLevel p_23586_, EnhancedBetta enhancedBetta, long p_23588_) {
        return enhancedBetta.getBrain().hasMemoryValue(AddonMemoryModuleTypes.MAKING_NEST.get());
    }
}
