package elecvrsn.GeneticBettas.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BettasCommonConfig {

    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static String getFileNameForLoader() {
        return "genetic-bettas-common.toml";
    }

    public static ForgeConfigSpec getConfigSpecForLoader() {
        return COMMON_SPEC;
    }

    public static class CommonConfig {
        public final ForgeConfigSpec.IntValue bettaWildTypeChance;
        public final ForgeConfigSpec.IntValue bettaAdultAge;
        public final ForgeConfigSpec.IntValue bettaHatchTime;
        public final ForgeConfigSpec.IntValue bettaSpawnWeight;
        public final ForgeConfigSpec.IntValue bettaMinimumGroup;
        public final ForgeConfigSpec.IntValue bettaMaximumGroup;
        public final ForgeConfigSpec.BooleanValue replaceOtherBettas;
        public final ForgeConfigSpec.BooleanValue allowPlantCloning;


        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("betta");
            bettaWildTypeChance = builder
                    .comment("Default: 90")
                    .defineInRange("How random the genes should be, 100 is all wildtype animals, 0 is completely random:", 90, 0, 100);
            bettaAdultAge = builder
                    .comment("Default: 48000")
                    .defineInRange("How many ticks it takes for a Betta to become an adult:", 48000, 1, Integer.MAX_VALUE);
            bettaHatchTime = builder
                    .comment("Default: 24000")
                    .defineInRange("How many ticks it takes a Betta egg to hatch:", 24000, 1, Integer.MAX_VALUE);
            bettaSpawnWeight = builder
                    .comment("Default: 10")
                    .defineInRange("How highly Betta spawning is weighted, larger numbers spawn more:", 5, 1, 20);
            bettaMinimumGroup = builder
                    .comment("Default: 4")
                    .defineInRange("The minimum number of Bettas you want to find in a group at spawn:", 4, 1, 60);
            bettaMaximumGroup = builder
                    .comment("Default: 6")
                    .defineInRange("The maximum number of Bettas you want to find in a group at spawn", 6, 1, 60);
            replaceOtherBettas = builder
                    .comment("Default: false \nNOTE: I do not recommend enabling betta replacement unless you've disabled spawns for your other betta mod (e.g. DragN's or Coda's respective betta mods). Genetic bettas don't despawn, so you will get far too many spawns otherwise!")
                    .define("Replace Bettas from other mods", false);
            allowPlantCloning = builder
                    .comment("Default: false")
                    .define("Allow aquatic plants to be duplicated with bonemeal", false);
            builder.pop();
        }
    }
}
