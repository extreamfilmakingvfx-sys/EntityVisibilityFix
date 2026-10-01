package com.extream.entityvisibilityfix.mixin;

import com.extream.entityvisibilityfix.EntityVisibilityFixClient;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Derivative/Iris compatibility hook for Minecraft 26.3 framed maps.
 *
 * A map in an item frame is submitted through ItemFrameRenderer -> MapRenderer,
 * while normal items use the regular item renderer.  Keep the map/text vertex
 * layout intact (unlike v0.4) and only change the depth variant for the framed
 * submission.  This deliberately does not touch held maps.
 */
@Mixin(MapRenderer.class)
public abstract class MapRendererMixin {
    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;text(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"
        ),
        require = 0
    )
    private RenderType evf$mapType(Identifier texture) {
        // textSeeThrough keeps the same TEXT vertex format.  That is critical:
        // substituting an entity RenderType caused the v0.4 vertex crash.
        // For framed maps only, bypass Derivative's depth-classified text path.
        if (EntityVisibilityFixClient.renderingFramedMap()) {
            return RenderTypes.textSeeThrough(texture);
        }
        return RenderTypes.text(texture);
    }
}
