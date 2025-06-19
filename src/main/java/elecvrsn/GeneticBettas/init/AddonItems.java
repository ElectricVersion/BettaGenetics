package elecvrsn.GeneticBettas.init;

import elecvrsn.GeneticBettas.items.DuckweedPlantItem;
import elecvrsn.GeneticBettas.items.EnhancedBettaBucket;
import elecvrsn.GeneticBettas.items.EnhancedBettaEggBucket;
import elecvrsn.GeneticBettas.util.AddonReference;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
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
    public static final RegistryObject<Item> BETTA = ITEMS_DEFERRED_REGISTRY.register("betta", () ->  new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).build())));
    public static final RegistryObject<Item> ENHANCED_BETTA_BUCKET = ITEMS_DEFERRED_REGISTRY.register("enhanced_betta_bucket", () -> new EnhancedBettaBucket(new Item.Properties().stacksTo(1), AddonEntities.ENHANCED_BETTA, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH));
    public static final RegistryObject<Item> ENHANCED_BETTA_EGG_BUCKET = ITEMS_DEFERRED_REGISTRY.register("enhanced_betta_egg_bucket", () -> new EnhancedBettaEggBucket(new Item.Properties().stacksTo(1), AddonEntities.ENHANCED_BETTA_EGG, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH));
    public static final RegistryObject<Item> BUBBLE_NEST_ITEM = ITEMS_DEFERRED_REGISTRY.register("bubble_nest", () -> new BlockItem(AddonBlocks.BUBBLE_NEST.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
//    public static final RegistryObject<Item> TANK_GLASS_ITEM = ITEMS_DEFERRED_REGISTRY.register("tank_glass", () -> new BlockItem(AddonBlocks.TANK_GLASS.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> DISPLAY_TANK_ITEM = ITEMS_DEFERRED_REGISTRY.register("display_tank", () -> new BlockItem(AddonBlocks.DISPLAY_TANK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> E_CORDIFOLIUS_ITEM = ITEMS_DEFERRED_REGISTRY.register("e_cordifolius", () -> new BlockItem(AddonBlocks.E_CORDIFOLIUS.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> TALL_E_CORDIFOLIUS_ITEM = ITEMS_DEFERRED_REGISTRY.register("tall_e_cordifolius", () -> new BlockItem(AddonBlocks.TALL_E_CORDIFOLIUS.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> A_BARTERI_ITEM = ITEMS_DEFERRED_REGISTRY.register("a_barteri", () -> new BlockItem(AddonBlocks.A_BARTERI.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> VARIEGATED_A_BARTERI_ITEM = ITEMS_DEFERRED_REGISTRY.register("variegated_a_barteri", () -> new BlockItem(AddonBlocks.VARIEGATED_A_BARTERI.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> V_AMERICANA_ITEM = ITEMS_DEFERRED_REGISTRY.register("v_americana", () -> new BlockItem(AddonBlocks.V_AMERICANA.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> DUCKWEED_ITEM = ITEMS_DEFERRED_REGISTRY.register("duckweed", () -> new DuckweedPlantItem(AddonBlocks.DUCKWEED.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> FILLED_DISPLAY_TANK_ITEM = ITEMS_DEFERRED_REGISTRY.register("filled_display_tank", () -> new BlockItem(AddonBlocks.FILLED_DISPLAY_TANK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static void register(IEventBus modEventBus) {
        ITEMS_DEFERRED_REGISTRY.register(modEventBus);
    }
}
