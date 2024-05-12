package elecvrsn.BettaGenetics.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import elecvrsn.BettaGenetics.entity.EnhancedBetta;
import elecvrsn.BettaGenetics.model.modeldata.BettaModelData;
import elecvrsn.BettaGenetics.model.modeldata.BettaPhenotype;
import mokiyoki.enhancedanimals.model.EnhancedAnimalModel;
import mokiyoki.enhancedanimals.model.modeldata.Phenotype;
import mokiyoki.enhancedanimals.model.modeldata.PigModelData;
import mokiyoki.enhancedanimals.model.util.WrappedModelPart;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ModelEnhancedBetta<T extends EnhancedBetta> extends EnhancedAnimalModel<T> {
    private BettaModelData bettaModelData;

    /*** BONES ***/
    private static WrappedModelPart theBetta;
    private WrappedModelPart theBodyFront;
    private WrappedModelPart theBodyBack;
    private WrappedModelPart theHead;
    private WrappedModelPart theFinLeft;
    private WrappedModelPart theFinRight;
    private WrappedModelPart theDorsalFin;
    private WrappedModelPart theBottomFinFront;
    private WrappedModelPart theBottomFinBack;
    private WrappedModelPart theVentralFinLeft;
    private WrappedModelPart theVentralFinRight;
    private WrappedModelPart theTailFin;

    /*** BLOCKS ***/

    private WrappedModelPart bodyFront;
    private WrappedModelPart bodyBack;
    private WrappedModelPart head;
    private WrappedModelPart finLeft;
    private WrappedModelPart finRight;
    private WrappedModelPart dorsalFin;
    private WrappedModelPart bottomFinFront;
    private WrappedModelPart bottomFinBack;
    private WrappedModelPart ventralFinLeft;
    private WrappedModelPart ventralFinRight;
    private WrappedModelPart tailFin;

    public ModelEnhancedBetta(ModelPart modelPart) {
        super(modelPart);
        ModelPart base = modelPart.getChild("base");
        ModelPart bBetta = base.getChild("bBetta");
        ModelPart bBodyF = bBetta.getChild("bBodyF");
        ModelPart bBodyB = bBodyF.getChild("bBodyB");
        ModelPart bHead = bBodyF.getChild("bHead");
        ModelPart bDorsalFin = bBodyF.getChild("bDorsalFin");
        ModelPart bTailFin = bBodyB.getChild("bTailFin");

        theBetta = new WrappedModelPart(base, "bBetta");

        theBodyFront = new WrappedModelPart(bBodyF, "bBodyF");
        bodyFront = new WrappedModelPart("bodyF", bBodyF);
        theBodyFront.addChild(bodyFront);
        theBetta.addChild(theBodyFront);

        theBodyBack = new WrappedModelPart(bBodyB, "bBodyB");
        bodyBack = new WrappedModelPart("bodyB", bBodyB);
        theBodyBack.addChild(bodyBack);
        theBodyFront.addChild(theBodyBack);

        theHead = new WrappedModelPart(bHead, "bHead");
        head = new WrappedModelPart("head", bHead);
        theHead.addChild(head);
        theBodyFront.addChild(theHead);


        theDorsalFin = new WrappedModelPart(bDorsalFin, "bDorsalFin");
        dorsalFin = new WrappedModelPart("dorsalFin", bDorsalFin);
        theDorsalFin.addChild(dorsalFin);
        theBodyFront.addChild(theDorsalFin);


        theTailFin = new WrappedModelPart(bTailFin, "bTailFin");
        tailFin = new WrappedModelPart("tailFin", bTailFin);
        theTailFin.addChild(tailFin);
        theBodyBack.addChild(theTailFin);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition base = meshdefinition.getRoot().addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));
        PartDefinition bBetta = base.addOrReplaceChild("bBetta", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        PartDefinition bBodyFront = bBetta.addOrReplaceChild("bBodyF", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        PartDefinition bBodyBack = bBodyFront.addOrReplaceChild("bBodyB", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 2F, 0F, 0F, 0F));
        PartDefinition bHead = bBodyFront.addOrReplaceChild("bHead", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, -5F, 0F, 0F, 0F));
        PartDefinition bFinLeft = bBodyFront.addOrReplaceChild("bFinL", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        PartDefinition bFinRight = bBodyFront.addOrReplaceChild("bFinR", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        PartDefinition bDorsalFin = bBodyFront.addOrReplaceChild("bDorsalFin", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        PartDefinition bBottomFinFront = bBodyFront.addOrReplaceChild("bBottomFinF", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        PartDefinition bBottomFinBack = bBodyBack.addOrReplaceChild("bBottomFinB", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        PartDefinition bVentralFinLeft = bBodyFront.addOrReplaceChild("bVentralFinL", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        PartDefinition bVentralFinRight = bBodyFront.addOrReplaceChild("bVentralFinR", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        PartDefinition bTailFin = bBodyBack.addOrReplaceChild("bTailFin", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 1F, 0F, 0F, 0F));

        bBodyFront.addOrReplaceChild("bodyF", CubeListBuilder.create()
                        .texOffs(1, 1)
                        .addBox(-1F, -2F, -2F, 2, 4, 4),
                PartPose.ZERO
        );
        bBodyBack.addOrReplaceChild("bodyB", CubeListBuilder.create()
                        .texOffs(3, 12)
                        .addBox(-1F, -2F, 0F, 2, 4, 2),
                PartPose.ZERO
        );
        bHead.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(16, 10)
                        .addBox(-2F, -2F, 0.5F, 4, 4, 4, new CubeDeformation(-0.5F)),
                PartPose.ZERO
        );
        bDorsalFin.addOrReplaceChild("dorsalFin", CubeListBuilder.create()
                        .texOffs(36, -3)
                        .addBox(0F, -7F, 0F, 0, 5, 5),
                PartPose.ZERO
        );

        bTailFin.addOrReplaceChild("tailFin", CubeListBuilder.create()
                        .texOffs(2, 41)
                        .addBox(0F, -6F, 0F, 0, 11, 8),
                PartPose.ZERO
        );

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    protected Phenotype createPhenotype(T enhancedAnimal) {
        return new BettaPhenotype(enhancedAnimal.getSharedGenes().getAutosomalGenes(), enhancedAnimal.getStringUUID().charAt(5));
    }
    private BettaModelData getCreateBettaModelData(T enhancedBetta) {
        return (BettaModelData) getCreateAnimalModelData(enhancedBetta);
    }

    @Override
    protected void setInitialModelData(T enhancedBetta) {
        BettaModelData bettaModelData = new BettaModelData();
        setBaseInitialModelData(bettaModelData, enhancedBetta);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.bettaModelData = getCreateBettaModelData(entityIn);
    }
    private void resetCubes() {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.bettaModelData!=null && this.bettaModelData.getPhenotype() != null) {
            resetCubes();
            super.renderToBuffer(this.bettaModelData, poseStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            Map<String, List<Float>> mapOfScale = new HashMap<>();

            poseStack.pushPose();

            poseStack.scale(1F, 1F, 1F);
            poseStack.translate(0.0F, 0F, 0.0F);

            gaRender(theBetta, mapOfScale, poseStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);

            poseStack.popPose();
        }
    }
}