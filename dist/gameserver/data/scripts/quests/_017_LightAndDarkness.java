package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _017_LightAndDarkness extends Quest implements ScriptFile
{
	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _017_LightAndDarkness()
	{
		super(false);
		this.addStartNpc(31517);
		this.addTalkId(new int[] { 31508 });
		this.addTalkId(new int[] { 31509 });
		this.addTalkId(new int[] { 31510 });
		this.addTalkId(new int[] { 31511 });
		addQuestItem(new int[] { 7168 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("dark_presbyter_q0017_04.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.giveItems(7168, 4L);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("blessed_altar1_q0017_02.htm"))
		{
			st.takeItems(7168, 1L);
			st.set("cond", "2");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equals("blessed_altar2_q0017_02.htm"))
		{
			st.takeItems(7168, 1L);
			st.set("cond", "3");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equals("blessed_altar3_q0017_02.htm"))
		{
			st.takeItems(7168, 1L);
			st.set("cond", "4");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equals("blessed_altar4_q0017_02.htm"))
		{
			st.takeItems(7168, 1L);
			st.set("cond", "5");
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
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 61)
					htmltext = "dark_presbyter_q0017_01.htm";
				else
				{
					htmltext = "dark_presbyter_q0017_03.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond > 0 && cond < 5 && st.getQuestItemsCount(7168) > 0L)
				htmltext = "dark_presbyter_q0017_05.htm";
			else if(cond > 0 && cond < 5 && st.getQuestItemsCount(7168) == 0L)
			{
				htmltext = "dark_presbyter_q0017_06.htm";
				st.set("cond", "0");
				st.exitCurrentQuest(false);
			}
			else if(cond == 5 && st.getQuestItemsCount(7168) == 0L)
			{
				htmltext = "dark_presbyter_q0017_07.htm";
				st.addExpAndSp(697040L, 54887L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 31508)
		{
			if(cond == 1)
			{
				if(st.getQuestItemsCount(7168) != 0L)
					htmltext = "blessed_altar1_q0017_01.htm";
				else
					htmltext = "blessed_altar1_q0017_03.htm";
			}
			else if(cond == 2)
				htmltext = "blessed_altar1_q0017_05.htm";
		}
		else if(npcId == 31509)
		{
			if(cond == 2)
			{
				if(st.getQuestItemsCount(7168) != 0L)
					htmltext = "blessed_altar2_q0017_01.htm";
				else
					htmltext = "blessed_altar2_q0017_03.htm";
			}
			else if(cond == 3)
				htmltext = "blessed_altar2_q0017_05.htm";
		}
		else if(npcId == 31510)
		{
			if(cond == 3)
			{
				if(st.getQuestItemsCount(7168) != 0L)
					htmltext = "blessed_altar3_q0017_01.htm";
				else
					htmltext = "blessed_altar3_q0017_03.htm";
			}
			else if(cond == 4)
				htmltext = "blessed_altar3_q0017_05.htm";
		}
		else if(npcId == 31511)
			if(cond == 4)
			{
				if(st.getQuestItemsCount(7168) != 0L)
					htmltext = "blessed_altar4_q0017_01.htm";
				else
					htmltext = "blessed_altar4_q0017_03.htm";
			}
			else if(cond == 5)
				htmltext = "blessed_altar4_q0017_05.htm";
		return htmltext;
	}
}
