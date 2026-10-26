package net.stylestudio;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class StyleStudioClient implements ClientModInitializer {
	public static final String MOD_ID = "style_studio";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static KeyBinding openKey;

	@Override
	public void onInitializeClient() {
		StyleConfig.load();

		// Taste K (im Spiel aenderbar unter Steuerung > Style Studio)
		openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.style_studio.open",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_K,
			KeyBinding.Category.create(Identifier.of(MOD_ID, "main"))
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openKey.wasPressed()) {
				if (!(client.currentScreen instanceof StyleStudioScreen)) {
					client.setScreen(new StyleStudioScreen(client.currentScreen));
				}
			}
		});

		// Zusaetzlich ein Button im Titelbildschirm und im Pausenmenue
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof TitleScreen || screen instanceof GameMenuScreen) {
				Screens.getButtons(screen).add(
					ButtonWidget.builder(Text.translatable("screen.style_studio.button"),
						b -> MinecraftClient.getInstance().setScreen(new StyleStudioScreen(screen)))
						.dimensions(6, 6, 90, 20).build()
				);
			}
		});

		LOGGER.info("Style Studio geladen");
	}
}
