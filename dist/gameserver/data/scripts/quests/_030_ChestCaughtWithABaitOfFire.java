package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _030_ChestCaughtWithABaitOfFire extends Quest implements ScriptFile
{
	int Linnaeus;
	int Rukal;
	int RedTreasureChest;
	int RukalsMusicalScore;
	int NecklaceOfProtection;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _030_ChestCaughtWithABaitOfFire()
	{
		super(false);
		Linnaeus = 31577;
		Rukal = 30629;
		RedTreasureChest = 6511;
		RukalsMusicalScore = 7628;
		NecklaceOfProtection = 916;
		this.addStartNpc(Linnaeus);
		this.addTalkId(new int[] { Rukal });
		addQuestItem(new int[] { RukalsMusicalScore });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("fisher_linneaus_q0030_0104.htm"))
		{
			st.setState(2);
			st.setCond(1);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("fisher_linneaus_q0030_0201.htm"))
		{
			if(st.getQuestItemsCount(RedTreasureChest) > 0L)
			{
				st.takeItems(RedTreasureChest, 1L);
				st.giveItems(RukalsMusicalScore, 1L);
				st.setCond(2);
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				htmltext = "fisher_linneaus_q0030_0202.htm";
		}
		else if(event.equals("bard_rukal_q0030_0301.htm"))
			if(st.getQuestItemsCount(RukalsMusicalScore) == 1L)
			{
				st.takeItems(RukalsMusicalScore, -1L);
				st.giveItems(NecklaceOfProtection, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
			else
			{
				htmltext = "bard_rukal_q0030_0302.htm";
				st.exitCurrentQuest(true);
			}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int id = st.getState();
		id = st.getState();
		final int cond = st.getCond();
		if(npcId == Linnaeus)
		{
			if(id == 1)
			{
				if(st.getPlayer().getLevel() < 60)
				{
					htmltext = "fisher_linneaus_q0030_0102.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					final QuestState LinnaeusSpecialBait = st.getPlayer().getQuestState(53);
					if(LinnaeusSpecialBait != null)
					{
						if(LinnaeusSpecialBait.isCompleted())
							htmltext = "fisher_linneaus_q0030_0101.htm";
						else
						{
							htmltext = "fisher_linneaus_q0030_0102.htm";
							st.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "fisher_linneaus_q0030_0103.htm";
						st.exitCurrentQuest(true);
					}
				}
			}
			else if(cond == 1)
			{
				htmltext = "fisher_linneaus_q0030_0105.htm";
				if(st.getQuestItemsCount(RedTreasureChest) == 0L)
					htmltext = "fisher_linneaus_q0030_0106.htm";
			}
			else if(cond == 2)
				htmltext = "fisher_linneaus_q0030_0203.htm";
		}
		else if(npcId == Rukal)
			if(cond == 2)
				htmltext = "bard_rukal_q0030_0201.htm";
			else
				htmltext = "bard_rukal_q0030_0302.htm";
		return htmltext;
	}
}
