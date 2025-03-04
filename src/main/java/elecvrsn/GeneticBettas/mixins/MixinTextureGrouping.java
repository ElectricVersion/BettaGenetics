package elecvrsn.GeneticBettas.mixins;

import com.mojang.blaze3d.platform.NativeImage;
import elecvrsn.GeneticBettas.IMixinTextureGrouping;
import mokiyoki.enhancedanimals.entity.util.Colouration;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TextureLayer;
import elecvrsn.GeneticBettas.renderer.texture.UpdatedTexturingType;
import org.spongepowered.asm.mixin.*;

import java.util.List;

import static mokiyoki.enhancedanimals.renderer.texture.TexturingUtils.*;

@Mixin(TextureGrouping.class)
@Implements(@Interface(iface = IMixinTextureGrouping.class, prefix = "betta$"))
public abstract class MixinTextureGrouping {
    @Unique
    public UpdatedTexturingType betta$updatedTexturingType = UpdatedTexturingType.NONE;
//    @Shadow
//    private List<TextureLayer> textureLayers;
//    @Shadow
//    private TexturingType texturingType;
//    @Shadow
//    private List<TextureGrouping> textureGroupings = new ArrayList<>();
//    @Shadow
//    private void layerGroups(NativeImage compiledImage, List<NativeImage> groupImages) {}
//    @Shadow
//    private void maskAlpha(NativeImage alphaMaskImage, List<NativeImage> groupImages) {}
//    @Shadow
//    private void blendAverage(NativeImage compiledImage, List<NativeImage> groupImages) {}
//    @Shadow
//    private void blendGroupDye(NativeImage baseImage, List<NativeImage> groupImages, int dyeColour) {}

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
                    case INTERSECT_GROUP -> this.intersectAlpha(baseImage, groupImages);
                }
                return baseImage;
            } else {
                return this.applyGroupMerging(groupImages, colouration);
            }
        } else {
            return null;
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
