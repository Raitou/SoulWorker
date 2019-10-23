package dev.spiritworker.game.inventory;

import org.bson.types.ObjectId;

import dev.morphia.annotations.AlsoLoad;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Indexed;
import dev.morphia.annotations.PostLoad;
import dev.morphia.annotations.Transient;
import dev.spiritworker.database.DatabaseHelper;
import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.game.data.SoulWorker;
import dev.spiritworker.game.data.def.ItemDef;
import dev.spiritworker.net.packet.util.PacketWriter;
import dev.spiritworker.util.Utils;

@Entity(value = "items", noClassnameStored = true)
public class Item {
	public static final int UNKNOWN2 = 91675916;
	
	@Id
	private ObjectId id;
	private int itemId;
	@Transient private ItemDef itemDef;
	
	@Transient private int uniqueId = 0;
	private static int nextUniqueId = 0;
	
	private int tab = InventorySlotType.NORMAL.getValue();
	private int slot;
	private int count;
	private int durability;
	private int dyeColor;
	private String inner;
	
	private int upgrades;
	private int usedUpgrades;
	
	private int defence;
	@AlsoLoad("weaponDamage") private int damage;
	
	@Indexed private int characterId;
	@Indexed private int accountId;
	
	private static String defaultInner = "000000000000000";
	
	// DO NOT USE THIS CONSTRUCTOR - THIS IS FOR MORPHIA
	public Item() {
		this.uniqueId = ++nextUniqueId;
	}
	
	public Item(int itemId, int count) {
		this(itemId);
		this.count = count;
	}
	
	public Item(int itemId) {
		this();
		this.id = new ObjectId();
		this.itemId = itemId;
		this.count = 1;
		this.inner = defaultInner;
		
		this.itemDef = SoulWorker.getItemDefById(this.itemId);
		if (this.itemDef != null) {
			if (itemDef.getDamageMax() > 0) {
				this.damage = Utils.randomRange(itemDef.getDamageMin(), itemDef.getDamageMax());
			}
			if (itemDef.getDefenceMax() > 0) {
				this.defence = Utils.randomRange(itemDef.getDefenceMin(), itemDef.getDefenceMax());
			}
			this.durability = itemDef.getMaxDurability();
		}
	}
	
	public ObjectId getObjectId() {
		return this.id;
	}
	
	public int getUniqueId() {
		return this.uniqueId;
	}

	public int getItemId() {
		return itemId;
	}
	
	public ItemDef getItemDef() {
		return this.itemDef;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public int getCharacterId() {
		return characterId;
	}

	public int getAccountId() {
		return accountId;
	}
	
	public void setOwner(GameCharacter owner) {
		this.characterId = owner.getId();
		this.accountId = owner.getAccountId();
	}
	
	public int getTab() {
		return this.tab;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int tab, int slot) {
		this.tab = tab;
		this.slot = slot;
	}
	
	public int getWeaponDamage() {
		return this.damage;
	}
	

	public int getDefence() {
		return this.defence;
	}
	
	public int getUpgrades() {
		return this.upgrades;
	}
	
	public void addUpgrade() {
		this.upgrades++;
	}
	
	public int getUsedUpgrades() {
		return this.usedUpgrades;
	}
	
	public void addUsedUpgrades() {
		this.usedUpgrades++;
	}
	
	public int getDurability() {
		return this.durability;
	}
	
	public void setDurability(int durability) {
		this.durability = durability;
	}
	
	public int getDyeColor() {
		return dyeColor;
	}

	public void setDyeColor(int dyeColor) {
		this.dyeColor = dyeColor;
	}

	public void write(PacketWriter p) {
		p.writeUint8(getTab());
		p.writeUint16(getSlot());	// Slot # in the inventory tab
		p.writeInt32(getCount() > 0 ? itemId : -1);		// Item id 
		// Item metadata
		writeMetadata(p);
	}
	
	public void writeMetadata(PacketWriter p) {
		p.writeInt32(this.uniqueId);	// Item unique id
		p.writeInt32(UNKNOWN2);		// Unknown (shows up in character list packet though)
		p.writeUint16(this.getCount()); // Count
		p.writeUint8(1);				// Soulbound probably
		p.writeUint8(0);				// Strength????
		p.writeUint8(0);				// Nothing
		p.writeUint8(0);				// Nothing
		p.writeEmpty(27);
		p.writeUint8(getUpgrades());	// Upgrade
		p.writeUint8(getDurability());
		p.writeEmpty(9);
		p.writeUint8(getUsedUpgrades());	// Used upgrade amount
		p.writeEmpty(6);
		p.writeString8(inner);
		p.writeEmpty(3);
		p.writeUint32(getWeaponDamage());	// Weapon damage
		p.writeUint32(getDefence());		// Defence
		p.writeEmpty(4);
		p.writeUint32(getDyeColor());
		p.writeEmpty(1);
	}

	public static void writeEmpty(PacketWriter p, int tab, int slot) {
		p.writeUint8(tab);
		p.writeUint16(slot);
		p.writeInt32(-1);
		p.writeInt32(-1);	
		p.writeInt32(-1);
		p.writeEmpty(51);
		p.writeString8(defaultInner);
		p.writeEmpty(20);
	}
	
	public void save() {
		if (this.count > 0 && this.characterId != 0) {
			DatabaseHelper.saveItem(this);
		} else {
			DatabaseHelper.deleteItem(this);
		}
	}
	
	@PostLoad 
	public void onLoad() {
		if (this.getItemDef() == null) {
			this.itemDef = SoulWorker.getItemDefById(getItemId());
		}
	}
}
