package elecvrsn.GeneticBettas;

import com.mojang.blaze3d.platform.NativeImage;
import mokiyoki.enhancedanimals.entity.util.Colouration;

import java.util.List;

public interface IMixinTextureGrouping {
    public NativeImage applyGroupMerging(List<NativeImage> groupImages, Colouration colouration);
}
