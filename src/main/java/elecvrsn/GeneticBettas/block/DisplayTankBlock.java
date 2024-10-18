package elecvrsn.GeneticBettas.block;

import elecvrsn.GeneticBettas.block.entity.DisplayTankBlockEntity;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DisplayTankBlock extends BaseEntityBlock {
    public DisplayTankBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DisplayTankBlockEntity(blockPos, blockState);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }



    public static boolean fillWithEntityTag(Level level, BlockPos pos, BlockState state, ItemStack stack) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DisplayTankBlockEntity && state.is(AddonBlocks.DISPLAY_TANK.get())) {
            CompoundTag tag = stack.getOrCreateTag().copy();
            ((DisplayTankBlockEntity)blockEntity).setDisplayEntityTag(tag);
            return true;
        }
        if (blockEntity != null) blockEntity.setChanged();
        return false;
    }

    public static void setEntity(Level level, BlockPos pos, EnhancedBetta entity) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DisplayTankBlockEntity) ((DisplayTankBlockEntity) blockEntity).displayEntity = entity;
    }


}
