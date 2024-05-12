package elecvrsn.BettaGenetics.util.handlers;


import elecvrsn.BettaGenetics.model.ModelEnhancedBetta;
import elecvrsn.BettaGenetics.renderer.RenderEnhancedBetta;
import mokiyoki.enhancedanimals.model.*;
import elecvrsn.BettaGenetics.util.Reference;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static elecvrsn.BettaGenetics.init.ModEntities.ENHANCED_BETTA;

@Mod.EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventRegistry {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onEntityRenderersRegistry(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ENHANCED_BETTA.get(), RenderEnhancedBetta::new);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(RenderEnhancedBetta.BETTA_LAYER, ModelEnhancedBetta::createBodyLayer);
    }

}
