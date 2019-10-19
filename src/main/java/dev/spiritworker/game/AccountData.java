package dev.spiritworker.game;

import dev.morphia.annotations.*;

@Entity(value = "accounts", noClassnameStored = true)
public class AccountData {
	@Id private int id;
	
	@Indexed(options = @IndexOptions(unique = true))
	@Collation(locale = "simple", caseLevel = true)
	private String username;
	private String password;
	private String salt;
	
	@Indexed(options = @IndexOptions(unique = true)) 
	private String token;
	
	private long soulcash;
	
	public AccountData() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getSoulCash() {
		return soulcash;
	}

	public void setSoulCash(long soulcash) {
		this.soulcash = soulcash;
	}
}
