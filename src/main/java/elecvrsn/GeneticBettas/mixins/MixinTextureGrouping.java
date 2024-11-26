package elecvrsn.GeneticBettas.mixins;

import com.mojang.blaze3d.platform.NativeImage;
import elecvrsn.GeneticBettas.IMixinTextureGrouping;
import elecvrsn.GeneticBettas.IMixinTextureLayer;
import mokiyoki.enhancedanimals.entity.util.Colouration;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TextureLayer;
import mokiyoki.enhancedanimals.renderer.texture.TexturingType;
import mokiyoki.enhancedanimals_backported.renderer.texture.UpdatedTextureGrouping;
import mokiyoki.enhancedanimals_backported.renderer.texture.UpdatedTexturingType;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.List;

import static mokiyoki.enhancedanimals.renderer.texture.TexturingUtils.*;
import static mokiyoki.enhancedanimals_backported.renderer.texture.UpdatedTextureGrouping.applyBGRABlend;

@Mixin(TextureGrouping.class)
@Implements(@Interface(iface = IMixinTextureGrouping.class, prefix = "betta$"))
public abstract class MixinTextureGrouping {
    @Unique
    public UpdatedTexturingType betta$updatedTexturingType = UpdatedTexturingType.NONE;
    @Shadow
    private List<TextureLayer> textureLayers;
    @Shadow
    private TexturingType texturingType;
    @Shadow
    private List<TextureGrouping> textureGroupings = new ArrayList<>();
    @Shadow
    private void layerGroups(NativeImage compiledImage, List<NativeImage> groupImages) {}
    @Shadow
    private void maskAlpha(NativeImage alphaMaskImage, List<NativeImage> groupImages) {}
    @Shadow
    private void blendAverage(NativeImage compiledImage, List<NativeImage> groupImages) {}
    @Shadow
    private void blendGroupDye(NativeImage baseImage, List<NativeImage> groupImages, int dyeColour) {}

    @Shadow
    protected abstract NativeImage applyGroupMerging(List<NativeImage> groupImages, Colouration colouration);
    @Shadow
    protected abstract void applyLayerSpecifics(TextureLayer layer, Colouration colouration);

    @Intrinsic(displace = true)
    public NativeImage betta$applyGroupMerging(List<NativeImage> groupImages, Colouration colouration) {
        if (!groupImages.isEmpty()) {
            if (betta$updatedTexturingType != UpdatedTexturingType.NONE && betta$updatedTexturingType != null) {
                NativeImage baseImage = groupImages.remove(0);
                switch (this.betta$updatedTexturingType) {
                    case MASK_GROUP -> this.maskAlpha(baseImage, groupImages);
                    case INTERSECT_GROUP -> this.intersectAlpha(baseImage, groupImages);
                    case CUTOUT_GROUP -> this.cutoutTextures(baseImage, groupImages);
                }
                return baseImage;
            } else {
                return this.applyGroupMerging(groupImages, colouration);
            }
        } else {
            return null;
        }
    }

    @Intrinsic(displace = true)
    public void betta$applyLayerSpecifics(TextureLayer layer, Colouration colouration) {
        if (((IMixinTextureLayer)layer).getUpdatedTexturingType() != UpdatedTexturingType.NONE && ((IMixinTextureLayer)layer).getUpdatedTexturingType() != null) {
            switch (((IMixinTextureLayer)layer).getUpdatedTexturingType()) {
                case APPLY_RGB -> layer.setTextureImage(applyBGRBlend(layer.getTextureImage(), layer.getRGB()));
                case APPLY_RGBA -> layer.setTextureImage(applyBGRABlend(layer.getTextureImage(), layer.getRGB()));
            }
        }
        else {
            this.applyLayerSpecifics(layer, colouration);
        }
    }

    protected void cutoutTextures(NativeImage cutoutImage, List<NativeImage> groupImages) {
        if (!groupImages.isEmpty()) {
            NativeImage image = groupImages.remove(0);
            if (!groupImages.isEmpty()) {
                layerGroups(image, groupImages);
            }
            int h = cutoutImage.getHeight();
            int w = cutoutImage.getWidth();
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    UpdatedTextureGrouping.cutoutAlpha(j, i, cutoutImage, image);
                }
            }
        }
    }

    @Unique
    protected void intersectAlpha(NativeImage colorImage, List<NativeImage> groupImages) {
        if (!groupImages.isEmpty()) {
            for (NativeImage groupImage : groupImages) {
                applyAlphaMaskBlend(colorImage, groupImage);
            }
        }
    }

    @Unique
    public void betta$setUpdatedTexturingType(UpdatedTexturingType betta$updatedTexturingType) {
        this.betta$updatedTexturingType = betta$updatedTexturingType;
    }

}
