package dev.spiritworker;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetSocketAddress;

import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.qos.logback.classic.Logger;
import dev.spiritworker.database.DatabaseHelper;
import dev.spiritworker.database.DatabaseManager;
import dev.spiritworker.game.data.ResourceLoader;
import dev.spiritworker.server.auth.AuthServer;
import dev.spiritworker.server.game.GameServer;

public class SpiritWorker {
	private static Logger log = (Logger) LoggerFactory.getLogger(SpiritWorker.class);
	private static Config config;
	
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static File  configFile = new File("./config.json");
	
	public static RunMode MODE = RunMode.BOTH;

	public static void main(String[] args) {
		SpiritWorker.loadConfig();
		
		for (String arg : args) {
			if (arg.equalsIgnoreCase("-auth")) {
				MODE = RunMode.AUTH;
			} else if (arg.equalsIgnoreCase("-game")) {
				MODE = RunMode.GAME;
			}
		}
		
		// Load from resources
		if (MODE != RunMode.AUTH) {
			ResourceLoader.loadDefinitions();
		}
		
		// Load from database
		DatabaseManager.initialize();
		DatabaseHelper.createAccount("Test", "aaaaa");
		
		// Run server
		if (MODE != RunMode.GAME) {
			AuthServer authServer = new AuthServer();
			authServer.start();
		} else {
			GameServer gameServer = new GameServer(SpiritWorker.getConfig().GameServerId, SpiritWorker.getConfig().GameServerName, new InetSocketAddress(SpiritWorker.getConfig().GameServerIp, SpiritWorker.getConfig().GameServerPort));
			gameServer.start();
		}
	}
	
	public static Config getConfig() {
		return config;
	}
	
	public static Logger getLogger() {
		return log;
	}
	
	public static Gson getGsonFactory() {
		return gson;
	}
	
	public static void loadConfig() {
		try (FileReader file = new FileReader(configFile)) {
			// Read from file
			config = gson.fromJson(file, Config.class);
		} catch (Exception e) {
			SpiritWorker.config = new Config();
			saveConfig();
		}
	}
	
	public static void saveConfig() {
		try (FileWriter file = new FileWriter(configFile)) {
			// Save to file
			file.write(gson.toJson(config));
		} catch (Exception e) {
			SpiritWorker.getLogger().error("Config save error");
		}
	}
	
	public enum RunMode {
		BOTH,
		AUTH,
		GAME;
	}
}
