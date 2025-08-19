package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _294_CovertBusiness extends Quest implements ScriptFile
{
	public static int BatFang;
	public static int RingOfRaccoon;
	public static int BarbedBat;
	public static int BladeBat;
	public static int Keef;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _294_CovertBusiness()
	{
		super(false);
		this.addStartNpc(_294_CovertBusiness.Keef);
		this.addTalkId(new int[] { _294_CovertBusiness.Keef });
		this.addKillId(new int[] { _294_CovertBusiness.BarbedBat });
		this.addKillId(new int[] { _294_CovertBusiness.BladeBat });
		addQuestItem(new int[] { _294_CovertBusiness.BatFang });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("elder_keef_q0294_03.htm"))
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
		final int id = st.getState();
		if(id == 1)
		{
			if(st.getPlayer().getRace() != Race.dwarf)
			{
				htmltext = "elder_keef_q0294_00.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				if(st.getPlayer().getLevel() >= 10)
				{
					htmltext = "elder_keef_q0294_02.htm";
					return htmltext;
				}
				htmltext = "elder_keef_q0294_01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(st.getQuestItemsCount(_294_CovertBusiness.BatFang) < 100L)
			htmltext = "elder_keef_q0294_04.htm";
		else
		{
			if(st.getQuestItemsCount(_294_CovertBusiness.RingOfRaccoon) < 1L)
			{
				st.giveItems(_294_CovertBusiness.RingOfRaccoon, 1L);
				htmltext = "elder_keef_q0294_05.htm";
			}
			else
			{
				st.giveItems(57, 2400L);
				htmltext = "elder_keef_q0294_06.htm";
			}
			st.addExpAndSp(0L, 600L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getCond() == 1)
			st.rollAndGive(_294_CovertBusiness.BatFang, 1, 2, 100, 100.0);
		return null;
	}

	static
	{
		_294_CovertBusiness.BatFang = 1491;
		_294_CovertBusiness.RingOfRaccoon = 1508;
		_294_CovertBusiness.BarbedBat = 20370;
		_294_CovertBusiness.BladeBat = 20480;
		_294_CovertBusiness.Keef = 30534;
	}
}
