package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _297_GateKeepersFavor extends Quest implements ScriptFile
{
	private static final int STARSTONE = 1573;
	private static final int GATEKEEPER_TOKEN = 1659;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _297_GateKeepersFavor()
	{
		super(false);
		this.addStartNpc(30540);
		this.addTalkId(new int[] { 30540 });
		this.addKillId(new int[] { 20521 });
		addQuestItem(new int[] { 1573 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("gatekeeper_wirphy_q0297_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30540)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 15)
					htmltext = "gatekeeper_wirphy_q0297_02.htm";
				else
					htmltext = "gatekeeper_wirphy_q0297_01.htm";
			}
			else if(cond == 1 && st.getQuestItemsCount(1573) < 20L)
				htmltext = "gatekeeper_wirphy_q0297_04.htm";
			else if(cond == 2 && st.getQuestItemsCount(1573) < 20L)
				htmltext = "gatekeeper_wirphy_q0297_04.htm";
			else if(cond == 2 && st.getQuestItemsCount(1573) >= 20L)
			{
				htmltext = "gatekeeper_wirphy_q0297_05.htm";
				st.takeItems(1573, -1L);
				st.giveItems(1659, 2L);
				st.exitCurrentQuest(true);
				st.playSound(Quest.SOUND_FINISH);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		st.rollAndGive(1573, 1, 1, 20, 33.0);
		if(st.getQuestItemsCount(1573) >= 20L)
			st.set("cond", "2");
		return null;
	}
}
