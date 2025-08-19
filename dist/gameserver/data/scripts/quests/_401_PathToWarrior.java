package quests;

import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _401_PathToWarrior extends Quest implements ScriptFile
{
	int AURON;
	int SIMPLON;
	int TRACKER_SKELETON;
	int POISON_SPIDER;
	int TRACKER_SKELETON_LD;
	int ARACHNID_TRACKER;
	int EINS_LETTER_ID;
	int WARRIOR_GUILD_MARK_ID;
	int RUSTED_BRONZE_SWORD1_ID;
	int RUSTED_BRONZE_SWORD2_ID;
	int SIMPLONS_LETTER_ID;
	int POISON_SPIDER_LEG2_ID;
	int MEDALLION_OF_WARRIOR_ID;
	int RUSTED_BRONZE_SWORD3_ID;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _401_PathToWarrior()
	{
		super(false);
		AURON = 30010;
		SIMPLON = 30253;
		TRACKER_SKELETON = 20035;
		POISON_SPIDER = 20038;
		TRACKER_SKELETON_LD = 20042;
		ARACHNID_TRACKER = 20043;
		EINS_LETTER_ID = 1138;
		WARRIOR_GUILD_MARK_ID = 1139;
		RUSTED_BRONZE_SWORD1_ID = 1140;
		RUSTED_BRONZE_SWORD2_ID = 1141;
		SIMPLONS_LETTER_ID = 1143;
		POISON_SPIDER_LEG2_ID = 1144;
		MEDALLION_OF_WARRIOR_ID = 1145;
		RUSTED_BRONZE_SWORD3_ID = 1142;
		this.addStartNpc(AURON);
		this.addTalkId(new int[] { SIMPLON });
		this.addKillId(new int[] { TRACKER_SKELETON });
		this.addKillId(new int[] { POISON_SPIDER });
		this.addKillId(new int[] { TRACKER_SKELETON_LD });
		this.addKillId(new int[] { ARACHNID_TRACKER });
		addQuestItem(new int[] {
				SIMPLONS_LETTER_ID,
				RUSTED_BRONZE_SWORD2_ID,
				EINS_LETTER_ID,
				WARRIOR_GUILD_MARK_ID,
				RUSTED_BRONZE_SWORD1_ID,
				POISON_SPIDER_LEG2_ID,
				RUSTED_BRONZE_SWORD3_ID });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("401_1"))
		{
			if(st.getPlayer().getClassId().getId() == 0)
			{
				if(st.getPlayer().getLevel() >= 18)
				{
					if(st.getQuestItemsCount(MEDALLION_OF_WARRIOR_ID) > 0L)
						htmltext = "ein_q0401_04.htm";
					else
						htmltext = "ein_q0401_05.htm";
				}
				else
					htmltext = "ein_q0401_02.htm";
			}
			else if(st.getPlayer().getClassId().getId() == 1)
				htmltext = "ein_q0401_02a.htm";
			else
				htmltext = "ein_q0401_03.htm";
		}
		else if(event.equalsIgnoreCase("401_2"))
			htmltext = "ein_q0401_10.htm";
		else if(event.equalsIgnoreCase("401_3"))
		{
			htmltext = "ein_q0401_11.htm";
			st.takeItems(SIMPLONS_LETTER_ID, 1L);
			st.takeItems(RUSTED_BRONZE_SWORD2_ID, 1L);
			st.giveItems(RUSTED_BRONZE_SWORD3_ID, 1L);
			st.set("cond", "5");
		}
		else if(event.equalsIgnoreCase("1"))
		{
			if(st.getQuestItemsCount(EINS_LETTER_ID) == 0L)
			{
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
				st.giveItems(EINS_LETTER_ID, 1L);
				htmltext = "ein_q0401_06.htm";
			}
		}
		else if(event.equalsIgnoreCase("30253_1"))
		{
			htmltext = "trader_simplon_q0401_02.htm";
			st.takeItems(EINS_LETTER_ID, 1L);
			st.giveItems(WARRIOR_GUILD_MARK_ID, 1L);
			st.set("cond", "2");
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		final int cond = st.getInt("cond");
		if(id == 1)
		{
			st.setState(2);
			st.set("cond", "0");
		}
		if(npcId == AURON && cond == 0)
			htmltext = "ein_q0401_01.htm";
		else if(npcId == AURON && st.getQuestItemsCount(EINS_LETTER_ID) > 0L)
			htmltext = "ein_q0401_07.htm";
		else if(npcId == AURON && st.getQuestItemsCount(WARRIOR_GUILD_MARK_ID) == 1L)
			htmltext = "ein_q0401_08.htm";
		else if(npcId == SIMPLON && st.getQuestItemsCount(EINS_LETTER_ID) > 0L)
			htmltext = "trader_simplon_q0401_01.htm";
		else if(npcId == SIMPLON && st.getQuestItemsCount(WARRIOR_GUILD_MARK_ID) > 0L)
		{
			if(st.getQuestItemsCount(RUSTED_BRONZE_SWORD1_ID) < 1L)
				htmltext = "trader_simplon_q0401_03.htm";
			else if(st.getQuestItemsCount(RUSTED_BRONZE_SWORD1_ID) < 10L)
				htmltext = "trader_simplon_q0401_04.htm";
			else if(st.getQuestItemsCount(RUSTED_BRONZE_SWORD1_ID) >= 10L)
			{
				st.takeItems(WARRIOR_GUILD_MARK_ID, -1L);
				st.takeItems(RUSTED_BRONZE_SWORD1_ID, -1L);
				st.giveItems(RUSTED_BRONZE_SWORD2_ID, 1L);
				st.giveItems(SIMPLONS_LETTER_ID, 1L);
				st.set("cond", "4");
				htmltext = "trader_simplon_q0401_05.htm";
			}
		}
		else if(npcId == SIMPLON && st.getQuestItemsCount(SIMPLONS_LETTER_ID) > 0L)
			htmltext = "trader_simplon_q0401_06.htm";
		else if(npcId == AURON && st.getQuestItemsCount(SIMPLONS_LETTER_ID) > 0L && st.getQuestItemsCount(RUSTED_BRONZE_SWORD2_ID) > 0L && st.getQuestItemsCount(WARRIOR_GUILD_MARK_ID) == 0L && st.getQuestItemsCount(EINS_LETTER_ID) == 0L)
			htmltext = "ein_q0401_09.htm";
		else if(npcId == AURON && st.getQuestItemsCount(RUSTED_BRONZE_SWORD3_ID) > 0L && st.getQuestItemsCount(WARRIOR_GUILD_MARK_ID) == 0L && st.getQuestItemsCount(EINS_LETTER_ID) == 0L)
			if(st.getQuestItemsCount(POISON_SPIDER_LEG2_ID) < 20L)
				htmltext = "ein_q0401_12.htm";
			else if(st.getQuestItemsCount(POISON_SPIDER_LEG2_ID) > 19L)
			{
				st.takeItems(POISON_SPIDER_LEG2_ID, -1L);
				st.takeItems(RUSTED_BRONZE_SWORD3_ID, -1L);
				if(st.getPlayer().getClassId().getLevel() == 1)
				{
					st.giveItems(MEDALLION_OF_WARRIOR_ID, 1L);
					if(!st.getPlayer().getVarBoolean("prof1"))
					{
						st.getPlayer().setVar("prof1", "1");
						st.addExpAndSp(295862L, 16814L, true);
						st.giveItems(57, 81900L, Config.RATE_QUESTS_OCCUPATION_CHANGE);
					}
				}
				htmltext = "ein_q0401_13.htm";
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == TRACKER_SKELETON || npcId == TRACKER_SKELETON_LD)
		{
			if(cond == 2 && st.getQuestItemsCount(RUSTED_BRONZE_SWORD1_ID) < 10L)
			{
				st.giveItems(RUSTED_BRONZE_SWORD1_ID, 1L);
				if(st.getQuestItemsCount(RUSTED_BRONZE_SWORD1_ID) == 10L)
				{
					st.playSound(Quest.SOUND_MIDDLE);
					st.set("cond", "3");
				}
				else
					st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		else if((npcId == ARACHNID_TRACKER || npcId == POISON_SPIDER) && st.getQuestItemsCount(POISON_SPIDER_LEG2_ID) < 20L && st.getQuestItemsCount(RUSTED_BRONZE_SWORD3_ID) == 1L && st.getItemEquipped(7) == RUSTED_BRONZE_SWORD3_ID)
		{
			st.giveItems(POISON_SPIDER_LEG2_ID, 1L);
			if(st.getQuestItemsCount(POISON_SPIDER_LEG2_ID) == 20L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "6");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
