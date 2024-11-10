package elecvrsn.GeneticBettas;

import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals_backported.renderer.texture.UpdatedTexturingType;

public interface IMixinEnhancedAnimalAbstract {
    public void addTextureToAnimalTextureGrouping(TextureGrouping textureGroup, UpdatedTexturingType texturingType, String texture, String textureID, Integer RGB);
    public void addTextureToAnimalTextureGrouping(TextureGrouping textureGroup, String texture, boolean hasTexture);
}
