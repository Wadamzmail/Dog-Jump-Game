package dev.mutwakil.dogjump.ui.ingame;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.widget.VisDialog;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.screen.GameScreen;
import dev.mutwakil.dogjump.script.Onigais;
import dev.mutwakil.dogjump.ui.BaseAlertWindow;
import dev.mutwakil.dogjump.ui.MyLabel;
import dev.mutwakil.dogjump.ui.MyTextButton;

public class SendReCreateRequest extends BaseAlertWindow {
	private GameScreen gs;
	public SendReCreateRequest(GameScreen gs){
		super();
		this.gs=gs;
		bg.clear();
		bg.add(table).width(Dj.width(0.98f,bg)).center();
		MyLabel label = new MyLabel("في إنتظار قبول الخصم لطلب الإعادة");
		MyTextButton cancel = new MyTextButton("إلغاء الطلب");
		table.add(label).padTop(Dj.size(0.05f)).padBottom(Dj.size(0.05f)).center().row();
		table.add(cancel).width(width(0.4f)).height(Dj.size(0.06f,this)).center();
		/*
		gs.myOnigai = Onigais.RECREATE_PLEASE;
		if(gs.isClient())gs.game.client.sendTCP(Onigais.RECREATE_PLEASE);	
		if(gs.isServer())gs.game.client.sendTCP(Onigais.RECREATE_PLEASE);
		*/
		cancel.setOnClickListener((event,x,y)->{
			gs.myOnigai = Onigais.NONE;
			if(gs.isClient()){
				gs.game.client.sendTCP(Onigais.RECREATE_CANCELED);
			}
			if(gs.isServer()){
				gs.game.server.sendTCP(Onigais.RECREATE_CANCELED);
			}
			close();
		});
	}
	@Override
	public VisDialog show(Stage stage) {
		gs.myOnigai = Onigais.RECREATE_PLEASE;
		if(gs.isClient())gs.game.client.sendTCP(Onigais.RECREATE_PLEASE);
		if(gs.isServer())gs.game.server.sendTCP(Onigais.RECREATE_PLEASE);
		return super.show(stage);	
	}
}