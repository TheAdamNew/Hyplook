package me.theadamnew.hyplook.keybind;

import me.theadamnew.hyplook.Hyplook;
import me.theadamnew.hyplook.config.ModConfig;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {

    public static final String CATEGORY = "key.categories.hyplook";

    public static KeyBinding perspectiveKey;

    private KeyBindings() {
    }

    public static void register() {
        perspectiveKey = new KeyBinding("key.hyplook.perspective", ModConfig.keyCode, CATEGORY);
        ClientRegistry.registerKeyBinding(perspectiveKey);
    }

    public static void rebind(int keyCode) {
        if (perspectiveKey == null || Hyplook.mc == null || Hyplook.mc.gameSettings == null) {
            return;
        }
        Hyplook.mc.gameSettings.setOptionKeyBinding(perspectiveKey, keyCode);
        ModConfig.setKeyCode(keyCode);
    }
}
