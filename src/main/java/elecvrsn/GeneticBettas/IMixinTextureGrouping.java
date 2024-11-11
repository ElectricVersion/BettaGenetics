package elecvrsn.GeneticBettas;

import com.mojang.blaze3d.platform.NativeImage;
import mokiyoki.enhancedanimals.entity.util.Colouration;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TextureLayer;
import mokiyoki.enhancedanimals.renderer.texture.TexturingType;
import mokiyoki.enhancedanimals_backported.renderer.texture.UpdatedTexturingType;

import java.util.List;

public interface IMixinTextureGrouping {
    //    public void applyLayerSpecifics(TextureLayer layer, Colouration colouration);
    //    public void cutoutTextures(NativeImage cutoutImage, List<NativeImage> groupImages);
    public NativeImage applyGroupMerging(List<NativeImage> groupImages, Colouration colouration);
    public void setUpdatedTexturingType(UpdatedTexturingType updatedTexturingType);

}
