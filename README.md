# Style Studio

Fabric-Mod für **Minecraft 1.21.11** (Client). Ein Menü, in dem du per Klick das Aussehen von
PvP-relevanten Dingen änderst: Kristalle, Obsidian, Mace, Trident, Himmel, Schwerter, Spitzhacken,
Äxte, Rüstung, Totem, Enderperle, Goldener Apfel, Schild, Feuer, Respawn-Anker, Spinnennetz, Stein,
Deepslate, Schnee, Glowstone und Hotbar.

## Benutzen
1. Mod-JAR + Fabric API in den `mods`-Ordner.
2. Im Spiel **K** drücken (änderbar unter Steuerung) oder den Button **Style Studio** oben links im
   Titelbildschirm / Pausenmenü klicken.
3. Links Kategorie wählen, rechts Farbe anklicken, unten **Anwenden**. Die Texturen werden neu geladen.
4. **Alles zurücksetzen** stellt das normale Minecraft wieder her.

## So funktioniert es
Die Mod liefert **keine fremden Texturen** mit. Beim Anwenden liest sie die Original-Texturen aus deinem
Minecraft-Jar, färbt sie um (`Recolor.java`) und schreibt sie in das Resourcepack
`resourcepacks/StyleStudio-Generated`, das automatisch ganz oben aktiviert wird.
Die Auswahl liegt in `config/style_studio.json`.

## Bauen
- GitHub: pushen, der Workflow `.github/workflows/build.yml` baut die JAR (Reiter *Actions* → Artifact `style-studio`).
- Lokal: Java 21 + Gradle 9.2 (`gradle build`). Für den Gradle Wrapper einmal `gradle wrapper --gradle-version 9.2.1` ausführen.

## Eigene Styles / Kategorien
- Neue Farbe: Eintrag in `Styles.java` (Zielfarbton, Sättigung, Helligkeit, Vorschaufarbe).
- Neue Kategorie: Eintrag in `Categories.java` mit Regex auf den Texturpfad unter `assets/minecraft/`.

## Lizenz
MIT (siehe `LICENSE`). Minecraft-Texturen gehören Mojang und werden nicht mitgeliefert.
