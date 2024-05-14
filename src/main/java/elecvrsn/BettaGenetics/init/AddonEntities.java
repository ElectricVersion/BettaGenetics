package elecvrsn.BettaGenetics.init;
import elecvrsn.BettaGenetics.entity.EnhancedBetta;
import elecvrsn.BettaGenetics.util.AddonReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AddonEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES_DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITIES, AddonReference.MODID);
    public static final RegistryObject<EntityType<EnhancedBetta>> ENHANCED_BETTA = ENTITIES_DEFERRED_REGISTRY.register("enhanced_betta", () -> EntityType.Builder.of(EnhancedBetta::new, MobCategory.CREATURE).sized(1.0F, 1.0F).build(AddonReference.MODID + ":enhanced_betta"));

    public static void register(IEventBus modEventBus) {
        ENTITIES_DEFERRED_REGISTRY.register(modEventBus);
    }
}
