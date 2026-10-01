package com.extream.entityvisibilityfix.mixin;

import com.extream.entityvisibilityfix.EntityVisibilityFixClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Inject(method = "submit*", at = @At("HEAD"))
    private void evf$beginPlayer(LivingEntityRenderState state, PoseStack poseStack,
                                 SubmitNodeCollector collector, CameraRenderState camera,
                                 CallbackInfo ci) {
        if (state instanceof AvatarRenderState) EntityVisibilityFixClient.beginAvatar();
    }

    @Inject(method = "submit*", at = @At("TAIL"))
    private void evf$endPlayer(LivingEntityRenderState state, PoseStack poseStack,
                               SubmitNodeCollector collector, CameraRenderState camera,
                               CallbackInfo ci) {
        if (state instanceof AvatarRenderState) EntityVisibilityFixClient.endAvatar();
    }
}
