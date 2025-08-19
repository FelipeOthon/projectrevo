package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _007_ATripBegins extends Quest implements ScriptFile
{
	int MIRABEL;
	int ARIEL;
	int ASTERIOS;
	int ARIELS_RECOMMENDATION;
	int SCROLL_OF_ESCAPE_GIRAN;
	int MARK_OF_TRAVELER;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _007_ATripBegins()
	{
		super(false);
		MIRABEL = 30146;
		ARIEL = 30148;
		ASTERIOS = 30154;
		ARIELS_RECOMMENDATION = 7572;
		SCROLL_OF_ESCAPE_GIRAN = 7126;
		MARK_OF_TRAVELER = 7570;
		this.addStartNpc(MIRABEL);
		this.addTalkId(new int[] { MIRABEL });
		this.addTalkId(new int[] { ARIEL });
		this.addTalkId(new int[] { ASTERIOS });
		addQuestItem(new int[] { ARIELS_RECOMMENDATION });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("mint_q0007_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("ariel_q0007_0201.htm"))
		{
			st.giveItems(ARIELS_RECOMMENDATION, 1L);
			st.set("cond", "2");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("ozzy_q0007_0301.htm"))
		{
			st.takeItems(ARIELS_RECOMMENDATION, -1L);
			st.set("cond", "3");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("mint_q0007_0401.htm"))
		{
			st.giveItems(SCROLL_OF_ESCAPE_GIRAN, 1L);
			st.giveItems(MARK_OF_TRAVELER, 1L);
			st.set("cond", "0");
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == MIRABEL)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getRace() == Race.elf && st.getPlayer().getLevel() >= 3)
					htmltext = "mint_q0007_0101.htm";
				else
				{
					htmltext = "mint_q0007_0102.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "mint_q0007_0105.htm";
			else if(cond == 3)
				htmltext = "mint_q0007_0301.htm";
		}
		else if(npcId == ARIEL)
		{
			if(cond == 1 && st.getQuestItemsCount(ARIELS_RECOMMENDATION) == 0L)
				htmltext = "ariel_q0007_0101.htm";
			else if(cond == 2)
				htmltext = "ariel_q0007_0202.htm";
		}
		else if(npcId == ASTERIOS)
			if(cond == 2 && st.getQuestItemsCount(ARIELS_RECOMMENDATION) > 0L)
				htmltext = "ozzy_q0007_0201.htm";
			else if(cond == 2 && st.getQuestItemsCount(ARIELS_RECOMMENDATION) == 0L)
				htmltext = "ozzy_q0007_0302.htm";
			else if(cond == 3)
				htmltext = "ozzy_q0007_0303.htm";
		return htmltext;
	}
}
