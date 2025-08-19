package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _601_WatchingEyes extends Quest implements ScriptFile
{
	private static int EYE_OF_ARGOS;
	private static int PROOF_OF_AVENGER;
	private static int[] MOBS;
	private static int[][] REWARDS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _601_WatchingEyes()
	{
		super(true);
		this.addStartNpc(_601_WatchingEyes.EYE_OF_ARGOS);
		this.addKillId(_601_WatchingEyes.MOBS);
		addQuestItem(new int[] { _601_WatchingEyes.PROOF_OF_AVENGER });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("eye_of_argos_q0601_0104.htm"))
		{
			if(st.getPlayer().getLevel() < 71)
			{
				htmltext = "eye_of_argos_q0601_0103.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				st.setState(2);
				st.set("cond", "1");
				st.playSound(Quest.SOUND_ACCEPT);
			}
		}
		else if(event.equalsIgnoreCase("eye_of_argos_q0601_0201.htm"))
		{
			final int random = Rnd.get(101);
			int i = 0;
			int item = 0;
			int adena = 0;
			while(i < _601_WatchingEyes.REWARDS.length)
			{
				item = _601_WatchingEyes.REWARDS[i][0];
				adena = _601_WatchingEyes.REWARDS[i][1];
				if(_601_WatchingEyes.REWARDS[i][2] <= random && random <= _601_WatchingEyes.REWARDS[i][3])
					break;
				++i;
			}
			st.giveItems(57, (long) (adena * Config.RATE_QUESTS_REWARD), true);
			if(item != 0)
			{
				st.giveItems(item, (long) (5.0f * Config.RATE_QUESTS_REWARD), true);
				st.addExpAndSp((long) (120000.0f * Config.RATE_QUESTS_REWARD), (long) (10000.0f * Config.RATE_QUESTS_REWARD), true);
			}
			st.takeItems(_601_WatchingEyes.PROOF_OF_AVENGER, -1L);
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
		if(cond == 0)
			htmltext = "eye_of_argos_q0601_0101.htm";
		else if(cond == 1)
			htmltext = "eye_of_argos_q0601_0106.htm";
		else if(cond == 2 && st.getQuestItemsCount(_601_WatchingEyes.PROOF_OF_AVENGER) >= 100L)
			htmltext = "eye_of_argos_q0601_0105.htm";
		else
		{
			htmltext = "eye_of_argos_q0601_0202.htm";
			st.set("cond", "1");
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1)
		{
			st.rollAndGive(_601_WatchingEyes.PROOF_OF_AVENGER, (int) Config.RATE_QUESTS_DROP, (int) Config.RATE_QUESTS_DROP, 100, 50.0);
			st.playSound(Quest.SOUND_ITEMGET);
			if(st.getQuestItemsCount(_601_WatchingEyes.PROOF_OF_AVENGER) >= 100L)
			{
				st.set("cond", "2");
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		return null;
	}

	static
	{
		_601_WatchingEyes.EYE_OF_ARGOS = 31683;
		_601_WatchingEyes.PROOF_OF_AVENGER = 7188;
		_601_WatchingEyes.MOBS = new int[] { 21306, 21308, 21309, 21310, 21311 };
		_601_WatchingEyes.REWARDS = new int[][] { { 6699, 90000, 0, 19 }, { 6698, 80000, 20, 39 }, { 6700, 40000, 40, 49 }, { 0, 230000, 50, 100 } };
	}
}
