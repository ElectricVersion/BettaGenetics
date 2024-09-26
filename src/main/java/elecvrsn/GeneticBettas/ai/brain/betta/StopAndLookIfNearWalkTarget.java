package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import mokiyoki.enhancedanimals.init.ModMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.Optional;

public class StopAndLookIfNearWalkTarget extends Behavior<EnhancedBetta> {
    protected boolean canStillUse(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        return true;
    }

    public StopAndLookIfNearWalkTarget() {
        //Condition, Min Duration, Max Duration
        super(ImmutableMap.of(
                ModMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                ModMemoryModuleTypes.FOCUS_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT
        ), 1, 1);
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EnhancedBetta enhancedBetta) {
        return enhancedBetta.hasReachedTarget();
    }

    public void start(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        betta.getBrain().setMemoryWithExpiry(ModMemoryModuleTypes.FOCUS_BRAIN.get(), true, 50);
    }

}
