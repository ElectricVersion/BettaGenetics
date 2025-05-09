package elecvrsn.GeneticBettas.block.entity;

import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import elecvrsn.GeneticBettas.items.EnhancedBettaBucket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.entity.Entity.RemovalReason.DISCARDED;

public class FilledDisplayTankBlockEntity extends BlockEntity {
    private EnhancedBetta displayEntity;
    private CompoundTag entityTag;

    public FilledDisplayTankBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AddonBlocks.FILLED_DISPLAY_TANK_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public EnhancedBetta getOrCreateDisplayEntity() {
        if (level != null && displayEntity == null && hasEntityTag()) {
            displayEntity = EnhancedBettaBucket.spawnBetta(level, entityTag, getBlockPos());
            if (displayEntity != null) {
                displayEntity.setInTank(true);
            }
        }
        else if (level != null && displayEntity != null && !hasEntityTag()) {
            displayEntity.remove(DISCARDED);
            displayEntity = null;
        }
        return displayEntity;
    }

    public boolean hasEntityTag() {
        return entityTag != null && !entityTag.isEmpty();
    }

    public void setEntityTag(CompoundTag nbtData) {
        if (level != null && !level.isClientSide()) {
            entityTag = nbtData;
//            getOrCreateDisplayEntity();
            setChanged();
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("EntityData", hasEntityTag() ? entityTag : new CompoundTag());
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        entityTag = compound.getCompound("EntityData");
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }
}
