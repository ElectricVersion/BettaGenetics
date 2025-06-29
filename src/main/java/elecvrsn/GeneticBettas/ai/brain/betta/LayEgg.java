package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.block.entity.BubbleNestBlockEntity;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.entity.EnhancedBettaEgg;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import mokiyoki.enhancedanimals.util.Genes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA_EGG;


public class LayEgg extends Behavior<EnhancedBetta> {
    private int eggLayingTimer = -1;

    BlockPos nestPos = null;
    public LayEgg() {
        //Condition, Min Duration, Max Duration
        super(ImmutableMap.of(
                AddonMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                AddonMemoryModuleTypes.FOCUS_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                AddonMemoryModuleTypes.HAS_EGG.get(), MemoryStatus.VALUE_PRESENT
        ), 5, 10000);
    }

    public void tick(@NotNull ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        if (nestPos != betta.getNestPos() || nestPos == null) {
            nestPos = betta.getNestPos();
            if (nestPos == null) {
                // TODO: Make this not run every tick if it fails
                nestPos = betta.setNestPos(betta.findExistingNest());
            }
            if (nestPos != null) {
                WalkTarget walkTarget = new WalkTarget(nestPos, 0.45F, 0);
                betta.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(nestPos));
                betta.getBrain().setMemory(MemoryModuleType.WALK_TARGET, walkTarget);
            }
        }
        if (nestPos != null) {
            if (nestPos.closerToCenterThan(betta.position(), 0.5F)) {
//                betta.setPos(new Vec3(existingNest.getX()+0.5, existingNest.getY()+0.5, existingNest.getZ()+0.5));
            }
            else if (nestPos.closerToCenterThan(betta.position(), 1.5F)) {
                betta.setPos(EnhancedBetta.moveCloser(betta.position(), new Vec3(nestPos.getX()+0.5, nestPos.getY()+0.4, nestPos.getZ()+0.5), 0.01));
            }
        }
        BlockPos blockPos = betta.blockPosition();
        BlockState blockState = serverLevel.getBlockState(blockPos);
        if (betta.isInWater() && blockState.is(AddonBlocks.BUBBLE_NEST.get())) {
            if (!betta.getMateGenetics().isValid()) {
                betta.setHasEgg(false);
                return;
            }
            if (eggLayingTimer == -1) {
                eggLayingTimer = 0;
            } else if (eggLayingTimer >= 100) {
                if (eggLayingTimer == 100) {
                    eggLayingTimer = 100 + (betta.getRandom().nextInt(4) * 20);
                }

                if (eggLayingTimer%20 == 0) {
                    Level level = betta.level();
                    BlockPos pos = betta.blockPosition();
                    String mateName = betta.getMateName().isEmpty() ? "???" : betta.getMateName();
                    String name = betta.hasCustomName() ? betta.getName().getString() : "???";
                    level.playSound((Player)null, blockPos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
                    Genes eggGenes = new Genes(betta.getGenes()).makeChild(true, true, betta.getMateGenetics());
                    EnhancedBettaEgg egg = ENHANCED_BETTA_EGG.get().create(level);
                    egg.setParentNames(mateName, name);
                    egg.setGenes(eggGenes);
                    egg.moveTo(pos.getX()+0.125D+(ThreadLocalRandom.current().nextFloat()*0.75F), betta.getY(), pos.getZ()+0.125D+(ThreadLocalRandom.current().nextFloat()*0.75F), ThreadLocalRandom.current().nextInt(4)* (Mth.HALF_PI*0.5F), 0.0F);
                    level.addFreshEntity(egg);
                    // Refresh the placement time of the nest, so it doesn't break until all eggs hatch
                    BlockEntity blockEntity = level.getBlockEntity(blockPos);
                    if (blockEntity instanceof BubbleNestBlockEntity) {
                        ((BubbleNestBlockEntity)blockEntity).setPlacementTime(gameTime);
                    }
                }

                if (eggLayingTimer > 180) {
                    betta.setHasEgg(false);
                    eggLayingTimer = -1;
                    betta.setMateName("???"); //Reset mate name
                    betta.getBrain().eraseMemory(AddonMemoryModuleTypes.HAS_EGG.get());
                }
            }

            if (eggLayingTimer != -1) {
                eggLayingTimer++;
            }
        }
        if (eggLayingTimer == 0) {
            betta.setHasEgg(false);
            eggLayingTimer = -1;
            betta.setMateName("???"); //Reset mate name
            betta.getBrain().eraseMemory(AddonMemoryModuleTypes.HAS_EGG.get());
            betta.setNestPos(null);
        }
    }

    @Override
    protected void stop(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        betta.setHasEgg(false);
        betta.setNestPos(null);
    }

    @Override
    protected void start(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        nestPos = null;
    }

    protected boolean canStillUse(ServerLevel p_23586_, EnhancedBetta betta, long p_23588_) {
        return betta.getBrain().hasMemoryValue(AddonMemoryModuleTypes.HAS_EGG.get());
    }
}
