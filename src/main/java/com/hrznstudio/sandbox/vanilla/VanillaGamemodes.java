package com.hrznstudio.sandbox.vanilla;

import com.hrznstudio.sandbox.api.Gamemode;
import com.hrznstudio.sandbox.vanilla.gamemode.GamemodeVanilla;

public class VanillaGamemodes {
    public static final Gamemode DEBUG = new GamemodeVanilla(Gamemode.properties("debug").setDisplayName("Debug").setRichImage("gm_debug"));
    public static final Gamemode SURVIVAL = new GamemodeVanilla(Gamemode.properties("survival").setDisplayName("Survival").setRichImage("gm_survival"));
    public static final Gamemode CREATIVE = new GamemodeVanilla(Gamemode.properties("creative").setDisplayName("Creative").setRichImage("gm_creative"));
}