package net.stylestudio;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Eine Kategorie im Menue (z.B. Kristalle). Die Texturen werden ueber Regex-Muster auf dem
 * Pfad unter assets/minecraft/ gefunden, damit kleine Umbenennungen zwischen Versionen nichts kaputt machen.
 */
public record StyleCategory(String id, String name, float brightness, List<Pattern> patterns) {
	public static StyleCategory of(String id, String name, float brightness, String... regexes) {
		List<Pattern> list = new ArrayList<>();
		for (String r : regexes) {
			list.add(Pattern.compile(r));
		}
		return new StyleCategory(id, name, brightness, List.copyOf(list));
	}

	public boolean matches(String relPath) {
		for (Pattern p : patterns) {
			if (p.matcher(relPath).matches()) return true;
		}
		return false;
	}
}
