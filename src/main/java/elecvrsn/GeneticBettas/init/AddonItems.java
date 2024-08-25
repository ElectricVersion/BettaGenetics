package elecvrsn.GeneticBettas.init;

import elecvrsn.GeneticBettas.items.EnhancedBettaBucket;
import elecvrsn.GeneticBettas.util.AddonReference;
import mokiyoki.enhancedanimals.EnhancedAnimals;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AddonItems {
    private static final DeferredRegister<Item> ITEMS_DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, AddonReference.MODID);
    public static final RegistryObject<ForgeSpawnEggItem> ENHANCED_BETTA_EGG = ITEMS_DEFERRED_REGISTRY.register("enhanced_betta_spawn_egg", () ->  new ForgeSpawnEggItem(AddonEntities.ENHANCED_BETTA, 0x1588a8, 0xbb005b, new Item.Properties().tab(EnhancedAnimals.GENETICS_ANIMALS_GROUP)));
    public static final RegistryObject<Item> FISH_FOOD = ITEMS_DEFERRED_REGISTRY.register("fish_food", () ->  new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> BETTA = ITEMS_DEFERRED_REGISTRY.register("betta", () ->  new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD)));
    public static final RegistryObject<Item> ENHANCED_BETTA_BUCKET = ITEMS_DEFERRED_REGISTRY.register("enhanced_betta_bucket", () -> new EnhancedBettaBucket(new Item.Properties().stacksTo(1), AddonEntities.ENHANCED_BETTA, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH));
    public static final RegistryObject<Item> BUBBLE_NEST_ITEM = ITEMS_DEFERRED_REGISTRY.register("bubble_nest", () -> new BlockItem(AddonBlocks.BUBBLE_NEST.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static void register(IEventBus modEventBus) {
        ITEMS_DEFERRED_REGISTRY.register(modEventBus);
    }
}
