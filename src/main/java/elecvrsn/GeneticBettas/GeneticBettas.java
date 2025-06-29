package elecvrsn.GeneticBettas;

import elecvrsn.GeneticBettas.config.BettasCommonConfig;
import elecvrsn.GeneticBettas.init.*;
import elecvrsn.GeneticBettas.util.handlers.AnimalReplacementHandler;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Predicate;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("geneticbettas")
public class GeneticBettas {
    public static GeneticBettas instance;

    public GeneticBettas() {
        instance = this;
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BettasCommonConfig.getConfigSpecForLoader(), BettasCommonConfig.getFileNameForLoader());

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new AnimalReplacementHandler());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        AddonEntities.register(modEventBus);
        AddonFeatures.register(modEventBus);
        AddonBlocks.register(modEventBus);
        AddonItems.register(modEventBus);
        AddonSensorTypes.register(modEventBus);
        AddonMemoryModuleTypes.register(modEventBus);
        AddonActivities.register(modEventBus);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(AddonBlocks.BUBBLE_NEST.get(), RenderType.cutout());
        //Tanks
        ItemBlockRenderTypes.setRenderLayer(AddonBlocks.DISPLAY_TANK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(AddonBlocks.FILLED_DISPLAY_TANK.get(), RenderType.translucent());
        //Glass
//        ItemBlockRenderTypes.setRenderLayer(AddonBlocks.TANK_GLASS.get(), RenderType.cutout());
        //Plants
        ItemBlockRenderTypes.setRenderLayer(AddonBlocks.E_CORDIFOLIUS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(AddonBlocks.TALL_E_CORDIFOLIUS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(AddonBlocks.A_BARTERI.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(AddonBlocks.VARIEGATED_A_BARTERI.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(AddonBlocks.V_AMERICANA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(AddonBlocks.DUCKWEED.get(), RenderType.cutout());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Compostable Bettas
        ComposterBlock.COMPOSTABLES.put(AddonItems.BETTA.get(), 0.85F);
        // Allow all plants to be compostable
        ComposterBlock.COMPOSTABLES.put(AddonItems.E_CORDIFOLIUS_ITEM.get(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(AddonItems.TALL_E_CORDIFOLIUS_ITEM.get(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(AddonItems.A_BARTERI_ITEM.get(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(AddonItems.VARIEGATED_A_BARTERI_ITEM.get(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(AddonItems.V_AMERICANA_ITEM.get(), 0.5F);
        // Since duckweed spreads, make it compost for less
        ComposterBlock.COMPOSTABLES.put(AddonItems.DUCKWEED_ITEM.get(), 0.3F);
    }
}
