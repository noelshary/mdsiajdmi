package net.stylestudio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import net.fabricmc.loader.api.FabricLoader;

/** Sucht alle Original-Texturen im Minecraft-Jar des Spielers (nichts davon wird mitgeliefert). */
public final class VanillaTextures {
	private VanillaTextures() {}

	private static Map<String, Path> index;

	/** Schluessel: Pfad unter assets/minecraft/, z.B. "textures/block/obsidian.png". */
	public static synchronized Map<String, Path> index() {
		if (index != null) return index;

		Map<String, Path> map = new TreeMap<>();
		FabricLoader.getInstance().getModContainer("minecraft").ifPresent(container -> {
			for (Path root : container.getRootPaths()) {
				Path base = root.resolve("assets").resolve("minecraft");
				Path textures = base.resolve("textures");
				if (!Files.isDirectory(textures)) continue;
				try (Stream<Path> walk = Files.walk(textures)) {
					walk.filter(Files::isRegularFile).forEach(p -> {
						String rel = base.relativize(p).toString().replace('\\', '/');
						if (rel.endsWith(".png")) map.putIfAbsent(rel, p);
					});
				} catch (IOException e) {
					StyleStudioClient.LOGGER.error("Konnte Minecraft-Texturen nicht lesen", e);
				}
			}
		});

		StyleStudioClient.LOGGER.info("Style Studio: {} Original-Texturen gefunden", map.size());
		index = map;
		return map;
	}
}
