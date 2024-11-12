package elecvrsn.GeneticBettas.mixins;

import com.google.gson.Gson;
import elecvrsn.GeneticBettas.IMixinFoodSerialiser;
import mokiyoki.enhancedanimals.init.FoodSerialiser;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(FoodSerialiser.class)
public abstract class MixinFoodSerialiser extends SimpleJsonResourceReloadListener implements IMixinFoodSerialiser {
    @Shadow
    private static Map<String, FoodSerialiser.AnimalFoodMap> compiledAnimalFoodMap = new HashMap<>();

    public MixinFoodSerialiser(Gson p_10768_, String p_10769_) {
        super(p_10768_, p_10769_);
    }

    public FoodSerialiser.AnimalFoodMap betta$getAnimalFoodMap(String animal) {
        return compiledAnimalFoodMap.get(animal);
    }

}
