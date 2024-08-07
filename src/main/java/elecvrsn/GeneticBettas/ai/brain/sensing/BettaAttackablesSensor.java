package elecvrsn.GeneticBettas.ai.brain.sensing;

import elecvrsn.GeneticBettas.init.AddonEntities;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;

public class BettaAttackablesSensor extends NearestVisibleLivingEntitySensor {
    public static final float TARGET_DETECTION_DISTANCE = 8.0F;

    protected boolean isMatchingEntity(LivingEntity p_148266_, LivingEntity p_148267_) {
        return this.isClose(p_148266_, p_148267_) && p_148267_.isInWaterOrBubble() && (this.isHostileTarget(p_148267_) || this.isBettaTarget(p_148267_)) && Sensor.isEntityAttackable(p_148266_, p_148267_);
    }

    private boolean isBettaTarget(LivingEntity p_148273_) {
        return p_148273_.getType() == AddonEntities.ENHANCED_BETTA.get();
    }

    private boolean isHostileTarget(LivingEntity livingEntity) {
        return livingEntity.getType().is(EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES);
    }

    private boolean isClose(LivingEntity p_148275_, LivingEntity p_148276_) {
        return p_148276_.distanceToSqr(p_148275_) <= 36.0D;
    }

    protected MemoryModuleType<LivingEntity> getMemory() {
        return MemoryModuleType.NEAREST_ATTACKABLE;
    }
}
