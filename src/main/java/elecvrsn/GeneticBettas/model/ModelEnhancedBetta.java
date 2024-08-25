package elecvrsn.GeneticBettas.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.model.modeldata.BettaModelData;
import elecvrsn.GeneticBettas.model.modeldata.BettaPhenotype;
import mokiyoki.enhancedanimals.model.EnhancedAnimalModel;
import mokiyoki.enhancedanimals.model.modeldata.AnimalModelData;
import mokiyoki.enhancedanimals.model.modeldata.Phenotype;
import mokiyoki.enhancedanimals.model.util.ModelHelper;
import mokiyoki.enhancedanimals.model.util.WrappedModelPart;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.particles.ParticleTypes;
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
    private WrappedModelPart theBetta;
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
    private WrappedModelPart theGillLeft;
    private WrappedModelPart theGillRight;
    private WrappedModelPart theTailFin;

    /*** BLOCKS ***/

    private WrappedModelPart bodyFront;
    private WrappedModelPart bodyBack;
    private WrappedModelPart head;
    private WrappedModelPart lips;
    private WrappedModelPart eyes;
    private WrappedModelPart finLeft;
    private WrappedModelPart finRight;
    private WrappedModelPart dorsalFin;
    private WrappedModelPart bottomFinFront;
    private WrappedModelPart bottomFinBack;
    private WrappedModelPart ventralFinLeft;
    private WrappedModelPart ventralFinRight;
    private WrappedModelPart gillLeft;
    private WrappedModelPart gillRight;
    private WrappedModelPart tailFin;

    public ModelEnhancedBetta(ModelPart modelPart) {
        super(modelPart);
        ModelPart base = modelPart.getChild("base");
        ModelPart bBetta = base.getChild("bBetta");
        ModelPart bBodyF = bBetta.getChild("bBodyF");
        ModelPart bBodyB = bBodyF.getChild("bBodyB");
        ModelPart bHead = bBodyF.getChild("bHead");
        ModelPart bGillL = bHead.getChild("bGillL");
        ModelPart bGillR = bHead.getChild("bGillR");
        ModelPart bFinL = bBodyF.getChild("bFinL");
        ModelPart bFinR = bBodyF.getChild("bFinR");
        ModelPart bDorsalFin = bBodyF.getChild("bDorsalFin");
        ModelPart bBottomFinF = bBodyF.getChild("bBottomFinF");
        ModelPart bBottomFinB = bBodyB.getChild("bBottomFinB");
        ModelPart bVentralFinL = bBodyF.getChild("bVentralFinL");
        ModelPart bVentralFinR = bBodyF.getChild("bVentralFinR");
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
        lips = new WrappedModelPart("lips", bHead);
        eyes = new WrappedModelPart("eyes", bHead);
        theHead.addChild(head);
        theHead.addChild(lips);
        theHead.addChild(eyes);

        theGillLeft = new WrappedModelPart(bGillL, "bGillL");
        theGillRight = new WrappedModelPart(bGillR, "bGillR");

        gillLeft = new WrappedModelPart("gillL", bGillL);
        gillRight = new WrappedModelPart("gillR", bGillR);

        theGillLeft.addChild(gillLeft);
        theGillRight.addChild(gillRight);

        theHead.addChild(theGillLeft);
        theHead.addChild(theGillRight);
        theBodyFront.addChild(theHead);

        theFinLeft = new WrappedModelPart(bFinL, "bFinL");
        finLeft = new WrappedModelPart("finL", bFinL);
        theFinLeft.addChild(finLeft);
        theBodyFront.addChild(theFinLeft);

        theFinRight = new WrappedModelPart(bFinR, "bFinR");
        finRight = new WrappedModelPart("finR", bFinR);
        theFinRight.addChild(finRight);
        theBodyFront.addChild(theFinRight);

        theDorsalFin = new WrappedModelPart(bDorsalFin, "bDorsalFin");
        dorsalFin = new WrappedModelPart("dorsalFin", bDorsalFin);
        theDorsalFin.addChild(dorsalFin);
        theBodyFront.addChild(theDorsalFin);

        theBottomFinFront = new WrappedModelPart(bBottomFinF, "bBottomFinF");
        bottomFinFront = new WrappedModelPart("bottomFinF", bBottomFinF);
        theBottomFinFront.addChild(bottomFinFront);
        theBodyFront.addChild(theBottomFinFront);

        theBottomFinBack = new WrappedModelPart(bBottomFinB, "bBottomFinB");
        bottomFinBack = new WrappedModelPart("bottomFinB", bBottomFinB);
        theBottomFinBack.addChild(bottomFinBack);
        theBodyBack.addChild(theBottomFinBack);

        theVentralFinLeft = new WrappedModelPart(bVentralFinL, "bVentralFinL");
        ventralFinLeft = new WrappedModelPart("ventralFinL", bVentralFinL);
        theVentralFinLeft.addChild(ventralFinLeft);
        theBodyFront.addChild(theVentralFinLeft);

        theVentralFinRight = new WrappedModelPart(bVentralFinR, "bVentralFinR");
        ventralFinRight = new WrappedModelPart("ventralFinR", bVentralFinR);
        theVentralFinRight.addChild(ventralFinRight);
        theBodyFront.addChild(theVentralFinRight);

        theTailFin = new WrappedModelPart(bTailFin, "bTailFin");
        tailFin = new WrappedModelPart("tailFin", bTailFin);
        theTailFin.addChild(tailFin);
        theBodyBack.addChild(theTailFin);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition base = meshdefinition.getRoot().addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));
        PartDefinition bBetta = base.addOrReplaceChild("bBetta", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        PartDefinition bBodyFront = bBetta.addOrReplaceChild("bBodyF", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, -2F, 0F, 0F, 0F));
        PartDefinition bBodyBack = bBodyFront.addOrReplaceChild("bBodyB", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 2F, 0F, 0F, 0F));
        PartDefinition bHead = bBodyFront.addOrReplaceChild("bHead", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 4F, -1.5F, 0F, 0F, 0F));
        PartDefinition bGillLeft = bHead.addOrReplaceChild("bGillL", CubeListBuilder.create(), PartPose.offsetAndRotation(1.25F, -2F, -0.25F, 0F, -Mth.HALF_PI, 0F));
        PartDefinition bGillRight = bHead.addOrReplaceChild("bGillR", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.25F, -2F, -0.25F, 0F, Mth.HALF_PI, 0F));

        PartDefinition bFinLeft = bBodyFront.addOrReplaceChild("bFinL", CubeListBuilder.create(), PartPose.offsetAndRotation(1F, 4.5F, -1F, 0F, Mth.HALF_PI*0.5F, 0F));
        PartDefinition bFinRight = bBodyFront.addOrReplaceChild("bFinR", CubeListBuilder.create(), PartPose.offsetAndRotation(-1F, 4.5F, -1F, 0F, -Mth.HALF_PI*0.5F, 0F));
        PartDefinition bDorsalFin = bBodyFront.addOrReplaceChild("bDorsalFin", CubeListBuilder.create(), PartPose.offsetAndRotation(0.025F, 2F, 0F, 0F, 0F, 0F));
        PartDefinition bBottomFinFront = bBodyFront.addOrReplaceChild("bBottomFinF", CubeListBuilder.create(), PartPose.offsetAndRotation(0.025F, 2F, -1F, 0F, 0F, 0F));
        PartDefinition bBottomFinBack = bBodyBack.addOrReplaceChild("bBottomFinB", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.025F, 1F, 1F, 0F, 0F, 0F));
        PartDefinition bVentralFinLeft = bBodyFront.addOrReplaceChild("bVentralFinL", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, 2F, -1F, 0F, Mth.HALF_PI*0.15F, 0F));
        PartDefinition bVentralFinRight = bBodyFront.addOrReplaceChild("bVentralFinR", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 2F, -1F, 0F, -Mth.HALF_PI*0.15F, 0F));
        PartDefinition bTailFin = bBodyBack.addOrReplaceChild("bTailFin", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 4F, 1F, 0F, 0F, 0F));

        bBodyFront.addOrReplaceChild("bodyF", CubeListBuilder.create()
                        .texOffs(1, 1)
                        .addBox(-1F, 2F, -2F, 2, 4, 4),
                PartPose.ZERO
        );
        bBodyBack.addOrReplaceChild("bodyB", CubeListBuilder.create()
                        .texOffs(3, 12)
                        .addBox(-1F, 2F, 0F, 2, 4, 2),
                PartPose.ZERO
        );
        bHead.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(16, 10)
                        .addBox(-2F, -2F, -3F, 4, 4, 4, new CubeDeformation(-0.5F)),
                PartPose.ZERO
        );

        bHead.addOrReplaceChild("lips", CubeListBuilder.create()
                        .texOffs(20, 2)
                        .addBox(-1F, -1F, -1F, 2, 2, 2, new CubeDeformation(-0.375F)),
                PartPose.offsetAndRotation(0F,0.125F,-2.5F,-Mth.HALF_PI*0.05F,0F,0F)
        );
        bHead.addOrReplaceChild("eyes", CubeListBuilder.create()
                        .texOffs(46, 21)
                        .addBox(1.51F, -2F, -2F, 0, 4, 4, new CubeDeformation(0F,-1.25F, -1.25F))
                        .texOffs(29, 21)
                        .addBox(-1.51F, -2F, -2F, 0, 4, 4, new CubeDeformation(0F,-1.25F, -1.25F)),
                PartPose.offset(0F, 0F, -1.375F)
        );

        bGillLeft.addOrReplaceChild("gillL", CubeListBuilder.create()
                        .texOffs(53, 49)
                        .addBox(-0.5F, 0F, 0F, 4, 4, 0, new CubeDeformation(-0.5F,-0.5F, 0F)),
                PartPose.ZERO
        );

        bGillRight.addOrReplaceChild("gillR", CubeListBuilder.create()
                        .texOffs(53, 57)
                        .addBox(-3.5F, 0F, 0F, 4, 4, 0, new CubeDeformation(-0.5F,-0.5F, 0F)),
                PartPose.ZERO
        );


        bFinLeft.addOrReplaceChild("finL", CubeListBuilder.create()
                        .texOffs(2, 16)
                        .addBox(0F, -2.5F, -1F, 0, 5, 6),
                PartPose.ZERO
        );

        bFinRight.addOrReplaceChild("finR", CubeListBuilder.create()
                        .texOffs(2, 24)
                        .addBox(0F, -2.5F, -1F, 0, 5, 6),
                PartPose.ZERO
        );

        bDorsalFin.addOrReplaceChild("dorsalFin", CubeListBuilder.create()
                        .texOffs(36, -3)
                        .addBox(0F, -5F, 0F, 0, 5, 5),
                PartPose.ZERO
        );

        bBottomFinFront.addOrReplaceChild("bottomFinF", CubeListBuilder.create()
                        .texOffs(36, 10)
                        .addBox(0F, 0F, 0F, 0, 4, 5),
                PartPose.offset(0F, 4F, 0F)
        );
        bBottomFinBack.addOrReplaceChild("bottomFinB", CubeListBuilder.create()
                        .texOffs(50, 10)
                        .addBox(0F, 1F, 4F, 0, 5, 5),
                PartPose.offset(0F, 3F, -4F)
        );

        bVentralFinLeft.addOrReplaceChild("ventralFinL", CubeListBuilder.create()
                        .texOffs(18, 18)
                        .addBox(0F, 0F, -2F, 0, 4, 4),
                PartPose.offset(0F, 4F, 0F)
        );

        bVentralFinRight.addOrReplaceChild("ventralFinR", CubeListBuilder.create()
                        .texOffs(18, 26)
                        .addBox(0F, 0F, -2F, 0, 4, 4),
                PartPose.offset(0F, 4F, 0F)
        );

        bTailFin.addOrReplaceChild("tailFin", CubeListBuilder.create()
                        .texOffs(2, 41)
                        .addBox(0F, -6F, 1F, 0, 11, 8),
                PartPose.offset(0F, 0F, -1F)
        );

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    protected Phenotype createPhenotype(T enhancedAnimal) {
        return new BettaPhenotype(enhancedAnimal.getGenes().getAutosomalGenes(), enhancedAnimal.getStringUUID().charAt(5), enhancedAnimal.getOrSetIsFemale());
    }
    private BettaModelData getCreateBettaModelData(T enhancedBetta) {
        return (BettaModelData) getCreateAnimalModelData(enhancedBetta);
    }

    @Override
    protected void setInitialModelData(T enhancedBetta) {
        BettaModelData bettaModelData = new BettaModelData();
        setBaseInitialModelData(bettaModelData, enhancedBetta);
    }

    private void resetCubes() {
//        gillLeft.hide();
//        gillRight.hide();
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.bettaModelData!=null && this.bettaModelData.getPhenotype() != null) {
            resetCubes();
            super.renderToBuffer(this.bettaModelData, poseStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            Map<String, List<Float>> mapOfScale = new HashMap<>();
            BettaPhenotype betta = this.bettaModelData.getPhenotype();

            float finScaleMult = betta.isFemale ? 0.75F : 1F;
            List<Float> pectoralFinScalings = ModelHelper.createScalings(1F, betta.dumbo ? 0.8F : 0.75F, 0.75F, 0F, 0F, 0F);
            mapOfScale.put("finL", pectoralFinScalings);
            mapOfScale.put("finR", pectoralFinScalings);
            List<Float> dorsalFinScalings = ModelHelper.createScalings(1F, betta.isFemale ? 0.625F : 1F, finScaleMult*betta.dorsalWidth, 0F, 0F, 0F);
            mapOfScale.put("dorsalFin", dorsalFinScalings);
//            List<Float> tailFinScalings = ModelHelper.createScalings(1F, finScaleMult, finScaleMult, 0F, 0F, 0F);
            mapOfScale.put("tailFin", ModelHelper.createScalings(1F, 1F, 1F, 0F, 0F, betta.isFemale ? -1F/16F : 0F));
            List<Float> ventralFinScalings = ModelHelper.createScalings(1F, betta.isFemale ? 0.725F : 1F, finScaleMult, 0F, 0F, 0F);
            mapOfScale.put("ventralFinL", ventralFinScalings);
            mapOfScale.put("ventralFinR", ventralFinScalings);
//            List<Float> bottomFinScalings = ModelHelper.createScalings(1F, finScaleMult, betta.isFemale ? 0.875F : 1F, 0F, 0F, 0F);
            mapOfScale.put("bottomFinF", ModelHelper.createScalings(1F, finScaleMult, betta.isFemale ? 0.875F : 1F, 0F, 0F, 0F));
            mapOfScale.put("bottomFinB", ModelHelper.createScalings(1F, betta.isFemale ? 0.8F : 1F, betta.isFemale ? 0.85F : 1F, 0F, 0F, 0F));
            mapOfScale.put("bBodyF", ModelHelper.createScalings(1F, betta.isFemale ? 1F : 0.925F, 1F, 0F, 0F, 0F));

            poseStack.pushPose();

            poseStack.scale(1F, 1F, 1F);
            poseStack.translate(0.0F, 0F, 0.0F);

            gaRender(theBetta, mapOfScale, poseStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);

            poseStack.popPose();
        }
    }


    private void setupInitialAnimationValues(AnimalModelData data, float netHeadYaw, float headPitch, BettaPhenotype axolotl) {
        Map<String, Vector3f> map = data.offsets;
        if (map.isEmpty()) {
            this.theBetta.setRotation(0.0F, 0.0F, 0.0F);
            this.theHead.setRotation(0.0F, 0.0F, 0.0F);
            this.theBodyBack.setRotation(0.0F, 0.0F, 0.0F);
            this.theFinLeft.setRotation(0.0F, Mth.HALF_PI*0.5F, 0.0F);
            this.theFinRight.setRotation(0.0F, -Mth.HALF_PI*0.5F, 0.0F);
            this.theTailFin.setRotation(0.0F, 0.0F, 0.0F);
            this.theGillLeft.setRotation(0.0F, -Mth.HALF_PI, 0.0F);
            this.theGillRight.setRotation(0.0F, Mth.HALF_PI, 0.0F);
        } else {
            this.setRotationFromVector(this.theBetta, map.get("bBetta"));
            this.setRotationFromVector(this.theHead, map.get("bHead"));
            this.setRotationFromVector(this.theBodyBack, map.get("bBodyB"));
            this.setRotationFromVector(this.theFinLeft, map.get("bFinL"));
            this.setRotationFromVector(this.theFinRight, map.get("bFinR"));
            this.setRotationFromVector(this.theGillLeft, map.get("bGillL"));
            this.setRotationFromVector(this.theGillRight, map.get("bGillR"));
            this.setRotationFromVector(this.theTailFin, map.get("bTailFin"));
        }
    }

    protected void saveAnimationValues(AnimalModelData data) {
        Map<String, Vector3f> map = data.offsets;
        map.put("bBetta", this.getRotationVector(this.theBetta));
        map.put("bHead", this.getRotationVector(this.theHead));
        map.put("bBodyB", this.getRotationVector(this.theBodyBack));
        map.put("bFinL", this.getRotationVector(this.theFinLeft));
        map.put("bFinR", this.getRotationVector(this.theFinRight));
        map.put("bGillL", this.getRotationVector(this.theGillLeft));
        map.put("bGillR", this.getRotationVector(this.theGillRight));
        map.put("bTailFin", this.getRotationVector(this.theTailFin));
    }
    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.bettaModelData = getCreateBettaModelData(entityIn);
        if (this.bettaModelData != null && this.bettaModelData.getPhenotype() != null) {
            BettaPhenotype betta = this.bettaModelData.getPhenotype();
            this.setupInitialAnimationValues(this.bettaModelData, netHeadYaw, headPitch, betta);
            boolean isMoving = entityIn.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7D || entityIn.xOld != entityIn.getX() || entityIn.zOld != entityIn.getZ();
            float yDelta = isMoving ? -(float)entityIn.getDeltaMovement().y * (float)Mth.HALF_PI : 0F;

            if (this.bettaModelData.bubblingTimer <= ageInTicks) {
                bettaModelData.bubblingTimer = (int)ageInTicks + entityIn.getRandom().nextInt(600) + 30;
            }
            else if (this.bettaModelData.bubblingTimer <= ageInTicks + 10) {
                setupBubbleAnimation(ageInTicks, headPitch);
            }

            if (!entityIn.isAnimalSleeping()) {
                if (!isMoving && entityIn.isInWater()) {
                    this.setupIdleAnimation(ageInTicks, headPitch);
                } else {
                    this.setupSwimmingAnimation(ageInTicks, headPitch, yDelta);
                }
            }
            this.setupFlareAnimation(entityIn.getIsAngry());

            this.saveAnimationValues(this.bettaModelData);
        }
    }

    private void setupSwimmingAnimation(float ageInTicks, float headPitch, float yDelta) {
        float f = ageInTicks * 0.33F;
        float f1 = Mth.sin(f);
        float f2 = Mth.sin(f*2F);
        float f3 = Mth.cos(f*2F);
        this.theBetta.setXRot(this.lerpTo(0.25F, this.theBetta.getXRot(), yDelta * (yDelta > 0F ? 2.5F : 10F)));
        this.theHead.setYRot(f1 * (float)Mth.HALF_PI*0.10F);
        this.theBodyBack.setYRot(f1 * (float)Mth.HALF_PI*0.075F);
        this.theTailFin.setYRot(f1 * (float)Mth.HALF_PI*0.05F);
        this.theFinLeft.setYRot(Mth.HALF_PI*0.5F + (f2 * (float)Mth.HALF_PI*0.15F));
        this.theFinRight.setYRot(-Mth.HALF_PI*0.5F + (f3 * (float)Mth.HALF_PI*0.15F));
    }

    private void setupBubbleAnimation(float ageInTicks, float headPitch) {
        if (this.bettaModelData != null) {
            this.bettaModelData.isBubbling = true;
        }
    }

    private void setupFlareAnimation(boolean open) {
        if (theGillLeft.getYRot() < (-Mth.HALF_PI)+0.025F) { //Only check left gill because they move together
            gillLeft.show(false);
            gillRight.show(false);
        }
        else {
            gillLeft.show(true);
            gillRight.show(true);
        }
        this.theGillLeft.setYRot(this.lerpTo(0.25F, this.theGillLeft.getYRot(), open ? 0F : -Mth.HALF_PI));
        this.theGillRight.setYRot(this.lerpTo(0.25F, this.theGillRight.getYRot(), open ? 0F : Mth.HALF_PI));
    }

    private void setupIdleAnimation(float ageInTicks, float headPitch) {
        float f = ageInTicks * 0.33F;
        float f2 = Mth.sin(f*1.75F);
        float f3 = Mth.cos(f*1.75F);
        this.theBetta.setXRot(this.lerpTo(0.5F, this.theBetta.getXRot(), 0F));
        this.theFinLeft.setYRot(Mth.HALF_PI*0.5F + (f2 * (float)Mth.HALF_PI*0.15F));
        this.theFinRight.setYRot(-Mth.HALF_PI*0.5F + (f3 * (float)Mth.HALF_PI*0.15F));
    }
}