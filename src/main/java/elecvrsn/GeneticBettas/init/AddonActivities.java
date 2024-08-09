package elecvrsn.GeneticBettas.init;

import elecvrsn.GeneticBettas.util.AddonReference;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AddonActivities {

    public static final DeferredRegister<Activity> ACTIVITIES_DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.ACTIVITIES, AddonReference.MODID);

    public static void register(IEventBus modEventBus) {
        ACTIVITIES_DEFERRED_REGISTRY.register(modEventBus);
    }
}
