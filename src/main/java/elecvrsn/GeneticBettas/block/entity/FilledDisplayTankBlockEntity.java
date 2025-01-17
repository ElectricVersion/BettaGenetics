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

public class FilledDisplayTankBlockEntity extends BlockEntity {
    public EnhancedBetta displayEntity;
    private CompoundTag entityTag;
    public FilledDisplayTankBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AddonBlocks.FILLED_DISPLAY_TANK_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public EnhancedBetta getOrCreateDisplayEntity(Level level) {
        if (displayEntity == null && entityTag != null && !entityTag.isEmpty()) {
            displayEntity = EnhancedBettaBucket.spawnBettaFromTag(level, entityTag, getBlockPos());
            displayEntity.setInTank(true);
        }
        return displayEntity;
    }

    public boolean hasEntityTag() {
        return entityTag != null;
    }

    public void setDisplayEntityTag(CompoundTag nbtData) {
        entityTag = nbtData;
        if (entityTag == null) displayEntity = null;
    }

    public CompoundTag getDisplayEntityTag() {
        return entityTag;
    }
//
    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);

        if (entityTag != null) {
            compound.put("EntityData", entityTag);
        }
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
