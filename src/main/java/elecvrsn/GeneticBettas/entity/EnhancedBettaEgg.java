package elecvrsn.GeneticBettas.entity;

import elecvrsn.GeneticBettas.config.BettasCommonConfig;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import elecvrsn.GeneticBettas.items.EnhancedBettaEggBucket;
import mokiyoki.enhancedanimals.util.Genes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;
import static elecvrsn.GeneticBettas.init.AddonItems.ENHANCED_BETTA_EGG_BUCKET;
import static net.minecraft.world.level.material.Fluids.WATER;

public class EnhancedBettaEgg extends Entity {
    private static final EntityDataAccessor<String> GENES = SynchedEntityData.<String>defineId(EnhancedBettaEgg.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> SIRE = SynchedEntityData.<String>defineId(EnhancedBettaEgg.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DAM = SynchedEntityData.<String>defineId(EnhancedBettaEgg.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> HATCH_TIME = SynchedEntityData.<Integer>defineId(EnhancedBettaEgg.class, EntityDataSerializers.INT);
    private boolean hasParents = false;
    public EnhancedBettaEgg(EntityType<? extends EnhancedBettaEgg> entityType, Level level) {
        super(entityType, level);
        setHatchTime(BettasCommonConfig.COMMON.bettaHatchTime.get());
    }

    public void setGenes(Genes eggGenes) {
        if (eggGenes != null) {
            this.getEntityData().set(GENES, eggGenes.getGenesAsString());
        } else {
            this.getEntityData().set(GENES, "INFERTILE");
        }
    }

    public void setGenes(String genes) {
        this.getEntityData().set(GENES, genes);
    }

    public String getGenes() {
        return this.entityData.get(GENES);
    }

    public void setParentNames(String sireName, String damName) {
        if (sireName!=null && !sireName.isEmpty()) {
            this.getEntityData().set(SIRE, sireName);
        }
        if (damName!=null && !damName.isEmpty()) {
            this.getEntityData().set(DAM, damName);
        }
    }

    public String getSire() {
        String sireName = this.entityData.get(SIRE);
        if (sireName!=null && !sireName.isEmpty()) {
            return sireName;
        } else {
            return "???";
        }
    }

    public String getDam() {
        String damName = this.entityData.get(DAM);
        if (damName!=null && !damName.isEmpty()) {
            return damName;
        } else {
            return "???";
        }
    }

    private int getHatchTime() {
        return this.entityData.get(HATCH_TIME);
    }

    public void setHatchTime(int hatchTime) {
        this.entityData.set(HATCH_TIME, hatchTime);
    }

    public boolean skipAttackInteraction(Entity entity) {
        if (entity instanceof Player player) {
            return !this.level.mayInteract(player, this.blockPosition()) || this.hurt(DamageSource.playerAttack(player), 0.0F);
        } else {
            return false;
        }
    }

    public boolean hurt(DamageSource damageSource, float p_31716_) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else {
            if (!this.isRemoved() && !this.level.isClientSide) {
                this.kill();
                this.markHurt();
            }

            return true;
        }
    }

    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        if (this.level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemStack = player.getItemInHand(interactionHand);
            if (!itemStack.isEmpty() && itemStack.is(Items.WATER_BUCKET)) {
                this.discard();
                itemStack = new ItemStack(ENHANCED_BETTA_EGG_BUCKET.get());
                EnhancedBettaEggBucket.setData(itemStack, this.getSire(), this.getDam(), this.getGenes(), this.getHatchTime());
                if (this.hasCustomName()) {
                    itemStack.setHoverName(this.getCustomName());
                }
                player.setItemInHand(interactionHand, itemStack);
            }
            return InteractionResult.CONSUME;
        }
    }

    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    public boolean isPickable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (!onEggAttachableBlock(this.blockPosition(), this.level)) {
            fallOrFloat(this.blockPosition(), this.level);
        }

        pushEntities();

        if (this.getHatchTime() == 0) {
            if (!this.level.isClientSide) {
                createAndSpawnChild();
            }
        } else if (this.getHatchTime() > 0){
            this.setHatchTime(this.getHatchTime() - 1);
        }
    }

    public static boolean isEggAttachableBlock(BlockState blockState) {
        Block block = blockState.getBlock();
        return AddonBlocks.BUBBLE_NEST.get().equals(block);
    }

    public boolean onEggAttachableBlock(BlockPos blockPos, LevelReader level) {
        BlockGetter blockGetter = level.getChunkForCollisions(this.chunkPosition().x, this.chunkPosition().z);
        BlockState blockState = level.getBlockState(blockPos);
        VoxelShape voxelShape = blockState.getShape(blockGetter, this.blockPosition());
        if (!voxelShape.isEmpty()) {
            if (isEggAttachableBlock(blockState)) {
                return this.getBoundingBox().maxY<=(blockPos.getY()+voxelShape.bounds().maxY) && this.getBoundingBox().maxY>=(blockPos.getY()+voxelShape.bounds().minY);
            }
        }
        return false;
    }

    private void fallOrFloat(BlockPos blockPos, LevelReader level) {
        if (!this.isNoGravity()) {
            if ((level.getBlockState(blockPos.above()).isAir() && level.getFluidState(blockPos).isSourceOfType(WATER) && this.getBoundingBox().getCenter().y >= blockPos.above().getY()-0.125 && this.getBoundingBox().getCenter().y <= blockPos.above().getY()+0.125)) {
                this.setDeltaMovement(this.getDeltaMovement().scale(0D));
                this.setPos(this.position().x, blockPos.above().getY()-0.1875D, this.position().z);
            }
            else {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, this.isInWater() ? 0.01D : -0.04D, 0.0D));
            }
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    protected void pushEntities() {
        List<EnhancedBettaEgg> list = this.level.getEntitiesOfClass(EnhancedBettaEgg.class, this.getBoundingBox());
        if (!list.isEmpty()) {
            int max_cramming = this.level.getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
            if (max_cramming > 0 && list.size() > max_cramming - 1 && this.random.nextInt(4) == 0) {
                int colliding_entities = 0;

                for (EnhancedBettaEgg enhancedBettaEgg : list) {
                    if (!enhancedBettaEgg.isPassenger()) {
                        colliding_entities++;
                    }
                }

                if (colliding_entities > max_cramming - 1) {
                    this.hurt(DamageSource.CRAMMING, 6.0F);
                }
            }

            for (Entity entity : list) {
                entity.push(this);
            }
        }

    }

    private void createAndSpawnChild() {
        this.playSound(SoundEvents.SLIME_BLOCK_HIT, 1.0F, 1.0F);
        EnhancedBetta betta = ENHANCED_BETTA.get().create(level);
        if (betta == null) return; // Prevent a server-crashing null pointer error, just in case

        if (!this.getGenes().isEmpty() && !this.getGenes().equals("INFERTILE")) {
            betta.setGenes(new Genes(this.getGenes()));
            betta.setSharedGenes(new Genes(this.getGenes()));
        } else {
            Genes genes = betta.createInitialBreedGenes(betta.getCommandSenderWorld(), betta.blockPosition(), "WanderingTrader");
            betta.setGenes(genes);
            betta.setSharedGenes(genes);
        }
        betta.setSireName(this.getSire());
        betta.setDamName(this.getDam());
        betta.setAge(-betta.getAdultAge());
        betta.setBirthTime(String.valueOf(level.getGameTime()));
        betta.initilizeAnimalSize();
        betta.moveTo(this.xo,this.yo, this.zo, this.xRotO, this.yRotO);
        if (this.hasCustomName()) {
            betta.setCustomName(this.getCustomName());
        }
        level.addFreshEntity(betta);
        betta.setInitialDefaults();
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(GENES, "");
        this.getEntityData().define(SIRE, "");
        this.getEntityData().define(DAM, "");
        this.getEntityData().define(HATCH_TIME, -1);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        String genes = compound.getString("genes");
        this.getEntityData().set(GENES, genes);
        this.getEntityData().set(SIRE, compound.getString("SireName"));
        this.getEntityData().set(DAM, compound.getString("DamName"));
        this.hasParents = compound.getBoolean("hasParents");
        this.setHatchTime(compound.contains("HatchTime") ? compound.getInt("HatchTime") : BettasCommonConfig.COMMON.bettaHatchTime.get());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        String genes = this.getGenes();
        if (!genes.isEmpty()) {
            compound.putString("genes", genes);
        }
        compound.putString("SireName", this.getSire());
        compound.putString("DamName", this.getDam());
        compound.putBoolean("hasParents", this.hasParents);
        compound.putInt("HatchTime", this.getHatchTime());
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
