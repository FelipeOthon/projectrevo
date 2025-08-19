package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _605_AllianceWithKetraOrcs extends Quest implements ScriptFile
{
	private static final int[] KETRA_NPC_LIST;
	private static final Map<Integer, Double> Chance;
	private static final int MARK_OF_KETRA_ALLIANCE1 = 7211;
	private static final int MARK_OF_KETRA_ALLIANCE2 = 7212;
	private static final int MARK_OF_KETRA_ALLIANCE3 = 7213;
	private static final int MARK_OF_KETRA_ALLIANCE4 = 7214;
	private static final int MARK_OF_KETRA_ALLIANCE5 = 7215;
	private static final int VB_SOLDIER = 7216;
	private static final int VB_CAPTAIN = 7217;
	private static final int VB_GENERAL = 7218;
	private static final int TOTEM_OF_VALOR = 7219;
	private static final int TOTEM_OF_WISDOM = 7220;
	private static final int RECRUIT = 21350;
	private static final int FOOTMAN = 21351;
	private static final int SCOUT = 21353;
	private static final int HUNTER = 21354;
	private static final int SHAMAN = 21355;
	private static final int PRIEST = 21357;
	private static final int WARRIOR = 21358;
	private static final int MEDIUM = 21360;
	private static final int MAGUS = 21361;
	private static final int OFFICIER = 21362;
	private static final int COMMANDER = 21369;
	private static final int ELITE_GUARD = 21370;
	private static final int GREAT_MAGUS = 21365;
	private static final int GENERAL = 21366;
	private static final int GREAT_SEER = 21368;
	private static final int VARKA_PROPHET = 21373;
	private static final int DISCIPLE_OF_PROPHET = 21375;
	private static final int PROPHET_GUARDS = 21374;
	private static final int HEAD_MAGUS = 21371;
	private static final int HEAD_GUARDS = 21372;
	private static final int Wahkan = 31371;

	public _605_AllianceWithKetraOrcs()
	{
		super(true);
		_605_AllianceWithKetraOrcs.Chance.put(21350, 50.0);
		_605_AllianceWithKetraOrcs.Chance.put(21351, 50.0);
		_605_AllianceWithKetraOrcs.Chance.put(21353, 50.9);
		_605_AllianceWithKetraOrcs.Chance.put(21354, 52.1);
		_605_AllianceWithKetraOrcs.Chance.put(21355, 51.9);
		_605_AllianceWithKetraOrcs.Chance.put(21357, 50.0);
		_605_AllianceWithKetraOrcs.Chance.put(21358, 50.0);
		_605_AllianceWithKetraOrcs.Chance.put(21360, 50.9);
		_605_AllianceWithKetraOrcs.Chance.put(21361, 51.8);
		_605_AllianceWithKetraOrcs.Chance.put(21362, 50.0);
		_605_AllianceWithKetraOrcs.Chance.put(21364, 52.7);
		_605_AllianceWithKetraOrcs.Chance.put(21365, 50.0);
		_605_AllianceWithKetraOrcs.Chance.put(21366, 62.8);
		_605_AllianceWithKetraOrcs.Chance.put(21368, 50.8);
		_605_AllianceWithKetraOrcs.Chance.put(21369, 51.8);
		_605_AllianceWithKetraOrcs.Chance.put(21370, 60.4);
		_605_AllianceWithKetraOrcs.Chance.put(21371, 62.7);
		_605_AllianceWithKetraOrcs.Chance.put(21372, 60.4);
		_605_AllianceWithKetraOrcs.Chance.put(21373, 64.9);
		_605_AllianceWithKetraOrcs.Chance.put(21374, 62.6);
		_605_AllianceWithKetraOrcs.Chance.put(21375, 62.6);
		this.addStartNpc(31371);
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[0] = 21324;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[1] = 21325;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[2] = 21327;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[3] = 21328;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[4] = 21329;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[5] = 21331;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[6] = 21332;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[7] = 21334;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[8] = 21335;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[9] = 21336;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[10] = 21338;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[11] = 21339;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[12] = 21340;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[13] = 21342;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[14] = 21343;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[15] = 21344;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[16] = 21345;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[17] = 21346;
		_605_AllianceWithKetraOrcs.KETRA_NPC_LIST[18] = 21347;
		this.addKillId(_605_AllianceWithKetraOrcs.KETRA_NPC_LIST);
		this.addKillId(new int[] { 21350 });
		this.addKillId(new int[] { 21351 });
		this.addKillId(new int[] { 21353 });
		this.addKillId(new int[] { 21354 });
		this.addKillId(new int[] { 21355 });
		this.addKillId(new int[] { 21357 });
		this.addKillId(new int[] { 21358 });
		this.addKillId(new int[] { 21360 });
		this.addKillId(new int[] { 21361 });
		this.addKillId(new int[] { 21362 });
		this.addKillId(new int[] { 21369 });
		this.addKillId(new int[] { 21370 });
		this.addKillId(new int[] { 21365 });
		this.addKillId(new int[] { 21366 });
		this.addKillId(new int[] { 21368 });
		this.addKillId(new int[] { 21373 });
		this.addKillId(new int[] { 21375 });
		this.addKillId(new int[] { 21374 });
		this.addKillId(new int[] { 21371 });
		this.addKillId(new int[] { 21372 });
		addQuestItem(new int[] { 7216 });
		addQuestItem(new int[] { 7217 });
		addQuestItem(new int[] { 7218 });
	}

	public boolean isKetraNpc(final int npc)
	{
		for(final int i : _605_AllianceWithKetraOrcs.KETRA_NPC_LIST)
			if(npc == i)
				return true;
		return false;
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	@Override
	public void onAbort(final QuestState st)
	{
		takeAllMarks(st);
		st.set("cond", "0");
		st.getPlayer().updateKetraVarka();
		st.playSound(Quest.SOUND_MIDDLE);
	}

	private static void takeAllMarks(final QuestState st)
	{
		st.takeItems(7211, -1L);
		st.takeItems(7212, -1L);
		st.takeItems(7213, -1L);
		st.takeItems(7214, -1L);
		st.takeItems(7215, -1L);
	}

	private void checkMarks(final QuestState st)
	{
		if(st.getQuestItemsCount(7215) > 0L)
			st.set("cond", "6");
		else
		{
			if(st.getInt("cond") == 0)
				return;
			if(st.getQuestItemsCount(7214) > 0L)
				st.set("cond", "5");
			else if(st.getQuestItemsCount(7213) > 0L)
				st.set("cond", "4");
			else if(st.getQuestItemsCount(7212) > 0L)
				st.set("cond", "3");
			else if(st.getQuestItemsCount(7211) > 0L)
				st.set("cond", "2");
			else
				st.set("cond", "1");
		}
	}

	private static boolean CheckNextLevel(final QuestState st, final int soilder_count, final int capitan_count, final int general_count, final int other_item, final boolean take)
	{
		if(soilder_count > 0 && st.getQuestItemsCount(7216) < soilder_count)
			return false;
		if(capitan_count > 0 && st.getQuestItemsCount(7217) < capitan_count)
			return false;
		if(general_count > 0 && st.getQuestItemsCount(7218) < general_count)
			return false;
		if(other_item > 0 && st.getQuestItemsCount(other_item) < 1L)
			return false;
		if(take)
		{
			if(soilder_count > 0)
				st.takeItems(7216, soilder_count);
			if(capitan_count > 0)
				st.takeItems(7217, capitan_count);
			if(general_count > 0)
				st.takeItems(7218, general_count);
			if(other_item > 0)
				st.takeItems(other_item, 1L);
			takeAllMarks(st);
		}
		return true;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("first-2.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			return event;
		}
		checkMarks(st);
		final int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("first-have-2.htm") && cond == 1 && CheckNextLevel(st, 100, 0, 0, 0, true))
		{
			st.giveItems(7211, 1L);
			st.set("cond", "2");
			st.getPlayer().updateKetraVarka();
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("second-have-2.htm") && cond == 2 && CheckNextLevel(st, 200, 100, 0, 0, true))
		{
			st.giveItems(7212, 1L);
			st.set("cond", "3");
			st.getPlayer().updateKetraVarka();
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("third-have-2.htm") && cond == 3 && CheckNextLevel(st, 300, 200, 100, 0, true))
		{
			st.giveItems(7213, 1L);
			st.set("cond", "4");
			st.getPlayer().updateKetraVarka();
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("fourth-have-2.htm") && cond == 4 && CheckNextLevel(st, 300, 300, 200, 7219, true))
		{
			st.giveItems(7214, 1L);
			st.set("cond", "5");
			st.getPlayer().updateKetraVarka();
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("fifth-have-2.htm") && cond == 5 && CheckNextLevel(st, 400, 400, 200, 7220, true))
		{
			st.giveItems(7215, 1L);
			st.set("cond", "6");
			st.getPlayer().updateKetraVarka();
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("quit-2.htm"))
		{
			takeAllMarks(st);
			st.set("cond", "0");
			st.getPlayer().updateKetraVarka();
			st.playSound(Quest.SOUND_MIDDLE);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(st.getPlayer().getVarka() > 0)
		{
			st.exitCurrentQuest(true);
			return "isvarka.htm";
		}
		checkMarks(st);
		if(st.getState() == 1 && st.getInt("cond") != 6)
			st.set("cond", "0");
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 31371)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 74)
				{
					st.exitCurrentQuest(true);
					return "no-level.htm";
				}
				return "first.htm";
			}
			else
			{
				if(cond == 1)
					return CheckNextLevel(st, 100, 0, 0, 0, false) ? "first-have.htm" : "first-havenot.htm";
				if(cond == 2)
					return CheckNextLevel(st, 200, 100, 0, 0, false) ? "second-have.htm" : "second.htm";
				if(cond == 3)
					return CheckNextLevel(st, 300, 200, 100, 0, false) ? "third-have.htm" : "third.htm";
				if(cond == 4)
					return CheckNextLevel(st, 300, 300, 200, 7219, false) ? "fourth-have.htm" : "fourth.htm";
				if(cond == 5)
					return CheckNextLevel(st, 400, 400, 200, 7220, false) ? "fifth-have.htm" : "fifth.htm";
				if(cond == 6)
				{
					if(st.getPlayer().getKetra() < 5)
						st.getPlayer().updateKetraVarka();
					return "high.htm";
				}
			}
		return "noquest";
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(isKetraNpc(npcId))
			if(st.getQuestItemsCount(7215) > 0L)
			{
				takeAllMarks(st);
				st.giveItems(7214, 1L);
				st.getPlayer().updateKetraVarka();
			}
			else if(st.getQuestItemsCount(7214) > 0L)
			{
				takeAllMarks(st);
				st.giveItems(7213, 1L);
				st.getPlayer().updateKetraVarka();
			}
			else if(st.getQuestItemsCount(7213) > 0L)
			{
				takeAllMarks(st);
				st.giveItems(7212, 1L);
				st.getPlayer().updateKetraVarka();
			}
			else if(st.getQuestItemsCount(7212) > 0L)
			{
				takeAllMarks(st);
				st.giveItems(7211, 1L);
				st.getPlayer().updateKetraVarka();
			}
			else if(st.getQuestItemsCount(7211) > 0L)
			{
				takeAllMarks(st);
				st.getPlayer().updateKetraVarka();
			}
			else if(st.getPlayer().getKetra() > 0)
			{
				st.getPlayer().updateKetraVarka();
				st.exitCurrentQuest(true);
				return "quit-2.htm";
			}
		if(st.getQuestItemsCount(7215) > 0L)
			return null;
		final int cond = st.getInt("cond");
		if(npcId == 21350 || npcId == 21351 || npcId == 21353 || npcId == 21354 || npcId == 21355)
		{
			if(cond > 0)
				st.dropItems(7216, 1, 1, cond == 1 ? 100L : cond == 2 ? 200L : cond == 3 || cond == 4 ? 300L : 400L, _605_AllianceWithKetraOrcs.Chance.get(npcId), true);
		}
		else if(npcId == 21357 || npcId == 21358 || npcId == 21360 || npcId == 21361 || npcId == 21362 || npcId == 21369 || npcId == 21370)
		{
			if(cond > 1)
				st.dropItems(7217, 1, 1, cond == 2 ? 100L : cond == 3 ? 200L : cond == 4 ? 300L : 400L, _605_AllianceWithKetraOrcs.Chance.get(npcId), true);
		}
		else if((npcId == 21365 || npcId == 21366 || npcId == 21368 || npcId == 21373 || npcId == 21375 || npcId == 21374 || npcId == 21371 || npcId == 21372) && cond > 2)
			st.dropItems(7218, 1, 1, cond == 3 ? 100L : 200L, _605_AllianceWithKetraOrcs.Chance.get(npcId), true);
		return null;
	}

	static
	{
		KETRA_NPC_LIST = new int[19];
		Chance = new HashMap<Integer, Double>();
	}
}
