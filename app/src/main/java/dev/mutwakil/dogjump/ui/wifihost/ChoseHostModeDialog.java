package dev.mutwakil.dogjump.ui.wifihost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.widget.VisDialog;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.ui.BaseAlertWindow;
import dev.mutwakil.dogjump.ui.MyTextButton;

public class ChoseHostModeDialog extends BaseAlertWindow {
	private MyTextButton host,join;
	public ChoseHostModeDialog(){
		super();
		host = new MyTextButton("إنشاء غرفة");
		join = new MyTextButton("إنضمام للغرفة");
		table.add(host).width(width(0.5f)).height(Dj.size(0.07f,this)).padBottom(Dj.size(0.015f,this)).row();
		table.add(join).width(width(0.5f)).height(Dj.size(0.07f,this)).padBottom(Dj.size(0.015f,this));
		host.setOnClickListener((event,x,y)->new WaitingWifiJoinDialog().show(getStage()));
		join.setOnClickListener((event,x,y)->new RoomsListDialog().show(getStage()));
	}
	
}