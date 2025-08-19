package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _015_SweetWhispers extends Quest implements ScriptFile
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

	public _015_SweetWhispers()
	{
		super(false);
		this.addStartNpc(31302);
		this.addTalkId(new int[] { 31517 });
		this.addTalkId(new int[] { 31518 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("trader_vladimir_q0015_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("dark_necromancer_q0015_0201.htm"))
			st.set("cond", "2");
		else if(event.equalsIgnoreCase("dark_presbyter_q0015_0301.htm"))
		{
			st.addExpAndSp(350531L, 28204L);
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
		if(npcId == 31302)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 60)
					htmltext = "trader_vladimir_q0015_0101.htm";
				else
				{
					htmltext = "trader_vladimir_q0015_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond >= 1)
				htmltext = "trader_vladimir_q0015_0105.htm";
		}
		else if(npcId == 31518)
		{
			if(cond == 1)
				htmltext = "dark_necromancer_q0015_0101.htm";
			else if(cond == 2)
				htmltext = "dark_necromancer_q0015_0202.htm";
		}
		else if(npcId == 31517 && cond == 2)
			htmltext = "dark_presbyter_q0015_0201.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		return null;
	}
}
