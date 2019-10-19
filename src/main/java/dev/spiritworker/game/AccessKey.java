package dev.spiritworker.game;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import dev.spiritworker.database.DatabaseHelper;
import dev.spiritworker.netty.SoulWorkerSession;

@Entity(value = "access", noClassnameStored = true)
public class AccessKey {
	@Id
	private int id;
	private String ip;
	private long timelimit;
	
	public AccessKey() {

	}
	
	public AccessKey(SoulWorkerSession session) {
		this.id = session.getAccountId();
		this.ip = session.getAddressString();
		this.timelimit = System.currentTimeMillis() + 30000;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public long getTimelimit() {
		return timelimit;
	}
	
	public void setTimelimit(long timelimit) {
		this.timelimit = timelimit;
	}
	
	public static boolean validate(SoulWorkerSession session, int playerId) {
		// Get key from db
		AccessKey key = DatabaseHelper.getAccessKey(playerId);
		
		if (key == null || !key.getIp().equals(session.getAddressString())) {
			// Invalid key for the player
			return false;
		}
		
		if (System.currentTimeMillis() > key.getTimelimit()) {
			// Expired key
			DatabaseHelper.deleteAccessKey(playerId);
			return false;
		}
		
		// Valid key
		DatabaseHelper.deleteAccessKey(playerId);
		return true;
	}
}
