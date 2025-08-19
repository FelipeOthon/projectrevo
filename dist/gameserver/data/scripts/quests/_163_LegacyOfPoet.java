package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _163_LegacyOfPoet extends Quest implements ScriptFile
{
	int RUMIELS_POEM_1_ID;
	int RUMIELS_POEM_3_ID;
	int RUMIELS_POEM_4_ID;
	int RUMIELS_POEM_5_ID;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _163_LegacyOfPoet()
	{
		super(false);
		RUMIELS_POEM_1_ID = 1038;
		RUMIELS_POEM_3_ID = 1039;
		RUMIELS_POEM_4_ID = 1040;
		RUMIELS_POEM_5_ID = 1041;
		this.addStartNpc(30220);
		this.addTalkId(new int[] { 30220 });
		this.addTalkId(new int[] { 30220 });
		this.addKillId(new int[] { 20372 });
		this.addKillId(new int[] { 20373 });
		addQuestItem(new int[] { RUMIELS_POEM_1_ID, RUMIELS_POEM_3_ID, RUMIELS_POEM_4_ID, RUMIELS_POEM_5_ID });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("1"))
		{
			st.set("id", "0");
			htmltext = "30220-07.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		if(id == 1)
		{
			st.setState(2);
			st.set("cond", "0");
			st.set("id", "0");
		}
		if(npcId == 30220 && st.getInt("cond") == 0)
		{
			if(st.getInt("cond") < 15)
			{
				if(st.getPlayer().getRace() == Race.darkelf)
					htmltext = "30220-00.htm";
				else
				{
					if(st.getPlayer().getLevel() >= 11)
					{
						htmltext = "30220-03.htm";
						return htmltext;
					}
					htmltext = "30220-02.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "30220-02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == 30220 && st.getInt("cond") == 0)
			htmltext = "completed";
		else if(npcId == 30220 && st.getInt("cond") > 0)
			if(st.getQuestItemsCount(RUMIELS_POEM_1_ID) == 1L && st.getQuestItemsCount(RUMIELS_POEM_3_ID) == 1L && st.getQuestItemsCount(RUMIELS_POEM_4_ID) == 1L && st.getQuestItemsCount(RUMIELS_POEM_5_ID) == 1L)
			{
				if(st.getInt("id") != 163)
				{
					st.set("id", "163");
					htmltext = "30220-09.htm";
					st.takeItems(RUMIELS_POEM_1_ID, 1L);
					st.takeItems(RUMIELS_POEM_3_ID, 1L);
					st.takeItems(RUMIELS_POEM_4_ID, 1L);
					st.takeItems(RUMIELS_POEM_5_ID, 1L);
					st.giveItems(57, 13890L);
					st.addExpAndSp(21643L, 943L);
					st.playSound(Quest.SOUND_FINISH);
					st.exitCurrentQuest(false);
				}
			}
			else
				htmltext = "30220-08.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(npcId == 20372 || npcId == 20373)
		{
			st.set("id", "0");
			if(st.getInt("cond") == 1)
			{
				if(Rnd.chance(10) && st.getQuestItemsCount(RUMIELS_POEM_1_ID) == 0L)
				{
					st.giveItems(RUMIELS_POEM_1_ID, 1L);
					if(st.getQuestItemsCount(RUMIELS_POEM_1_ID) + st.getQuestItemsCount(RUMIELS_POEM_3_ID) + st.getQuestItemsCount(RUMIELS_POEM_4_ID) + st.getQuestItemsCount(RUMIELS_POEM_5_ID) == 4L)
						st.playSound(Quest.SOUND_MIDDLE);
					else
						st.playSound(Quest.SOUND_ITEMGET);
				}
				if(Rnd.chance(70) && st.getQuestItemsCount(RUMIELS_POEM_3_ID) == 0L)
				{
					st.giveItems(RUMIELS_POEM_3_ID, 1L);
					if(st.getQuestItemsCount(RUMIELS_POEM_1_ID) + st.getQuestItemsCount(RUMIELS_POEM_3_ID) + st.getQuestItemsCount(RUMIELS_POEM_4_ID) + st.getQuestItemsCount(RUMIELS_POEM_5_ID) == 4L)
						st.playSound(Quest.SOUND_MIDDLE);
					else
						st.playSound(Quest.SOUND_ITEMGET);
				}
				if(Rnd.chance(70) && st.getQuestItemsCount(RUMIELS_POEM_4_ID) == 0L)
				{
					st.giveItems(RUMIELS_POEM_4_ID, 1L);
					if(st.getQuestItemsCount(RUMIELS_POEM_1_ID) + st.getQuestItemsCount(RUMIELS_POEM_3_ID) + st.getQuestItemsCount(RUMIELS_POEM_4_ID) + st.getQuestItemsCount(RUMIELS_POEM_5_ID) == 4L)
						st.playSound(Quest.SOUND_MIDDLE);
					else
						st.playSound(Quest.SOUND_ITEMGET);
				}
				if(Rnd.chance(50) && st.getQuestItemsCount(RUMIELS_POEM_5_ID) == 0L)
				{
					st.giveItems(RUMIELS_POEM_5_ID, 1L);
					if(st.getQuestItemsCount(RUMIELS_POEM_1_ID) + st.getQuestItemsCount(RUMIELS_POEM_3_ID) + st.getQuestItemsCount(RUMIELS_POEM_4_ID) + st.getQuestItemsCount(RUMIELS_POEM_5_ID) == 4L)
						st.playSound(Quest.SOUND_MIDDLE);
					else
						st.playSound(Quest.SOUND_ITEMGET);
				}
			}
		}
		return null;
	}
}
