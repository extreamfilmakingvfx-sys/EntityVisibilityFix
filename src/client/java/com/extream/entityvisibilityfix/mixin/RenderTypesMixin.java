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
    @Inject(method = "entityTranslucent(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;",
            at = @At("HEAD"), cancellable = true)
    private static void evf$forcePlayerCutout(Identifier texture, CallbackInfoReturnable<RenderType> cir) {
        if (EntityVisibilityFixClient.shouldForceOpaque()) {
            cir.setReturnValue(RenderTypes.entityCutout(texture));
        }
    }
}
