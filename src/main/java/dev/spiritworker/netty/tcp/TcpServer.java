package dev.spiritworker.netty.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public abstract class TcpServer extends Thread {
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootStrap;
    private Channel channel = null;
    private InetSocketAddress address;
    private ChannelInitializer<SocketChannel> serverInitializer;
    
    public TcpServer(InetSocketAddress address) {
    	this.address = address;
    	this.setName("Netty Server Thread");
    }
    
    public InetSocketAddress getAddress() {
    	return this.address;
    }
    
    public ChannelInitializer<SocketChannel> getServerInitializer() {
    	return serverInitializer;
    }
    
    public void setServerInitializer(ChannelInitializer<SocketChannel> socketInitializer) {
    	this.serverInitializer = socketInitializer;
    }
    
    @Override
	public void run() {
    	// Null check
    	if (this.getServerInitializer() == null) {
    		this.setServerInitializer(new TcpServerInitializer(this));
    	}
    	
    	// Start server
    	this.bossGroup = new NioEventLoopGroup(1);
    	this.workerGroup = new NioEventLoopGroup();
        try {
        	this.bootStrap = new ServerBootstrap();
        	this.bootStrap.group(bossGroup, workerGroup)
        		.channel(NioServerSocketChannel.class)
        		.childOption(ChannelOption.TCP_NODELAY, true)
        		.childOption(ChannelOption.SO_REUSEADDR, true)
        		.childOption(ChannelOption.SO_KEEPALIVE, true)
        		.childHandler(getServerInitializer());
        		
            this.channel = null;
			try {
				this.channel = this.bootStrap.bind(this.address).sync().channel();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// End
				return;
			}
			
			// Event
			this.onStart();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
        	// Close
			finish();
        }
	}
    
    // Called when the server thread is finished
    public void finish() { 
    	try {
    		// Close channel and then workers
    		if (this.channel.isOpen()) {
    			this.channel.closeFuture().sync();
    		}
        	if (bossGroup != null) {
        		bossGroup.shutdownGracefully().sync();
        	}
        	if (workerGroup != null) {
        		workerGroup.shutdownGracefully().sync();
        	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // Closes the server
    public void close() { 
    	// Close workers first + channel
    	try {
        	if (bossGroup != null) {
        		bossGroup.shutdownGracefully().sync();
        	}
        	if (workerGroup != null) {
        		workerGroup.shutdownGracefully().sync();
        	}
        	if (this.channel.isOpen()) {
    			this.channel.close().sync();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    // Event
    protected abstract void onStart();
}
