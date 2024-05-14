package elecvrsn.BettaGenetics;

import elecvrsn.BettaGenetics.init.*;
import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("bettagenetics")
public class BettaGenetics
{
    public static BettaGenetics instance;
    public BettaGenetics() {
        instance = this;

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(instance);

        AddonItems.register(FMLJavaModLoadingContext.get().getModEventBus());
        AddonEntities.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

}
