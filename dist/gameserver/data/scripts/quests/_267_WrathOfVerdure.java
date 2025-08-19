package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _267_WrathOfVerdure extends Quest implements ScriptFile
{
	private static int Treant_Bremec;
	private static int Goblin_Raider;
	private static int Goblin_Club;
	private static int Silvery_Leaf;
	private static int Goblin_Club_Chance;

	public _267_WrathOfVerdure()
	{
		super(false);
		this.addStartNpc(_267_WrathOfVerdure.Treant_Bremec);
		this.addKillId(new int[] { _267_WrathOfVerdure.Goblin_Raider });
		addQuestItem(new int[] { _267_WrathOfVerdure.Goblin_Club });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("bri_mec_tran_q0267_03.htm") && _state == 1 && st.getPlayer().getRace() == Race.elf && st.getPlayer().getLevel() >= 4)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("bri_mec_tran_q0267_06.htm") && _state == 2)
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(npc.getNpcId() != _267_WrathOfVerdure.Treant_Bremec)
			return htmltext;
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getRace() != Race.elf)
			{
				htmltext = "bri_mec_tran_q0267_00.htm";
				st.exitCurrentQuest(true);
			}
			else if(st.getPlayer().getLevel() < 4)
			{
				htmltext = "bri_mec_tran_q0267_01.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "bri_mec_tran_q0267_02.htm";
				st.set("cond", "0");
			}
		}
		else if(_state == 2)
		{
			final long Goblin_Club_Count = st.getQuestItemsCount(_267_WrathOfVerdure.Goblin_Club);
			if(Goblin_Club_Count > 0L)
			{
				htmltext = "bri_mec_tran_q0267_05.htm";
				st.takeItems(_267_WrathOfVerdure.Goblin_Club, -1L);
				st.giveItems(_267_WrathOfVerdure.Silvery_Leaf, Goblin_Club_Count);
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				htmltext = "bri_mec_tran_q0267_04.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		if(Rnd.chance(_267_WrathOfVerdure.Goblin_Club_Chance))
		{
			qs.giveItems(_267_WrathOfVerdure.Goblin_Club, 1L);
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
		_267_WrathOfVerdure.Treant_Bremec = 31853;
		_267_WrathOfVerdure.Goblin_Raider = 20325;
		_267_WrathOfVerdure.Goblin_Club = 1335;
		_267_WrathOfVerdure.Silvery_Leaf = 1340;
		_267_WrathOfVerdure.Goblin_Club_Chance = 50;
	}
}
