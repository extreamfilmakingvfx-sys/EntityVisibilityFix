package com.extream.entityvisibilityfix;

import net.fabricmc.api.ClientModInitializer;

public final class EntityVisibilityFixClient implements ClientModInitializer {
    public static volatile boolean ENABLED = true;
    public static volatile boolean FIX_MAPS = true;
    private static final ThreadLocal<Boolean> RENDERING_AVATAR = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Integer> FRAMED_MAP_DEPTH = ThreadLocal.withInitial(() -> 0);

    @Override
    public void onInitializeClient() {
        System.out.println("[EntityVisibilityFix] 0.3.0 loaded - player skin + targeted item-frame map fixes ON");
    }

    public static void beginAvatar() {
        if (ENABLED) RENDERING_AVATAR.set(true);
    }

    public static void endAvatar() {
        RENDERING_AVATAR.set(false);
    }

    public static void beginFramedMap() {
        if (ENABLED && FIX_MAPS) FRAMED_MAP_DEPTH.set(FRAMED_MAP_DEPTH.get() + 1);
    }

    public static void endFramedMap() {
        FRAMED_MAP_DEPTH.set(Math.max(0, FRAMED_MAP_DEPTH.get() - 1));
    }

    public static boolean renderingFramedMap() {
        return ENABLED && FIX_MAPS && FRAMED_MAP_DEPTH.get() > 0;
    }

    public static boolean shouldForceOpaque() {
        return ENABLED && (RENDERING_AVATAR.get() || renderingFramedMap());
    }
}
