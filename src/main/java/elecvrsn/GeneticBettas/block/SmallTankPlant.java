package elecvrsn.GeneticBettas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

public class SmallTankPlant extends BushBlock implements BonemealableBlock, SimpleWaterloggedBlock {
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final float AABB_OFFSET = 6.0F;
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public SmallTankPlant(BlockBehaviour.Properties p_154583_) {
        super(p_154583_);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return getter.getFluidState(pos.above()).isSourceOfType(Fluids.WATER) && isGroundPlaceable(state);
    }

    private boolean isGroundPlaceable(BlockState state) {
        return state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(Blocks.GRAVEL) || state.is(Blocks.FARMLAND);
    }

    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = reader.getBlockState(blockpos);
        return this.mayPlaceOn(blockstate, reader, blockpos);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState blockState, LevelAccessor accessor, BlockPos pos1, BlockPos pos2) {
        if (state.getValue(WATERLOGGED)) {
            accessor.scheduleTick(pos1, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
        }
        return super.updateShape(state, direction, blockState, accessor, pos1, pos2);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(WATERLOGGED);
    }

    public boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos pos, BlockState state, boolean b) {
        return true;
    }

    public boolean isBonemealSuccess(Level level, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
        popResource(level, pos, new ItemStack(this));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8 ? super.getStateForPlacement(context) : null;
    }

    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }
    @Override
    public float getMaxHorizontalOffset() {
        return 0.05F;
    }
    @Override
    public float getMaxVerticalOffset() {
        return 0.05F;
    }
    @Override
    public BlockBehaviour.OffsetType getOffsetType() {
        return BlockBehaviour.OffsetType.XZ;
    }

}