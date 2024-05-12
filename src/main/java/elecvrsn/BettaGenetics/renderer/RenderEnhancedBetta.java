package elecvrsn.BettaGenetics.renderer;

import elecvrsn.BettaGenetics.entity.EnhancedBetta;
import elecvrsn.BettaGenetics.model.ModelEnhancedBetta;
import elecvrsn.BettaGenetics.util.Reference;
import mokiyoki.enhancedanimals.entity.util.Colouration;
import mokiyoki.enhancedanimals.renderer.texture.EnhancedLayeredTexturer;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.util.LayeredTextureCacher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderEnhancedBetta extends MobRenderer<EnhancedBetta, ModelEnhancedBetta<EnhancedBetta>> {

    private static final LayeredTextureCacher textureCache = new LayeredTextureCacher();
    private static final String ENHANCED_BETTA_TEXTURE_LOCATION = "bettagenetics:textures/entities/betta/";
    private static final ResourceLocation ERROR_TEXTURE_LOCATION = new ResourceLocation("bettagenetics:textures/entities/betta/bettabase.png");

    public static final ModelLayerLocation BETTA_LAYER = new ModelLayerLocation(new ResourceLocation(Reference.MODID, "betta"), "betta_layer");
    public RenderEnhancedBetta(EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new ModelEnhancedBetta<>(renderManager.bakeLayer(BETTA_LAYER)), 0.8F);
    }

    @Override
    public ResourceLocation getTextureLocation(EnhancedBetta entity) {
        String s = entity.getTexture();
        Colouration colourRGB = entity.getRgb();

        if (s == null || s.isEmpty() || colourRGB == null) {
            return ERROR_TEXTURE_LOCATION;
        }

        s = s + colourRGB.getRGBStrings();

        ResourceLocation resourcelocation = textureCache.getFromCache(s);

        if (resourcelocation == null) {

            TextureGrouping textureGrouping = entity.getTextureGrouping();

            if (textureGrouping == null || !textureGrouping.isPopulated()) {
                return ERROR_TEXTURE_LOCATION;
            }

            try {
                resourcelocation = new ResourceLocation(s);
                Minecraft.getInstance().getTextureManager().register(resourcelocation, new EnhancedLayeredTexturer(ENHANCED_BETTA_TEXTURE_LOCATION, textureGrouping, colourRGB, 128, 128));

                textureCache.putInCache(s, resourcelocation);
            } catch (IllegalStateException e) {
                return ERROR_TEXTURE_LOCATION;
            }
        }

        return resourcelocation;
    }
}
