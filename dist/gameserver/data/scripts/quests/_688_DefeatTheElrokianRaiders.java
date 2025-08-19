package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _688_DefeatTheElrokianRaiders extends Quest implements ScriptFile
{
	private static int DROP_CHANCE;
	private static int DINOSAUR_FANG_NECKLACE;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _688_DefeatTheElrokianRaiders()
	{
		super(false);
		this.addStartNpc(32105);
		this.addTalkId(new int[] { 32105 });
		this.addKillId(new int[] { 22214 });
		addQuestItem(new int[] { _688_DefeatTheElrokianRaiders.DINOSAUR_FANG_NECKLACE });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final long count = st.getQuestItemsCount(_688_DefeatTheElrokianRaiders.DINOSAUR_FANG_NECKLACE);
		if(event.equalsIgnoreCase("32105-02.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("32105-08.htm"))
		{
			if(count > 0L)
			{
				st.takeItems(_688_DefeatTheElrokianRaiders.DINOSAUR_FANG_NECKLACE, -1L);
				st.giveItems(57, count * 3000L);
			}
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("32105-06.htm"))
		{
			st.takeItems(_688_DefeatTheElrokianRaiders.DINOSAUR_FANG_NECKLACE, -1L);
			st.giveItems(57, count * 3000L);
		}
		else if(event.equalsIgnoreCase("32105-07.htm"))
		{
			if(count >= 100L)
			{
				st.takeItems(_688_DefeatTheElrokianRaiders.DINOSAUR_FANG_NECKLACE, 100L);
				st.giveItems(57, 450000L);
			}
			else
				htmltext = "32105-04.htm";
		}
		else if(event.equalsIgnoreCase("None"))
			htmltext = null;
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		final long count = st.getQuestItemsCount(_688_DefeatTheElrokianRaiders.DINOSAUR_FANG_NECKLACE);
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() >= 75)
				htmltext = "32105-01.htm";
			else
			{
				htmltext = "32105-00.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 1)
			if(count == 0L)
				htmltext = "32105-04.htm";
			else
				htmltext = "32105-05.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final long count = st.getQuestItemsCount(_688_DefeatTheElrokianRaiders.DINOSAUR_FANG_NECKLACE);
		if(st.getInt("cond") == 1 && count < 100L && Rnd.chance(_688_DefeatTheElrokianRaiders.DROP_CHANCE))
		{
			long numItems = (int) Config.RATE_QUESTS_REWARD;
			if(count + numItems > 100L)
				numItems = 100L - count;
			if(count + numItems >= 100L)
				st.playSound("ItemSound.quest_middle");
			else
				st.playSound("ItemSound.quest_itemget");
			st.giveItems(_688_DefeatTheElrokianRaiders.DINOSAUR_FANG_NECKLACE, numItems);
		}
		return null;
	}

	static
	{
		_688_DefeatTheElrokianRaiders.DROP_CHANCE = 50;
		_688_DefeatTheElrokianRaiders.DINOSAUR_FANG_NECKLACE = 8785;
	}
}
