package dev.mutwakil.dogjump.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import dev.mutwakil.dogjump.Room;
import dev.mutwakil.dogjump.res.Assets;
import dev.mutwakil.dogjump.res.Res;
import dev.mutwakil.dogjump.screen.MenuScreen;
import dev.mutwakil.dogjump.util.Utils;
import dev.mutwakil.lh.GameClient;
import dev.mutwakil.lh.GameServer;
import java.util.Random;

public class Dj extends Game{
	public SpriteBatch batch;
	public InputMultiplexer input;
	public static Dj instance;
	public GameServer server;
	public GameClient client;
	public Room room = Room.NONE;
	public String ip="";
	@Override
	public void create() {
		instance = this;
		initRes();
		batch = new SpriteBatch();
		input = new InputMultiplexer();
		Gdx.input.setInputProcessor(input);
		SettingsManager.load();
		setScreen(new MenuScreen());
	}
	private void initRes(){
		Res.init();
	}
	public Drawable getDrawable(String name){
		return VisUI.getSkin().getDrawable(name);
	}
	
	public static float height(float ratio) {
		return Gdx.graphics.getHeight() * ratio;
	}
	public static float width(float ratio) {
		return Gdx.graphics.getWidth() * ratio;
	}
	public static Value width(float f,Actor t){
		return Value.percentWidth(f,t);
	}
	public static Value height(float f,Actor t){
		return Value.percentHeight(f,t);
	}
	public static float size(float x, float y) {
		return Math.min(width(x), height(y));
	}
	public static Value size(float f,Actor actor){
		return Gdx.graphics.getWidth()<Gdx.graphics.getHeight()?Value.percentWidth(f,actor):Value.percentHeight(f,actor);
	}
	public static float size(float f){
		return Gdx.graphics.getWidth()<Gdx.graphics.getHeight()?Dj.width(f):Dj.height(f);
	}
	public static int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	@Override
	public void render() {
	    super.render();	
	}
	@Override
	public void dispose() {
		batch.dispose();
		super.dispose();
		Res.dispose();
		if(server!=null)server.dispose();
		if(client!=null)client.dispose();
		ip="";
	}

}