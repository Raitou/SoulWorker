package dev.spiritworker.net.packet.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import dev.spiritworker.SpiritWorker;
import dev.spiritworker.net.packet.PacketOpcodes;
import dev.spiritworker.util.crypto.Crypto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class PacketWriter {
	// Little endian
	private ByteArrayOutputStream baos;
	private int opcode;

	public PacketWriter(int opcode, int type) {
		this.baos = new ByteArrayOutputStream(128);
		this.opcode = opcode;
		
		this.writeUint16(2);		// Key
		this.writeUint16(0);		// Length (Filler)
		this.writeUint8(type);			// Type
		
		// Opcode (Big endian)
		this.writeUint8(opcode >>> 8);
		this.writeUint8(opcode);
	}
	
	public PacketWriter(int opcode) {
		this(opcode, 1);
	}

	public byte[] getPacket() {
		// Create packet
		byte[] packet = baos.toByteArray();
		
		// Set length
		packet[2] = (byte) packet.length;
		packet[3] = (byte) (packet.length >>> 8);
		
		// Log
		if (opcode != PacketOpcodes.ClientKeepAlive && SpiritWorker.getConfig().LOG_PACKETS) {
			ByteBuf buf = Unpooled.wrappedBuffer(packet);
			SpiritWorker.getLogger().info("Sent:\n" + ByteBufUtil.prettyHexDump(buf));
		}
		
		// Crypto
		Crypto.xor(packet);
		
		// Close baos
		try {
			baos.close();
		} catch (IOException e) {
			
		}
		
		// Done
        return packet;
    }
	
	// Little endian
	
	public void writeEmpty(int i) {
		while (i > 0) {
			baos.write(0);
			i--;
		}
	}
	
	public void writeInt8(byte b) {
        baos.write(b);
    }
	
	public void writeBoolean(boolean b) {
        baos.write(b ? 1 : 0);
    }
	
	public void writeUint8(byte b) {
		// Unsigned byte
        baos.write(b & 0xFF);
    }
	
	public void writeUint8(int i) {

		baos.write((byte) i & 0xFF);
	}
	
	public void writeUint16(int i) {
		// Unsigned short
        baos.write((byte) (i & 0xFF));
        baos.write((byte) ((i >>> 8) & 0xFF));
    }
	
	public void writeUint24(int i) {
		// 24 bit integer
        baos.write((byte) (i & 0xFF));
        baos.write((byte) ((i >>> 8) & 0xFF));
        baos.write((byte) ((i >>> 16) & 0xFF));
    }
	
	public void writeInt16(int i) {
		// Signed short
        baos.write((byte) i);
        baos.write((byte) (i >>> 8));
    }

    public void writeUint32(int i) {
    	// Unsigned int
        baos.write((byte) (i & 0xFF));
        baos.write((byte) ((i >>> 8) & 0xFF));
        baos.write((byte) ((i >>> 16) & 0xFF));
        baos.write((byte) ((i >>> 24) & 0xFF));
    }
    
    public void writeInt32(int i) {
    	// Signed int
        baos.write((byte) i);
        baos.write((byte) (i >>> 8));
        baos.write((byte) (i >>> 16));
        baos.write((byte) (i >>> 24));
    }
    
    public void writeUint32(long i) {
    	// Unsigned int (long)
        baos.write((byte) (i & 0xFF));
        baos.write((byte) ((i >>> 8) & 0xFF));
        baos.write((byte) ((i >>> 16) & 0xFF));
        baos.write((byte) ((i >>> 24) & 0xFF));
    }
    
    public void writeFloat(float f){
    	this.writeUint32(Float.floatToRawIntBits(f));
    }

    public void writeUint64(long l) {
        baos.write((byte) (l & 0xFF));
        baos.write((byte) ((l >>> 8) & 0xFF));
        baos.write((byte) ((l >>> 16) & 0xFF));
        baos.write((byte) ((l >>> 24) & 0xFF));
        baos.write((byte) ((l >>> 32) & 0xFF));
        baos.write((byte) ((l >>> 40) & 0xFF));
        baos.write((byte) ((l >>> 48) & 0xFF));
        baos.write((byte) ((l >>> 56) & 0xFF));
    }
    
    public void writeDouble(double d){
    	long l = Double.doubleToLongBits(d);
    	this.writeUint64(l);
    }
    
    public void writeString16(String s) {
    	this.writeUint16(s.length() * 2);
    	for (int i = 0; i < s.length(); i++) {
    		char c = s.charAt(i);
    		this.writeUint16((short) c);
    	}
    }
    
    public void writeString8(String s) {
    	this.writeUint16(s.length());
    	for (int i = 0; i < s.length(); i++) {
    		char c = s.charAt(i);
    		this.writeUint8((byte) c);
    	}
    }
    
    public void writeDirectString8(String s) {
    	for (int i = 0; i < s.length(); i++) {
    		char c = s.charAt(i);
    		this.writeUint8((byte) c);
    	}
    }
    
    public void writeBytes(byte[] bytes) {
    	try {
			baos.write(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
