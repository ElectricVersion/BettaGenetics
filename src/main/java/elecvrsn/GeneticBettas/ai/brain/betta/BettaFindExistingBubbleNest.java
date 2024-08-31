package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import mokiyoki.enhancedanimals.init.ModMemoryModuleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class BettaFindExistingBubbleNest extends Behavior<EnhancedBetta> {
    private BlockPos existingNest;
    public BettaFindExistingBubbleNest() {
        //Condition, Min Duration, Max Duration
        super(ImmutableMap.of(
                ModMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                ModMemoryModuleTypes.FOCUS_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                ModMemoryModuleTypes.HAS_EGG.get(), MemoryStatus.VALUE_PRESENT
        ), 1, 100);
    }

    public void tick(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        if (gameTime % 20 == 0) {
            if (existingNest == null) {
                existingNest = betta.findExistingNest();
            }
            if (existingNest != null) {
                WalkTarget walkTarget = new WalkTarget(existingNest, 0.6F, 0);
                betta.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(existingNest));
                betta.getBrain().setMemory(MemoryModuleType.WALK_TARGET, walkTarget);
            }
        }
    }


    @Override
    protected void start(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        betta.getBrain().setMemory(ModMemoryModuleTypes.FOCUS_BRAIN.get(), true);
    }
    @Override
    protected void stop(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        existingNest = null;
        betta.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        betta.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        betta.getBrain().eraseMemory(ModMemoryModuleTypes.FOCUS_BRAIN.get());
    }
    protected boolean canStillUse(ServerLevel p_23586_, EnhancedBetta enhancedBetta, long p_23588_) {
        return enhancedBetta.getBrain().hasMemoryValue(ModMemoryModuleTypes.HAS_EGG.get());
    }
}