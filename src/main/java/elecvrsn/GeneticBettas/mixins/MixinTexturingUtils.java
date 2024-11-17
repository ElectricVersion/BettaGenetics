package elecvrsn.GeneticBettas.mixins;

import com.mojang.blaze3d.platform.NativeImage;
import mokiyoki.enhancedanimals.renderer.texture.TexturingUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static com.mojang.blaze3d.platform.NativeImage.*;
import static mokiyoki.enhancedanimals_backported.renderer.texture.UpdatedTextureGrouping.COLOUR_DEGREE;

@Mixin(TexturingUtils.class)
public class MixinTexturingUtils {

    @Shadow
    private static float cleanValue(float value) {
        return 0;
    }

    /**
     * @author ElectricVersion
     * @reason Fixing premultiplication bug
     */
    @Overwrite(remap = false)
    public static void applyPixelLayer(NativeImage baseImage, NativeImage appliedImage) {
        for(int i = 0; i < appliedImage.getHeight(); ++i) {
            for(int j = 0; j < appliedImage.getWidth(); ++j) {
                layerPixelUpdated(baseImage, j, i, appliedImage.getPixelRGBA(j, i));
            }
        }
    }

    private static void layerPixelUpdated(NativeImage baseImage, int xIn, int yIn, int colIn) {
        int i = baseImage.getPixelRGBA(xIn, yIn);
        float layerA = (float)getA(colIn) * COLOUR_DEGREE;
        float layerB = (float)getB(colIn) * COLOUR_DEGREE;
        float layerG = (float)getG(colIn) * COLOUR_DEGREE;
        float layerR = (float)getR(colIn) * COLOUR_DEGREE;
        float baseA = (float)getA(i) * COLOUR_DEGREE;
        float baseB = (float)getB(i) * COLOUR_DEGREE;
        float baseG = (float)getG(i) * COLOUR_DEGREE;
        float baseR = (float)getR(i) * COLOUR_DEGREE;
        float inverseBaseA = 1.0F - baseA;
        float inverseLayerA = 1.0F - layerA;
        float outAlpha = cleanValue(baseA+(inverseBaseA * layerA));
        float outBlue = cleanValue(((layerB * layerA) + (inverseLayerA * (baseB*baseA)))/outAlpha);
        float outGreen = cleanValue(((layerG * layerA) + (inverseLayerA * (baseG*baseA)))/outAlpha);
        float outRed = cleanValue(((layerR * layerA) + (inverseLayerA * (baseR*baseA)))/outAlpha);

        int j = (int)(outAlpha * 255.0F);
        int k = (int)(outBlue * 255.0F);
        int l = (int)(outGreen * 255.0F);
        int i1 = (int)(outRed * 255.0F);
        baseImage.setPixelRGBA(xIn, yIn, combine(j, k, l, i1));
    }
}
