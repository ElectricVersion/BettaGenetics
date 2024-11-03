package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonActivities;
import elecvrsn.GeneticBettas.init.AddonEntities;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import mokiyoki.enhancedanimals.init.ModActivities;
import mokiyoki.enhancedanimals.init.ModMemoryModuleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;

import java.util.Optional;


public class BettaBrain  {
    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
    private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.5F;
    private static final float SPEED_MULTIPLIER_WHEN_ON_LAND = 0.25F;
    private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5F;
    private static final float SPEED_MULTIPLIER_WHEN_CHASING_IN_WATER = 0.55F;

    public static Brain<?> makeBrain(Brain<EnhancedBetta> bettaBrain) {
        initCoreActivity(bettaBrain);
        initIdleActivity(bettaBrain);
        initLayEggActivity(bettaBrain);
        initSleepingActivity(bettaBrain);
        initPauseBrainActivity(bettaBrain);
        initFightActivity(bettaBrain);
        initNipActivity(bettaBrain);
        initFleeActivity(bettaBrain);
        initNestingActivity(bettaBrain);
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
                        new RunIf<>(BettaBrain::isAttackTargetFarEnough, new StopBeingMad()),
                        new SetWalkTargetFromAttackTargetIfTargetOutOfReach(BettaBrain::getSpeedModifierChasing),
                        new BettaMeleeAttack(),
                        new RunIf<>(EnhancedBetta::isNotHighlyAggressive, SetWalkTargetAwayFrom.entity(MemoryModuleType.ATTACK_TARGET, 0.4F, 3, true)),
                        new EraseMemoryIf<>(BettaBrain::isBreeding, MemoryModuleType.ATTACK_TARGET)
                ),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    private static void initNipActivity(Brain<EnhancedBetta> brain) {
        brain.addActivityAndRemoveMemoriesWhenStopped(
                AddonActivities.NIP.get(),
                ImmutableList.of(
                        Pair.of(0, new StopAttackingIfTargetInvalid<>(EnhancedBetta::onStopAttacking)),
                        Pair.of(1, new RunIf<>(BettaBrain::isAttackTargetFarEnough, new StopBeingMad())),
                        Pair.of(2, new SetWalkTargetFromAttackTargetIfTargetOutOfReach(BettaBrain::getSpeedModifierChasing)),
                        Pair.of(3, new BettaNip()),
                        Pair.of(4, new RunIf<>(ImmutableMap.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_PRESENT), SetWalkTargetAwayFromWithExpiry.entity(MemoryModuleType.ATTACK_TARGET, 0.5F, 3, true, 70))),
                        Pair.of(5, new EraseMemoryIf<>(BettaBrain::isBreeding, MemoryModuleType.ATTACK_TARGET))
                ),
                ImmutableSet.of(
                        Pair.of(AddonMemoryModuleTypes.IS_ATTACK_NIP.get(), MemoryStatus.VALUE_PRESENT),
                        Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
                ),
                ImmutableSet.of(
                        MemoryModuleType.ATTACK_TARGET
                )
        );
    }

    private static void initFleeActivity(Brain<EnhancedBetta> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.AVOID,
                0,
                ImmutableList.of(
                        new EraseMemoryIf<>(BettaBrain::isHurtByTimerExpired, MemoryModuleType.HURT_BY_ENTITY),
                        SetWalkTargetAwayFrom.entity(MemoryModuleType.HURT_BY_ENTITY, 0.5F, 6, true)
                ),
                MemoryModuleType.HURT_BY_ENTITY
        );
    }

    private static void initLayEggActivity(Brain<EnhancedBetta> brain) {
        brain.addActivityWithConditions(
                AddonActivities.LAY_EGG.get(),
                ImmutableList.of(
                    Pair.of(0, new LayEgg()),
                    Pair.of(1, new LookAtTargetSink(45, 90)),
                    Pair.of(2, new MoveToTargetSink())
                ),
                ImmutableSet.of(Pair.of(ModMemoryModuleTypes.HAS_EGG.get(), MemoryStatus.VALUE_PRESENT))
                );
    }

    private static void initPauseBrainActivity(Brain<EnhancedBetta> brain) {
        brain.addActivityAndRemoveMemoriesWhenStopped(
                ModActivities.PAUSE_BRAIN.get(),
                ImmutableList.of(
                        Pair.of(0, new PauseBrain())
                ),
                ImmutableSet.of(
                        Pair.of(ModMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_PRESENT)
                ),
                ImmutableSet.of(
                        ModMemoryModuleTypes.PAUSE_BRAIN.get()
                )
        );
    }

    private static void initCoreActivity(Brain<EnhancedBetta> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                new ValidatePauseBrain(),
                new TrustBetta(),
                new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS))
        );
    }

    private static void initNestingActivity(Brain<EnhancedBetta> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(AddonActivities.MAKE_BUBBLE_NEST.get(),
                0, ImmutableList.of(
                new FindGoodNestLocation(),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                new MakeBubbleNest()),
                AddonMemoryModuleTypes.MAKING_NEST.get()
        );
    }

    private static void initSleepingActivity(Brain<EnhancedBetta> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.REST,
                0, ImmutableList.of(
                        new FindPlaceToSleep(),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()),
                ModMemoryModuleTypes.SLEEPING.get()
        );
    }

    private static void initIdleActivity(Brain<EnhancedBetta> brain) {
        brain.addActivity(Activity.IDLE, ImmutableList.of(
                Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(EntityType.PLAYER, 9.0F), UniformInt.of(20, 60))),
                Pair.of(1, new BettaMakeLove(AddonEntities.ENHANCED_BETTA.get(), SPEED_MULTIPLIER_WHEN_MAKING_LOVE)),
                Pair.of(1, new RunIf<>(EnhancedBetta::isAnimalSleeping, new FindPlaceToSleep())),
                Pair.of(2, new RunOne<>(ImmutableList.of(
                        Pair.of(new FollowTemptation(BettaBrain::getSpeedModifier), 1),
                        Pair.of(new BabyFollowAdult<>(ADULT_FOLLOW_RANGE, BettaBrain::getSpeedModifierFollowingAdult), 1)))
                ),
                Pair.of(2, new RunSometimes<>(new StopAndLookIfNearWalkTarget(), UniformInt.of(10, 40))),
                Pair.of(3, new StartAttacking<>(BettaBrain::findNearestValidAttackTarget)),
                Pair.of(3, new TryFindWater(6, SPEED_MULTIPLIER_WHEN_ON_LAND)),
                Pair.of(4, new GateBehavior<>(
                                ImmutableMap.of(
                                        MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                                        ModMemoryModuleTypes.FOCUS_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                                        ModMemoryModuleTypes.HAS_EGG.get(), MemoryStatus.VALUE_ABSENT),
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
        Activity activity = brain.getActiveNonCoreActivity().orElse(null);
        if (betta.isAnimalSleeping()) {
            brain.setMemory(ModMemoryModuleTypes.SLEEPING.get(), true);
            if (betta.isOnGround()) {
                brain.setMemory(ModMemoryModuleTypes.PAUSE_BRAIN.get(), true);
            }
        }
        else if (brain.hasMemoryValue(ModMemoryModuleTypes.SLEEPING.get())) {
            brain.eraseMemory(ModMemoryModuleTypes.SLEEPING.get());
        }
        if (activity != ModActivities.PAUSE_BRAIN.get()) {
            brain.setActiveActivityToFirstValid(betta.isBaby() ? betta.getBabyActivities() : betta.getAdultActivities());
            if (betta.isAggressive()) {
                if (activity == Activity.FIGHT && brain.getActiveNonCoreActivity().orElse(null) != Activity.FIGHT) {
                    brain.setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 2400L);
                }
                else if (brain.getActiveNonCoreActivity().orElse(null) == Activity.FIGHT) {
                    betta.setIsAngry(true);
                }
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

    private static boolean isHurtByTimerExpired(EnhancedBetta betta) {
        return betta.getLastHurtByMobTimestamp()+100 < betta.tickCount;
    }


    private static boolean isAttackTargetFarEnough(EnhancedBetta betta) {
        if (betta.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
            return betta.distanceToSqr(betta.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get()) > (betta.isHighlyAggressive() ? 9 : 4);
        }
        return true;
    }

}
