package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import mokiyoki.enhancedanimals.init.ModMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class PauseBrain extends Behavior<EnhancedBetta> {
    public PauseBrain() {
        super(ImmutableMap.of(
                        ModMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_PRESENT
                )
        );
    }

    protected boolean canStillUse(ServerLevel serverLevel, EnhancedBetta eanimal, long p_149324_) {
        return eanimal.getBrain().hasMemoryValue(ModMemoryModuleTypes.PAUSE_BRAIN.get());
    }

    protected void start(ServerLevel serverLevel, EnhancedBetta eanimal, long p_149332_) {
        Brain<EnhancedBetta> brain = eanimal.getBrain();
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
}