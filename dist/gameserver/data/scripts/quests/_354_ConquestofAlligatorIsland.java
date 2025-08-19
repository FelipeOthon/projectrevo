package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _354_ConquestofAlligatorIsland extends Quest implements ScriptFile
{
	public final int KLUCK = 30895;
	public final int CROKIAN_LAD = 20804;
	public final int DAILAON_LAD = 20805;
	public final int CROKIAN_LAD_WARRIOR = 20806;
	public final int FARHITE_LAD = 20807;
	public final int NOS_LAD = 20808;
	public final int SWAMP_TRIBE = 20991;
	public final int ALLIGATOR_TOOTH = 5863;
	public final int TORN_MAP_FRAGMENT = 5864;
	public final int PIRATES_TREASURE_MAP = 5915;
	public final int CHANCE = 35;
	public final int CHANCE2 = 10;
	public final int[] MOBLIST;
	public final int[][] RANDOM_REWARDS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _354_ConquestofAlligatorIsland()
	{
		super(false);
		MOBLIST = new int[] { 20804, 20805, 20806, 20807, 20808, 20991 };
		RANDOM_REWARDS = new int[][] {
				{ 736, 15 },
				{ 1061, 20 },
				{ 734, 10 },
				{ 735, 5 },
				{ 1878, 25 },
				{ 1875, 10 },
				{ 1879, 10 },
				{ 1880, 10 },
				{ 956, 1 },
				{ 955, 1 } };
		this.addStartNpc(30895);
		for(final int i : MOBLIST)
			this.addKillId(new int[] { i });
		addQuestItem(new int[] { 5863, 5864 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final long amount = st.getQuestItemsCount(5863);
		if(event.equalsIgnoreCase("30895-00a.htm"))
			st.exitCurrentQuest(true);
		else if(event.equalsIgnoreCase("1"))
		{
			st.setState(2);
			st.set("cond", "1");
			htmltext = "30895-02.htm";
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30895-06.htm"))
		{
			if(st.getQuestItemsCount(5864) > 9L)
				htmltext = "30895-07.htm";
		}
		else if(event.equalsIgnoreCase("30895-05.htm"))
		{
			if(amount > 0L)
				if(amount > 99L)
				{
					st.giveItems(57, amount * 300L);
					st.takeItems(5863, -1L);
					st.playSound(Quest.SOUND_ITEMGET);
					final int random = Rnd.get(RANDOM_REWARDS.length);
					st.giveItems(RANDOM_REWARDS[random][0], RANDOM_REWARDS[random][1]);
					htmltext = "30895-05b.htm";
				}
				else
				{
					st.giveItems(57, amount * 100L);
					st.takeItems(5863, -1L);
					st.playSound(Quest.SOUND_ITEMGET);
					htmltext = "30895-05a.htm";
				}
		}
		else if(event.equalsIgnoreCase("30895-08.htm"))
		{
			st.giveItems(5915, 1L);
			st.takeItems(5864, -1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		else if(event.equalsIgnoreCase("30895-09.htm"))
		{
			st.exitCurrentQuest(true);
			st.playSound(Quest.SOUND_FINISH);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond < 1)
		{
			if(st.getPlayer().getLevel() < 38)
				htmltext = "30895-00.htm";
			else
				htmltext = "30895-01.htm";
		}
		else
			htmltext = "30895-03.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(Rnd.chance(35))
		{
			st.giveItems(5863, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		if(Rnd.chance(10) && st.getQuestItemsCount(5864) < 10L)
		{
			st.giveItems(5864, 1L);
			if(st.getQuestItemsCount(5864) < 10L)
				st.playSound(Quest.SOUND_ITEMGET);
			else
				st.playSound(Quest.SOUND_MIDDLE);
		}
		return null;
	}
}
