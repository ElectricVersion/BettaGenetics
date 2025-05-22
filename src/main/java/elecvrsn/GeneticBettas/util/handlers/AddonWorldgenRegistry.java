package elecvrsn.GeneticBettas.util.handlers;

import elecvrsn.GeneticBettas.util.AddonReference;
import elecvrsn.GeneticBettas.world.feature.AddonPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = AddonReference.MODID)
public class AddonWorldgenRegistry {

    @SubscribeEvent
    public static void addPlantGen(BiomeLoadingEvent event) {
        ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        if(types.contains(BiomeDictionary.Type.SWAMP)) {
            List<Holder<PlacedFeature>> base =
                    event.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION);
            base.add(AddonPlacedFeatures.PATCH_DUCKWEED);
            base.add(AddonPlacedFeatures.PATCH_E_CORDIFOLIUS);
        }
    }
}
