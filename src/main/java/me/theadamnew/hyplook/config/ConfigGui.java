package me.theadamnew.hyplook.config;

import me.theadamnew.hyplook.keybind.KeyBindings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigGui extends GuiScreen {

    private GuiButton enabledButton;
    private GuiButton holdButton;
    private GuiButton invertButton;
    private GuiButton nametagsButton;
    private GuiButton keyButton;
    private GuiButton doneButton;

    private boolean bindingKey = false;

    @Override
    public void initGui() {
        buttonList.clear();
        int centerX = width / 2;
        int startY = height / 2 - 60;

        enabledButton = new GuiButton(0, centerX - 110, startY, 220, 20, "");
        holdButton = new GuiButton(1, centerX - 110, startY + 25, 220, 20, "");
        invertButton = new GuiButton(2, centerX - 110, startY + 50, 220, 20, "");
        nametagsButton = new GuiButton(3, centerX - 110, startY + 75, 220, 20, "");
        keyButton = new GuiButton(4, centerX - 110, startY + 100, 220, 20, "");
        doneButton = new GuiButton(5, centerX - 60, startY + 135, 120, 20, "Done");

        buttonList.add(enabledButton);
        buttonList.add(holdButton);
        buttonList.add(invertButton);
        buttonList.add(nametagsButton);
        buttonList.add(keyButton);
        buttonList.add(doneButton);

        updateButtons();
    }

    private void updateButtons() {
        enabledButton.displayString = "Mod Enabled: " + bool(ModConfig.modEnabled);
        holdButton.displayString = "Hold Mode: " + bool(ModConfig.holdMode);
        invertButton.displayString = "Invert Pitch: " + bool(ModConfig.invertPitch);
        nametagsButton.displayString = "Nametags Face Camera: " + bool(ModConfig.nametagsFaceCamera);
        keyButton.displayString = "Keybind: " + (bindingKey ? "Press a key or mouse button..." : GameSettings.getKeyDisplayString(ModConfig.keyCode));
    }

    private static String bool(boolean b) {
        return b ? "ON" : "OFF";
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (bindingKey) {
            return;
        }
        switch (button.id) {
            case 0:
                ModConfig.setModEnabled(!ModConfig.modEnabled);
                break;
            case 1:
                ModConfig.setHoldMode(!ModConfig.holdMode);
                break;
            case 2:
                ModConfig.setInvertPitch(!ModConfig.invertPitch);
                break;
            case 3:
                ModConfig.setNametagsFaceCamera(!ModConfig.nametagsFaceCamera);
                break;
            case 4:
                bindingKey = true;
                break;
            case 5:
                mc.displayGuiScreen(null);
                break;
        }
        updateButtons();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (bindingKey) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                bindingKey = false;
            } else {
                KeyBindings.rebind(keyCode);
                bindingKey = false;
            }
            updateButtons();
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (bindingKey) {
            KeyBindings.rebind(mouseButton - 100);
            bindingKey = false;
            updateButtons();
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Hyplook Settings", width / 2, height / 2 - 95, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
