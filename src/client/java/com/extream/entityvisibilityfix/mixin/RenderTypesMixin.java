package com.extream.entityvisibilityfix.mixin;

import com.extream.entityvisibilityfix.EntityVisibilityFixClient;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderTypes.class)
public abstract class RenderTypesMixin {
    // Existing player-skin fix. Keep this behavior unchanged.
    @Inject(method = "entityTranslucent(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;",
            at = @At("HEAD"), cancellable = true)
    private static void evf$forcePlayerCutout(Identifier texture, CallbackInfoReturnable<RenderType> cir) {
        if (EntityVisibilityFixClient.shouldForceOpaque()) {
            cir.setReturnValue(RenderTypes.entityCutout(texture));
        }
    }

    // Minecraft 26.3 MapRenderer submits the dynamic map texture through the
    // text-family render path. Derivative loses that path when the map is
    // submitted from ItemFrameRenderer, even though the same map works in hand.
    // While ItemFrameRenderer is actively submitting a map, reroute these
    // textured quads to entityCutout so Iris/Derivative sees an opaque textured
    // feature instead of the broken framed-map/text feature.
    @Inject(method = "text(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;",
            at = @At("HEAD"), cancellable = true, require = 0)
    private static void evf$framedMapText(Identifier texture, CallbackInfoReturnable<RenderType> cir) {
        if (EntityVisibilityFixClient.renderingFramedMap()) {
            cir.setReturnValue(RenderTypes.entityCutout(texture));
        }
    }

    @Inject(method = "textPolygonOffset(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;",
            at = @At("HEAD"), cancellable = true, require = 0)
    private static void evf$framedMapTextPolygonOffset(Identifier texture, CallbackInfoReturnable<RenderType> cir) {
        if (EntityVisibilityFixClient.renderingFramedMap()) {
            cir.setReturnValue(RenderTypes.entityCutout(texture));
        }
    }
}
