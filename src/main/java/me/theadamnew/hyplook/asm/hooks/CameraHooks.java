package me.theadamnew.hyplook.asm.hooks;

import me.theadamnew.hyplook.Hyplook;
import net.minecraft.entity.Entity;

public class CameraHooks {

    private CameraHooks() {
    }

    public static float rotationYaw(Entity entity) {
        return perspective() ? Hyplook.instance.cameraYaw : entity.rotationYaw;
    }

    public static float prevRotationYaw(Entity entity) {
        return perspective() ? Hyplook.instance.cameraYaw : entity.prevRotationYaw;
    }

    public static float rotationPitch(Entity entity) {
        return perspective() ? Hyplook.instance.cameraPitch : entity.rotationPitch;
    }

    public static float prevRotationPitch(Entity entity) {
        return perspective() ? Hyplook.instance.cameraPitch : entity.prevRotationPitch;
    }

    private static boolean perspective() {
        return Hyplook.instance != null && Hyplook.instance.isPerspectiveActive();
    }
}