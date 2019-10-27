package dev.spiritworker.database;

import java.util.List;

import com.mongodb.WriteResult;

import dev.morphia.query.Query;
import dev.morphia.query.internal.MorphiaCursor;
import dev.spiritworker.game.AccessKey;
import dev.spiritworker.game.AccountData;
import dev.spiritworker.game.character.CharacterSkills;
import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.game.inventory.InventorySlotType;
import dev.spiritworker.game.inventory.Item;
import dev.spiritworker.game.inventory.data.BankUpgradeData;
import dev.spiritworker.netty.SoulWorkerSession;

public class DatabaseHelper {
	
	public static AccountData createAccount(String username, String token) {
		// Unique names only
		MorphiaCursor<AccountData> cursor = DatabaseManager.getDatastore().createQuery(AccountData.class).field("username").equalIgnoreCase(username).find();
		
		if (cursor.hasNext()) {
			return null;
		}
		
		// Account
		AccountData account = new AccountData();
		account.setId(DatabaseManager.getNextId(account));
		account.setUsername(username);
		account.setToken(token);
		DatabaseManager.getDatastore().save(account);
		return account;
	}
	
	public static AccountData getAccountByToken(String token) {
		MorphiaCursor<AccountData> cursor = DatabaseManager.getDatastore().createQuery(AccountData.class).field("token").equal(token).find();
		if (!cursor.hasNext()) return null;
		return cursor.next();
	}
	
	public static AccountData getAccountById(int id) {
		MorphiaCursor<AccountData> cursor = DatabaseManager.getDatastore().createQuery(AccountData.class).field("_id").equal(id).find();
		if (!cursor.hasNext()) return null;
		return cursor.next();
	}
	
	public static boolean deleteAccount(String username) {
		Query<AccountData> q = DatabaseManager.getDatastore().createQuery(AccountData.class).field("username").equal(username);
		return DatabaseManager.getDatastore().findAndDelete(q) != null;
	}
	
	public static void createAccessKey(SoulWorkerSession session) {
		AccessKey key = new AccessKey(session);
		DatabaseManager.getDatastore().save(key);
	}
	
	public static AccessKey getAccessKey(int id) {
		MorphiaCursor<AccessKey> cursor = DatabaseManager.getDatastore().createQuery(AccessKey.class).field("_id").equal(id).find();
		if (!cursor.hasNext()) return null;
		return cursor.next();
	}
	
	public static boolean deleteAccessKey(int id) {
		Query<AccessKey> q = DatabaseManager.getDatastore().createQuery(AccessKey.class).field("_id").equal(id);
		return DatabaseManager.getDatastore().findAndDelete(q) != null;
	}
	
	public static long getCharacterCount(SoulWorkerSession session) {
		return DatabaseManager.getDatastore().createQuery(GameCharacter.class).field("accountId").equal(session.getAccountId()).count();
	}
	
	public static List<GameCharacter> getCharacterList(SoulWorkerSession session) {
		return DatabaseManager.getDatastore().createQuery(GameCharacter.class).field("accountId").equal(session.getAccountId()).find().toList();
	}
	
	public static boolean isCharacterNameTaken(String name) {
		MorphiaCursor<GameCharacter> cursor = DatabaseManager.getDatastore().createQuery(GameCharacter.class).field("name").equalIgnoreCase(name).find();
		return cursor.hasNext();
	}
	
	public static GameCharacter getCharacterById(SoulWorkerSession session, int characterId) {
		Query<GameCharacter> query = DatabaseManager.getDatastore().createQuery(GameCharacter.class);
		query.and(
			query.criteria("_id").equal(characterId),
			query.criteria("accountId").equal(session.getAccountId())
		);
		MorphiaCursor<GameCharacter> cursor = query.find();
		
		if (!cursor.hasNext()) return null;
		
		GameCharacter character = cursor.next();
		character.setSession(session);
		
		return character;
	}
	
	public static GameCharacter createCharacter(SoulWorkerSession owner) {
		GameCharacter character = new GameCharacter(owner);
		character.setId(DatabaseManager.getNextId(character));
		DatabaseManager.getDatastore().save(character);
		return character;
	}
	
	public static void saveCharacter(GameCharacter character) {
		DatabaseManager.getDatastore().save(character);
	}
	
	public static boolean deleteCharacter(int characterId) {
		Query<GameCharacter> q = DatabaseManager.getDatastore().createQuery(GameCharacter.class).field("_id").equal(characterId);
		return DatabaseManager.getDatastore().findAndDelete(q) != null;
	}
	
	public static void saveItem(Item item) {
		DatabaseManager.getDatastore().save(item);
	}
	
	public static boolean deleteItem(Item item) {
		WriteResult result = DatabaseManager.getDatastore().delete(item);
		return result.wasAcknowledged();
	}
	
	public static boolean deleteInventoryAndEquippedItems(GameCharacter character) {
		Query<Item> query = DatabaseManager.getDatastore().createQuery(Item.class);
		query.and(
			query.criteria("characterId").equal(character.getId()),
			query.criteria("tab").lessThanOrEq(InventorySlotType.PREMIUM.getValue())
		);
		
		return DatabaseManager.getDatastore().delete(query).wasAcknowledged();
	}

	public static List<Item> getInventoryItems(GameCharacter character) {
		Query<Item> query = DatabaseManager.getDatastore().createQuery(Item.class);
		query.and(
			query.criteria("characterId").equal(character.getId()),
			query.criteria("tab").lessThanOrEq(InventorySlotType.PREMIUM.getValue())
		);
		
		return query.find().toList();
	}
	
	public static List<Item> getBankItems(GameCharacter character) {
		Query<Item> query = DatabaseManager.getDatastore().createQuery(Item.class);
		query.and(
			query.criteria("accountId").equal(character.getAccountId()),
			query.criteria("tab").greaterThanOrEq(InventorySlotType.BANK_NORMAL.getValue())
		);
		
		return query.find().toList();
	}
	
	public static List<Item> getCosmeticItems(GameCharacter character) {
		Query<Item> query = DatabaseManager.getDatastore().createQuery(Item.class);
		query.and(
			query.criteria("characterId").equal(character.getId()),
			query.criteria("tab").equal(InventorySlotType.COSMETIC.getValue())
		);
		
		return query.find().toList();
	}
	
	public static Item getWeaponItem(GameCharacter character) {
		Query<Item> query = DatabaseManager.getDatastore().createQuery(Item.class);
		query.and(
			query.criteria("characterId").equal(character.getId()),
			query.criteria("tab").equal(InventorySlotType.EQUIPPED.getValue()),
			query.criteria("slot").equal(0)
		);
		
		MorphiaCursor<Item> result = query.find();
		
		return result.hasNext() ? result.next() : null;
	}

	public static BankUpgradeData getBankUpgradeData(GameCharacter character) {
		MorphiaCursor<BankUpgradeData> cursor = DatabaseManager.getDatastore().createQuery(BankUpgradeData.class).filter("_id", character.getAccountId()).find();
		
		if (!cursor.hasNext()) {
			return new BankUpgradeData(character);	
		}
		
		return cursor.next();
	}

	public static void saveBankUpgradeData(BankUpgradeData bankUpgradeData) {
		DatabaseManager.getDatastore().save(bankUpgradeData);
	}
	
	public static void saveCharacterSkills(CharacterSkills skills) {
		DatabaseManager.getDatastore().save(skills);
	}
	
	public static CharacterSkills getCharacterSkills(GameCharacter character) {
		MorphiaCursor<CharacterSkills> cursor = DatabaseManager.getDatastore().createQuery(CharacterSkills.class).filter("_id", character.getId()).find();
		if (!cursor.hasNext()) return null;
		return cursor.next();
	}
}
