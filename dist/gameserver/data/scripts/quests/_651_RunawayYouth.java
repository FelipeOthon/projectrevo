package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _651_RunawayYouth extends Quest implements ScriptFile
{
	private static int IVAN;
	private static int BATIDAE;
	protected NpcInstance _npc;
	private static int SOE;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _651_RunawayYouth()
	{
		super(false);
		this.addStartNpc(_651_RunawayYouth.IVAN);
		this.addTalkId(new int[] { _651_RunawayYouth.BATIDAE });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("runaway_boy_ivan_q0651_03.htm"))
		{
			if(st.getQuestItemsCount(_651_RunawayYouth.SOE) > 0L)
			{
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
				st.takeItems(_651_RunawayYouth.SOE, 1L);
				htmltext = "runaway_boy_ivan_q0651_04.htm";
				st.startQuestTimer("ivan_timer", 20000L);
			}
		}
		else if(event.equalsIgnoreCase("runaway_boy_ivan_q0651_05.htm"))
		{
			st.exitCurrentQuest(true);
			st.playSound(Quest.SOUND_GIVEUP);
		}
		else if(event.equalsIgnoreCase("ivan_timer"))
		{
			_npc.deleteMe();
			htmltext = null;
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == _651_RunawayYouth.IVAN && cond == 0)
		{
			if(st.getPlayer().getLevel() >= 26)
				htmltext = "runaway_boy_ivan_q0651_01.htm";
			else
			{
				htmltext = "runaway_boy_ivan_q0651_01a.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == _651_RunawayYouth.BATIDAE && cond == 1)
		{
			htmltext = "fisher_batidae_q0651_01.htm";
			st.giveItems(57, Math.round(2883.0f * st.getRateQuestsReward()));
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	static
	{
		_651_RunawayYouth.IVAN = 32014;
		_651_RunawayYouth.BATIDAE = 31989;
		_651_RunawayYouth.SOE = 736;
	}
}
