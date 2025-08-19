package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _121_PavelTheGiants extends Quest implements ScriptFile
{
	private static int NEWYEAR;
	private static int YUMI;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _121_PavelTheGiants()
	{
		super(false);
		this.addStartNpc(_121_PavelTheGiants.NEWYEAR);
		this.addTalkId(new int[] { _121_PavelTheGiants.NEWYEAR, _121_PavelTheGiants.YUMI });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("collecter_yumi_q0121_0201.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.addExpAndSp(76960L, 5793L, true);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		final int cond = st.getCond();
		if(id == 1 && npcId == _121_PavelTheGiants.NEWYEAR)
		{
			if(st.getPlayer().getLevel() >= 46)
			{
				htmltext = "head_blacksmith_newyear_q0121_0101.htm";
				st.setCond(1);
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else
			{
				htmltext = "head_blacksmith_newyear_q0121_0103.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(id == 2)
			if(npcId == _121_PavelTheGiants.YUMI && cond == 1)
				htmltext = "collecter_yumi_q0121_0101.htm";
			else
				htmltext = "head_blacksmith_newyear_q0121_0105.htm";
		return htmltext;
	}

	static
	{
		_121_PavelTheGiants.NEWYEAR = 31961;
		_121_PavelTheGiants.YUMI = 32041;
	}
}
