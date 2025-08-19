package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _029_ChestCaughtWithABaitOfEarth extends Quest implements ScriptFile
{
	int Willie;
	int Anabel;
	int SmallPurpleTreasureChest;
	int SmallGlassBox;
	int PlatedLeatherGloves;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _029_ChestCaughtWithABaitOfEarth()
	{
		super(false);
		Willie = 31574;
		Anabel = 30909;
		SmallPurpleTreasureChest = 6507;
		SmallGlassBox = 7627;
		PlatedLeatherGloves = 2455;
		this.addStartNpc(Willie);
		this.addTalkId(new int[] { Anabel });
		addQuestItem(new int[] { SmallGlassBox });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("fisher_willeri_q0029_0104.htm"))
		{
			st.setState(2);
			st.setCond(1);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("fisher_willeri_q0029_0201.htm"))
		{
			if(st.getQuestItemsCount(SmallPurpleTreasureChest) > 0L)
			{
				st.setCond(2);
				st.playSound(Quest.SOUND_MIDDLE);
				st.takeItems(SmallPurpleTreasureChest, 1L);
				st.giveItems(SmallGlassBox, 1L);
			}
			else
				htmltext = "fisher_willeri_q0029_0202.htm";
		}
		else if(event.equals("29_GiveGlassBox"))
			if(st.getQuestItemsCount(SmallGlassBox) == 1L)
			{
				htmltext = "magister_anabel_q0029_0301.htm";
				st.takeItems(SmallGlassBox, -1L);
				st.giveItems(PlatedLeatherGloves, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
			else
			{
				htmltext = "magister_anabel_q0029_0302.htm";
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
		if(npcId == Willie)
		{
			if(id == 1)
			{
				if(st.getPlayer().getLevel() < 48)
				{
					htmltext = "fisher_willeri_q0029_0102.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					final QuestState WilliesSpecialBait = st.getPlayer().getQuestState(52);
					if(WilliesSpecialBait != null)
					{
						if(WilliesSpecialBait.isCompleted())
							htmltext = "fisher_willeri_q0029_0101.htm";
						else
						{
							htmltext = "fisher_willeri_q0029_0102.htm";
							st.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "fisher_willeri_q0029_0103.htm";
						st.exitCurrentQuest(true);
					}
				}
			}
			else if(cond == 1)
			{
				htmltext = "fisher_willeri_q0029_0105.htm";
				if(st.getQuestItemsCount(SmallPurpleTreasureChest) == 0L)
					htmltext = "fisher_willeri_q0029_0106.htm";
			}
			else if(cond == 2)
				htmltext = "fisher_willeri_q0029_0203.htm";
		}
		else if(npcId == Anabel)
			if(cond == 2)
				htmltext = "magister_anabel_q0029_0201.htm";
			else
				htmltext = "magister_anabel_q0029_0302.htm";
		return htmltext;
	}
}
