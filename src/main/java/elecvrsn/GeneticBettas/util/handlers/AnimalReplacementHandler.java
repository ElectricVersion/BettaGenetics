package elecvrsn.GeneticBettas.util.handlers;

import elecvrsn.GeneticBettas.config.BettasCommonConfig;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;

public class AnimalReplacementHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getCommandSenderWorld().isClientSide()) {
            if (entity instanceof AbstractFish) {
                if (BettasCommonConfig.COMMON.replaceOtherBettas.get()) {
                    if (entity.getClass().getName().contains("Betta")) {
                        EnhancedBetta enhancedBetta = ENHANCED_BETTA.get().spawn((ServerLevel) entity.getCommandSenderWorld(), null, null, null, entity.blockPosition(), MobSpawnType.NATURAL, false, false);
                        if (enhancedBetta != null) {
                            if (entity.hasCustomName()) {
                                enhancedBetta.setCustomName(entity.getCustomName());
                            }
                        }
                        entity.remove(Entity.RemovalReason.DISCARDED);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
