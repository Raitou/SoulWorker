package dev.spiritworker.database;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity(value = "counters", noClassnameStored = true)
public class DatabaseCounter {
	@Id
	private String id;
	private int count;
	
	public DatabaseCounter() {}
	
	public DatabaseCounter(String id) {
		this.id = id;
	}
	
	public int getNextId() {
		int id = ++count;
		return id;
	}
}
