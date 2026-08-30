package cc.pe3epwithyou.applejam;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppleJam implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("apple-jam");
    private boolean restoreSprintOnNextTick;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Apple Jam");
        ScreenEvents.BEFORE_INIT.register((minecraft, screen, _, _) ->
            ScreenEvents.remove(screen).register((_) -> onScreenClose(minecraft))
        );
        ClientTickEvents.START_CLIENT_TICK.register(this::onClientTick);
        LOGGER.info("Apple Jam initialized :)");
    }

    private void onClientTick(Minecraft client) {
        if (restoreSprintOnNextTick) {
            restoreKey(client.options.keySprint, client);
            restoreSprintOnNextTick = false;
        }
    }

    private void onScreenClose(Minecraft client) {
        if (isMac()) {
            restoreKey(client.options.keyUp, client);
            restoreKey(client.options.keyLeft, client);
            restoreKey(client.options.keyRight, client);
            restoreKey(client.options.keyDown, client);
            restoreKey(client.options.keyJump, client);
            restoreSprintOnNextTick = true;
        }
    }

    private void restoreKey(KeyMapping binding, Minecraft client) {
        long handle = client.getWindow().handle();
        InputConstants.Key key = InputConstants.getKey(binding.saveString());

        boolean isPressed = key.getType() == InputConstants.Type.MOUSE ? GLFW.glfwGetMouseButton(handle, key.getValue()) == 1 : GLFW.glfwGetKey(handle, key.getValue()) == 1;
        binding.setDown(isPressed);
    }

    private boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }
}
