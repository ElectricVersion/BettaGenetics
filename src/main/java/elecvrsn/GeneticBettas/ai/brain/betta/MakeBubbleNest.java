package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import mokiyoki.enhancedanimals.init.ModMemoryModuleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

import java.util.Map;
import java.util.Optional;

public class MakeBubbleNest extends Behavior<EnhancedBetta> {

    boolean nestMade = false;
    public MakeBubbleNest() {
        //Condition, Min Duration, Max Duration
        super(ImmutableMap.of(
                ModMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                ModMemoryModuleTypes.FOCUS_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                AddonMemoryModuleTypes.SEEKING_NEST.get(), MemoryStatus.VALUE_PRESENT
        ), 10, 100);
    }

    public void start(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long gameTime) {
        enhancedBetta.getBrain().setMemory(ModMemoryModuleTypes.FOCUS_BRAIN.get(), true);
        enhancedBetta.findNestLocation();
        BlockPos nestPos = enhancedBetta.getNestPos();
        if (nestPos != BlockPos.ZERO) {
            WalkTarget walkTarget = new WalkTarget(nestPos, 0.5F, 0);
//        enhancedBetta.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(enhancedBetta.getNestPos()));
            enhancedBetta.getBrain().setMemory(MemoryModuleType.WALK_TARGET, walkTarget);
        }
    }

    protected void stop(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long gameTime) {
    }


    public void tick(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long gameTime) {
        BlockPos nestPos = enhancedBetta.getNestPos();
        if (nestPos.closerToCenterThan(enhancedBetta.position(), 1.0F) && serverLevel.isWaterAt(nestPos)) {
            serverLevel.setBlock(nestPos, AddonBlocks.BUBBLE_NEST.get().defaultBlockState(), 2);
            enhancedBetta.setNestPos(BlockPos.ZERO);
            enhancedBetta.getBrain().eraseMemory(ModMemoryModuleTypes.FOCUS_BRAIN.get());
            enhancedBetta.getBrain().eraseMemory(AddonMemoryModuleTypes.SEEKING_NEST.get());
        }
        else {
//            enhancedBetta.getBrain().setMemory(AddonMemoryModuleTypes.SEEKING_NEST.get(), true);
        }
    }

    protected boolean canStillUse(ServerLevel p_23586_, EnhancedBetta enhancedBetta, long p_23588_) {
        return enhancedBetta.getBrain().hasMemoryValue(ModMemoryModuleTypes.FOCUS_BRAIN.get());
    }
}
