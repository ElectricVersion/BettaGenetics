package elecvrsn.GeneticBettas.init;

import elecvrsn.GeneticBettas.util.AddonReference;
import elecvrsn.GeneticBettas.world.feature.AquaticPlantFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AddonFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES_DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, AddonReference.MODID);
    public static final RegistryObject<Feature<SimpleBlockConfiguration>> AQUATIC_PLANT = FEATURES_DEFERRED_REGISTRY.register("aquatic_plant", () -> new AquaticPlantFeature(SimpleBlockConfiguration.CODEC));

    public static void register(IEventBus modEventBus) {
        FEATURES_DEFERRED_REGISTRY.register(modEventBus);
    }

}
