package dev.mutwakil.dogjump.util;

 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import deep.richi.libgdx.rtl.RtlController;
import deep.richi.libgdx.rtl.RtlFreeTypeFontGenerator;
import dev.mutwakil.dogjump.MainActivity;
import dev.mutwakil.dogjump.game.Dj;
import java.util.UUID;

public class Utils {
	public static BitmapFont genrateRtlFont(String name,int size) {
		RtlFreeTypeFontGenerator generator = new RtlFreeTypeFontGenerator(Gdx.files.internal(name));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters += RtlController.getInstance().getAllRtlChars();
		parameter.size = fontSize(size);
		parameter.color = Color.WHITE;
		return generator.generateRtlFont(parameter);
	}
	public static BitmapFont genrateRtlFont(String name){
		return genrateRtlFont(name,48);
	}
	public static BitmapFont genrateRtlFont(String name,int size,boolean useEnglishDigits) {
		RtlFreeTypeFontGenerator generator = new RtlFreeTypeFontGenerator(Gdx.files.internal(name));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters += RtlController.getInstance().getAllRtlChars();
		parameter.size = fontSize(size);
		parameter.color = Color.WHITE;
		return generator.generateRtlFont(parameter,useEnglishDigits);
	}
	public static BitmapFont genrateRtlFontWithBorders(String name,int size,boolean useEnglishDigits) {
		RtlFreeTypeFontGenerator generator = new RtlFreeTypeFontGenerator(Gdx.files.internal(name));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters += RtlController.getInstance().getAllRtlChars();
		parameter.size = fontSize(size);
		parameter.color = Color.WHITE;
		parameter.borderWidth=1;
		parameter.borderColor=Color.BLACK;
		return generator.generateRtlFont(parameter,useEnglishDigits);
	}
	public static BitmapFont genrateRtlFont(String name,boolean useEnglishDigits){
		return genrateRtlFont(name,48,useEnglishDigits);
	}
	public static String generateUUID(){
		return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
	}
	public static int getRandomNumber(){
		return Dj.getRandom(0,9653);
	}
	public static int fontSize(int size){
		int baselineWidth = 1080;
		int baselineHeight = 2340;
		float baseline = (baselineWidth + baselineHeight) / 2f;
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		float current = (screenWidth + screenHeight) / 2f;
		float scale = current / baseline;
		return Math.round(size * scale);
	}
	public static String transliterateArabicToEnglish(String input) {
		String withoutDiacritics = input.replaceAll("[\\u064B-\\u0652]", "");
		 String transliterated= withoutDiacritics.replace("ا", "a")
				.replace("أ","a")
				.replace("آ","a")
				.replace("إ","a")
		.replace("ب", "b")
		.replace("ت", "t")
		.replace("ث", "th")
		.replace("ج", "j")
		.replace("ح", "h")
		.replace("خ", "kh")
		.replace("د", "d")
		.replace("ذ", "dh")
		.replace("ر", "r")
		.replace("ز", "z")
		.replace("س", "s")
		.replace("ش", "sh")
		.replace("ص", "s")
		.replace("ض", "d")
		.replace("ط", "t")
		.replace("ظ", "z")
		.replace("ع", "a")
		.replace("غ", "gh")
		.replace("ف", "f")
		.replace("ق", "q")
		.replace("ك", "k")
		.replace("ل", "l")
		.replace("م", "m")
		.replace("ن", "n")
		.replace("ه", "h")
		.replace("و", "w")
		.replace("ي", "y")
		.replace("ى","a")
		.replace("ؤ","ou")
		.replace("ئ","eu")
		.replace("ء","au")
		.replace("ة","a"); 
		return transliterated.replaceAll("[^a-zA-Z0-9]", "");
	}
}