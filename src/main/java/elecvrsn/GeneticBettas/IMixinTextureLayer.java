package elecvrsn.GeneticBettas;

import mokiyoki.enhancedanimals_backported.renderer.texture.UpdatedTexturingType;

public interface IMixinTextureLayer {
    public void setRGB(Integer RGB);
    public void setUpdatedTexturingType(UpdatedTexturingType updatedTexturingType);
    public UpdatedTexturingType getUpdatedTexturingType();

}
