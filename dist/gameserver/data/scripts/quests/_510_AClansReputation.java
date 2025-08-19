package quests;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class _510_AClansReputation extends Quest implements ScriptFile
{
	private static final int VALDIS = 31331;
	private static final int CLAW = 8767;
	private static final int CLAN_POINTS_REWARD = 50;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _510_AClansReputation()
	{
		super(2);
		this.addStartNpc(31331);
		for(int npc = 22215; npc <= 22217; ++npc)
			this.addKillId(new int[] { npc });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int cond = st.getInt("cond");
		if(event.equals("31331-3.htm"))
		{
			if(cond == 0)
			{
				st.set("cond", "1");
				st.setState(2);
			}
		}
		else if(event.equals("31331-6.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "<html><body>You are either not on a quest that involves this NPC, or you don't meet this NPC's minimum quest requirements.</body></html>";
		final Player player = st.getPlayer();
		final Clan clan = player.getClan();
		if(player.getClan() == null || !player.isClanLeader())
		{
			st.exitCurrentQuest(true);
			htmltext = "31331-0.htm";
		}
		else if(player.getClan().getLevel() < 5)
		{
			st.exitCurrentQuest(true);
			htmltext = "31331-0.htm";
		}
		else
		{
			final int cond = st.getInt("cond");
			final int id = st.getState();
			if(id == 1 && cond == 0)
				htmltext = "31331-1.htm";
			else if(id == 2 && cond == 1)
			{
				final long count = st.getQuestItemsCount(8767);
				if(count == 0L)
					htmltext = "31331-4.htm";
				else if(count >= 1L)
				{
					htmltext = "31331-7.htm";
					st.takeItems(8767, -1L);
					player.sendPacket(new SystemMessage(1777).addNumber(clan.incReputation(50 * (int) count, true, "_510_AClansReputation")));
				}
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(!st.getPlayer().isClanLeader())
			st.exitCurrentQuest(true);
		else if(st.getState() == 2)
		{
			final int npcId = npc.getNpcId();
			if(npcId >= 22215 && npcId <= 22218)
			{
				st.giveItems(8767, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		return null;
	}
}
