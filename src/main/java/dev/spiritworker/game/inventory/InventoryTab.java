package dev.spiritworker.game.inventory;

import dev.spiritworker.Constants;
import dev.spiritworker.net.packet.PacketBuilder;

public class InventoryTab extends BaseInventoryTab {
	private int upgrades;
	private int capacity;
	
	public InventoryTab(Inventory inventory, InventorySlotType slotType) {
		super(inventory, slotType);
		this.recalcCapacity();
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	
	public int getUpgrades() {
		return this.upgrades;
	}
	
	public int getFreeSlotsCount() {
		int count = 0;
		for (int i = 0; i < getItems().length; i++) {
			if (this.getItemAt(i) == null) {
				count++;
			}
		}
		return count;
	}
	
	private void recalcCapacity() {
		this.capacity = getSlotType().getDefaultCapacity() + (this.upgrades * Constants.INVENTORY_SLOTS_PER_UPGRADE);
		
		Item[] oldItems = this.items;
		this.items = new Item[getCapacity()];
		
		if (oldItems != null) {
			System.arraycopy(oldItems, 0, this.items, 0, oldItems.length);
		}
	}
	
	public boolean upgrade() {
		// Check if upgrade cap has been reached
		if (this.upgrades >= getSlotType().getMaxUpgrades()) {
			return false;
		}
		
		this.setUpgrades(getUpgrades() + 1);
		
		if (!getSlotType().isBankTab()) {
			getCharacter().getInventoryUpgradeData().save(this);
		} else if (getCharacter().getBankUpgradeData() != null) {
			getCharacter().getBankUpgradeData().save(this);
		}
		
		return true;
	}
	
	public void setUpgrades(int upgrades) {
		this.upgrades = Math.min(upgrades, getSlotType().getMaxUpgrades());
		recalcCapacity();
	}
	
	public synchronized int addItem(Item item) {
		int slot = addItemDirectly(item);
		if (slot != -1) {
			getCharacter().getSession().sendPacket(PacketBuilder.sendClientItemCreate(item));
		}
		return slot;
	}
	
	public synchronized int addItemDirectly(Item item) {
		for (int slot = 0; slot < items.length; slot++) {
			if (items[slot] == null) {
				putItem(slot, item);
				item.save();
				return slot;
			}
		}
		return -1;
	}
}
