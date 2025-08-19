package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _031_SecretBuriedInTheSwamp extends Quest implements ScriptFile
{
	int ABERCROMBIE;
	int FORGOTTEN_MONUMENT_1;
	int FORGOTTEN_MONUMENT_2;
	int FORGOTTEN_MONUMENT_3;
	int FORGOTTEN_MONUMENT_4;
	int CORPSE_OF_DWARF;
	int KRORINS_JOURNAL;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _031_SecretBuriedInTheSwamp()
	{
		super(false);
		ABERCROMBIE = 31555;
		FORGOTTEN_MONUMENT_1 = 31661;
		FORGOTTEN_MONUMENT_2 = 31662;
		FORGOTTEN_MONUMENT_3 = 31663;
		FORGOTTEN_MONUMENT_4 = 31664;
		CORPSE_OF_DWARF = 31665;
		KRORINS_JOURNAL = 7252;
		this.addStartNpc(ABERCROMBIE);
		for(int i = 31661; i <= 31665; ++i)
			this.addTalkId(new int[] { i });
		addQuestItem(new int[] { KRORINS_JOURNAL });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int cond = st.getInt("cond");
		if(event.equals("31555-1.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("31665-1.htm") && cond == 1)
		{
			st.set("cond", "2");
			st.playSound(Quest.SOUND_ITEMGET);
			st.giveItems(KRORINS_JOURNAL, 1L);
		}
		else if(event.equals("31555-4.htm") && cond == 2)
			st.set("cond", "3");
		else if(event.equals("31661-1.htm") && cond == 3)
			st.set("cond", "4");
		else if(event.equals("31662-1.htm") && cond == 4)
			st.set("cond", "5");
		else if(event.equals("31663-1.htm") && cond == 5)
			st.set("cond", "6");
		else if(event.equals("31664-1.htm") && cond == 6)
		{
			st.set("cond", "7");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equals("31555-7.htm") && cond == 7)
		{
			st.takeItems(KRORINS_JOURNAL, -1L);
			st.addExpAndSp(130000L, 0L);
			st.giveItems(57, 40000L);
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
		if(npcId == ABERCROMBIE)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 66)
					htmltext = "31555-0.htm";
				else
				{
					htmltext = "31555-0a.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "31555-2.htm";
			else if(cond == 2)
				htmltext = "31555-3.htm";
			else if(cond == 3)
				htmltext = "31555-5.htm";
			else if(cond == 7)
				htmltext = "31555-6.htm";
		}
		else if(npcId == CORPSE_OF_DWARF)
		{
			if(cond == 1)
				htmltext = "31665-0.htm";
			else if(cond == 2)
				htmltext = "31665-2.htm";
		}
		else if(npcId == FORGOTTEN_MONUMENT_1)
		{
			if(cond == 3)
				htmltext = "31661-0.htm";
			else if(cond > 3)
				htmltext = "31661-2.htm";
		}
		else if(npcId == FORGOTTEN_MONUMENT_2)
		{
			if(cond == 4)
				htmltext = "31662-0.htm";
			else if(cond > 4)
				htmltext = "31662-2.htm";
		}
		else if(npcId == FORGOTTEN_MONUMENT_3)
		{
			if(cond == 5)
				htmltext = "31663-0.htm";
			else if(cond > 5)
				htmltext = "31663-2.htm";
		}
		else if(npcId == FORGOTTEN_MONUMENT_4)
			if(cond == 6)
				htmltext = "31664-0.htm";
			else if(cond > 6)
				htmltext = "31664-2.htm";
		return htmltext;
	}
}
