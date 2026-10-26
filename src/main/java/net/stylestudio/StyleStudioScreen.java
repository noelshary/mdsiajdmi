package net.stylestudio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/** Das Style-Menue: links Kategorien, rechts Farbvarianten, unten Anwenden / Zuruecksetzen / Fertig. */
public class StyleStudioScreen extends Screen {
	private final Screen parent;
	private final Map<String, String> staged = new LinkedHashMap<>(StyleConfig.selection);
	private final List<int[]> swatches = new ArrayList<>();

	private StyleCategory current = Categories.ALL.get(0);
	private int catPage = 0;

	private int left, top, panelW, panelH, styleX;

	public StyleStudioScreen(Screen parent) {
		super(Text.translatable("screen.style_studio.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		swatches.clear();

		panelW = Math.max(280, Math.min(this.width - 16, 400));
		panelH = Math.max(180, Math.min(this.height - 16, 230));
		left = (this.width - panelW) / 2;
		top = (this.height - panelH) / 2;

		int colX = left + 8;
		int colW = 96;
		int listTop = top + 28;
		int footerY = top + panelH - 26;
		int listBottom = footerY - 30;

		// --- Kategorien (links, mit Seiten) ---
		int perPage = Math.max(3, (listBottom - listTop) / 20);
		int total = Categories.ALL.size();
		int pages = (total + perPage - 1) / perPage;
		catPage = Math.max(0, Math.min(catPage, pages - 1));
		int start = catPage * perPage;

		for (int i = 0; i < perPage && start + i < total; i++) {
			StyleCategory c = Categories.ALL.get(start + i);
			Text label = Text.literal(c.name()).formatted(c == current ? Formatting.YELLOW : Formatting.WHITE);
			this.addDrawableChild(ButtonWidget.builder(label, b -> {
				current = c;
				this.clearAndInit();
			}).dimensions(colX, listTop + i * 20, colW, 18).build());
		}
		if (pages > 1) {
			int py = listTop + perPage * 20;
			this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), b -> {
				catPage--;
				this.clearAndInit();
			}).dimensions(colX, py, 30, 16).build()).active = catPage > 0;
			this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), b -> {
				catPage++;
				this.clearAndInit();
			}).dimensions(colX + colW - 30, py, 30, 16).build()).active = catPage < pages - 1;
		}

		// --- Farbvarianten (rechts) ---
		styleX = colX + colW + 8;
		int areaW = left + panelW - 8 - styleX;
		int cols = panelH < 210 ? 3 : 2;
		int bw = (areaW - (cols - 1) * 4) / cols;
		String selected = staged.getOrDefault(current.id(), "default");

		for (int i = 0; i < Styles.ALL.size(); i++) {
			Style st = Styles.ALL.get(i);
			int cx = styleX + (i % cols) * (bw + 4);
			int cy = listTop + (i / cols) * 22;
			boolean on = st.id().equals(selected);
			Text label = Text.literal(st.name()).formatted(on ? Formatting.GREEN : Formatting.WHITE);
			this.addDrawableChild(ButtonWidget.builder(label, b -> {
				staged.put(current.id(), st.id());
				this.clearAndInit();
			}).dimensions(cx, cy, bw, 20).build());
			swatches.add(new int[] { cx + 4, cy + 5, st.swatch() });
		}

		// --- Fußleiste ---
		int fw = (panelW - 16 - 8) / 3;
		this.addDrawableChild(ButtonWidget.builder(Text.translatable("screen.style_studio.apply"), b -> applyNow())
			.dimensions(left + 8, footerY, fw, 20).build());
		this.addDrawableChild(ButtonWidget.builder(Text.translatable("screen.style_studio.reset"), b -> {
			staged.clear();
			applyNow();
			this.clearAndInit();
		}).dimensions(left + 8 + fw + 4, footerY, fw, 20).build());
		this.addDrawableChild(ButtonWidget.builder(Text.translatable("screen.style_studio.done"), b -> this.close())
			.dimensions(left + 8 + (fw + 4) * 2, footerY, fw, 20).build());
	}

	private void applyNow() {
		StyleConfig.selection = new LinkedHashMap<>(staged);
		StyleConfig.save();
		StylePack.applyAndReload(staged);
	}

	private boolean hasUnappliedChanges() {
		return !StyleConfig.normalize(staged).equals(StyleConfig.normalize(StyleConfig.selection));
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		super.renderBackground(context, mouseX, mouseY, delta);
		// Panel mit dezentem Rahmen
		context.fill(left - 1, top - 1, left + panelW + 1, top + panelH + 1, 0xFF3A3A55);
		context.fill(left, top, left + panelW, top + panelH, 0xE00F0F18);
		context.fill(left, top + 22, left + panelW, top + 23, 0xFF3A3A55);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		context.drawTextWithShadow(this.textRenderer, this.title, left + 8, top + 8, 0xFFFFFFFF);
		context.drawTextWithShadow(this.textRenderer, Text.literal(current.name()), styleX, top + 8, 0xFFFFD84A);

		for (int[] s : swatches) {
			context.fill(s[0], s[1], s[0] + 10, s[1] + 10, 0xFF000000);
			context.fill(s[0] + 1, s[1] + 1, s[0] + 9, s[1] + 9, s[2]);
		}

		if (hasUnappliedChanges()) {
			Text hint = Text.translatable("screen.style_studio.unsaved").formatted(Formatting.YELLOW);
			context.drawTextWithShadow(this.textRenderer, hint, left + 8, top + panelH - 38, 0xFFFFFF55);
		}
	}

	@Override
	public void close() {
		if (this.client != null) this.client.setScreen(parent);
	}
}
