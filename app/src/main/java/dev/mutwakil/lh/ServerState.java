package dev.mutwakil.lh;

public interface ServerState {
	public void created(String _ip);
	public void closed(String _ip);
	public void connected();
	public void disconnected();
}