package elecvrsn.GeneticBettas.renderer.texture;

import elecvrsn.GeneticBettas.IMixinTextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TexturingType;

public class UpdatedTextureGrouping extends TextureGrouping {

    public final static float COLOUR_DEGREE = 1F/255F;

    public UpdatedTextureGrouping(UpdatedTexturingType texturingType) {
        super(TexturingType.NONE);
        IMixinTextureGrouping mixin = ((IMixinTextureGrouping) this);
        mixin.setUpdatedTexturingType(texturingType);
    }

//    public UpdatedTextureGrouping(TexturingType texturingType) {
//        super(texturingType);
//    }

}
