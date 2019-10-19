package dev.spiritworker.game.data;

import java.nio.ByteBuffer;

public abstract class ResourceDef {
	private final int id;
	
	public ResourceDef(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public abstract void loadFromRes(ByteBuffer buf);
	
	public String readString(ByteBuffer buf) {
		int len = buf.getShort();
		StringBuffer sb = new StringBuffer(len);
		int i;
        for (int j = 0; j < len; j++) {
            i = buf.getShort();
            if (i == 0) { // End of string
            	break;
            } else {
            	sb.append((char) i);
            }
        }
        return sb.toString();
	}
}
