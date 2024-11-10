package mokiyoki.enhancedanimals_backported.renderer.texture;

import com.mojang.blaze3d.platform.NativeImage;
import mokiyoki.enhancedanimals.entity.util.Colouration;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TexturingType;

import java.util.List;

import static mokiyoki.enhancedanimals.renderer.texture.TexturingUtils.applyAlphaMaskBlend;
import static mokiyoki.enhancedanimals.renderer.texture.TexturingUtils.applyPixelLayer;

/** Most of this code is copied from the dev version of GA for compatbility purposes **/
public class UpdatedTextureGrouping extends TextureGrouping {

    private UpdatedTexturingType updatedTexturingType;

    public UpdatedTextureGrouping(UpdatedTexturingType texturingType) {
        super(TexturingType.NONE);
        updatedTexturingType = texturingType;
    }

    private NativeImage applyGroupMerging(List<NativeImage> groupImages, Colouration colouration) {
        if (!groupImages.isEmpty()) {
            NativeImage baseImage = groupImages.remove(0);

            if (updatedTexturingType == UpdatedTexturingType.CUTOUT_GROUP) {
                cutoutTextures(baseImage, groupImages);
            } else if (updatedTexturingType == UpdatedTexturingType.MASK_GROUP) {
                maskAlpha(baseImage, groupImages);
            }

            return baseImage;
        }

        return null;
    }

    private void cutoutTextures(NativeImage cutoutImage, List<NativeImage> groupImages) {
        if (!groupImages.isEmpty()) {
            NativeImage image = groupImages.remove(0);
            if (!groupImages.isEmpty()) {
                layerGroups(image, groupImages);
            }
            int h = cutoutImage.getHeight();
            int w = cutoutImage.getWidth();
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    cutoutAlpha(j, i, cutoutImage, image);
                }
            }
        }
    }
    private void layerGroups(NativeImage compiledImage, List<NativeImage> groupImages) {
        for (NativeImage applyImage : groupImages) {
            applyPixelLayer(compiledImage, applyImage);
        }
    }

    protected static void cutoutAlpha(int xIn, int yIn, NativeImage maskingImage, NativeImage nativeImage) {
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

    private void maskAlpha(NativeImage alphaMaskImage, List<NativeImage> groupImages) {
        if (!groupImages.isEmpty()) {
            NativeImage mergeGroup = groupImages.get(0);
            //First merge image groups
            if (groupImages.size() > 1) {
                NativeImage baseImage = groupImages.get(0);
                for (int i = 1; i < groupImages.size(); i++) {
                    applyPixelLayer(baseImage, groupImages.get(i));
                }
                mergeGroup = baseImage;
            }

            applyAlphaMaskBlend(alphaMaskImage, mergeGroup);
        }
    }
}
