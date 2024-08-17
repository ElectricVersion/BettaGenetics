package elecvrsn.GeneticBettas.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import elecvrsn.GeneticBettas.ai.brain.betta.BettaBrain;
import elecvrsn.GeneticBettas.entity.genetics.BettaGeneticsInitialiser;
import elecvrsn.GeneticBettas.init.AddonItems;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import elecvrsn.GeneticBettas.init.AddonSensorTypes;
import elecvrsn.GeneticBettas.items.EnhancedBettaBucket;
import elecvrsn.GeneticBettas.model.modeldata.BettaModelData;
import elecvrsn.GeneticBettas.util.AddonReference;
import mokiyoki.enhancedanimals.ai.general.EnhancedBreedGoal;
import mokiyoki.enhancedanimals.config.EanimodCommonConfig;
import mokiyoki.enhancedanimals.entity.EnhancedAnimalAbstract;
import mokiyoki.enhancedanimals.entity.EntityState;
import mokiyoki.enhancedanimals.entity.util.Colouration;
import mokiyoki.enhancedanimals.init.FoodSerialiser;
import mokiyoki.enhancedanimals.init.ModMemoryModuleTypes;
import mokiyoki.enhancedanimals.model.modeldata.AnimalModelData;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TexturingType;
import mokiyoki.enhancedanimals.util.Genes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BigDripleafBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Optional;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;

public class EnhancedBetta extends EnhancedAnimalAbstract implements Bucketable {

    protected static final ImmutableList<? extends SensorType<? extends Sensor<? super EnhancedBetta>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.HURT_BY, AddonSensorTypes.BETTA_ATTACKABLES.get(), AddonSensorTypes.BETTA_FOOD_TEMPTATIONS.get());
    protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(ModMemoryModuleTypes.SLEEPING.get(), ModMemoryModuleTypes.PAUSE_BRAIN.get(), ModMemoryModuleTypes.FOCUS_BRAIN.get(), MemoryModuleType.BREED_TARGET, ModMemoryModuleTypes.HAS_EGG.get(), MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.HAS_HUNTING_COOLDOWN, AddonMemoryModuleTypes.FOUND_SLEEP_SPOT.get(), AddonMemoryModuleTypes.SEEKING_NEST.get());
    private static final EntityDataAccessor<Boolean> PREGNANT = SynchedEntityData.defineId(EnhancedBetta.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EnhancedBetta.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ANGRY = SynchedEntityData.defineId(EnhancedBetta.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> NEST_POS = SynchedEntityData.defineId(EnhancedBetta.class, EntityDataSerializers.BLOCK_POS);

    private boolean isTempted = false;
    private int aggression = -1;

    private TextureGrouping transRootGroup;
    private TextureGrouping iridescenceGroup;

//    private static final String[] TEXTURES_FINS = new String[] {
//            "mask/wildtype.png", "mask/halfmoon.png"
//    };

    private static final String[] TEXTURES_FIN_ALPHA = new String[] {
            "mask/solid.png", "mask/lace_lg.png", "mask/cambodian_lg.png"
    };

    private static final String[] TEXTURES_IRI_FINS = new String[] {
            "", "iri/fin/low_fin_iri.png", "iri/fin/med_fin_iri.png", "iri/fin/high_fin_iri.png",
    };

    private static final String[] TEXTURES_IRI_BODY = new String[] {
            "", "iri/body/low_body_iri.png", "iri/body/med_body_iri.png", "iri/body/high_body_iri.png",
            "iri/body/spread.png",
    };
    private static final String[] TEXTURES_IRI_MASK = new String[] {
            "", "iri/body/mask_low1.png", "iri/body/mask_low2.png",
            "iri/body/mask_med1.png", "iri/body/mask_med2.png",
            "iri/body/mask_high1.png", "iri/body/mask_high2.png",
            "iri/body/mask_max.png",
    };
    private static final String[] TEXTURES_RED_FIN = new String[] {
            "", "red/fin/lower_3.png", "red/fin/lower_2.png", "red/fin/lower_1.png", "red/fin/wildtype.png"
    };

//    private static final String[] TEXTURES_BUTTERFLY = new String[] {
//            "", "butterfly/long_butterfly_min.png", "butterfly/long_butterfly_medium.png", "butterfly/long_butterfly_high.png", "butterfly/long_butterfly_max.png",
//    };
    private static final String[] TEXTURES_RED_BODY = new String[] {
            "", "red/body/min.png", "red/body/low.png", "red/body/med.png", "red/body/high.png", "red/extended.png", "red/extended_het_mask.png", "red/extended_homo_mask.png"
    };

    private static final String[] TEXTURES_ALPHA = new String[] {
            "mask/percent25.png", "mask/percent50.png", "mask/percent75.png", "mask/solid.png"
    };

    private static final String[] TEXTURES_EYES = new String[] {
            "eyes_wildtype.png"
    };

    private static final String[] TEXTURES_SHADING = new String[] {
            "body_shading.png", "body_shading_light.png"
    };

    private static final String[] TEXTURES_PASTEL_OPAQUE = new String[] {
            "", "iri/pastel.png", "iri/opaque.png"
    };

    private static final String[] TEXTURES_MARBLE_BLACK = new String[] {
            "", "black/marble/marble_1.png", "black/marble/marble_2.png", "black/marble/marble_3.png", "black/marble/marble_4.png", "black/marble/marble_5.png", "black/marble/marble_6.png", "black/marble/marble_7.png", "black/marble/marble_8.png", "black/marble/marble_9.png", "black/marble/marble_10.png", "black/marble/marble_11.png",
    };

    private static final String[][][] TEXTURES_MARBLE = new String[][][] {
            {
                    //None
                    {"mask/solid.png"}
            },
            {
                    //Poor Grizzle
                    {"mask/transparent.png"},
                    {"marble/poorgrizzle_small_1.png", "marble/poorgrizzle_small_2.png", "marble/poorgrizzle_small_3.png", "marble/poorgrizzle_small_4.png", "marble/poorgrizzle_small_5.png"},
                    {"marble/poorgrizzle_medium_1.png", "marble/poorgrizzle_medium_2.png", "marble/poorgrizzle_medium_3.png", "marble/poorgrizzle_medium_4.png", "marble/poorgrizzle_medium_5.png"},
                    {"marble/poorgrizzle_large_1.png", "marble/poorgrizzle_large_2.png", "marble/poorgrizzle_large_3.png", "marble/poorgrizzle_large_1.png", "marble/poorgrizzle_large_1.png"},
                    {"marble/poorgrizzle_xl_1.png", "marble/poorgrizzle_xl_2.png", "marble/poorgrizzle_xl_3.png", "marble/poorgrizzle_xl_1.png", "marble/poorgrizzle_xl_1.png"},
                    {"mask/solid.png"}
            },
            {
                    //Medium Grizzle
                    {"mask/transparent.png"},
                    {"marble/mediumgrizzle_small_1.png", "marble/mediumgrizzle_small_1.png", "marble/mediumgrizzle_small_1.png", "marble/mediumgrizzle_small_1.png", "marble/mediumgrizzle_small_1.png"},
                    {"marble/mediumgrizzle_medium_1.png", "marble/mediumgrizzle_medium_2.png", "marble/mediumgrizzle_medium_3.png", "marble/mediumgrizzle_medium_4.png", "marble/mediumgrizzle_medium_1.png"},
                    {"marble/mediumgrizzle_large_1.png", "marble/mediumgrizzle_large_1.png", "marble/mediumgrizzle_large_1.png", "marble/mediumgrizzle_large_1.png", "marble/mediumgrizzle_large_1.png"},
                    {"marble/mediumgrizzle_xl_1.png", "marble/mediumgrizzle_xl_1.png", "marble/mediumgrizzle_xl_1.png", "marble/mediumgrizzle_xl_1.png", "marble/mediumgrizzle_xl_1.png"},
                    {"mask/solid.png"}
            },
            {
                    //High Grizzle
                    {"mask/transparent.png"},
                    {"marble/highgrizzle_small_1.png", "marble/highgrizzle_small_1.png", "marble/highgrizzle_small_1.png", "marble/highgrizzle_small_1.png", "marble/highgrizzle_small_1.png"},
                    {"marble/highgrizzle_medium_1.png", "marble/highgrizzle_medium_2.png", "marble/highgrizzle_medium_3.png", "marble/highgrizzle_medium_4.png", "marble/highgrizzle_medium_1.png"},
                    {"marble/highgrizzle_large_1.png", "marble/highgrizzle_large_2.png", "marble/highgrizzle_large_3.png", "marble/highgrizzle_large_1.png", "marble/highgrizzle_large_1.png"},
                    {"marble/highgrizzle_xl_1.png", "marble/highgrizzle_xl_2.png", "marble/highgrizzle_xl_1.png", "marble/highgrizzle_xl_1.png", "marble/highgrizzle_xl_1.png"},
                    {"mask/solid.png"}

            },
            {
                    //Medium Spots
                    {"mask/transparent.png"},
                    {"marble/secondbestspots_small_1.png", "marble/secondbestspots_small_2.png", "marble/secondbestspots_small_3.png", "marble/secondbestspots_small_4.png", "marble/secondbestspots_small_5.png"},
                    {"marble/secondbestspots_medium_1.png", "marble/secondbestspots_medium_2.png", "marble/secondbestspots_medium_3.png", "marble/secondbestspots_medium_4.png", "marble/secondbestspots_medium_5.png"},
                    {"marble/secondbestspots_large_1.png", "marble/secondbestspots_large_2.png", "marble/secondbestspots_large_3.png", "marble/secondbestspots_large_4.png", "marble/secondbestspots_large_5.png"},
                    {"marble/secondbestspots_xl_1.png", "marble/secondbestspots_xl_1.png", "marble/secondbestspots_xl_1.png", "marble/secondbestspots_xl_1.png", "marble/secondbestspots_xl_1.png"},
                    {"mask/solid.png"}
            },
            {
                    //Best Spots
                    {"mask/transparent.png"},
                    {"marble/bestspots_small_1.png", "marble/bestspots_small_2.png", "marble/bestspots_small_3.png", "marble/bestspots_small_4.png", "marble/bestspots_small_1.png"},
                    {"marble/bestspots_medium_1.png", "marble/bestspots_medium_2.png", "marble/bestspots_medium_3.png", "marble/bestspots_medium_4.png", "marble/bestspots_medium_1.png"},
                    {"marble/bestspots_large_1.png", "marble/bestspots_large_2.png", "marble/bestspots_large_3.png", "marble/bestspots_large_1.png", "marble/bestspots_large_1.png"},
                    {"marble/bestspots_xl_1.png", "marble/bestspots_xl_2.png", "marble/bestspots_xl_3.png", "marble/bestspots_xl_1.png", "marble/bestspots_xl_1.png"},
                    {"mask/solid.png"}
            },
    };


    private static final String[][][] TEXTURES_FINS = new String[][][] {
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
    };

    private static final String[][][] TEXTURES_BUTTERFLY = new String[][][] {
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
    };

    private static final String[][][] TEXTURES_IRI_RIM = new String[][][] {
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
    };
    private boolean resetTexture = true;

    @OnlyIn(Dist.CLIENT)
    private BettaModelData bettaModelData;

    @OnlyIn(Dist.CLIENT)
    private Genes getClientSidedGenes() {
        if(this.genesSplitForClient==null) {
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

    public Genes getGenes(){
        if (this.level instanceof ServerLevel) {
            return this.genetics;
        } else {
            return this.getClientSidedGenes();
        }
    }
    public EnhancedBetta(EntityType<? extends EnhancedBetta> entityType, Level worldIn) {
        super(entityType, worldIn, AddonReference.BETTA_SEXLINKED_GENES_LENGTH, AddonReference.BETTA_AUTOSOMAL_GENES_LENGTH, true);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new EnhancedBetta.BettaMoveControl(this);
        this.lookControl = new EnhancedBetta.BettaLookControl(this, 20);
        this.maxUpStep = 1.0F;
        this.initilizeAnimalSize();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PREGNANT, false);
        this.entityData.define(IS_ANGRY, false);
        this.entityData.define(FROM_BUCKET, false);
        this.entityData.define(NEST_POS, BlockPos.ZERO);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    protected void initializeHealth(EnhancedAnimalAbstract animal, float health) {
//        int[] genes = this.genetics.getAutosomalGenes();
//        super.initializeHealth(animal, (health + 15F));
        calcMaxHealth(true);
    }

    private float calcMaxHealth() {
        int[] genes = this.getGenes().getAutosomalGenes();
        float health = 8F;
        if (genes[62] == 2 && genes[63] == 2) {
            health -= 2F;
        }
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)health);
        return health;
    }
    private float calcMaxHealth(boolean setHealth) {
        float health = calcMaxHealth();
        if (setHealth) {
            this.setHealth(health);
        }
        return health;
    }

    @Override
    protected String getSpecies() {
        return "entity.bettagenetics.enhanced_betta";
    }

    @Override
    protected int getAdultAge() { return EanimodCommonConfig.COMMON.adultAgePig.get();}

    @Override
    protected int gestationConfig() {
        return 100;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor inWorld, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag itemNbt) {
        //registerAnimalAttributes();
        return commonInitialSpawnSetup(inWorld, livingdata, 60000, 30000, 80000, spawnReason);
    }

    public static float[] getHSBFromHex(String colourHex) {
        if (colourHex.length() == 7) {
            colourHex=colourHex.substring(1,7);
        }
        int[] color =
        {
            Integer.valueOf( colourHex.substring( 0, 2 ), 16 ),
            Integer.valueOf( colourHex.substring( 2, 4 ), 16 ),
            Integer.valueOf( colourHex.substring( 4, 6 ), 16 )
        };
        return Color.RGBtoHSB(color[0], color[1], color[2], (float[])null);
    }

    public static int getARGBFromHex(String colourHex) {
        if (colourHex.length() == 7) {
            colourHex=colourHex.substring(1,7);
        }
        int[] color =
                {
                        Integer.valueOf( colourHex.substring( 0, 2 ), 16 ),
                        Integer.valueOf( colourHex.substring( 2, 4 ), 16 ),
                        Integer.valueOf( colourHex.substring( 4, 6 ), 16 )
                };
        return Integer.MIN_VALUE | Math.min(color[0], 255) << 16 | Math.min(color[1], 255) << 8 | Math.min(color[2], 255);
    }


    protected  void incrementHunger() {
        if(this.sleeping) {
            hunger = hunger + (1.0F*getHungerModifier());
        } else {
            hunger = hunger + (2.0F*getHungerModifier());
        }
    }
    @Override
    protected void runExtraIdleTimeTick() {
    }
    public void lethalGenes(){
    }

    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.enhancedAnimalTextureGrouping == null) {
            this.setTexturePaths();
        } else if (this.resetTexture && !this.isBaby()) {
            this.resetTexture = false;
            this.reloadTextures();
        }

        return getCompiledTextures("enhanced_betta");
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.getBrain().eraseMemory(ModMemoryModuleTypes.PAUSE_BRAIN.get());
        this.getBrain().eraseMemory(ModMemoryModuleTypes.FOCUS_BRAIN.get());

    }
    @Override
    @OnlyIn(Dist.CLIENT)
    protected void setTexturePaths() {
        if (this.getGenes() != null) {
            int[] gene = getGenes().getAutosomalGenes();

            char[] uuidArry = getStringUUID().toCharArray();

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
            int black = 1;
            int butterfly = 0;
            int iriRim = 0;
            int iriIntensity = 2;
            boolean pastelOpaque = false;
            boolean cambodian = false;
            int metallic = 0;
            int marbleBlackQual = 1;
            int marbleBlackSize = 0;
            int marbleBlackRand = 0;
            int marbleRedQual = 1;
            int marbleRedSize = 0;
            int marbleRedRand = 0;
            int marbleIriQual = 1;
            int marbleIriSize = 0;
            int marbleIriRand = 0;
            boolean dumbo = false;

            /*** COLORATION ***/
            float[] melanin = {0.0427F, 0.527F, 0.251F};
            float[] pheomelanin = { 0.991F, 0.978F, 0.655F };
            float[] cellophane = getHSBFromHex("ebe8e4");
            float[] iridescenceLight = { 0.44F, 0.978F, 0.878F };
            float[] iridescence = { 0.530F, 0.715F, 0.634F };
            float[] iridescenceDark = { 0.582F, 0.808F, 0.604F };
            /* Opaque/Pastel normally appears as a modifier of iridescence,
                but it needs separate colors + its own layer since it marbles separately
             */
            float[] opaqueLight = { 0.44F, 0.095F, 0.953F };
            float[] opaque = { 0.530F, 0.093F, 0.95F };
            float[] opaqueDark = { 0.582F, 0.166F, 0.922F };
            float[] metallic1 = getHSBFromHex("00ab74");
            float[] metallic2 = getHSBFromHex("00ab74");
            float[] metallic3 = getHSBFromHex("e7d094");

            //Body Iridescence Level
            int bodyIriLevel = 0;
            for (int i = 24; i < 28; i++) {
                if (gene[i] == 2) {
                    bodyIriLevel++;
                }
            }
            bodyIri += bodyIriLevel/2;

            //Fin Iridescence Level
            int finIriLevel = 0;
            for (int i = 28; i < 32; i++) {
                if (gene[i] == 2) {
                    finIriLevel++;
                }
            }
            finIri += finIriLevel/2;

            if (gene[32] == 2 || gene[33] == 2) {
                //Higher Iridescence Intensity
                iriIntensity++;
            }
            if (gene[34] == 2 || gene[35] == 2) {
                //Lower Iridescence Intensity
                iriIntensity--;
            }
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

            if (gene[2] == 2 || gene[3] == 2) {
                //Spread
                iriIntensity = 3;
                bodyIri = 4;
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
                maskIriLevel = maskIriLevel/2;
                switch (maskIriLevel) {
                    case 0 -> maskIri = 1;
                    case 1 -> maskIri = 3;
                    case 2 -> maskIri = 5;
                    case 3 -> maskIri = 7;
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
                }
                else if (metallic == 1) {
                    iridescenceLight = getHSBFromHex("89a5a6");
                    iridescence = getHSBFromHex("597d86");
                    iridescenceDark = getHSBFromHex("3e5c62");
                    metallic2 = getHSBFromHex("A7C7A5");
                }
                else {
                    iridescenceLight[0] = 0.533F;
                    iridescenceLight[1] = 0.248F;
                    iridescenceLight[2] = 0.792F;
                    iridescence[0] = 0.556F;
                    iridescence[1] = 0.443F;
                    iridescence[2] = 0.655F;
                    iridescenceDark[0] = 0.551F;
                    iridescenceDark[1] = 0.480F;
                    iridescenceDark[2] = 0.482F;
                }            }
            else if (gene[0] == 2 || gene[1] == 2) {
                //Royal Blue (het steel blue)
                metallic1 = getHSBFromHex("418bb1");
                metallic2 = getHSBFromHex("00AC73");
                metallic3 = getHSBFromHex("88e8ff");
                if (metallic == 2) {
                    iridescenceLight = getHSBFromHex("55d9ea");
                    iridescence = getHSBFromHex("2a8fab");
                    iridescenceDark = getHSBFromHex("296d88");
                }
                else if (metallic == 1) {
                    iridescenceLight = getHSBFromHex("5cb6dd");
                    iridescence = getHSBFromHex("2c72a5");
                    iridescenceDark = getHSBFromHex("2c5882");
                    metallic2 = getHSBFromHex("A7C7A5");
                }
                else {
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
            }
            else if (metallic == 2) {
                //Turquoise Metallic
                iridescenceLight = getHSBFromHex("6fa970");
                iridescence = getHSBFromHex("318c64");
                iridescenceDark = getHSBFromHex("218074");
            }
            else if (metallic == 1) {
                //Turquoise Het Metallic
                iridescenceLight = getHSBFromHex("38c380");
                iridescence = getHSBFromHex("349c95");
                iridescenceDark = getHSBFromHex("217695");
            }

            //Main Red Color
            if (gene[42] == 2 && gene[43] == 2) {
                //Orange
                pheomelanin = getHSBFromHex("D44E07");
            }
            else if (gene[42] == 3 && gene[43] == 3) {
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
                finAlpha = 1;
                if (gene[6] == 2 && gene[7] == 2) {
                    //Double Black/Super Black
                    melanin[1] = 0.171F;
                    melanin[2] = 0.081F;
                } else {
                    //Just Laced Black
                    melanin[1] = 0.091F;
                    melanin[2] = 0.151F;
                }
            }
            else if (gene[6] == 2 && gene[7] == 2) {
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

            if (gene[12] == 2 || gene[13] == 2) {
                //Extended Red
                bodyRed = 5;
                if (gene[16] == 2 && gene[17] == 2) {
                    //Homozygous Masked
                    bodyRed = 7;
                }
                else if (gene[16] == 2 || gene[17] == 2) {
                    //Heterozygous Masked
                    bodyRed = 6;
                }
            }

            if (gene[14] == 2 && gene[15] == 2) {
                //Blonde
                melanin[1] -= 0.1F;
                melanin[2] += 0.2F;
                pheomelanin[1] -= 0.3F;
                pheomelanin[2] += 0.2F;
                iridescence[1] -= 0.3F;
                iridescence[2] += 0.2F;
                iridescenceLight[1] -= 0.3F;
                iridescenceLight[2] += 0.2F;
                iridescenceDark[1] -= 0.3F;
                iridescenceDark[2] += 0.2F;
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
                //Butterfly Extensions
                if (gene[68] == 2) {
                    iriRim += 1;
                }
                if (gene[69] == 2) {
                    iriRim += 1;
                }
                if (gene[70] == 2 || gene[71] == 2) {
                    iriRim += 1;
                }
            }


            if (gene[56] == 2 && gene[57] == 2) {
                dumbo = true;
            }

            if (gene[58] == 2 || gene[59] == 2) {
                //Long Fins
                if (gene[60] == 2 || gene[61] == 2) {
                    if (gene[60] == gene[61]) {
                        //Halfmoon
                        fins = 5;
                    }
                    else {
                        //Delta
                        fins = 4;
                    }
                }
                else {
                    //Veil
                    fins = 3;
                }
            }
            else {
                //Short Fins
                if (gene[60] == 2 || gene[61] == 2) {
                    if (gene[60] == gene[61]) {
                        //Halfmoon Plakat
                        fins = 2;
                    }
                    else {
                        //Delta Plakat
                        fins = 1;
                    }
                }
                else {
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
                }
                else {
                    //Het Crowntail
                    crowntail = 1;
                }
            }

            if (marbleRedQual == 1) {
                // Random Marble
                marbleRedQual += uuidArry[3] % 5;
                marbleRedSize += uuidArry[6] % 6;
                if (marbleRedSize != 0 && marbleRedSize != 5) {
                    marbleRedRand += uuidArry[9] % 5;
                }
            }
            if (marbleBlackQual == 1) {
                // Random Marble
                marbleBlackQual += uuidArry[4] % 5;
                marbleBlackSize += uuidArry[7] % 6;
                if (marbleBlackSize != 0 && marbleBlackSize != 5) {
                    marbleBlackRand += uuidArry[10] % 5;
                }

            }
            if (marbleIriQual == 1) {
                // Random Marble
                marbleIriQual += uuidArry[5] % 5;
                marbleIriSize += uuidArry[8] % 6;
                if (marbleIriSize != 0 && marbleIriSize != 5) {
                    marbleIriRand += uuidArry[11] % 5;
                }
            }


            clampRGB(melanin);
            clampRGB(pheomelanin);
            clampRGB(iridescenceDark);
            clampRGB(iridescenceLight);
            clampRGB(iridescence);
            clampRGB(metallic1);
            clampRGB(metallic2);
            clampRGB(metallic3);

            int metallic1RGB = Colouration.HSBtoARGB(metallic1[0], metallic1[1], metallic1[2]);
            int metallic2RGB = Colouration.HSBtoARGB(metallic2[0], metallic2[1], metallic2[2]);
            int metallic3RGB = Colouration.HSBtoARGB(metallic3[0], metallic3[1], metallic3[2]);

            int pheomelaninRGB = Colouration.HSBtoABGR(pheomelanin[0], pheomelanin[1], pheomelanin[2]);
            int melaninRGB = Colouration.HSBtoABGR(melanin[0], melanin[1], melanin[2]);
            int iriRGB = Colouration.HSBtoARGB(iridescence[0], iridescence[1], iridescence[2]);
            int iriLightRGB = Colouration.HSBtoARGB(iridescenceLight[0], iridescenceLight[1], iridescenceLight[2]);
            int iriDarkRGB = Colouration.HSBtoARGB(iridescenceDark[0], iridescenceDark[1], iridescenceDark[2]);
            int opaqueRGB = Colouration.HSBtoARGB(opaque[0], opaque[1], opaque[2]);
            int opaqueLightRGB = Colouration.HSBtoARGB(opaqueLight[0], opaqueLight[1], opaqueLight[2]);
            int opaqueDarkRGB = Colouration.HSBtoARGB(opaqueDark[0], opaqueDark[1], opaqueDark[2]);
            int cellophaneRGB = Colouration.HSBtoARGB(cellophane[0], cellophane[1], cellophane[2]);
            this.colouration.setMelaninColour(melaninRGB);
            this.colouration.setPheomelaninColour(pheomelaninRGB);

//            char[] uuidArry = getStringUUID().toCharArray();
//            // Texture Randomizaton
//            if (bodyIri == 5) {
//                bodyIri += uuidArry[1] % 6;
//            }

            /*** TEXTURES ***/

            TextureGrouping iriAlphaGroup = new TextureGrouping(TexturingType.MASK_GROUP);

            TextureGrouping iriOpacityAlphaGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            addTextureToAnimalTextureGrouping(iriOpacityAlphaGroup, TEXTURES_MARBLE, marbleIriQual, marbleIriSize, marbleIriRand, marbleIriQual != 0);
            addTextureToAnimalTextureGrouping(iriOpacityAlphaGroup, TEXTURES_ALPHA, iriIntensity, true);
            iriAlphaGroup.addGrouping(iriOpacityAlphaGroup);

            if (bodyIri != 0) {
                TextureGrouping iriMaskAlphaGroup = new TextureGrouping(TexturingType.MASK_GROUP);
                TextureGrouping iriBodyAndFinAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                addTextureToAnimalTextureGrouping(iriBodyAndFinAlphaGroup, "iri/body/spread.png");
                addTextureToAnimalTextureGrouping(iriBodyAndFinAlphaGroup, TEXTURES_IRI_MASK, maskIri, l -> l != 0);
                iriMaskAlphaGroup.addGrouping(iriBodyAndFinAlphaGroup);
                TextureGrouping iriBodyAndFinGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                addTextureToAnimalTextureGrouping(iriBodyAndFinGroup, TEXTURES_IRI_BODY, bodyIri, l -> l != 0);
                addTextureToAnimalTextureGrouping(iriBodyAndFinGroup, TEXTURES_IRI_BODY, bodyIri, l -> l != 0);
                addTextureToAnimalTextureGrouping(iriBodyAndFinGroup, TEXTURES_IRI_FINS, finIri, l -> l != 0);
                addTextureToAnimalTextureGrouping(iriBodyAndFinGroup, TEXTURES_IRI_RIM, fins, doubletail, iriRim, iriRim != 0);
                iriMaskAlphaGroup.addGrouping(iriBodyAndFinGroup);
                iriAlphaGroup.addGrouping(iriMaskAlphaGroup);
            }

            transRootGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            TextureGrouping transAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                TextureGrouping finAlphaGroup = new TextureGrouping(TexturingType.MASK_GROUP);
                    TextureGrouping finAlphaGroup1 = new TextureGrouping(TexturingType.MASK_GROUP);
                    addTextureToAnimalTextureGrouping(finAlphaGroup1, dumbo ? "mask/solid.png" : "mask/nondumbo.png", true);
                    addTextureToAnimalTextureGrouping(finAlphaGroup1, TEXTURES_FINS, fins, doubletail, crowntail, true);
            finAlphaGroup.addGrouping(finAlphaGroup1);
                    TextureGrouping finAlphaGroup2 = new TextureGrouping(TexturingType.MERGE_GROUP);
                        addTextureToAnimalTextureGrouping(finAlphaGroup2, TEXTURES_FIN_ALPHA, finAlpha, l -> true);
                    finAlphaGroup.addGrouping(finAlphaGroup2);
                    TextureGrouping finAlphaGroup3 = new TextureGrouping(TexturingType.CUTOUT_GROUP);
                        TextureGrouping finCutoutGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                            if (finAlpha == 1) {
                                addTextureToAnimalTextureGrouping(finCutoutGroup, TEXTURES_BUTTERFLY, fins, doubletail, butterfly, butterfly != 0);
                            }
                        finAlphaGroup3.addGrouping(finCutoutGroup);
                        TextureGrouping finPigmentGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                            TextureGrouping finRedPigmentGroup = new TextureGrouping(TexturingType.MASK_GROUP);
                            addTextureToAnimalTextureGrouping(finRedPigmentGroup, TEXTURES_MARBLE, marbleRedQual, marbleRedSize, marbleRedRand, marbleRedQual != 0);
                            addTextureToAnimalTextureGrouping(finRedPigmentGroup, TEXTURES_RED_FIN, finRed, l -> l != 0);
                            addTextureToAnimalTextureGrouping(finRedPigmentGroup, TEXTURES_RED_BODY, bodyRed, l -> l != 0);
                            finPigmentGroup.addGrouping(finRedPigmentGroup);
                            finPigmentGroup.addGrouping(iriAlphaGroup);
                         finAlphaGroup3.addGrouping(finPigmentGroup);
                    finAlphaGroup.addGrouping(finAlphaGroup3);
                transAlphaGroup.addGrouping(finAlphaGroup);
                TextureGrouping eyeAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                  addTextureToAnimalTextureGrouping(eyeAlphaGroup, "mask/eyes.png", true);
                transAlphaGroup.addGrouping(eyeAlphaGroup);
            transRootGroup.addGrouping(transAlphaGroup);

            TextureGrouping rootGroup = new TextureGrouping(TexturingType.MASK_GROUP);
                TextureGrouping bodyAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                addTextureToAnimalTextureGrouping(bodyAlphaGroup, "mask/body.png", true);
                addTextureToAnimalTextureGrouping(bodyAlphaGroup, "mask/gills.png", true);
            rootGroup.addGrouping(bodyAlphaGroup);

            TextureGrouping texturesGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            /** CELLOPHANE **/
            TextureGrouping cellophaneGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(cellophaneGroup, TexturingType.APPLY_RGB, "mask/solid.png", "cel", cellophaneRGB);
            texturesGroup.addGrouping(cellophaneGroup);
            /** RED **/
            TextureGrouping redGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            addTextureToAnimalTextureGrouping(redGroup, TEXTURES_MARBLE, marbleRedQual, marbleRedSize, marbleRedRand, marbleRedQual != 0);
            addTextureToAnimalTextureGrouping(redGroup, TexturingType.APPLY_RED, TEXTURES_ALPHA, 3, l -> true);
            texturesGroup.addGrouping(redGroup);
            /** BLACK **/
            TextureGrouping blackGroup = new TextureGrouping(TexturingType.CUTOUT_GROUP);
            TextureGrouping blackCutoutGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            //Cut the red layer out of the black
            addTextureToAnimalTextureGrouping(blackCutoutGroup, TEXTURES_RED_BODY, bodyRed, l -> l != 0);
            addTextureToAnimalTextureGrouping(blackCutoutGroup, TEXTURES_RED_FIN, finRed, l -> l != 0);
            blackGroup.addGrouping(blackCutoutGroup);
            TextureGrouping blackColorGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            //The marble gene(s) cut holes in each layer
            addTextureToAnimalTextureGrouping(blackColorGroup, TEXTURES_MARBLE, marbleBlackQual, marbleBlackSize, marbleBlackRand, marbleBlackQual != 0);
            addTextureToAnimalTextureGrouping(blackColorGroup, TexturingType.APPLY_BLACK, TEXTURES_ALPHA, 3, l -> l != 0);
            blackGroup.addGrouping(blackColorGroup);
            texturesGroup.addGrouping(blackGroup);
            /** DETAILS **/
            TextureGrouping shadingGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(shadingGroup, "body_shading.png", true);
            texturesGroup.addGrouping(shadingGroup);
            /** IRIDESCENCE **/
            iridescenceGroup = new TextureGrouping(TexturingType.MASK_GROUP);

            iridescenceGroup.addGrouping(iriAlphaGroup);

            TextureGrouping iriColorGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(iriColorGroup, TexturingType.APPLY_RGB, "iri_base.png", "iri", iriRGB);
            addTextureToAnimalTextureGrouping(iriColorGroup, TexturingType.APPLY_RGB, "iri_dark.png", "iri-d", iriDarkRGB);
            addTextureToAnimalTextureGrouping(iriColorGroup, TexturingType.APPLY_RGB, "iri_light.png", "iri-l", iriLightRGB);
            iridescenceGroup.addGrouping(iriColorGroup);
            if (pastelOpaque) {
                TextureGrouping opaqueColorGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                addTextureToAnimalTextureGrouping(opaqueColorGroup, TexturingType.APPLY_RGB, "iri_base.png", "op", opaqueRGB);
                addTextureToAnimalTextureGrouping(opaqueColorGroup, TexturingType.APPLY_RGB, "iri_dark.png", "op-d", opaqueDarkRGB);
                addTextureToAnimalTextureGrouping(opaqueColorGroup, TexturingType.APPLY_RGB, "iri_light.png", "op-l", opaqueLightRGB);
                addTextureToAnimalTextureGrouping(opaqueColorGroup, TexturingType.APPLY_RGB, "body_shading_iri.png", "op-s", opaqueDarkRGB);
                iridescenceGroup.addGrouping(opaqueColorGroup);
            }
            if (metallic != 0) {
                TextureGrouping metallicGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                if (gene[0] == 2 && gene[1] == 2) {
                    //Steel Blue
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/steel_metalliclayer1.png", "sb-mt1", metallic1RGB);
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/steel_metalliclayer2.png", "sb-mt2", metallic2RGB);
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/steel_metalliclayer3.png", "sb-mt3", metallic3RGB);
                }
                else if (gene[0] == 2 || gene[1] == 2) {
                    //Royal Blue
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/royal_metalliclayer1.png", "rb-mt1", metallic1RGB);
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/royal_metalliclayer2.png", "rb-mt2", metallic2RGB);
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/royal_metalliclayer3.png", "rb-mt3", metallic3RGB);
                }
                else {
                    if (metallic == 2) {
                        addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB, "iri/turq_metalliclayer2.png", "t-mt2", metallic2RGB);
                    }
                    addTextureToAnimalTextureGrouping(metallicGroup, TexturingType.APPLY_RGB,"iri/turq_metalliclayer3.png", "t-mt3", metallic3RGB);
                }
                iridescenceGroup.addGrouping(metallicGroup);
                // Metallic seems to look better with normal shading
                iridescenceGroup.addGrouping(shadingGroup);
            }

            texturesGroup.addGrouping(iridescenceGroup);
            /** BUTTERFLY **/
            if (butterfly !=0) {
                TextureGrouping butterflyGroup = new TextureGrouping(TexturingType.MASK_GROUP);
                TextureGrouping butterflyMaskGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                addTextureToAnimalTextureGrouping(butterflyMaskGroup, TEXTURES_BUTTERFLY, fins, doubletail, butterfly, butterfly != 0);
                butterflyGroup.addGrouping(butterflyMaskGroup);
                TextureGrouping butterflyTexGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                if (finAlpha == 1) {
                    addTextureToAnimalTextureGrouping(butterflyTexGroup, TexturingType.APPLY_BLACK, TEXTURES_ALPHA, 3, l -> true);
                } else {
                    addTextureToAnimalTextureGrouping(butterflyTexGroup, TEXTURES_ALPHA, 3, true);
                }
                butterflyGroup.addGrouping(butterflyTexGroup);
                butterflyGroup.addGrouping(shadingGroup);
                texturesGroup.addGrouping(butterflyGroup);
            }
            /** FIN DETAIL **/
            TextureGrouping detailGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(detailGroup, "halfmoon_fins_64.png", true);
            texturesGroup.addGrouping(detailGroup);
            /** EYES **/
            TextureGrouping eyeGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(eyeGroup, TEXTURES_EYES, 0, l -> true);
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

    @Override
    protected EnhancedAnimalAbstract createEnhancedChild(Level level, EnhancedAnimalAbstract otherParent) {
        EnhancedBetta enhancedBetta = ENHANCED_BETTA.get().create(this.level);
        Genes babyGenes = new Genes(this.getGenes()).makeChild(this.getOrSetIsFemale(), otherParent.getOrSetIsFemale(), otherParent.getGenes());
        enhancedBetta.setGenes(babyGenes);
        enhancedBetta.setSharedGenes(babyGenes);
        enhancedBetta.setSireName(otherParent.getCustomName()==null ? "???" : otherParent.getCustomName().getString());
        enhancedBetta.setDamName(this.getCustomName()==null ? "???" : this.getCustomName().getString());
        enhancedBetta.setParent(this.getUUID().toString());
        enhancedBetta.setGrowingAge();
        enhancedBetta.setBirthTime();
        enhancedBetta.initilizeAnimalSize();
        enhancedBetta.setEntityStatus(EntityState.CHILD_STAGE_ONE.toString());
        enhancedBetta.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
        enhancedBetta.calcMaxHealth(true);
        return enhancedBetta;
    }

    protected void createAndSpawnEnhancedChild(Level inWorld) {
        EnhancedBetta enhancedBetta = ENHANCED_BETTA.get().create(this.level);
        Genes babyGenes = new Genes(this.genetics).makeChild(this.getOrSetIsFemale(), this.mateGender, this.mateGenetics);
        defaultCreateAndSpawn(enhancedBetta, inWorld, babyGenes, -this.getAdultAge());
        enhancedBetta.calcMaxHealth(true);

        this.level.addFreshEntity(enhancedBetta);
    }
    @Override
    protected boolean canBePregnant() {
        return true;
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

//    @Override
//    protected void registerGoals() {
//        super.registerGoals();
////        this.goalSelector.addGoal(1, new EnhancedBreedGoal(this, 1.0D));
//    }

    protected Brain<?> makeBrain(Dynamic<?> p_149138_) {
        return BettaBrain.makeBrain(this.brainProvider().makeBrain(p_149138_));
    }
    public Brain<EnhancedBetta> getBrain() {
        return (Brain<EnhancedBetta>)super.getBrain();
    }
    protected void customServerAiStep() {
        this.getBrain().tick((ServerLevel) this.level, this);
        if (!this.isNoAi()) {
            BettaBrain.updateActivity(this);
            if ( Mth.sin(this.level.getGameTime() * 0.25F) > 0.99F ) {
                this.getBrain().setMemory(AddonMemoryModuleTypes.SEEKING_NEST.get(), true);
            }
        }
    }
    public boolean canBreatheUnderwater() {
        return true;
    }

    public void aiStep() {
        if (!this.isInWater() && this.onGround && this.verticalCollision) {
            //The flop on land
//            this.setDeltaMovement(this.getDeltaMovement().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), (double)0.4F, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
//            this.onGround = false;
//            this.hasImpulse = true;
//            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
        }
        else if (this.isAnimalSleeping() && !this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, this.brain.hasMemoryValue(AddonMemoryModuleTypes.FOUND_SLEEP_SPOT.get()) ? -0.001 : -0.01, 0.0));
        }
        super.aiStep();
        bubble();
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

    private void clampRGB(float[] color) {
//        if (clampHue) {
//            float minHue = 0.020F;
//            float maxHue = 0.101F;
//            if (color[0] < minHue) {
//                color[0] = minHue;
//            }
//            else if (color[0] > maxHue) {
//                color[0] = maxHue;
//            }
//        }
        for (int i = 0; i <= 2; i++) {
            if (color[i] > 1.0F) {
                color[i] = 1.0F;
            } else if (color[i] < 0.0F) {
                color[i] = 0.0F;
            }
        }
    }

    public TextureGrouping getIridescenceGroup() {
        return iridescenceGroup;
    }

    public boolean isHighlyAggressive() {
        return getAggression() >= 8;
    }
    public boolean isAggressive() {
        return getAggression() >= 4;
    }

    public boolean isPassive() {
        return !isAggressive();
    }


    public int getAggression() {
        if (this.aggression == -1 && this.getGenes() != null) {
            int[] gene = this.getGenes().getAutosomalGenes();
            this.aggression = (gene[72] + gene[73])/2;
        }
        return aggression;
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
            super(pBetta, 85, 10, 0.1F, 0.5F, false);
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
        } else {
            readLegacyGenes(compoundNBT.getList(key.equals("Genetics") ? "Genes" : "FatherGenes", 10), genetics);
        }
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
            EnhancedBettaBucket.setGenes(stack, this.genetics!=null? this.genetics : getGenes());
            EnhancedBettaBucket.setParentNames(stack, this.sireName, this.damName);
            EnhancedBettaBucket.setEquipment(stack, this.animalInventory.getItem(1));
            if (this.isPregnant() && this.mateGenetics != null) {
                EnhancedBettaBucket.setMateGenes(stack, this.mateGenetics, this.mateGender);
            }
            EnhancedBettaBucket.setBettaUUID(stack, this.getUUID().toString());
            EnhancedBettaBucket.setBirthTime(stack, this.getBirthTime());
        }
    }

    public boolean isPregnant() {
        return this.entityData.get(PREGNANT) || this.pregnant;
    }

    public void setPregnant(boolean hasEgg) {
        this.entityData.set(PREGNANT, hasEgg);
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
        this.setIsFemale(tag.getCompound("Genetics"));
        this.toggleReloadTexture();
        calcMaxHealth(true);
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
        enhancedBetta.setIsAngry(false);
    }

    @Override
    public Boolean isAnimalSleeping() {
        if (!this.isInWaterRainOrBubble()) {
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
        return (((this.level.getDayTime()%24000 >= 12600 && this.level.getDayTime()%24000 <= 22000) || this.level.isThundering()) && this.awokenTimer == 0 && !this.sleeping);
    }


    public void findPlaceToSleep() {
        int horizontalRange = 5;
        int verticalRange = 5;

        if (this.getLeashHolder() != null) {
            horizontalRange = 2;
            verticalRange = 2;
        }

        BlockPos baseBlockPos = new BlockPos(this.blockPosition());
        BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos();

        for(int k = 0; k <= verticalRange; k = k > 0 ? -k : 1 - k) {
            for(int l = 0; l < horizontalRange; ++l) {
                for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                    for(int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                        mutableblockpos.set(baseBlockPos).move(i1, k-1, j1);
                        // Is Dripleaf?
                        if (this.level.getBlockState(mutableblockpos).getBlock() instanceof BigDripleafBlock) {
                            this.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(mutableblockpos,0.4F, 0));
                            this.getBrain().setMemoryWithExpiry(AddonMemoryModuleTypes.FOUND_SLEEP_SPOT.get(), level.getGameTime(),500);
                            return;
                        }
                    }
                }
            }
        }
        return;
    }

    public boolean hasNoSleepSpot() {
        return !this.getBrain().hasMemoryValue(AddonMemoryModuleTypes.FOUND_SLEEP_SPOT.get());
    }

    public boolean isNotSleeping() {
        return !isAnimalSleeping();
    }

    public void setIsAngry(boolean angry) {
        this.entityData.set(IS_ANGRY, angry);
    }

    public boolean getIsAngry() {
        return this.entityData.get(IS_ANGRY);
    }

    public boolean canMakeBubbleNest(BlockPos blockPos) {
        if (this.level.isEmptyBlock(blockPos) && this.level.isWaterAt(blockPos) && !this.level.isWaterAt(blockPos.above())) {
            return true;
        }
        return false;
    }

    public void setNestPos(BlockPos position) {
        this.entityData.set(NEST_POS, position);
    }

    public BlockPos getNestPos() {
        return this.entityData.get(NEST_POS);
    }

    public void findNestLocation() {
        BlockPos baseBlockPos = new BlockPos(this.blockPosition());
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        int maxSearchHeight = 8;

        for (int y = 0; y < maxSearchHeight; y++) {
            mutableBlockPos.set(baseBlockPos).move(0, y, 0);
            if (this.level.isWaterAt(mutableBlockPos) && !this.level.isWaterAt(mutableBlockPos.above())) {
                setNestPos(mutableBlockPos);
                return;
            }
        }
    }
}
