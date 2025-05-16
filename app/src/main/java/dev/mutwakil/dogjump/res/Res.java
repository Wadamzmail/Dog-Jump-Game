package dev.mutwakil.dogjump.res;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import deep.richi.libgdx.rtl.RtlController;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.game.SettingsManager;
import dev.mutwakil.dogjump.res.Assets.Fonts;
import dev.mutwakil.dogjump.res.Assets.Images;
import dev.mutwakil.dogjump.res.Assets.Sfx;
import dev.mutwakil.dogjump.util.Utils;

public class Res {
    public static Texture APP_ICON,
     BACKGROUND,BG,BOTTOM,BOX_DOWN,
     BOX_UP,BTTN_DOWN,BTTN_UP,CANCEL_DOWN,
     CANCEL_UP,CLOSE,DTABLE_BG,HIGHLIGHT,
     IC_PAUSE,IC_PERSON,IC_SETTINGS,MENU,
     SLIDER,SLIDER_KNOB,SLIDER_KNOB_DOWN,
     TABLE_BG,TABLE_BG_GREEN,TARGET,TF_BG,
     TF_CURSOR,TF_SELECTION,TITLE_BG,TOP,
	 WINDOW_DIM,MBG;
    public static MyAssetManager assetManager;
    public static Sound MOVE,EXPLOSION,BUTTON_CLICK,
	WIN,LOSE;
    public static BitmapFont font,
    titleFont,turnFont,BLAKA_REGULAR,CAIRO_SEMIBOLD; 
    public static Color DEEP_ORANGE,DEEP_GREEN,DEEP_RED;
    
    public static void init() {
        if (!isLoaded()) {
            VisUI.load(SkinScale.X2);
            assetManager = new MyAssetManager();
            load();
        }
    }

    private static void load() {
        DEEP_ORANGE = Color.valueOf("#E67319");
		DEEP_RED = Color.valueOf("#E63946");
		DEEP_GREEN=Color.valueOf("#4CAF32");
		CAIRO_SEMIBOLD = Utils.genrateRtlFont(Fonts.CAIRO_SEMIBOLD,28,true);
        font = Utils.genrateRtlFont(Fonts.CAIRO_BOLD,32, true);
        turnFont = Utils.genrateRtlFont(Fonts.CAIRO_BOLD, 50, true);
        BLAKA_REGULAR = Utils.genrateRtlFontWithBorders(Fonts.BLAKA_REGULAR,190,true);
        titleFont = Utils.genrateRtlFont(Fonts.CAIRO_BOLD,64, true);//80
        assetManager.load(Images.APP_ICON, Texture.class);
        assetManager.load(Images.BOTTOM, Texture.class);
        assetManager.load(Images.BTTN_UP, Texture.class);
        assetManager.load(Images.TARGET, Texture.class);
        assetManager.load(Images.TOP, Texture.class);
        assetManager.load(Images.BG, Texture.class);
        assetManager.load(Images.TABLE_BG, Texture.class);
        assetManager.load(Images.BACKGROUND, Texture.class);
        assetManager.load(Images.BTTN_DOWN, Texture.class);
        assetManager.load(Images.BOX_UP, Texture.class);
        assetManager.load(Images.BOX_DOWN, Texture.class);
        assetManager.load(Images.MENU, Texture.class);
        assetManager.load(Images.CANCEL_UP, Texture.class);
        assetManager.load(Images.CANCEL_DOWN, Texture.class);
        assetManager.load(Images.TITLE_BG, Texture.class);
        assetManager.load(Images.CLOSE, Texture.class);
        assetManager.load(Sfx.BUTTON_CLICK, Sound.class);
        assetManager.load(Images.SLIDER, Texture.class);
        assetManager.load(Images.SLIDER_KNOB, Texture.class);
        assetManager.load(Images.SLIDER_KNOB_DOWN, Texture.class);
        assetManager.load(Sfx.EXPLOSION, Sound.class);
        assetManager.load(Images.TF_BG, Texture.class);
        assetManager.load(Images.TF_SELECTION, Texture.class);
        assetManager.load(Images.TF_CURSOR, Texture.class);
        assetManager.load(Images.IC_PERSON, Texture.class);
        assetManager.load(Images.IC_PAUSE, Texture.class);
        assetManager.load(Images.IC_SETTINGS, Texture.class);
        assetManager.load(Images.DTABLE_BG, Texture.class);
        assetManager.load(Images.HIGHLIGHT, Texture.class);
        assetManager.load(Images.TABLE_BG_GREEN, Texture.class);
        assetManager.load(Sfx.MOVE, Sound.class);
		assetManager.load(Images.WINDOW_DIM,Texture.class);
		assetManager.load(Images.MBG,Texture.class);
		assetManager.load(Sfx.WIN,Sound.class);
		assetManager.load(Sfx.LOSE,Sound.class);
        assetManager.finishLoading();
        eque();
    }

    private static void eque() {
        APP_ICON = assetManager.get(Images.APP_ICON, Texture.class);
        BOTTOM = assetManager.get(Images.BOTTOM, Texture.class);
        BTTN_UP = assetManager.get(Images.BTTN_UP, Texture.class);
        TARGET = assetManager.get(Images.TARGET, Texture.class);
        TOP = assetManager.get(Images.TOP, Texture.class);
        BG = assetManager.get(Images.BG, Texture.class);
        TABLE_BG = assetManager.get(Images.TABLE_BG, Texture.class);
        BACKGROUND = assetManager.get(Images.BACKGROUND, Texture.class);
        BTTN_DOWN = assetManager.get(Images.BTTN_DOWN, Texture.class);
        BOX_UP = assetManager.get(Images.BOX_UP, Texture.class);
        BOX_DOWN = assetManager.get(Images.BOX_DOWN, Texture.class);
        MENU = assetManager.get(Images.MENU, Texture.class);
        CANCEL_UP = assetManager.get(Images.CANCEL_UP, Texture.class);
        CANCEL_DOWN = assetManager.get(Images.CANCEL_DOWN, Texture.class);
        TITLE_BG = assetManager.get(Images.TITLE_BG, Texture.class);
        CLOSE = assetManager.get(Images.CLOSE, Texture.class);
        BUTTON_CLICK = assetManager.get(Sfx.BUTTON_CLICK, Sound.class);
        SLIDER = assetManager.get(Images.SLIDER, Texture.class);
        SLIDER_KNOB = assetManager.get(Images.SLIDER_KNOB, Texture.class);
        SLIDER_KNOB_DOWN = assetManager.get(Images.SLIDER_KNOB_DOWN, Texture.class);
        EXPLOSION = assetManager.get(Sfx.EXPLOSION, Sound.class);
        TF_BG = assetManager.get(Images.TF_BG, Texture.class);
        TF_SELECTION = assetManager.get(Images.TF_SELECTION, Texture.class);
        TF_CURSOR = assetManager.get(Images.TF_CURSOR, Texture.class);
        IC_PERSON = assetManager.get(Images.IC_PERSON, Texture.class);
        IC_PAUSE = assetManager.get(Images.IC_PAUSE, Texture.class);
        IC_SETTINGS = assetManager.get(Images.IC_SETTINGS, Texture.class);
        DTABLE_BG = assetManager.get(Images.DTABLE_BG, Texture.class);
        HIGHLIGHT = assetManager.get(Images.HIGHLIGHT, Texture.class);
        TABLE_BG_GREEN = assetManager.get(Images.TABLE_BG_GREEN, Texture.class);
        MOVE = assetManager.get(Sfx.MOVE, Sound.class);
		WINDOW_DIM= assetManager.get(Images.WINDOW_DIM,Texture.class);
		MBG = assetManager.get(Images.MBG,Texture.class);
		WIN = assetManager.get(Sfx.WIN,Sound.class);
		LOSE = assetManager.get(Sfx.LOSE,Sound.class);
    }

    public static NinePatchDrawable nineDrawable(Texture texture) {
        int rad = 16;// (int) Dj.size(0.01f);
        NinePatch ninePatch = new NinePatch(texture, rad, rad, rad, rad);
        float pad = Dj.size(0.01f);
        ninePatch.setPadding(pad, 7, pad, pad);
        return new NinePatchDrawable(ninePatch);
    }

    public static void playSfx(Sound sound) {
        if (SettingsManager.get().sfxVolume != 0.0f) {
            sound.play(SettingsManager.get().sfxVolume);
        }
    }

    public static Drawable drawable(Texture texture) {
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    public static boolean isLoaded() {
        return VisUI.isLoaded();
    }

    public static void playMusic(Music music) {
    }
    
    public static void dispose() {
        assetManager.dispose();
        VisUI.dispose();
        APP_ICON.dispose();
        BOTTOM.dispose();
        BTTN_UP.dispose();
        TARGET.dispose();
        TOP.dispose();
        BG.dispose();
        TABLE_BG.dispose();
        BACKGROUND.dispose();
        BTTN_DOWN.dispose();
        BOX_UP.dispose();
        BOX_DOWN.dispose();
        CANCEL_UP.dispose();
        CANCEL_DOWN.dispose();
        TITLE_BG.dispose();
        CLOSE.dispose();
        BUTTON_CLICK.dispose();
        font.dispose();
        BLAKA_REGULAR.dispose();
        titleFont.dispose();
        turnFont.dispose();
        SLIDER.dispose();
        SLIDER_KNOB.dispose();
        SLIDER_KNOB_DOWN.dispose();
        EXPLOSION.dispose();
        TF_BG.dispose();
        TF_SELECTION.dispose();
        TF_CURSOR.dispose();
        IC_PAUSE.dispose();
        IC_PERSON.dispose();
        IC_SETTINGS.dispose();
        DTABLE_BG.dispose();
        HIGHLIGHT.dispose();
        TABLE_BG_GREEN.dispose();
        MOVE.dispose();
		WINDOW_DIM.dispose();
		MBG.dispose();
		WIN.dispose();
		LOSE.dispose();
    }
}