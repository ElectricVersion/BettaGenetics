package elecvrsn.GeneticBettas.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import elecvrsn.GeneticBettas.ai.brain.betta.BettaBrain;
import elecvrsn.GeneticBettas.entity.genetics.BettaGeneticsInitialiser;
import elecvrsn.GeneticBettas.model.modeldata.BettaModelData;
import elecvrsn.GeneticBettas.util.AddonReference;
import mokiyoki.enhancedanimals.ai.general.EnhancedBreedGoal;
import mokiyoki.enhancedanimals.config.EanimodCommonConfig;
import mokiyoki.enhancedanimals.entity.EnhancedAnimalAbstract;
import mokiyoki.enhancedanimals.entity.EntityState;
import mokiyoki.enhancedanimals.entity.util.Colouration;
import mokiyoki.enhancedanimals.init.FoodSerialiser;
import mokiyoki.enhancedanimals.init.ModMemoryModuleTypes;
import mokiyoki.enhancedanimals.init.ModSensorTypes;
import mokiyoki.enhancedanimals.model.modeldata.AnimalModelData;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TexturingType;
import mokiyoki.enhancedanimals.util.Genes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import java.awt.*;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;

public class EnhancedBetta extends EnhancedAnimalAbstract {

    protected static final ImmutableList<? extends SensorType<? extends Sensor<? super EnhancedBetta>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.HURT_BY, ModSensorTypes.COW_FOOD_TEMPTATIONS.get());
    protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.BREED_TARGET, ModMemoryModuleTypes.HAS_EGG.get(), MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.HAS_HUNTING_COOLDOWN);
//    private TextureGrouping iridescenceGroup;
    private TextureGrouping transRootGroup;
    private TextureGrouping iridescenceGroup;

//    private static final String[] TEXTURES_FINS = new String[] {
//            "mask/wildtype.png", "mask/halfmoon.png"
//    };

    private static final String[] TEXTURES_FIN_ALPHA = new String[] {
            "mask/solid.png", "mask/lace_lg.png", "mask/cambodian_lg.png"
    };


    private static final String[] TEXTURES_BLACK = new String[] {
            "", "wildtype_black.png"
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

    private static final String[] TEXTURES_BUTTERFLY = new String[] {
            "", "butterfly/long_butterfly_min.png", "butterfly/long_butterfly_medium.png", "butterfly/long_butterfly_high.png", "butterfly/long_butterfly_max.png",
    };
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
            "", "black/marble/marble_1.png", "black/marble/marble_2.png", "black/marble/marble_4.png", "black/marble/marble_5.png",
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
        this.moveControl = new EnhancedBetta.BettaMoveControl(this);
        this.lookControl = new EnhancedBetta.BettaLookControl(this, 20);
        this.initilizeAnimalSize();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
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
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    protected void setTexturePaths() {
        if (this.getGenes() != null) {
            int[] gene = getGenes().getAutosomalGenes();

            /*** PHENOTYPE ***/
            int body_iri = 1;
            int fin_iri = 1;
            int mask_iri = 0;
            int fins = 1;
            int finAlpha = 0;
            int fin_red = 4;
            int bodyRed = 2;
            int black = 1;
            int butterfly = 0;
            int iriIntensity = 2;
            boolean pastelOpaque = false;
            boolean cambodian = false;
            int metallic = 0;
            int marble_black = 0;
            boolean dumbo = false;

            /*** COLORATION ***/
            float[] melanin = {0.0427F, 0.527F, 0.251F};
            float[] pheomelanin = { 0.991F, 0.978F, 0.655F };
            float[] cellophane = { 0.1025F, 0.105F, 0.972F };
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
            body_iri += bodyIriLevel/2;

            //Fin Iridescence Level
            int finIriLevel = 0;
            for (int i = 28; i < 32; i++) {
                if (gene[i] == 2) {
                    finIriLevel++;
                }
            }
            fin_iri += finIriLevel/2;

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
                    fin_red--;
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
                body_iri = 4;
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
                    case 0 -> mask_iri = 1;
                    case 1 -> mask_iri = 3;
                    case 2 -> mask_iri = 5;
                    case 3 -> mask_iri = 7;
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

            if (gene[56] == 2 && gene[57] == 2) {
                dumbo = true;
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
//            if (body_iri == 5) {
//                body_iri += uuidArry[1] % 6;
//            }


            /*** TEXTURES ***/
            transRootGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            TextureGrouping transAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                TextureGrouping finAlphaGroup = new TextureGrouping(TexturingType.MASK_GROUP);
                    TextureGrouping finAlphaGroup1 = new TextureGrouping(TexturingType.MASK_GROUP);
                    addTextureToAnimalTextureGrouping(finAlphaGroup1, dumbo ? "mask/solid.png" : "mask/nondumbo.png", true);
                    addTextureToAnimalTextureGrouping(finAlphaGroup1, TEXTURES_FINS, fins, 0, 0, true);
            finAlphaGroup.addGrouping(finAlphaGroup1);
                    TextureGrouping finAlphaGroup2 = new TextureGrouping(TexturingType.MERGE_GROUP);
                        addTextureToAnimalTextureGrouping(finAlphaGroup2, TEXTURES_FIN_ALPHA, finAlpha, l -> true);
                    finAlphaGroup.addGrouping(finAlphaGroup2);
                    TextureGrouping finAlphaGroup3 = new TextureGrouping(TexturingType.CUTOUT_GROUP);
                        TextureGrouping finCutoutGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                            if (finAlpha == 1) {
                                addTextureToAnimalTextureGrouping(finCutoutGroup, TEXTURES_BUTTERFLY, butterfly, l -> l != 0);
                            }
                        finAlphaGroup3.addGrouping(finCutoutGroup);
                        TextureGrouping finPigmentGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
                            addTextureToAnimalTextureGrouping(finPigmentGroup, TEXTURES_RED_FIN, fin_red, l -> l != 0);
                            addTextureToAnimalTextureGrouping(finPigmentGroup, TEXTURES_RED_BODY, bodyRed, l -> l != 0);
                            addTextureToAnimalTextureGrouping(finPigmentGroup, TEXTURES_IRI_BODY, body_iri, l -> l != 0);
                            addTextureToAnimalTextureGrouping(finPigmentGroup, TEXTURES_IRI_FINS, fin_iri, l -> l != 0);
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
            rootGroup.addGrouping(bodyAlphaGroup);

            TextureGrouping texturesGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            /** CELLOPHANE **/
            TextureGrouping cellophaneGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(cellophaneGroup, TexturingType.APPLY_RGB, "mask/solid.png", "cel", cellophaneRGB);
            texturesGroup.addGrouping(cellophaneGroup);
            /** RED **/
            TextureGrouping redGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(redGroup, TexturingType.APPLY_RED, TEXTURES_ALPHA, 3, l -> l != 0);
            texturesGroup.addGrouping(redGroup);
            /** BLACK **/
            TextureGrouping blackGroup = new TextureGrouping(TexturingType.CUTOUT_GROUP);
            TextureGrouping blackCutoutGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            //Cut the red layer out of the black
            addTextureToAnimalTextureGrouping(blackCutoutGroup, TEXTURES_RED_BODY, bodyRed, l -> l != 0);
            addTextureToAnimalTextureGrouping(blackCutoutGroup, TEXTURES_RED_FIN, fin_red, l -> l != 0);
            //The marble gene(s) cut holes in each layer
            addTextureToAnimalTextureGrouping(blackCutoutGroup, TEXTURES_MARBLE_BLACK, marble_black, l -> l != 0);
            blackGroup.addGrouping(blackCutoutGroup);
            TextureGrouping blackColorGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(blackColorGroup, TexturingType.APPLY_BLACK, TEXTURES_ALPHA, 3, l -> l != 0);
            blackGroup.addGrouping(blackColorGroup);
            texturesGroup.addGrouping(blackGroup);
            /** DETAILS **/
            TextureGrouping shadingGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(shadingGroup, "body_shading.png", true);
            texturesGroup.addGrouping(shadingGroup);
            /** IRIDESCENCE **/
            iridescenceGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            TextureGrouping iriAlphaGroup = new TextureGrouping(TexturingType.MASK_GROUP);

            TextureGrouping iriOpacityAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(iriOpacityAlphaGroup, TEXTURES_ALPHA, iriIntensity, true);
            iriAlphaGroup.addGrouping(iriOpacityAlphaGroup);

            TextureGrouping iriBodyAndFinAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(iriBodyAndFinAlphaGroup, TEXTURES_IRI_FINS, fin_iri, l -> l != 0);
            addTextureToAnimalTextureGrouping(iriBodyAndFinAlphaGroup, TEXTURES_IRI_BODY, body_iri, l -> l != 0);
            iriAlphaGroup.addGrouping(iriBodyAndFinAlphaGroup);

            if (body_iri != 0) {
                TextureGrouping iriMaskAlphaGroup = new TextureGrouping(TexturingType.MASK_GROUP);
                addTextureToAnimalTextureGrouping(iriMaskAlphaGroup, TEXTURES_IRI_BODY, body_iri, l -> l != 0);
                addTextureToAnimalTextureGrouping(iriMaskAlphaGroup, "iri/body/spread.png", mask_iri == 0);
                addTextureToAnimalTextureGrouping(iriMaskAlphaGroup, TEXTURES_IRI_MASK, mask_iri, l -> l != 0);
                iriAlphaGroup.addGrouping(iriMaskAlphaGroup);
            }

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
                addTextureToAnimalTextureGrouping(butterflyMaskGroup, TEXTURES_BUTTERFLY, butterfly, l -> l != 0);
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
        return enhancedBetta;
    }

    protected void createAndSpawnEnhancedChild(Level inWorld) {
        EnhancedBetta enhancedbetta = ENHANCED_BETTA.get().create(this.level);
        Genes babyGenes = new Genes(this.genetics).makeChild(this.getOrSetIsFemale(), this.mateGender, this.mateGenetics);
        defaultCreateAndSpawn(enhancedbetta, inWorld, babyGenes, -this.getAdultAge());

        this.level.addFreshEntity(enhancedbetta);
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

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new EnhancedBreedGoal(this, 1.0D));
    }

    protected Brain<?> makeBrain(Dynamic<?> p_149138_) {
        return BettaBrain.makeBrain(this.brainProvider().makeBrain(p_149138_));
    }
    public Brain<EnhancedBetta> getBrain() {
        return (Brain<EnhancedBetta>)super.getBrain();
    }
    protected void customServerAiStep() {
        this.getBrain().tick((ServerLevel)this.level, this);
        BettaBrain.updateActivity(this);
    }
    public boolean canBreatheUnderwater() {
        return true;
    }

    public void aiStep() {
        super.aiStep();
//        bubble();
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
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.BUBBLE, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
            bettaModelData.isBubbling = false;
        }
    }

}
