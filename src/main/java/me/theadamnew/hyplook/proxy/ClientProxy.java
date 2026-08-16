package me.theadamnew.hyplook.proxy;

import me.theadamnew.hyplook.command.HyplookCommand;
import me.theadamnew.hyplook.handler.HyplookHandler;
import me.theadamnew.hyplook.keybind.KeyBindings;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        KeyBindings.register();
        MinecraftForge.EVENT_BUS.register(new HyplookHandler());
        ClientCommandHandler.instance.registerCommand(new HyplookCommand());
    }
}
