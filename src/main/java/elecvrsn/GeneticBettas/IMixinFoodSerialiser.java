package elecvrsn.GeneticBettas;

import mokiyoki.enhancedanimals.init.FoodSerialiser;

public interface IMixinFoodSerialiser {
    public FoodSerialiser.AnimalFoodMap getAnimalFoodMap(String animal);

}