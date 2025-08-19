package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _649_ALooterandaRailroadMan extends Quest implements ScriptFile
{
	private static final int OBI = 32052;
	private static final int THIEF_GUILD_MARK = 8099;
	private static final int[][] DROPLIST_COND;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _649_ALooterandaRailroadMan()
	{
		super(true);
		this.addStartNpc(32052);
		for(int i = 0; i < _649_ALooterandaRailroadMan.DROPLIST_COND.length; ++i)
			this.addKillId(new int[] { _649_ALooterandaRailroadMan.DROPLIST_COND[i][2] });
		addQuestItem(new int[] { 8099 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "railman_obi_q0649_0103.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("649_3"))
			if(st.getQuestItemsCount(8099) == 200L)
			{
				htmltext = "railman_obi_q0649_0201.htm";
				st.takeItems(8099, -1L);
				st.giveItems(57, 21698L, true);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
			{
				st.set("cond", "1");
				htmltext = "railman_obi_q0649_0202.htm";
			}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		int cond = 0;
		if(id != 1)
			cond = st.getInt("cond");
		if(npcId == 32052)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 30)
				{
					htmltext = "railman_obi_q0649_0102.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "railman_obi_q0649_0101.htm";
			}
			else if(cond == 1)
				htmltext = "railman_obi_q0649_0106.htm";
			else if(cond == 2 && st.getQuestItemsCount(8099) == 200L)
				htmltext = "railman_obi_q0649_0105.htm";
			else
			{
				htmltext = "railman_obi_q0649_0106.htm";
				st.set("cond", "1");
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		for(int i = 0; i < _649_ALooterandaRailroadMan.DROPLIST_COND.length; ++i)
			if(cond == _649_ALooterandaRailroadMan.DROPLIST_COND[i][0] && npcId == _649_ALooterandaRailroadMan.DROPLIST_COND[i][2] && (_649_ALooterandaRailroadMan.DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(_649_ALooterandaRailroadMan.DROPLIST_COND[i][3]) > 0L))
				if(_649_ALooterandaRailroadMan.DROPLIST_COND[i][5] == 0)
					st.rollAndGive(_649_ALooterandaRailroadMan.DROPLIST_COND[i][4], _649_ALooterandaRailroadMan.DROPLIST_COND[i][7], _649_ALooterandaRailroadMan.DROPLIST_COND[i][6]);
				else if(st.rollAndGive(_649_ALooterandaRailroadMan.DROPLIST_COND[i][4], _649_ALooterandaRailroadMan.DROPLIST_COND[i][7], _649_ALooterandaRailroadMan.DROPLIST_COND[i][7], _649_ALooterandaRailroadMan.DROPLIST_COND[i][5], _649_ALooterandaRailroadMan.DROPLIST_COND[i][6]) && _649_ALooterandaRailroadMan.DROPLIST_COND[i][1] != cond && _649_ALooterandaRailroadMan.DROPLIST_COND[i][1] != 0)
				{
					st.set("cond", String.valueOf(_649_ALooterandaRailroadMan.DROPLIST_COND[i][1]));
					st.setState(2);
				}
		return null;
	}

	static
	{
		DROPLIST_COND = new int[][] {
				{ 1, 2, 22017, 0, 8099, 200, 50, 1 },
				{ 1, 2, 22018, 0, 8099, 200, 50, 1 },
				{ 1, 2, 22019, 0, 8099, 200, 50, 1 },
				{ 1, 2, 22021, 0, 8099, 200, 50, 1 },
				{ 1, 2, 22022, 0, 8099, 200, 50, 1 },
				{ 1, 2, 22023, 0, 8099, 200, 50, 1 },
				{ 1, 2, 22024, 0, 8099, 200, 50, 1 },
				{ 1, 2, 22026, 0, 8099, 200, 50, 1 } };
	}
}
