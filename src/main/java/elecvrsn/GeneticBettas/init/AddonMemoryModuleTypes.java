package elecvrsn.GeneticBettas.init;

import com.mojang.serialization.Codec;
import static elecvrsn.GeneticBettas.util.AddonReference.MODID;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;

public class AddonMemoryModuleTypes {

    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPE_DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, MODID);
    public static final RegistryObject<MemoryModuleType<Boolean>> FOUND_SLEEP_SPOT = MEMORY_MODULE_TYPE_DEFERRED_REGISTRY.register("found_sleep_spot", () -> new MemoryModuleType<Boolean>(Optional.of(Codec.BOOL)));
    public static final RegistryObject<MemoryModuleType<Boolean>> MAKING_NEST = MEMORY_MODULE_TYPE_DEFERRED_REGISTRY.register("making_nest", () -> new MemoryModuleType<Boolean>(Optional.of(Codec.BOOL)));
    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> TRUSTED_BETTAS = MEMORY_MODULE_TYPE_DEFERRED_REGISTRY.register("trusted_bettas", () -> new MemoryModuleType<List<LivingEntity>>(Optional.empty()));
    public static void register(IEventBus modEventBus) {
        MEMORY_MODULE_TYPE_DEFERRED_REGISTRY.register(modEventBus);
    }
}
