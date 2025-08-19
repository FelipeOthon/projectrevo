package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _016_TheComingDarkness extends Quest implements ScriptFile
{
	public final int HIERARCH = 31517;
	public final int[][] ALTAR_LIST;
	public final int CRYSTAL_OF_SEAL = 7167;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _016_TheComingDarkness()
	{
		super(false);
		ALTAR_LIST = new int[][] { { 31512, 1 }, { 31513, 2 }, { 31514, 3 }, { 31515, 4 }, { 31516, 5 } };
		this.addStartNpc(31517);
		for(final int[] element : ALTAR_LIST)
			this.addTalkId(new int[] { element[0] });
		addQuestItem(new int[] { 7167 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("31517-02.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.giveItems(7167, 5L);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		for(final int[] element : ALTAR_LIST)
			if(event.equalsIgnoreCase(String.valueOf(element[0]) + "-02.htm"))
			{
				st.takeItems(7167, 1L);
				st.set("cond", String.valueOf(element[1] + 1));
				st.playSound(Quest.SOUND_MIDDLE);
			}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 31517)
			if(cond < 1)
			{
				if(st.getPlayer().getLevel() < 61)
				{
					htmltext = "31517-00.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "31517-01.htm";
			}
			else if(cond > 0 && cond < 6 && st.getQuestItemsCount(7167) > 0L)
				htmltext = "31517-02r.htm";
			else if(cond > 0 && cond < 6 && st.getQuestItemsCount(7167) < 1L)
			{
				htmltext = "31517-proeb.htm";
				st.exitCurrentQuest(false);
			}
			else if(cond > 5 && st.getQuestItemsCount(7167) < 1L)
			{
				htmltext = "31517-03.htm";
				st.addExpAndSp(865187L, 69172L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		for(final int[] element : ALTAR_LIST)
			if(npcId == element[0])
				if(cond == element[1])
				{
					if(st.getQuestItemsCount(7167) > 0L)
						htmltext = String.valueOf(element[0]) + "-01.htm";
					else
						htmltext = String.valueOf(element[0]) + "-03.htm";
				}
				else if(cond == element[1] + 1)
					htmltext = String.valueOf(element[0]) + "-04.htm";
		return htmltext;
	}
}
