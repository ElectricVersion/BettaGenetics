package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.*;

public class TrustBetta extends Behavior<EnhancedBetta> {

    public TrustBetta() {
        super(ImmutableMap.of(
                AddonMemoryModuleTypes.NEAREST_TRUSTABLE.get(), MemoryStatus.VALUE_PRESENT
        ));
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EnhancedBetta enhancedBetta) {
        return enhancedBetta.isBaby();
    }

    protected void start(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long gameTime) {
        Brain<EnhancedBetta> brain = enhancedBetta.getBrain();
        if (brain.getMemory(AddonMemoryModuleTypes.NEAREST_TRUSTABLE.get()).isPresent()) {
            if (!brain.hasMemoryValue(AddonMemoryModuleTypes.TRUSTED_BETTAS.get()) || !brain.getMemory(AddonMemoryModuleTypes.TRUSTED_BETTAS.get()).isPresent() ) {
                brain.setMemory(AddonMemoryModuleTypes.TRUSTED_BETTAS.get(), new ArrayList<>(Collections.singleton((brain.getMemory(AddonMemoryModuleTypes.NEAREST_TRUSTABLE.get()).get().getUUID()))));
            }
            else if (!(brain.getMemory(AddonMemoryModuleTypes.TRUSTED_BETTAS.get()).get().contains(brain.getMemory(AddonMemoryModuleTypes.NEAREST_TRUSTABLE.get()).get().getUUID()))) {
                ArrayList<UUID> currentList = new ArrayList<>(brain.getMemory(AddonMemoryModuleTypes.TRUSTED_BETTAS.get()).get());
                currentList.add(brain.getMemory(AddonMemoryModuleTypes.NEAREST_TRUSTABLE.get()).get().getUUID());
                brain.setMemory(AddonMemoryModuleTypes.TRUSTED_BETTAS.get(), currentList);
            }
        }
    }

    protected boolean canStillUse(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long gameTime) {
        return enhancedBetta.isBaby();
    }
}
