package elecvrsn.GeneticBettas.ai.brain.sensing;

import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonEntities;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;

public class BettaAttackablesSensor extends NearestVisibleLivingEntitySensor {
    public static final float TARGET_DETECTION_DISTANCE = 8.0F;

    protected boolean isMatchingEntity(LivingEntity betta, LivingEntity target) {
        return this.isClose((EnhancedBetta)(betta), target) && target.isInWaterOrBubble() && (this.isHostileTarget(target) || ((this.isBettaTarget(target) && !isTrusted((EnhancedBetta)betta, target)) && (areBothMale((EnhancedBetta)betta, (EnhancedBetta)target) || ((EnhancedBetta) betta).isHighlyAggressive() ))) && Sensor.isEntityAttackable(betta, target);
    }

    private boolean isBettaTarget(LivingEntity livingEntity) {
        return livingEntity.getType() == AddonEntities.ENHANCED_BETTA.get();
    }

    private boolean isTrusted(EnhancedBetta betta, LivingEntity livingEntity) {
        return betta.getBrain().hasMemoryValue(AddonMemoryModuleTypes.TRUSTED_BETTAS.get()) && betta.getBrain().getMemory(AddonMemoryModuleTypes.TRUSTED_BETTAS.get()).get().contains(livingEntity.getUUID());
    }

    private boolean areBothMale(EnhancedBetta subject, EnhancedBetta target) {
        return !subject.getOrSetIsFemale() && !target.getOrSetIsFemale();
    }

    private boolean isHostileTarget(LivingEntity livingEntity) {
        return livingEntity.getType().is(EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES);
    }

    private boolean isClose(EnhancedBetta betta, LivingEntity p_148276_) {
        return betta.distanceToSqr(p_148276_) <= (betta.isHighlyAggressive() ? 9 : 4);
    }

    protected MemoryModuleType<LivingEntity> getMemory() {
        return MemoryModuleType.NEAREST_ATTACKABLE;
    }
}
