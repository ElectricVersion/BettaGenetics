package elecvrsn.GeneticBettas.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import elecvrsn.GeneticBettas.ai.brain.betta.BettaBrain;
import elecvrsn.GeneticBettas.config.BettasCommonConfig;
import elecvrsn.GeneticBettas.entity.genetics.BettaGeneticsInitialiser;
import elecvrsn.GeneticBettas.init.*;
import elecvrsn.GeneticBettas.items.EnhancedBettaBucket;
import elecvrsn.GeneticBettas.model.modeldata.BettaModelData;
import elecvrsn.GeneticBettas.util.AddonReference;
import mokiyoki.enhancedanimals.config.GeneticAnimalsConfig;
import mokiyoki.enhancedanimals.entity.EnhancedAnimalAbstract;
import mokiyoki.enhancedanimals.entity.EntityState;
import mokiyoki.enhancedanimals.entity.util.Colouration;
import mokiyoki.enhancedanimals.init.FoodSerialiser;
import mokiyoki.enhancedanimals.model.modeldata.AnimalModelData;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TexturingType;
import mokiyoki.enhancedanimals.util.Genes;
import elecvrsn.GeneticBettas.ai.brain.ValidatePath;
import elecvrsn.GeneticBettas.renderer.texture.UpdatedTextureGrouping;
import elecvrsn.GeneticBettas.renderer.texture.UpdatedTexturingType;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;
import static elecvrsn.GeneticBettas.util.ColorUtil.clampRGB;
import static elecvrsn.GeneticBettas.util.ColorUtil.getHSBFromHex;
import static net.minecraft.world.level.block.Blocks.WATER;

public class EnhancedBetta extends EnhancedAnimalAbstract implements Bucketable {
    public static String speciesTranslationKey = "entity.geneticbettas.enhanced_betta";
    protected static final ImmutableList<? extends SensorType<? extends Sensor<? super EnhancedBetta>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.HURT_BY, AddonSensorTypes.BETTA_ATTACKABLES.get(), AddonSensorTypes.BETTA_TRUSTABLES.get(), AddonSensorTypes.BETTA_FOOD_TEMPTATIONS.get());
    protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            AddonMemoryModuleTypes.SLEEPING.get(), AddonMemoryModuleTypes.PAUSE_BRAIN.get(), AddonMemoryModuleTypes.FOCUS_BRAIN.get(),
            MemoryModuleType.BREED_TARGET, AddonMemoryModuleTypes.HAS_EGG.get(), MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT,
            MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_ATTACKABLE, AddonMemoryModuleTypes.NEAREST_TRUSTABLE.get(),
            AddonMemoryModuleTypes.TRUSTED_BETTAS.get(), MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED, MemoryModuleType.HAS_HUNTING_COOLDOWN, AddonMemoryModuleTypes.FOUND_SLEEP_SPOT.get(),
            AddonMemoryModuleTypes.MAKING_NEST.get(), AddonMemoryModuleTypes.IS_ATTACK_NIP.get(), AddonMemoryModuleTypes.NEST_POS.get(),
            AddonMemoryModuleTypes.LAST_MATE.get());
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EnhancedBetta.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_FLARING = SynchedEntityData.defineId(EnhancedBetta.class, EntityDataSerializers.BOOLEAN);

    private ImmutableList<Activity> babyActivities = null;
    private ImmutableList<Activity> adultActivities = null;
    private int aggression = -1;

    private static final int BETTA_TOTAL_AIR_SUPPLY = 2000;

    private boolean isInTank;
    private TextureGrouping transRootGroup;

    private static final String[] TEXTURES_FIN_ALPHA = new String[]{
            "mask/solid.png", "mask/lace_lg.png", "mask/cambodian_lg.png"
    };

    private static final String[] TEXTURES_IRI_FINS = new String[]{
            "", "iri/fin/low_fin_iri.png", "iri/fin/med_fin_iri.png", "iri/fin/high_fin_iri.png",
    };

    private static final String[] TEXTURES_IRI_BODY = new String[]{
            "", "iri/body/low_body_iri.png", "iri/body/med_body_iri.png", "iri/body/high_body_iri.png",
            "mask/solid.png", "iri/body/spread_low_opacity.png"
    };

    private static final String[] TEXTURES_IRI_MASK = new String[]{
            "iri/body/nonmask.png", "iri/body/mask_low1.png", "iri/body/mask_low2.png",
            "iri/body/mask_med1.png", "iri/body/mask_med2.png",
            "iri/body/mask_high1.png", "iri/body/mask_high2.png",
            "iri/body/mask_max.png",
    };

    private static final String[] TEXTURES_DRAGONSCALE = new String[]{
            "",
            "iri/dragonscale/dragon_het_min_1.png", "iri/dragonscale/dragon_het_min_2.png",  "iri/dragonscale/dragon_het_min_3.png",
            "iri/dragonscale/dragon_het_normal_1.png",
            "iri/dragonscale/dragon_homo_low_1.png", "iri/dragonscale/dragon_homo_low_2.png", "iri/dragonscale/dragon_homo_low_3.png", "iri/dragonscale/dragon_homo_low_4.png", "iri/dragonscale/dragon_homo_low_5.png",
            "iri/dragonscale/dragon_homo_normal_1.png", "iri/dragonscale/dragon_homo_normal_2.png"
    };
    private static final String[] TEXTURES_DRAGONSCALE_MASK = new String[]{
            "", "iri/dragonscale/dragon_het_mask.png", "iri/dragonscale/dragon_homo_mask.png"
    };

    private static final String[][][] TEXTURES_RED_FIN = new String[][][]{
            {
                    {"", "red/fin/plakat_red_low.png", "red/fin/plakat_red_med.png", "red/fin/plakat_red_high.png", "red/fin/wildtype.png"},
                    {"", "red/fin/plakat_double_red_low.png", "red/fin/plakat_double_red_med.png", "red/fin/plakat_double_red_high.png", "red/fin/wildtype.png"},
            },
            {
                    {"", "red/fin/dpk_red_low.png", "red/fin/dpk_red_med.png", "red/fin/dpk_red_high.png", "red/fin/wildtype.png"},
                    {"", "red/fin/dpk_double_red_low.png", "red/fin/dpk_double_red_med.png", "red/fin/dpk_double_red_high.png", "red/fin/wildtype.png"},
            },
            {
                    {"", "red/fin/hmpk_red_low.png", "red/fin/hmpk_red_med.png", "red/fin/hmpk_red_high.png", "red/fin/wildtype.png"},
                    {"", "red/fin/hmpk_double_red_low.png", "red/fin/hmpk_double_red_med.png", "red/fin/hmpk_double_red_high.png", "red/fin/wildtype.png"},
            },
            {
                    {"", "red/fin/veil_red_low.png", "red/fin/veil_red_med.png", "red/fin/veil_red_high.png", "red/fin/wildtype.png"},
                    {"", "red/fin/veil_double_red_low.png", "red/fin/veil_double_red_med.png", "red/fin/veil_double_red_high.png", "red/fin/wildtype.png"},
            },
            {
                    {"", "red/fin/delta_red_low.png", "red/fin/delta_red_med.png", "red/fin/delta_red_high.png", "red/fin/wildtype.png"},
                    {"", "red/fin/delta_double_red_low.png", "red/fin/delta_double_red_med.png", "red/fin/delta_double_red_high.png", "red/fin/wildtype.png"},
            },
            {
                    {"", "red/fin/halfmoon_red_low.png", "red/fin/halfmoon_red_med.png", "red/fin/halfmoon_red_high.png", "red/fin/wildtype.png"},
                    {"", "red/fin/halfmoon_double_red_low.png", "red/fin/halfmoon_double_red_med.png", "red/fin/halfmoon_double_red_high.png", "red/fin/wildtype.png"},
            },
            {
                    {"", "red/fin/plakat_red_low.png", "red/fin/plakat_red_med.png", "red/fin/plakat_red_high.png", "red/fin/wildtype.png"},
                    {"", "red/fin/plakat_double_red_low.png", "red/fin/plakat_double_red_med.png", "red/fin/plakat_double_red_high.png", "red/fin/wildtype.png"},
            },
    };

    private static final String[] TEXTURES_RED_BODY = new String[]{
            "", "red/body/min.png", "red/body/low.png", "red/body/med.png", "red/body/high.png", "red/extended.png", "red/extended_het_mask.png", "red/extended_homo_mask.png"
    };

    private static final String[] TEXTURES_ALPHA = new String[]{
            "mask/transparent.png", "mask/percent25.png", "mask/percent50.png", "mask/percent75.png", "mask/solid.png"
    };

    private static final String[] TEXTURES_EYE_L = new String[]{
            "eyes/eye_l_wildtype.png", "eyes/eye_l_dark.png", "eyes/eye_l_light.png"
    };

    private static final String[] TEXTURES_EYE_R = new String[]{
            "eyes/eye_r_wildtype.png", "eyes/eye_r_dark.png", "eyes/eye_r_light.png"
    };


    private static final String[][][] TEXTURES_MARBLE = new String[][][]{
            {
                    {"mask/solid.png"}
            },
            {
                    //Poor Grizzle
                    {"mask/transparent.png"},
                    {"marble/poorgrizzle_small_1.png", "marble/poorgrizzle_small_2.png", "marble/poorgrizzle_small_3.png", "marble/poorgrizzle_small_4.png", "marble/poorgrizzle_small_5.png"},
                    {"marble/poorgrizzle_medium_1.png", "marble/poorgrizzle_medium_2.png", "marble/poorgrizzle_medium_3.png", "marble/poorgrizzle_medium_4.png", "marble/poorgrizzle_medium_5.png"},
                    {"marble/poorgrizzle_large_1.png", "marble/poorgrizzle_large_2.png", "marble/poorgrizzle_large_3.png", "marble/poorgrizzle_large_4.png", "marble/poorgrizzle_large_5.png"},
                    {"marble/poorgrizzle_xl_1.png", "marble/poorgrizzle_xl_2.png", "marble/poorgrizzle_xl_3.png", "marble/poorgrizzle_xl_4.png", "marble/poorgrizzle_xl_5.png"},
                    {"mask/solid.png"}
            },
            {
                    //Medium Grizzle
                    {"mask/transparent.png"},
                    {"marble/mediumgrizzle_small_1.png", "marble/mediumgrizzle_small_2.png", "marble/mediumgrizzle_small_3.png", "marble/mediumgrizzle_small_4.png", "marble/mediumgrizzle_small_1.png"},
                    {"marble/mediumgrizzle_medium_1.png", "marble/mediumgrizzle_medium_2.png", "marble/mediumgrizzle_medium_3.png", "marble/mediumgrizzle_medium_4.png", "marble/mediumgrizzle_medium_5.png"},
                    {"marble/mediumgrizzle_large_1.png", "marble/mediumgrizzle_large_2.png", "marble/mediumgrizzle_large_3.png", "marble/mediumgrizzle_large_1.png", "marble/mediumgrizzle_large_1.png"},
                    {"marble/mediumgrizzle_xl_1.png", "marble/mediumgrizzle_xl_1.png", "marble/mediumgrizzle_xl_1.png", "marble/mediumgrizzle_xl_1.png", "marble/mediumgrizzle_xl_1.png"},
                    {"mask/solid.png"}
            },
            {
                    //High Grizzle
                    {"mask/transparent.png"},
                    {"marble/highgrizzle_small_1.png", "marble/highgrizzle_small_2.png", "marble/highgrizzle_small_3.png", "marble/highgrizzle_small_4.png", "marble/highgrizzle_small_5.png",
                            "marble/highgrizzle_small_1.png", "marble/highgrizzle_small_2.png", "marble/highgrizzle_small_3.png", "marble/highgrizzle_small_4.png", "marble/highgrizzle_small_5.png"},
                    {"marble/highgrizzle_medium_1.png", "marble/highgrizzle_medium_2.png", "marble/highgrizzle_medium_3.png", "marble/highgrizzle_medium_4.png", "marble/highgrizzle_medium_5.png",
                            "marble/highgrizzle_medium_6.png", "marble/highgrizzle_medium_7.png", "marble/highgrizzle_medium_3.png", "marble/highgrizzle_medium_4.png", "marble/highgrizzle_medium_5.png"},
                    {"marble/highgrizzle_large_1.png", "marble/highgrizzle_large_2.png", "marble/highgrizzle_large_3.png", "marble/highgrizzle_large_4.png", "marble/highgrizzle_large_5.png",
                            "marble/highgrizzle_large_1.png", "marble/highgrizzle_large_2.png", "marble/highgrizzle_large_3.png", "marble/highgrizzle_large_4.png", "marble/highgrizzle_large_5.png"},
                    {"marble/highgrizzle_xl_1.png", "marble/highgrizzle_xl_2.png", "marble/highgrizzle_xl_3.png", "marble/highgrizzle_xl_4.png", "marble/highgrizzle_xl_5.png",
                            "marble/highgrizzle_xl_1.png", "marble/highgrizzle_xl_2.png", "marble/highgrizzle_xl_3.png", "marble/highgrizzle_xl_4.png", "marble/highgrizzle_xl_5.png"},
                    {"mask/solid.png"}

            },
            {
                    //Medium Spots
                    {"mask/transparent.png"},
                    {"marble/secondbestspots_small_1.png", "marble/secondbestspots_small_2.png", "marble/secondbestspots_small_3.png", "marble/secondbestspots_small_4.png", "marble/secondbestspots_small_5.png",
                            "marble/secondbestspots_small_6.png", "marble/secondbestspots_small_7.png", "marble/secondbestspots_small_8.png", "marble/secondbestspots_small_9.png", "marble/secondbestspots_small_10.png"},
                    {"marble/secondbestspots_medium_1.png", "marble/secondbestspots_medium_2.png", "marble/secondbestspots_medium_3.png", "marble/secondbestspots_medium_4.png", "marble/secondbestspots_medium_5.png",
                            "marble/secondbestspots_medium_6.png", "marble/secondbestspots_medium_7.png", "marble/secondbestspots_medium_8.png", "marble/secondbestspots_medium_9.png", "marble/secondbestspots_medium_10.png"},
                    {"marble/secondbestspots_large_1.png", "marble/secondbestspots_large_2.png", "marble/secondbestspots_large_3.png", "marble/secondbestspots_large_4.png", "marble/secondbestspots_large_5.png",
                            "marble/secondbestspots_large_6.png", "marble/secondbestspots_large_7.png", "marble/secondbestspots_large_8.png", "marble/secondbestspots_large_9.png", "marble/secondbestspots_large_10.png"},
                    {"marble/secondbestspots_xl_1.png", "marble/secondbestspots_xl_2.png", "marble/secondbestspots_xl_3.png", "marble/secondbestspots_xl_4.png", "marble/secondbestspots_xl_5.png",
                            "marble/secondbestspots_xl_6.png", "marble/secondbestspots_xl_7.png", "marble/secondbestspots_xl_8.png", "marble/secondbestspots_xl_9.png", "marble/secondbestspots_xl_5.png"},
                    {"mask/solid.png"}
            },
            {
                    //Best Spots
                    {"mask/transparent.png"},
                    {"marble/bestspots_small_1.png", "marble/bestspots_small_2.png", "marble/bestspots_small_3.png", "marble/bestspots_small_4.png", "marble/bestspots_small_5.png",
                            "marble/bestspots_small_6.png", "marble/bestspots_small_7.png", "marble/bestspots_small_8.png", "marble/bestspots_small_9.png", "marble/bestspots_small_10.png"},
                    {"marble/bestspots_medium_1.png", "marble/bestspots_medium_2.png", "marble/bestspots_medium_3.png", "marble/bestspots_medium_4.png", "marble/bestspots_medium_5.png",
                            "marble/bestspots_medium_6.png", "marble/bestspots_medium_7.png", "marble/bestspots_medium_8.png", "marble/bestspots_medium_9.png", "marble/bestspots_medium_10.png"},
                    {"marble/bestspots_large_1.png", "marble/bestspots_large_2.png", "marble/bestspots_large_3.png", "marble/bestspots_large_4.png", "marble/bestspots_large_5.png",
                            "marble/bestspots_large_6.png", "marble/bestspots_large_7.png", "marble/bestspots_large_8.png", "marble/bestspots_large_9.png", "marble/bestspots_large_10.png"},
                    {"marble/bestspots_xl_1.png", "marble/bestspots_xl_2.png", "marble/bestspots_xl_3.png", "marble/bestspots_xl_4.png", "marble/bestspots_xl_5.png",
                            "marble/bestspots_xl_6.png", "marble/bestspots_xl_7.png", "marble/bestspots_xl_8.png", "marble/bestspots_xl_9.png", "marble/bestspots_xl_5.png"},
                    {"mask/solid.png"}
            },
    };


    private static final String[][][] TEXTURES_FINS = new String[][][]{
            {
                    {"fin/plakat.png", "fin/plakat_hetcrown.png", "fin/plakat_homocrown.png"},
                    {"fin/plakat_double.png", "fin/plakat_double_hetcrown.png", "fin/plakat_double_homocrown.png"},
            },
            {
                    {"fin/dpk.png", "fin/dpk_hetcrown.png", "fin/dpk_homocrown.png"},
                    {"fin/dpk_double.png", "fin/dpk_double_hetcrown.png", "fin/dpk_double_homocrown.png"},
            },
            {
                    {"fin/hmpk.png", "fin/hmpk_hetcrown.png", "fin/hmpk_homocrown.png"},
                    {"fin/hmpk_double.png", "fin/hmpk_double_hetcrown.png", "fin/hmpk_double_homocrown.png"},
            },
            {
                    {"fin/veil.png", "fin/veil_hetcrown.png", "fin/veil_homocrown.png"},
                    {"fin/veil_double.png", "fin/veil_double_hetcrown.png", "fin/veil_double_homocrown.png"},
            },
            {
                    {"fin/delta.png", "fin/delta_hetcrown.png", "fin/delta_homocrown.png"},
                    {"fin/delta_double.png", "fin/delta_double_hetcrown.png", "fin/delta_double_homocrown.png"},
            },
            {
                    {"fin/halfmoon.png", "fin/halfmoon_hetcrown.png", "fin/halfmoon_homocrown.png"},
                    {"fin/halfmoon_double.png", "fin/halfmoon_double_hetcrown.png", "fin/halfmoon_double_homocrown.png"},
            },
            {
                    {"fin/babyfish.png", "fin/babyfish_hetcrown.png", "fin/babyfish_homocrown.png"},
                    {"fin/babyfish_double.png", "fin/babyfish_double_hetcrown.png", "fin/babyfish_double_homocrown.png"},
            },
    };

    private static final String[] TEXTURES_DUMBO = new String[]{
            "fin/dumbo.png", "fin/dumbo_hetcrown.png", "fin/dumbo_homocrown.png"
    };

    private static final String[] TEXTURES_BABY = new String[]{
            "", "baby_stripes.png"
    };
    private static final String[] TEXTURES_DUMBO_BUTTERFLY = new String[]{
            "", "butterfly/dumbo_low.png", "butterfly/dumbo_med.png", "butterfly/dumbo_high.png", "butterfly/dumbo_max.png",
    };

    private static final String[] TEXTURES_DUMBO_ALPHA = new String[]{
            "mask/nondumbo.png", "mask/solid.png"
    };
    private static final String[][][] TEXTURES_BUTTERFLY = new String[][][]{
            {
                    {"", "butterfly/plakat_low.png", "butterfly/plakat_med.png", "butterfly/plakat_high.png", "butterfly/plakat_max.png"},
                    {"", "butterfly/plakat_double_low.png", "butterfly/plakat_double_med.png", "butterfly/plakat_double_high.png", "butterfly/plakat_double_max.png"},
            },
            {
                    {"", "butterfly/dpk_low.png", "butterfly/dpk_med.png", "butterfly/dpk_high.png", "butterfly/dpk_max.png"},
                    {"", "butterfly/dpk_double_low.png", "butterfly/dpk_double_med.png", "butterfly/dpk_double_high.png", "butterfly/dpk_double_max.png"},
            },
            {
                    {"", "butterfly/hmpk_low.png", "butterfly/hmpk_med.png", "butterfly/hmpk_high.png", "butterfly/hmpk_max.png"},
                    {"", "butterfly/hmpk_double_low.png", "butterfly/hmpk_double_med.png", "butterfly/hmpk_double_high.png", "butterfly/hmpk_double_max.png"},
            },
            {
                    {"", "butterfly/veil_low.png", "butterfly/veil_med.png", "butterfly/veil_high.png", "butterfly/veil_max.png"},
                    {"", "butterfly/veil_double_low.png", "butterfly/veil_double_med.png", "butterfly/veil_double_high.png", "butterfly/veil_double_max.png"},
            },
            {
                    {"", "butterfly/delta_low.png", "butterfly/delta_med.png", "butterfly/delta_high.png", "butterfly/delta_max.png"},
                    {"", "butterfly/delta_double_low.png", "butterfly/delta_double_med.png", "butterfly/delta_double_high.png", "butterfly/delta_double_max.png"},
            },
            {
                    {"", "butterfly/halfmoon_low.png", "butterfly/halfmoon_med.png", "butterfly/halfmoon_high.png", "butterfly/halfmoon_max.png"},
                    {"", "butterfly/halfmoon_double_low.png", "butterfly/halfmoon_double_med.png", "butterfly/halfmoon_double_high.png", "butterfly/halfmoon_double_max.png"},
            },
            {
                    {"", "butterfly/plakat_low.png", "butterfly/plakat_med.png", "butterfly/plakat_high.png", "butterfly/plakat_max.png"},
                    {"", "butterfly/plakat_double_low.png", "butterfly/plakat_double_med.png", "butterfly/plakat_double_high.png", "butterfly/plakat_double_max.png"},
            },
    };

    private static final String[][][] TEXTURES_IRI_RIM = new String[][][]{
            {
                    {"", "iri/rim/plakat_low.png", "iri/rim/plakat_med.png", "iri/rim/plakat_high.png", "iri/rim/plakat_max.png"},
                    {"", "iri/rim/plakat_double_low.png", "iri/rim/plakat_double_med.png", "iri/rim/plakat_double_high.png", "iri/rim/plakat_double_max.png"},
            },
            {
                    {"", "iri/rim/dpk_low.png", "iri/rim/dpk_med.png", "iri/rim/dpk_high.png", "iri/rim/dpk_max.png"},
                    {"", "iri/rim/dpk_double_low.png", "iri/rim/dpk_double_med.png", "iri/rim/dpk_double_high.png", "iri/rim/dpk_double_max.png"},
            },
            {
                    {"", "iri/rim/hmpk_low.png", "iri/rim/hmpk_med.png", "iri/rim/hmpk_high.png", "iri/rim/hmpk_max.png"},
                    {"", "iri/rim/hmpk_double_low.png", "iri/rim/hmpk_double_med.png", "iri/rim/hmpk_double_high.png", "iri/rim/hmpk_double_max.png"},
            },
            {
                    {"", "iri/rim/veil_low.png", "iri/rim/veil_med.png", "iri/rim/veil_high.png", "iri/rim/veil_max.png"},
                    {"", "iri/rim/veil_double_low.png", "iri/rim/veil_double_med.png", "iri/rim/veil_double_high.png", "iri/rim/veil_double_max.png"},
            },
            {
                    {"", "iri/rim/delta_low.png", "iri/rim/delta_med.png", "iri/rim/delta_high.png", "iri/rim/delta_max.png"},
                    {"", "iri/rim/delta_double_low.png", "iri/rim/delta_double_med.png", "iri/rim/delta_double_high.png", "iri/rim/delta_double_max.png"},
            },
            {
                    {"", "iri/rim/halfmoon_low.png", "iri/rim/halfmoon_med.png", "iri/rim/halfmoon_high.png", "iri/rim/halfmoon_max.png"},
                    {"", "iri/rim/halfmoon_double_low.png", "iri/rim/halfmoon_double_med.png", "iri/rim/halfmoon_double_high.png", "iri/rim/halfmoon_double_max.png"},
            },
            {
                    {"", "iri/rim/plakat_low.png", "iri/rim/plakat_med.png", "iri/rim/plakat_high.png", "iri/rim/plakat_max.png"},
                    {"", "iri/rim/plakat_double_low.png", "iri/rim/plakat_double_med.png", "iri/rim/plakat_double_high.png", "iri/rim/plakat_double_max.png"},
            },
    };

    private static final String[] TEXTURES_HYBRID_IRI = new String[]{
            "", "iri/hybrid_iri.png"
    };

    private static final String[] TEXTURES_VANDA = new String[]{
            "red/vanda/vanda_1.png", "red/vanda/vanda_2.png", "red/vanda/vanda_3.png", "red/vanda/vanda_4.png",
            "red/vanda/vanda_high_1.png", "red/vanda/vanda_high_2.png", "red/vanda/vanda_high_3.png","red/vanda/vanda_high_4.png", "red/vanda/vanda_high_5.png", "red/vanda/vanda_high_6.png", "red/vanda/vanda_high_7.png"
    };
    private boolean resetTexture = true;

    @OnlyIn(Dist.CLIENT)
    private BettaModelData bettaModelData;

    @OnlyIn(Dist.CLIENT)
    private Genes getClientSidedGenes() {
        if (this.genesSplitForClient == null) {
            String sharedGenes = this.entityData.get(SHARED_GENES);
            if (sharedGenes.isEmpty()) {
                return null;
            }

            Genes genes = new Genes(sharedGenes);

            this.genesSplitForClient = genes;
            return genes;
        }
        return this.genesSplitForClient;
    }

    @Override
    protected void fixGeneLengths() {
    }

    public Genes getGenes() {
        if (this.level instanceof ServerLevel) {
            return this.genetics;
        } else {
            return this.getClientSidedGenes();
        }
    }

    public EnhancedBetta(EntityType<? extends EnhancedBetta> entityType, Level worldIn) {
        super(entityType, worldIn, AddonReference.BETTA_SEXLINKED_GENES_LENGTH, AddonReference.BETTA_AUTOSOMAL_GENES_LENGTH, true);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0F);
        this.moveControl = new EnhancedBetta.BettaMoveControl(this);
        this.lookControl = new EnhancedBetta.BettaLookControl(this, 20);
//        this.maxUpStep = 1.0F;
        this.initilizeAnimalSize();
    }

    @Override
    public float getStepHeight() {
        return 0.75F;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_FLARING, false);
        this.entityData.define(FROM_BUCKET, false);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }

    @Override
    protected void initializeHealth(EnhancedAnimalAbstract animal, float health) {
        int[] genes = this.getGenes().getAutosomalGenes();
        health = 8F;
        if (genes[62] == 2 && genes[63] == 2) {
            //Homo doubletail decreases health
            health -= 2F;
        }
        if (genes[172] == 2 && genes[173] == 2) {
            //Homo dragonscale decreases health
            health -= 2F;
        }
        super.initializeHealth(animal, health);
    }

    @Override
    protected String getSpecies() {
        return I18n.get(speciesTranslationKey);
    }

    @Override
    protected int getAdultAge() {
        return BettasCommonConfig.COMMON.bettaAdultAge.get();
    }

    @Override
    protected int gestationConfig() {
        return 0;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor inWorld, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData livingData, @Nullable CompoundTag itemNbt) {
        return commonInitialSpawnSetup(inWorld, livingData, 60000, 30000, 80000, spawnReason);
    }

    @Override
    protected void incrementHunger() {}

    @Override
    protected void runExtraIdleTimeTick() {
    }

    public void lethalGenes() {
    }

    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.enhancedAnimalTextureGrouping == null) {
            this.setTexturePaths();
        } else if (this.resetTexture && !this.isVeryBaby()) {
            this.resetTexture = false;
            this.reloadTextures();
        }

        return getCompiledTextures("enhanced_betta");
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.getBrain().eraseMemory(AddonMemoryModuleTypes.PAUSE_BRAIN.get());
        this.getBrain().eraseMemory(AddonMemoryModuleTypes.FOCUS_BRAIN.get());

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void setTexturePaths() {
        if (this.getGenes() != null || this.genetics != null) {
            int[] gene = this.getGenes() != null ? getGenes().getAutosomalGenes() : genetics.getAutosomalGenes();

            char[] uuidArry = getStringUUID().toCharArray();

            boolean babyColors = this.isVeryBaby();

            /*** PHENOTYPE ***/
            int bodyIri = 1;
            int finIri = 1;
            int maskIri = 0;
            int fins = 0;
            int doubletail = 0;
            int crowntail = 0;
            int finAlpha = 0;
            int finRed = 4;
            int bodyRed = 2;
            int butterfly = 0;
            int iriRim = 0;
            int iriIntensity = 3;
            boolean pastelOpaque = false;
            boolean cambodian = false;
            int metallic = 0;
            int marbleRedQual = 0;
            int marbleRedSize = 0;
            int marbleRedRand = 0;
            int marbleBlackQual = 0;
            int marbleBlackSize = 0;
            int marbleBlackRand = 0;
            int marbleIriQual = 0;
            int marbleIriSize = 0;
            int marbleIriRand = 0;
            int marbleOpaqueQual = 0;
            int marbleOpaqueSize = 0;
            int marbleOpaqueRand = 0;
            int marbleBloodredQual = 0;
            int marbleBloodredSize = 0;
            int marbleBloodredRand = 0;
            int eyeLeft = 0;
            int eyeRight = 0;
            boolean vanda = false;
            int vandaLevel = 0;
            boolean dumbo = false;
            boolean isMale = !getOrSetIsFemale();
            boolean bloodred = false;
            boolean extendedRed = false;
            int finBloodred = 4;
            int bodyBloodred = 2;
            int dragonscale = 0;
            int hybridIri = 0;

            /*** COLORATION ***/
            float[] melanin = {0.0427F, 0.527F, 0.251F};
            float[] pheomelanin = {0.991F, 0.978F, 0.655F};
            float[] bloodredColor = {0.996F, 0.978F, 0.405F};
            float[] cellophane = getHSBFromHex("ebe8e4");
            float[] iridescenceLight = {0.44F, 0.978F, 0.878F};
            float[] iridescence = {0.530F, 0.715F, 0.634F};
            float[] iridescenceDark = {0.582F, 0.808F, 0.604F};
            /* Opaque/Pastel normally appears as a modifier of iridescence,
                but it needs separate colors + its own layer since it marbles separately
             */
            float[] opaqueLight = {0.44F, 0.095F, 0.953F};
            float[] opaque = {0.530F, 0.093F, 0.95F};
            float[] opaqueDark = {0.582F, 0.166F, 0.922F};
            float[] metallic1 = getHSBFromHex("00ab74");
            float[] metallic2 = getHSBFromHex("00ab74");
            float[] metallic3 = getHSBFromHex("e7d094");

            /*** IRIDESCENCE ***/
            //Body Iridescence Level
            int bodyIriLevel = 0;
            for (int i = 24; i < 28; i++) {
                if (gene[i] == 2) {
                    bodyIriLevel++;
                }
            }
            bodyIri += bodyIriLevel / 2;

            //Fin Iridescence Level
            int finIriLevel = 0;
            for (int i = 28; i < 32; i++) {
                if (gene[i] == 2) {
                    finIriLevel++;
                }
            }
            finIri += finIriLevel / 2;

            if ((gene[32] == 2 || gene[33] == 2) && gene[32] != 3 && gene[33] != 3) {
                //Higher Iridescence Intensity
                iriIntensity++;
            }
            else if (gene[32] == 3 && gene[33] == 3) {
                //Recessive Lower Iri Allele so iridescence can be bred out
                iriIntensity--;
            }

            if (gene[34] == 2 || gene[35] == 2) {
                //Lower Iridescence Intensity
                iriIntensity -= gene[34] == gene[35] ? 2 : 1;
            }

            if (gene[2] == 2 || gene[3] == 2) {
                //Spread
//                iriIntensity = iriIntensity < 2 ? iriIntensity+2 : 4;
                bodyIri = iriIntensity < 2 ? 5 : 4;
                finIri = 0;
                iriIntensity = 4;
            }

            if (gene[4] == 2 || gene[5] == 2) {
                //Masked

                //Mask Iridescence Level
                int maskIriLevel = 0;
                for (int i = 50; i < 56; i++) {
                    if (gene[i] == 2) {
                        maskIriLevel++;
                    }
                }
                maskIriLevel = maskIriLevel / 2;
                switch (maskIriLevel) {
                    case 0 -> maskIri = 1;
                    case 1 -> maskIri = 3;
                    case 2 -> maskIri = 5;
                    case 3 -> maskIri = 7;
                }
            }

            /*** DRAGONSCALE ***/
            if (gene[172] == 2 || gene[173] == 2) {
                boolean dragonscaleMod = gene[234] == 2 || gene[235] == 2;
                int dragonRand = uuidArry[9];
                if (gene[172] == gene[173]) {
                    //Homo
                    dragonscale = dragonscaleMod ? (5 + (dragonRand % 5)) : (10 + (dragonRand % 2));
                }
                else {
                    //Het
                    dragonscale = dragonscaleMod ? (1 + (dragonRand % 3)) : 4;
                }
            }


            /*** RED ***/
            for (int i = 36; i < 40; i++) {
                // Lower Fin Red
                if (gene[i] == 2) {
                    finRed--;
                }
            }

            //Body Red
            if (gene[44] == 2) {
                //Higher Body Red
                bodyRed++;
            }
            if (gene[45] == 2) {
                //Higher Body Red
                bodyRed++;
            }
            if (gene[46] == 2) {
                //Lower Body Red
                bodyRed--;
            }
            if (gene[47] == 2) {
                //Lower Body Red
                bodyRed--;
            }

            if (gene[12] == 2 || gene[13] == 2) {
                //Extended Red
                extendedRed = true;
                bodyRed = 5;
                if (gene[16] == 2 && gene[17] == 2) {
                    //Homozygous Masked
                    bodyRed = 7;
                } else if (gene[16] == 2 || gene[17] == 2) {
                    //Heterozygous Masked
                    bodyRed = 6;
                }
            }

            /*** BLOODRED ***/
            for (int i = 158; i < 162; i++) {
                // Lower Fin Red
                if (gene[i] == 2) {
                    finBloodred--;
                }
            }

            //Body Red
            if (gene[162] == 2) {
                //Higher Body Bloodred
                bodyBloodred++;
            }
            if (gene[163] == 2) {
                //Higher Body Bloodred
                bodyBloodred++;
            }
            if (gene[164] == 2) {
                //Lower Body Bloodred
                bodyBloodred--;
            }
            if (gene[165] == 2) {
                //Lower Body Bloodred
                bodyBloodred--;
            }

            if (gene[154] == 2 || gene[155] == 2) {
                //Extended Bloodred
                bodyBloodred = 5;
                if (gene[156] == 2 && gene[157] == 2) {
                    //Homozygous Masked
                    bodyBloodred = 7;
                } else if (gene[156] == 2 || gene[157] == 2) {
                    //Heterozygous Masked
                    bodyBloodred = 6;
                }
            }


            if (gene[48] == 2 || gene[49] == 2) {
                metallic = gene[48] == gene[49] ? 2 : 1;
            }

            // Iridescence Color
            if (gene[0] == 2 && gene[1] == 2) {
                //Steel Blue
                metallic1 = getHSBFromHex("968967");
                metallic2 = getHSBFromHex("663b45");
                metallic3 = getHSBFromHex("e8cf8f");
                if (metallic == 2) {
                    iridescenceLight = getHSBFromHex("989b86");
                    iridescence = getHSBFromHex("6f765e");
                    iridescenceDark = getHSBFromHex("515543");
                } else if (metallic == 1) {
                    iridescenceLight = getHSBFromHex("89a5a6");
                    iridescence = getHSBFromHex("597d86");
                    iridescenceDark = getHSBFromHex("3e5c62");
                    metallic2 = getHSBFromHex("A7C7A5");
                } else {
                    iridescenceLight[0] = 0.533F;
                    iridescenceLight[1] = 0.248F;
                    iridescenceLight[2] = 0.792F;
                    iridescence[0] = 0.556F;
                    iridescence[1] = 0.443F;
                    iridescence[2] = 0.655F;
                    iridescenceDark[0] = 0.551F;
                    iridescenceDark[1] = 0.480F;
                    iridescenceDark[2] = 0.482F;
                }
            } else if (gene[0] == 2 || gene[1] == 2) {
                //Royal Blue (het steel blue)
                metallic1 = getHSBFromHex("418bb1");
                metallic2 = getHSBFromHex("00AC73");
                metallic3 = getHSBFromHex("88e8ff");
                if (metallic == 2) {
                    iridescenceLight = getHSBFromHex("55d9ea");
                    iridescence = getHSBFromHex("2a8fab");
                    iridescenceDark = getHSBFromHex("296d88");
                } else if (metallic == 1) {
                    iridescenceLight = getHSBFromHex("5cb6dd");
                    iridescence = getHSBFromHex("2c72a5");
                    iridescenceDark = getHSBFromHex("2c5882");
                    metallic2 = getHSBFromHex("A7C7A5");
                } else {
                    iridescenceLight[0] = 0.609F;
                    iridescenceLight[1] = 0.543F;
                    iridescenceLight[2] = 0.788F;
                    iridescence[0] = 0.635F;
                    iridescence[1] = 0.706F;
                    iridescence[2] = 0.623F;
                    iridescenceDark[0] = 0.647F;
                    iridescenceDark[1] = 0.713F;
                    iridescenceDark[2] = 0.475F;
                }
            } else if (metallic == 2) {
                //Turquoise Metallic
                iridescenceLight = getHSBFromHex("6fa970");
                iridescence = getHSBFromHex("318c64");
                iridescenceDark = getHSBFromHex("218074");
            } else if (metallic == 1) {
                //Turquoise Het Metallic
                iridescenceLight = getHSBFromHex("38c380");
                iridescence = getHSBFromHex("349c95");
                iridescenceDark = getHSBFromHex("217695");
            }

            //Main Red Color
            if (gene[42] == 2 && gene[43] == 2) {
                //Orange
                pheomelanin = getHSBFromHex("D44407");
            } else if (gene[42] == 3 && gene[43] == 3) {
                //Yellow
                pheomelanin = getHSBFromHex("FFBF43");
            }

            if (gene[40] == 2 || gene[41] == 2) {
                pastelOpaque = true;
                if (gene[40] == gene[41]) {
                    //Homo Pastel/Opaque
                    opaqueLight[0] = iridescenceLight[0];
                    opaque[0] = iridescenceLight[0];
                    opaqueDark[0] = iridescenceLight[0];
                    metallic1[1] -= 0.5F;
                    metallic1[2] += 0.5F;
                    metallic2[1] -= 0.5F;
                    metallic2[2] += 0.5F;
                    metallic3[1] -= 0.5F;
                    metallic3[2] += 0.5F;
                } else {
                    //Het Pastel/Opaque
                    if (gene[0] == 2 && gene[1] == 2) {
                        //SteelBlue
                        opaqueLight = getHSBFromHex("dceef3");
                        opaque = getHSBFromHex("c4deeb");
                        opaqueDark = getHSBFromHex("aac9d8");
                    } else if (gene[0] == 2 || gene[1] == 2) {
                        //RoyalBlue
                        opaqueLight = getHSBFromHex("c5d4fe");
                        opaque = getHSBFromHex("a4b5fd");
                        opaqueDark = getHSBFromHex("949ff3");
                    } else {
                        //Turquoise
                        opaqueLight = getHSBFromHex("92fed8");
                        opaque = getHSBFromHex("9be8f9");
                        opaqueDark = getHSBFromHex("91c8fe");
                    }
                }
            }

            // Black
            if (gene[8] == 2 && gene[9] == 2) {
                //Laced Black
                if (gene[6] == 2 && gene[7] == 2) {
                    //Double Black/Super Black
                    melanin[1] = 0.171F;
                    melanin[2] = 0.081F;
                } else {
                    //Just Laced Black
                    finAlpha = 1;
                    melanin[1] = 0.091F;
                    melanin[2] = 0.151F;
                }
            } else if (gene[6] == 2 && gene[7] == 2) {
                //Melano Black
                melanin[1] = 0.125F;
                melanin[2] = 0.115F;
            }

            if (gene[10] == 2 && gene[11] == 2) {
                //Cambodian
                cambodian = true;
                finAlpha = 2;
//                shading = 1;
//                bodyRed = 1;
                melanin = getHSBFromHex("D1C5B7");
            }

            if (gene[14] == 2 && gene[15] == 2) {
                //Blonde
                melanin[1] -= 0.1F;
                melanin[2] += 0.1F;
                pheomelanin[1] -= 0.3F;
                pheomelanin[2] += 0.2F;
                bloodredColor[1] -= 0.3F;
                bloodredColor[2] += 0.2F;
                iridescence[1] -= 0.3F;
                iridescence[2] += 0.2F;
                iridescenceLight[1] -= 0.3F;
                iridescenceLight[2] += 0.2F;
                iridescenceDark[1] -= 0.3F;
                iridescenceDark[2] += 0.2F;
            }

            if (gene[8] == 2 && gene[9] == 2) {
                //Laced Black should make cellophane match melanin
                cellophane[0] = melanin[0];
                cellophane[1] = melanin[1];
                cellophane[2] = melanin[2];
            }

            if (gene[18] == 2 || gene[19] == 2) {
                //Butterfly
                butterfly = 1;
                //Butterfly Extensions
                if (gene[20] == 2) {
                    butterfly += 1;
                }
                if (gene[21] == 2) {
                    butterfly += 1;
                }
                if (gene[22] == 2 || gene[23] == 2) {
                    butterfly += 1;
                }
            }

            if (gene[66] == 2 || gene[67] == 2) {
                //Iridescent Rims
                iriRim = 1;
                //Iri Rim Extensions
                if (gene[68] == 2) {
                    iriRim += 1;
                }
                if (gene[69] == 2) {
                    iriRim += 1;
                }
                if (gene[70] == 2 || gene[71] == 2) {
                    iriRim += 1;
                }
                if (iriRim == butterfly+1 && iriRim+1 <= 4) {
                    //if the grade of iri rims is one higher than the butterfly grade, increase the iri rims grade by 1 to prevent weirdy buggy/overlap
                    iriRim +=1;
                }
            }

            //Hybrid Iridescence
            if (gene[236] == 2 || gene[237] == 2) {
                hybridIri = 1;
            }

            if (gene[152] == 2 || gene[153] == 2) {
                bloodred = true;
            }


            //FINS
            if (gene[56] == 2 && gene[57] == 2) {
                dumbo = true;
            }
            if (babyColors) {
                fins = 6;
            }
            else if (gene[58] == 2 || gene[59] == 2) {
                //Long Fins
                if (gene[60] == 2 || gene[61] == 2) {
                    if (gene[60] == gene[61]) {
                        //Halfmoon
                        fins = isMale ? 5 : 2;
                    } else {
                        //Delta
                        fins = isMale ? 4 : 1;
                    }
                } else {
                    //Veil
                    fins = isMale ? 3 : 0;
                }
            } else {
                //Short Fins
                if (gene[60] == 2 || gene[61] == 2) {
                    if (gene[60] == gene[61]) {
                        //Halfmoon Plakat
                        fins = isMale ? 2 : 0;
                    } else {
                        //Delta Plakat
                        fins = isMale ? 1 : 0;
                    }
                } else {
                    //Plakat
                    fins = 0;
                }
            }

            if (gene[62] == 2 && gene[63] == 2) {
                //Doubletail
                doubletail = 1;
            }

            if (gene[64] == 2 || gene[65] == 2) {
                if (gene[64] == gene[65]) {
                    //Homo Crowntail
                    crowntail = 2;
                } else {
                    //Het Crowntail
                    crowntail = 1;
                }
            }

            if (gene[80] == 2 || gene[81] == 2) {
                //Marble
                if (babyColors) {
                    //Set all babies to quality 1 without setting size, so they defualt to transparent for babies
                    marbleRedQual=1;
                    marbleBlackQual=1;
                    marbleBloodredQual=1;
                    marbleIriQual=1;
                    marbleOpaqueQual=1;
                }
                else {
                    //Red Marble
                    //Size
                    int marbleRedSizeMod = 0;
                    for (int i = 82; i < 86; i++) {
                        if (gene[i] == 2) {
                            marbleRedSizeMod--;
                        }
                    }
                    for (int i = 86; i < 92; i++) {
                        if (gene[i] == 2) {
                            marbleRedSizeMod++;
                        }
                    }
                    marbleRedSize = 2 + (marbleRedSizeMod / 2);

                    //Quality
                    int marbleRedQualMod = 0;
                    for (int i = 92; i < 96; i++) {
                        marbleRedQualMod += gene[i] - 1;
                    }
                    if (marbleRedQualMod < 4) {
                        marbleRedQual = 1;
                    } else if (marbleRedQualMod < 8) {
                        marbleRedQual = 2;
                    } else if (marbleRedQualMod < 12) {
                        marbleRedQual = 3;
                    } else if (marbleRedQualMod < 16) {
                        marbleRedQual = 4;
                    } else {
                        marbleRedQual = 5;
                    }

                    //Random
                    if (marbleRedSize != 0 && marbleRedSize != 5) {
                        marbleRedRand += uuidArry[3] % (marbleRedQual > 2 ? 10 : 5);
                    }


                    // Black Marble
                    //Size
                    int marbleBlackSizeMod = 0;
                    for (int i = 96; i < 100; i++) {
                        if (gene[i] == 2) {
                            marbleBlackSizeMod--;
                        }
                    }
                    for (int i = 100; i < 106; i++) {
                        if (gene[i] == 2) {
                            marbleBlackSizeMod++;
                        }
                    }
                    marbleBlackSize = 2 + (marbleBlackSizeMod / 2);

                    //Quality
                    int marbleBlackQualMod = 0;
                    for (int i = 106; i < 110; i++) {
                        marbleBlackQualMod += gene[i] - 1;
                    }
                    if (marbleBlackQualMod < 4) {
                        marbleBlackQual = 1;
                    } else if (marbleBlackQualMod < 8) {
                        marbleBlackQual = 2;
                    } else if (marbleBlackQualMod < 12) {
                        marbleBlackQual = 3;
                    } else if (marbleBlackQualMod < 16) {
                        marbleBlackQual = 4;
                    } else {
                        marbleBlackQual = 5;
                    }

                    //Random
                    if (marbleBlackSize != 0 && marbleBlackSize != 5) {
                        marbleBlackRand += uuidArry[4] % (marbleBlackQual > 2 ? 10 : 5);
                    }

                    // Bloodred Marble
                    //Size
                    int marbleBloodredSizeMod = 0;
                    for (int i = 110; i < 114; i++) {
                        if (gene[i] == 2) {
                            marbleBloodredSizeMod--;
                        }
                    }
                    for (int i = 114; i < 120; i++) {
                        if (gene[i] == 2) {
                            marbleBloodredSizeMod++;
                        }
                    }
                    marbleBloodredSize = 2 + (marbleBloodredSizeMod / 2);

                    //Quality
                    int marbleBloodredQualMod = 0;
                    for (int i = 120; i < 124; i++) {
                        marbleBloodredQualMod += gene[i] - 1;
                    }
                    if (marbleBloodredQualMod < 4) {
                        marbleBloodredQual = 1;
                    } else if (marbleBloodredQualMod < 8) {
                        marbleBloodredQual = 2;
                    } else if (marbleBloodredQualMod < 12) {
                        marbleBloodredQual = 3;
                    } else if (marbleBloodredQualMod < 16) {
                        marbleBloodredQual = 4;
                    } else {
                        marbleBloodredQual = 5;
                    }

                    //Random
                    if (marbleBloodredSize != 0 && marbleBloodredSize != 5) {
                        marbleBloodredRand += uuidArry[5] % (marbleBloodredQual > 2 ? 10 : 5);
                    }

                    // Iri Marble
                    //Size
                    int marbleIriSizeMod = 0;
                    for (int i = 124; i < 128; i++) {
                        if (gene[i] == 2) {
                            marbleIriSizeMod--;
                        }
                    }
                    for (int i = 128; i < 134; i++) {
                        if (gene[i] == 2) {
                            marbleIriSizeMod++;
                        }
                    }
                    marbleIriSize = 2 + (marbleIriSizeMod / 2);

                    //Quality
                    int marbleIriQualMod = 0;
                    for (int i = 134; i < 138; i++) {
                        marbleIriQualMod += gene[i] - 1;
                    }
                    if (marbleIriQualMod < 4) {
                        marbleIriQual = 1;
                    } else if (marbleIriQualMod < 8) {
                        marbleIriQual = 2;
                    } else if (marbleIriQualMod < 12) {
                        marbleIriQual = 3;
                    } else if (marbleIriQualMod < 16) {
                        marbleIriQual = 4;
                    } else {
                        marbleIriQual = 5;
                    }

                    //Random
                    if (marbleIriSize != 0 && marbleIriSize != 5) {
                        marbleIriRand += uuidArry[6] % (marbleIriQual > 2 ? 10 : 5);
                    }

                    // Opaque Marble
                    //Size
                    int marbleOpaqueSizeMod = 0;
                    for (int i = 138; i < 142; i++) {
                        if (gene[i] == 2) {
                            marbleOpaqueSizeMod--;
                        }
                    }
                    for (int i = 142; i < 148; i++) {
                        if (gene[i] == 2) {
                            marbleOpaqueSizeMod++;
                        }
                    }
                    marbleOpaqueSize = 2 + (marbleOpaqueSizeMod / 2);

                    //Quality
                    int marbleOpaqueQualMod = 0;
                    for (int i = 148; i < 152; i++) {
                        marbleOpaqueQualMod += gene[i] - 1;
                    }
                    if (marbleOpaqueQualMod < 4) {
                        marbleOpaqueQual = 1;
                    } else if (marbleOpaqueQualMod < 8) {
                        marbleOpaqueQual = 2;
                    } else if (marbleOpaqueQualMod < 12) {
                        marbleOpaqueQual = 3;
                    } else if (marbleOpaqueQualMod < 16) {
                        marbleOpaqueQual = 4;
                    } else {
                        marbleOpaqueQual = 5;
                    }

                    //Random
                    if (marbleOpaqueSize != 0 && marbleOpaqueSize != 5) {
                        marbleOpaqueRand += uuidArry[7] % (marbleOpaqueQual > 2 ? 10 : 5);
                    }
                }
                // Eye Marble
                eyeLeft = uuidArry[10] > 'b' ? 1 : uuidArry[10] > '7' ? 2 : 0;
                eyeRight = uuidArry[11] > 'b' ? 1 : uuidArry[11] > '7' ? 2 : 0;
            } else if (gene[80] == 3 || gene[81] == 3) {
                //Vanda
                vanda = true;
                //Size
                int vandaSizeMod = 0;
                for (int i = 110; i < 114; i++) {
                    if (gene[i] == 2) {
                        vandaSizeMod--;
                    }
                }
                for (int i = 114; i < 120; i++) {
                    if (gene[i] == 2) {
                        vandaSizeMod++;
                    }
                }
                if (vandaSizeMod > 0) vandaLevel = 4;
                vandaLevel += uuidArry[5] % (vandaSizeMod > 0 ? 7 : 4);
            }

            // Fine Red Rufousing Genes
            int r = 0;
            for (int i = 174; i < 194; i++) {
                if (gene[i] == 2) {
                    r += i < 184 ? -1 : +1;
                }
            }

            if (gene[42] == 2 && gene[43] == 2) {
                //Orange
                if (r < 0) pheomelanin[0] += r * 0.0005F; // Light orange looks sort of pink-ish
                pheomelanin[1] += r * 0.02F;
                pheomelanin[2] -= r * (r > 0 ? 0.03F : 0.001F);
            }
            else if (gene[42] == 3 && gene[43] == 3) {
                //Yellow
                if (r > 0) pheomelanin[0] -= r * 0.0005F;
                pheomelanin[1] += r * (r > 0 ? 0.02F : 0.01F);
                pheomelanin[2] -= r * (r > 0 ? 0.01F : 0.001F);
            }
            else {
                //Red
                pheomelanin[1] += r * 0.02F;
                pheomelanin[2] -= r * (r > 0 ? 0.03F : 0.002F);
            }

            // Fine BloodRed Rufousing Genes
            r = 0;
            for (int i = 194; i < 214; i++) {
                if (gene[i] == 2) {
                    r += i < 204 ? -1 : +1;
                }
            }

            bloodredColor[1] += r * 0.01F;
            bloodredColor[2] -= r * (r > 0 ? 0.015F : 0.02F);

            // Fine Iridescence Hue Genes
            r = 0;
            for (int i = 214; i < 234; i++) {
                if (gene[i] == 2) {
                    r += i < 224 ? -1 : +1;
                }
            }

            iridescence[0] += r * 0.005F;
            iridescenceLight[0] += r * 0.005F;
            iridescenceDark[0] += r * 0.005F;
            opaque[0] = iridescence[0];
            opaqueLight[0] = iridescenceLight[0];
            opaqueDark[0] = iridescenceDark[0];
            if (gene[0] == 2 && gene[1] == 2) {
                metallic1[1] -= r * 0.02F;
                metallic2[1] -= r * 0.02F;
                metallic3[1] -= r * 0.02F;
                iridescence[1] -= r * 0.01F;
                iridescenceLight[1] -= r * 0.01F;
                iridescenceDark[1] -= r * 0.01F;
            }
            else {
                metallic1[0] += r * 0.005F;
                metallic2[0] += r * 0.005F;
                metallic3[0] += r * 0.005F;
            }

            melanin = clampRGB(melanin);
            pheomelanin = clampRGB(pheomelanin);
            bloodredColor = clampRGB(bloodredColor);
            iridescence = clampRGB(iridescence);
            iridescenceLight = clampRGB(iridescenceLight);
            iridescenceDark = clampRGB(iridescenceDark);
            opaque = clampRGB(opaque);
            opaqueLight = clampRGB(opaqueLight);
            opaqueDark = clampRGB(opaqueDark);
            metallic1 = clampRGB(metallic1);
            metallic2 = clampRGB(metallic2);
            metallic3 = clampRGB(metallic3);

            int metallic1RGB = Colouration.HSBtoARGB(metallic1[0], metallic1[1], metallic1[2]);
            int metallic2RGB = Colouration.HSBtoARGB(metallic2[0], metallic2[1], metallic2[2]);
            int metallic3RGB = Colouration.HSBtoARGB(metallic3[0], metallic3[1], metallic3[2]);

            float saturationMult = babyColors ? 0.75F : 1F;

            int pheomelaninRGB = Colouration.HSBtoABGR(pheomelanin[0], pheomelanin[1]*saturationMult, pheomelanin[2]);
            int melaninRGB = Colouration.HSBtoABGR(melanin[0], melanin[1]*saturationMult, melanin[2]);
            int bloodredRGB = Colouration.HSBtoARGB(bloodredColor[0], bloodredColor[1]*saturationMult, bloodredColor[2]);
            int iriRGB = Colouration.HSBtoARGB(iridescence[0], iridescence[1], iridescence[2]);
            int iriLightRGB = Colouration.HSBtoARGB(iridescenceLight[0], iridescenceLight[1], iridescenceLight[2]);
            int iriDarkRGB = Colouration.HSBtoARGB(iridescenceDark[0], iridescenceDark[1], iridescenceDark[2]);
            int opaqueRGB = Colouration.HSBtoARGB(opaque[0], opaque[1], opaque[2]);
            int opaqueLightRGB = Colouration.HSBtoARGB(opaqueLight[0], opaqueLight[1], opaqueLight[2]);
            int opaqueDarkRGB = Colouration.HSBtoARGB(opaqueDark[0], opaqueDark[1], opaqueDark[2]);
            int cellophaneRGB = Colouration.HSBtoARGB(cellophane[0], cellophane[1], cellophane[2]);
            this.colouration.setMelaninColour(melaninRGB);
            this.colouration.setPheomelaninColour(pheomelaninRGB);

            /*** TEXTURES ***/

            //TODO: Optimize this all using INTERSECT_GROUP
            TextureGrouping iriAlphaGroup = new TextureGrouping(TexturingType.MASK_GROUP);

            TextureGrouping iriAlphaIntersectGroup = new UpdatedTextureGrouping(UpdatedTexturingType.INTERSECT_GROUP);
            if (dragonscale > 0) {
                addTextureToAnimalTextureGrouping(iriAlphaIntersectGroup, TEXTURES_ALPHA, 4, true);
            }
            else {
                addTextureToAnimalTextureGrouping(iriAlphaIntersectGroup, TEXTURES_MARBLE, marbleIriQual, marbleIriSize, marbleIriRand, true);
                addTextureToAnimalTextureGrouping(iriAlphaIntersectGroup, TEXTURES_ALPHA, iriIntensity, true);
            }
            //Funky hybrid iridescence markings
            addTextureToAnimalTextureGrouping(iriAlphaIntersectGroup, TEXTURES_HYBRID_IRI, hybridIri, l -> l!=0);
            iriAlphaGroup.addGrouping(iriAlphaIntersectGroup);

            if (bodyIri != 0) {
                TextureGrouping iriMaskAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                if (dragonscale > 0) {
                    TextureGrouping iriBodyAndFinAlphaGroup = new TextureGrouping(TexturingType.CUTOUT_GROUP);
                    addTextureToAnimalTextureGrouping(iriBodyAndFinAlphaGroup, "iri/scale_overlay.png", true);
                    addTextureToAnimalTextureGrouping(iriBodyAndFinAlphaGroup, TEXTURES_DRAGONSCALE, dragonscale, l -> l != 0);
                    addTextureToAnimalTextureGrouping(iriBodyAndFinAlphaGroup, TEXTURES_DRAGONSCALE_MASK, maskIri >= 4 ? 2 : 1, maskIri > 0);
                    iriMaskAlphaGroup.addGrouping(iriBodyAndFinAlphaGroup);
                }
                else {
                    TextureGrouping iriBodyAndFinAlphaGroup = new TextureGrouping(TexturingType.MASK_GROUP);
                    addTextureToAnimalTextureGrouping(iriBodyAndFinAlphaGroup, TEXTURES_IRI_BODY, bodyIri, l -> l != 0);
                    addTextureToAnimalTextureGrouping(iriBodyAndFinAlphaGroup, TEXTURES_IRI_MASK, maskIri, true);
                    iriMaskAlphaGroup.addGrouping(iriBodyAndFinAlphaGroup);
                }
                TextureGrouping iriBodyAndFinGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                addTextureToAnimalTextureGrouping(iriBodyAndFinGroup, TEXTURES_IRI_FINS, finIri, l -> l != 0);
                addTextureToAnimalTextureGrouping(iriBodyAndFinGroup, TEXTURES_IRI_RIM, fins, doubletail, iriRim, iriRim != 0);
                iriMaskAlphaGroup.addGrouping(iriBodyAndFinGroup);
                iriAlphaGroup.addGrouping(iriMaskAlphaGroup);
            }

            transRootGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            TextureGrouping transAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);

            TextureGrouping finAlphaGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            TextureGrouping finShapeGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            //Remove the pectoral fins from the fin shape texture via masking when dumbo is present.
            addTextureToAnimalTextureGrouping(finShapeGroup, TEXTURES_DUMBO_ALPHA, dumbo ? 1 : 0, true);
            addTextureToAnimalTextureGrouping(finShapeGroup, TEXTURES_FINS, fins, doubletail, crowntail, true);
            addTextureToAnimalTextureGrouping(finShapeGroup, TEXTURES_DUMBO, crowntail, dumbo);
            finAlphaGroup.addGrouping(finShapeGroup);

            TextureGrouping finTransparencyGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            TextureGrouping finCutoutGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            if (butterfly != 0 ) {
                new TextureGrouping(TexturingType.CUTOUT_GROUP);
            }
            if (finAlpha == 1 && butterfly != 0) {
                // Cut butterfly out of black lace
                //Laced Black and Butterfly
                addTextureToAnimalTextureGrouping(finCutoutGroup, TexturingType.APPLY_RGBA, TEXTURES_BUTTERFLY[fins][doubletail][butterfly], "bf-lb", (int) (32) << 24 );
            }
            addTextureToAnimalTextureGrouping(finCutoutGroup, TEXTURES_FIN_ALPHA, finAlpha, l -> true);
            finTransparencyGroup.addGrouping(finCutoutGroup);

            TextureGrouping finPigmentGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            // Things in this group cancel out the effect of finCutoutGroup
            TextureGrouping finRedPigmentGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            // Red pigment adds opacity
            addTextureToAnimalTextureGrouping(finRedPigmentGroup, TEXTURES_MARBLE, marbleRedQual, marbleRedSize, marbleRedRand, true);
            addTextureToAnimalTextureGrouping(finRedPigmentGroup, TEXTURES_RED_FIN, fins, doubletail, finRed, finRed != 0);
            addTextureToAnimalTextureGrouping(finRedPigmentGroup, TEXTURES_RED_BODY, bodyRed, l -> l != 0);
            finPigmentGroup.addGrouping(finRedPigmentGroup);
            if (bloodred) {
                // Bloodred pigment also adds opacity
                TextureGrouping finBloodredPigmentGroup = new TextureGrouping(TexturingType.MASK_GROUP);
                if (vanda) {
                    addTextureToAnimalTextureGrouping(finBloodredPigmentGroup, TEXTURES_VANDA, vandaLevel, true);
                }
                else {
                    addTextureToAnimalTextureGrouping(finBloodredPigmentGroup, TEXTURES_MARBLE, marbleBloodredQual, marbleBloodredSize, marbleBloodredRand, true);
                }
                addTextureToAnimalTextureGrouping(finBloodredPigmentGroup, TEXTURES_RED_FIN, fins, doubletail, finBloodred, finBloodred != 0);
                addTextureToAnimalTextureGrouping(finBloodredPigmentGroup, TEXTURES_RED_BODY, bodyBloodred, l -> l != 0);
                finPigmentGroup.addGrouping(finBloodredPigmentGroup);
            }

            //Iridescence adds opacity but isnt cut through by butterfly
            finTransparencyGroup.addGrouping(iriAlphaGroup);

            TextureGrouping finDumboGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(finDumboGroup, TEXTURES_DUMBO, crowntail, dumbo);
            finPigmentGroup.addGrouping(finDumboGroup);
            finTransparencyGroup.addGrouping(finPigmentGroup);
            finAlphaGroup.addGrouping(finTransparencyGroup);
            transAlphaGroup.addGrouping(finAlphaGroup);
            transRootGroup.addGrouping(transAlphaGroup);

            TextureGrouping rootGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            TextureGrouping bodyAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(bodyAlphaGroup, "mask/body.png", true);
            addTextureToAnimalTextureGrouping(bodyAlphaGroup, "mask/gills.png", true);
            rootGroup.addGrouping(bodyAlphaGroup);

            TextureGrouping texturesGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            /** CELLOPHANE **/
            TextureGrouping cellophaneGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(cellophaneGroup, TexturingType.APPLY_RGB, TEXTURES_FIN_ALPHA[finAlpha], "ce-f", cellophaneRGB);
            addTextureToAnimalTextureGrouping(cellophaneGroup, TexturingType.APPLY_RGB, "mask/body.png", "ce-b", cellophaneRGB);
            addTextureToAnimalTextureGrouping(cellophaneGroup, TexturingType.APPLY_RGB, "mask/gills.png", "ce-g", cellophaneRGB);
            if (dumbo) {
                addTextureToAnimalTextureGrouping(cellophaneGroup, TexturingType.APPLY_RGB, TEXTURES_DUMBO[crowntail], "ce-dumbo", cellophaneRGB);
            }
            texturesGroup.addGrouping(cellophaneGroup);
            /** Everything that isn't cellophane **/
            TextureGrouping nonCellophaneGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            if (butterfly != 0) {
                nonCellophaneGroup = new TextureGrouping(TexturingType.CUTOUT_GROUP);
            }
            /** BUTTERFLY **/
            if (butterfly != 0) {
                TextureGrouping butterflyGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                addTextureToAnimalTextureGrouping(butterflyGroup, "mask/dumbo.png", dumbo);
                addTextureToAnimalTextureGrouping(butterflyGroup, TEXTURES_BUTTERFLY, fins, doubletail, butterfly, true);
                nonCellophaneGroup.addGrouping(butterflyGroup);
            }
            TextureGrouping pigmentGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            /** RED **/
            TextureGrouping redGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(redGroup, TexturingType.APPLY_RED, TEXTURES_MARBLE, marbleRedQual, marbleRedSize, marbleRedRand, true);
            pigmentGroup.addGrouping(redGroup);
            /** BLACK **/
            TextureGrouping blackGroup = new TextureGrouping(TexturingType.CUTOUT_GROUP);
            TextureGrouping blackCutoutGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            //Cut the red layer out of the black
            addTextureToAnimalTextureGrouping(blackCutoutGroup, TEXTURES_RED_BODY, bodyRed, l -> l != 0);
            addTextureToAnimalTextureGrouping(blackCutoutGroup, TEXTURES_RED_FIN, fins, doubletail, finRed, finRed != 0);
            blackGroup.addGrouping(blackCutoutGroup);
            TextureGrouping blackColorGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            //The marble gene(s) cut holes in each layer
            addTextureToAnimalTextureGrouping(blackColorGroup, TexturingType.APPLY_BLACK, TEXTURES_MARBLE, marbleBlackQual, marbleBlackSize, marbleBlackRand, true);
            blackGroup.addGrouping(blackColorGroup);
            pigmentGroup.addGrouping(blackGroup);
            /*** BLOODRED ***/
            if (bloodred) {
                TextureGrouping bloodredGroup = new TextureGrouping(TexturingType.MASK_GROUP);
                TextureGrouping bloodredMaskGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                addTextureToAnimalTextureGrouping(bloodredMaskGroup, TEXTURES_RED_BODY, bodyBloodred, l -> l != 0);
                addTextureToAnimalTextureGrouping(bloodredMaskGroup, TEXTURES_RED_FIN, fins, doubletail, finBloodred, finBloodred != 0);
                bloodredGroup.addGrouping(bloodredMaskGroup);
                TextureGrouping bloodredColorGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                if (vanda) {
                    addTextureToAnimalTextureGrouping(bloodredColorGroup, TexturingType.APPLY_RGB, TEXTURES_VANDA[vandaLevel], "br", bloodredRGB);
                } else {
                    addTextureToAnimalTextureGrouping(bloodredColorGroup, TexturingType.APPLY_RGB, TEXTURES_MARBLE[marbleBloodredQual][marbleBloodredSize][marbleBloodredRand], "br", bloodredRGB);
                }
                bloodredGroup.addGrouping(bloodredColorGroup);
                pigmentGroup.addGrouping(bloodredGroup);
            }
            /** DETAILS **/
            TextureGrouping shadingGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(shadingGroup, "body_shading.png", true);
            pigmentGroup.addGrouping(shadingGroup);
            /** IRIDESCENCE **/
            TextureGrouping iridescenceGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            iridescenceGroup.addGrouping(iriAlphaGroup);
            TextureGrouping iriColorGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(iriColorGroup, TexturingType.APPLY_RGB, "iri_base.png", "iri", iriRGB);
            addTextureToAnimalTextureGrouping(iriColorGroup, TexturingType.APPLY_RGB, "iri_dark.png", "iri-d", iriDarkRGB);
            addTextureToAnimalTextureGrouping(iriColorGroup, TexturingType.APPLY_RGB, "iri_light.png", "iri-l", iriLightRGB);
            iridescenceGroup.addGrouping(iriColorGroup);
            if (pastelOpaque) {
                TextureGrouping opaqueGroup = new TextureGrouping(TexturingType.MASK_GROUP);
                TextureGrouping opaqueAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                addTextureToAnimalTextureGrouping(opaqueAlphaGroup, TEXTURES_MARBLE, marbleOpaqueQual, marbleOpaqueSize, marbleOpaqueRand, true);
                opaqueGroup.addGrouping(opaqueAlphaGroup);
                TextureGrouping opaqueColorGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                addTextureToAnimalTextureGrouping(opaqueColorGroup, TexturingType.APPLY_RGB, "iri_base.png", "op", opaqueRGB);
                addTextureToAnimalTextureGrouping(opaqueColorGroup, TexturingType.APPLY_RGB, "iri_dark.png", "op-d", opaqueDarkRGB);
                addTextureToAnimalTextureGrouping(opaqueColorGroup, TexturingType.APPLY_RGB, "iri_light.png", "op-l", opaqueLightRGB);
                addTextureToAnimalTextureGrouping(opaqueColorGroup, TexturingType.APPLY_RGB, "body_shading_iri.png", "op-s", opaqueDarkRGB);
                opaqueGroup.addGrouping(opaqueColorGroup);
                iridescenceGroup.addGrouping(opaqueGroup);
            }
            if (metallic != 0) {
                TextureGrouping metallicGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                if (gene[0] == 2 && gene[1] == 2) {
                    //Steel Blue
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/steel_metalliclayer1.png", "sb-mt1", metallic1RGB);
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/steel_metalliclayer2.png", "sb-mt2", metallic2RGB);
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/steel_metalliclayer3.png", "sb-mt3", metallic3RGB);
                } else if (gene[0] == 2 || gene[1] == 2) {
                    //Royal Blue
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/royal_metalliclayer1.png", "rb-mt1", metallic1RGB);
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/royal_metalliclayer2.png", "rb-mt2", metallic2RGB);
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/royal_metalliclayer3.png", "rb-mt3", metallic3RGB);
                } else {
                    if (metallic == 2) {
                        addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/turq_metalliclayer2.png", "t-mt2", metallic2RGB);
                    }
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/turq_metalliclayer3.png", "t-mt3", metallic3RGB);
                }
                iridescenceGroup.addGrouping(metallicGroup);
                // Metallic seems to look better with normal shading
                iridescenceGroup.addGrouping(shadingGroup);
            }

            nonCellophaneGroup.addGrouping(pigmentGroup);
            /** BABY STRIPES IF APPLICABLE **/
            if (babyColors) {
                TextureGrouping babyGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                addTextureToAnimalTextureGrouping(babyGroup, extendedRed ? TexturingType.APPLY_RED : TexturingType.APPLY_BLACK, TEXTURES_BABY, cambodian ? 0 : 1, l->l!=0);
                addTextureToAnimalTextureGrouping(babyGroup, TexturingType.APPLY_BLACK, "mask/percent50.png"); // low opacity overlay helps dull the colors
                nonCellophaneGroup.addGrouping(babyGroup);
            }
            nonCellophaneGroup.addGrouping(iridescenceGroup);
            texturesGroup.addGrouping(nonCellophaneGroup);
            /** FIN DETAIL **/
            TextureGrouping detailGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(detailGroup, "halfmoon_fins_64.png", true);
            texturesGroup.addGrouping(detailGroup);
            /** EYES **/
            TextureGrouping eyeGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(eyeGroup, TEXTURES_EYE_L, eyeLeft, l -> true);
            addTextureToAnimalTextureGrouping(eyeGroup, TEXTURES_EYE_R, eyeRight, l -> true);
            texturesGroup.addGrouping(eyeGroup);

            rootGroup.addGrouping(texturesGroup);
            transRootGroup.addGrouping(texturesGroup);

            this.setTextureGrouping(rootGroup);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void setAlphaTexturePaths() {
    }

    public void initilizeAnimalSize() {
        this.setAnimalSize(0.25F);
    }

//    @Override
    protected EnhancedAnimalAbstract createEnhancedChild(Level level, EnhancedAnimalAbstract otherParent) {
        EnhancedBetta enhancedBetta = ENHANCED_BETTA.get().create(this.level);
        Genes babyGenes = new Genes(this.getGenes()).makeChild(this.getOrSetIsFemale(), otherParent.getOrSetIsFemale(), otherParent.getGenes());
        enhancedBetta.setGenes(babyGenes);
        enhancedBetta.setSharedGenes(babyGenes);
        enhancedBetta.setSireName(otherParent.getCustomName() == null ? "???" : otherParent.getCustomName().getString());
        enhancedBetta.setDamName(this.getCustomName() == null ? "???" : this.getCustomName().getString());
        enhancedBetta.setParent(this.getUUID().toString());
        enhancedBetta.setGrowingAge();
        enhancedBetta.setBirthTime();
        enhancedBetta.initilizeAnimalSize();
        enhancedBetta.setEntityStatus(EntityState.CHILD_STAGE_ONE.toString());
        enhancedBetta.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
        enhancedBetta.setInitialDefaults();
        return enhancedBetta;
    }
    @Override
    protected void createAndSpawnEnhancedChild(Level inWorld) {
        EnhancedBetta enhancedBetta = ENHANCED_BETTA.get().create(this.level);
        Genes babyGenes = new Genes(this.genetics).makeChild(this.getOrSetIsFemale(), this.mateGender, this.mateGenetics);
        defaultCreateAndSpawn(enhancedBetta, inWorld, babyGenes, -this.getAdultAge());
        this.level.addFreshEntity(enhancedBetta);
        enhancedBetta.setInitialDefaults();
    }

    @Override
    protected boolean canBePregnant() {
        return false;
    }

    @Override
    protected boolean canLactate() {
        return false;
    }

    @Override
    protected FoodSerialiser.AnimalFoodMap getAnimalFoodType() {
        return FoodSerialiser.getAnimalFoodMap("betta");
    }

    public boolean isBreedingItem(ItemStack stack) {
        return this.getAnimalFoodType().isBreedingItem(stack.getItem());
    }

    @Override
    protected Genes createInitialGenes(LevelAccessor world, BlockPos pos, boolean isDomestic) {
        return new BettaGeneticsInitialiser().generateNewGenetics(world, pos, isDomestic);
    }

    @Override
    public Genes createInitialBreedGenes(LevelAccessor world, BlockPos pos, String breed) {
        return new BettaGeneticsInitialiser().generateWithBreed(world, pos, breed);
    }

    @OnlyIn(Dist.CLIENT)
    public BettaModelData getModelData() {
        return this.bettaModelData;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setModelData(AnimalModelData animalModelData) {
        this.bettaModelData = (BettaModelData) animalModelData;
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return EntityDimensions.scalable(0.4F, 0.35F);
    }

    protected Brain<?> makeBrain(Dynamic<?> p_149138_) {
        return BettaBrain.makeBrain(this.brainProvider().makeBrain(p_149138_));
    }

    public Brain<EnhancedBetta> getBrain() {
        return (Brain<EnhancedBetta>) super.getBrain();
    }

    protected void customServerAiStep() {
        this.getBrain().tick((ServerLevel) this.level, this);
        if (!this.isNoAi()) {
            BettaBrain.updateActivity(this);
        }
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public void aiStep() {
        if (!this.isInWater() && this.onGround && this.verticalCollision) {
            //The flop on land
            this.setDeltaMovement(this.getDeltaMovement().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), (double)0.4F, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
            this.onGround = false;
            this.hasImpulse = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
        } else if (this.isAnimalSleeping() && !this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, this.brain.hasMemoryValue(AddonMemoryModuleTypes.FOUND_SLEEP_SPOT.get()) ? -0.001 : -0.01, 0.0));
        }
        super.aiStep();
        bubble();
    }

    private SoundEvent getFlopSound() {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    protected Brain.Provider<EnhancedBetta> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public void travel(Vec3 delta) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), delta);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(delta);
        }
    }

    public TextureGrouping getFinGroup() {
        return transRootGroup;
    }

    public boolean isHighlyAggressive() {
        return getAggression() >= 8;
    }

    public boolean isNotHighlyAggressive() {
        return !isHighlyAggressive();
    }

    public boolean isNotPassive() {
        return !isPassive();
    }

    public boolean isPassive() {
        return getAggression() < 4;
    }

    public boolean hasReachedTarget() {
        Optional<WalkTarget> walkTarget;
        if (!brain.hasMemoryValue(MemoryModuleType.WALK_TARGET) || !(walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET)).isPresent()) {
            return false;
        }
        boolean isCloseEnough = this.position().closerThan(walkTarget.get().getTarget().currentPosition(), 1.5F);
        return isCloseEnough;
    }

    public int getAggression() {
        //Should return a value from 1 to 10
        if (this.aggression == -1 && this.getGenes() != null) {
            this.aggression = 0;
            int[] gene = this.getGenes().getAutosomalGenes();
            for (int i = 72; i < 80; i++) {
                this.aggression += gene[i];
            }
            this.aggression /= 4;
        }
        return this.aggression;
    }

    public boolean isInTank() {
        return isInTank;
    }

    public void setInTank(boolean inTank) {
        isInTank = inTank;
    }

    class BettaLookControl extends SmoothSwimmingLookControl {
        public BettaLookControl(EnhancedBetta pBetta, int p_149211_) {
            super(pBetta, p_149211_);
        }

        public void tick() {
            super.tick();
        }
    }

    static class BettaMoveControl extends SmoothSwimmingMoveControl {
        private final EnhancedBetta betta;

        public BettaMoveControl(EnhancedBetta pBetta) {
            super(pBetta, 85, 60, 0.1F, 0.5F, false);
            this.betta = pBetta;
        }

        public void tick() {
            super.tick();
        }
    }

    static class BettaPathNavigation extends WaterBoundPathNavigation {
        BettaPathNavigation(EnhancedBetta p_149218_, Level p_149219_) {
            super(p_149218_, p_149219_);
        }

        protected boolean canUpdatePath() {
            return true;
        }

        protected PathFinder createPathFinder(int p_149222_) {
//            this.nodeEvaluator = new AmphibiousNodeEvaluator(false);
            this.nodeEvaluator = new SwimNodeEvaluator(false);
            return new PathFinder(this.nodeEvaluator, p_149222_);
        }

        public boolean isStableDestination(BlockPos p_149224_) {
            return !this.level.getBlockState(p_149224_.below()).isAir();
        }
    }

    protected PathNavigation createNavigation(Level p_149128_) {
        return new EnhancedBetta.BettaPathNavigation(this, p_149128_);
    }

    @Override
    protected void readNBTGenes(CompoundTag compoundNBT, String key, Genes genetics) {
        if (compoundNBT.contains(key)) {
            CompoundTag nbtGenetics = compoundNBT.getCompound(key);
            genetics.setGenes(nbtGenetics.getIntArray("SGenes"), nbtGenetics.getIntArray("AGenes"));
        }
        setSpeed();
    }

    public void bubble() {
        if (this.bettaModelData != null && bettaModelData.isBubbling) {
            if (this.isInWater()) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level.addParticle(ParticleTypes.BUBBLE, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
            }
            bettaModelData.isBubbling = false;
        }
    }

    /*
     * BUCKETABLE STUFF
     */
    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean p_149196_) {
        this.entityData.set(FROM_BUCKET, p_149196_);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();

        if (item == AddonItems.ENHANCED_BETTA_EGG.get()) {
            return InteractionResult.SUCCESS;
        }

        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    protected void usePlayerItem(Player player, InteractionHand hand, ItemStack itemStack) {
        if (itemStack.getItem() instanceof MobBucketItem) {
            if (!player.isCreative()) {
                player.setItemInHand(hand, new ItemStack(Items.WATER_BUCKET));
            }
        } else {
            super.usePlayerItem(player, hand, itemStack);
        }
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        if (stack.getItem() instanceof EnhancedBettaBucket) {
            EnhancedBettaBucket.setGenes(stack, this.genetics != null ? this.genetics : getGenes());
            EnhancedBettaBucket.setParentNames(stack, this.sireName, this.damName);
            EnhancedBettaBucket.setEquipment(stack, this.animalInventory.getItem(1));
            if (this.hasEgg() && this.mateGenetics != null) {
                EnhancedBettaBucket.setMateGenes(stack, this.mateGenetics, this.mateGender);
            }
            EnhancedBettaBucket.setBettaUUID(stack, this.getUUID().toString());
            EnhancedBettaBucket.setBirthTime(stack, this.getBirthTime());
        }
    }

    public boolean hasEgg() {
        return getBrain().hasMemoryValue(AddonMemoryModuleTypes.HAS_EGG.get());
    }

    @Override
    protected int getPregnancyProgression() {
        return this.hasEgg() ? 10 : -1;
    }

    public void setHasEgg(boolean hasEgg) {
        if (hasEgg) {
            this.getBrain().setMemoryWithExpiry(AddonMemoryModuleTypes.HAS_EGG.get(), true, 500);
            return;
        }
        this.getBrain().eraseMemory(AddonMemoryModuleTypes.HAS_EGG.get());
    }

    public void setMakingNest(boolean makingNest) {
        if (makingNest) {
            this.getBrain().setMemoryWithExpiry(AddonMemoryModuleTypes.MAKING_NEST.get(), true, 500);
            return;
        }
        this.getBrain().eraseMemory(AddonMemoryModuleTypes.MAKING_NEST.get());
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
        this.setIsFemale(tag.getCompound("Genetics"));
        this.setInitialDefaults();
        this.toggleReloadTexture();
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(AddonItems.ENHANCED_BETTA_BUCKET.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    public static void onStopAttacking(EnhancedBetta enhancedBetta) {
        Optional<LivingEntity> optional = enhancedBetta.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (optional.isPresent()) {
            Level level = enhancedBetta.level;
            LivingEntity livingentity = optional.get();
            if (livingentity.isDeadOrDying()) {
                DamageSource damagesource = livingentity.getLastDamageSource();
                if (damagesource != null) {
                    Entity entity = damagesource.getEntity();
                }
            }
        }
        enhancedBetta.setIsFlaring(false);
    }

    @Override
    public Boolean isAnimalSleeping() {
        if (!this.isInWaterRainOrBubble() || this.hasEgg()) {
            return false;
        } else if (!(this.getLeashHolder() instanceof LeashFenceKnotEntity) && this.getLeashHolder() != null) {
            return false;
        } else {
            this.sleeping = this.entityData.get(SLEEPING);
            return this.sleeping;
        }
    }

    @Override
    public boolean sleepingConditional() {
        return (((this.level.getDayTime() % 24000 >= 12600 && this.level.getDayTime() % 24000 <= 22000) || this.level.isThundering()) && this.awokenTimer == 0 && !this.sleeping);
    }

    public void setIsFlaring(boolean angry) {
        this.entityData.set(IS_FLARING, angry);
    }

    public boolean getIsFlaring() {
        return this.entityData.get(IS_FLARING);
    }

    public BlockPos setNestPos(BlockPos position) {
        this.getBrain().setMemory(AddonMemoryModuleTypes.NEST_POS.get(), position);
        return position;
    }

    public BlockPos getNestPos() {
        return this.getBrain().getMemory(AddonMemoryModuleTypes.NEST_POS.get()).orElse(null);
    }


    public BlockPos findExistingNest() {
        BlockPos baseBlockPos = new BlockPos(this.blockPosition());
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        int maxSearchHeight = 6;
        int maxSearchWidth = 5;

        for (int y = 0; y < maxSearchHeight; y++) {
            int x = 0;
            boolean xi = true; //When true, increment x. otherwise multiply x by -1
            while (x < maxSearchWidth) {
                int z = 0;
                boolean zi = true; //When true, increment z. otherwise multiply z by -1
                while (z < maxSearchWidth) {
                    mutableBlockPos.set(baseBlockPos).move(xi ? x : -x, y, zi ? z : -z);
                    if (this.level.getBlockState(mutableBlockPos).is(AddonBlocks.BUBBLE_NEST.get())) {
                        if (ValidatePath.isValidPath(this, mutableBlockPos, 16)) {
                            return mutableBlockPos;
                        }
                    }
                    if (zi) z++;
                    zi = !zi;
                }
                if (xi) x++;
                xi = !xi;
            }
        }
        return null;
    }

    public boolean findLocationForNest() {
        BlockPos baseBlockPos = new BlockPos(this.blockPosition());
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        int maxSearchHeight = 8;
        int maxSearchWidth = 1;

        for (int y = level.isWaterAt(baseBlockPos) ? 0 : -1; y < maxSearchHeight; y++) {
            for (int x = -maxSearchWidth; x < maxSearchWidth; x++) {
                for (int z = -maxSearchWidth; z < maxSearchWidth; z++) {
                    if (x == 0 && z == 0) continue; //Minecraft seems to be unable to pathfnd directly upwards.
                    //Besides, it looks better if they move around a little
                    mutableBlockPos.set(baseBlockPos).move(x, y, z);
                    if (this.level.getBlockState(mutableBlockPos).is(WATER) && !this.level.isWaterAt(mutableBlockPos.above())) {
                        setNestPos(mutableBlockPos);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        ItemStack bettaStack = new ItemStack(AddonItems.BETTA.get(), 1);
        this.spawnAtLocation(bettaStack);
    }

    public boolean checkSpawnObstruction(LevelReader levelReader) {
        return levelReader.isUnobstructed(this);
    }

    public static boolean checkBettaSpawnRules(EntityType<EnhancedBetta> entityType, LevelAccessor accessor, MobSpawnType spawnType, BlockPos pos, Random random) {
        int maxY = 70;
        int minY = 40;
        return random.nextFloat() > 0.75F && pos.getY() >= minY && pos.getY() <= maxY && accessor.getFluidState(pos.below()).is(FluidTags.WATER) && accessor.getBlockState(pos.above()).is(WATER);
    }

    public ImmutableList<Activity> getAdultActivities() {
        if (adultActivities == null && getGenes() != null) {
            ArrayList<Activity> mutableAdultActivities = new ArrayList<>();
            mutableAdultActivities.add(AddonActivities.PAUSE_BRAIN.get());
            mutableAdultActivities.add(Activity.REST);
            if (!isHighlyAggressive()) {
                mutableAdultActivities.add(Activity.AVOID);
            }
            mutableAdultActivities.add(AddonActivities.LAY_EGG.get());
            mutableAdultActivities.add(AddonActivities.MAKE_BUBBLE_NEST.get());
            if ( (!this.getOrSetIsFemale() && isNotPassive()) || (this.getOrSetIsFemale() && isHighlyAggressive()) ) {
                mutableAdultActivities.add(AddonActivities.NIP.get());
                if (!this.getOrSetIsFemale()) {
                    mutableAdultActivities.add(Activity.FIGHT);
                }
            }
            mutableAdultActivities.add(Activity.IDLE);
            adultActivities = ImmutableList.copyOf(mutableAdultActivities);
        }
        return adultActivities;
    }

    public ImmutableList<Activity> getBabyActivities() {
        if (babyActivities == null) {
            ArrayList<Activity> mutableBabyActivities = new ArrayList<>();
            mutableBabyActivities.add(AddonActivities.PAUSE_BRAIN.get());
            mutableBabyActivities.add(Activity.REST);
            mutableBabyActivities.add(Activity.IDLE);
            babyActivities = ImmutableList.copyOf(mutableBabyActivities);
        }
        return babyActivities;
    }


    public String getMateName() {
        return this.mateName;
    }

    public void setMateName(String mateName) {
        if (mateName != null && !mateName.equals("")) {
            this.mateName = mateName;
        } else {
            this.mateName = "???";
        }
    }

    public Genes getMateGenetics() {
        return mateGenetics;
    }

    @Override
    protected void handlePartnerBreeding(AgeableMob ageable) {
        if (GeneticAnimalsConfig.COMMON.omnigenders.get()) {
            this.mateGenetics = ((EnhancedBetta) ageable).getGenes();
            this.setHasEgg(true);
            this.setMateGender(((EnhancedBetta) ageable).getOrSetIsFemale());
            if (ageable.hasCustomName()) {
                this.setMateName(ageable.getCustomName().getString());
            }
            ((EnhancedBetta) ageable).setMateGenes(this.genetics);
            ((EnhancedBetta) ageable).setHasEgg(true);
            ((EnhancedBetta) ageable).setMateGender(this.getOrSetIsFemale());
            if (this.hasCustomName()) {
                ((EnhancedBetta) ageable).setMateName(this.getCustomName().getString());
            }
        } else if (this.getOrSetIsFemale()) {
            this.mateGenetics = ((EnhancedBetta) ageable).getGenes();
            this.setHasEgg(true);
            this.setMateGender(false);
            if (ageable.hasCustomName()) {
                this.setMateName(ageable.getCustomName().getString());
            } else {
                this.setMateName("???"); //Reset mate name
            }
        } else if (((EnhancedBetta) ageable).getOrSetIsFemale()) {
            ((EnhancedBetta) ageable).setMateGenes(this.genetics);
            ((EnhancedBetta) ageable).setHasEgg(true);
            ((EnhancedBetta) ageable).setMateGender(false);
            if (this.hasCustomName()) {
                ((EnhancedBetta) ageable).setMateName(this.getCustomName().getString());
            } else {
                ((EnhancedBetta) ageable).setMateName("???"); //Reset mate name
            }
        }
    }

    // Pilfered from GA's Nesting code - because BlockTargets are imprecise, so teleport!
    public static Vec3 moveCloser(Vec3 vec1, Vec3 vec2, double step) {
        // Calculate the direction vector from vec1 to vec2
        Vec3 direction = vec2.subtract(vec1);

        // Normalize the direction vector to get the unit vector
        Vec3 unitDirection = direction.normalize();

        // Scale the unit vector by the step size
        Vec3 stepVector = unitDirection.scale(step);

        // Add the step vector to vec1 to get the new vector
        Vec3 newVec = vec1.add(stepVector);

        return newVec;
    }

    protected void setSpeed() {
        //Check to prevent wikizoomer bug
        float speedMod = 0.0F;
        if (this.getGenes() != null) {
            int[] genes = this.getGenes().getAutosomalGenes();
            if (genes[60] == 2 || genes[61] == 2) {
                //Halfmoon/Delta                  Halfmoon : Delta
                speedMod -= (genes[60] == genes[61]) ? 0.1F : 0.05F;
            }

            if (getOrSetIsFemale()) {
                speedMod *= 0.25F;
            } else if (genes[58] == 2 || genes[59] == 2) {
                //LongTail only impacts speed of males
                speedMod -= 0.15F;
            }
        }
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5 * (1.0 + speedMod));
    }

    @Override
    public void setInitialDefaults() {
        super.setInitialDefaults();
        setSpeed();
    }

    @Override
    protected net.minecraft.network.chat.Component getTypeName() {
        return new TranslatableComponent((speciesTranslationKey + (getOrSetIsFemale() ? ".female":".male")));
    }

    @Override
    public int getMaxAirSupply() {
        return BETTA_TOTAL_AIR_SUPPLY;
    }

    protected void handleAirSupply(int p_149194_) {
        if (this.isAlive() && !this.isInWaterRainOrBubble()) {
            this.setAirSupply(p_149194_ - 1);
            if (this.getAirSupply() < 0) {
                this.setAirSupply(0);
                this.hurt(DamageSource.DRY_OUT, 2.0F);
            }
        } else {
            this.setAirSupply(this.getMaxAirSupply());
        }

    }

    @Override
    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        if (!this.isNoAi()) {
            this.handleAirSupply(i);
        }
    }

    public boolean isVeryBaby() {
        // GA's definition of "baby" is anything under adulthood.
        // We don't really need bettas to keep their baby appearance that long
        int age = this.getEnhancedAnimalAge();
        int adultAge = this.getAdultAge();
        return age < adultAge/2F;
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float damageAmount) {
        boolean flag = super.hurt(damageSource, damageAmount);
        if (this.level.isClientSide) {
            return false;
        } else {
            if (flag && damageSource.getEntity() instanceof LivingEntity && this.isHighlyAggressive()) {
                BettaBrain.wasHurtBy(this, (LivingEntity)damageSource.getEntity());
            }
            return flag;
        }
    }

}