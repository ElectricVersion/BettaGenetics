package elecvrsn.GeneticBettas.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import elecvrsn.GeneticBettas.block.entity.DisplayTankBlockEntity;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;

public class RenderDisplayTank<T extends DisplayTankBlockEntity> implements BlockEntityRenderer<T> {

    public RenderDisplayTank(BlockEntityRendererProvider.Context context) {
    }
    @Override
    public void render(T blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockPos blockPos = blockEntity.getBlockPos();
        EnhancedBetta entity = blockEntity.getOrCreateDisplayEntity(blockEntity.getLevel(), blockPos);
        if (entity != null) {
            EntityRenderDispatcher renderer = Minecraft.getInstance().getEntityRenderDispatcher();

            matrixStack.pushPose();
            matrixStack.translate(0.5F, 0.25F, 0.5F);
            matrixStack.scale(0.5F, 0.5F, 0.5F);

            renderer.render(entity, 0, 0, 0, 0.0F, partialTicks, matrixStack, buffer, combinedLight);

            matrixStack.popPose();
        }
    }
}
