package com.extream.entityvisibilityfix;

import net.fabricmc.api.ClientModInitializer;

public final class EntityVisibilityFixClient implements ClientModInitializer {
    public static volatile boolean ENABLED = true;
    private static final ThreadLocal<Boolean> RENDERING_AVATAR = ThreadLocal.withInitial(() -> false);

    @Override
    public void onInitializeClient() {
        System.out.println("[EntityVisibilityFix] 0.1.0 loaded - Force Player Opaque is ON");
    }

    public static void beginAvatar() {
        if (ENABLED) RENDERING_AVATAR.set(true);
    }

    public static void endAvatar() {
        RENDERING_AVATAR.set(false);
    }

    public static boolean shouldForceOpaque() {
        return ENABLED && RENDERING_AVATAR.get();
    }
}
