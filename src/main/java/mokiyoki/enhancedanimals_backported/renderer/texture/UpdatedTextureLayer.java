package mokiyoki.enhancedanimals_backported.renderer.texture;

import mokiyoki.enhancedanimals.renderer.texture.TextureLayer;

public class UpdatedTextureLayer extends TextureLayer {
    private UpdatedTexturingType updatedTexturingType;

    public UpdatedTextureLayer(UpdatedTexturingType texturingType, String texture) {
        super(texture);
        updatedTexturingType = texturingType;
    }
}
