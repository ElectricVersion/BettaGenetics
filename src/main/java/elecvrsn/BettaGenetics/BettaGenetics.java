package elecvrsn.BettaGenetics;

import com.mojang.logging.LogUtils;

import elecvrsn.BettaGenetics.init.*;
import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("bettagenetics")
public class BettaGenetics
{
    public static BettaGenetics instance;
    public BettaGenetics() {
        instance = this;

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(instance);

//        ModItems.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModEntities.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

}
