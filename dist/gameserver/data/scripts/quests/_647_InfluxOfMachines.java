package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _647_InfluxOfMachines extends Quest implements ScriptFile
{
	private static final int DROP_CHANCE = 60;
	private static final int DESTROYED_GOLEM_SHARD = 8100;
	private static final int[] RECIPES_60;
	private static final int[] RECIPES_100;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _647_InfluxOfMachines()
	{
		super(true);
		this.addStartNpc(32069);
		this.addTalkId(new int[] { 32069 });
		this.addTalkId(new int[] { 32069 });
		this.addTalkId(new int[] { 32069 });
		for(int i = 22052; i < 22079; ++i)
			this.addKillId(new int[] { i });
		addQuestItem(new int[] { 8100 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("32069-02.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("32069-06.htm"))
			if(st.getQuestItemsCount(8100) >= 500L)
			{
				st.takeItems(8100, 500L);
				if(Config.ALT_100_RECIPES_B)
					st.giveItems(_647_InfluxOfMachines.RECIPES_100[Rnd.get(_647_InfluxOfMachines.RECIPES_100.length)], 1L);
				else
					st.giveItems(_647_InfluxOfMachines.RECIPES_60[Rnd.get(_647_InfluxOfMachines.RECIPES_60.length)], 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "32069-04.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		final long count = st.getQuestItemsCount(8100);
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() >= 46)
				htmltext = "32069-01.htm";
			else
			{
				htmltext = "32069-03.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 1 && count < 500L)
			htmltext = "32069-04.htm";
		else if(cond == 2 && count >= 500L)
			htmltext = "32069-05.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1 && st.rollAndGive(8100, (int) Config.RATE_QUESTS_DROP, (int) Config.RATE_QUESTS_DROP, 500, 60.0))
		{
			st.playSound(Quest.SOUND_MIDDLE);
			st.set("cond", "2");
		}
		return null;
	}

	static
	{
		RECIPES_60 = new int[] {
				4963,
				4964,
				4965,
				4966,
				4967,
				4968,
				4969,
				4970,
				4971,
				4972,
				5000,
				5001,
				5002,
				5003,
				5004,
				5005,
				5006,
				5007,
				8298,
				8306,
				8310,
				8312,
				8322,
				8324 };
		RECIPES_100 = new int[] {
				4182,
				4183,
				4184,
				4185,
				4186,
				4187,
				4188,
				4189,
				4190,
				4191,
				4192,
				4193,
				4194,
				4195,
				4196,
				4197,
				4198,
				4199,
				8297,
				8305,
				8309,
				8311,
				8321,
				8323 };
	}
}
