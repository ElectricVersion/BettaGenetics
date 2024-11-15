package elecvrsn.GeneticBettas.mixins;

import mokiyoki.enhancedanimals.init.FoodSerialiser;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(FoodSerialiser.class)
public interface IMixinFoodSerialiser {

    @Accessor(value = "compiledAnimalFoodMap")
    static Map<String, FoodSerialiser.AnimalFoodMap> getFoodMap() {
        throw new NotImplementedException("Error in IMixinFoodSerialiser");
    }


}
