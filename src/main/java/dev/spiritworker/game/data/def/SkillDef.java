package dev.spiritworker.game.data.def;

import java.nio.ByteBuffer;

import dev.spiritworker.game.data.ResourceDef;

public class SkillDef extends ResourceDef {
	public int skillId;
	public int v2;
	public int v3;
	public int skillRank;
	public int v5;
	private int type;
	public int v7;
	public int v8;
	public int cost;
	public int v10;
	public int v11;
	public int v12;
	private int nextSkill;
	private int modifier1;
	private int modifier2;
	private int prevSkill;
	public int v17;
	public int v18;
	public int v19;
	public int v20;
	public int v21;
	public float v22;
	public float v23;
	public float v24;
	public float v25;
	public float v26;
	public int v27;
	public int v28;
	public int v29;
	public int v30;
	public int v31;
	public int v32;
	public int v33;
	public int v34;
	public int costSF;
	public int v36;
	public int v37;
	public int v38;
	public int v39;
	public int v40;
	public int v41;
	public int v42;
	public int v43;
	public int v44;
	public int v45;
	public int v46;
	public int v47;
	public int v48;
	public int v49;
	public int v50;
	public int v51;
	public int v52;
	public int v53;
	public int v54;
	public int v55;
	public int v56;
	public int v57;
	public int v58;
	public String v59;
	public int v60;
	public int v61;
	public int v62;
	public String v63;
	public String v64;
	public String v65;
	public String v66;
	public int v67;
	public int v68;
	public int v69;
	public int v70;
	public int v71;
	public int v72;
	public String v73;
	public String v74;
	public String v75;
	public int v76;
	public String v77;
	public String v78;
	public int v79;
	public int v80;
	public int v81;
	public int v82;
	public int v83;
	public int v84;
	
	public SkillDef(int id) {
		super(id);
	}
	
	public int getCharacterType() {
		return type;
	}
	
	public int getPrevSkill() {
		return this.prevSkill;
	}
	
	public int getCost() {
		return cost;
	}

	public int getModifier1() {
		return modifier1;
	}

	public int getModifier2() {
		return modifier2;
	}

	@Override
	public void loadFromRes(ByteBuffer buf) {
		skillId = buf.getShort();
		v2 = buf.getInt();
		v3 = buf.getInt();
		skillRank = buf.get();
		v5 = buf.get();
		type = buf.get();
		v7 = buf.get();
		v8 = buf.get();
		cost = buf.get();
		v10 = buf.get();
		v11 = buf.get();
		v12 = buf.getInt();
		nextSkill = buf.getInt();
		modifier1 = buf.getInt();
		modifier2 = buf.getInt();
		prevSkill = buf.getInt();
		v17 = buf.get();
		v18 = buf.get();
		v19 = buf.get();
		v20 = buf.get();
		v21 = buf.getShort();
		v22 = buf.getFloat();
		v23 = buf.getFloat();
		v24 = buf.getFloat();
		v25 = buf.getFloat();
		v26 = buf.getFloat();
		v27 = buf.get();
		v28 = buf.getShort();
		v29 = buf.get();
		v30 = buf.get();
		v31 = buf.get();
		v32 = buf.getInt();
		v33 = buf.get();
		v34 = buf.get();
		costSF = buf.get();
		v36 = buf.get();
		v37 = buf.get();
		v38 = buf.getInt();
		v39 = buf.getInt();
		v40 = buf.getInt();
		v41 = buf.getInt();
		v42 = buf.getInt();
		v43 = buf.getInt();
		v44 = buf.getInt();
		v45 = buf.get();
		v46 = buf.getShort();
		v47 = buf.getShort();
		v48 = buf.get();
		v49 = buf.get();
		v50 = buf.get();
		v51 = buf.get();
		v52 = buf.get();
		v53 = buf.getInt();
		v54 = buf.get();
		v55 = buf.getInt();
		v56 = buf.getInt();
		v57 = buf.get();
		v58 = buf.getShort();
		v59 = readString(buf);
		v60 = buf.getShort();
		v61 = buf.getInt();
		v62 = buf.get();
		v63 = readString(buf);
		v64 = readString(buf);
		v65 = readString(buf);
		v66 = readString(buf);
		v67 = buf.get();
		v68 = buf.getInt();
		v69 = buf.getShort();
		v70 = buf.get();
		v71 = buf.get();
		v72 = buf.getShort();
		v73 = readString(buf);
		v74 = readString(buf);
		v75 = readString(buf);
		v76 = buf.get();
		v77 = readString(buf);
		v78 = readString(buf);
		v79 = buf.get();
		v80 = buf.get();
		v81 = buf.get();
		v82 = buf.get();
		v83 = buf.get();
		v84 = buf.get();
	}
}
