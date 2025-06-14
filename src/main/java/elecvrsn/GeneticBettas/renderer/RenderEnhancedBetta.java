package elecvrsn.GeneticBettas.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.model.ModelEnhancedBetta;
import elecvrsn.GeneticBettas.util.AddonReference;
import mokiyoki.enhancedanimals.entity.util.Colouration;
import mokiyoki.enhancedanimals.renderer.texture.EnhancedLayeredTexturer;
import mokiyoki.enhancedanimals.renderer.texture.TextureGrouping;
import mokiyoki.enhancedanimals.renderer.util.LayeredTextureCacher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderEnhancedBetta extends MobRenderer<EnhancedBetta, ModelEnhancedBetta<EnhancedBetta>> {

    private static final LayeredTextureCacher textureCache = new LayeredTextureCacher();
    private static final String ENHANCED_BETTA_TEXTURE_LOCATION = "geneticbettas:textures/entities/betta/";
    private static final ResourceLocation ERROR_TEXTURE_LOCATION = new ResourceLocation("geneticbettas:textures/entities/betta/bettabase.png");

    public static final ModelLayerLocation BETTA_LAYER = new ModelLayerLocation(new ResourceLocation(AddonReference.MODID, "betta"), "betta_layer");
    public RenderEnhancedBetta(EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new ModelEnhancedBetta<>(renderManager.bakeLayer(BETTA_LAYER)), 0.2F);
    }

    @Override
    public void render(EnhancedBetta betta, float p_115309_, float p_115310_, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightIn) {
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<EnhancedBetta, ModelEnhancedBetta<EnhancedBetta>>(betta, this, p_115310_, poseStack, multiBufferSource, packedLightIn))) return;
        poseStack.pushPose();
        this.model.attackTime = this.getAttackAnim(betta, p_115310_);

        boolean shouldSit = betta.isPassenger() && (betta.getVehicle() != null && betta.getVehicle().shouldRiderSit());
        this.model.riding = shouldSit;
        this.model.young = betta.isBaby();
        float f = Mth.rotLerp(p_115310_, betta.yBodyRotO, betta.yBodyRot);
        float f1 = Mth.rotLerp(p_115310_, betta.yHeadRotO, betta.yHeadRot);
        float f2 = f1 - f;
        if (shouldSit && betta.getVehicle() instanceof LivingEntity livingEntity) {
            f = Mth.rotLerp(p_115310_, livingEntity.yBodyRotO, livingEntity.yBodyRot);
            f2 = f1 - f;
            float f3 = Mth.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = Mth.lerp(p_115310_, betta.xRotO, betta.getXRot());
        if (isEntityUpsideDown(betta)) {
            f6 *= -1.0F;
            f2 *= -1.0F;
        }

        if (betta.getPose() == Pose.SLEEPING) {
            Direction direction = betta.getBedOrientation();
            if (direction != null) {
                float f4 = betta.getEyeHeight(Pose.STANDING) - 0.1F;
                poseStack.translate((float)(-direction.getStepX()) * f4, 0.0D, (float)(-direction.getStepZ()) * f4);
            }
        }

        float f7 = this.getBob(betta, p_115310_);
        this.setupRotations(betta, poseStack, f7, f, p_115310_);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(betta, poseStack, p_115310_);
        poseStack.translate(0.0D, -1.501F, 0.0D);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && betta.isAlive()) {
            f8 = Mth.lerp(p_115310_, betta.animationSpeedOld, betta.animationSpeed);
            f5 = betta.animationPosition - betta.animationSpeed * (1.0F - p_115310_);
            if (betta.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.model.prepareMobModel(betta, f5, f8, p_115310_);
        this.model.setupAnim(betta, f5, f8, f7, f2, f6);
        RenderType mainRenderType = this.model.renderType(this.getTextureLocation(betta));
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(mainRenderType);
        int i1 = getOverlayCoords(betta, this.getWhiteOverlayProgress(betta, p_115310_));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLightIn, i1, 1.0F, 1.0F, 1.0F, 1.0F);
        VertexConsumer translucentVertexConsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(this.getEyeAndFinLocation(betta)));
        this.model.renderToBuffer(poseStack, translucentVertexConsumer, packedLightIn, i1, 1.0F, 1.0F, 1.0F, 1.0F);

        if (!betta.isSpectator()) {
            for(RenderLayer<EnhancedBetta, ModelEnhancedBetta<EnhancedBetta>> renderlayer : this.layers) {
                renderlayer.render(poseStack, multiBufferSource, packedLightIn, betta, f5, f8, p_115310_, f7, f2, f6);
            }
        }

        poseStack.popPose();
//        super.render(betta, p_115309_, p_115310_, poseStack, multiBufferSource, packedLightIn);
        //Line above is replaced w the following block of code...
        net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(betta, betta.getDisplayName(), this, poseStack, multiBufferSource, packedLightIn, p_115310_);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(betta))) {
            this.renderNameTag(betta, renderNameplateEvent.getContent(), poseStack, multiBufferSource, packedLightIn);
        }
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<>(betta, this, p_115310_, poseStack, multiBufferSource, packedLightIn));
        Entity entity = betta.getLeashHolder();
        if (entity != null) {
            this.renderLeash(betta, p_115310_, poseStack, multiBufferSource, entity);
        }
    }

    public ResourceLocation getEyeAndFinLocation(EnhancedBetta entity) {
        String s = entity.getTexture();
        Colouration colourRGB = entity.getRgb();

        if (s == null || s.isEmpty() || colourRGB == null) {
            return ERROR_TEXTURE_LOCATION;
        }

        s = s + "_fin";

        s = s + colourRGB.getRGBStrings();

        ResourceLocation resourcelocation = textureCache.getFromCache(s);

        if (resourcelocation == null) {

            TextureGrouping textureGrouping = entity.getFinGroup();

            if (textureGrouping == null || !textureGrouping.isPopulated()) {
                return ERROR_TEXTURE_LOCATION;
            }

            try {
                resourcelocation = new ResourceLocation(s);
                EnhancedLayeredTexturer layeredTexture = new EnhancedLayeredTexturer(ENHANCED_BETTA_TEXTURE_LOCATION, textureGrouping, entity.colouration, 128,128);
                Minecraft.getInstance().getTextureManager().register(resourcelocation, layeredTexture);

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

    @Override
    protected boolean shouldShowName(EnhancedBetta entity) {
        return (!entity.isInPhotoMode) && super.shouldShowName(entity);
    }
}
