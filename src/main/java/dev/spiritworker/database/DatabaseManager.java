package dev.spiritworker.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.spiritworker.SpiritWorker;
import dev.spiritworker.game.AccessKey;
import dev.spiritworker.game.AccountData;
import dev.spiritworker.game.character.CharacterSkills;
import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.game.inventory.Item;
import dev.spiritworker.game.inventory.data.BankUpgradeData;

public class DatabaseManager {
	private static MongoClient mongoClient;
	private static Morphia morphia;
	private static Datastore datastore;
	
	private static Class<?>[] mappedClasses = new Class<?>[] {
		DatabaseCounter.class, AccessKey.class, AccountData.class, GameCharacter.class, Item.class, BankUpgradeData.class, CharacterSkills.class
	};
    
    public static MongoClient getMongoClient() {
        return mongoClient;
    }
    
    public static Datastore getDatastore() {
    	return datastore;
    }
    
    public static Connection getConnection() throws SQLException {
        return null;
    }
	
	public static void initialize() {
		// Initialize
		mongoClient = new MongoClient(new MongoClientURI(SpiritWorker.getConfig().DatabaseUrl));
		morphia = new Morphia();
		
		// Map
		morphia.map(mappedClasses);
		
		// Build datastore
		datastore = morphia.createDatastore(mongoClient, SpiritWorker.getConfig().DatabaseCollection);
		datastore.ensureIndexes();
	}
	
	public static int getNextId(Object o) {
		DatabaseCounter counter = getDatastore().createQuery(DatabaseCounter.class).field("_id").equal(o.getClass().getSimpleName()).find().tryNext();
		if (counter == null) {
			counter = new DatabaseCounter(o.getClass().getSimpleName());
		}
		try {
			return counter.getNextId();
		} finally {
			getDatastore().save(counter);
		}
	}
}
