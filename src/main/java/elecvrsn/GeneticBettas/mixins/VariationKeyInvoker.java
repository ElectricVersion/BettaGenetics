package elecvrsn.GeneticBettas.mixins;

import mokiyoki.enhancedanimals.entity.util.VariationKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = VariationKey.class, remap = false)
public interface VariationKeyInvoker {

    @Invoker("legacy")
    static VariationKey invokeLegacy(String name, int legacyIndex) {
        throw new AssertionError();
    }
}
