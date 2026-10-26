package net.stylestudio;

/**
 * Eine Farbvariante. Die Mod faerbt die Original-Texturen von Minecraft zur Laufzeit um,
 * es werden also keine fremden Texturen mitgeliefert.
 *
 * @param hue    Zielfarbton in Grad (0-360), {@link #GRAY} = Graustufen, {@link #KEEP} = Farbton behalten
 * @param satMul Saettigung wird damit multipliziert
 * @param satAdd Saettigung wird um diesen Wert erhoeht (macht graue Texturen farbig)
 * @param valMul Helligkeit wird damit multipliziert
 * @param swatch Farbe der Vorschau-Kachel im Menue (ARGB)
 */
public record Style(String id, String name, float hue, float satMul, float satAdd, float valMul, int swatch) {
	public static final float GRAY = -1f;
	public static final float KEEP = -2f;

	public boolean isDefault() {
		return "default".equals(id);
	}
}
