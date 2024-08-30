package elecvrsn.GeneticBettas.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import elecvrsn.GeneticBettas.entity.EnhancedBettaEgg;
import elecvrsn.GeneticBettas.model.ModelEnhancedBettaEgg;
import elecvrsn.GeneticBettas.util.AddonReference;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderEnhancedBettaEgg extends EntityRenderer<EnhancedBettaEgg> {
    private static final ResourceLocation ENHANCED_BETTA_EGG = new ResourceLocation("geneticbettas:textures/entities/betta_egg/betta_egg.png");
    private final ModelEnhancedBettaEgg<EnhancedBettaEgg> model;
    public static final ModelLayerLocation BETTA_EGG_LAYER = new ModelLayerLocation(new ResourceLocation(AddonReference.MODID, "betta_egg"), "betta_egg_layer");

    public RenderEnhancedBettaEgg(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelEnhancedBettaEgg<>(context.bakeLayer(BETTA_EGG_LAYER), RenderType::entityTranslucent);
    }

    @Override
    public void render(EnhancedBettaEgg enhancedBettaEgg, float p_114486_, float p_114487_, PoseStack pose, MultiBufferSource p_114489_, int packedLight) {
        this.model.renderToBuffer(pose, p_114489_.getBuffer(RenderType.entityTranslucent(ENHANCED_BETTA_EGG)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.model.setupAnim(enhancedBettaEgg, 0, 0, 0, 0, 0);
    }

    @Override
    public ResourceLocation getTextureLocation(EnhancedBettaEgg p_114482_) {
        return ENHANCED_BETTA_EGG;
    }
}
