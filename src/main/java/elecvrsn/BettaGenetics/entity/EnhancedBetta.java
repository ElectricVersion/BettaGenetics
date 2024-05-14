package elecvrsn.BettaGenetics.entity;

import elecvrsn.BettaGenetics.model.modeldata.BettaModelData;
import mokiyoki.enhancedanimals.config.EanimodCommonConfig;
import mokiyoki.enhancedanimals.entity.EnhancedAnimalAbstract;
import elecvrsn.BettaGenetics.entity.genetics.BettaGeneticsInitialiser;
import mokiyoki.enhancedanimals.init.FoodSerialiser;
import mokiyoki.enhancedanimals.model.modeldata.AnimalModelData;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TexturingType;
import mokiyoki.enhancedanimals.util.Genes;
import elecvrsn.BettaGenetics.util.AddonReference;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import static elecvrsn.BettaGenetics.init.AddonEntities.ENHANCED_BETTA;
import static mokiyoki.enhancedanimals.init.FoodSerialiser.pigFoodMap;

public class EnhancedBetta extends EnhancedAnimalAbstract {
    private static final String[] TEXTURES_BASE = new String[] {
            "wildtype.png"
    };
    private static final String[] TEXTURES_EYES = new String[] {
            "eyes.png"
    };
    private boolean resetTexture = true;

    @OnlyIn(Dist.CLIENT)
    private BettaModelData bettaModelData;
    public EnhancedBetta(EntityType<? extends EnhancedBetta> entityType, Level worldIn) {
        super(entityType, worldIn, 2, AddonReference.BETTA_AUTOSOMAL_GENES_LENGTH, true);
        this.initilizeAnimalSize();
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
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
        return EanimodCommonConfig.COMMON.gestationDaysPig.get();
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
    @OnlyIn(Dist.CLIENT)
    protected void setTexturePaths() {
        if (this.getGenes() != null) {
            TextureGrouping rootGroup = new TextureGrouping(TexturingType.MERGE_GROUP);
            addTextureToAnimalTextureGrouping(rootGroup, TEXTURES_BASE, 0, l -> true);
            addTextureToAnimalTextureGrouping(rootGroup, TEXTURES_EYES, 0, l -> true);
            this.setTextureGrouping(rootGroup);
        }
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    protected void setAlphaTexturePaths() {
    }

    public void initilizeAnimalSize() {
        this.setAnimalSize(1.0F);
    }

    @Override
    protected EnhancedAnimalAbstract createEnhancedChild(Level level, EnhancedAnimalAbstract enhancedAnimalAbstract) {
        return null;
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
        return pigFoodMap();
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
    protected void registerGoals() {
        int napmod = this.random.nextInt(1200);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
    }
}
