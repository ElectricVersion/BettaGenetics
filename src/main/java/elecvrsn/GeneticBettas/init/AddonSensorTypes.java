package elecvrsn.GeneticBettas.init;

import elecvrsn.GeneticBettas.ai.brain.sensing.BettaAttackablesSensor;
import mokiyoki.enhancedanimals.ai.brain.sensor.EnhancedTemptingSensor;
import mokiyoki.enhancedanimals.init.FoodSerialiser;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Predicate;

import static elecvrsn.GeneticBettas.util.AddonReference.MODID;


public class AddonSensorTypes {

    private static final Predicate<ItemStack> BETTA_FOOD = itemStack -> FoodSerialiser.getAnimalFoodMap("betta").isFoodItem(itemStack.getItem());
    public static final DeferredRegister<SensorType<?>> ADDON_SENSOR_TYPES_DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, MODID);
    public static final RegistryObject<SensorType<EnhancedTemptingSensor>> BETTA_FOOD_TEMPTATIONS = ADDON_SENSOR_TYPES_DEFERRED_REGISTRY.register("betta_food_temptations",
            () -> new SensorType<>(() -> new EnhancedTemptingSensor(BETTA_FOOD)));

    public static final RegistryObject<SensorType<BettaAttackablesSensor>> BETTA_ATTACKABLES = ADDON_SENSOR_TYPES_DEFERRED_REGISTRY.register("betta_attackables", () -> new SensorType<>(BettaAttackablesSensor::new));

    public static void register(IEventBus modEventBus) {
        ADDON_SENSOR_TYPES_DEFERRED_REGISTRY.register(modEventBus);
    }

}
