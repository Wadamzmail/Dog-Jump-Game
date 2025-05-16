package dev.mutwakil.dogjump.ui.wifihost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.StringBuilder;
import deep.richi.libgdx.rtl.RtlController;
import dev.mutwakil.dogjump.Room;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.game.GameType;
import dev.mutwakil.dogjump.game.PlayerData;
import dev.mutwakil.dogjump.res.Res;
import dev.mutwakil.dogjump.screen.GameScreen;
import dev.mutwakil.dogjump.ui.BaseAlertWindow;
import dev.mutwakil.dogjump.ui.MyLabel;
import dev.mutwakil.lh.GameServer;
import dev.mutwakil.lh.ServerState;

public class WaitingWifiJoinDialog extends BaseAlertWindow {
	private MyLabel yourIp,ip,waiting;
	public WaitingWifiJoinDialog(){
		super();
		yourIp = new MyLabel(yourIp(true));
		ip = new MyLabel(ip(""));
		ip.getStyle().font=Res.titleFont;
		ip.setStyle(ip.getStyle());
		waiting = new MyLabel("في إنتظار اللاعب للإنضمام");
		waiting.setWrap(true);
		table.add(yourIp).width(Dj.size(0.9f,table)).padBottom(Dj.size(0.015f,this)).center().row();
		table.add(ip).width(Dj.size(0.9f,table)).padBottom(Dj.size(0.015f,this)).center().row();
		table.add(waiting).width(Dj.size(0.8f,table)).padBottom(Dj.size(0.015f,this)).center().row();
		PlayerData.load();
		if(Dj.instance.server!=null)Dj.instance.server.dispose();
		Dj.instance.server = new GameServer(null,new ServerState(){
			@Override
			public void created(String _ip) {
				Gdx.app.postRunnable(()->ip.setText(ip(_ip)));
			}

			@Override
			public void closed(String _ip) {
				Gdx.app.postRunnable(()->ip.setText(ip(_ip)));
			}
			
			@Override
			public void connected() {
				Gdx.app.postRunnable(()->{
					hide();
					Dj.instance.room=Room.SERVER;
					Dj.instance.setScreen(new GameScreen(GameType.LOCALHOST));
					});
			}

			@Override
			public void disconnected() {
				
			}

		});
		
		close.setOnClickListener((event,x,y)->{
			Dj.instance.server.dispose();
			Dj.instance.server=null;
			hide();
		});
	}
	public String yourIp(boolean isSucces){
		String result ="عنوان ال IP الخاص بالغرفة :";
		return RtlController.getInstance().getRtl(result).toString();
	}
	public String ip(String s){
		return RtlController.getInstance().getRtl(s).toString();
	}
	@Override
	public void hide() {
		super.hide();
	}
}