package dev.spiritworker.game;

import dev.spiritworker.game.data.def.DistrictDef;

public class District extends GameMap {
	private final DistrictDef districtDef;

	public District(DistrictDef districtDef) {
		super(districtDef.getId());
		this.districtDef = districtDef;
	}

	public DistrictDef getDef() {
		return districtDef;
	}
}
