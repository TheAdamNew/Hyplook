package me.theadamnew.hyplook;

import me.theadamnew.hyplook.config.ModConfig;
import me.theadamnew.hyplook.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Hyplook.MODID, name = Hyplook.NAME, version = Hyplook.VERSION,
        acceptedMinecraftVersions = "[1.8.9]", clientSideOnly = true,
        guiFactory = "me.theadamnew.hyplook.config.ConfigGuiFactory")
public class Hyplook {

    public static final String MODID = "hyplook";
    public static final String NAME = "Hyplook";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(Hyplook.MODID)
    public static Hyplook instance;

    @SidedProxy(clientSide = "me.theadamnew.hyplook.proxy.ClientProxy",
            serverSide = "me.theadamnew.hyplook.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static Minecraft mc;

    public boolean perspectiveToggled = false;
    public float cameraYaw = 0F;
    public float cameraPitch = 0F;

    private int previousPerspective = 0;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModConfig.load(event.getSuggestedConfigurationFile());
        mc = Minecraft.getMinecraft();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    public boolean isPerspectiveActive() {
        return ModConfig.modEnabled && perspectiveToggled;
    }

    public void onKeyStateChanged(boolean pressed) {
        if (ModConfig.modEnabled) {
            if (pressed) {
                if (perspectiveToggled) {
                    resetPerspective();
                } else {
                    enterPerspective();
                }
            } else if (ModConfig.holdMode && perspectiveToggled) {
                resetPerspective();
            }
        } else if (perspectiveToggled) {
            resetPerspective();
        }
    }

    public void enterPerspective() {
        if (mc == null || mc.thePlayer == null) {
            return;
        }
        perspectiveToggled = true;
        cameraYaw = mc.thePlayer.rotationYaw;
        cameraPitch = mc.thePlayer.rotationPitch;
        if (mc.gameSettings != null) {
            previousPerspective = mc.gameSettings.thirdPersonView;
            mc.gameSettings.thirdPersonView = 1;
        }
        if (mc.renderGlobal != null) {
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
    }

    public void resetPerspective() {
        if (!perspectiveToggled) {
            return;
        }
        perspectiveToggled = false;
        if (mc != null && mc.gameSettings != null) {
            mc.gameSettings.thirdPersonView = previousPerspective;
        }
        if (mc != null && mc.renderGlobal != null) {
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
    }
}
