package cc.pe3epwithyou.applejam;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppleJam implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("apple-jam");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Apple Jam");
        ScreenEvents.BEFORE_INIT.register((minecraft, screen, width, height) ->
            ScreenEvents.remove(screen).register((screen1) -> onScreenClose(minecraft))
        );
        LOGGER.info("Apple Jam initialized :)");
    }

    private void onScreenClose(Minecraft client) {
        if (isMac()) {
            restoreKey(client.options.keyUp, client);
            restoreKey(client.options.keyLeft, client);
            restoreKey(client.options.keyRight, client);
            restoreKey(client.options.keyDown, client);
            restoreKey(client.options.keyJump, client);
        }
    }

    private void restoreKey(KeyMapping binding, Minecraft client) {
        long handle = client.getWindow().handle();
        InputConstants.Key key = binding.getDefaultKey();

        boolean isPressed = key.getType() == InputConstants.Type.MOUSE ? GLFW.glfwGetMouseButton(handle, key.getValue()) == 1 : GLFW.glfwGetKey(handle, key.getValue()) == 1;
        binding.setDown(isPressed);
    }

    private boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }
}
