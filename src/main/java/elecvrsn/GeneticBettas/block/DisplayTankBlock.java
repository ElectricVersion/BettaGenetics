package elecvrsn.GeneticBettas.block;

import elecvrsn.GeneticBettas.init.AddonBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

public class DisplayTankBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public DisplayTankBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        return this.defaultBlockState().setValue(FACING, p_48689_.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_48725_) {
        p_48725_.add(FACING);
    }
    public BlockState rotate(BlockState p_48722_, Rotation p_48723_) {
        return p_48722_.setValue(FACING, p_48723_.rotate(p_48722_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_48719_, Mirror p_48720_) {
        return p_48719_.rotate(p_48720_.getRotation(p_48719_.getValue(FACING)));
    }

    public static Direction getFacing(BlockState state) {
        return state.getValue(FACING);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == Items.WATER_BUCKET) {
            fill(state, level, pos);
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static BlockState fill(BlockState state, Level level, BlockPos pos) {
        if (state.is(AddonBlocks.DISPLAY_TANK.get())) {
            level.setBlock(pos, AddonBlocks.FILLED_DISPLAY_TANK.get().withPropertiesOf(state), 2);
        }
        return level.getBlockState(pos);
    }
}
