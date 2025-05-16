package dev.mutwakil.dogjump.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import dev.mutwakil.dogjump.model.SettingsModel;

public class SettingsManager {
	private static final String SETTINGS_FILE = "settings.json";
	private static final Json json = new Json();
	private static SettingsModel settings;
	
	private SettingsManager(){
		
	}
	
	public static void load() {
		FileHandle file = Gdx.files.local(SETTINGS_FILE);
		if (file.exists()) {
			settings = json.fromJson(SettingsModel.class, file.readString());
			settings.validate();
			} else {
			settings = new SettingsModel();
			save();
		}
	}
	
	public static void save() {
		FileHandle file = Gdx.files.local(SETTINGS_FILE);
		file.writeString(json.toJson(settings), false);
	}
	
	public static SettingsModel get() {
		if (settings == null) load();
		return settings;
	}
	
	public static void update(SettingsModel newSettings) {
		settings = newSettings;
		save();
	}
}
