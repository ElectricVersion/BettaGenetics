package elecvrsn.GeneticBettas.block.entity;

import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import elecvrsn.GeneticBettas.items.EnhancedBettaBucket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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

    public EnhancedBetta getOrCreateDisplayEntity(Level level) {
            if (displayEntity == null && hasEntityTag()) {
                displayEntity = EnhancedBettaBucket.spawnBettaFromTag(level, entityTag, getBlockPos());
                displayEntity.setInTank(true);
            } else if (displayEntity != null && !hasEntityTag()) {
                displayEntity.remove(DISCARDED);
                displayEntity = null;
            }
            return displayEntity;
    }

    public boolean hasEntityTag() {
        return entityTag != null && !entityTag.isEmpty();
    }

    public void setDisplayEntityTag(CompoundTag nbtData) {
        entityTag = nbtData;
        if (!hasEntityTag() && level != null && level.isClientSide()) {
            displayEntity.remove(DISCARDED);
            displayEntity = null;
        }
        if (level != null) {
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
