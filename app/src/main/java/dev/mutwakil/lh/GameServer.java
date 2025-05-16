package dev.mutwakil.lh;

import com.badlogic.gdx.Gdx;
import com.bfo.zeroconf.Service;
import com.bfo.zeroconf.Zeroconf;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import dev.mutwakil.dogjump.MainActivity;
import dev.mutwakil.dogjump.game.PlayerData;
import dev.mutwakil.dogjump.model.Move;
import dev.mutwakil.dogjump.model.PlayerModel;
import dev.mutwakil.dogjump.script.Onigais;
import dev.mutwakil.dogjump.script.Ping;
import dev.mutwakil.dogjump.util.Utils;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GameServer {
  private Server server;
  private OnReseved listener;
  private ZeroconfManager zm;
  public String ip="not set";
  private ServerState state;

  public GameServer(OnReseved lis,ServerState state) {
    this.listener = lis;
	this.state = state;

    NetworkExecutor.execute(
            () -> {
              zm =
                  new ZeroconfManager(
                      new ZeroconfManager.ZeroconfServiceListener() {

                        @Override
                        public void onServiceDiscovered(Service service) {}

                        @Override
                        public void onServiceRemoved(Service service) {}
                      });
                    
              server = new Server();
			  server.getKryo().register(Move.class);
			  server.getKryo().register(PlayerModel.class);
			  server.getKryo().register(Onigais.class);
			  server.getKryo().register(Ping.class);
                server.start();        
				try{
				server.bind(54555, 54777);
              } catch (IOException e) {
                e.printStackTrace();
				MainActivity.toast(""+e);
				//if(state!=null)state.closed("غير متاح");
              } 
               ip = getLocalIpAddress();
			   zm.announceService(Utils.transliterateArabicToEnglish(PlayerData.get().name),54555,PlayerData.get().name,ip);
			   if(state!=null)state.created(getLocalIpAddress());
              server.addListener(
                  new Listener() {
                    @Override
                    public void connected(Connection connection) {
                      if (server.getConnections().length > 1) {
                        connection.close(); // نرفض الاتصال الجديد
                      } else {
                         if(state!=null)state.connected();
						 connection.setKeepAliveTCP(1000);
                      }
                    }

                    @Override
                    public void disconnected(Connection connection) {
						//if(server.getConnections().length==1)return;
						if(state!=null)state.disconnected();
                      //if(listener!=null)listener.onReserved("Client disconnected: " + connection.getID());
                    }

                    @Override
                    public void received(Connection connection, Object object) {
                       
                        if(listener!=null)listener.onReserved( object);
                      
                    }
                  });
            });
  }
  public GameServer(OnReseved list){
	  this(list,null);
  }
  public void setListeners(OnReseved list,ServerState state){
	  this.listener = list;
	  this.state = state;
  }
  public void sendTCP(Object message) {
    NetworkExecutor.execute(
            () -> {
             /* for (Connection conn : server.getConnections()) {
                conn.sendTCP(message);
              }*/
			  if(server.getConnections().length==0){
				  state.disconnected();
				  return;
			  }
			  server.getConnections()[0].sendTCP(message);
            });
  }

  public int getConnectedClientsCount() {
    return server.getConnections().length;
  }
  public void dispose(){
	//  new Thread(()->{
	 if(server!=null){
		 server.close();
		 server.stop();
		 NetworkExecutor.shutdown();
		/* try{
		 server.dispose();
		 }catch(IOException e){
		 e.printStackTrace();
	     }*/
	 server =null;
	 }
	  zm.close();
	//  }).start();
  }

  public static String getLocalIpAddress() {
    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
          en.hasMoreElements(); ) {
        NetworkInterface intf = en.nextElement();
        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
            enumIpAddr.hasMoreElements(); ) {
          InetAddress inetAddress = enumIpAddr.nextElement();
          if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
            return inetAddress.getHostAddress();
          }
        }
      }
    } catch (SocketException ex) {
      ex.printStackTrace();
    }
    return "";
  }
}
