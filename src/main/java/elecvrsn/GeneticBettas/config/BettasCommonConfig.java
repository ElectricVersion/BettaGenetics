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


        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("betta");
            bettaWildTypeChance = builder
                    .defineInRange("How random the genes should be, 100 is all wildtype animals, 0 is completely random which often results in all white animals:", 90, 0, 100);
            bettaAdultAge = builder
                    .defineInRange("How many ticks it takes for a Betta to become an adult, 24000 = 1 Minecraft Day:", 60000, 1, Integer.MAX_VALUE);
            bettaHatchTime = builder
                    .defineInRange("How long in ticks it takes a Betta egg to hatch, Default is 24000", 24000, 1, Integer.MAX_VALUE);
            bettaSpawnWeight = builder
                    .defineInRange("How highly Betta spawning is weighted, larger numbers spawn more, Default is 10", 10, 1, 20);
            bettaMinimumGroup = builder
                    .defineInRange("The minimum number of Bettas you want to find in a group at spawn, Default is 4", 4, 1, 60);
            bettaMaximumGroup = builder
                    .defineInRange("The maximum number of Bettas you want to find in a group at spawn, Default is 6", 6, 1, 60);
            builder.pop();
        }
    }
}
