package elecvrsn.GeneticBettas.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import elecvrsn.GeneticBettas.entity.EnhancedBettaEgg;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class ModelEnhancedBettaEgg<T extends EnhancedBettaEgg> extends EntityModel<T> {
    private final ModelPart root;
    private final ModelPart egg;

    public ModelEnhancedBettaEgg(ModelPart modelPart, Function<ResourceLocation, RenderType> renderType) {
        super(renderType);
        this.root = modelPart;
        this.egg = modelPart.getChild("egg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("egg",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.0F, 0F, -1.0F, 2, 2, 2)
                , PartPose.ZERO
        );
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0F, 0.0D);
        poseStack.scale(1.0F, 1.0F, 1.0F);
        this.egg.render(poseStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        poseStack.popPose();
    }
}
