package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class StopBeingMad extends Behavior<EnhancedBetta> {
    public StopBeingMad() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
                )
        );
    }

    @Override
    protected void start(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long p_149332_) {
        Brain<EnhancedBetta> brain = enhancedBetta.getBrain();
        brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
        enhancedBetta.setIsFlaring(false);
    }

    @Override
    public boolean checkExtraStartConditions(ServerLevel level, EnhancedBetta betta) {
        if (betta.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
            return betta.distanceToSqr(betta.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get()) > (betta.isHighlyAggressive() ? 9 : 4);
        }
        return true;
    }
}
