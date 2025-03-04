package elecvrsn.GeneticBettas.mixins;

import elecvrsn.GeneticBettas.IMixinTextureLayer;
import mokiyoki.enhancedanimals.renderer.texture.TextureLayer;
import elecvrsn.GeneticBettas.renderer.texture.UpdatedTexturingType;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TextureLayer.class)
@Implements(@Interface(iface = IMixinTextureLayer.class, prefix = "betta$"))
public abstract class MixinTextureLayer {
    public UpdatedTexturingType updatedTexturingType = UpdatedTexturingType.NONE;
    @Shadow
    private int RGB;
    public void setRGB(Integer RGB) {
        this.RGB = RGB;
    }

    public void setUpdatedTexturingType(UpdatedTexturingType updatedTexturingType) {
        this.updatedTexturingType = updatedTexturingType;
    }

    public UpdatedTexturingType getUpdatedTexturingType() {
        return this.updatedTexturingType;
    }
}
