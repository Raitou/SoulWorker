package dev.spiritworker.netty.tcp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import dev.spiritworker.SpiritWorker;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

public abstract class TcpHandler extends ChannelInboundHandlerAdapter {
	private Channel channel;
    private ChannelHandlerContext ctx;
    
    public InetSocketAddress getAddress() {
    	return this.channel != null ? (InetSocketAddress) this.channel.remoteAddress() : null;
    }
    
    public String getAddressString() {
    	InetSocketAddress address = getAddress();
		return address != null ? address.getHostString() : null;
	}
    
    public boolean isOpen() {
    	return this.channel != null && this.channel.isActive();
    }
    
    public boolean isClosed() {
    	return this.channel == null || !this.channel.isActive();
    }
    
    public boolean isWritable() {
    	return this.channel != null && this.channel.isWritable();
    }
    
    @Override
	public void channelRegistered(ChannelHandlerContext ctx) {
		// Register channel
		this.ctx = ctx;
		this.channel = ctx.channel();
		// Fire event
		this.onConnect(ctx);
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) {
		// Close
		this.close();
		// On disconnect
		this.onDisconnect(ctx);
	}
    
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        onMessage(buf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	if (!cause.toString().contains("An existing connection was forcibly closed by the remote host")) {
    		cause.printStackTrace();
    	}
        ctx.close();
    }
    
    public void trySendPacket(byte[] packet) {
    	if (packet != null) {
    		sendPacket(packet);
    	}
    }
    
    public void sendPacket(byte[] packet) {
    	ByteBuf buf = Unpooled.wrappedBuffer(packet);
		this.ctx.writeAndFlush(buf);
    }
    
    public void close() {
    	if (this.isOpen() && this.ctx != null) {
    		ctx.close();
    	}
    }
    
    protected void logPacket(ByteBuffer buf) {
		ByteBuf b = Unpooled.wrappedBuffer(buf.array());
    	logPacket(b);
    } 
    
    protected void logPacket(ByteBuf buf) {
    	SpiritWorker.getLogger().info("Received:\n" + ByteBufUtil.prettyHexDump(buf));
    	buf.release();
    } 
    
    // Events
    
    public abstract void onConnect(ChannelHandlerContext ctx);
    
    public abstract void onDisconnect(ChannelHandlerContext ctx);

    public abstract void onMessage(ByteBuf data);
}
