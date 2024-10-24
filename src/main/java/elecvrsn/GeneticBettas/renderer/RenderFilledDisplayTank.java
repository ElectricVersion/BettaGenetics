package elecvrsn.GeneticBettas.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import elecvrsn.GeneticBettas.block.FilledDisplayTankBlock;
import elecvrsn.GeneticBettas.block.entity.FilledDisplayTankBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

public class RenderFilledDisplayTank<T extends FilledDisplayTankBlockEntity> implements BlockEntityRenderer<T> {

    public RenderFilledDisplayTank(BlockEntityRendererProvider.Context context) {
    }
    @Override
    public void render(T blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        renderEntity(blockEntity, partialTicks, matrixStack, buffer, combinedLight);
    }

    private void renderEntity(T blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight) {
        Entity entity = blockEntity.getOrCreateDisplayEntity(blockEntity.getLevel());
        if (entity != null) {
            EntityRenderDispatcher renderer = Minecraft.getInstance().getEntityRenderDispatcher();

            matrixStack.pushPose();
            matrixStack.translate(0.5F, 0.25F, 0.5F);
            Direction facing = FilledDisplayTankBlock.getFacing(blockEntity.getBlockState());
            //TODO: Convert this to a switch statement that just picks from 4 predefined quaternions
            matrixStack.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, -facing.toYRot(), 0)));
//            matrixStack.scale(0.95F, 0.95F, 0.95F);

            renderer.setRenderShadow(false);
            renderer.render(entity, 0, 0, 0, 0.0F, partialTicks, matrixStack, buffer, combinedLight);
            renderer.setRenderShadow(true);

            matrixStack.popPose();
        }
    }
}
