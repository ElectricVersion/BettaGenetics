package elecvrsn.GeneticBettas.mixins;

import mokiyoki.enhancedanimals.init.FoodSerialiser;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

//@Mixin(FoodSerialiser.class)
public class MixinFoodSerialiser {

//    @Shadow
    private static Map<String, FoodSerialiser.AnimalFoodMap> compiledAnimalFoodMap = new HashMap<>();


    public static FoodSerialiser.AnimalFoodMap getAnimalFoodMap(String animal) {
        return compiledAnimalFoodMap.get(animal);
    }

}
