package elecvrsn.GeneticBettas;

import com.mojang.blaze3d.platform.NativeImage;
import mokiyoki.enhancedanimals.entity.util.Colouration;
import mokiyoki.enhancedanimals.renderer.texture.TextureLayer;
import elecvrsn.GeneticBettas.renderer.texture.UpdatedTexturingType;

import java.util.List;

public interface IMixinTextureGrouping {
    public NativeImage applyGroupMerging(List<NativeImage> groupImages, Colouration colouration);
    public void applyLayerSpecifics(TextureLayer layer, Colouration colouration);
    public void setUpdatedTexturingType(UpdatedTexturingType updatedTexturingType);

}
