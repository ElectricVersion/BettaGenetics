package mokiyoki.enhancedanimals_backported.renderer.texture;

import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TexturingType;

public class UpdatedTextureGrouping extends TextureGrouping {
    private UpdatedTexturingType updatedTexturingType = UpdatedTexturingType.NONE;

    public UpdatedTextureGrouping(UpdatedTexturingType texturingType) {
        super(TexturingType.NONE);
        updatedTexturingType = texturingType;
    }

}
