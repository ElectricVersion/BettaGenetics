package elecvrsn.GeneticBettas.block;

import elecvrsn.GeneticBettas.config.BettasCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_5;

public class DuckweedPlant extends BushBlock implements BonemealableBlock {
    protected static final VoxelShape AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.5D, 16.0D);
    public static final IntegerProperty AGE = AGE_5;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    public DuckweedPlant(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(PERSISTENT, false));
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return AABB;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        FluidState fluidstate = getter.getFluidState(pos);
        FluidState fluidStateAbove = getter.getFluidState(pos.above());
        return (fluidstate.getType() == Fluids.WATER || state.is(Blocks.ICE)) && fluidStateAbove.getType() == Fluids.EMPTY;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos pos, BlockState state, boolean isClientSide) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource randomSource, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        if (age < 5) {
            level.setBlock(pos, state.setValue(AGE, age+1), 3);
        }
        else {
            int direction = randomSource.nextInt(4);
            BlockPos neighboringBlock = switch (direction) {
                case 0 -> pos.north();
                case 1 -> pos.east();
                case 2 -> pos.south();
                default -> pos.west();
            };
            if (level.getBlockState(neighboringBlock).isAir()
                    && level.getFluidState(neighboringBlock.below()).getType()==Fluids.WATER
                    && level.getFluidState(neighboringBlock.above()).getType()==Fluids.EMPTY) {
                level.setBlock(neighboringBlock, this.defaultBlockState().setValue(PERSISTENT, true), 3);
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AGE).add(PERSISTENT);
    }

    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(PERSISTENT) && state.getValue(AGE) < 5;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (age == 0) {
            level.setBlock(pos, state.setValue(AGE, 1), 3);
        }
        else if (age < 5) {
            level.setBlock(pos, state.setValue(AGE, age+1), 3);
            int direction = random.nextInt(4);
            BlockPos neighboringBlock = switch (direction) {
                case 0 -> pos.north();
                case 1 -> pos.east();
                case 2 -> pos.south();
                default -> pos.west();
            };
            if (level.getBlockState(neighboringBlock).isAir()
                    && level.getFluidState(neighboringBlock.below()).getType()==Fluids.WATER
                    && level.getFluidState(neighboringBlock.above()).getType()==Fluids.EMPTY) {
                level.setBlock(neighboringBlock, this.defaultBlockState().setValue(PERSISTENT, true), 3);
            }
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(PERSISTENT, true);
    }
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == Items.SHEARS && state.getValue(PERSISTENT)) {
            level.setBlockAndUpdate(pos, state.setValue(PERSISTENT, false));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, result);
    }

    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        BlockPos blockpos = pos.below();
        if (state.getBlock() == this)
            return reader.getFluidState(blockpos).getType() == Fluids.WATER;
        return this.mayPlaceOn(reader.getBlockState(blockpos), reader, blockpos);
    }

}
