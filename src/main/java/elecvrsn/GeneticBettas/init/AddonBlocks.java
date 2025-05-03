package elecvrsn.GeneticBettas.init;

import elecvrsn.GeneticBettas.block.*;
import elecvrsn.GeneticBettas.block.entity.FilledDisplayTankBlockEntity;
import elecvrsn.GeneticBettas.block.entity.BubbleNestBlockEntity;
import elecvrsn.GeneticBettas.util.AddonReference;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AddonBlocks {
    public static final DeferredRegister<Block> BLOCKS_DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, AddonReference.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES_DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, AddonReference.MODID);
    public static final RegistryObject<Block> BUBBLE_NEST = BLOCKS_DEFERRED_REGISTRY.register("bubble_nest", () -> new BubbleNestBlock(Block.Properties.of(Material.WATER, MaterialColor.ICE).noCollission().instabreak().sound(SoundType.LILY_PAD)));
    public static final RegistryObject<Block> TANK_GLASS = BLOCKS_DEFERRED_REGISTRY.register("tank_glass", () -> new TankGlassBlock(Block.Properties.of(Material.GLASS, MaterialColor.WOOL).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<Block> FILLED_DISPLAY_TANK = BLOCKS_DEFERRED_REGISTRY.register("filled_display_tank", () -> new FilledDisplayTankBlock(Block.Properties.of(Material.GLASS, MaterialColor.WOOL).sound(SoundType.GLASS).strength(0.3F).noOcclusion()));
    public static final RegistryObject<Block> DISPLAY_TANK = BLOCKS_DEFERRED_REGISTRY.register("display_tank", () -> new DisplayTankBlock(Block.Properties.of(Material.GLASS, MaterialColor.WOOL).sound(SoundType.GLASS).strength(0.3F).noOcclusion()));
    public static final RegistryObject<Block> E_CORDIFOLIUS = BLOCKS_DEFERRED_REGISTRY.register("e_cordifolius", () -> new SmallTankPlant(Block.Properties.of(Material.PLANT, MaterialColor.PLANT).sound(SoundType.BIG_DRIPLEAF).noCollission().instabreak().noOcclusion()));
    public static final RegistryObject<Block> TALL_E_CORDIFOLIUS = BLOCKS_DEFERRED_REGISTRY.register("tall_e_cordifolius", () -> new LargeTankPlant(Block.Properties.of(Material.PLANT, MaterialColor.PLANT).sound(SoundType.BIG_DRIPLEAF).noCollission().instabreak().noOcclusion()));
    public static final RegistryObject<Block> A_BARTERI = BLOCKS_DEFERRED_REGISTRY.register("a_barteri", () -> new SmallTankPlant(Block.Properties.of(Material.PLANT, MaterialColor.PLANT).sound(SoundType.BIG_DRIPLEAF).noCollission().instabreak().noOcclusion()));
    public static final RegistryObject<Block> VARIEGATED_A_BARTERI = BLOCKS_DEFERRED_REGISTRY.register("variegated_a_barteri", () -> new SmallTankPlant(Block.Properties.of(Material.PLANT, MaterialColor.PLANT).sound(SoundType.BIG_DRIPLEAF).noCollission().instabreak().noOcclusion()));
    public static final RegistryObject<Block> V_AMERICANA = BLOCKS_DEFERRED_REGISTRY.register("v_americana", () -> new LargeTankPlant(Block.Properties.of(Material.PLANT, MaterialColor.PLANT).sound(SoundType.BIG_DRIPLEAF).noCollission().instabreak().noOcclusion()));
    public static final RegistryObject<Block> DUCKWEED = BLOCKS_DEFERRED_REGISTRY.register("duckweed", () -> new DuckweedPlant(Block.Properties.of(Material.PLANT, MaterialColor.PLANT).sound(SoundType.BIG_DRIPLEAF).noCollission().instabreak().noOcclusion()));
    public static final RegistryObject<BlockEntityType<FilledDisplayTankBlockEntity>> FILLED_DISPLAY_TANK_BLOCK_ENTITY = BLOCK_ENTITIES_DEFERRED_REGISTRY.register("filled_display_tank", () -> BlockEntityType.Builder.of(FilledDisplayTankBlockEntity::new, AddonBlocks.FILLED_DISPLAY_TANK.get()).build(null));
    public static final RegistryObject<BlockEntityType<BubbleNestBlockEntity>> BUBBLE_NEST_BLOCK_ENTITY = BLOCK_ENTITIES_DEFERRED_REGISTRY.register("bubble_nest", () -> BlockEntityType.Builder.of(BubbleNestBlockEntity::new, AddonBlocks.BUBBLE_NEST.get()).build(null));
    public static void register(IEventBus modEventBus) {
        BLOCKS_DEFERRED_REGISTRY.register(modEventBus);
        BLOCK_ENTITIES_DEFERRED_REGISTRY.register(modEventBus);
    }

}
