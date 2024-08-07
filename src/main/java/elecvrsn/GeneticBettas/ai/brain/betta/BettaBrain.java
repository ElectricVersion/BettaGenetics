package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonEntities;
import mokiyoki.enhancedanimals.entity.EnhancedAxolotl;
import mokiyoki.enhancedanimals.init.ModActivities;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;

import java.util.Optional;


public class BettaBrain  {
    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
    private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.2F;
    private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5F;
    private static final float SPEED_MULTIPLIER_WHEN_CHASING_IN_WATER = 0.6F;

    public static Brain<?> makeBrain(Brain<EnhancedBetta> bettaBrain) {
        initCoreActivity(bettaBrain);
        initIdleActivity(bettaBrain);
        initFightActivity(bettaBrain);
        bettaBrain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        bettaBrain.setDefaultActivity(Activity.IDLE);
        bettaBrain.useDefaultActivity();
        return bettaBrain;
    }

    private static void initFightActivity(Brain<EnhancedBetta> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                0,
                ImmutableList.of(
                        new StopAttackingIfTargetInvalid<>(EnhancedBetta::onStopAttacking),
                        new SetWalkTargetFromAttackTargetIfTargetOutOfReach(BettaBrain::getSpeedModifierChasing),
                        new RunIf<>(EnhancedBetta::isAggressive, new MeleeAttack(20)),
                        new EraseMemoryIf<EnhancedBetta>(BettaBrain::isBreeding, MemoryModuleType.ATTACK_TARGET)
                ),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    private static void initCoreActivity(Brain<EnhancedBetta> p_149307_) {
        p_149307_.addActivity(Activity.CORE, 0, ImmutableList.of(
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS))
        );
    }

    private static void initIdleActivity(Brain<EnhancedBetta> brain) {
        brain.addActivity(Activity.IDLE, ImmutableList.of(
                Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(EntityType.PLAYER, 6.0F), UniformInt.of(30, 60))),
                Pair.of(1, new AnimalMakeLove(AddonEntities.ENHANCED_BETTA.get(), 0.2F)),
                Pair.of(2, new RunOne<>(ImmutableList.of(
                        Pair.of(new FollowTemptation(BettaBrain::getSpeedModifier), 1),
                        Pair.of(new BabyFollowAdult<>(ADULT_FOLLOW_RANGE, BettaBrain::getSpeedModifierFollowingAdult), 1)))
                ),
                Pair.of(3, new StartAttacking<>(BettaBrain::findNearestValidAttackTarget)),
                Pair.of(3, new TryFindWater(6, 0.15F)),
                Pair.of(4, new GateBehavior<>(
                                ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                ImmutableSet.of(),
                                GateBehavior.OrderPolicy.ORDERED,
                                GateBehavior.RunningPolicy.TRY_ALL,
                                ImmutableList.of(
                                        Pair.of(new RandomSwim(0.5F), 2),
                                        Pair.of(new RandomStroll(0.15F, false), 2),
                                        Pair.of(new SetWalkTargetFromLookTarget(BettaBrain::canSetWalkTargetFromLookTarget, BettaBrain::getSpeedModifier, 3), 3),
                                        Pair.of(new RunIf<>(Entity::isInWaterOrBubble, new DoNothing(30, 60)), 5),
                                        Pair.of(new RunIf<>(Entity::isOnGround, new DoNothing(200, 400)), 5)
                                )
                        )
                )
        ));
    }

    private static boolean canSetWalkTargetFromLookTarget(LivingEntity livingEntity) {
        Level level = livingEntity.level;
        Optional<PositionTracker> optional = livingEntity.getBrain().getMemory(MemoryModuleType.LOOK_TARGET);
        if (optional.isPresent()) {
            BlockPos blockpos = optional.get().currentBlockPosition();
            return level.isWaterAt(blockpos) == livingEntity.isInWaterOrBubble();
        } else {
            return false;
        }
    }

    public static void updateActivity(EnhancedBetta betta) {
        Brain<EnhancedBetta> brain = betta.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse((Activity) null);
        if (activity != Activity.PLAY_DEAD && activity != ModActivities.PAUSE_BRAIN.get()) {
            brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.PLAY_DEAD, ModActivities.PAUSE_BRAIN.get(), Activity.FIGHT, Activity.IDLE));
            if (activity == Activity.FIGHT && brain.getActiveNonCoreActivity().orElse((Activity) null) != Activity.FIGHT) {
                brain.setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 2400L);
            }
        }

    }

    private static float getSpeedModifierChasing(LivingEntity livingEntity) {
        return livingEntity.isInWaterOrBubble() ? SPEED_MULTIPLIER_WHEN_CHASING_IN_WATER : 0.15F;
    }

    private static float getSpeedModifierFollowingAdult(LivingEntity livingEntity) {
        return livingEntity.isInWaterOrBubble() ? SPEED_MULTIPLIER_WHEN_CHASING_IN_WATER : 0.15F;
    }

    private static float getSpeedModifier(LivingEntity livingEntity) {
        return livingEntity.isInWaterOrBubble() ? SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER : 0.15F;
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(EnhancedBetta betta) {
        return isBreeding(betta) ? Optional.empty() : betta.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
    }

    private static boolean isBreeding(EnhancedBetta betta) {
        return betta.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET);
    }
}
