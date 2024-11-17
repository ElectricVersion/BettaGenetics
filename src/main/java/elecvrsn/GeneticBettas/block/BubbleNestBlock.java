package elecvrsn.GeneticBettas.block;

import elecvrsn.GeneticBettas.block.entity.BubbleNestBlockEntity;
import elecvrsn.GeneticBettas.config.BettasCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

public class BubbleNestBlock extends BaseEntityBlock implements LiquidBlockContainer {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 14.D, 0.0D, 16.0D, 15.0D, 16.0D);

    public BubbleNestBlock(BlockBehaviour.Properties p_154496_) {
        super(p_154496_);
    }
    public VoxelShape getShape(BlockState p_154525_, BlockGetter p_154526_, BlockPos p_154527_, CollisionContext p_154528_) {
        return SHAPE;
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BubbleNestBlockEntity(blockPos, blockState);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8 && context.getLevel().isEmptyBlock(context.getClickedPos().above()) ? super.getStateForPlacement(context) : null;
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        BlockState blockstate = super.updateShape(blockState, direction, blockState1, levelAccessor, blockPos, blockPos1);
        if (!blockstate.isAir()) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }

        return blockstate;
    }

    public FluidState getFluidState(BlockState blockState) {
        return Fluids.WATER.getSource(false);
    }


    public boolean canPlaceLiquid(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return false;
    }

    public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        return false;
    }

    public boolean isRandomlyTicking(BlockState blockState) {
        return true;
    }

    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if (serverLevel.getBlockEntity(blockPos) instanceof BubbleNestBlockEntity &&
                ((BubbleNestBlockEntity) serverLevel.getBlockEntity(blockPos)).getPlacementTime() != 0 &&
                serverLevel.getGameTime() > ((BubbleNestBlockEntity) serverLevel.getBlockEntity(blockPos)).getPlacementTime() + BettasCommonConfig.COMMON.bettaHatchTime.get()) {
            serverLevel.removeBlock(blockPos, false);
        }

    }
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

}