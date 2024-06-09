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

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;

public class EnhancedBetta extends EnhancedAnimalAbstract {

    protected static final ImmutableList<? extends SensorType<? extends Sensor<? super EnhancedBetta>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.HURT_BY, ModSensorTypes.COW_FOOD_TEMPTATIONS.get());
    protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.BREED_TARGET, ModMemoryModuleTypes.HAS_EGG.get(), MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.HAS_HUNTING_COOLDOWN);
//    private TextureGrouping iridescenceGroup;
    private TextureGrouping transRootGroup;
    private TextureGrouping iridescenceGroup;

    private static final String[] TEXTURES_FINS = new String[] {
            "mask/wildtype.png", "mask/halfmoon.png"
    };

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
            "iri/body/spread.png", "iri/body/spread_het_mask.png", "iri/body/spread_homo_mask.png"
    };
    private static final String[] TEXTURES_RED = new String[] {
            "", "red/wildtype.png", "red/lower_1.png", "red/lower_2.png", "red/extended.png", "red/extended_het_mask.png", "red/extended_homo_mask.png"
    };

    private static final String[] TEXTURES_BUTTERFLY = new String[] {
            "", "butterfly/long_butterfly_min.png", "butterfly/long_butterfly_medium.png", "butterfly/long_butterfly_high.png", "butterfly/long_butterfly_max.png",
    };
    private static final String[] TEXTURES_RED_TINT = new String[] {
            "", "red/bodyred1.png"
    };

    private static final String[] TEXTURES_EYES = new String[] {
            "eyes_wildtype.png"
    };

    private static final String[] TEXTURES_SHADING = new String[] {
            "body_shading.png", "body_shading_light.png"
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
            int fins = 1;
            int finAlpha = 0;
            int red = 1;
            int red_tint = 0;
            int black = 1;
            int shading = 0;
            int butterfly = 0;
            boolean cambodian = false;

            /*** COLORATION ***/
            float[] melanin = {0.0427F, 0.527F, 0.251F};
            float[] pheomelanin = { 0.991F, 0.978F, 0.655F };
            float[] iridescenceLight = { 0.44F, 0.978F, 0.878F };
            float[] iridescence = { 0.530F, 0.715F, 0.634F };
            float[] iridescenceDark = { 0.582F, 0.808F, 0.604F };
            float[] cellophane = { 0.1025F, 0.105F, 0.972F };

            //Body Iridescence Level
            int body_iri_level = 0;
            for (int i = 24; i < 28; i++) {
                if (gene[i] == 2) {
                    body_iri_level++;
                }
            }
            body_iri += body_iri_level/2;

            //Fin Iridescence Level
            int fin_iri_level = 0;
            for (int i = 28; i < 32; i++) {
                if (gene[i] == 2) {
                    fin_iri_level++;
                }
            }
            fin_iri += fin_iri_level/2;

            if (gene[2] == 2 || gene[3] == 2) {
                //Spread
                body_iri = 4;
                if (gene[4] == 2 && gene[5] == 2) {
                    //Homozygous Masked
                    body_iri = 6;
                }
                else if (gene[4] == 2 || gene[5] == 2) {
                    //Heterozygous Masked
                    body_iri = 5;
                }
            }

            // Iridescence Color
            if (gene[0] == 2 && gene[1] == 2) {
                //Steel Blue
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
            else if (gene[0] == 2 || gene[1] == 2) {
                //Royal Blue (het steel blue)
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
                shading = 1;
                red_tint = 1;
                melanin[0] = 0.1025F;
                melanin[1] = 0.105F;
                melanin[2] = 0.972F;
            }

            if (gene[12] == 2 || gene[13] == 2) {
                //Extended Red
                red = 4;
                if (gene[16] == 2 && gene[17] == 2) {
                    //Homozygous Masked
                    red = 6;
                }
                else if (gene[16] == 2 || gene[17] == 2) {
                    //Heterozygous Masked
                    red = 5;
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

            clampRGB(melanin);
            clampRGB(pheomelanin);
            clampRGB(iridescenceDark);
            clampRGB(iridescenceLight);
            clampRGB(iridescence);

            int pheomelaninRGB = Colouration.HSBtoABGR(pheomelanin[0], pheomelanin[1], pheomelanin[2]);
            int melaninRGB = Colouration.HSBtoABGR(melanin[0], melanin[1], melanin[2]);
            int iriRGB = Colouration.HSBtoARGB(iridescence[0], iridescence[1], iridescence[2]);
            int iriLightRGB = Colouration.HSBtoARGB(iridescenceLight[0], iridescenceLight[1], iridescenceLight[2]);
            int iriDarkRGB = Colouration.HSBtoARGB(iridescenceDark[0], iridescenceDark[1], iridescenceDark[2]);
            int cellophaneRGB = Colouration.HSBtoARGB(cellophane[0], cellophane[1], cellophane[2]);
            this.colouration.setMelaninColour(melaninRGB);
            this.colouration.setPheomelaninColour(pheomelaninRGB);

            /*** TEXTURES ***/
            transRootGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            TextureGrouping transAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            TextureGrouping finAlphaGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            TextureGrouping finAlphaGroup1 = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(finAlphaGroup1, TEXTURES_FINS, fins, l -> true);
            finAlphaGroup.addGrouping(finAlphaGroup1);
            TextureGrouping finAlphaGroup2 = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(finAlphaGroup2, TEXTURES_FIN_ALPHA, finAlpha, l -> true);
            finAlphaGroup.addGrouping(finAlphaGroup2);
            TextureGrouping finAlphaGroup3 = new TextureGrouping(TexturingType.CUTOUT_GROUP);
            TextureGrouping finCutoutGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
//            addTextureToAnimalTextureGrouping(finCutoutGroup, TEXTURES_BUTTERFLY, butterfly, l -> l != 0);
            finAlphaGroup3.addGrouping(finCutoutGroup);
            TextureGrouping finPigmentGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(finPigmentGroup, TEXTURES_RED, red, l -> l != 0);
            addTextureToAnimalTextureGrouping(finPigmentGroup, TEXTURES_RED_TINT, red_tint, l -> l != 0);
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
            /** BLACK **/
            TextureGrouping blackGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(blackGroup, TexturingType.APPLY_BLACK, TEXTURES_BLACK, black, l -> l != 0);
            texturesGroup.addGrouping(blackGroup);
            /** RED **/
            TextureGrouping redGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(redGroup, TexturingType.APPLY_RED, TEXTURES_RED_TINT, red_tint, l -> l != 0);
            addTextureToAnimalTextureGrouping(redGroup, TexturingType.APPLY_RED, TEXTURES_RED, red, l -> l != 0);
            texturesGroup.addGrouping(redGroup);
            /** IRIDESCENCE **/
            iridescenceGroup = new TextureGrouping(TexturingType.MASK_GROUP);
            TextureGrouping iriAlphaGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(iriAlphaGroup, TEXTURES_IRI_FINS, fin_iri, l -> l != 0);
            addTextureToAnimalTextureGrouping(iriAlphaGroup, TEXTURES_IRI_BODY, body_iri, l -> l != 0);
            iridescenceGroup.addGrouping(iriAlphaGroup);
            TextureGrouping iriColorGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(iriColorGroup, TexturingType.APPLY_RGB, "iri_base.png", "iri", iriRGB);
            addTextureToAnimalTextureGrouping(iriColorGroup, TexturingType.APPLY_RGB, "iri_dark.png", "iri-d", iriDarkRGB);
            addTextureToAnimalTextureGrouping(iriColorGroup, TexturingType.APPLY_RGB, "iri_light.png", "iri-l", iriLightRGB);
            iridescenceGroup.addGrouping(iriColorGroup);
            texturesGroup.addGrouping(iridescenceGroup);
            /** BUTTERFLY **/
            TextureGrouping butterflyGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(butterflyGroup, TEXTURES_BUTTERFLY, butterfly, l -> l != 0);
            texturesGroup.addGrouping(butterflyGroup);
            /** DETAILS **/
            TextureGrouping detailGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(detailGroup, "halfmoon_fins_64.png", true);
            addTextureToAnimalTextureGrouping(detailGroup, TEXTURES_SHADING, shading, true);
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
}
