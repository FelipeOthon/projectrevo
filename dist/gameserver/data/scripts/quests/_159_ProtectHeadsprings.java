package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _159_ProtectHeadsprings extends Quest implements ScriptFile
{
	int PLAGUE_DUST_ID;
	int HYACINTH_CHARM1_ID;
	int HYACINTH_CHARM2_ID;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _159_ProtectHeadsprings()
	{
		super(false);
		PLAGUE_DUST_ID = 1035;
		HYACINTH_CHARM1_ID = 1071;
		HYACINTH_CHARM2_ID = 1072;
		this.addStartNpc(30154);
		this.addKillId(new int[] { 27017 });
		addQuestItem(new int[] { PLAGUE_DUST_ID, HYACINTH_CHARM1_ID, HYACINTH_CHARM2_ID });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("quest_accept"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			if(st.getQuestItemsCount(HYACINTH_CHARM1_ID) == 0L)
			{
				st.giveItems(HYACINTH_CHARM1_ID, 1L);
				htmltext = "ozzy_q0159_04.htm";
			}
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getRace() != Race.elf)
			{
				htmltext = "ozzy_q0159_00.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				if(st.getPlayer().getLevel() >= 12)
				{
					htmltext = "ozzy_q0159_03.htm";
					return htmltext;
				}
				htmltext = "ozzy_q0159_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 1)
			htmltext = "ozzy_q0159_05.htm";
		else if(cond == 2)
		{
			st.takeItems(PLAGUE_DUST_ID, -1L);
			st.takeItems(HYACINTH_CHARM1_ID, -1L);
			st.giveItems(HYACINTH_CHARM2_ID, 1L);
			st.set("cond", "3");
			htmltext = "ozzy_q0159_06.htm";
		}
		else if(cond == 3)
			htmltext = "ozzy_q0159_07.htm";
		else if(cond == 4)
		{
			st.takeItems(PLAGUE_DUST_ID, -1L);
			st.takeItems(HYACINTH_CHARM2_ID, -1L);
			st.giveItems(57, 18250L);
			st.playSound(Quest.SOUND_FINISH);
			htmltext = "ozzy_q0159_08.htm";
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getInt("cond");
		if(cond == 1 && Rnd.chance(60))
		{
			st.giveItems(PLAGUE_DUST_ID, 1L);
			st.set("cond", "2");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(cond == 3 && Rnd.chance(60))
			if(st.getQuestItemsCount(PLAGUE_DUST_ID) == 4L)
			{
				st.giveItems(PLAGUE_DUST_ID, 1L);
				st.set("cond", "4");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
			{
				st.giveItems(PLAGUE_DUST_ID, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		return null;
	}
}
