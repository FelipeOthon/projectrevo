package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _020_BringUpWithLove extends Quest implements ScriptFile
{
	int TUNATUN;
	int JEWEL_INNOCENCE;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _020_BringUpWithLove()
	{
		super(false);
		TUNATUN = 31537;
		JEWEL_INNOCENCE = 7185;
		this.addStartNpc(TUNATUN);
		addQuestItem(new int[] { JEWEL_INNOCENCE });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int id = st.getState();
		if(id == 1)
		{
			st.set("cond", "0");
			if(event.equals("beast_herder_tunatun_q0020_03.htm") || event.equals("beast_herder_tunatun_q0020_04.htm") || event.equals("beast_herder_tunatun_q0020_06.htm") || event.equals("beast_herder_tunatun_q0020_07.htm") || event.equals("beast_herder_tunatun_q0020_08.htm"))
				return event;
			if(event.equals("beast_herder_tunatun_q0020_09.htm"))
			{
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
				return event;
			}
		}
		else if(event.equals("beast_herder_tunatun_q0020_12.htm"))
		{
			st.takeItems(JEWEL_INNOCENCE, -1L);
			st.giveItems(57, 65200L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int id = st.getState();
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(id == 1)
				if(st.getPlayer().getLevel() <= 64)
				{
					st.exitCurrentQuest(true);
					htmltext = "beast_herder_tunatun_q0020_02.htm";
				}
				else
					htmltext = "beast_herder_tunatun_q0020_01.htm";
		}
		else if(cond == 1 && st.getQuestItemsCount(JEWEL_INNOCENCE) == 0L)
			htmltext = "beast_herder_tunatun_q0020_10.htm";
		else if(cond == 2 && st.getQuestItemsCount(JEWEL_INNOCENCE) >= 1L)
			htmltext = "beast_herder_tunatun_q0020_11.htm";
		return htmltext;
	}
}
