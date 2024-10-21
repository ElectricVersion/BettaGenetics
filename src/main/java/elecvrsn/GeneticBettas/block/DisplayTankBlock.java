package elecvrsn.GeneticBettas.block;

import elecvrsn.GeneticBettas.block.entity.DisplayTankBlockEntity;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DisplayTankBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
//    public static final BooleanProperty FULL = BooleanProperty.create("full");;

    public DisplayTankBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DisplayTankBlockEntity(blockPos, blockState);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
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

    public static void fillWithEntityTag(Level level, BlockPos pos, BlockState state, ItemStack stack) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof DisplayTankBlockEntity && state.is(AddonBlocks.DISPLAY_TANK.get())) {
            ((DisplayTankBlockEntity)blockEntity).setDisplayEntityTag(stack.getOrCreateTag().copy());
            blockEntity.setChanged();
        }
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == Items.WATER_BUCKET) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DisplayTankBlockEntity && ((DisplayTankBlockEntity)blockEntity).getOrCreateDisplayEntity(level) != null)  {
                Bucketable.bucketMobPickup(player, hand, ((DisplayTankBlockEntity) blockEntity).getOrCreateDisplayEntity(level));
                ((DisplayTankBlockEntity) blockEntity).setDisplayEntityTag(null);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

}
