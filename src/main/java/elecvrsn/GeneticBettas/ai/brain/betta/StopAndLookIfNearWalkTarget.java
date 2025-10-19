package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class StopAndLookIfNearWalkTarget extends Behavior<EnhancedBetta> {
    protected boolean canStillUse(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        return true;
    }

    public StopAndLookIfNearWalkTarget() {
        //Condition, Min Duration, Max Duration
        super(ImmutableMap.of(
                AddonMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                AddonMemoryModuleTypes.FOCUS_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT
        ), 1, 1);
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EnhancedBetta enhancedBetta) {
        return enhancedBetta.hasReachedTarget();
    }

    public void start(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        betta.getBrain().setMemoryWithExpiry(AddonMemoryModuleTypes.FOCUS_BRAIN.get(), true, 50);
    }

}
