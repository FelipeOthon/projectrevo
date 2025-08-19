package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _631_DeliciousTopChoiceMeat extends Quest implements ScriptFile
{
	public final int TUNATUN = 31537;
	public final int[] MOB_LIST;
	public final int TOP_QUALITY_MEAT = 7546;
	public final int MOLD_GLUE = 4039;
	public final int MOLD_LUBRICANT = 4040;
	public final int MOLD_HARDENER = 4041;
	public final int ENRIA = 4042;
	public final int ASOFE = 4043;
	public final int THONS = 4044;
	public final int[][] REWARDS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _631_DeliciousTopChoiceMeat()
	{
		super(false);
		MOB_LIST = new int[] {
				21460,
				21461,
				21462,
				21463,
				21464,
				21465,
				21466,
				21467,
				21468,
				21469,
				21479,
				21480,
				21481,
				21482,
				21483,
				21484,
				21485,
				21486,
				21487,
				21488,
				21498,
				21499,
				21500,
				21501,
				21502,
				21503,
				21504,
				21505,
				21506,
				21507 };
		REWARDS = new int[][] { { 1, 4039, 15 }, { 2, 4043, 15 }, { 3, 4044, 15 }, { 4, 4040, 10 }, { 5, 4042, 10 }, { 6, 4041, 5 } };
		this.addStartNpc(31537);
		this.addTalkId(new int[] { 31537 });
		for(final int i : MOB_LIST)
			this.addKillId(new int[] { i });
		addQuestItem(new int[] { 7546 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("31537-03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("31537-05.htm") && st.getQuestItemsCount(7546) >= 120L)
			st.set("cond", "3");
		for(final int[] element : REWARDS)
			if(event.equalsIgnoreCase(String.valueOf(element[0])))
				if(st.getInt("cond") == 3 && st.getQuestItemsCount(7546) >= 120L)
				{
					htmltext = "31537-06.htm";
					st.takeItems(7546, -1L);
					st.giveItems(element[1], element[2] * (int) st.getRateQuestsReward());
					st.playSound(Quest.SOUND_FINISH);
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "31537-07.htm";
					st.set("cond", "1");
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
			if(st.getPlayer().getLevel() < 65)
			{
				htmltext = "31537-02.htm";
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "31537-01.htm";
		}
		else if(cond == 1)
			htmltext = "31537-01a.htm";
		else if(cond == 2)
		{
			if(st.getQuestItemsCount(7546) < 120L)
			{
				htmltext = "31537-01a.htm";
				st.set("cond", "1");
			}
			else
				htmltext = "31537-04.htm";
		}
		else if(cond == 3)
			htmltext = "31537-05.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(st.getInt("cond") == 1)
			for(final int i : MOB_LIST)
				if(npcId == i && Rnd.chance(80))
				{
					st.giveItems(7546, (long) st.getRateQuestsDrop(false));
					if(st.getQuestItemsCount(7546) < 120L)
						st.playSound(Quest.SOUND_ITEMGET);
					else
					{
						st.playSound(Quest.SOUND_MIDDLE);
						st.set("cond", "2");
					}
				}
		return null;
	}
}
