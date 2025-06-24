package elecvrsn.GeneticBettas.ai.brain.sensing;

import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonEntities;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;

public class BettaTrustablesSensor extends NearestVisibleLivingEntitySensor {
    protected boolean isMatchingEntity(LivingEntity betta, LivingEntity target) {
        return this.isClose((EnhancedBetta)(betta), target) && target.isInWaterOrBubble() && this.isBabyBetta(target);
    }

    private boolean isBabyBetta(LivingEntity livingEntity) {
        return livingEntity.getType() == AddonEntities.ENHANCED_BETTA.get() && livingEntity.isBaby();
    }

    private boolean isClose(EnhancedBetta betta, LivingEntity p_148276_) {
        return betta.distanceToSqr(p_148276_) <= (betta.isPassive() ? 6 : 5);
    }

    protected MemoryModuleType<LivingEntity> getMemory() {
        return AddonMemoryModuleTypes.NEAREST_TRUSTABLE.get();
    }
}
