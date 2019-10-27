package dev.spiritworker.net.packet;

public class PacketOpcodes {
	
	// Etc
	public static final int ClientKeepAlive = 0x0106; // Keep Alive?
	
	// Auth Server
	public static final int ClientLogin = 0x0201;
	public static final int ClientLoginResult = 0x0202;
	public static final int ClientLogoutServerReq = 0x0203; // After logout
	public static final int ClientServerList = 0x0204;
	public static final int ClientJoinServer = 0x0205;
	public static final int ClientLoginGameForge = 0x0218;
	public static final int ClientServerListRequest = 0x0203;
	public static final int ClientServerConnectRequest = 0x0205;
	public static final int ClientServerSelect = 0x0211;
	public static final int ClientOptionLoad = 0x0231;
	
	// Game Server
	public static final int ClientConnectGameResponse = 0x0214;
	public static final int ClientConnectGameServer = 0x0213;
	public static final int ClientCreateCharacter = 0x0301;
	public static final int ClientDeleteCharacter = 0x0302;
	public static final int ClientCharacterListRequest = 0x0311;
	public static final int ClientCharacterList = 0x0312;
	public static final int ClientPlayGameResponse = 0x0315;
	public static final int ClientEnterGameServer = 0x0313;
	public static final int ClientSelectCharacter = 0x0347;
	public static final int ClientRequestLogout = 0x0360;
	public static final int ClientViewPlayerInfo = 0x0375;
	
	public static final int CurrentServerDate = 0x0403;
	public static final int CurrentServerVersion = 0x0404;
	
	public static final int ServerSpecialOption = 0x0107;
	public static final int ErrorCode = 0x0392;
	
	// World server
	public static final int ClientConnectWorldServer = 0x0321;
	public static final int ClientEnterGameServerResponse = 0x0322;
	public static final int ClientCharacterLoadTitle = 0x0323;
	public static final int ClientCharacterUpdateTitle = 0x0325;
	public static final int ClientCharacterInfoRequest = 0x0331;
	public static final int ClientCharacterInfoResponse = 0x0332;
	public static final int ClientCharacterUpdate = 0x0334; // Guessed
	public static final int ClientCharacterUpdateSpecialOptionList = 0x0347; // Profile info request?
	
	public static final int ClientLeaveMaze = 0x0401;
	public static final int ClientJoinMap = 0x0402;
	public static final int ClientPlayersRequest = 0x0406; //?
	public static final int ClientMazePortalEnter = 0x0407; 
	public static final int ClientUpdatePosition = 0x0408; 
	public static final int ClientMazePortalUpdate = 0x0409; 
	public static final int ClientPlayerSpawn = 0x0411; // Spawns another player on client
	public static final int ClientPlayerRemove = 0x0412; // Removes player on client
	public static final int ClientMonsterAdd = 0x0415;
	public static final int ClientPlayersInfo = 0x0421;
	public static final int ClientNpcsInfo = 0x0422;
	public static final int ClientSpawnMonsters = 0x0423;
	public static final int ClientResetPositionRequest = 0x0433;
	public static final int ClientCreateMaze = 0x0441;
	public static final int Unknown = 0444;
	
	public static final int ClientMovementMove = 0x0501;
	public static final int ClientPlayerMovementMove = 0x0502;
	public static final int ClientMovementStop = 0x0503;
	public static final int ClientPlayerMovementStop = 0x0504;
	public static final int ClientMovementJump = 0x0505;
	public static final int ClientPlayerMovementJump = 0x0506;
	public static final int ClientCancelGestureRequest = 0x0532;
	public static final int ClientCancelGesture = 0x0533;
	
	public static final int ClientActivateSkill = 0x0608;
	public static final int ClientActivateSkillResponse = 0x0609;
	public static final int ClientSkillDamageInfo = 0x0613;
	public static final int ClientSkillsInfo = 0x0670;
	public static final int ClientUpgradeSkill = 0x0671;
	public static final int ClientUpdateSkillPoints = 0x0673;
	public static final int ClientUpdateSkillLoadout = 0x0675;
	public static final int ClientUpgradeSkillModifier = 0x0677;
	
	public static final int ClientChatNormal = 0x0701;
	public static final int ClientChatWhisper = 0x0702;
	public static final int ClientChatTrade = 0x0703;
	public static final int ClientChatMegaphone = 0x0706;
	public static final int ClientChatGmCommand = 0x070A;
	
	public static final int ClientItemInvenInfo = 0x0801;
	public static final int ClientItemMove = 0x0802;
	public static final int ClientItemCombine = 0x0803;
	public static final int ClientItemDivide = 0x0804;
	public static final int ClientItemBreak = 0x0805;
	public static final int ClientItemCreate = 0x0806;
	public static final int ClientItemUpdate = 0x080d;
	public static final int ClientItemOpenSlotInfo = 0x080e;
	public static final int ClientItemUpdateSlotInfo = 0x080f;
	public static final int ClientItemBankInfo = 0x0810;
	public static final int ClientItemUse = 0x0811;
	public static final int ClientItemUpdateCount = 0x0812;
	public static final int ClientItemUpdateDye = 0x0815;
	
	public static final int ClientAppearanceInfo = 0x0850;
	public static final int ClientAppearancePick = 0x0852;
	
	public static final int ClientShopBuy = 0x0901;
	public static final int ClientShopSell = 0x0902;
	
	public static final int ClientEnteredMaze = 0x1122;
	public static final int ClientQuitMaze = 0x1123;
	public static final int ClientFinishMazeOpenPortal = 0x1152; // Guessed name
	public static final int ClientFinishMaze = 0x1175;
	
	public static final int ClientPartyInvite = 0x1201;
	public static final int ClientPartyAccept = 0x1202;
	public static final int ClientPartyChangeMaster = 0x1203;
	public static final int ClientPartyKickOut = 0x1204;
	public static final int ClientPartyLeave = 0x1205;
	public static final int ClientPartyCancel = 0x1208;
	public static final int ClientPartyUpdateInfo = 0x1209;
	
	public static final int ClientItemMake = 0x1801;
	public static final int ClientItemUpgrade = 0x1802;
	public static final int ClientItemExchange = 0x1803;
	public static final int ClientItemDisassemble = 0x1804;
	public static final int ClientItemDye = 0x1836;
	
	public static final int ClientPostSendList = 0x2001;
	public static final int ClientPostRecvList = 0x2002;
	        
	public static final int ClientDoGesture = 0x2301;
	public static final int ClientGestureInfo = 0x2302;
	public static final int ClientGestureSlotUpdate = 0x2303;
	
	public static final int ClientChannelInfo = 0xF101;
	
}
