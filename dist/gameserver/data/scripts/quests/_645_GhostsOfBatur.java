package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _645_GhostsOfBatur extends Quest implements ScriptFile
{
	private static final int Karuda = 32017;
	private static final int CursedGraveGoods = 8089;
	private static final int[][] REWARDS;
	private static final int[] MOBS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _645_GhostsOfBatur()
	{
		super(false);
		this.addStartNpc(32017);
		this.addTalkId(new int[] { 32017 });
		for(final int i : _645_GhostsOfBatur.MOBS)
			this.addKillId(new int[] { i });
		addQuestItem(new int[] { 8089 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("32017-03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(st.getInt("cond") == 2)
			if(st.getQuestItemsCount(8089) >= 180L)
			{
				for(int i = 0; i < _645_GhostsOfBatur.REWARDS.length; ++i)
					if(event.equalsIgnoreCase(String.valueOf(_645_GhostsOfBatur.REWARDS[i][0])))
					{
						st.takeItems(8089, -1L);
						st.giveItems(_645_GhostsOfBatur.REWARDS[i][0], _645_GhostsOfBatur.REWARDS[i][1], true);
						htmltext = "32017-07.htm";
						st.playSound(Quest.SOUND_FINISH);
						st.exitCurrentQuest(true);
					}
			}
			else
			{
				htmltext = "32017-04.htm";
				st.set("cond", "1");
			}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() < 21 || st.getPlayer().getLevel() >= 31)
			{
				htmltext = "32017-02.htm";
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "32017-01.htm";
		}
		else if(cond == 1)
			htmltext = "32017-04.htm";
		else if(cond == 2)
			if(st.getQuestItemsCount(8089) >= 180L)
				htmltext = "32017-05.htm";
			else
				htmltext = "32017-01.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1 && st.getQuestItemsCount(8089) < 180L && st.rollAndGive(8089, 1, 2, 180, 70.0))
		{
			st.set("cond", "2");
			st.setState(2);
		}
		return null;
	}

	static
	{
		REWARDS = new int[][] { { 1878, 18 }, { 1879, 7 }, { 1880, 4 }, { 1881, 6 }, { 1882, 10 }, { 1883, 2 } };
		MOBS = new int[] { 22007, 22009, 22010, 22011, 22012, 22013, 22014, 22015, 22016 };
	}
}
