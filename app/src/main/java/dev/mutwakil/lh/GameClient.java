package dev.mutwakil.lh;

import com.bfo.zeroconf.Service;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import dev.mutwakil.dogjump.MainActivity;
import dev.mutwakil.dogjump.model.Move;
import dev.mutwakil.dogjump.model.PlayerModel;
import dev.mutwakil.dogjump.script.Onigais;
import dev.mutwakil.dogjump.script.Ping;
import java.io.IOException;
import java.util.ArrayList;

public class GameClient {
    private Client client;

    public interface OnConnectionStateChanged {
        void onConnected();
        void onDisconnected();
        void onFailed(Exception e);
    }

    public GameClient(String ip, OnReseved lis, OnConnectionStateChanged stateListener) {
        NetworkExecutor.execute(() -> {
            try{
            client = new Client();
			client.getKryo().register(Move.class);
			client.getKryo().register(PlayerModel.class);
			client.getKryo().register(Onigais.class);
			client.getKryo().register(Ping.class);
            client.start();
            
                client.connect(5000, ip, 54555, 54777);
                if (client.isConnected() && stateListener != null) stateListener.onConnected();
            } catch (IOException e) {
                if (stateListener != null) stateListener.onFailed(e);
                return;
            }

            client.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                        lis.onReserved(object);
                }

                @Override
                public void disconnected(Connection connection) {
                    if (stateListener != null) stateListener.onDisconnected();
                }
            });
        });
    }

    public void sendTCP(Object message) {
        NetworkExecutor.execute(() -> {
            if (client != null) {
                client.sendTCP(message);
            }
        });
    }
	public void dispose(){
		//new Thread(()->{
		if(client!=null){
			client.close();
			client.stop();
			NetworkExecutor.shutdown();
			/*try{
			client.dispose();
			}catch(IOException e){
				e.printStackTrace();
				MainActivity.toast(e.toString());
			}*/
		client = null;
		}
		//}).start();
	}
     
    public void ping() {
       NetworkExecutor.execute(()-> client.updateReturnTripTime());
        //sendMessageToServer("ping :"+client.getReturnTripTime());
    }
}