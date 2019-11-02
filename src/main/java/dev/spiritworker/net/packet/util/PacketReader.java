package dev.spiritworker.net.packet.util;

public class PacketReader {
	//public final Byte[] data;
	/*
	public PacketReader(final ByteBuffer bb) {
        this.data = bb;
    }
	
	public final int getSize() {
		return this.data.capacity();
	}
	
	public final byte nextInt8() {
		return data.get();
	}
	
	public final int nextUint8() {
		return data.get() & 0xFF;
	}
	
	public final int nextUint16() {
        final int byte1 = data.get() & 0xFF;
        final int byte2 = data.get() & 0xFF;
        return ((byte2 << 8) + byte1);
    }
	
	public final short nextInt16() {
        final int byte1 = data.get() & 0xFF;
        final int byte2 = data.get() & 0xFF;
        return (short) ((byte2 << 8) + byte1);
    }
	
	public final int nextInt32() {
        final int byte1 = data.get() & 0xFF;
        final int byte2 = data.get() & 0xFF;
        final int byte3 = data.get() & 0xFF;
        final int byte4 = data.get() & 0xFF;
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }
	
	public float nextFloat() {
        return Float.intBitsToFloat(nextInt32());
    }
	
	public final long nextLong() {
        final long byte1 = data.get() & 0xFF;
        final long byte2 = data.get() & 0xFF;
        final long byte3 = data.get() & 0xFF;
        final long byte4 = data.get() & 0xFF;
        final long byte5 = data.get() & 0xFF;
        final long byte6 = data.get() & 0xFF;
        final long byte7 = data.get() & 0xFF;
        final long byte8 = data.get() & 0xFF;
        return (byte8 << 56) + (byte7 << 48) + (byte6 << 40) + (byte5 << 32) + (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }
	
	public double nextDouble() {
        return Double.longBitsToDouble(nextLong());
    }
	
	public String nextString16() {
		return nextString16(128);
	}
	
	public String nextString16(int maxLen) {
		StringBuffer sb = new StringBuffer();
		int i;
        while (this.data.remaining() > 1 && sb.length() < maxLen) {
            i = nextUint16();
            if (i == 0) { // End of string
            	break;
            } else {
            	sb.append((char) i);
            }
        }
        return sb.toString();
	}
	
	public String nextString8(){
		return nextString8(128);
	}
	
	public String nextString8(int maxLen) {
		StringBuffer sb = new StringBuffer();
		int i;
		while (this.data.remaining() > 0 && sb.length() < maxLen) {
            i = nextUint8();
            if (i == 0) { // End of string
            	break;
            } else {
            	sb.append((char) i);
            }
        }
        return sb.toString();
	}
	*/
}
