package dev.spiritworker.game.inventory.data;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.spiritworker.database.DatabaseHelper;
import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.game.inventory.InventorySlotType;
import dev.spiritworker.game.inventory.InventoryTab;

@Entity(value = "bank", noClassnameStored = true)
public class BankUpgradeData {
	@Id
	private int id;
	private int normal;
	private int fashion;
	
	public BankUpgradeData() {
		
	}
	
	public BankUpgradeData(GameCharacter character) {
		this.id = character.getAccountId();
	}

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
		if (tab.getSlotType() == InventorySlotType.BANK_NORMAL) {
			this.normal = tab.getUpgrades();
			saveToDatabase();
		} else if (tab.getSlotType() == InventorySlotType.BANK_FASHION) {
			this.fashion = tab.getUpgrades();
			saveToDatabase();
		}
	}
	
	private void saveToDatabase() {
		DatabaseHelper.saveBankUpgradeData(this);
	}
	
}
