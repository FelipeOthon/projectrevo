package quests;

import l2s.gameserver.Config;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _004_LongLivethePaagrioLord extends Quest implements ScriptFile
{
	int HONEY_KHANDAR;
	int BEAR_FUR_CLOAK;
	int BLOODY_AXE;
	int ANCESTOR_SKULL;
	int SPIDER_DUST;
	int DEEP_SEA_ORB;
	int[][] NPC_GIFTS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _004_LongLivethePaagrioLord()
	{
		super(false);
		HONEY_KHANDAR = 1541;
		BEAR_FUR_CLOAK = 1542;
		BLOODY_AXE = 1543;
		ANCESTOR_SKULL = 1544;
		SPIDER_DUST = 1545;
		DEEP_SEA_ORB = 1546;
		NPC_GIFTS = new int[][] {
				{ 30585, BEAR_FUR_CLOAK },
				{ 30566, HONEY_KHANDAR },
				{ 30562, BLOODY_AXE },
				{ 30560, ANCESTOR_SKULL },
				{ 30559, SPIDER_DUST },
				{ 30587, DEEP_SEA_ORB } };
		this.addStartNpc(30578);
		this.addTalkId(new int[] { 30559, 30560, 30562, 30566, 30578, 30585, 30587 });
		addQuestItem(new int[] { SPIDER_DUST, ANCESTOR_SKULL, BLOODY_AXE, HONEY_KHANDAR, BEAR_FUR_CLOAK, DEEP_SEA_ORB });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30578-03.htm"))
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
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30578)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getRace() != Race.orc)
				{
					htmltext = "30578-00.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() >= 2)
					htmltext = "30578-02.htm";
				else
				{
					htmltext = "30578-01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "30578-04.htm";
			else if(cond == 2)
			{
				htmltext = "30578-06.htm";
				for(final int[] item : NPC_GIFTS)
					st.takeItems(item[1], -1L);
				st.giveItems(4, 1L, false);
				st.giveItems(57, (int) ((Config.RATE_QUESTS_REWARD - 1.0f) * 590.0f + 1850.0f * Config.RATE_QUESTS_REWARD), false);
				st.getPlayer().addExpAndSp(4254L, 335L, false, false);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else if(cond == 1)
			for(final int[] Id : NPC_GIFTS)
				if(Id[0] == npcId)
				{
					final int item2 = Id[1];
					if(st.getQuestItemsCount(item2) > 0L)
						htmltext = npcId + "-02.htm";
					else
					{
						st.giveItems(item2, 1L, false);
						htmltext = npcId + "-01.htm";
						int count = 0;
						for(final int[] item3 : NPC_GIFTS)
							count += (int) st.getQuestItemsCount(item3[1]);
						if(count == 6)
						{
							st.set("cond", "2");
							st.playSound(Quest.SOUND_MIDDLE);
						}
						else
							st.playSound(Quest.SOUND_ITEMGET);
					}
					return htmltext;
				}
		return htmltext;
	}
}
