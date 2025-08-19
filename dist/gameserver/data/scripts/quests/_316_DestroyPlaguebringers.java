package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _316_DestroyPlaguebringers extends Quest implements ScriptFile
{
	private static int Ellenia;
	private static int Sukar_Wererat;
	private static int Sukar_Wererat_Leader;
	private static int Varool_Foulclaw;
	private static int Wererats_Fang;
	private static int Varool_Foulclaws_Fang;
	private static int Wererats_Fang_Chance;
	private static int Varool_Foulclaws_Fang_Chance;

	public _316_DestroyPlaguebringers()
	{
		super(false);
		this.addStartNpc(_316_DestroyPlaguebringers.Ellenia);
		this.addKillId(new int[] { _316_DestroyPlaguebringers.Sukar_Wererat });
		this.addKillId(new int[] { _316_DestroyPlaguebringers.Sukar_Wererat_Leader });
		this.addKillId(new int[] { _316_DestroyPlaguebringers.Varool_Foulclaw });
		addQuestItem(new int[] { _316_DestroyPlaguebringers.Wererats_Fang });
		addQuestItem(new int[] { _316_DestroyPlaguebringers.Varool_Foulclaws_Fang });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("elliasin_q0316_04.htm") && _state == 1 && st.getPlayer().getRace() == Race.elf && st.getPlayer().getLevel() >= 18)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("elliasin_q0316_08.htm") && _state == 2)
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(npc.getNpcId() != _316_DestroyPlaguebringers.Ellenia)
			return htmltext;
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getRace() != Race.elf)
			{
				htmltext = "elliasin_q0316_00.htm";
				st.exitCurrentQuest(true);
			}
			else if(st.getPlayer().getLevel() < 18)
			{
				htmltext = "elliasin_q0316_02.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "elliasin_q0316_03.htm";
				st.set("cond", "0");
			}
		}
		else if(_state == 2)
		{
			final long Reward = st.getQuestItemsCount(_316_DestroyPlaguebringers.Wererats_Fang) * 90L + st.getQuestItemsCount(_316_DestroyPlaguebringers.Varool_Foulclaws_Fang) * 10000L;
			if(Reward > 0L)
			{
				htmltext = "elliasin_q0316_07.htm";
				st.takeItems(_316_DestroyPlaguebringers.Wererats_Fang, -1L);
				st.takeItems(_316_DestroyPlaguebringers.Varool_Foulclaws_Fang, -1L);
				st.giveItems(57, Reward);
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				htmltext = "elliasin_q0316_05.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		if(npc.getNpcId() == _316_DestroyPlaguebringers.Varool_Foulclaw && qs.getQuestItemsCount(_316_DestroyPlaguebringers.Varool_Foulclaws_Fang) == 0L && Rnd.chance(_316_DestroyPlaguebringers.Varool_Foulclaws_Fang_Chance))
		{
			qs.giveItems(_316_DestroyPlaguebringers.Varool_Foulclaws_Fang, 1L);
			qs.playSound(Quest.SOUND_ITEMGET);
		}
		else if(Rnd.chance(_316_DestroyPlaguebringers.Wererats_Fang_Chance))
		{
			qs.giveItems(_316_DestroyPlaguebringers.Wererats_Fang, 1L);
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
		_316_DestroyPlaguebringers.Ellenia = 30155;
		_316_DestroyPlaguebringers.Sukar_Wererat = 20040;
		_316_DestroyPlaguebringers.Sukar_Wererat_Leader = 20047;
		_316_DestroyPlaguebringers.Varool_Foulclaw = 27020;
		_316_DestroyPlaguebringers.Wererats_Fang = 1042;
		_316_DestroyPlaguebringers.Varool_Foulclaws_Fang = 1043;
		_316_DestroyPlaguebringers.Wererats_Fang_Chance = 50;
		_316_DestroyPlaguebringers.Varool_Foulclaws_Fang_Chance = 30;
	}
}
