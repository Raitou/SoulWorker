package dev.spiritworker.game.inventory.data;

import dev.spiritworker.game.inventory.InventorySlotType;
import dev.spiritworker.game.inventory.InventoryTab;

public class InventoryUpgradeData {
	private int normal;
	private int fashion;
	
	public int getNormalTabUpgrades() {
		return normal;
	}
	
	public void setNormalTabUpgrades(int normal) {
		this.normal = normal;
	}
	
	public int getFashionTabUpgrades() {
		return fashion;
	}
	
	public void setFashionTabUpgrades(int fashion) {
		this.fashion = fashion;
	}

	public void save(InventoryTab tab) {
		if (tab.getSlotType() == InventorySlotType.NORMAL) {
			this.normal = tab.getUpgrades();
		} else if (tab.getSlotType() == InventorySlotType.FASHION) {
			this.fashion = tab.getUpgrades();
		}
	}
	
}
