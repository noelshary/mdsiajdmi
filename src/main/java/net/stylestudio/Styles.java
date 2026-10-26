package net.stylestudio;

import java.util.List;

public final class Styles {
	private Styles() {}

	public static final List<Style> ALL = List.of(
		new Style("default",  "Standard", Style.KEEP, 1.0f, 0.00f, 1.00f, 0xFF8A8A8A),
		new Style("lavender", "Lavendel", 270f,       0.9f, 0.25f, 1.15f, 0xFFB57EFF),
		new Style("purple",   "Lila",     285f,       1.3f, 0.40f, 1.00f, 0xFF9B30FF),
		new Style("blue",     "Blau",     215f,       1.2f, 0.40f, 1.05f, 0xFF3C78FF),
		new Style("ice",      "Eis",      190f,       0.7f, 0.20f, 1.30f, 0xFF9BE8FF),
		new Style("pink",     "Pink",     325f,       1.0f, 0.35f, 1.20f, 0xFFFF6EC7),
		new Style("red",      "Rot",      0f,         1.3f, 0.45f, 1.00f, 0xFFE02828),
		new Style("orange",   "Orange",   25f,        1.2f, 0.45f, 1.10f, 0xFFFF8A1E),
		new Style("gold",     "Gold",     45f,        1.0f, 0.45f, 1.20f, 0xFFFFC21E),
		new Style("lime",     "Limette",  100f,       1.1f, 0.40f, 1.10f, 0xFF8CFF2E),
		new Style("black",    "Schwarz",  Style.GRAY, 0.0f, 0.00f, 0.40f, 0xFF1A1A1A),
		new Style("white",    "Weiß",     Style.GRAY, 0.0f, 0.00f, 1.70f, 0xFFF2F2F2)
	);

	public static Style byId(String id) {
		if (id == null) return null;
		for (Style s : ALL) {
			if (s.id().equals(id)) return s;
		}
		return null;
	}
}
