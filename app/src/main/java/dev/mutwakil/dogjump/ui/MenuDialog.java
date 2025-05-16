package dev.mutwakil.dogjump.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.game.GameType;
import dev.mutwakil.dogjump.res.Res;
import dev.mutwakil.dogjump.screen.GameScreen;
import dev.mutwakil.dogjump.screen.MenuScreen;

public class MenuDialog extends BaseDialog {
	private MyTextButton.VisTextButtonStyle textButtonStyle;
	private MyTextButton resume,replay,mainMenu,settings;
	private GameScreen game;
	public MenuDialog(String s){
		super(s);
		table.defaults().growX().padBottom(height(0.02f)).padLeft(width(0.1f)).padRight(width(0.1f)).center();
		 resume = new MyTextButton("مواصلة");
		 replay = new MyTextButton("إعادة");
		 mainMenu = new MyTextButton("القائمة الرئيسية");
		 settings = new MyTextButton("الإعدادات");
		 table.add(resume).row();
		 table.add(replay).row();
		 table.add(mainMenu).row();
		 table.add(settings);
		 listeners();
	}
	public MenuDialog(GameScreen game){
		this("القائمة");
		this.game = game;	
	}
	void listeners(){
		resume.setOnClickListener((event,x,y)->close());
		replay.setOnClickListener((event,x,y)->{
			if(game.gType==GameType.TABLE)
					game.reCreate(); 
			if(game.isClient()||game.isServer())
				game.reCreateRequest.show(getStage());
				close();
			});
		mainMenu.setOnClickListener((event,x,y)->Dj.instance.setScreen(new MenuScreen()));
		settings.setOnClickListener((event,x,y)->new SettingsDialog().show(getStage()));		 
	}
	 
}