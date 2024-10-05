package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.entity.EnhancedBettaEgg;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import mokiyoki.enhancedanimals.init.ModMemoryModuleTypes;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.ThreadLocalRandom;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA_EGG;


public class LayEgg extends Behavior<EnhancedBetta> {
    private int eggLayingTimer = -1;
    private long startTime = -1;
    private BlockPos existingNest;

    public LayEgg() {
        //Condition, Min Duration, Max Duration
        super(ImmutableMap.of(
                ModMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                ModMemoryModuleTypes.FOCUS_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                ModMemoryModuleTypes.HAS_EGG.get(), MemoryStatus.VALUE_PRESENT
        ), 5, 10000);
    }

    private boolean isBlockBubbleNest(BlockState blockState) {
        Block block = blockState.getBlock();
        return AddonBlocks.BUBBLE_NEST.get().equals(block);
    }

    public void tick(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        if (existingNest == null) {
            if ((existingNest = betta.findExistingNest()) != null) {
                WalkTarget walkTarget = new WalkTarget(existingNest, 0.45F, 0);
                betta.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(existingNest));
                betta.getBrain().setMemory(MemoryModuleType.WALK_TARGET, walkTarget);
            }
            else if (gameTime > startTime+100){
                //If there's no existing nest nearby then just end the pregnancy I guess until I think of a better solution
                betta.setHasEgg(false);
                betta.getBrain().eraseMemory(ModMemoryModuleTypes.HAS_EGG.get());
                return;
            }
        }
        if (existingNest != null) {
            if (existingNest.closerToCenterThan(betta.position(), 0.5F)) {
//                betta.setPos(new Vec3(existingNest.getX()+0.5, existingNest.getY()+0.5, existingNest.getZ()+0.5));
            }
            else if (existingNest.closerToCenterThan(betta.position(), 1.5F)) {
                betta.setPos(EnhancedBetta.moveCloser(betta.position(), new Vec3(existingNest.getX()+0.5, existingNest.getY()+0.4, existingNest.getZ()+0.5), 0.01));
            }
        }
        BlockPos blockPos = betta.blockPosition();
        BlockState blockState = serverLevel.getBlockState(blockPos);
        if (betta.isInWater() && this.isBlockBubbleNest(blockState)) {
            if (eggLayingTimer == -1) {
                eggLayingTimer = 0;
            } else if (eggLayingTimer >= 200) {
                if (eggLayingTimer == 200) {
                    eggLayingTimer = 200 + (betta.getRandom().nextInt(4) * 40);
                }

                if (eggLayingTimer%40 == 0) {
                    Level level = betta.getLevel();
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
                }

                if (eggLayingTimer > 320) {
                    betta.setHasEgg(false);
                    eggLayingTimer = -1;
                    betta.setMateName("???"); //Reset mate name
                    betta.getBrain().eraseMemory(ModMemoryModuleTypes.HAS_EGG.get());
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
            betta.getBrain().eraseMemory(ModMemoryModuleTypes.HAS_EGG.get());
        }
    }

    @Override
    protected void stop(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        existingNest = null;
    }

    @Override
    protected void start(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        existingNest = null;
        startTime = gameTime;
    }
    protected boolean canStillUse(ServerLevel p_23586_, EnhancedBetta betta, long p_23588_) {
        return betta.getBrain().hasMemoryValue(ModMemoryModuleTypes.HAS_EGG.get());
    }
}
