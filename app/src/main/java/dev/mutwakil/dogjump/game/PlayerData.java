package dev.mutwakil.dogjump.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import dev.mutwakil.dogjump.model.PlayerModel;

public class PlayerData {
	private static final String PLAYER_DATA_FILE = "player.json";
	private static final Json json = new Json();
	private static PlayerModel playerData;
	
	private PlayerData(){
		
	}
	
	public static void load() {
		FileHandle file = Gdx.files.local(PLAYER_DATA_FILE);
		if (file.exists()) {
			playerData = json.fromJson(PlayerModel.class, file.readString());
			playerData.validate();
		} else {
			playerData = new PlayerModel();
			//save();
		}
	}
	
	public static void save() {
		FileHandle file = Gdx.files.local(PLAYER_DATA_FILE);
		file.writeString(json.toJson(playerData), false);
	}
	
	public static PlayerModel get() {
		if (playerData == null) load();
		return playerData;
	}
	
	public static void update(PlayerModel newData) {
		playerData = newData;
		save();
	}
}