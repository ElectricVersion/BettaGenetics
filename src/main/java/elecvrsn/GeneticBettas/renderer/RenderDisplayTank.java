package elecvrsn.GeneticBettas.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import elecvrsn.GeneticBettas.block.entity.DisplayTankBlockEntity;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import static elecvrsn.GeneticBettas.util.AddonReference.MODID;

public class RenderDisplayTank<T extends DisplayTankBlockEntity> implements BlockEntityRenderer<T> {

    public RenderDisplayTank(BlockEntityRendererProvider.Context context) {
    }
    @Override
    public void render(T blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        renderEntity(blockEntity, partialTicks, matrixStack, buffer, combinedLight);
//        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
//        ModelManager modelManager = Minecraft.getInstance().getModelManager();
//        BakedModel waterModel = modelManager.getModel(new ModelResourceLocation(MODID + ":block/display_tank_water"));
//        blockRenderer.getModelRenderer().renderModel(matrixStack, buffer.getBuffer(RenderType.translucent()), null, waterModel, 1, 1, 1, combinedLight, combinedOverlay, modelData);

    }

    private void renderEntity(T blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight) {
        BlockPos blockPos = blockEntity.getBlockPos();
        Entity entity = blockEntity.getOrCreateDisplayEntity(blockEntity.getLevel());
        if (entity != null) {
            EntityRenderDispatcher renderer = Minecraft.getInstance().getEntityRenderDispatcher();

            matrixStack.pushPose();
            matrixStack.translate(0.5F, 0.25F, 0.5F);
            matrixStack.scale(0.95F, 0.95F, 0.95F);

            renderer.setRenderShadow(false);
            renderer.render(entity, 0, 0, 0, 0.0F, partialTicks, matrixStack, buffer, combinedLight);
            renderer.setRenderShadow(true);

            matrixStack.popPose();
        }
    }
}
