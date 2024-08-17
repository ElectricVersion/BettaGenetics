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

import java.util.Map;

public class FindGoodNestLocation extends Behavior<EnhancedBetta> {
    public FindGoodNestLocation() {
        //Condition, Min Duration, Max Duration
        super(ImmutableMap.of(
                ModMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_ABSENT
//                AddonMemoryModuleTypes.SEEKING_NEST.get(), MemoryStatus.VALUE_PRESENT
        ), 1, 10);
    }

    public void start(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long gameTime) {
        enhancedBetta.findNestLocation();
        BlockPos nestPos = enhancedBetta.getNestPos();
        if (nestPos != BlockPos.ZERO) {
            WalkTarget walkTarget = new WalkTarget(nestPos, 0.5F, 0);
//        enhancedBetta.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(enhancedBetta.getNestPos()));
            enhancedBetta.getBrain().setMemory(MemoryModuleType.WALK_TARGET, walkTarget);
//            enhancedBetta.getBrain().eraseMemory(AddonMemoryModuleTypes.SEEKING_NEST.get());
        }
    }
}
