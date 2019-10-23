package dev.spiritworker.game.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.spiritworker.SpiritWorker;
import dev.spiritworker.database.DatabaseHelper;
import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.game.data.SoulWorker;
import dev.spiritworker.game.data.def.PackageDef;
import dev.spiritworker.game.data.def.PackageDef.PackageItemData;
import dev.spiritworker.net.packet.PacketBuilder;

public class Inventory {
	private GameCharacter character;
	
	private Map<Integer, BaseInventoryTab> tabs;
	
	public Inventory(GameCharacter character) {
		this.character = character;
		this.tabs = new HashMap<Integer, BaseInventoryTab>();
		this.initTabs();
	}

	public GameCharacter getCharacter() {
		return character;
	}
	
	public InventoryEquippable getCosmeticItems() {
		return (InventoryEquippable) getTabByType(InventorySlotType.COSMETIC);
	}
	
	public InventoryEquippable getEquippedItems() {
		return (InventoryEquippable) getTabByType(InventorySlotType.EQUIPPED);
	}
	
	private void initTabs() {
		registerTab(new InventoryEquippable(this, InventorySlotType.COSMETIC));
		registerTab(new InventoryEquippable(this, InventorySlotType.EQUIPPED));
		registerTab(new InventoryTab(this, InventorySlotType.NORMAL));
		registerTab(new InventoryTab(this, InventorySlotType.FASHION));
		registerTab(new InventoryTab(this, InventorySlotType.PREMIUM));
		registerTab(new InventoryTab(this, InventorySlotType.BANK_NORMAL));
		registerTab(new InventoryTab(this, InventorySlotType.BANK_FASHION));
		registerTab(new InventoryTab(this, InventorySlotType.BANK_PREMIUM));
	}
	
	private void registerTab(BaseInventoryTab tab) {
		tabs.put((int) tab.getSlotType().getValue(), tab);
	}
	
	public BaseInventoryTab getTabByType(InventorySlotType slotType) {
		return getTabByType(slotType.getValue());
	}
	
	public BaseInventoryTab getTabByType(int id) {
		return this.tabs.get(id);
	}
	
	public InventoryTab getInventoryTabByType(InventorySlotType slotType) {
		return getInventoryTabByType(slotType.getValue());
	}
	
	public InventoryTab getInventoryTabByType(int id) {
		BaseInventoryTab tab = getTabByType(id);
		return tab != null && tab instanceof InventoryTab ? (InventoryTab) tab : null;
	}
	
	public Item getWeapon() {
		return this.getEquippedItems().getItemAt(0);
	}
	
	public void addItem(Item item) {
		// TODO add item to its proper tab
		getInventoryTabByType(InventorySlotType.NORMAL).addItem(item);
	}
	
	public void deleteItem(InventorySlotType slotType, int slot, int count) {
		deleteItem(slotType.getValue(), slot, count);
	}
	
	public void deleteItem(int slotType, int slot, int count) {
		BaseInventoryTab tab = getTabByType(slotType);
		if (tab != null) {
			tab.deleteItem(slot, count);
		}
	}
	
	public synchronized void moveItem(int slotTypeSrc, int slotSrc, int slotTypeDest, int slotDest) {
		BaseInventoryTab tab1 = this.getTabByType(slotTypeSrc);
		BaseInventoryTab tab2 = this.getTabByType(slotTypeDest);
		
		if (tab1 == null || tab2 == null) {
			return;
		}
		
		// Make sure both source and destination slots exist
		if (!tab1.isValidItemSlot(slotSrc) || !tab2.isValidItemSlot(slotDest)) {
			return;
		}
		
		Item itemSrc = tab1.getItemAt(slotSrc);
		
		// Make sure item exists
		if (itemSrc == null) {
			return;
		}
		
		Item itemDest = tab2.getItemAt(slotDest);
		
		tab2.putItem(slotDest, itemSrc);
		tab1.putItem(slotSrc, itemDest);
		
		// Database
		if (itemDest != null) {
			itemDest.save();
		}
		if (itemSrc != null) {
			itemSrc.save();
		}
		
		// Packet
		byte[] packet = PacketBuilder.sendClientItemMove(slotTypeSrc, slotSrc, itemDest, slotTypeDest, slotDest, itemSrc);
		getCharacter().getSession().sendPacket(packet);

		// This updates the character for other people
		if (getCharacter().getMap().getCharacters().size() > 1) {
			if (tab1.getSlotType() == InventorySlotType.COSMETIC) {
				getCharacter().getMap().broadcastPacketFrom(
					getCharacter(), 
					PacketBuilder.sendClientItemUpdate(getCharacter(), slotTypeSrc, slotSrc, itemDest)
				);
			} else if (tab2.getSlotType() == InventorySlotType.COSMETIC) {
				getCharacter().getMap().broadcastPacketFrom(
					getCharacter(), 
					PacketBuilder.sendClientItemUpdate(getCharacter(), slotTypeDest, slotDest, itemSrc)
				);
			}
		}
		
		// Recalc stats
		if (tab1.getSlotType() == InventorySlotType.EQUIPPED || tab2.getSlotType() == InventorySlotType.EQUIPPED) {
			getCharacter().getStats().recalc();
		}
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void divideItem(int slotTypeSrc, int slotSrc, int slotTypeDest, int slotDest, int count) {
		InventoryTab tab1 = this.getInventoryTabByType(slotTypeSrc);
		InventoryTab tab2 = this.getInventoryTabByType(slotTypeDest);
		
		if (tab1 == null || tab2 == null) {
			return;
		}
		
		// Make sure both source and destination slots exist
		if (!tab1.isValidItemSlot(slotSrc) || !tab2.isValidItemSlot(slotDest)) {
			return;
		}
		
		Item itemSrc = tab1.getItemAt(slotSrc);
		
		// Make sure item exists and can be split
		if (itemSrc == null || itemSrc.getCount() <= 1 || count >= itemSrc.getCount()) {
			return;
		}
		
		Item itemDest = tab2.getItemAt(slotDest);
		
		// Make sure item at dest does not exist, if it exists make sure that it can hold the items
		if (itemDest != null) {
			if (itemDest.getItemId() == itemSrc.getItemId() && itemDest.getCount() < 200) {
				// Destination item is the same type as source item, so we can add them together
			} else {
				return;
			}
		} else {
			itemDest = new Item(itemSrc.getItemId());
			itemDest.setCount(0);
		}
		
		// Move
		int move = Math.min(count, 200 - itemDest.getCount());
		
		itemSrc.setCount(itemSrc.getCount() - move);
		itemDest.setCount(move + itemDest.getCount());
		
		// Put item
		if (itemDest.getCount() > 0) {
			// Place
			tab2.putItem(slotDest, itemDest);
			
			// Save to database
			itemSrc.save();
			itemDest.save();
			
			// Packet
			getCharacter().getSession().sendPacket(PacketBuilder.sendClientItemDivide(itemSrc, itemDest));
		}
	}
	
	public synchronized void combineItem(int slotTypeSrc, int slotSrc, int slotTypeDest, int slotDest, int count) {
		InventoryTab tab1 = this.getInventoryTabByType(slotTypeSrc);
		InventoryTab tab2 = this.getInventoryTabByType(slotTypeDest);
		
		if (tab1 == null || tab2 == null) {
			return;
		}
		
		// Make sure both source and destination slots exist
		if (!tab1.isValidItemSlot(slotSrc) || !tab2.isValidItemSlot(slotDest)) {
			return;
		}
		
		Item itemSrc = tab1.getItemAt(slotSrc);
		
		// Make sure item exists
		if (itemSrc == null) {
			return;
		}
		
		Item itemDest = tab2.getItemAt(slotDest);
		
		// Make sure dest item exists and can hold the items
		if (itemDest == null || itemDest.getItemId() != itemSrc.getItemId() || itemDest.getCount() >= 200) {
			return;
		}
		
		// Move
		int move = Math.min(itemSrc.getCount(), Math.min(count, 200 - itemDest.getCount()));
		
		itemSrc.setCount(itemSrc.getCount() - move);
		itemDest.setCount(move + itemDest.getCount());
		
		if (itemSrc.getCount() <= 0) {
			tab1.putItem(itemSrc.getSlot(), null);
			itemSrc.save();
		}
		
		// Put item
		if (itemDest.getCount() > 0) {
			// Place
			tab2.putItem(slotDest, itemDest);
			
			// Save to database
			itemDest.save();
			
			// Packet
			getCharacter().getSession().sendPacket(PacketBuilder.sendClientItemCombine(getCharacter(), itemSrc, itemDest));
		}
	}

	public synchronized void useItem(int slotType, int slot) {
		InventoryTab tab = getInventoryTabByType(slotType);
		
		if (tab == null) {
			return;
		}
		
		Item item = tab.getItemAt(slot);
		
		if (item == null) {
			return;
		}
		
		// Package
		PackageDef packageDef = SoulWorker.getPackageDefs().get(item.getItemId());
		if (packageDef != null) {
			// Not enough space
			if (getInventoryTabByType(InventorySlotType.NORMAL).getFreeSlotsCount() < packageDef.getItems().size()) {
				return;
			} 
			
			// Add items from package
			List<Item> openedItems = new ArrayList<Item>(packageDef.getItems().size());
			for (PackageItemData data : packageDef.getItems()) {
				Item created = new Item(data.getItemId());
				created.setCount(data.getCount());
				this.addItem(created);
				openedItems.add(created);
			}
			
			// Item open successful
			getCharacter().getSession().sendPacket(PacketBuilder.sendClientOpenPackageResult(item, openedItems));
		}
		
		// Use up
		tab.deleteItem(slot, 1);
	}
	
	// Loading
	
	private void loadItem(Item item) {
		if (item.getObjectId() == null) {
			SpiritWorker.getLogger().warn("Item with null object id to be loaded");
			return;
		}
		
		BaseInventoryTab tab = getTabByType(item.getTab());
		if (tab != null) {
			if (tab.getItemAt(item.getSlot()) != null) {
				SpiritWorker.getLogger().error("Two items loaded at the same inventory spot!");
				deleteItem(item.getTab(), item.getSlot(), item.getCount());
			}
			tab.putItem(item.getSlot(), item, false);
		}
	}

	public void loadCosmetics() {
		List<Item> items;
		
		items = DatabaseHelper.getCosmeticItems(getCharacter());
		for (Item item : items) {
			loadItem(item);
		}
		
		Item weapon = DatabaseHelper.getWeaponItem(getCharacter());
		if (weapon != null) {
			loadItem(weapon);
		}
	}
	
	public void loadItems() {
		// Load tab spaces first
		getInventoryTabByType(InventorySlotType.NORMAL).setUpgrades(getCharacter().getInventoryUpgradeData().getNormalTabUpgrades());
		getInventoryTabByType(InventorySlotType.FASHION).setUpgrades(getCharacter().getInventoryUpgradeData().getFashionTabUpgrades());
		
		getCharacter().setBankUpgradeData(DatabaseHelper.getBankUpgradeData(getCharacter()));
		
		getInventoryTabByType(InventorySlotType.BANK_NORMAL).setUpgrades(getCharacter().getBankUpgradeData().getNormalTabUpgrades());
		getInventoryTabByType(InventorySlotType.BANK_FASHION).setUpgrades(getCharacter().getBankUpgradeData().getFashionTabUpgrades());
		
		// Get items from database
		List<Item> items;
		
		items = DatabaseHelper.getInventoryItems(getCharacter());
		for (Item item : items) {
			loadItem(item);
		}
		
		items = DatabaseHelper.getBankItems(getCharacter());
		for (Item item : items) {
			loadItem(item);
		}
	}

}
