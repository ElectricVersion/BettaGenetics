package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class FlareIfHurt extends Behavior<EnhancedBetta> {
    public FlareIfHurt() {
        super(ImmutableMap.of(
                MemoryModuleType.HURT_BY_ENTITY, MemoryStatus.VALUE_PRESENT
        ));
    }
    protected void start(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long gameTime) {
        enhancedBetta.setIsFlaring(true);
    }

    protected void stop(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long gameTime) {
        enhancedBetta.setIsFlaring(false);
    }


    protected boolean canStillUse(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long gameTime) {
        return enhancedBetta.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY_ENTITY);
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EnhancedBetta enhancedBetta) {
        return enhancedBetta.isNotPassive();
    }
}
