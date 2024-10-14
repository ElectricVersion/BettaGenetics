package elecvrsn.GeneticBettas.block.entity;

import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.entity.genetics.BettaGeneticsInitialiser;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import elecvrsn.GeneticBettas.init.AddonEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Function;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;
import static net.minecraft.world.entity.EntityType.PIG;
import static net.minecraft.world.entity.EntityType.SALMON;

public class DisplayTankBlockEntity extends BlockEntity {
    private EnhancedBetta enhancedBetta;
    public DisplayTankBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AddonBlocks.DISPLAY_TANK_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Nullable
    public EnhancedBetta getOrCreateDisplayEntity(Level level, BlockPos blockPos) {
        if (enhancedBetta == null && level != null) {
            enhancedBetta = AddonEntities.ENHANCED_BETTA.get().create(level);
            if (enhancedBetta != null && blockPos != null) {
                enhancedBetta.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            }
        }
        return enhancedBetta;
    }
}
