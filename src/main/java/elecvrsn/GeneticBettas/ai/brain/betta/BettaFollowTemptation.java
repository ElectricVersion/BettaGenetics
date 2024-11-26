package elecvrsn.GeneticBettas.ai.brain.betta;

import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.FollowTemptation;

import java.util.function.Function;

public class BettaFollowTemptation extends FollowTemptation {
    public BettaFollowTemptation(Function<LivingEntity, Float> p_147486_) {
        super(p_147486_);
    }
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EnhancedBetta betta) {
        return !betta.getBrain().hasMemoryValue(AddonMemoryModuleTypes.HAS_EGG.get());
    }
}
