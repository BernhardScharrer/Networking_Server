package bernhard.scharrer.netapi.server;

import java.net.Socket;

import bernhard.scharrer.netapi.packet.Packet;

public class Client {
	
	private TCPChannel channel;
	private UDPChannel uchannel;
	
	private Console console;
	private String ip;
	private int port;
	private int uport;
	private int buffer;
	private TrafficManager manager;
	
	private int uuid;
	private boolean connected;
	private int cuid;
	private static int count_uuid = 0;
	
	Client(TrafficManager manager, Socket socket, Console console, int uport, int buffer) {
		
		this.manager = manager;
		this.ip = socket.getInetAddress().getHostAddress();
		this.port = socket.getPort();
		this.uport = uport;
		this.buffer = buffer;
		this.channel = new TCPChannel(this, manager, socket, console);
		this.console = console;
		this.uuid = count_uuid++;
		
		if (this.channel.isBound()) {
			connected = true;
			manager.connect(this);
		} else {
			cleanUp();
		}
		
	}
	
	void bindUDP() {
		
		this.cuid = UDPChannel.getFreeSlot();
		
		if (uport != -1) {
			if (uchannel==null) {
				uchannel = new UDPChannel(this);
				send("\r\r\r;"+uport+";"+buffer+";"+cuid);
			} else {
				console.warn("UDP modul  has already been bound to this client!");
			}
		} else {
			send("\r\r\r;-1");
		}
	}
	
	int getCUID() {
		return cuid;
	}
	
	void cleanUp() {
		connected = false;
		manager.disconnect(this);
		console.debug("Cleaning up client.");
		if (channel!=null) channel.cleanUp();
		if (uchannel!=null) uchannel.cleanUp();
	}
	
	public void send(String message) {
		if (channel!=null) channel.send(message);
	}
	
	public void send(Packet packet) {
		if (channel!=null) channel.send(packet);
	}
	
	public void send(int[] data) {
		if (uchannel!= null) uchannel.send(data);
	}
	
	public void send(float[] data) {
		if (uchannel!= null) uchannel.send(data);
	}

	public String getIP() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}

	public int getUUID() {
		return uuid;
	}

	public boolean isConnected() {
		return connected;
	}
	
}
