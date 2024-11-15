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

public class FindGoodNestLocation extends Behavior<EnhancedBetta> {
    public FindGoodNestLocation() {
        //Condition, Min Duration, Max Duration
        super(ImmutableMap.of(
                AddonMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                AddonMemoryModuleTypes.MAKING_NEST.get(), MemoryStatus.VALUE_PRESENT
        ), 1, 10);
    }
    @Override
    public void tick(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        if (!betta.findLocationForNest()) {
            betta.getBrain().eraseMemory(AddonMemoryModuleTypes.MAKING_NEST.get());
            return;
        }
        BlockPos nestPos = betta.getNestPos();
        if (nestPos != null) {
            WalkTarget walkTarget = new WalkTarget(nestPos, 0.5F, 0);
            betta.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(nestPos));
            betta.getBrain().setMemory(MemoryModuleType.WALK_TARGET, walkTarget);
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel p_23586_, EnhancedBetta betta, long gameTime) {
        return betta.getNestPos() == null;
    }
    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EnhancedBetta betta) {
        return betta.getNestPos() == null;
    }
}
