package dev.spiritworker.game.data.def;

import java.nio.ByteBuffer;

import dev.spiritworker.game.data.ResourceDef;
import dev.spiritworker.game.data.spawns.MazeData;

public class MazeDef extends ResourceDef {
	private MazeData data;
	
	private int v1;
	private int v2;
	private int v3;
	private int v4;
	private int v5;
	private int v6;
	private int v7;
	private int v8;
	private int v9;
	private int v10;
	private int v11;
	private int v12;
	private int v13;
	private int v14;
	private int v15;
	private int v16;
	private int v17;
	private int v18;
	private int v19;
	private String v20;
	private String v21;
	private String v22;
	private String v23;
	private String v24;
	private String v25;
	private int v26;
	private int v27;
	private int v28;
	private int v29;
	private String v30;
	private String v31;
	private int v32;
	private int v33;
	private int v34;
	private int v35;
	private int v36;
	private int v37;
	private int v38;
	private int v39;
	private int v40;
	private int v41;
	private int v42;
	private String v43;
	private int v44;
	
	public MazeDef(int id) {
		super(id);
	}

	public MazeData getData() {
		return data;
	}

	public void setData(MazeData data) {
		this.data = data;
	}

	@Override
	public void loadFromRes(ByteBuffer buf) {
		v1 = buf.get();
		v2 = buf.getShort();
		v3 = buf.get();
		v4 = buf.get();
		v5 = buf.get();
		v6 = buf.getShort();
		v7 = buf.getShort();
		v8 = buf.get();
		v9 = buf.get();
		v10 = buf.get();
		v11 = buf.getInt();
		v12 = buf.get();
		v13 = buf.get();
		v14 = buf.get();
		v15 = buf.get();
		v16 = buf.get();
		v17 = buf.getInt();
		v18 = buf.getShort();
		v19 = buf.getShort();
		v20 = readString(buf);
		v21 = readString(buf);
		v22 = readString(buf);
		v23 = readString(buf);
		v24 = readString(buf);
		v25 = readString(buf);
		v26 = buf.getShort();
		v27 = buf.getShort();
		v28 = buf.get();
		v29 = buf.getShort();
		v30 = readString(buf);
		v31 = readString(buf);
		v32 = buf.getShort();
		v33 = buf.getShort();
		v34 = buf.getInt();
		v35 = buf.getInt();
		v36 = buf.getInt();
		v37 = buf.getInt();
		v38 = buf.getInt();
		v39 = buf.getInt();
		v40 = buf.getInt();
		v41 = buf.get();
		v42 = buf.get();
		v43 = readString(buf);
		v44 = buf.get();
	}

}
