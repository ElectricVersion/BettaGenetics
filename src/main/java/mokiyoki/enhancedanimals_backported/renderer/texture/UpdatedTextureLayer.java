package mokiyoki.enhancedanimals_backported.renderer.texture;

import elecvrsn.GeneticBettas.IMixinTextureGrouping;
import elecvrsn.GeneticBettas.IMixinTextureLayer;
import mokiyoki.enhancedanimals.renderer.texture.TextureLayer;

public class UpdatedTextureLayer extends TextureLayer {

    public UpdatedTextureLayer(UpdatedTexturingType texturingType, String texture) {
        super(texture);
        ((IMixinTextureLayer)this).setUpdatedTexturingType(texturingType);
    }
}
