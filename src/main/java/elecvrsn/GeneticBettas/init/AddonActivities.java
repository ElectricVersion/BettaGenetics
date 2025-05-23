package elecvrsn.GeneticBettas.init;

import elecvrsn.GeneticBettas.util.AddonReference;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AddonActivities {

    public static final DeferredRegister<Activity> ACTIVITIES_DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.ACTIVITIES, AddonReference.MODID);
    public static final RegistryObject<Activity> MAKE_BUBBLE_NEST = ACTIVITIES_DEFERRED_REGISTRY.register("make_bubble_nest", () -> new Activity("make_bubble_nest"));
    public static final RegistryObject<Activity> LAY_EGG = ACTIVITIES_DEFERRED_REGISTRY.register("lay_egg", () -> new Activity("lay_egg"));
    public static final RegistryObject<Activity> NIP = ACTIVITIES_DEFERRED_REGISTRY.register("nip", () -> new Activity("nip"));
    public static final RegistryObject<Activity> PAUSE_BRAIN = ACTIVITIES_DEFERRED_REGISTRY.register("addon_pause_brain", () -> new Activity("addon_pause_brain"));

    public static void register(IEventBus modEventBus) {
        ACTIVITIES_DEFERRED_REGISTRY.register(modEventBus);
    }
}
