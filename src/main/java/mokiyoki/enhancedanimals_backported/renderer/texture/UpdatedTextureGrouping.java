package mokiyoki.enhancedanimals_backported.renderer.texture;

import com.mojang.blaze3d.platform.NativeImage;
import elecvrsn.GeneticBettas.IMixinTextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TexturingType;
import org.spongepowered.asm.mixin.Shadow;

public class UpdatedTextureGrouping extends TextureGrouping {

    private final static float COLOUR_DEGREE = 1F/255F;

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

    public static NativeImage applyBGRABlend(NativeImage textureImage, int rgba) {
        for(int i = 0; i < textureImage.getHeight(); ++i) {
            for (int j = 0; j < textureImage.getWidth(); ++j) {
                blendBGRA(j, i, rgba, textureImage);
            }
        }
        return textureImage;
    }
    private static void blendBGRA(int xIn, int yIn, int rgba, NativeImage nativeimage) {
        int i = nativeimage.getPixelRGBA(xIn, yIn);

        float originalAlpha = (float)(i >> 24 & 255) * COLOUR_DEGREE;

        if(originalAlpha != 0.0F) {
            float a = (float)(rgba >> 24 & 255) * COLOUR_DEGREE;
            if (a != 0.0F) {
                float r = (float) (rgba >> 16 & 255) * COLOUR_DEGREE;
                float g = (float) (rgba >> 8 & 255) * COLOUR_DEGREE;
                float b = (float) (rgba >> 0 & 255) * COLOUR_DEGREE;
                float originalBlue = (float) (i >> 16 & 255) * COLOUR_DEGREE;
                float originalGreen = (float) (i >> 8 & 255) * COLOUR_DEGREE;
                float originalRed = (float) (i >> 0 & 255) * COLOUR_DEGREE;

                float f10 = b * 255 * originalBlue;
                float f11 = g * 255 * originalGreen;
                float f12 = r * 255 * originalRed;

                int j = (int) (originalAlpha * a * 255);
                int k = (int) (f10);
                int l = (int) (f11);
                int i1 = (int) (f12);

                nativeimage.setPixelRGBA(xIn, yIn, j << 24 | k << 16 | l << 8 | i1 << 0);
            } else {
                nativeimage.setPixelRGBA(xIn, yIn, 0);
            }
        }
    }

}
