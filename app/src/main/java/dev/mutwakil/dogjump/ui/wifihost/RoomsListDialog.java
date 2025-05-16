package dev.mutwakil.dogjump.ui.wifihost;

import com.badlogic.gdx.Gdx;
import com.bfo.zeroconf.Service;
import com.kotcrab.vis.ui.widget.VisTable;
import dev.mutwakil.dogjump.Room;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.game.GameType;
import dev.mutwakil.dogjump.screen.GameScreen;
import dev.mutwakil.dogjump.ui.BaseAlertWindow;
import dev.mutwakil.dogjump.ui.BaseDialog;
import dev.mutwakil.dogjump.ui.MyTextButton;
import dev.mutwakil.lh.GameClient;
import dev.mutwakil.lh.ZeroconfManager;

public class RoomsListDialog extends BaseDialog {
	private VisTable listTable;
	private ZeroconfManager zm;
	public RoomsListDialog(){
		super("الإنضمام لغرفة");
		 listTable = new VisTable();
		 //table.add(listTable).grow();
		 zm = new ZeroconfManager(new ZeroconfManager.ZeroconfServiceListener(){
			@Override
			public void onServiceDiscovered(Service service) {
				Gdx.app.postRunnable(()->upd());
			}

			@Override
			public void onServiceRemoved(Service service) {
				Gdx.app.postRunnable(()->upd());
			}
			 
		 });
		 zm.discoverServices();
		 
	}
	public void upd(){
		table.clear();
		 zm.getDiscoveredServices().forEach(service->{
			 MyTextButton bttn = new MyTextButton(service.getText().get("owner").toString());
			 bttn.setOnClickListener((event,x,y)->{
				 hide();
				 Dj.instance.room = Room.CLIENT;
				 Dj.instance.ip = service.getText().get("ip").toString();
				 Dj.instance.setScreen(new GameScreen(GameType.LOCALHOST));
			 });
			 table.add(bttn).width(Dj.size(0.8f,table)).height(Dj.size(0.07f,this)).row();
			 pack();
		 });
	}
	@Override
	public void hide() {
		super.hide();
		zm.close();
	}
}