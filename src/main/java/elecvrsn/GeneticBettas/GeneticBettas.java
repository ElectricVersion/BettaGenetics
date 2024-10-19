package elecvrsn.GeneticBettas;

import elecvrsn.GeneticBettas.config.BettasCommonConfig;
import elecvrsn.GeneticBettas.init.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Predicate;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("geneticbettas")
public class GeneticBettas
{
    public static GeneticBettas instance;
    public GeneticBettas() {
        instance = this;
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BettasCommonConfig.getConfigSpecForLoader(), BettasCommonConfig.getFileNameForLoader());

        MinecraftForge.EVENT_BUS.register(this);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        AddonEntities.register(modEventBus);
        AddonBlocks.register(modEventBus);
        AddonItems.register(modEventBus);
        AddonSensorTypes.register(modEventBus);
        AddonMemoryModuleTypes.register(modEventBus);
        AddonActivities.register(modEventBus);
        modEventBus.addListener(this::clientSetup);
    }
    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(AddonBlocks.BUBBLE_NEST.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(AddonBlocks.DISPLAY_TANK.get(), Predicate.<RenderType>isEqual(RenderType.cutout()).or(Predicate.isEqual(RenderType.translucent())));
    }

//    @SubscribeEvent
//    public void registerCaps(RegisterCapabilitiesEvent event) {
//        event.register(IExampleCapability.class);
//    }
}
