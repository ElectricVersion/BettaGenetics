package elecvrsn.GeneticBettas.block.entity;

import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import elecvrsn.GeneticBettas.init.AddonEntities;
import elecvrsn.GeneticBettas.items.EnhancedBettaBucket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Function;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;

public class DisplayTankBlockEntity extends BlockEntity {
    public EnhancedBetta displayEntity;
    private CompoundTag entityTag;
    public DisplayTankBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AddonBlocks.DISPLAY_TANK_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public Entity getOrCreateDisplayEntity(Level level) {
        if (displayEntity == null && entityTag != null && !entityTag.isEmpty()) {
            displayEntity = EnhancedBettaBucket.spawnBettaFromTag(level, entityTag, getBlockPos());
        }
        return displayEntity;
    }

    public void setDisplayEntityTag(CompoundTag nbtData) {
        entityTag = nbtData;
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
