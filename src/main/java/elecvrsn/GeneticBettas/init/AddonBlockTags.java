package elecvrsn.GeneticBettas.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static elecvrsn.GeneticBettas.util.AddonReference.MODID;

public class AddonBlockTags {
    public static final TagKey<Block> AQUATIC_PLANT_PLACEABLE = BlockTags.create(new ResourceLocation(MODID,"aquatic_plant_placeable"));
}
