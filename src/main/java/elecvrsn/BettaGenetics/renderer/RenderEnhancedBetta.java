package elecvrsn.BettaGenetics.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import elecvrsn.BettaGenetics.entity.EnhancedBetta;
import elecvrsn.BettaGenetics.model.ModelEnhancedBetta;
import elecvrsn.BettaGenetics.util.AddonReference;
import mokiyoki.enhancedanimals.entity.util.Colouration;
import mokiyoki.enhancedanimals.renderer.texture.DrawnTexture;
import mokiyoki.enhancedanimals.renderer.texture.EnhancedLayeredTexturer;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.util.LayeredTextureCacher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderEnhancedBetta extends MobRenderer<EnhancedBetta, ModelEnhancedBetta<EnhancedBetta>> {

    private static final LayeredTextureCacher textureCache = new LayeredTextureCacher();
    private static final String ENHANCED_BETTA_TEXTURE_LOCATION = "bettagenetics:textures/entities/betta/";
    private static final ResourceLocation ERROR_TEXTURE_LOCATION = new ResourceLocation("bettagenetics:textures/entities/betta/bettabase.png");

    public static final ModelLayerLocation BETTA_LAYER = new ModelLayerLocation(new ResourceLocation(AddonReference.MODID, "betta"), "betta_layer");
    public RenderEnhancedBetta(EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new ModelEnhancedBetta<>(renderManager.bakeLayer(BETTA_LAYER)), 0.8F);
    }

    @Override
    public void render(EnhancedBetta betta, float p_115456_, float p_115457_, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightIn) {
        //Render Normal
        super.render(betta, p_115456_, p_115457_, poseStack, multiBufferSource, packedLightIn);
        //Render Iridescence
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(this.model.renderType(this.getIriLocation(betta)));
        int i = getOverlayCoords(betta, this.getWhiteOverlayProgress(betta, p_115457_));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLightIn, i, 1.0F, 1.0F, 1.0F, false ? 0.15F : 1.0F);

    }

    public ResourceLocation getIriLocation(EnhancedBetta entity) {
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
                EnhancedLayeredTexturer layeredTexture = new EnhancedLayeredTexturer(ENHANCED_BETTA_TEXTURE_LOCATION, textureGrouping, entity.colouration, 64, 64);
                Minecraft.getInstance().getTextureManager().register(resourcelocation, layeredTexture);
                // Wonder if I can compile a second texture group to use as an alpha mask?
                DrawnTexture texture = new DrawnTexture("eanimod:textures/entities/axolotl/blank.png", layeredTexture, null);
                Minecraft.getInstance().getTextureManager().register(resourcelocation, texture);
                textureCache.putInCache(s, resourcelocation);

            } catch (IllegalStateException e) {
                return ERROR_TEXTURE_LOCATION;
            }
        }

        return resourcelocation;
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
