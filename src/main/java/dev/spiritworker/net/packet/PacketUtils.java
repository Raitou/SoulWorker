package dev.spiritworker.net.packet;

import java.nio.ByteBuffer;

public class PacketUtils {
	
	public static String readString16(ByteBuffer packet) {
		return readString16(packet, 128);
	}

	public static String readString16(ByteBuffer packet, int maxLen) {
		if (packet.remaining() >= 2) {
			maxLen = packet.getShort() / 2;
		} else {
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		int i;
        while (packet.remaining() > 1 && sb.length() < maxLen) {
            i = packet.getShort();
            if (i == 0) { // End of string
            	break;
            } else {
            	sb.append((char) i);
            }
        }
        return sb.toString();
	}
	
	public static String readString8(ByteBuffer packet){
		return readString8(packet, 128);
	}

	public static String readString8(ByteBuffer packet, int maxLen) {
		if (packet.remaining() >= 2) {
			maxLen = packet.getShort();
		} else {
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		int i;
		while (packet.remaining() > 0 && sb.length() < maxLen) {
            i = packet.get();
            if (i == 0) { // End of string
            	break;
            } else {
            	sb.append((char) i);
            }
        }
        return sb.toString();
	}

}
