package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _303_CollectArrowheads extends Quest implements ScriptFile
{
	int ORCISH_ARROWHEAD;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _303_CollectArrowheads()
	{
		super(false);
		ORCISH_ARROWHEAD = 963;
		this.addStartNpc(30029);
		this.addTalkId(new int[] { 30029 });
		this.addKillId(new int[] { 20361 });
		addQuestItem(new int[] { ORCISH_ARROWHEAD });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("minx_q0303_04.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() >= 10)
				htmltext = "minx_q0303_03.htm";
			else
			{
				htmltext = "minx_q0303_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(st.getQuestItemsCount(ORCISH_ARROWHEAD) < 10L)
			htmltext = "minx_q0303_05.htm";
		else
		{
			st.takeItems(ORCISH_ARROWHEAD, -1L);
			st.giveItems(57, 1000L);
			st.addExpAndSp(2000L, 0L);
			htmltext = "minx_q0303_06.htm";
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(ORCISH_ARROWHEAD) < 10L)
		{
			st.giveItems(ORCISH_ARROWHEAD, 1L);
			if(st.getQuestItemsCount(ORCISH_ARROWHEAD) == 10L)
			{
				st.set("cond", "2");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
