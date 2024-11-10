package elecvrsn.GeneticBettas.mixins;

import elecvrsn.GeneticBettas.IMixinEnhancedAnimalAbstract;
import elecvrsn.GeneticBettas.IMixinTextureLayer;
import mokiyoki.enhancedanimals.entity.EnhancedAnimalAbstract;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.texture.TextureLayer;
import mokiyoki.enhancedanimals_backported.renderer.texture.UpdatedTextureLayer;
import mokiyoki.enhancedanimals_backported.renderer.texture.UpdatedTexturingType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnhancedAnimalAbstract.class)
public class MixinEnhancedAnimalAbstract implements IMixinEnhancedAnimalAbstract {
    @Shadow
    protected List<String> texturesIndexes = new ArrayList<>();
//    @Final
//    @Shadow
//    protected static String CACHE_DELIMITER;

    public void addTextureToAnimalTextureGrouping(TextureGrouping textureGroup, UpdatedTexturingType texturingType, String texture, String textureID, Integer RGB) {
        TextureLayer textureLayer = new UpdatedTextureLayer(texturingType, texture);
        ((IMixinTextureLayer)textureLayer).setRGB(RGB);
        textureGroup.addTextureLayers(textureLayer);
        this.texturesIndexes.add(textureID);
        this.texturesIndexes.add(String.valueOf(RGB));
        this.texturesIndexes.add("-");
    }

    public void addTextureToAnimalTextureGrouping(TextureGrouping textureGroup, String texture, boolean hasTexture) {
        if (hasTexture) textureGroup.addTextureLayers(new TextureLayer(texture));
        this.texturesIndexes.add(String.valueOf(hasTexture?0:1));
        this.texturesIndexes.add("-");
    }
}
