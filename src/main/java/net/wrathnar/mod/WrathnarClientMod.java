package net.wrathnar.mod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class WrathnarClientMod implements ClientModInitializer {

	public static KeyBinding flyKey;

	@Override
	public void onInitializeClient() {
		flyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.wrathnar.fly",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_Z,
				"category.wrathnar"
		));

		WrathnarFlyHandler.register();
	}
}
