package quests;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _383_SearchingForTreasure extends Quest implements ScriptFile
{
	private static final int PIRATES_TREASURE_MAP = 5915;
	private static final int SHARK = 20314;
	private static final int ESPEN = 30890;
	private static final int PIRATES_CHEST = 31148;
	private static List<rewardInfo> rewards;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _383_SearchingForTreasure()
	{
		super(false);
		this.addStartNpc(30890);
		this.addTalkId(new int[] { 31148 });
		addQuestItem(new int[] { 5915 });
		_383_SearchingForTreasure.rewards.add(new rewardInfo(952, 1, 8));
		_383_SearchingForTreasure.rewards.add(new rewardInfo(956, 1, 15));
		_383_SearchingForTreasure.rewards.add(new rewardInfo(1337, 1, 130));
		_383_SearchingForTreasure.rewards.add(new rewardInfo(1338, 2, 150));
		_383_SearchingForTreasure.rewards.add(new rewardInfo(2450, 1, 2));
		_383_SearchingForTreasure.rewards.add(new rewardInfo(2451, 1, 2));
		_383_SearchingForTreasure.rewards.add(new rewardInfo(3452, 1, 140));
		_383_SearchingForTreasure.rewards.add(new rewardInfo(3455, 1, 120));
		_383_SearchingForTreasure.rewards.add(new rewardInfo(4408, 1, 220));
		_383_SearchingForTreasure.rewards.add(new rewardInfo(4409, 1, 220));
		_383_SearchingForTreasure.rewards.add(new rewardInfo(4418, 1, 220));
		_383_SearchingForTreasure.rewards.add(new rewardInfo(4419, 1, 220));
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30890-03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("30890-07.htm"))
		{
			if(st.getQuestItemsCount(5915) > 0L)
			{
				st.set("cond", "2");
				st.takeItems(5915, 1L);
				st.addSpawn(31148, 106583, 197747, -4209, 900000);
				st.addSpawn(20314, 106570, 197740, -4209, 900000);
				st.addSpawn(20314, 106580, 197747, -4209, 900000);
				st.addSpawn(20314, 106590, 197743, -4209, 900000);
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else
			{
				htmltext = "You don't have required items";
				st.exitCurrentQuest(true);
			}
		}
		else if(event.equalsIgnoreCase("30890-02b.htm"))
		{
			if(st.getQuestItemsCount(5915) > 0L)
			{
				st.giveItems(57, 1000L);
				st.playSound("ItemSound.quest_finish");
			}
			else
				htmltext = "You don't have required items";
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("31148-02.htm"))
			if(st.getQuestItemsCount(1661) > 0L)
			{
				st.takeItems(1661, 1L);
				st.giveItems(57, 500 + Rnd.get(5) * 300);
				int count = 0;
				while(count < 1)
					for(final rewardInfo reward : _383_SearchingForTreasure.rewards)
					{
						final int id = reward.id;
						final int qty = reward.count;
						final int chance = reward.chance;
						if(Rnd.get(1000) < chance && count < 2)
						{
							st.giveItems(id, Rnd.get(qty) + 1);
							++count;
						}
						if(count < 2)
							for(int i = 4481; i <= 4505; ++i)
								if(Rnd.get(500) == 1 && count < 2)
								{
									st.giveItems(i, 1L);
									++count;
								}
					}
				st.playSound("ItemSound.quest_finish");
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "31148-03.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		if(st.getState() == 1)
		{
			if(st.getPlayer().getLevel() >= 42)
			{
				if(st.getQuestItemsCount(5915) > 0L)
					htmltext = "30890-01.htm";
				else
				{
					htmltext = "30890-00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "30890-01a.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == 30890)
			htmltext = "30890-03a.htm";
		else if(npcId == 31148 && st.getInt("cond") == 2 && st.getState() == 2)
			htmltext = "31148-01.htm";
		return htmltext;
	}

	static
	{
		_383_SearchingForTreasure.rewards = new ArrayList<rewardInfo>();
	}

	private class rewardInfo
	{
		public int id;
		public int count;
		public int chance;

		public rewardInfo(final int _id, final int _count, final int _chance)
		{
			id = _id;
			count = _count;
			chance = _chance;
		}
	}
}
