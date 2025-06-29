package elecvrsn.GeneticBettas.entity.genetics;

import elecvrsn.GeneticBettas.config.BettasCommonConfig;
import elecvrsn.GeneticBettas.init.breeds.BettaBreeds;
import elecvrsn.GeneticBettas.util.AddonReference;
import mokiyoki.enhancedanimals.entity.genetics.AbstractGeneticsInitialiser;
import mokiyoki.enhancedanimals.util.Breed;
import mokiyoki.enhancedanimals.util.Genes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BettaGeneticsInitialiser extends AbstractGeneticsInitialiser {

    int WTC = BettasCommonConfig.COMMON.bettaWildTypeChance.get();
    public BettaGeneticsInitialiser() {
//        this.breeds.add(BettaBreeds.GENERIC);
//        this.breeds.add(BettaBreeds.NEUTRAL);
//        this.breeds.add(BettaBreeds.AGGRESSIVE);
        this.breeds.add(BettaBreeds.PASSIVE);
        this.breeds.add(BettaBreeds.MAXRUFOUS);
        this.breeds.add(BettaBreeds.MINRUFOUS);
        this.breeds.add(BettaBreeds.KOI);
        this.breeds.add(BettaBreeds.SAMURAI);
        this.breeds.add(BettaBreeds.GIANT);
        this.breeds.add(BettaBreeds.MUSTARDGAS);
        this.breeds.add(BettaBreeds.BLUEMARBLE);
        this.breeds.add(BettaBreeds.SALAMANDER);
        this.breeds.add(BettaBreeds.NEMO);
    }

    List<Breed> breeds = new ArrayList<>();

    public Genes generateNewGenetics(LevelAccessor world, BlockPos pos, boolean generateBreed) {
        return super.generateNewGenetics(world, pos, generateBreed, this.breeds);
    }

    public Genes generateWithBreed(LevelAccessor world, BlockPos pos, String breed) {
        return super.generateWithBreed(world, pos, this.breeds, breed);
    }
    @Override
    public Genes generateLocalWildGenetics(Holder<Biome> biomeHolder, BlockPos blockPos, boolean isFlat) {
        int[] sexlinkedGenes = new int[AddonReference.BETTA_SEXLINKED_GENES_LENGTH];
        int[] autosomalGenes = new int[AddonReference.BETTA_AUTOSOMAL_GENES_LENGTH];

        sexlinkedGenes[0] = 1;
        sexlinkedGenes[1] = 1;

        Biome biome = biomeHolder.value();
//        //Temporary Filler WTs
//        for (int i = 0; i < AddonReference.BETTA_AUTOSOMAL_GENES_LENGTH; i++) {
//            autosomalGenes[i] = 1;
//        }
        //Iridescence [Turquoise <> Steel Blue]
        autosomalGenes[0] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[1] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Spread Iridescence [wildtype < Spread]
        autosomalGenes[2] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[3] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Masked Iridescence (modifies spread) [Wildtype < Masked]
        autosomalGenes[4] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[5] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Melano Black [Wildtype > melano]
        //Homozygous melano causes infertility on females
        autosomalGenes[6] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[7] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Laced Black [Wildtype > laced]
        //Supposedly prevents the infertility when paired with melano. somehow.
        autosomalGenes[8] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[9] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Cambodian [Wildtype > cambodian]
        autosomalGenes[10] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[11] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Extended Red [wildtype < Extended]
        autosomalGenes[12] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[13] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Blonde [Wildtype > blonde]
        autosomalGenes[14] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[15] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Red Mask [Wildtype <> Mask]
        autosomalGenes[16] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[17] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Butterfly [wildtype < Butterfly]
        autosomalGenes[18] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[19] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Butterfly Level 1 [Wildtype <> Higher]
        autosomalGenes[20] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[21] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Butterfly Level 2 [wildtype < Higher]
        autosomalGenes[22] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[23] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Body Iridescence Area [Wildtype > higher]
        autosomalGenes[24] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[25] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Body Iridescence Area [Wildtype > higher]
        autosomalGenes[26] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[27] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Fin Iridescence Area [Wildtype > higher]
        autosomalGenes[28] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[29] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        //Fin Iridescence Area [Wildtype > higher]
        autosomalGenes[30] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[31] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Iridescence Intensity 1 [wildtype < Higher > lower]
        autosomalGenes[32] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(3) + 1) : 1;
        autosomalGenes[33] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(3) + 1) : 1;
        //Iridescence Intensity 2 [wildtype < Lower]
        autosomalGenes[34] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[35] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Fin Red Level 1 [wildtype <> lower]
        autosomalGenes[36] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[37] = 1;
        //Fin Red Level 2 [wildtype <> lower]
        autosomalGenes[38] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[39] = 1;

        //Opaque [Iridescent <> Opaque]
        autosomalGenes[40] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[41] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Non-Red [Red > orange != yellow]
        autosomalGenes[42] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(3) + 1) : 1;
        autosomalGenes[43] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(3) + 1) : 1;

        //Body Red Area [Wildtype <> higher]
        autosomalGenes[44] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[45] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Body Red Area [Wildtype <> lower]
        autosomalGenes[46] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[47] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Metallic [Wildtype <> Metallic]
        autosomalGenes[48] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[49] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Mask Iridescence Area [Wildtype > higher]
        autosomalGenes[50] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[51] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        //Mask Iridescence Area [Wildtype > higher]
        autosomalGenes[52] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[53] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        //Mask Iridescence Area 3 [Wildtype > higher]
        autosomalGenes[54] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[55] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Dumbo [Wildtype > dumbo]
        autosomalGenes[56] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[57] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Fin Length [plakat+ < Veil]
        autosomalGenes[58] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[59] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Halfmoon [Wildtype <> Halfmoon] (het form is delta)
        autosomalGenes[60] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[61] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Doubletail [Wildtype <> Doubletail]
        autosomalGenes[62] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[63] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Crowntail [Wildtype <> Horrifying]
        autosomalGenes[64] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[65] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Iridescent Rims [wildtype < Rims]
        autosomalGenes[66] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[67] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Iridescent Rims Level 1 [Wildtype <> Higher]
        autosomalGenes[68] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[69] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Iridescent Rims Level 2 [wildtype < Higher]
        autosomalGenes[70] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[71] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Aggression Polygenes [lower <> higher]
        autosomalGenes[72] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 2;
        autosomalGenes[73] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 2;

        autosomalGenes[74] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 2;
        autosomalGenes[75] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 2;

        autosomalGenes[76] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 2;
        autosomalGenes[77] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 2;

        autosomalGenes[78] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 2;
        autosomalGenes[79] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 2;

        //Marble [wildtype <> Marble > Vanda] (Homoezygous decreases the size of marble)
        autosomalGenes[80] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(3) + 1) : 1;
//        autosomalGenes[81] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(3) + 1) : 1;
        autosomalGenes[81] = 1;

        //RED MARBLE GENES
        //Red Marble Size-
        autosomalGenes[82] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[83] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Red Marble Size-
        autosomalGenes[84] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[85] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Red Marble Size+
        autosomalGenes[86] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 2;
        autosomalGenes[87] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 2;

        //Red Marble Size+
        autosomalGenes[88] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[89] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Red Marble Size+
        autosomalGenes[90] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[91] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Red Marble Quality 1
        autosomalGenes[92] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;
        autosomalGenes[93] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;

        //Red Marble Quality 2
        autosomalGenes[94] = (ThreadLocalRandom.current().nextInt(5) + 1);
        autosomalGenes[95] = (ThreadLocalRandom.current().nextInt(5) + 1);


        //BLACK MARBLE GENES
        //Black Marble Size-
        autosomalGenes[96] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[97] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Black Marble Size-
        autosomalGenes[98] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[99] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Black Marble Size+
        autosomalGenes[100] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 2;
        autosomalGenes[101] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 2;

        //Black Marble Size+
        autosomalGenes[102] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[103] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Black Marble Size+
        autosomalGenes[104] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[105] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Black Marble Quality 1
        autosomalGenes[106] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;
        autosomalGenes[107] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;

        //Black Marble Quality 2
        autosomalGenes[108] = (ThreadLocalRandom.current().nextInt(5) + 1);
        autosomalGenes[109] = (ThreadLocalRandom.current().nextInt(5) + 1);

        //BLOODRED MARBLE GENES
        //Bloodred Marble Size-
        autosomalGenes[110] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;;
        autosomalGenes[111] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;;

        //Bloodred Marble Size-
        autosomalGenes[112] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;;
        autosomalGenes[113] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;;

        //Bloodred Marble Size+
        autosomalGenes[114] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;;
        autosomalGenes[115] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;;

        //Bloodred Marble Size+
        autosomalGenes[116] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 2;;
        autosomalGenes[117] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 2;;

        //Bloodred Marble Size+
        autosomalGenes[118] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;;
        autosomalGenes[119] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;;

        //Bloodred Marble Quality 1
        autosomalGenes[120] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;
        autosomalGenes[121] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;

        //Bloodred Marble Quality 2
        autosomalGenes[122] = (ThreadLocalRandom.current().nextInt(5) + 1);
        autosomalGenes[123] = (ThreadLocalRandom.current().nextInt(5) + 1);

        //IRI MARBLE GENES
        //Iri Marble Size-
        autosomalGenes[124] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[125] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Iri Marble Size-
        autosomalGenes[126] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[127] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Iri Marble Size+
        autosomalGenes[128] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 2;
        autosomalGenes[129] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 2;

        //Iri Marble Size+
        autosomalGenes[130] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[131] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Iri Marble Size+
        autosomalGenes[132] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[133] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Iri Marble Quality 1
        autosomalGenes[134] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;
        autosomalGenes[135] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;

        //Iri Marble Quality 2
        autosomalGenes[136] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;
        autosomalGenes[137] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;

        //OPAQUE MARBLE GENES
        //Opaque Marble Size-
        autosomalGenes[138] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[139] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Opaque Marble Size-
        autosomalGenes[140] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[141] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Opaque Marble Size+
        autosomalGenes[142] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[143] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Opaque Marble Size+
        autosomalGenes[144] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[145] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Opaque Marble Size+
        autosomalGenes[146] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[147] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Opaque Marble Quality 1
        autosomalGenes[148] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;
        autosomalGenes[149] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;

        //Opaque Marble Quality 2
        autosomalGenes[150] = (ThreadLocalRandom.current().nextInt(5) + 1);
        autosomalGenes[151] = (ThreadLocalRandom.current().nextInt(5) + 1);

        //Bloodred (Inheritance unknown - dominant?) [wildtype < Bloodred]
        autosomalGenes[152] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[153] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Extended Bloodred [wildtype < Extended]
        autosomalGenes[154] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[155] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Bloodred Mask [Wildtype <> Mask]
        autosomalGenes[156] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[157] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Fin Bloodred Level 1 [wildtype <> lower]
        autosomalGenes[158] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[159] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Fin Bloodred Level 2 [wildtype <> lower]
        autosomalGenes[160] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[161] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Body Bloodred Area [Wildtype <> higher]
        autosomalGenes[162] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[163] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Body Bloodred Area [Wildtype <> lower]
        autosomalGenes[164] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[165] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Giant [wildtype <> Giant]
        autosomalGenes[166] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[167] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Larger Size [1-5]
        autosomalGenes[168] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;
        autosomalGenes[169] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;

        //Smaller Size [1-5]
        autosomalGenes[170] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;
        autosomalGenes[171] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(5) + 1) : 1;

        //Dragonscale [Wildtype <> Dragon]
        autosomalGenes[172] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[173] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Rufousing genes [174-183: lighter; 184-193: darker]
        for (int i = 174; i < 194; i++) {
            if (ThreadLocalRandom.current().nextInt(100) > WTC*0.6F) {
                autosomalGenes[i] = ThreadLocalRandom.current().nextInt(2)+1;
            } else {
                autosomalGenes[i] = 1;
            }
        }

        //Bloodred Rufousing genes [194-203: lighter; 204-213: darker]
        for (int i = 194; i < 214; i++) {
            if (ThreadLocalRandom.current().nextInt(100) > WTC*0.6F) {
                autosomalGenes[i] = ThreadLocalRandom.current().nextInt(2)+1;
            } else {
                autosomalGenes[i] = 1;
            }
        }

        //Iridescence Hue genes [214-223: greener; 224-233: bluer]
        for (int i = 214; i < 234; i++) {
            if (ThreadLocalRandom.current().nextInt(100) > WTC*0.6F) {
                autosomalGenes[i] = ThreadLocalRandom.current().nextInt(2)+1;
            } else {
                autosomalGenes[i] = 1;
            }
        }

        //Dragonscale Modifier [wildtype < Less Dragonscale]
        autosomalGenes[234] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[235] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;

        //Hybrid Iridescence [wildtype < Hybrid]
//        autosomalGenes[236] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
//        autosomalGenes[237] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(2) + 1) : 1;
        autosomalGenes[236] = 1;
        autosomalGenes[237] = 1;

        //Dumbo Fin Size Adder [normal <> bigger <> biggest]
        autosomalGenes[238] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(3) + 1) : 1;
        autosomalGenes[239] = ThreadLocalRandom.current().nextInt(100) > WTC ? (ThreadLocalRandom.current().nextInt(3) + 1) : 1;

        return new Genes(sexlinkedGenes, autosomalGenes);
    }
}
