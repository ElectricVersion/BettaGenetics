package elecvrsn.GeneticBettas.ai.brain.betta;


import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import mokiyoki.enhancedanimals.init.ModMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class ValidatePauseBrain extends Behavior<EnhancedBetta> {
    public ValidatePauseBrain() {
        super(ImmutableMap.of(
                        ModMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_PRESENT
                )
        );
    }

    protected void start(ServerLevel serverLevel, EnhancedBetta eanimal, long p_149341_) {
        Brain<EnhancedBetta> brain = eanimal.getBrain();

        if (!eanimal.isAnimalSleeping()) {
            brain.eraseMemory(ModMemoryModuleTypes.SLEEPING.get());
            brain.eraseMemory(ModMemoryModuleTypes.PAUSE_BRAIN.get());
            brain.eraseMemory(AddonMemoryModuleTypes.FOUND_SLEEP_SPOT.get());
            brain.useDefaultActivity();
        }

    }
}
