package elecvrsn.GeneticBettas.block;

import elecvrsn.GeneticBettas.block.entity.DisplayTankBlockEntity;
import net.minecraft.core.BlockPos;
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

}
