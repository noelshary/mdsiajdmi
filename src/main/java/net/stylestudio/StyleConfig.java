package net.stylestudio;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.fabricmc.loader.api.FabricLoader;

/** Speichert pro Kategorie die gewaehlte Style-ID in config/style_studio.json. */
public final class StyleConfig {
	private StyleConfig() {}

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {}.getType();

	/** Kategorie-ID -> Style-ID. Fehlt ein Eintrag, gilt "default". */
	public static Map<String, String> selection = new LinkedHashMap<>();

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("style_studio.json");
	}

	public static void load() {
		Path f = file();
		if (!Files.isRegularFile(f)) return;
		try (Reader r = Files.newBufferedReader(f)) {
			Map<String, String> loaded = GSON.fromJson(r, MAP_TYPE);
			if (loaded != null) selection = new LinkedHashMap<>(loaded);
		} catch (Exception e) {
			StyleStudioClient.LOGGER.error("Konnte style_studio.json nicht lesen", e);
		}
	}

	public static void save() {
		try (Writer w = Files.newBufferedWriter(file())) {
			GSON.toJson(selection, MAP_TYPE, w);
		} catch (IOException e) {
			StyleStudioClient.LOGGER.error("Konnte style_studio.json nicht speichern", e);
		}
	}

	/** Entfernt "default"-Eintraege, damit zwei Auswahlen vergleichbar sind. */
	public static Map<String, String> normalize(Map<String, String> in) {
		Map<String, String> out = new LinkedHashMap<>();
		for (Map.Entry<String, String> e : in.entrySet()) {
			if (e.getValue() != null && !"default".equals(e.getValue())) out.put(e.getKey(), e.getValue());
		}
		return out;
	}
}
