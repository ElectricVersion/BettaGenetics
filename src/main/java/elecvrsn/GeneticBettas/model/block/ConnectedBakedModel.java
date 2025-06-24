package elecvrsn.GeneticBettas.model.block;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ConnectedBakedModel implements IDynamicBakedModel {
    protected final List<BakedQuad> unculledFaces;
    protected final Map<Direction, List<BakedQuad>> culledFaces;
    protected final boolean hasAmbientOcclusion;
    protected final boolean isGui3d;
    protected final boolean usesBlockLight;
    protected final TextureAtlasSprite particleIcon;
    protected final ItemTransforms transforms;
    protected final ItemOverrides overrides;

    public ConnectedBakedModel(List<BakedQuad> unculledFaces, Map<Direction, List<BakedQuad>> culledFaces, boolean hasAO, boolean usesBlockLight, boolean isGui3d, TextureAtlasSprite particleIcon, ItemTransforms transforms, ItemOverrides overrides) {
        this.unculledFaces = unculledFaces;
        this.culledFaces = culledFaces;
        this.hasAmbientOcclusion = hasAO;
        this.isGui3d = isGui3d;
        this.usesBlockLight = usesBlockLight;
        this.particleIcon = particleIcon;
        this.transforms = transforms;
        this.overrides = overrides;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        return side == null ? this.unculledFaces : this.culledFaces.get(side);
    }

    public boolean useAmbientOcclusion() {
        return this.hasAmbientOcclusion;
    }

    public boolean isGui3d() {
        return this.isGui3d;
    }

    public boolean usesBlockLight() {
        return this.usesBlockLight;
    }

    public boolean isCustomRenderer() {
        return false;
    }

    public @NotNull TextureAtlasSprite getParticleIcon() {
        return this.particleIcon;
    }

    public @NotNull ItemOverrides getOverrides() {
        return this.overrides;
    }

}
