package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _014_WhereaboutsoftheArchaeologist extends Quest implements ScriptFile
{
	private static final int LETTER_TO_ARCHAEOLOGIST = 7253;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _014_WhereaboutsoftheArchaeologist()
	{
		super(false);
		this.addStartNpc(31263);
		this.addTalkId(new int[] { 31263 });
		this.addTalkId(new int[] { 31538 });
		addQuestItem(new int[] { 7253 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("31263-2.htm"))
		{
			st.set("cond", "1");
			st.giveItems(7253, 1L);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("31538-2.htm"))
		{
			st.takeItems(7253, -1L);
			st.giveItems(57, 113228L);
			st.set("cond", "0");
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
			return "31538-2.htm";
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 31263)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 74)
					htmltext = "31263-1.htm";
				else
				{
					htmltext = "31263-00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "31263-2.htm";
		}
		else if(npcId == 31538 && cond == 1 && st.getQuestItemsCount(7253) == 1L)
			htmltext = "31538-1.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		return null;
	}
}
