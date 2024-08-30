package elecvrsn.GeneticBettas.util.handlers;


import elecvrsn.GeneticBettas.model.ModelEnhancedBetta;
import elecvrsn.GeneticBettas.model.ModelEnhancedBettaEgg;
import elecvrsn.GeneticBettas.renderer.RenderEnhancedBetta;
import elecvrsn.GeneticBettas.renderer.RenderEnhancedBettaEgg;
import elecvrsn.GeneticBettas.util.AddonReference;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;
import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA_EGG;

@Mod.EventBusSubscriber(modid = AddonReference.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AddonClientEventRegistry {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onEntityRenderersRegistry(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ENHANCED_BETTA.get(), RenderEnhancedBetta::new);
        event.registerEntityRenderer(ENHANCED_BETTA_EGG.get(), RenderEnhancedBettaEgg::new);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(RenderEnhancedBetta.BETTA_LAYER, ModelEnhancedBetta::createBodyLayer);
        event.registerLayerDefinition(RenderEnhancedBettaEgg.BETTA_EGG_LAYER, ModelEnhancedBettaEgg::createBodyLayer);
    }

}
