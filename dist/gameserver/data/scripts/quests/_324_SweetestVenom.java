package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _324_SweetestVenom extends Quest implements ScriptFile
{
	private static int ASTARON;
	private static int Prowler;
	private static int Venomous_Spider;
	private static int Arachnid_Tracker;
	private static int VENOM_SAC;
	private static int VENOM_SAC_BASECHANCE;

	public _324_SweetestVenom()
	{
		super(false);
		this.addStartNpc(_324_SweetestVenom.ASTARON);
		this.addKillId(new int[] { _324_SweetestVenom.Prowler });
		this.addKillId(new int[] { _324_SweetestVenom.Venomous_Spider });
		this.addKillId(new int[] { _324_SweetestVenom.Arachnid_Tracker });
		addQuestItem(new int[] { _324_SweetestVenom.VENOM_SAC });
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(npc.getNpcId() != _324_SweetestVenom.ASTARON)
			return htmltext;
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getLevel() >= 18)
			{
				htmltext = "astaron_q0324_03.htm";
				st.set("cond", "0");
			}
			else
			{
				htmltext = "astaron_q0324_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(_state == 2)
		{
			final long _count = st.getQuestItemsCount(_324_SweetestVenom.VENOM_SAC);
			if(_count >= 10L)
			{
				htmltext = "astaron_q0324_06.htm";
				st.takeItems(_324_SweetestVenom.VENOM_SAC, -1L);
				st.giveItems(57, 5810L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "astaron_q0324_05.htm";
		}
		return htmltext;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("astaron_q0324_04.htm") && st.getState() == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		final long _count = qs.getQuestItemsCount(_324_SweetestVenom.VENOM_SAC);
		final int _chance = _324_SweetestVenom.VENOM_SAC_BASECHANCE + (npc.getNpcId() - _324_SweetestVenom.Prowler) / 4 * 12;
		if(_count < 10L && Rnd.chance(_chance))
		{
			qs.giveItems(_324_SweetestVenom.VENOM_SAC, 1L);
			if(_count == 9L)
			{
				qs.set("cond", "2");
				qs.playSound(Quest.SOUND_MIDDLE);
			}
			else
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
		_324_SweetestVenom.ASTARON = 30351;
		_324_SweetestVenom.Prowler = 20034;
		_324_SweetestVenom.Venomous_Spider = 20038;
		_324_SweetestVenom.Arachnid_Tracker = 20043;
		_324_SweetestVenom.VENOM_SAC = 1077;
		_324_SweetestVenom.VENOM_SAC_BASECHANCE = 60;
	}
}
