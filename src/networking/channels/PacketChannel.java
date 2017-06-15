package networking.channels;

public class PacketChannel extends ObjectChannel {

	public PacketChannel(String name) {
		super(name);
	}

	/**
	 * implemented methods
	 */
	
	@Override
	protected void recieve(Object obj) {
		if (obj instanceof Packet) super.con.getStreamManager().incoming(con, this, (Packet) obj);
		else console.error("Wrong type: "+obj.getClass().getName());
	}

	@Override
	public ChannelType getType() {
		return ChannelType.PACKET;
	}

}
