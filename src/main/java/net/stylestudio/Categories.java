package net.stylestudio;

import java.util.List;

public final class Categories {
	private Categories() {}

	private static final String ARMOR_PIECES = "(netherite|diamond|iron|golden)";

	public static final List<StyleCategory> ALL = List.of(
		StyleCategory.of("crystal",  "Kristalle",       1.0f, "textures/entity/end_crystal/.*\\.png"),
		StyleCategory.of("obsidian", "Obsidian",        1.9f, "textures/block/(crying_)?obsidian\\.png"),
		StyleCategory.of("mace",     "Mace",            1.0f, "textures/item/mace\\.png"),
		StyleCategory.of("trident",  "Trident",         1.0f, "textures/entity/trident.*\\.png", "textures/item/trident.*\\.png"),
		StyleCategory.of("sky",      "Himmel",          1.0f, "textures/environment/.*(sun|moon|clouds|end_sky).*\\.png"),
		StyleCategory.of("sword",    "Schwerter",       1.0f, "textures/item/" + ARMOR_PIECES + "_sword\\.png"),
		StyleCategory.of("pickaxe",  "Spitzhacken",     1.0f, "textures/item/" + ARMOR_PIECES + "_pickaxe\\.png"),
		StyleCategory.of("axe",      "Äxte",            1.0f, "textures/item/" + ARMOR_PIECES + "_axe\\.png"),
		StyleCategory.of("armor",    "Rüstung",         1.0f,
			"textures/entity/equipment/humanoid(_leggings)?/(netherite|diamond|iron|gold)\\.png",
			"textures/item/" + ARMOR_PIECES + "_(helmet|chestplate|leggings|boots)\\.png"),
		StyleCategory.of("totem",    "Totem",           1.0f, "textures/item/totem_of_undying\\.png"),
		StyleCategory.of("pearl",    "Enderperle",      1.0f, "textures/item/ender_pearl\\.png"),
		StyleCategory.of("gapple",   "Goldener Apfel",  1.0f, "textures/item/(enchanted_)?golden_apple\\.png"),
		StyleCategory.of("shield",   "Schild",          1.0f, "textures/entity/shield.*\\.png"),
		StyleCategory.of("fire",     "Feuer",           1.0f, "textures/block/fire_[01]\\.png"),
		StyleCategory.of("anchor",   "Respawn-Anker",   1.0f, "textures/block/respawn_anchor_.*\\.png"),
		StyleCategory.of("cobweb",   "Spinnennetz",     1.0f, "textures/block/cobweb\\.png"),
		StyleCategory.of("stone",    "Stein",           1.0f, "textures/block/(stone|cobblestone|stone_bricks)\\.png"),
		StyleCategory.of("deepslate","Deepslate",       1.5f, "textures/block/(deepslate|cobbled_deepslate|polished_deepslate|deepslate_bricks)\\.png"),
		StyleCategory.of("snow",     "Schnee",          1.0f, "textures/block/(snow|powder_snow)\\.png"),
		StyleCategory.of("glowstone","Glowstone",       1.0f, "textures/block/glowstone\\.png"),
		StyleCategory.of("hotbar",   "Hotbar",          1.0f, "textures/gui/sprites/hud/hotbar.*\\.png")
	);
}
