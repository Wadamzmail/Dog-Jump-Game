package dev.mutwakil.dogjump.ui.ingame;

import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.screen.GameScreen;
import dev.mutwakil.dogjump.script.Onigais;
import dev.mutwakil.dogjump.ui.BaseAlertWindow;
import dev.mutwakil.dogjump.ui.MyLabel;
import dev.mutwakil.dogjump.ui.MyTextButton;

public class RespondReCreateRequest extends BaseAlertWindow {
	
	public RespondReCreateRequest(GameScreen gs){
		super();
		bg.clear();
		bg.add(table).width(Dj.width(0.98f,bg)).center();
		MyLabel label = new MyLabel("طلب الخصم إعادة بدأ اللعبة");
		MyTextButton ok = new MyTextButton("قبول");
		MyTextButton reject = new MyTextButton("رفض");
		table.add(label).center().pad(Dj.size(0.02f)).colspan(2).row();
		table.add(reject).height(Dj.size(0.06f,this)).padLeft(Dj.size(0.02f)).padRight(Dj.size(0.02f)).growX().center();
		table.add(ok).height(Dj.size(0.06f,this)).padLeft(Dj.size(0.02f)).padRight(Dj.size(0.02f)).growX().center();
		pack();
		ok.setOnClickListener((event,x,y)->{
			gs.myOnigai = Onigais.NONE;
			if(gs.isClient()){
				gs.game.client.sendTCP(Onigais.RECREATE_ACCEPTED);
			}
			if(gs.isServer()){
				gs.game.server.sendTCP(Onigais.RECREATE_ACCEPTED);
			}
			if(gs.isEnded)
			gs.newMatch();
			 else 
			gs.reCreate();
			hide();
			gs.gameEndLocalHost.hide();
		});
		reject.setOnClickListener((event,x,y)->{
			gs.myOnigai = Onigais.NONE;
			if(gs.isClient()){
				gs.game.client.sendTCP(Onigais.RECREATE_REJECTED);
			}
			if(gs.isServer()){
				gs.game.server.sendTCP(Onigais.RECREATE_REJECTED);
			}
			hide();
		});
	}
}