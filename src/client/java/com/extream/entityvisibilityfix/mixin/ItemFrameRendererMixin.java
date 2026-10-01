package com.extream.entityvisibilityfix.mixin;

import com.extream.entityvisibilityfix.EntityVisibilityFixClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Minecraft 26.3 keeps framed-map data directly on ItemFrameRenderState.
 * We only enable the opaque override while an item frame containing a map
 * is submitting its render nodes. Held maps are deliberately untouched.
 */
@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameRendererMixin {
    @Inject(method = "submit", at = @At("HEAD"))
    private void evf$beginFramedMap(ItemFrameRenderState state, PoseStack poseStack,
                                    SubmitNodeCollector collector, CameraRenderState camera,
                                    CallbackInfo ci) {
        if (state.mapId != null) {
            EntityVisibilityFixClient.beginFramedMap();
        }
    }

    @Inject(method = "submit", at = @At("TAIL"))
    private void evf$endFramedMap(ItemFrameRenderState state, PoseStack poseStack,
                                  SubmitNodeCollector collector, CameraRenderState camera,
                                  CallbackInfo ci) {
        if (state.mapId != null) {
            EntityVisibilityFixClient.endFramedMap();
        }
    }
}
