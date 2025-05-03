package elecvrsn.GeneticBettas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_5;

public class DuckweedPlant extends BushBlock implements BonemealableBlock {
    protected static final VoxelShape AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.5D, 16.0D);
    public static final IntegerProperty AGE = AGE_5;

    public DuckweedPlant(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return AABB;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        FluidState fluidstate = getter.getFluidState(pos);
        FluidState fluidStateAbove = getter.getFluidState(pos.above());
        return (fluidstate.getType() == Fluids.WATER || state.getMaterial() == Material.ICE) && fluidStateAbove.getType() == Fluids.EMPTY;
    }

    public boolean isValidBonemealTarget(BlockGetter getter, BlockPos pos, BlockState state, boolean isClientSide) {
        return true;
    }

    public boolean isBonemealSuccess(Level level, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
        popResource(level, pos, new ItemStack(this));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AGE);
    }

    public boolean isRandomlyTicking(BlockState p_48930_) {
        return p_48930_.getValue(AGE) < 5;
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
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
                level.setBlock(neighboringBlock, this.defaultBlockState(), 3);
            }
        }
    }
}
