package elecvrsn.GeneticBettas.block;

import elecvrsn.GeneticBettas.init.AddonBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class DisplayTankBlock extends Block {
    public DisplayTankBlock(Properties p_49795_) {
        super(p_49795_);
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
            level.setBlock(pos, AddonBlocks.FILLED_DISPLAY_TANK.get().defaultBlockState(), 2);
        }
        return level.getBlockState(pos);
    }
}
