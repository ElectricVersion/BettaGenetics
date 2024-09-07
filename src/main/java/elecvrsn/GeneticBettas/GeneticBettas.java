package elecvrsn.GeneticBettas;

import elecvrsn.GeneticBettas.init.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("geneticbettas")
public class GeneticBettas
{
    public static GeneticBettas instance;
    public GeneticBettas() {
        instance = this;

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
    }

}
