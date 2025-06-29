package elecvrsn.GeneticBettas.datagen;

import elecvrsn.GeneticBettas.util.AddonReference;
import elecvrsn.GeneticBettas.world.feature.AddonConfiguredFeatures;
import elecvrsn.GeneticBettas.world.feature.AddonPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AddonWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, AddonConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, AddonPlacedFeatures::bootstrap);

    public AddonWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(AddonReference.MODID));
    }
}
