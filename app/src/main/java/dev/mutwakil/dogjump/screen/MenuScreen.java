package dev.mutwakil.dogjump.screen;

import android.opengl.GLES20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.game.GameType;
import dev.mutwakil.dogjump.game.PlayerData;
import dev.mutwakil.dogjump.res.Assets;
import dev.mutwakil.dogjump.res.Res;
import dev.mutwakil.dogjump.ui.EditProfileDialog;
import dev.mutwakil.dogjump.ui.Licenses;
import dev.mutwakil.dogjump.ui.MyImageButton;
import dev.mutwakil.dogjump.ui.MyLabel;
import dev.mutwakil.dogjump.ui.MyTextButton;
import dev.mutwakil.dogjump.ui.SettingsDialog;
import dev.mutwakil.dogjump.ui.require.EnterYourNameWindow;
import dev.mutwakil.dogjump.ui.wifihost.ChoseHostModeDialog;

public class MenuScreen extends ScreenAdapter {
	private Dj game;
	private VisTable table,lTable,mTable,rTable;
	private Stage stage;
	private Viewport viewport;
	private MyTextButton local,host,license;
	private MyImageButton profile,settings;
	private VisImage bg;
	private MyLabel label;
	
	public MenuScreen(){
		this.game = Dj.instance;
	}
	private void initUi(){
		//table.debug();
		lTable = new VisTable();
		mTable = new VisTable();
		rTable = new VisTable();
		table.add(lTable).growX().top();
		table.add(mTable).growY().width(Dj.size(0.75f));
		table.add(rTable).growX().top();
		
		label = new MyLabel("نطة كلب");
		label.getStyle().fontColor=Color.WHITE;
		label.getStyle().font = Res.BLAKA_REGULAR;
		label.setStyle(label.getStyle());
		
		local = new MyTextButton("لعب على الأرض");
		host = new MyTextButton("غرفة WIFI");
		license = new MyTextButton("LICENSES");
		mTable.defaults().padBottom(Dj.size(0.020f));
		mTable.add(label).spaceBottom(Dj.size(0.5f)).row();
		mTable.add(local).width(Dj.size(0.5f,mTable)).height(Dj.size(0.08f)).center().row();
		mTable.add(host).width(Dj.size(0.5f,mTable)).height(Dj.size(0.08f)).center().row();
		//mTable.add(license).width(Dj.size(0.5f,mTable)).height(Dj.size(0.08f)).center();
		profile = new MyImageButton(Res.drawable(Res.IC_PERSON));
		settings = new MyImageButton(Res.drawable(Res.IC_SETTINGS));
		profile.getImage().setSize(Dj.size(0.05f),Dj.size(0.05f));
		settings.getImage().setSize(Dj.size(0.05f),Dj.size(0.05f));
		rTable.add(profile).size(Dj.size(0.1f)).padTop(Dj.size(0.1f)).padRight(Dj.size(0.025f)).right().top().row();
		lTable.add(settings).size(Dj.size(0.1f)).padTop(Dj.size(0.1f)).padLeft(Dj.size(0.025f)).left().top().row();
	}
	private void listeners(){
		local.setOnClickListener( (event,x,y)->game.setScreen(new GameScreen(GameType.TABLE)));
		host.setOnClickListener((event,x,y)->new ChoseHostModeDialog().show(stage));
		profile.setOnClickListener((event,x,y)->new EditProfileDialog().show(stage));	
		settings.setOnClickListener((event,x,y)->new SettingsDialog().show(stage));
		license.setOnClickListener((event,x,y)->new Licenses().show(stage));
	}
	@Override
	public void show() {
		super.show();
		viewport = new ScreenViewport();
		stage = new Stage(viewport);
		game.input.addProcessor(stage);
		bg = new VisImage();
		bg.setSize(Dj.width(1f),Dj.height(1f));
		bg.setDrawable(Res.drawable(Res.MBG));
		bg.setScaling(Scaling.fill);
		stage.addActor(bg);
		table = new VisTable();
		table.setFillParent(true);
		stage.addActor(table);
		initUi();
		listeners();
		PlayerData.load();
		if(PlayerData.get().name.equals("")){
			PlayerData.get().validate();
			new EnterYourNameWindow().show(stage);
		}
	}
	@Override
	public void hide() {
		super.hide();
		game.input.removeProcessor(stage);
	}
	@Override
	public void render(float delta) {
		super.render(delta);
		Gdx.gl.glClearColor(0.5f,0.5f,0.5f,1f);
		Gdx.gl.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		game.batch.setProjectionMatrix(viewport.getCamera().combined);
		stage.act(delta);
		stage.draw();
	}
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width,height);
	}
	 private void dispose(TextureRegionDrawable d){
		 try{d.getRegion().getTexture().dispose();}catch(Exception e){}
	 }
	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
	}
}