package elecvrsn.GeneticBettas.init.breeds;

import mokiyoki.enhancedanimals.util.Breed;
import mokiyoki.enhancedanimals.util.GeneSketch;
import net.minecraft.world.level.biome.Biomes;

public class BettaBreeds {
//    public static final Breed GENERIC = new Breed(new Breed.Properties().setData("Generic", Biomes.FOREST, Breed.Rarity.ORDINARY)
//            .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(0, "1")));
//
//
//    public static final Breed NEUTRAL = new Breed(new Breed.Properties().setData("Neutral", Biomes.FOREST, Breed.Rarity.ORDINARY)
//            .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(72, "3","3","3","3")));
//
//    public static final Breed AGGRESSIVE = new Breed(new Breed.Properties().setData("Aggressive", Biomes.FOREST, Breed.Rarity.ORDINARY)
//            .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(72, "5","5","5","5")));
//
    public static final Breed MAXRUFOUS = new Breed(new Breed.Properties().setData("MaxRufous", Biomes.FOREST, Breed.Rarity.ORDINARY)
            .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(174, "1","1","1","1","1", "2","2","2","2","2").add(194, "1","1","1","1","1", "2","2","2","2","2").add(214, "1","1","1","1","1", "2","2","2","2","2")));
    public static final Breed MINRUFOUS = new Breed(new Breed.Properties().setData("MinRufous", Biomes.FOREST, Breed.Rarity.ORDINARY)
            .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(174, "2","2","2","2","2", "1","1","1","1","1").add(194, "2","2","2","2","2", "1","1","1","1","1").add(214, "2","2","2","2","2", "1","1","1","1","1")));

    public static final Breed PASSIVE = new Breed(new Breed.Properties().setData("Passive", Biomes.FOREST, Breed.Rarity.ORDINARY)
        .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(72, "1","1","1","1")));

    public static final Breed KOI = new Breed(new Breed.Properties().setData("Koi", Biomes.FOREST, Breed.Rarity.ORDINARY)
        .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(80, "2").add(92, "5","5").add(106, "5","5").add(120, "5","5").add(134, "5","5").add(148, "5","5")));

    public static final Breed SAMURAI = new Breed(new Breed.Properties().setData("SamuraiDragonscale", Biomes.FOREST, Breed.Rarity.ORDINARY)
        .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(0,"2").add(4, "1|2", "2").add(36, "2,1|2","2","2").add(46, "2").add(172, "2,1|2").add(234, "1|2")));

    public static final Breed GIANT = new Breed(new Breed.Properties().setData("Giant", Biomes.FOREST, Breed.Rarity.ORDINARY)
        .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(166, "2")));

    public static final Breed MUSTARDGAS = new Breed(new Breed.Properties().setData("MustardGas", Biomes.FOREST, Breed.Rarity.ORDINARY)
        .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(0, "1,2").add(6, "1|2", "2").add(24, "2", "1|2", "_", "_", "2").add(42, "3")));

    public static final Breed BLUEMARBLE = new Breed(new Breed.Properties().setData("BlueMarble", Biomes.FOREST, Breed.Rarity.ORDINARY)
            .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(0, "1,1|2", "2", "2").add(40, "2").add(50, "2", "2", "2").add(80, "2").add(124, "1", "1", "2", "2", "2").add(148, "3-5").add(172, "1")));

    public static final Breed SALAMANDER = new Breed(new Breed.Properties().setData("Salamander", Biomes.FOREST, Breed.Rarity.ORDINARY)
        .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(0, "2,1|2").add(4, "2").add(12, "2").add(16, "1", "2").add(24, "2", "2", "2", "2").add(32, "3|1").add(56, "2")));

    public static final Breed NEMO = new Breed(new Breed.Properties().setData("Nemo", Biomes.FOREST, Breed.Rarity.ORDINARY)
        .setGeneSketch(new GeneSketch().add(0, "1"), new GeneSketch().add(42, "2").add(24, "1","1","1","1","3").add(80, "2").add(90,  "2", "4-5", "4-5").add(96, "2", "2", "4-5", "4-5").add(124, "2", "1|2").add(120, "4-5", "4-5").add(152, "2").add(194, "2","2")));
}
