package dev.spiritworker.netty;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import dev.spiritworker.netty.tcp.TcpHandler;
import dev.spiritworker.util.crypto.Crypto;
import io.netty.buffer.ByteBuf;

public abstract class SoulWorkerSession extends TcpHandler {
	private int accountId;
	
	public int getAccountId() {
		return this.accountId;
	}
	
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	public boolean isAuthenticated() {
		return this.accountId != 0;
	}

	@Override
	public void onMessage(ByteBuf data) {	
		// Convert to byte array
		byte[] bytes = new byte[data.readableBytes()];
		data.getBytes(data.readerIndex(), bytes);
		
		// Decrypt
		Crypto.xor(bytes, 5);
		
		// Create packet
		ByteBuffer packet = ByteBuffer.wrap(bytes);
		packet.order(ByteOrder.LITTLE_ENDIAN);
		packet.position(5);
		
		// Get opcode
		int id1 = packet.get() & 0xff;
		int id2 = packet.get() & 0xff;
		int opcode = id2 + (id1 << 8);
		
		// Handle
		handleMessage(opcode, packet);
	}

	protected abstract void handleMessage(int opcode, ByteBuffer packet);
}
