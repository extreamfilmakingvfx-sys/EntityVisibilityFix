package com.extream.entityvisibilityfix.mixin;

import com.extream.entityvisibilityfix.EntityVisibilityFixClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Inject(method = "submit*", at = @At("HEAD"), require = 0)
    private void evf$beginEntity(EntityRenderState state, PoseStack poseStack,
                                 SubmitNodeCollector collector, CameraRenderState camera,
                                 CallbackInfo ci) {
        EntityVisibilityFixClient.beginEntity();
    }

    @Inject(method = "submit*", at = @At("TAIL"), require = 0)
    private void evf$endEntity(EntityRenderState state, PoseStack poseStack,
                               SubmitNodeCollector collector, CameraRenderState camera,
                               CallbackInfo ci) {
        EntityVisibilityFixClient.endEntity();
    }
}
