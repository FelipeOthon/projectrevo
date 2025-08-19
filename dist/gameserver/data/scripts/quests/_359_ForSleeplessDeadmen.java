package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _359_ForSleeplessDeadmen extends Quest implements ScriptFile
{
	private static final int DROP_RATE = 10;
	private static final int REQUIRED = 60;
	private static final int REMAINS = 5869;
	private static final int PhoenixEarrPart = 6341;
	private static final int MajEarrPart = 6342;
	private static final int PhoenixNeclPart = 6343;
	private static final int MajNeclPart = 6344;
	private static final int PhoenixRingPart = 6345;
	private static final int MajRingPart = 6346;
	private static final int DarkCryShieldPart = 5494;
	private static final int NightmareShieldPart = 5495;
	private static final int ORVEN = 30857;
	private static final int DOOMSERVANT = 21006;
	private static final int DOOMGUARD = 21007;
	private static final int DOOMARCHER = 21008;
	private static final int DOOMTROOPER = 21009;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _359_ForSleeplessDeadmen()
	{
		super(false);
		this.addStartNpc(30857);
		this.addKillId(new int[] { 21006 });
		this.addKillId(new int[] { 21007 });
		this.addKillId(new int[] { 21008 });
		this.addKillId(new int[] { 21009 });
		addQuestItem(new int[] { 5869 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30857-06.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30857-07.htm"))
		{
			st.exitCurrentQuest(true);
			st.playSound(Quest.SOUND_FINISH);
		}
		else if(event.equalsIgnoreCase("30857-08.htm"))
		{
			st.set("cond", "1");
			final int chance = Rnd.get(100);
			int item;
			if(chance <= 16)
				item = 6343;
			else if(chance <= 33)
				item = 6341;
			else if(chance <= 50)
				item = 6345;
			else if(chance <= 58)
				item = 6344;
			else if(chance <= 67)
				item = 6342;
			else if(chance <= 76)
				item = 6346;
			else if(chance <= 84)
				item = 5494;
			else
				item = 5495;
			st.giveItems(item, 4L, true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int id = st.getState();
		final int cond = st.getInt("cond");
		if(id == 1)
		{
			if(st.getPlayer().getLevel() < 60)
			{
				st.exitCurrentQuest(true);
				htmltext = "30857-01.htm";
			}
			else
				htmltext = "30857-02.htm";
		}
		else if(id == 2)
		{
			if(cond == 3)
				htmltext = "30857-03.htm";
			else if(cond == 2 && st.getQuestItemsCount(5869) >= 60L)
			{
				st.takeItems(5869, 60L);
				st.set("cond", "3");
				htmltext = "30857-04.htm";
			}
			else
				htmltext = "30857-09.htm";
		}
		else
			htmltext = "30857-05.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(5869) < 60L && Rnd.chance(10))
		{
			st.giveItems(5869, (long) Config.RATE_QUESTS_DROP);
			if(st.getQuestItemsCount(5869) >= 60L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
