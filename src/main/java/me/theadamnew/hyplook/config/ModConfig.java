package me.theadamnew.hyplook.config;

import net.minecraftforge.common.config.Configuration;
import org.lwjgl.input.Keyboard;

import java.io.File;

public class ModConfig {

    public static final String GENERAL = "general";
    public static final String CONTROLS = "controls";

    private static final String MOD_ENABLED_COMMENT = "Master switch for the entire mod.";
    private static final String HOLD_MODE_COMMENT = "When true the perspective only stays active while the keybind is held. When false the keybind toggles it.";
    private static final String INVERT_PITCH_COMMENT = "Invert the camera pitch while looking around.";
    private static final String NAMETAGS_COMMENT = "Rotate nametags so they always face the camera while the perspective is active.";
    private static final String KEY_CODE_COMMENT = "Key code for the perspective keybind. Negative values are mouse buttons (-100 left, -99 right, -98 middle, -97 mouse 3, -96 mouse 4).";

    private static Configuration config;

    public static boolean modEnabled = true;
    public static boolean holdMode = true;
    public static boolean invertPitch = false;
    public static boolean nametagsFaceCamera = true;
    public static int keyCode = Keyboard.KEY_LMENU;

    private ModConfig() {
    }

    public static void load(File file) {
        config = new Configuration(file);
        sync();
    }

    public static void sync() {
        modEnabled = config.getBoolean("modEnabled", GENERAL, true, MOD_ENABLED_COMMENT);
        holdMode = config.getBoolean("holdMode", GENERAL, true, HOLD_MODE_COMMENT);
        invertPitch = config.getBoolean("invertPitch", GENERAL, false, INVERT_PITCH_COMMENT);
        nametagsFaceCamera = config.getBoolean("nametagsFaceCamera", GENERAL, true, NAMETAGS_COMMENT);
        keyCode = config.getInt("keyCode", CONTROLS, Keyboard.KEY_LMENU, -110, Keyboard.KEYBOARD_SIZE, KEY_CODE_COMMENT);

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void setModEnabled(boolean value) {
        modEnabled = value;
        set(GENERAL, "modEnabled", value, MOD_ENABLED_COMMENT);
    }

    public static void setHoldMode(boolean value) {
        holdMode = value;
        set(GENERAL, "holdMode", value, HOLD_MODE_COMMENT);
    }

    public static void setInvertPitch(boolean value) {
        invertPitch = value;
        set(GENERAL, "invertPitch", value, INVERT_PITCH_COMMENT);
    }

    public static void setNametagsFaceCamera(boolean value) {
        nametagsFaceCamera = value;
        set(GENERAL, "nametagsFaceCamera", value, NAMETAGS_COMMENT);
    }

    public static void setKeyCode(int value) {
        keyCode = value;
        if (config != null) {
            config.get(CONTROLS, "keyCode", Keyboard.KEY_LMENU, KEY_CODE_COMMENT).setValue(value);
            config.save();
        }
    }

    private static void set(String category, String name, boolean value, String comment) {
        if (config != null) {
            config.get(category, name, value, comment).setValue(value);
            config.save();
        }
    }
}
