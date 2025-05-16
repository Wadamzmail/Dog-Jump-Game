package dev.mutwakil.dogjump.ui.wifihost;

import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.ui.BaseAlertWindow;
import dev.mutwakil.dogjump.ui.MyLabel;
import dev.mutwakil.dogjump.ui.MyTextButton;
import dev.mutwakil.dogjump.ui.MyTextField;

public class EnterWifiRoomIp extends BaseAlertWindow {
	private MyTextField ipField;
	private MyLabel hint;
	private MyTextButton join;
	public EnterWifiRoomIp(){
		super();
		hint = new MyLabel("أدخل عنوان الغرفة الظاهر\n في شاشة اللاعب الآخر");
		ipField = new MyTextField();
		join = new MyTextButton("إنضمام");
		join.setOnClickListener((event,x,y)->{});
		table.add(hint).width(Dj.size(0.9f,table)).padBottom(Dj.size(0.015f,this)).center().row();
		table.add(ipField).width(Dj.size(0.8f,table)).height(Dj.size(0.06f,this)).padBottom(Dj.size(0.015f,this)).center().row();
		table.add(join).width(Dj.size(0.5f,table)).height(Dj.size(0.06f,this)).padBottom(Dj.size(0.015f,this)).center().row();
	}
}