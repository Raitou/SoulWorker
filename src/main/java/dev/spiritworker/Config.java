package dev.spiritworker;

public class Config {
	public String AuthServerIp = "127.0.0.1";
	public int AuthServerPort = 9000;
	
	public String GameServerIp = "127.0.0.1";
	public int GameServerPort = 9001;
	public String GameServerName = "Server";
	public final int GameServerId = 1;
	
	public String WorldServerIp = "127.0.0.1";
	public int WorldServerPort = 9002;
	
	public String DatabaseUrl = "mongodb://localhost:27017";
	public String DatabaseCollection = "spiritworker";
	
	public String RESOURCE_FOLDER = "./resources/";
	public static String DATA_FOLDER = "./data/";
	public String PACKETS_FOLDER = "./packets/";
	public boolean LOG_PACKETS = true;
}
