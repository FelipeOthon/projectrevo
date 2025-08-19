package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _011_SecretMeetingWithKetraOrcs extends Quest implements ScriptFile
{
	int CADMON;
	int LEON;
	int WAHKAN;
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

	public _011_SecretMeetingWithKetraOrcs()
	{
		super(false);
		CADMON = 31296;
		LEON = 31256;
		WAHKAN = 31371;
		MUNITIONS_BOX = 7231;
		this.addStartNpc(CADMON);
		this.addTalkId(new int[] { CADMON });
		this.addTalkId(new int[] { LEON });
		this.addTalkId(new int[] { WAHKAN });
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
		else if(event.equalsIgnoreCase("31256-2.htm"))
		{
			st.giveItems(MUNITIONS_BOX, 1L);
			st.set("cond", "2");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("31371-2.htm"))
		{
			st.takeItems(MUNITIONS_BOX, 1L);
			st.addExpAndSp(22787L, 0L);
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
		else if(npcId == LEON)
		{
			if(cond == 1)
				htmltext = "31256-1.htm";
			else if(cond == 2)
				htmltext = "31256-2r.htm";
		}
		else if(npcId == WAHKAN && cond == 2 && st.getQuestItemsCount(MUNITIONS_BOX) > 0L)
			htmltext = "31371-1.htm";
		return htmltext;
	}
}
