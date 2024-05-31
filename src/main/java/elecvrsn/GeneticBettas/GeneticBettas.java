package elecvrsn.GeneticBettas;

import elecvrsn.GeneticBettas.init.*;
import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("geneticbettas")
public class GeneticBettas
{
    public static GeneticBettas instance;
    public GeneticBettas() {
        instance = this;

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(instance);

        AddonItems.register(FMLJavaModLoadingContext.get().getModEventBus());
        AddonEntities.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

}
