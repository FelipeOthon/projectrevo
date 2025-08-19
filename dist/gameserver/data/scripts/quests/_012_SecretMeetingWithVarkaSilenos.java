package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _012_SecretMeetingWithVarkaSilenos extends Quest implements ScriptFile
{
	int CADMON;
	int HELMUT;
	int NARAN_ASHANUK;
	int MUNITIONS_BOX;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _012_SecretMeetingWithVarkaSilenos()
	{
		super(false);
		CADMON = 31296;
		HELMUT = 31258;
		NARAN_ASHANUK = 31378;
		MUNITIONS_BOX = 7232;
		this.addStartNpc(CADMON);
		this.addTalkId(new int[] { CADMON });
		this.addTalkId(new int[] { HELMUT });
		this.addTalkId(new int[] { NARAN_ASHANUK });
		addQuestItem(new int[] { MUNITIONS_BOX });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("31296-2.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("31258-2.htm"))
		{
			st.giveItems(MUNITIONS_BOX, 1L);
			st.set("cond", "2");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("31378-2.htm"))
		{
			st.takeItems(MUNITIONS_BOX, 1L);
			st.addExpAndSp(79761L, 0L);
			st.unset("cond");
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
		if(npcId == CADMON)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 74)
					htmltext = "31296-1.htm";
				else
				{
					htmltext = "31296-00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "31296-2r.htm";
		}
		else if(npcId == HELMUT)
		{
			if(cond == 1)
				htmltext = "31258-1.htm";
			else if(cond == 2)
				htmltext = "31258-2r.htm";
		}
		else if(npcId == NARAN_ASHANUK && cond == 2 && st.getQuestItemsCount(MUNITIONS_BOX) > 0L)
			htmltext = "31378-1.htm";
		return htmltext;
	}
}
