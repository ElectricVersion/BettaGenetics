package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class BettaNip extends Behavior<EnhancedBetta> {

    public BettaNip() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EnhancedBetta enhancedBetta) {
        LivingEntity livingentity = this.getAttackTarget(enhancedBetta);
        return BehaviorUtils.canSee(enhancedBetta, livingentity) && enhancedBetta.isWithinMeleeAttackRange(livingentity);
    }


    protected void start(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long l) {
        enhancedBetta.setIsFlaring(true);
        LivingEntity livingEntity = this.getAttackTarget(enhancedBetta);
        BehaviorUtils.lookAtEntity(enhancedBetta, livingEntity);
        livingEntity.hurt(enhancedBetta.damageSources().mobAttack(enhancedBetta), 0.0F);
        livingEntity.knockback((enhancedBetta.getAttributeValue(Attributes.ATTACK_KNOCKBACK) * 1F), Mth.sin(enhancedBetta.getYRot() * ((float)Math.PI / 180F)), -Mth.cos(enhancedBetta.getYRot() * ((float)Math.PI / 180F)));
        //Nipping and attacking should probably share a cooldown
        enhancedBetta.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, 70-(2L * enhancedBetta.getAggression()) );
    }

    private LivingEntity getAttackTarget(EnhancedBetta enhancedBetta) {
        return enhancedBetta.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }
}
