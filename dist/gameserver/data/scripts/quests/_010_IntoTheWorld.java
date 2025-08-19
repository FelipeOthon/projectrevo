package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _010_IntoTheWorld extends Quest implements ScriptFile
{
	int VERY_EXPENSIVE_NECKLACE;
	int SCROLL_OF_ESCAPE_GIRAN;
	int MARK_OF_TRAVELER;
	int BALANKI;
	int REED;
	int GERALD;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _010_IntoTheWorld()
	{
		super(false);
		VERY_EXPENSIVE_NECKLACE = 7574;
		SCROLL_OF_ESCAPE_GIRAN = 7126;
		MARK_OF_TRAVELER = 7570;
		BALANKI = 30533;
		REED = 30520;
		GERALD = 30650;
		this.addStartNpc(BALANKI);
		this.addTalkId(new int[] { BALANKI });
		this.addTalkId(new int[] { REED });
		this.addTalkId(new int[] { GERALD });
		addQuestItem(new int[] { VERY_EXPENSIVE_NECKLACE });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("elder_balanki_q0010_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("warehouse_chief_reed_q0010_0201.htm"))
		{
			st.giveItems(VERY_EXPENSIVE_NECKLACE, 1L);
			st.set("cond", "2");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("gerald_priest_of_earth_q0010_0301.htm"))
		{
			st.takeItems(VERY_EXPENSIVE_NECKLACE, -1L);
			st.set("cond", "3");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("warehouse_chief_reed_q0010_0401.htm"))
		{
			st.set("cond", "4");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("elder_balanki_q0010_0501.htm"))
		{
			st.giveItems(SCROLL_OF_ESCAPE_GIRAN, 1L);
			st.giveItems(MARK_OF_TRAVELER, 1L);
			st.exitCurrentQuest(false);
			st.playSound(Quest.SOUND_FINISH);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == BALANKI)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getRace() == Race.dwarf && st.getPlayer().getLevel() >= 3)
					htmltext = "elder_balanki_q0010_0101.htm";
				else
				{
					htmltext = "elder_balanki_q0010_0102.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "elder_balanki_q0010_0105.htm";
			else if(cond == 4)
				htmltext = "elder_balanki_q0010_0401.htm";
		}
		else if(npcId == REED)
		{
			if(cond == 1)
				htmltext = "warehouse_chief_reed_q0010_0101.htm";
			else if(cond == 2)
				htmltext = "warehouse_chief_reed_q0010_0202.htm";
			else if(cond == 3)
				htmltext = "warehouse_chief_reed_q0010_0301.htm";
			else if(cond == 4)
				htmltext = "warehouse_chief_reed_q0010_0402.htm";
		}
		else if(npcId == GERALD)
			if(cond == 2 && st.getQuestItemsCount(VERY_EXPENSIVE_NECKLACE) > 0L)
				htmltext = "gerald_priest_of_earth_q0010_0201.htm";
			else if(cond == 3)
				htmltext = "gerald_priest_of_earth_q0010_0302.htm";
			else
				htmltext = "gerald_priest_of_earth_q0010_0303.htm";
		return htmltext;
	}
}
