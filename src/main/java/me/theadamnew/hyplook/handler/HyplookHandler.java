package me.theadamnew.hyplook.handler;

import me.theadamnew.hyplook.Hyplook;
import me.theadamnew.hyplook.config.ModConfig;
import me.theadamnew.hyplook.keybind.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class HyplookHandler {

    private static final float MAX_CAMERA_DISTANCE = 4.0F;

    private boolean prevKeyState = false;

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Hyplook mod = Hyplook.instance;
        Minecraft mc = Hyplook.mc;
        if (mc == null) {
            return;
        }

        if (event.phase == TickEvent.Phase.START) {
            if (mod.perspectiveToggled) {
                if (mc.theWorld == null || mc.thePlayer == null || !ModConfig.modEnabled) {
                    mod.resetPerspective();
                    return;
                }
                mc.gameSettings.thirdPersonView = 1;
            }
            updateCamera(mod, mc);
        } else {
            boolean down = GameSettings.isKeyDown(KeyBindings.perspectiveKey);
            if (down != prevKeyState && mc.currentScreen == null) {
                prevKeyState = down;
                mod.onKeyStateChanged(down);
            }
        }
    }

    private static void updateCamera(Hyplook mod, Minecraft mc) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.entityRenderer == null) {
            return;
        }
        if (!mod.isPerspectiveActive()) {
            mc.entityRenderer.thirdPersonDistance = MAX_CAMERA_DISTANCE;
            return;
        }
        handleMouse(mod, mc);
        applyWallCollision(mod, mc);
    }

    private static void handleMouse(Hyplook mod, Minecraft mc) {
        if (!mc.inGameHasFocus || !Display.isActive()) {
            return;
        }
        float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f1 = f * f * f * 8.0F;
        mod.cameraYaw = MathHelper.wrapAngleTo180_float(mod.cameraYaw + (float) Mouse.getDX() * f1 * 0.15F);
        float pitchDelta = (float) Mouse.getDY() * f1 * 0.15F;
        mod.cameraPitch += ModConfig.invertPitch ? pitchDelta : -pitchDelta;
        mod.cameraPitch = MathHelper.clamp_float(mod.cameraPitch, -90F, 90F);
    }

    private static void applyWallCollision(Hyplook mod, Minecraft mc) {
        Entity player = mc.thePlayer;
        double d3 = (double) MAX_CAMERA_DISTANCE;
        double d0 = player.posX;
        double d1 = player.posY + (double) player.getEyeHeight();
        double d2 = player.posZ;

        float f1 = mod.cameraYaw;
        float f2 = mod.cameraPitch;
        double d4 = (double) (-MathHelper.sin(f1 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d3;
        double d5 = (double) (MathHelper.cos(f1 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d3;
        double d6 = (double) (-MathHelper.sin(f2 / 180.0F * (float) Math.PI)) * d3;

        Vec3 eye = new Vec3(d0, d1, d2);
        for (int i = 0; i < 8; ++i) {
            float f3 = (float) ((i & 1) * 2 - 1);
            float f4 = (float) ((i >> 1 & 1) * 2 - 1);
            float f5 = (float) ((i >> 2 & 1) * 2 - 1);
            f3 = f3 * 0.1F;
            f4 = f4 * 0.1F;
            f5 = f5 * 0.1F;
            MovingObjectPosition movingObjectPosition = mc.theWorld.rayTraceBlocks(
                    new Vec3(d0 + (double) f3, d1 + (double) f4, d2 + (double) f5),
                    new Vec3(d0 - d4 + (double) f3 + (double) f5, d1 - d6 + (double) f4, d2 - d5 + (double) f5));

            if (movingObjectPosition != null) {
                double d7 = movingObjectPosition.hitVec.distanceTo(eye);
                if (d7 < d3) {
                    d3 = d7;
                }
            }
        }
        mc.entityRenderer.thirdPersonDistance = (float) d3;
    }

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        Hyplook mod = Hyplook.instance;
        if (mod != null && mod.isPerspectiveActive()) {
            event.yaw = mod.cameraYaw + 180F;
            event.pitch = mod.cameraPitch;
            event.roll = 0F;
        }
    }

    @SubscribeEvent
    public void onRenderLivingSpecials(RenderLivingEvent.Specials.Pre event) {
        Hyplook mod = Hyplook.instance;
        if (mod == null || !mod.isPerspectiveActive() || !ModConfig.nametagsFaceCamera) {
            return;
        }
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.playerViewX = mod.cameraPitch;
        renderManager.playerViewY = mod.cameraYaw;
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        Hyplook mod = Hyplook.instance;
        if (mod != null && event.gui != null && mod.perspectiveToggled && ModConfig.holdMode) {
            mod.resetPerspective();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        Hyplook mod = Hyplook.instance;
        if (mod != null && event.world.isRemote && mod.perspectiveToggled) {
            mod.resetPerspective();
        }
    }
}
