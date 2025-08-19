package quests;

import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _629_CleanUpTheSwampOfScreams extends Quest implements ScriptFile
{
	private static int CAPTAIN;
	private static int CLAWS;
	private static int COIN;
	private static int[][] CHANCE;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _629_CleanUpTheSwampOfScreams()
	{
		super(false);
		this.addStartNpc(_629_CleanUpTheSwampOfScreams.CAPTAIN);
		for(int npcId = 21508; npcId < 21518; ++npcId)
			this.addKillId(new int[] { npcId });
		addQuestItem(new int[] { _629_CleanUpTheSwampOfScreams.CLAWS });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("merc_cap_peace_q0629_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("merc_cap_peace_q0629_0202.htm"))
		{
			if(st.getQuestItemsCount(_629_CleanUpTheSwampOfScreams.CLAWS) >= 100L)
			{
				st.takeItems(_629_CleanUpTheSwampOfScreams.CLAWS, 100L);
				st.giveItems(_629_CleanUpTheSwampOfScreams.COIN, (long) (20.0f * Config.RATE_QUESTS_REWARD), false);
			}
			else
				htmltext = "merc_cap_peace_q0629_0203.htm";
		}
		else if(event.equalsIgnoreCase("merc_cap_peace_q0629_0204.htm"))
		{
			st.takeItems(_629_CleanUpTheSwampOfScreams.CLAWS, -1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(st.getQuestItemsCount(7246) > 0L || st.getQuestItemsCount(7247) > 0L)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 66)
					htmltext = "merc_cap_peace_q0629_0101.htm";
				else
				{
					htmltext = "merc_cap_peace_q0629_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(st.getState() == 2)
				if(st.getQuestItemsCount(_629_CleanUpTheSwampOfScreams.CLAWS) >= 100L)
					htmltext = "merc_cap_peace_q0629_0105.htm";
				else
					htmltext = "merc_cap_peace_q0629_0106.htm";
		}
		else
		{
			htmltext = "merc_cap_peace_q0629_0205.htm";
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() == 2)
			st.rollAndGive(_629_CleanUpTheSwampOfScreams.CLAWS, (int) Config.RATE_QUESTS_DROP, _629_CleanUpTheSwampOfScreams.CHANCE[npc.getNpcId() - 21508][1]);
		return null;
	}

	static
	{
		_629_CleanUpTheSwampOfScreams.CAPTAIN = 31553;
		_629_CleanUpTheSwampOfScreams.CLAWS = 7250;
		_629_CleanUpTheSwampOfScreams.COIN = 7251;
		_629_CleanUpTheSwampOfScreams.CHANCE = new int[][] {
				{ 21508, 50 },
				{ 21509, 43 },
				{ 21510, 52 },
				{ 21511, 57 },
				{ 21512, 74 },
				{ 21513, 53 },
				{ 21514, 53 },
				{ 21515, 54 },
				{ 21516, 55 },
				{ 21517, 56 } };
	}
}
