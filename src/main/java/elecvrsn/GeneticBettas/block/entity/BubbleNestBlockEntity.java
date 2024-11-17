package elecvrsn.GeneticBettas.block.entity;

import elecvrsn.GeneticBettas.init.AddonBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BubbleNestBlockEntity extends BlockEntity {

    private long placementTime;

    public BubbleNestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AddonBlocks.BUBBLE_NEST_BLOCK_ENTITY.get(), blockPos, blockState);
        placementTime = 0;
    }

    public long getPlacementTime() {
        return placementTime;
    }

    public void setPlacementTime(long placementTime) {
        this.placementTime = placementTime;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putLong("placementTime", placementTime);
    }
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        placementTime = compound.getLong("placementTime");
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
