package elecvrsn.GeneticBettas.ai.brain.betta;

import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import mokiyoki.enhancedanimals.init.ModMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;

public class BettaMakeLove extends AnimalMakeLove {
    private EnhancedBetta mate = null;

    public BettaMakeLove(EntityType<? extends Animal> p_22391_, float p_22392_) {
        super(p_22391_, p_22392_);
    }

    @Override
    protected void start(ServerLevel level, Animal animal, long gameTime) {
        super.start(level, animal, gameTime);
    }

    @Override
    protected void stop(ServerLevel serverLevel, Animal animal, long gameTime) {
        mate = (EnhancedBetta) (((EnhancedBetta) animal).getBrain().getMemory(MemoryModuleType.BREED_TARGET).isPresent() ? ((EnhancedBetta) animal).getBrain().getMemory(MemoryModuleType.BREED_TARGET).get() : mate);
        super.stop(serverLevel, animal, gameTime);
        EnhancedBetta betta = ((EnhancedBetta) animal);
        if (mate != null) {
            if (betta.getOrSetIsFemale() && !mate.getOrSetIsFemale()) {
                mate.getBrain().setMemory(AddonMemoryModuleTypes.MAKING_NEST.get(), true);
                betta.getBrain().setMemory(ModMemoryModuleTypes.HAS_EGG.get(), true);
            } else if (!betta.getOrSetIsFemale() && mate.getOrSetIsFemale()) {
                betta.getBrain().setMemory(AddonMemoryModuleTypes.MAKING_NEST.get(), true);
                mate.getBrain().setMemory(ModMemoryModuleTypes.HAS_EGG.get(), true);
            }
        }
    }

    @Override
    protected void tick(ServerLevel serverLevel, Animal animal, long l) {
        mate = (EnhancedBetta) (((EnhancedBetta) animal).getBrain().getMemory(MemoryModuleType.BREED_TARGET).isPresent() ? ((EnhancedBetta) animal).getBrain().getMemory(MemoryModuleType.BREED_TARGET).get() : mate);
        super.tick(serverLevel, animal, l);
    }
}