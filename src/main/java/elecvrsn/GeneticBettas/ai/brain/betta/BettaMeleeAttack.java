package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.model.modeldata.BettaModelData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;

public class BettaMeleeAttack extends Behavior<EnhancedBetta> {
    private final int cooldownBetweenAttacks;

    public BettaMeleeAttack(int i) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));
        this.cooldownBetweenAttacks = i;
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EnhancedBetta enhancedBetta) {
        LivingEntity livingentity = this.getAttackTarget(enhancedBetta);
        return BehaviorUtils.canSee(enhancedBetta, livingentity) && BehaviorUtils.isWithinMeleeAttackRange(enhancedBetta, livingentity);
    }


    protected void start(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long l) {
        enhancedBetta.setIsAngry(true);
        LivingEntity livingentity = this.getAttackTarget(enhancedBetta);
        BehaviorUtils.lookAtEntity(enhancedBetta, livingentity);
        enhancedBetta.swing(InteractionHand.MAIN_HAND);
        enhancedBetta.doHurtTarget(livingentity);
            enhancedBetta.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, (long)this.cooldownBetweenAttacks);
    }

    private LivingEntity getAttackTarget(EnhancedBetta enhancedBetta) {
        return enhancedBetta.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
