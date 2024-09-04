package elecvrsn.GeneticBettas.init;

import elecvrsn.GeneticBettas.block.BubbleNestBlock;
//import elecvrsn.GeneticBettas.block.TankGlassBlock;
import elecvrsn.GeneticBettas.util.AddonReference;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AddonBlocks {
    public static final DeferredRegister<Block> BLOCKS_DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, AddonReference.MODID);
    public static final RegistryObject<Block> BUBBLE_NEST = BLOCKS_DEFERRED_REGISTRY.register("bubble_nest", () -> new BubbleNestBlock(Block.Properties.of(Material.WATER, MaterialColor.ICE).noCollission().instabreak().sound(SoundType.LILY_PAD)));
//    public static final RegistryObject<Block> TANK_GLASS = BLOCKS_DEFERRED_REGISTRY.register("tank_glass", () -> new TankGlassBlock(Block.Properties.of(Material.GLASS, MaterialColor.WOOL).sound(SoundType.GLASS)));
    public static void register(IEventBus modEventBus) {
        BLOCKS_DEFERRED_REGISTRY.register(modEventBus);
    }

}
