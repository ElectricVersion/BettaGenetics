package mokiyoki.enhancedanimals_backported.renderer.texture;

import com.mojang.blaze3d.platform.NativeImage;
import elecvrsn.GeneticBettas.IMixinTextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TexturingType;
import org.spongepowered.asm.mixin.Shadow;

public class UpdatedTextureGrouping extends TextureGrouping {

    public UpdatedTextureGrouping(UpdatedTexturingType texturingType) {
        super(TexturingType.NONE);
        IMixinTextureGrouping mixin = ((IMixinTextureGrouping) this);
        mixin.setUpdatedTexturingType(texturingType);
    }


    public UpdatedTextureGrouping(TexturingType texturingType) {
        super(texturingType);
    }

    public static void cutoutAlpha(int xIn, int yIn, NativeImage maskingImage, NativeImage nativeImage) {
        int maskA = maskingImage.getPixelRGBA(xIn, yIn) >> 24 & 255;
        if (maskA != 255) {
            int colour = nativeImage.getPixelRGBA(xIn, yIn);
            maskA = 255-maskA;
            float a = ((float)maskA)/255F;
            a = a * ((colour >> 24 & 255)/255F);
            maskA = (int)(a * 255F);

            maskingImage.setPixelRGBA(xIn, yIn, maskA << 24 | (colour >> 16 & 255) << 16 | (colour >> 8 & 255) << 8 | (colour & 255));
        } else {
            maskingImage.setPixelRGBA(xIn, yIn, 0);
        }
    }

}
