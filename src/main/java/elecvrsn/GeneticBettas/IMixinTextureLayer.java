package elecvrsn.GeneticBettas;

import elecvrsn.GeneticBettas.renderer.texture.UpdatedTexturingType;

public interface IMixinTextureLayer {
    public void setRGB(Integer RGB);
    public void setUpdatedTexturingType(UpdatedTexturingType updatedTexturingType);
    public UpdatedTexturingType getUpdatedTexturingType();

}
