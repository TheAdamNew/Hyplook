package me.theadamnew.hyplook.command;

import me.theadamnew.hyplook.Hyplook;
import me.theadamnew.hyplook.config.ConfigGui;
import me.theadamnew.hyplook.config.ModConfig;
import me.theadamnew.hyplook.keybind.KeyBindings;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyplookCommand extends CommandBase {

    private static final String PREFIX = "\u00A78[\u00A7eHypLook\u00A78]\u00A77";

    @Override
    public String getCommandName() {
        return "hyplook";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/hyplook <help|gui|on|off|toggle|hold|invert|nametags|key>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            showHelp(sender);
            return;
        }
        String arg = args[0].toLowerCase();
        switch (arg) {
            case "help":
                showHelp(sender);
                break;
            case "gui":
                Hyplook.mc.displayGuiScreen(new ConfigGui());
                break;
            case "on":
                if (!Hyplook.instance.perspectiveToggled) {
                    Hyplook.instance.enterPerspective();
                    message(sender, "Perspective enabled.");
                }
                break;
            case "off":
                if (Hyplook.instance.perspectiveToggled) {
                    Hyplook.instance.resetPerspective();
                    message(sender, "Perspective disabled.");
                }
                break;
            case "toggle":
                if (Hyplook.instance.perspectiveToggled) {
                    Hyplook.instance.resetPerspective();
                } else {
                    Hyplook.instance.enterPerspective();
                }
                message(sender, Hyplook.instance.perspectiveToggled ? "Perspective enabled." : "Perspective disabled.");
                break;
            case "hold":
                ModConfig.setHoldMode(!ModConfig.holdMode);
                message(sender, "Hold mode " + (ModConfig.holdMode ? "enabled" : "disabled") + ".");
                break;
            case "invert":
                ModConfig.setInvertPitch(!ModConfig.invertPitch);
                message(sender, "Invert pitch " + (ModConfig.invertPitch ? "enabled" : "disabled") + ".");
                break;
            case "nametags":
                ModConfig.setNametagsFaceCamera(!ModConfig.nametagsFaceCamera);
                message(sender, "Camera-facing nametags " + (ModConfig.nametagsFaceCamera ? "enabled" : "disabled") + ".");
                break;
            case "key":
                if (args.length > 1) {
                    int code = parseKey(args[1]);
                    if (code != -1) {
                        KeyBindings.rebind(code);
                        message(sender, "Keybind set to " + args[1].toUpperCase() + " (" + code + ").");
                    } else {
                        message(sender, "Unknown key name: " + args[1]
                                + ". Try LALT, RALT, RSHIFT, F5, mouse3, or a key code like -97.");
                    }
                } else {
                    message(sender, getCommandUsage(sender));
                }
                break;
            default:
                message(sender, getCommandUsage(sender));
                break;
        }
    }

    private static void showHelp(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText("\u00A78[\u00A7eHypLook\u00A78]\u00A77commands:"));
        sender.addChatMessage(new ChatComponentText("\u00A7e/hyplook gui \u00A77Open the config GUI"));
        sender.addChatMessage(new ChatComponentText("\u00A7e/hyplook on \u00A77Enable the perspective"));
        sender.addChatMessage(new ChatComponentText("\u00A7e/hyplook off \u00A77Disable the perspective"));
        sender.addChatMessage(new ChatComponentText("\u00A7e/hyplook toggle \u00A77Toggle the perspective"));
        sender.addChatMessage(new ChatComponentText("\u00A7e/hyplook hold \u00A77Toggle hold mode (held vs toggled)"));
        sender.addChatMessage(new ChatComponentText("\u00A7e/hyplook invert \u00A77Toggle invert camera pitch"));
        sender.addChatMessage(new ChatComponentText("\u00A7e/hyplook nametags \u00A77Toggle camera-facing nametags"));
        sender.addChatMessage(new ChatComponentText("\u00A7e/hyplook key <key> \u00A77Set the keybind (e.g. LALT, RSHIFT, F5, mouse3, -97)"));
    }

    private static int parseKey(String input) {
        try {
            int parsed = Integer.parseInt(input);
            return parsed >= -110 && parsed <= Keyboard.KEYBOARD_SIZE ? parsed : -1;
        } catch (NumberFormatException ignored) {
        }
        String normalized = input.toUpperCase();
        if (normalized.startsWith("MOUSE")) {
            try {
                return Integer.parseInt(normalized.substring(5)) - 100;
            } catch (NumberFormatException ignored) {
            }
        }
        Integer alias = keyAliases.get(normalized);
        if (alias != null) {
            return alias;
        }
        int index = Keyboard.getKeyIndex(normalized);
        return index == Keyboard.KEY_NONE ? -1 : index;
    }

    private static final Map<String, Integer> keyAliases = buildKeyAliases();

    private static Map<String, Integer> buildKeyAliases() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("LALT", Keyboard.KEY_LMENU);
        map.put("RALT", Keyboard.KEY_RMENU);
        map.put("LCTRL", Keyboard.KEY_LCONTROL);
        map.put("LCTL", Keyboard.KEY_LCONTROL);
        map.put("RCTRL", Keyboard.KEY_RCONTROL);
        map.put("RCTL", Keyboard.KEY_RCONTROL);
        map.put("ESC", Keyboard.KEY_ESCAPE);
        map.put("ENTER", Keyboard.KEY_RETURN);
        map.put("LWINDOW", Keyboard.KEY_LMETA);
        map.put("LWIN", Keyboard.KEY_LMETA);
        map.put("RWINDOW", Keyboard.KEY_RMETA);
        map.put("RWIN", Keyboard.KEY_RMETA);
        map.put("META", Keyboard.KEY_LMETA);
        map.put("PAGEUP", Keyboard.KEY_PRIOR);
        map.put("PGUP", Keyboard.KEY_PRIOR);
        map.put("PAGEDOWN", Keyboard.KEY_NEXT);
        map.put("PGDN", Keyboard.KEY_NEXT);
        map.put("CAPSLOCK", Keyboard.KEY_CAPITAL);
        map.put("PRINTSCREEN", Keyboard.KEY_SYSRQ);
        map.put("PRTSC", Keyboard.KEY_SYSRQ);
        map.put("SCROLLLOCK", Keyboard.KEY_SCROLL);
        map.put("NUMPADDECIMAL", Keyboard.KEY_DECIMAL);
        map.put("INS", Keyboard.KEY_INSERT);
        map.put("DEL", Keyboard.KEY_DELETE);
        map.put("ARROWLEFT", Keyboard.KEY_LEFT);
        map.put("ARROWRIGHT", Keyboard.KEY_RIGHT);
        map.put("ARROWUP", Keyboard.KEY_UP);
        map.put("ARROWDOWN", Keyboard.KEY_DOWN);
        return map;
    }

    private static void message(ICommandSender sender, String text) {
        sender.addChatMessage(new ChatComponentText(PREFIX + text));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}