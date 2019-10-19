package dev.spiritworker.netty;

import java.nio.ByteOrder;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class SoulWorkerPacketDecoder extends LengthFieldBasedFrameDecoder {

	public SoulWorkerPacketDecoder() {
		super(ByteOrder.LITTLE_ENDIAN, 4096, 2, 2, -4, 0, false);
	}

}
