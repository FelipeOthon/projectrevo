package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _368_TrespassingIntoTheSacredArea extends Quest implements ScriptFile
{
	private static int RESTINA;
	private static int BLADE_STAKATO_FANG;
	private static int BLADE_STAKATO_FANG_BASECHANCE;

	public _368_TrespassingIntoTheSacredArea()
	{
		super(false);
		this.addStartNpc(_368_TrespassingIntoTheSacredArea.RESTINA);
		for(int Blade_Stakato_id = 20794; Blade_Stakato_id <= 20797; ++Blade_Stakato_id)
			this.addKillId(new int[] { Blade_Stakato_id });
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(npc.getNpcId() != _368_TrespassingIntoTheSacredArea.RESTINA)
			return htmltext;
		if(st.getState() == 1)
		{
			if(st.getPlayer().getLevel() < 36)
			{
				htmltext = "30926-00.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "30926-01.htm";
				st.set("cond", "0");
			}
		}
		else
		{
			final long _count = st.getQuestItemsCount(_368_TrespassingIntoTheSacredArea.BLADE_STAKATO_FANG);
			if(_count > 0L)
			{
				htmltext = "30926-04.htm";
				st.takeItems(_368_TrespassingIntoTheSacredArea.BLADE_STAKATO_FANG, -1L);
				st.giveItems(57, _count * 2250L);
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				htmltext = "30926-03.htm";
		}
		return htmltext;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("30926-02.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30926-05.htm") && _state == 2)
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		if(Rnd.chance(npc.getNpcId() - 20794 + _368_TrespassingIntoTheSacredArea.BLADE_STAKATO_FANG_BASECHANCE))
		{
			qs.giveItems(_368_TrespassingIntoTheSacredArea.BLADE_STAKATO_FANG, 1L);
			qs.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		_368_TrespassingIntoTheSacredArea.RESTINA = 30926;
		_368_TrespassingIntoTheSacredArea.BLADE_STAKATO_FANG = 5881;
		_368_TrespassingIntoTheSacredArea.BLADE_STAKATO_FANG_BASECHANCE = 10;
	}
}
