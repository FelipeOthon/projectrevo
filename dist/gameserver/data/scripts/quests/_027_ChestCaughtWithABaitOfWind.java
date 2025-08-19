package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _027_ChestCaughtWithABaitOfWind extends Quest implements ScriptFile
{
	private static final int Lanosco = 31570;
	private static final int Shaling = 31434;
	private static final int StrangeGolemBlueprint = 7625;
	private static final int BigBlueTreasureChest = 6500;
	private static final int BlackPearlRing = 880;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _027_ChestCaughtWithABaitOfWind()
	{
		super(false);
		this.addStartNpc(31570);
		this.addTalkId(new int[] { 31434 });
		addQuestItem(new int[] { 7625 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("fisher_lanosco_q0027_0104.htm"))
		{
			st.setCond(1);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("fisher_lanosco_q0027_0201.htm"))
		{
			if(st.getQuestItemsCount(6500) > 0L)
			{
				st.takeItems(6500, 1L);
				st.giveItems(7625, 1L);
				st.setCond(2);
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				htmltext = "fisher_lanosco_q0027_0202.htm";
		}
		else if(event.equals("blueprint_seller_shaling_q0027_0301.htm"))
			if(st.getQuestItemsCount(7625) == 1L)
			{
				st.takeItems(7625, -1L);
				st.giveItems(880, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
			else
			{
				htmltext = "blueprint_seller_shaling_q0027_0302.htm";
				st.exitCurrentQuest(true);
			}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getCond();
		final int id = st.getState();
		if(npcId == 31570)
		{
			if(id == 1)
			{
				if(st.getPlayer().getLevel() < 27)
				{
					htmltext = "fisher_lanosco_q0027_0101.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					final QuestState LanoscosSpecialBait = st.getPlayer().getQuestState(50);
					if(LanoscosSpecialBait != null)
					{
						if(LanoscosSpecialBait.isCompleted())
							htmltext = "fisher_lanosco_q0027_0101.htm";
						else
						{
							htmltext = "fisher_lanosco_q0027_0102.htm";
							st.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "fisher_lanosco_q0027_0103.htm";
						st.exitCurrentQuest(true);
					}
				}
			}
			else if(cond == 1)
			{
				htmltext = "fisher_lanosco_q0027_0105.htm";
				if(st.getQuestItemsCount(6500) == 0L)
					htmltext = "fisher_lanosco_q0027_0106.htm";
			}
			else if(cond == 2)
				htmltext = "fisher_lanosco_q0027_0203.htm";
		}
		else if(npcId == 31434)
			if(cond == 2)
				htmltext = "blueprint_seller_shaling_q0027_0201.htm";
			else
				htmltext = "blueprint_seller_shaling_q0027_0302.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		return null;
	}
}
