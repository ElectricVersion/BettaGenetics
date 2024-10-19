package elecvrsn.GeneticBettas.util.handlers;


import elecvrsn.GeneticBettas.init.AddonBlocks;
import elecvrsn.GeneticBettas.init.AddonItems;
import elecvrsn.GeneticBettas.model.ModelEnhancedBetta;
import elecvrsn.GeneticBettas.model.ModelEnhancedBettaEgg;
import elecvrsn.GeneticBettas.renderer.RenderDisplayTank;
import elecvrsn.GeneticBettas.renderer.RenderEnhancedBetta;
import elecvrsn.GeneticBettas.renderer.RenderEnhancedBettaEgg;
import elecvrsn.GeneticBettas.util.AddonReference;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
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
        event.registerBlockEntityRenderer(AddonBlocks.DISPLAY_TANK_BLOCK_ENTITY.get(), RenderDisplayTank::new);

    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(RenderEnhancedBetta.BETTA_LAYER, ModelEnhancedBetta::createBodyLayer);
        event.registerLayerDefinition(RenderEnhancedBettaEgg.BETTA_EGG_LAYER, ModelEnhancedBettaEgg::createBodyLayer);
    }

    @SubscribeEvent
    public static void blockColorSetup(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((p_92621_, p_92622_, p_92623_, p_92624_) -> {
            return p_92622_ != null && p_92623_ != null ? lightenColor(BiomeColors.getAverageWaterColor(p_92622_, p_92623_)) : -1;
        }, AddonBlocks.BUBBLE_NEST.get());
        event.getBlockColors().register((p_92621_, p_92622_, p_92623_, p_92624_) -> {
            return p_92622_ != null && p_92623_ != null ? BiomeColors.getAverageWaterColor(p_92622_, p_92623_) : -1;
        }, AddonBlocks.DISPLAY_TANK.get());
    }
    @SubscribeEvent
    public static void itemColorSetup(ColorHandlerEvent.Item event) {
        event.getItemColors().register((p_92621_, p_92622_) -> {
            return 4159204;
        }, AddonItems.BUBBLE_NEST_ITEM.get());
        event.getItemColors().register((p_92621_, p_92622_) -> {
            return 4159204;
        }, AddonItems.DISPLAY_TANK_ITEM.get());
    }

    private static int lightenColor(int colorIn) {
        //TODO: remove this and figure out a better way to tint them while keeping the white "shine" pixels
        //Because the water tint is too dark to look good with the bubble nest texture.
        int r = (3*((colorIn >> 16) & 255) + 255)/4;
        int g = (3*((colorIn >> 8) & 255) + 255)/4;
        int b = (3*((colorIn) & 255) + 255)/4;
        return r << 16 | g << 8 | b;
    }

}
