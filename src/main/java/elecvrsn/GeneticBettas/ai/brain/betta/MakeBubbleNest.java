package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.block.entity.BubbleNestBlockEntity;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

import static net.minecraft.sounds.SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT;
import static net.minecraft.world.level.block.Blocks.WATER;


public class MakeBubbleNest extends Behavior<EnhancedBetta> {

    public MakeBubbleNest() {
        //Condition, Min Duration, Max Duration
        super(ImmutableMap.of(
                AddonMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                AddonMemoryModuleTypes.FOCUS_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                AddonMemoryModuleTypes.MAKING_NEST.get(), MemoryStatus.VALUE_PRESENT
        ), 5, 100);
    }

    public void tick(ServerLevel serverLevel, EnhancedBetta enhancedBetta, long gameTime) {
        BlockPos nestPos = enhancedBetta.getNestPos();
        if (nestPos != null && nestPos.closerToCenterThan(enhancedBetta.position(), 1.5F) && serverLevel.getBlockState(nestPos).is(WATER)) {
            enhancedBetta.getLevel().playSound((Player)null, nestPos, BUBBLE_COLUMN_UPWARDS_AMBIENT, enhancedBetta.getSoundSource(), 1.0F, 1.0F);
            serverLevel.setBlock(nestPos, AddonBlocks.BUBBLE_NEST.get().defaultBlockState(), 2);
            if (serverLevel.getBlockEntity(nestPos) != null && serverLevel.getBlockEntity(nestPos) instanceof BubbleNestBlockEntity) {
                ((BubbleNestBlockEntity) serverLevel.getBlockEntity(nestPos)).setPlacementTime(gameTime);
            }
            //Broadcast the nest position to the mate if she hasn't already reached a nest
            UUID mateUUID = enhancedBetta.getBrain().getMemory(AddonMemoryModuleTypes.LAST_MATE.get()).orElse(null);
            Entity mate = null;
            if (mateUUID != null && (mate = serverLevel.getEntity(mateUUID)) instanceof EnhancedBetta) {
                if (((EnhancedBetta)mate).getNestPos() == null || nestPos.distToCenterSqr(((EnhancedBetta)mate).position()) > 1.5F ) {
                    ((EnhancedBetta)mate).setNestPos(nestPos);
                }
            }
            enhancedBetta.setNestPos(null);
            enhancedBetta.getBrain().eraseMemory(AddonMemoryModuleTypes.MAKING_NEST.get());
        }
    }

    @Override
    protected void stop(ServerLevel level, EnhancedBetta betta, long gameTime) {
        betta.setNestPos(null);
    }

    protected boolean canStillUse(ServerLevel p_23586_, EnhancedBetta enhancedBetta, long p_23588_) {
        return enhancedBetta.getBrain().hasMemoryValue(AddonMemoryModuleTypes.MAKING_NEST.get());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EnhancedBetta betta) {
        return betta.getNestPos() != null;
    }
}
