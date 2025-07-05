package elecvrsn.GeneticBettas.util.handlers;

import elecvrsn.GeneticBettas.config.BettasCommonConfig;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.ThreadLocalRandom;

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
            } else if (entity instanceof WanderingTrader) {
                if (!entity.getTags().contains("bettaTrades")) {
                    entity.addTag("bettaTrades");
                    if (ThreadLocalRandom.current().nextInt(10) == 9) {
                        ((WanderingTrader) entity).getOffers().add(new MerchantOffer(new ItemStack(Items.EMERALD), new ItemStack(AddonItems.ENHANCED_BETTA_BUCKET.get()), 1, 2, 1F));
                    }
                }
            }
        }
    }

}