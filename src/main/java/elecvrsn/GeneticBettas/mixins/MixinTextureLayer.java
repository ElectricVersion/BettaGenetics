package elecvrsn.GeneticBettas.mixins;

import elecvrsn.GeneticBettas.IMixinTextureLayer;
import mokiyoki.enhancedanimals.renderer.texture.TextureLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TextureLayer.class)
public class MixinTextureLayer implements IMixinTextureLayer {
    @Shadow
    private int RGB;
    public void setRGB(Integer RGB) {
        this.RGB = RGB;
    }

}
