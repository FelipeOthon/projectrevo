package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _317_CatchTheWind extends Quest implements ScriptFile
{
	private static int Rizraell;
	private static int WindShard;
	private static int Lirein;
	private static int LireinElder;
	public final int[][] DROPLIST_COND;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _317_CatchTheWind()
	{
		super(false);
		DROPLIST_COND = new int[][] {
				{ 1, 0, _317_CatchTheWind.Lirein, 0, _317_CatchTheWind.WindShard, 0, 60, 1 },
				{ 1, 0, _317_CatchTheWind.LireinElder, 0, _317_CatchTheWind.WindShard, 0, 60, 1 } };
		this.addStartNpc(_317_CatchTheWind.Rizraell);
		for(int i = 0; i < DROPLIST_COND.length; ++i)
			this.addKillId(new int[] { DROPLIST_COND[i][2] });
		addQuestItem(new int[] { _317_CatchTheWind.WindShard });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("rizraell_q0317_04.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("rizraell_q0317_08.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == _317_CatchTheWind.Rizraell)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 18)
					htmltext = "rizraell_q0317_03.htm";
				else
				{
					htmltext = "rizraell_q0317_02.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
			{
				final long count = st.getQuestItemsCount(_317_CatchTheWind.WindShard);
				if(count > 0L)
				{
					st.takeItems(_317_CatchTheWind.WindShard, -1L);
					st.giveItems(57, 40L * count);
					htmltext = "rizraell_q0317_07.htm";
				}
				else
					htmltext = "rizraell_q0317_05.htm";
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		for(int i = 0; i < DROPLIST_COND.length; ++i)
			if(cond == DROPLIST_COND[i][0] && npcId == DROPLIST_COND[i][2] && (DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(DROPLIST_COND[i][3]) > 0L))
				if(DROPLIST_COND[i][5] == 0)
					st.rollAndGive(DROPLIST_COND[i][4], DROPLIST_COND[i][7], DROPLIST_COND[i][6]);
				else if(st.rollAndGive(DROPLIST_COND[i][4], DROPLIST_COND[i][7], DROPLIST_COND[i][7], DROPLIST_COND[i][5], DROPLIST_COND[i][6]) && DROPLIST_COND[i][1] != cond && DROPLIST_COND[i][1] != 0)
				{
					st.set("cond", String.valueOf(DROPLIST_COND[i][1]));
					st.setState(2);
				}
		return null;
	}

	static
	{
		_317_CatchTheWind.Rizraell = 30361;
		_317_CatchTheWind.WindShard = 1078;
		_317_CatchTheWind.Lirein = 20036;
		_317_CatchTheWind.LireinElder = 20044;
	}
}
