package quests;

import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _409_PathToOracle extends Quest implements ScriptFile
{
	public final int MANUEL = 30293;
	public final int ALLANA = 30424;
	public final int PERRIN = 30428;
	public final int LIZARDMAN_WARRIOR = 27032;
	public final int LIZARDMAN_SCOUT = 27033;
	public final int LIZARDMAN = 27034;
	public final int TAMIL = 27035;
	public final int CRYSTAL_MEDALLION_ID = 1231;
	public final int MONEY_OF_SWINDLER_ID = 1232;
	public final int DAIRY_OF_ALLANA_ID = 1233;
	public final int LIZARD_CAPTAIN_ORDER_ID = 1234;
	public final int LEAF_OF_ORACLE_ID = 1235;
	public final int HALF_OF_DAIRY_ID = 1236;
	public final int TAMATOS_NECKLACE_ID = 1275;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _409_PathToOracle()
	{
		super(false);
		this.addStartNpc(30293);
		this.addTalkId(new int[] { 30424 });
		this.addTalkId(new int[] { 30428 });
		this.addKillId(new int[] { 27032 });
		this.addKillId(new int[] { 27033 });
		this.addKillId(new int[] { 27034 });
		this.addKillId(new int[] { 27035 });
		addQuestItem(new int[] { 1232, 1233, 1234, 1231, 1236, 1275 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("1"))
		{
			if(event.equalsIgnoreCase("1"))
				if(st.getPlayer().getClassId().getId() != 25)
				{
					if(st.getPlayer().getClassId().getId() == 29)
						htmltext = "father_manuell_q0409_02a.htm";
					else
						htmltext = "father_manuell_q0409_02.htm";
				}
				else if(st.getPlayer().getLevel() < 18)
					htmltext = "father_manuell_q0409_03.htm";
				else if(st.getQuestItemsCount(1235) > 0L)
					htmltext = "father_manuell_q0409_04.htm";
				else
				{
					st.set("cond", "1");
					st.setState(2);
					st.playSound(Quest.SOUND_ACCEPT);
					st.giveItems(1231, 1L);
					htmltext = "father_manuell_q0409_05.htm";
				}
		}
		else if(event.equalsIgnoreCase("allana_q0409_08.htm"))
		{
			st.addSpawn(27032);
			st.addSpawn(27033);
			st.addSpawn(27034);
			st.set("cond", "2");
		}
		else if(event.equalsIgnoreCase("30424_1"))
			htmltext = "";
		else if(event.equalsIgnoreCase("30428_1"))
			htmltext = "perrin_q0409_02.htm";
		else if(event.equalsIgnoreCase("30428_2"))
			htmltext = "perrin_q0409_03.htm";
		else if(event.equalsIgnoreCase("30428_3"))
			st.addSpawn(27035);
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30293)
		{
			if(cond < 1)
				htmltext = "father_manuell_q0409_01.htm";
			else if(st.getQuestItemsCount(1231) > 0L)
				if(st.getQuestItemsCount(1232) < 1L && st.getQuestItemsCount(1233) < 1L && st.getQuestItemsCount(1234) < 1L && st.getQuestItemsCount(1236) < 1L)
					htmltext = "father_manuell_q0409_09.htm";
				else if(st.getQuestItemsCount(1232) > 0L && st.getQuestItemsCount(1233) > 0L && st.getQuestItemsCount(1234) > 0L && st.getQuestItemsCount(1236) < 1L)
				{
					htmltext = "father_manuell_q0409_08.htm";
					st.takeItems(1232, 1L);
					st.takeItems(1233, -1L);
					st.takeItems(1234, -1L);
					st.takeItems(1231, -1L);
					if(st.getPlayer().getClassId().getLevel() == 1)
					{
						st.giveItems(1235, 1L);
						if(!st.getPlayer().getVarBoolean("prof1"))
						{
							st.getPlayer().setVar("prof1", "1");
							st.addExpAndSp(295862L, 16894L, true);
							st.giveItems(57, 81900L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
						}
					}
					st.playSound(Quest.SOUND_FINISH);
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "father_manuell_q0409_07.htm";
		}
		else if(npcId == 30424)
		{
			if(st.getQuestItemsCount(1231) > 0L)
				if(st.getQuestItemsCount(1232) < 1L && st.getQuestItemsCount(1233) < 1L && st.getQuestItemsCount(1234) < 1L && st.getQuestItemsCount(1236) < 1L)
				{
					if(cond > 2)
						htmltext = "allana_q0409_05.htm";
					else
						htmltext = "allana_q0409_01.htm";
				}
				else if(st.getQuestItemsCount(1232) < 1L && st.getQuestItemsCount(1233) < 1L && st.getQuestItemsCount(1234) > 0L && st.getQuestItemsCount(1236) < 1L)
				{
					htmltext = "allana_q0409_02.htm";
					st.giveItems(1236, 1L);
					st.set("cond", "4");
				}
				else if(st.getQuestItemsCount(1232) < 1L && st.getQuestItemsCount(1233) < 1L && st.getQuestItemsCount(1234) > 0L && st.getQuestItemsCount(1236) > 0L)
				{
					if(st.getQuestItemsCount(1275) < 1L)
						htmltext = "allana_q0409_06.htm";
					else
						htmltext = "allana_q0409_03.htm";
				}
				else if(st.getQuestItemsCount(1232) > 0L && st.getQuestItemsCount(1233) < 1L && st.getQuestItemsCount(1234) > 0L && st.getQuestItemsCount(1236) > 0L)
				{
					htmltext = "allana_q0409_04.htm";
					st.takeItems(1236, -1L);
					st.giveItems(1233, 1L);
					st.set("cond", "7");
				}
				else if(st.getQuestItemsCount(1232) > 0L && st.getQuestItemsCount(1234) > 0L && st.getQuestItemsCount(1236) < 1L && st.getQuestItemsCount(1233) > 0L)
					htmltext = "allana_q0409_05.htm";
		}
		else if(npcId == 30428 && st.getQuestItemsCount(1231) > 0L && st.getQuestItemsCount(1234) > 0L)
			if(st.getQuestItemsCount(1275) > 0L)
			{
				htmltext = "perrin_q0409_04.htm";
				st.takeItems(1275, -1L);
				st.giveItems(1232, 1L);
				st.set("cond", "6");
			}
			else if(st.getQuestItemsCount(1232) > 0L)
				htmltext = "perrin_q0409_05.htm";
			else if(cond > 4)
				htmltext = "perrin_q0409_06.htm";
			else
				htmltext = "perrin_q0409_01.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 27032 | npcId == 27033 | npcId == 27034)
		{
			if(cond == 2 && st.getQuestItemsCount(1234) < 1L)
			{
				st.giveItems(1234, 1L);
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "3");
			}
		}
		else if(npcId == 27035 && cond == 4 && st.getQuestItemsCount(1275) < 1L)
		{
			st.giveItems(1275, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			st.set("cond", "5");
		}
		return null;
	}
}
