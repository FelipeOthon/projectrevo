package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _019_GoToThePastureland extends Quest implements ScriptFile
{
	int VLADIMIR;
	int TUNATUN;
	int BEAST_MEAT;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _019_GoToThePastureland()
	{
		super(false);
		VLADIMIR = 31302;
		TUNATUN = 31537;
		BEAST_MEAT = 7547;
		this.addStartNpc(VLADIMIR);
		this.addTalkId(new int[] { VLADIMIR });
		this.addTalkId(new int[] { TUNATUN });
		addQuestItem(new int[] { BEAST_MEAT });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("trader_vladimir_q0019_0104.htm"))
		{
			st.giveItems(BEAST_MEAT, 1L);
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		if(event.equals("beast_herder_tunatun_q0019_0201.htm"))
		{
			st.takeItems(BEAST_MEAT, -1L);
			st.addExpAndSp(385040L, 75250L);
			st.giveItems(57, 147200L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == VLADIMIR)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 82)
					htmltext = "trader_vladimir_q0019_0101.htm";
				else
				{
					htmltext = "trader_vladimir_q0019_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
				htmltext = "trader_vladimir_q0019_0105.htm";
		}
		else if(npcId == TUNATUN)
			if(st.getQuestItemsCount(BEAST_MEAT) >= 1L)
				htmltext = "beast_herder_tunatun_q0019_0101.htm";
			else
			{
				htmltext = "beast_herder_tunatun_q0019_0202.htm";
				st.exitCurrentQuest(true);
			}
		return htmltext;
	}
}
