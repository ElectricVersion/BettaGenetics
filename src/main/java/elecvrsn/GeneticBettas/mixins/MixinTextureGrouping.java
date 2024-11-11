package elecvrsn.GeneticBettas.mixins;

import com.mojang.blaze3d.platform.NativeImage;
import elecvrsn.GeneticBettas.IMixinTextureGrouping;
import mokiyoki.enhancedanimals.entity.util.Colouration;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TextureLayer;
import mokiyoki.enhancedanimals.renderer.texture.TexturingType;
import mokiyoki.enhancedanimals_backported.renderer.texture.UpdatedTextureGrouping;
import mokiyoki.enhancedanimals_backported.renderer.texture.UpdatedTexturingType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.List;

@Mixin(TextureGrouping.class)
@Implements(@Interface(iface = IMixinTextureGrouping.class, prefix = "betta$"))
public abstract class MixinTextureGrouping {
    private UpdatedTexturingType updatedTexturingType = UpdatedTexturingType.NONE;
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
    @Intrinsic(displace = true)
    public NativeImage betta$applyGroupMerging(List<NativeImage> groupImages, Colouration colouration) {
        if (!groupImages.isEmpty()) {
            if (updatedTexturingType != UpdatedTexturingType.NONE && updatedTexturingType != null) {
                NativeImage baseImage = (NativeImage)groupImages.remove(0);
                switch (this.updatedTexturingType) {
                    case MASK_GROUP:
                        this.layerGroups(baseImage, groupImages);
                        break;
                    case CUTOUT_GROUP:
                        this.layerGroups(baseImage, groupImages);
                        break;
                }
                return baseImage;
            } else {
                return this.applyGroupMerging(groupImages, colouration);
            }
        } else {
            return null;
        }
//        return this.applyGroupMerging(groupImages, colouration);
    }

}
