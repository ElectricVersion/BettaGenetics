package elecvrsn.GeneticBettas.util.handlers;

import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonEntities;
import elecvrsn.GeneticBettas.util.AddonReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;

@Mod.EventBusSubscriber(modid = AddonReference.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class AddonEventRegistry {

    @SubscribeEvent
    public static void onEntityAttributeCreationRegistry(EntityAttributeCreationEvent event) {
        event.put(ENHANCED_BETTA.get(), EnhancedBetta.prepareAttributes().build());
    }

    @SubscribeEvent
    public static void onEntitiesRegistry(RegistryEvent.Register<EntityType<?>> event) {
        SpawnPlacements.register(AddonEntities.ENHANCED_BETTA.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EnhancedBetta::checkBettaSpawnRules);
    }

}
