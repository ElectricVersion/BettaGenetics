package elecvrsn.GeneticBettas.ai.brain.sensing;

import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonEntities;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Optional;

import static net.minecraft.world.entity.EntityType.PLAYER;

public class BettaAttackablesSensor extends NearestVisibleLivingEntitySensor {
    public static final float TARGET_DETECTION_DISTANCE = 8.0F;
    protected boolean isMatchingEntity(LivingEntity bettaEntity, LivingEntity target) {
        EnhancedBetta betta = (EnhancedBetta)bettaEntity;
        // TODO: Clean up all of this. This sucks. Bad code
        return this.isClose(betta, target) && target.isInWaterOrBubble()
                && (isHostileTarget(target) || (isBettaTarget(target)
                && isAggressiveEnough(betta, target)
                && !isTrusted(betta, target))) && Sensor.isEntityAttackable(betta, target);
    }

    private boolean isBettaTarget(LivingEntity livingEntity) {
        return livingEntity.getType() == AddonEntities.ENHANCED_BETTA.get();
    }

    private boolean isTrusted(EnhancedBetta betta, LivingEntity livingEntity) {
        // If both male (e.g. it should be a full on attack rather than a nip) just remove the memory value rather than set it to false
        //So we can check it easily with VALUE_PRESENT
        betta.getBrain().setMemory(AddonMemoryModuleTypes.IS_ATTACK_NIP.get(), (!betta.getOrSetIsFemale() && !((EnhancedBetta)livingEntity).getOrSetIsFemale()) ? Optional.empty() : Optional.of(true));
        return betta.getBrain().hasMemoryValue(AddonMemoryModuleTypes.TRUSTED_BETTAS.get()) && betta.getBrain().getMemory(AddonMemoryModuleTypes.TRUSTED_BETTAS.get()).get().contains(livingEntity.getUUID());
    }

    private boolean isAggressiveEnough(EnhancedBetta betta, LivingEntity livingEntity) {
        return EnhancedBetta.isHighlyAggressive(betta) || (!betta.getOrSetIsFemale() && !((EnhancedBetta)livingEntity).getOrSetIsFemale());
    }
    private boolean isHostileTarget(LivingEntity livingEntity) {
        return livingEntity.getType().is(EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES);
    }

    private boolean isClose(EnhancedBetta betta, LivingEntity livingEntity) {
        return betta.distanceToSqr(livingEntity) <= (EnhancedBetta.isHighlyAggressive(betta) ? 6 : 3);
    }

    protected MemoryModuleType<LivingEntity> getMemory() {
        return MemoryModuleType.NEAREST_ATTACKABLE;
    }
}
