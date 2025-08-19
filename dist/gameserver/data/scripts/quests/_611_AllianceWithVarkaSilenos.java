package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _611_AllianceWithVarkaSilenos extends Quest implements ScriptFile
{
	private final int[] VARKA_NPC_LIST;
	private static final Map<Integer, Double> Chance;
	private static final int MARK_OF_VARKA_ALLIANCE1 = 7221;
	private static final int MARK_OF_VARKA_ALLIANCE2 = 7222;
	private static final int MARK_OF_VARKA_ALLIANCE3 = 7223;
	private static final int MARK_OF_VARKA_ALLIANCE4 = 7224;
	private static final int MARK_OF_VARKA_ALLIANCE5 = 7225;
	private static final int KB_SOLDIER = 7226;
	private static final int KB_CAPTAIN = 7227;
	private static final int KB_GENERAL = 7228;
	private static final int TOTEM_OF_VALOR = 7229;
	private static final int TOTEM_OF_WISDOM = 7230;
	private static final int RAIDER = 21327;
	private static final int FOOTMAN = 21324;
	private static final int SCOUT = 21328;
	private static final int WAR_HOUND = 21325;
	private static final int SHAMAN = 2132;
	private static final int SEER = 21338;
	private static final int WARRIOR = 21331;
	private static final int LIEUTENANT = 21332;
	private static final int ELITE_SOLDIER = 21335;
	private static final int MEDIUM = 21334;
	private static final int COMMAND = 21343;
	private static final int ELITE_GUARD = 21344;
	private static final int WHITE_CAPTAIN = 21336;
	private static final int BATTALION_COMMANDER_SOLDIER = 21340;
	private static final int GENERAL = 21339;
	private static final int GREAT_SEER = 21342;
	private static final int KETRA_PROPHET = 21347;
	private static final int DISCIPLE_OF_PROPHET = 21375;
	private static final int PROPHET_GUARDS = 21348;
	private static final int PROPHET_AIDE = 21349;
	private static final int HEAD_SHAMAN = 21345;
	private static final int HEAD_GUARDS = 21346;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	private static void takeAllMarks(final QuestState st)
	{
		st.takeItems(7221, -1L);
		st.takeItems(7222, -1L);
		st.takeItems(7223, -1L);
		st.takeItems(7224, -1L);
		st.takeItems(7225, -1L);
	}

	public _611_AllianceWithVarkaSilenos()
	{
		super(true);
		VARKA_NPC_LIST = new int[20];
		_611_AllianceWithVarkaSilenos.Chance.put(21324, 50.8);
		_611_AllianceWithVarkaSilenos.Chance.put(21325, 50.0);
		_611_AllianceWithVarkaSilenos.Chance.put(21327, 50.0);
		_611_AllianceWithVarkaSilenos.Chance.put(21328, 51.9);
		_611_AllianceWithVarkaSilenos.Chance.put(21329, 52.7);
		_611_AllianceWithVarkaSilenos.Chance.put(21331, 51.8);
		_611_AllianceWithVarkaSilenos.Chance.put(21332, 62.6);
		_611_AllianceWithVarkaSilenos.Chance.put(21334, 50.0);
		_611_AllianceWithVarkaSilenos.Chance.put(21335, 50.0);
		_611_AllianceWithVarkaSilenos.Chance.put(21336, 52.1);
		_611_AllianceWithVarkaSilenos.Chance.put(21338, 50.0);
		_611_AllianceWithVarkaSilenos.Chance.put(21339, 62.8);
		_611_AllianceWithVarkaSilenos.Chance.put(21340, 50.0);
		_611_AllianceWithVarkaSilenos.Chance.put(21342, 50.9);
		_611_AllianceWithVarkaSilenos.Chance.put(21343, 50.9);
		_611_AllianceWithVarkaSilenos.Chance.put(21344, 51.8);
		_611_AllianceWithVarkaSilenos.Chance.put(21345, 62.6);
		_611_AllianceWithVarkaSilenos.Chance.put(21346, 60.4);
		_611_AllianceWithVarkaSilenos.Chance.put(21347, 64.9);
		_611_AllianceWithVarkaSilenos.Chance.put(21348, 60.4);
		_611_AllianceWithVarkaSilenos.Chance.put(21349, 62.7);
		this.addStartNpc(31378);
		VARKA_NPC_LIST[0] = 21350;
		VARKA_NPC_LIST[1] = 21351;
		VARKA_NPC_LIST[2] = 21353;
		VARKA_NPC_LIST[3] = 21354;
		VARKA_NPC_LIST[4] = 21355;
		VARKA_NPC_LIST[5] = 21357;
		VARKA_NPC_LIST[6] = 21358;
		VARKA_NPC_LIST[7] = 21360;
		VARKA_NPC_LIST[8] = 21361;
		VARKA_NPC_LIST[9] = 21362;
		VARKA_NPC_LIST[10] = 21364;
		VARKA_NPC_LIST[11] = 21365;
		VARKA_NPC_LIST[12] = 21366;
		VARKA_NPC_LIST[13] = 21368;
		VARKA_NPC_LIST[14] = 21369;
		VARKA_NPC_LIST[15] = 21370;
		VARKA_NPC_LIST[16] = 21371;
		VARKA_NPC_LIST[17] = 21372;
		VARKA_NPC_LIST[18] = 21373;
		VARKA_NPC_LIST[19] = 21374;
		for(final int npcId : VARKA_NPC_LIST)
			this.addKillId(new int[] { npcId });
		this.addKillId(new int[] { 21327 });
		this.addKillId(new int[] { 21324 });
		this.addKillId(new int[] { 21328 });
		this.addKillId(new int[] { 21325 });
		this.addKillId(new int[] { 2132 });
		this.addKillId(new int[] { 21338 });
		this.addKillId(new int[] { 21331 });
		this.addKillId(new int[] { 21332 });
		this.addKillId(new int[] { 21335 });
		this.addKillId(new int[] { 21334 });
		this.addKillId(new int[] { 21343 });
		this.addKillId(new int[] { 21344 });
		this.addKillId(new int[] { 21336 });
		this.addKillId(new int[] { 21340 });
		this.addKillId(new int[] { 21339 });
		this.addKillId(new int[] { 21342 });
		this.addKillId(new int[] { 21347 });
		this.addKillId(new int[] { 21349 });
		this.addKillId(new int[] { 21348 });
		this.addKillId(new int[] { 21345 });
		this.addKillId(new int[] { 21346 });
		addQuestItem(new int[] { 7226 });
		addQuestItem(new int[] { 7227 });
		addQuestItem(new int[] { 7228 });
	}

	public boolean isVarkaNpc(final int npc)
	{
		for(final int i : VARKA_NPC_LIST)
			if(npc == i)
				return true;
		return false;
	}

	private static void checkMarks(final QuestState st)
	{
		if(st.getQuestItemsCount(7225) > 0L)
			st.set("cond", "6");
		else
		{
			if(st.getInt("cond") == 0)
				return;
			if(st.getQuestItemsCount(7224) > 0L)
				st.set("cond", "5");
			else if(st.getQuestItemsCount(7223) > 0L)
				st.set("cond", "4");
			else if(st.getQuestItemsCount(7222) > 0L)
				st.set("cond", "3");
			else if(st.getQuestItemsCount(7221) > 0L)
				st.set("cond", "2");
			else
				st.set("cond", "1");
		}
	}

	private static boolean CheckNextLevel(final QuestState st, final int soilder_count, final int capitan_count, final int general_count, final int other_item, final boolean take)
	{
		if(soilder_count > 0 && st.getQuestItemsCount(7226) < soilder_count)
			return false;
		if(capitan_count > 0 && st.getQuestItemsCount(7227) < capitan_count)
			return false;
		if(general_count > 0 && st.getQuestItemsCount(7228) < general_count)
			return false;
		if(other_item > 0 && st.getQuestItemsCount(other_item) < 1L)
			return false;
		if(take)
		{
			if(soilder_count > 0)
				st.takeItems(7226, soilder_count);
			if(capitan_count > 0)
				st.takeItems(7227, capitan_count);
			if(general_count > 0)
				st.takeItems(7228, general_count);
			if(other_item > 0)
				st.takeItems(other_item, 1L);
			takeAllMarks(st);
		}
		return true;
	}

	@Override
	public void onAbort(final QuestState st)
	{
		takeAllMarks(st);
		st.set("cond", "0");
		st.getPlayer().updateKetraVarka();
		st.playSound(Quest.SOUND_MIDDLE);
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("herald_naran_q0611_04.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			return event;
		}
		checkMarks(st);
		final int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("herald_naran_q0611_12.htm") && cond == 1 && CheckNextLevel(st, 100, 0, 0, 0, true))
		{
			st.giveItems(7221, 1L);
			st.set("cond", "2");
			st.getPlayer().updateKetraVarka();
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("herald_naran_q0611_15.htm") && cond == 2 && CheckNextLevel(st, 200, 100, 0, 0, true))
		{
			st.giveItems(7222, 1L);
			st.set("cond", "3");
			st.getPlayer().updateKetraVarka();
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("herald_naran_q0611_18.htm") && cond == 3 && CheckNextLevel(st, 300, 200, 100, 0, true))
		{
			st.giveItems(7223, 1L);
			st.set("cond", "4");
			st.getPlayer().updateKetraVarka();
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("herald_naran_q0611_21.htm") && cond == 4 && CheckNextLevel(st, 300, 300, 200, 7229, true))
		{
			st.giveItems(7224, 1L);
			st.set("cond", "5");
			st.getPlayer().updateKetraVarka();
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("herald_naran_q0611_23.htm") && cond == 5 && CheckNextLevel(st, 400, 400, 200, 7230, true))
		{
			st.giveItems(7225, 1L);
			st.set("cond", "6");
			st.getPlayer().updateKetraVarka();
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("herald_naran_q0611_26.htm"))
		{
			takeAllMarks(st);
			st.set("cond", "0");
			st.getPlayer().updateKetraVarka();
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(st.getPlayer().getKetra() > 0)
		{
			st.exitCurrentQuest(true);
			return "herald_naran_q0611_02.htm";
		}
		checkMarks(st);
		if(st.getState() == 1 && st.getInt("cond") != 6)
			st.set("cond", "0");
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 31378)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 74)
				{
					st.exitCurrentQuest(true);
					return "herald_naran_q0611_03.htm";
				}
				return "herald_naran_q0611_01.htm";
			}
			else
			{
				if(cond == 1)
					return CheckNextLevel(st, 100, 0, 0, 0, false) ? "herald_naran_q0611_11.htm" : "herald_naran_q0611_10.htm";
				if(cond == 2)
					return CheckNextLevel(st, 200, 100, 0, 0, false) ? "herald_naran_q0611_14.htm" : "herald_naran_q0611_13.htm";
				if(cond == 3)
					return CheckNextLevel(st, 300, 200, 100, 0, false) ? "herald_naran_q0611_17.htm" : "herald_naran_q0611_16.htm";
				if(cond == 4)
					return CheckNextLevel(st, 300, 300, 200, 7229, false) ? "herald_naran_q0611_20.htm" : "herald_naran_q0611_19.htm";
				if(cond == 5)
					return CheckNextLevel(st, 400, 400, 200, 7230, false) ? "herald_naran_q0611_27.htm" : "herald_naran_q0611_22.htm";
				if(cond == 6)
				{
					if(st.getPlayer().getVarka() < 5)
						st.getPlayer().updateKetraVarka();
					return "herald_naran_q0611_24.htm";
				}
			}
		return "noquest";
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(isVarkaNpc(npcId))
			if(st.getQuestItemsCount(7225) > 0L)
			{
				takeAllMarks(st);
				st.giveItems(7224, 1L);
				st.getPlayer().updateKetraVarka();
				checkMarks(st);
			}
			else if(st.getQuestItemsCount(7224) > 0L)
			{
				takeAllMarks(st);
				st.giveItems(7223, 1L);
				st.getPlayer().updateKetraVarka();
				checkMarks(st);
			}
			else if(st.getQuestItemsCount(7223) > 0L)
			{
				takeAllMarks(st);
				st.giveItems(7222, 1L);
				st.getPlayer().updateKetraVarka();
				checkMarks(st);
			}
			else if(st.getQuestItemsCount(7222) > 0L)
			{
				takeAllMarks(st);
				st.giveItems(7221, 1L);
				st.getPlayer().updateKetraVarka();
				checkMarks(st);
			}
			else if(st.getQuestItemsCount(7221) > 0L)
			{
				takeAllMarks(st);
				st.getPlayer().updateKetraVarka();
				checkMarks(st);
			}
			else if(st.getPlayer().getVarka() > 0)
			{
				st.getPlayer().updateKetraVarka();
				st.exitCurrentQuest(true);
				return "herald_naran_q0611_26.htm";
			}
		if(st.getQuestItemsCount(7225) > 0L)
			return null;
		final int cond = st.getInt("cond");
		if(npcId == 21327 || npcId == 21324 || npcId == 21328 || npcId == 21325 || npcId == 2132)
		{
			if(cond > 0)
				st.dropItems(7226, 1, 1, cond == 1 ? 100L : cond == 2 ? 200L : cond == 3 || cond == 4 ? 300L : 400L, _611_AllianceWithVarkaSilenos.Chance.get(npcId), true);
		}
		else if(npcId == 21338 || npcId == 21331 || npcId == 21332 || npcId == 21335 || npcId == 21334 || npcId == 21343 || npcId == 21344 || npcId == 21336)
		{
			if(cond > 1)
				st.dropItems(7227, 1, 1, cond == 2 ? 100L : cond == 3 ? 200L : cond == 4 ? 300L : 400L, _611_AllianceWithVarkaSilenos.Chance.get(npcId), true);
		}
		else if((npcId == 21340 || npcId == 21339 || npcId == 21342 || npcId == 21347 || npcId == 21375 || npcId == 21348 || npcId == 21345 || npcId == 21346) && cond > 2)
			st.dropItems(7228, 1, 1, cond == 3 ? 100L : 200L, _611_AllianceWithVarkaSilenos.Chance.get(npcId), true);
		return null;
	}

	static
	{
		Chance = new HashMap<Integer, Double>();
	}
}
