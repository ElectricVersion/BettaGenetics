package elecvrsn.GeneticBettas.init.breeds;

import mokiyoki.enhancedanimals.util.Breed;
import mokiyoki.enhancedanimals.util.GeneSketch;
import net.minecraft.world.level.biome.Biomes;

public class BettaBreeds {
    public static final Breed GENERIC = new Breed(new Breed.Properties().setData("Generic", Biomes.FOREST, Breed.Rarity.ORDINARY)
            .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(0, "1")));

    public static final Breed PASSIVE = new Breed(new Breed.Properties().setData("Passive", Biomes.FOREST, Breed.Rarity.ORDINARY)
            .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(72, "1")));

    public static final Breed NEUTRAL = new Breed(new Breed.Properties().setData("Neutral", Biomes.FOREST, Breed.Rarity.ORDINARY)
            .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(72, "5")));

    public static final Breed AGGRESSIVE = new Breed(new Breed.Properties().setData("Aggressive", Biomes.FOREST, Breed.Rarity.ORDINARY)
            .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(72, "10")));
}
