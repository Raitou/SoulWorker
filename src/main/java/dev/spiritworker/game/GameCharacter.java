package dev.spiritworker.game;

import dev.spiritworker.Constants;
import dev.spiritworker.database.DatabaseHelper;
import dev.spiritworker.game.data.SoulWorker;
import dev.spiritworker.game.inventory.Inventory;
import dev.spiritworker.game.inventory.Item;
import dev.spiritworker.game.inventory.data.BankUpgradeData;
import dev.spiritworker.game.inventory.data.InventoryUpgradeData;
import dev.spiritworker.net.packet.util.PacketWriter;
import dev.spiritworker.netty.SoulWorkerSession;
import dev.spiritworker.util.Position;

import dev.morphia.annotations.*;

@Entity(value = "characters", noClassnameStored = true)
public class GameCharacter {
	@Id
	private int id;
	private int accountId;
	
	@Transient
	private SoulWorkerSession owner;

	@Indexed(options = @IndexOptions(unique = true))
	@Collation(locale = "simple", caseLevel = true)
	private String name;
	private int type; // Character class
	private int level;
	private int exp;
	
	private int[] emotes;
	
	private int energy;
	private int extraEnergy;
	
	private short hairStyle;
	private short hairColor;
	private short eyeColor;
	private short skinColor;
	
	private Position position;
	private float angle;
	
	@Transient private GameMap map;
	private int mapId;

	private long money;
	private long ether;
	private long bp;
	
	
	@Transient private Inventory inventory;
	@Transient private BankUpgradeData upgradeDataBank;
	private InventoryUpgradeData upgradeData;
	
	public GameCharacter() {
		this.inventory = new Inventory(this);
		this.emotes = new int[Constants.MAX_EMOTE_SLOTS];
		this.upgradeData = new InventoryUpgradeData();
	}
	
	public GameCharacter(SoulWorkerSession owner) {
		this();
		this.owner = owner;
		this.accountId = owner.getAccountId();
		this.level = 1;
		this.exp = 0;
		this.energy = 200;
		this.position = new Position(10000f, 10000f, 100f); // (100, 100, 1)
		this.mapId = 10003;
		this.angle = 0f;
		
		// Initial emotes
		for (int i = 0; i < Constants.MAX_EMOTE_SLOTS; i++) {
			getEmotes()[i] = 7000 + i;
		}
	}
	
	public SoulWorkerSession getSession() {
		return this.owner;
	}
	
	public void setSession(SoulWorkerSession session) {
		this.owner = session;
	}

	public int getAccountId() {
		return this.owner.getAccountId();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int i) {
		this.level = i;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getExpMax() {
		// TODO
		return 100;
	}

	public short getHairStyle() {
		return hairStyle;
	}

	public void setHairStyle(int hairStyle) {
		this.hairStyle = (short) hairStyle;
	}

	public short getHairColor() {
		return hairColor;
	}

	public void setHairColor(int hairColor) {
		this.hairColor =  (short) hairColor;
	}

	public short getEyeColor() {
		return eyeColor;
	}

	public void setEyeColor(int eyeColor) {
		this.eyeColor = (short) eyeColor;
	}

	public short getSkinColor() {
		return skinColor;
	}

	public void setSkinColor(int skinColor) {
		this.skinColor = (short) skinColor;
	}
	
	public int[] getEmotes() {
		return this.emotes;
	}
	
	public int getEnergy() {
		return this.energy;
	}

	public int getExtraEnergy() {
		return this.extraEnergy;
	}
	
	private void setEnergy(short e) {
		this.energy = e;
	}
	
	private void setExtraEnergy(short e) {
		this.extraEnergy = e;
	}
	
	public int getTotalEnergy() {
		return this.getEnergy() + this.getExtraEnergy();
	}
	
	public boolean hasEnoughEnergy(int e) {
		return this.getTotalEnergy() >= e;
	}
	
	public void useEnergy(int usedEnergy) {
		if (this.extraEnergy > 0) {
			int consumed = Math.min(usedEnergy, this.extraEnergy);
			this.extraEnergy -= consumed;
			usedEnergy -= consumed;
		}
		
		if (usedEnergy > 0) {
			this.energy -= usedEnergy;
		}
	}

	public int getDistrictId() {
		return this.mapId; // 10003 = default
	}
	
	public void setMapId(short mapId) {
		this.mapId = mapId;
	}

	public Position getPosition() {
		return position;
	}

	public float getAngle() {
		return this.angle;
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
	}

	public Inventory getInventory() {
		return inventory;
	}
	
	public BankUpgradeData getBankUpgradeData() {
		return this.upgradeDataBank;
	}
	
	public void setBankUpgradeData(BankUpgradeData data) {
		this.upgradeDataBank = data;
	}
	
	public InventoryUpgradeData getInventoryUpgradeData() {
		return this.upgradeData;
	}

	public int getGmLevel() {
		return 1;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long dzenai) {
		this.money = dzenai;
	}

	public long getEther() {
		return ether;
	}

	public void setEther(long ether) {
		this.ether = ether;
	}

	public long getBp() {
		return bp;
	}

	public void setBp(long bp) {
		this.bp = bp;
	}
	
	public int getMapId() {
		return this.mapId;
	}
	
	public GameMap getMap() {
		return this.map;
	}
	
	public void setMap(GameMap map) {
		this.map = map;
		if (map != null) {
			this.mapId = map.getMapId();
		}
	}
	
	public void save() {
		// Save to database
		DatabaseHelper.saveCharacter(this);
	}
	
	public void writeMainData(PacketWriter p) {
		// Main data
		p.writeUint32(this.getId());
		p.writeString16(this.getName());
		p.writeUint8(this.getType());
		p.writeUint8(0); // Class advancement??
		p.writeUint32(0); // Portrait
		p.writeUint16(this.getHairStyle());
		p.writeUint16(this.getHairColor());
		p.writeUint16(this.getEyeColor());
		p.writeUint16(this.getSkinColor());
		p.writeUint64(0);
		p.writeUint16(this.getLevel());
		p.writeEmpty(10);
		
		// Current weapon
		Item weapon = this.getInventory().getWeapon();
		p.writeInt32(weapon != null ? weapon.getItemId() : -1); // Weapon item id
		p.writeUint8(0); // Unknown
		p.writeInt32(-1); // Unknown
	}
	
	public void writeCosmetics(PacketWriter p) {
		// Cosmetics
		for (int e = 0; e < 13; e++) {
			Item item = getInventory().getCosmeticItems().getItemAt(e);
			if (item != null) {
				p.writeInt32(-1); // ?
				p.writeInt32(Item.UNKNOWN2); // ?
				p.writeInt32(item.getItemId()); // Equipped item id
			} else {
				p.writeInt32(-1); // ?
				p.writeInt32(-1); // ?
				p.writeInt32(-1); // Equipped item id
			}
			p.writeUint32(0);
			p.writeInt32(-1);
			p.writeInt32(-1);
			p.writeInt32(-1); 
			p.writeUint32(0);
		}
	}
	
	public void writeExtraData(PacketWriter p) {
		p.writeUint32(0); // Unknown
		p.writeUint32(0); // 1st Title 
		p.writeUint32(0); // 2nd Title
		p.writeUint32(0); // Guild id?
		p.writeUint16(0); // Guild name (string16)
		p.writeUint32(0); // Unknown
		
		p.writeUint32(1450); // Current Hp
		p.writeUint32(1450); // Base Hp
		p.writeUint32(200); // Unknown
		p.writeUint32(200); // Unknown
		p.writeUint32(0); // Unknown
		p.writeUint32(0); // Unknown
		p.writeUint32(100); // Soul energy/Stamina??
		p.writeUint32(100); // Soul energy/Stamina??
		p.writeUint32(0); // Unknown
		p.writeUint32(0); // Unknown
		p.writeFloat(100f); // Soul energy/Stamina??
		p.writeFloat(100f); // Soul energy/Stamina??
		p.writeUint16(0); // Unknown
		p.writeUint8(0); // Unknown
		p.writeUint16(this.getEnergy()); // Energy
		p.writeUint16(this.getExtraEnergy()); // Extra Energy
		p.writeEmpty(14);
		p.writeUint16(this.getDistrictId()); // Map id (10021 == candus city??)
		p.writeUint16(101); // Unknown
		p.writeUint16(256); // Unknown
		p.writeUint16(this.getDistrictId()); // Unknown
		p.writeUint16(1); // Unknown
		p.writeFloat(this.getPosition().getX()); // Position X
		p.writeFloat(this.getPosition().getZ()); // Position Y
		p.writeFloat(this.getPosition().getY()); // Position Z
		p.writeFloat(-115.499969f); // Rotation?
		p.writeFloat(507.5f); // Unknown
		p.writeFloat(507.5f); // Unknown
	}
}
