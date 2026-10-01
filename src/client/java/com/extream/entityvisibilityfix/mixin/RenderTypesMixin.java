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
    // Keep the original working player-skin fix only.
    // IMPORTANT: framed maps must NOT be converted to entityCutout here.
    // MapRenderer 26.3 supplies a different vertex format; replacing its
    // RenderType caused "Missing elements in vertex" and crashed the client.
    @Inject(method = "entityTranslucent(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;",
            at = @At("HEAD"), cancellable = true)
    private static void evf$forcePlayerCutout(Identifier texture, CallbackInfoReturnable<RenderType> cir) {
        if (EntityVisibilityFixClient.shouldForceOpaque() && !EntityVisibilityFixClient.renderingFramedMap()) {
            cir.setReturnValue(RenderTypes.entityCutout(texture));
        }
    }
}
