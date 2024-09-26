package elecvrsn.GeneticBettas.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import elecvrsn.GeneticBettas.items.EnhancedBettaBucket;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class RenderEnhancedBettaBucket extends BlockEntityWithoutLevelRenderer {

    private static final ResourceLocation ERROR_TEXTURE_LOCATION = new ResourceLocation("minecraft:textures/item/tropical_fish_bucket.png");

    public RenderEnhancedBettaBucket(Supplier<BlockEntityRenderDispatcher> blockEntityRenderDispatcherSupplier, Supplier<EntityModelSet> entityModelSetSupplier) {
        super(blockEntityRenderDispatcherSupplier.get(), entityModelSetSupplier.get());
    }

    @Override
    public void renderByItem(@Nonnull ItemStack stack, @Nonnull ItemTransforms.TransformType transformType, @Nonnull PoseStack matrix, @Nonnull MultiBufferSource multiBufferSource, int light, int overlayLight) {
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(ItemStack stack) {
        if (stack.getItem() instanceof EnhancedBettaBucket) {
            boolean isFemale = EnhancedBettaBucket.getIsFemale(stack);
            ResourceLocation resourceLocation = new ResourceLocation(isFemale ? "geneticbettas:textures/item/enhanced_betta_bucket_female.png" : "geneticbettas:textures/item/enhanced_betta_bucket_male.png");
        }
        return ERROR_TEXTURE_LOCATION;
    }
}
