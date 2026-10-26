package net.stylestudio;

import java.awt.Color;
import java.awt.image.BufferedImage;

/** Faerbt ein Bild nach einem {@link Style} um. Transparenz bleibt erhalten. */
public final class Recolor {
	private Recolor() {}

	public static BufferedImage apply(BufferedImage src, Style style, float categoryBrightness) {
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		float[] hsb = new float[3];

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int argb = src.getRGB(x, y);
				int a = argb >>> 24;
				if (a == 0) {
					out.setRGB(x, y, argb);
					continue;
				}
				Color.RGBtoHSB((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF, hsb);
				float hue = hsb[0];
				float sat = hsb[1];
				float val = hsb[2];

				if (style.hue() >= 0f) {
					hue = style.hue() / 360f;
					sat = clamp(sat * style.satMul() + (val > 0.04f ? style.satAdd() : 0f));
				} else if (style.hue() == Style.GRAY) {
					sat = 0f;
				}
				val = clamp(val * style.valMul() * categoryBrightness);

				int rgb = Color.HSBtoRGB(hue, sat, val) & 0xFFFFFF;
				out.setRGB(x, y, (a << 24) | rgb);
			}
		}
		return out;
	}

	private static float clamp(float v) {
		return v < 0f ? 0f : Math.min(v, 1f);
	}
}
