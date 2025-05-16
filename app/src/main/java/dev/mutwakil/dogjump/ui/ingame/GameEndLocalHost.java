package dev.mutwakil.dogjump.ui.ingame;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.res.Res;
import dev.mutwakil.dogjump.screen.GameScreen;
import dev.mutwakil.dogjump.ui.BaseAlertWindow;
import dev.mutwakil.dogjump.ui.MyLabel;
import dev.mutwakil.dogjump.ui.MyTextButton;

public class GameEndLocalHost extends BaseAlertWindow {
	private GameScreen gs;
	private MyLabel win,info;
	private MyTextButton reCreate;
	String winStr = "لقد فزت";
	String lose = "خسارة";
	String good = "كانت لعبة ملحمية";
	String bad = "لديك %1$s حمار عليك إرجاعه في المرة القادمة";
	
	public GameEndLocalHost(GameScreen gs){
		super();
		this.gs=gs;
		setBackground(VisUI.getSkin().getDrawable("dialogDim"));
		clear();
		add(bg).width(Dj.size(0.65f,this));
		bg.clear();
		bg.add(table).width(Dj.width(0.98f,bg)).center();
		win = new MyLabel("");
		info = new MyLabel("");
		win.getStyle().font=Res.titleFont;
		win.setStyle(win.getStyle());
		reCreate = new MyTextButton("بدأ الماتش");
		reCreate.setOnClickListener((event,x,y)->{
			gs.reCreateRequest.show(getStage());
		});
		table.add(win).row();
		table.add(info).padTop(Dj.size(0.01f)).row();
		table.add(reCreate).width(Dj.width(0.4f,table)).padTop(Dj.size(0.02f));
		table.pack();
	}
	@Override
	public VisDialog show(Stage stage) {
		if(gs.isServer()){
			if(gs.bottoms.size()==0){
				Res.playSfx(Res.WIN);
				win.getStyle().fontColor=Res.DEEP_RED;
				win.setText(lose);
				info.setText(String.format(bad,gs.donkeysBottom));
				}else if(gs.tops.size()==0){
				win.getStyle().fontColor=Res.DEEP_GREEN;
				win.setText(winStr);
				info.setText(good);
			}
			}else if(gs.isClient()){
			if(gs.tops.size()==0){
				Res.playSfx(Res.LOSE);
				win.getStyle().fontColor=Res.DEEP_RED;
				win.setText(lose);
				info.setText(String.format(bad,gs.donkeysTop));
				}else if(gs.bottoms.size()==0){
				win.getStyle().fontColor=Res.DEEP_GREEN;
				win.setText(winStr);
				info.setText(good);
			}
		}
		table.pack();
		return super.show(stage);
	}
}