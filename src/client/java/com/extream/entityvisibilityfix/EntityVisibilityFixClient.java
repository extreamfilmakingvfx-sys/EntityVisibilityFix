package com.extream.entityvisibilityfix;

import net.fabricmc.api.ClientModInitializer;

public final class EntityVisibilityFixClient implements ClientModInitializer {
    public static volatile boolean ENABLED = true;
    public static volatile boolean FIX_MAPS = true;
    private static final ThreadLocal<Integer> ENTITY_DEPTH = ThreadLocal.withInitial(() -> 0);
    private static final ThreadLocal<Boolean> RENDERING_AVATAR = ThreadLocal.withInitial(() -> false);

    @Override
    public void onInitializeClient() {
        System.out.println("[EntityVisibilityFix] 0.2.0 loaded - player skin + item-frame map compatibility fixes ON");
    }

    public static void beginEntity() { if (ENABLED && FIX_MAPS) ENTITY_DEPTH.set(ENTITY_DEPTH.get() + 1); }
    public static void endEntity() { ENTITY_DEPTH.set(Math.max(0, ENTITY_DEPTH.get() - 1)); }
    public static boolean renderingEntity() { return ENABLED && FIX_MAPS && ENTITY_DEPTH.get() > 0; }

    public static void beginAvatar() { if (ENABLED) RENDERING_AVATAR.set(true); }
    public static void endAvatar() { RENDERING_AVATAR.set(false); }
    public static boolean shouldForceOpaque() { return ENABLED && (RENDERING_AVATAR.get() || renderingEntity()); }
}
