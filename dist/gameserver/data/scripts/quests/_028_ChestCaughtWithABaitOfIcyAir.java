package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _028_ChestCaughtWithABaitOfIcyAir extends Quest implements ScriptFile
{
	int OFulle;
	int Kiki;
	int BigYellowTreasureChest;
	int KikisLetter;
	int ElvenRing;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _028_ChestCaughtWithABaitOfIcyAir()
	{
		super(false);
		OFulle = 31572;
		Kiki = 31442;
		BigYellowTreasureChest = 6503;
		KikisLetter = 7626;
		ElvenRing = 881;
		this.addStartNpc(OFulle);
		this.addTalkId(new int[] { Kiki });
		addQuestItem(new int[] { KikisLetter });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("fisher_ofulle_q0028_0104.htm"))
		{
			st.setState(2);
			st.setCond(1);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("fisher_ofulle_q0028_0201.htm"))
		{
			if(st.getQuestItemsCount(BigYellowTreasureChest) > 0L)
			{
				st.setCond(2);
				st.takeItems(BigYellowTreasureChest, 1L);
				st.giveItems(KikisLetter, 1L);
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				htmltext = "fisher_ofulle_q0028_0202.htm";
		}
		else if(event.equals("mineral_trader_kiki_q0028_0301.htm"))
			if(st.getQuestItemsCount(KikisLetter) == 1L)
			{
				htmltext = "mineral_trader_kiki_q0028_0301.htm";
				st.takeItems(KikisLetter, -1L);
				st.giveItems(ElvenRing, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
			else
			{
				htmltext = "mineral_trader_kiki_q0028_0302.htm";
				st.exitCurrentQuest(true);
			}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		final int cond = st.getCond();
		if(npcId == OFulle)
		{
			if(id == 1)
			{
				if(st.getPlayer().getLevel() < 36)
				{
					htmltext = "fisher_ofulle_q0028_0101.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					final QuestState OFullesSpecialBait = st.getPlayer().getQuestState(51);
					if(OFullesSpecialBait != null)
					{
						if(OFullesSpecialBait.isCompleted())
							htmltext = "fisher_ofulle_q0028_0101.htm";
						else
						{
							htmltext = "fisher_ofulle_q0028_0102.htm";
							st.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "fisher_ofulle_q0028_0103.htm";
						st.exitCurrentQuest(true);
					}
				}
			}
			else if(cond == 1)
			{
				htmltext = "fisher_ofulle_q0028_0105.htm";
				if(st.getQuestItemsCount(BigYellowTreasureChest) == 0L)
					htmltext = "fisher_ofulle_q0028_0106.htm";
			}
			else if(cond == 2)
				htmltext = "fisher_ofulle_q0028_0203.htm";
		}
		else if(npcId == Kiki)
			if(cond == 2)
				htmltext = "mineral_trader_kiki_q0028_0201.htm";
			else
				htmltext = "mineral_trader_kiki_q0028_0302.htm";
		return htmltext;
	}
}
