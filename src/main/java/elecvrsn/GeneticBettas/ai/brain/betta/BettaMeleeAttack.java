package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class BettaMeleeAttack extends Behavior<EnhancedBetta> {

    public BettaMeleeAttack() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EnhancedBetta enhancedBetta) {
        LivingEntity livingentity = this.getAttackTarget(enhancedBetta);
        return BehaviorUtils.canSee(enhancedBetta, livingentity) && BehaviorUtils.isWithinMeleeAttackRange(enhancedBetta, livingentity);
    }


    protected void start(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long l) {
        enhancedBetta.setIsFlaring(true);
        LivingEntity livingEntity = this.getAttackTarget(enhancedBetta);
        BehaviorUtils.lookAtEntity(enhancedBetta, livingEntity);
        enhancedBetta.swing(InteractionHand.MAIN_HAND);
        enhancedBetta.doHurtTarget(livingEntity);
        enhancedBetta.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, 60-(2L * enhancedBetta.getAggression()) );
    }

    private LivingEntity getAttackTarget(EnhancedBetta enhancedBetta) {
        return enhancedBetta.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }
}
