package net.stylestudio;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;

/**
 * Erzeugt aus der Auswahl ein Resourcepack-Ordner "StyleStudio-Generated" im resourcepacks-Ordner,
 * aktiviert es ganz oben und laedt die Ressourcen neu.
 */
public final class StylePack {
	private StylePack() {}

	public static final String DIR_NAME = "StyleStudio-Generated";
	public static final String PACK_ID = "file/" + DIR_NAME;

	private static final String PACK_MCMETA = """
		{
		  "pack": {
		    "description": "Style Studio (automatisch erzeugt)",
		    "pack_format": 75,
		    "min_format": 75,
		    "max_format": 999
		  }
		}
		""";

	public static Path dir() {
		return FabricLoader.getInstance().getGameDir().resolve("resourcepacks").resolve(DIR_NAME);
	}

	/** Schreibt alle umgefaerbten Texturen. Gibt die Anzahl zurueck. */
	public static int generate(Map<String, String> selection) throws IOException {
		Path dir = dir();
		deleteTree(dir);

		Map<String, Path> index = VanillaTextures.index();
		int count = 0;

		for (StyleCategory cat : Categories.ALL) {
			Style style = Styles.byId(selection.get(cat.id()));
			if (style == null || style.isDefault()) continue;

			for (Map.Entry<String, Path> e : index.entrySet()) {
				if (!cat.matches(e.getKey())) continue;

				BufferedImage src;
				try (InputStream in = Files.newInputStream(e.getValue())) {
					src = ImageIO.read(in);
				}
				if (src == null) continue;

				BufferedImage out = Recolor.apply(src, style, cat.brightness());
				Path target = dir.resolve("assets").resolve("minecraft").resolve(e.getKey());
				Files.createDirectories(target.getParent());
				ImageIO.write(out, "png", target.toFile());

				// Animationen (z.B. Feuer) brauchen die .mcmeta-Datei des Originals
				Path meta = e.getValue().resolveSibling(e.getValue().getFileName() + ".mcmeta");
				if (Files.isRegularFile(meta)) {
					Files.copy(meta, target.resolveSibling(target.getFileName() + ".mcmeta"),
						StandardCopyOption.REPLACE_EXISTING);
				}
				count++;
			}
		}

		if (count > 0) {
			Files.createDirectories(dir);
			Files.writeString(dir.resolve("pack.mcmeta"), PACK_MCMETA, StandardCharsets.UTF_8);
		} else {
			deleteTree(dir);
		}
		return count;
	}

	/** Pack neu erzeugen, aktivieren (oder deaktivieren, wenn nichts gewaehlt) und neu laden. */
	public static void applyAndReload(Map<String, String> selection) {
		MinecraftClient client = MinecraftClient.getInstance();

		int count;
		try {
			count = generate(selection);
		} catch (IOException e) {
			StyleStudioClient.LOGGER.error("Style-Pack konnte nicht erzeugt werden", e);
			return;
		}
		StyleStudioClient.LOGGER.info("Style Studio: {} Texturen umgefaerbt", count);

		ResourcePackManager manager = client.getResourcePackManager();
		manager.scanPacks();

		List<String> enabled = new ArrayList<>(manager.getEnabledIds());
		enabled.remove(PACK_ID);
		if (count > 0 && manager.getProfile(PACK_ID) != null) {
			enabled.add(PACK_ID); // ganz hinten = hoechste Prioritaet
		}
		manager.setEnabledProfiles(enabled);

		client.options.resourcePacks.clear();
		for (ResourcePackProfile profile : manager.getEnabledProfiles()) {
			if (!profile.isPinned()) client.options.resourcePacks.add(profile.getId());
		}
		client.options.write();
		client.reloadResources();
	}

	private static void deleteTree(Path root) throws IOException {
		if (!Files.exists(root)) return;
		List<Path> all;
		try (Stream<Path> walk = Files.walk(root)) {
			all = walk.sorted(Comparator.reverseOrder()).toList();
		}
		for (Path p : all) {
			Files.deleteIfExists(p);
		}
	}
}
