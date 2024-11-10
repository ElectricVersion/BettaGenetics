package mokiyoki.enhancedanimals_backported.renderer.texture;

//TThis is the necessary pig/chicken update code that was added for the TexturingTypes enum. Temporarily copied into here for compatibility
public enum UpdatedTexturingType {

    //Group texturing types

    MASK_GROUP, //the grouping used for alpha masking layering. The first image(grouped images) in the group is treated as the mask the other group will be applied against it
    CUTOUT_GROUP, //a layer that merges its textures to cutout the textures in its sub-group
    APPLY_RGB, //a layer specific type, used to apply an RGB value to an individual texture
    APPLY_RGBA, //a layer specific type, used to apply an RGB value to an individual texture

    /*******/

    NONE //no special changes
}
