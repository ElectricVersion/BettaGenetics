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
        Biome.ClimateSettings climate = event.getClimate();
        if (event.getCategory() == Biome.BiomeCategory.SWAMP || (event.getCategory() != Biome.BiomeCategory.OCEAN && (climate.precipitation.equals(Biome.Precipitation.RAIN) && climate.temperature >= 0.8F && climate.downfall >= 0.85F))) {
            event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(AddonEntities.ENHANCED_BETTA.get(), 25, 1, 1));
        }
    }
}
