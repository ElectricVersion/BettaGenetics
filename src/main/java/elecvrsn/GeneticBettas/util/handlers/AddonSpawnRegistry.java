package elecvrsn.GeneticBettas.util.handlers;

import elecvrsn.GeneticBettas.init.AddonEntities;
import elecvrsn.GeneticBettas.util.AddonReference;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AddonReference.MODID)
public class AddonSpawnRegistry {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addBiomeSpawns(BiomeLoadingEvent event) {
        event.getSpawns().getSpawner(MobCategory.WATER_AMBIENT).add(new MobSpawnSettings.SpawnerData(AddonEntities.ENHANCED_BETTA.get(), 1, 1, 3));
    }
}
