package elecvrsn.GeneticBettas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

import static elecvrsn.GeneticBettas.init.AddonBlockTags.AQUATIC_PLANT_PLACEABLE;

public class LargeTankPlant extends DoublePlantBlock implements BonemealableBlock, SimpleWaterloggedBlock {
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final float AABB_OFFSET = 6.0F;
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public LargeTankPlant(BlockBehaviour.Properties p_154583_) {
        super(p_154583_);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    public VoxelShape getShape(BlockState p_154610_, BlockGetter p_154611_, BlockPos p_154612_, CollisionContext p_154613_) {
        return SHAPE;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter p_154637_, BlockPos p_154638_) {
        return p_154637_.getFluidState(p_154638_.above()).isSourceOfType(Fluids.WATER) && isGroundPlaceable(state);
    }

    private boolean isGroundPlaceable(BlockState state) {
        return state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(AQUATIC_PLANT_PLACEABLE);
    }


    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_154592_) {
        BlockState blockstate = super.getStateForPlacement(p_154592_);
        return blockstate != null ? copyWaterloggedFrom(p_154592_.getLevel(), p_154592_.getClickedPos(), blockstate) : null;
    }

    public void setPlacedBy(Level p_154599_, BlockPos p_154600_, BlockState p_154601_, LivingEntity p_154602_, ItemStack p_154603_) {
        if (!p_154599_.isClientSide()) {
            BlockPos blockpos = p_154600_.above();
            BlockState blockstate = DoublePlantBlock.copyWaterloggedFrom(p_154599_, blockpos, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER));
            p_154599_.setBlock(blockpos, blockstate, 3);
        }

    }

    public FluidState getFluidState(BlockState p_154634_) {
        return p_154634_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_154634_);
    }

    public boolean canSurvive(BlockState p_154615_, LevelReader p_154616_, BlockPos p_154617_) {
        if (p_154615_.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return super.canSurvive(p_154615_, p_154616_, p_154617_);
        } else {
            BlockPos blockpos = p_154617_.below();
            BlockState blockstate = p_154616_.getBlockState(blockpos);
            return this.mayPlaceOn(blockstate, p_154616_, blockpos);
        }
    }

    public BlockState updateShape(BlockState p_154625_, Direction p_154626_, BlockState p_154627_, LevelAccessor p_154628_, BlockPos p_154629_, BlockPos p_154630_) {
        if (p_154625_.getValue(WATERLOGGED)) {
            p_154628_.scheduleTick(p_154629_, Fluids.WATER, Fluids.WATER.getTickDelay(p_154628_));
        }

        return super.updateShape(p_154625_, p_154626_, p_154627_, p_154628_, p_154629_, p_154630_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_154632_) {
        p_154632_.add(HALF, WATERLOGGED);
    }

    public boolean isValidBonemealTarget(BlockGetter p_154594_, BlockPos p_154595_, BlockState p_154596_, boolean p_154597_) {
        return true;
    }

    public boolean isBonemealSuccess(Level p_154605_, Random p_154606_, BlockPos p_154607_, BlockState p_154608_) {
        return true;
    }

    public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
        popResource(level, pos, new ItemStack(this));
    }
    @Override
    public float getMaxHorizontalOffset() {
        return 0.05F;
    }
    @Override
    public float getMaxVerticalOffset() {
        return 0.05F;
    }

}