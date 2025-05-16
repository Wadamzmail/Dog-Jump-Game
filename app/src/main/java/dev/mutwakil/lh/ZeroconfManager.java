package dev.mutwakil.lh;

import com.bfo.zeroconf.*;
import com.bfo.zeroconf.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZeroconfManager {

  private Zeroconf zeroconf;
  private Service announcedService;
  private List<Service> discoveredServices = new ArrayList<>();
  private ZeroconfServiceListener listener;

  public interface ZeroconfServiceListener {
    void onServiceDiscovered(Service service);

    void onServiceRemoved(Service service);
  }

  public ZeroconfManager(ZeroconfServiceListener listener) {
    this.listener = listener;
    /*   new Thread(()->{
    this.zeroconf = new Zeroconf();
    setupListeners();
    }).start();*/
  }

  public ZeroconfManager() {}
	public void setListener(ZeroconfServiceListener listener)  {
		this.listener = listener;
	}

  private void setupListeners() {
    zeroconf.addListener(
        new ZeroconfListener() {
          @Override
          public void serviceAnnounced(Service service) {
            if (service.getType().equals("_dogjump._tcp")) {
              discoveredServices.add(service);
              if (listener != null) {
                listener.onServiceDiscovered(service);
              }
            }
          }

          @Override
          public void serviceExpired(Service service) {
            discoveredServices.remove(service);
            if (listener != null) {
              listener.onServiceRemoved(service);
            }
          }
        });
  }

  public void announceService(String name, int port, String owner,String ip) {
    new Thread(
            () -> {
              if (zeroconf == null) this.zeroconf = new Zeroconf();
              Service service =
                  new Service.Builder()
                      .setName(name)
                      .setType("_dogjump._tcp")
                      .setPort(port)
                      .put("owner", owner)
                      .put("ip", ip)
                      .build(zeroconf);
              service.announce();
              this.announcedService = service;
            })
        .start();
  }

  public void stopAnnouncement() {
    if (announcedService != null) {
      announcedService.cancel();
      announcedService = null;
    }
  }

  public void discoverServices() {
    new Thread(
            () -> {
              if (zeroconf == null) {
                this.zeroconf = new Zeroconf();
                setupListeners();
              }
             zeroconf.query("_dogjump._tcp", null);
            })
        .start();
  }

  public List<Service> getDiscoveredServices() {
    return new ArrayList<>(discoveredServices);
  }

  public void close() {
    new Thread(
            () -> {
              if (announcedService != null) {
                announcedService.cancel();
              }
              try {
                if (zeroconf != null) zeroconf.close();
              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();
  }
}
