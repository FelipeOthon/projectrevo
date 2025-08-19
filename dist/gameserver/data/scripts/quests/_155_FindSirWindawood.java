package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _155_FindSirWindawood extends Quest implements ScriptFile
{
	int OFFICIAL_LETTER;
	int HASTE_POTION;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _155_FindSirWindawood()
	{
		super(false);
		OFFICIAL_LETTER = 1019;
		HASTE_POTION = 734;
		this.addStartNpc(30042);
		this.addTalkId(new int[] { 30042 });
		this.addTalkId(new int[] { 30311 });
		addQuestItem(new int[] { OFFICIAL_LETTER });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("quest_accept"))
		{
			htmltext = "abel_q0155_04.htm";
			st.giveItems(OFFICIAL_LETTER, 1L);
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30042)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 3)
				{
					htmltext = "abel_q0155_03.htm";
					return htmltext;
				}
				htmltext = "abel_q0155_02.htm";
				st.exitCurrentQuest(true);
			}
			else if(cond == 1 && st.getQuestItemsCount(OFFICIAL_LETTER) == 1L)
				htmltext = "abel_q0155_05.htm";
		}
		else if(npcId == 30311 && cond == 1 && st.getQuestItemsCount(OFFICIAL_LETTER) == 1L)
		{
			htmltext = "sir_collin_windawood_q0155_01.htm";
			st.takeItems(OFFICIAL_LETTER, -1L);
			st.giveItems(HASTE_POTION, 1L);
			st.set("cond", "0");
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}
}
