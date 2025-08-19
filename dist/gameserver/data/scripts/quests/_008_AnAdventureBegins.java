package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _008_AnAdventureBegins extends Quest implements ScriptFile
{
	int JASMINE;
	int ROSELYN;
	int HARNE;
	int ROSELYNS_NOTE;
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

	public _008_AnAdventureBegins()
	{
		super(false);
		JASMINE = 30134;
		ROSELYN = 30355;
		HARNE = 30144;
		ROSELYNS_NOTE = 7573;
		SCROLL_OF_ESCAPE_GIRAN = 7126;
		MARK_OF_TRAVELER = 7570;
		this.addStartNpc(JASMINE);
		this.addTalkId(new int[] { JASMINE });
		this.addTalkId(new int[] { ROSELYN });
		this.addTalkId(new int[] { HARNE });
		addQuestItem(new int[] { ROSELYNS_NOTE });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("jasmine_q0008_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("sentry_roseline_q0008_0201.htm"))
		{
			st.giveItems(ROSELYNS_NOTE, 1L);
			st.set("cond", "2");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("harne_q0008_0301.htm"))
		{
			st.takeItems(ROSELYNS_NOTE, -1L);
			st.set("cond", "3");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("jasmine_q0008_0401.htm"))
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
		if(npcId == JASMINE)
		{
			if(cond == 0 && st.getPlayer().getRace() == Race.darkelf)
			{
				if(st.getPlayer().getLevel() >= 3)
					htmltext = "jasmine_q0008_0101.htm";
				else
				{
					htmltext = "jasmine_q0008_0102.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "jasmine_q0008_0105.htm";
			else if(cond == 3)
				htmltext = "jasmine_q0008_0301.htm";
		}
		else if(npcId == ROSELYN)
		{
			if(st.getQuestItemsCount(ROSELYNS_NOTE) == 0L)
				htmltext = "sentry_roseline_q0008_0101.htm";
			else
				htmltext = "sentry_roseline_q0008_0202.htm";
		}
		else if(npcId == HARNE)
			if(cond == 2 && st.getQuestItemsCount(ROSELYNS_NOTE) > 0L)
				htmltext = "harne_q0008_0201.htm";
			else if(cond == 2 && st.getQuestItemsCount(ROSELYNS_NOTE) == 0L)
				htmltext = "harne_q0008_0302.htm";
			else if(cond == 3)
				htmltext = "harne_q0008_0303.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		return null;
	}
}
