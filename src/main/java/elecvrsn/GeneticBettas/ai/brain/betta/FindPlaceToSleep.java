package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import mokiyoki.enhancedanimals.init.ModMemoryModuleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.BigDripleafBlock;

public class FindPlaceToSleep extends Behavior<EnhancedBetta> {
    public FindPlaceToSleep() {
        //Condition, Min Duration, Max Duration
        super(ImmutableMap.of(
                AddonMemoryModuleTypes.SLEEPING.get(), MemoryStatus.VALUE_PRESENT,
                AddonMemoryModuleTypes.PAUSE_BRAIN.get(), MemoryStatus.VALUE_ABSENT,
                AddonMemoryModuleTypes.FOUND_SLEEP_SPOT.get(), MemoryStatus.VALUE_ABSENT
        ), 1, 10);
    }

    protected boolean canStillUse(ServerLevel p_23586_, EnhancedBetta betta, long p_23588_) {
        return !betta.getBrain().hasMemoryValue(AddonMemoryModuleTypes.FOUND_SLEEP_SPOT.get());
    }
    public void tick(ServerLevel serverLevel, EnhancedBetta betta, long gameTime) {
        int horizontalRange = 5;
        int verticalRange = 5;

        if (betta.getLeashHolder() != null) {
            horizontalRange = 2;
            verticalRange = 2;
        }

        BlockPos baseBlockPos = new BlockPos(betta.blockPosition());
        BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos();

        for (int k = 0; k <= verticalRange; k = k > 0 ? -k : 1 - k) {
            for (int l = 0; l < horizontalRange; ++l) {
                for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                    for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                        mutableblockpos.set(baseBlockPos).move(i1, k - 1, j1);
                        // Is Dripleaf?
                        if (betta.level.getBlockState(mutableblockpos).getBlock() instanceof BigDripleafBlock) {
                            betta.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(mutableblockpos, 0.4F, 0));
                            betta.getBrain().setMemoryWithExpiry(AddonMemoryModuleTypes.FOUND_SLEEP_SPOT.get(), true, 500);
                            return;
                        }
                    }
                }
            }
        }
    }
}
