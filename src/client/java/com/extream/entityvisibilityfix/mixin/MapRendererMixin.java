package com.extream.entityvisibilityfix.mixin;

import com.extream.entityvisibilityfix.EntityVisibilityFixClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.MapRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Derivative/Iris compatibility for maps rendered inside item frames.
 *
 * Minecraft 26.x normally submits the map quad using RenderTypes.text().
 * Derivative handles that framed-map submission differently from a held map.
 * v0.4 replaced the type with entityCutout and crashed because that changes
 * the expected vertex format. v0.7 instead keeps the TEXT vertex format and
 * redirects only the framed-map RenderType selection to textPolygonOffset,
 * which has the same map/text vertex layout while giving Iris a distinct,
 * depth-safe path. Held maps remain on vanilla RenderTypes.text().
 */
@Mixin(MapRenderer.class)
public abstract class MapRendererMixin {
    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;text(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;",
            ordinal = 0
        ),
        require = 0
    )
    private RenderType evf$framedMapRenderType(net.minecraft.resources.Identifier texture) {
        if (EntityVisibilityFixClient.renderingFramedMap()) {
            return RenderTypes.textPolygonOffset(texture);
        }
        return RenderTypes.text(texture);
    }
}
