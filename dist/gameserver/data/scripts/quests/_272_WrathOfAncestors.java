package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _272_WrathOfAncestors extends Quest implements ScriptFile
{
	private static final int Livina = 30572;
	private static final int GraveRobbersHead = 1474;
	private static final int GoblinGraveRobber = 20319;
	private static final int GoblinTombRaiderLeader = 20320;
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

	public _272_WrathOfAncestors()
	{
		super(false);
		this.addStartNpc(30572);
		for(int i = 0; i < _272_WrathOfAncestors.DROPLIST_COND.length; ++i)
			this.addKillId(new int[] { _272_WrathOfAncestors.DROPLIST_COND[i][2] });
		addQuestItem(new int[] { 1474 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("1"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			htmltext = "seer_livina_q0272_03.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30572)
			if(cond == 0)
			{
				if(st.getPlayer().getRace() != Race.orc)
				{
					htmltext = "seer_livina_q0272_00.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					if(st.getPlayer().getLevel() >= 5)
					{
						htmltext = "seer_livina_q0272_02.htm";
						return htmltext;
					}
					htmltext = "seer_livina_q0272_01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "seer_livina_q0272_04.htm";
			else if(cond == 2)
			{
				st.takeItems(1474, -1L);
				st.giveItems(57, 1500L);
				htmltext = "seer_livina_q0272_05.htm";
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		for(int i = 0; i < _272_WrathOfAncestors.DROPLIST_COND.length; ++i)
			if(cond == _272_WrathOfAncestors.DROPLIST_COND[i][0] && npcId == _272_WrathOfAncestors.DROPLIST_COND[i][2] && (_272_WrathOfAncestors.DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(_272_WrathOfAncestors.DROPLIST_COND[i][3]) > 0L))
				if(_272_WrathOfAncestors.DROPLIST_COND[i][5] == 0)
					st.rollAndGive(_272_WrathOfAncestors.DROPLIST_COND[i][4], _272_WrathOfAncestors.DROPLIST_COND[i][7], _272_WrathOfAncestors.DROPLIST_COND[i][6]);
				else if(st.rollAndGive(_272_WrathOfAncestors.DROPLIST_COND[i][4], _272_WrathOfAncestors.DROPLIST_COND[i][7], _272_WrathOfAncestors.DROPLIST_COND[i][7], _272_WrathOfAncestors.DROPLIST_COND[i][5], _272_WrathOfAncestors.DROPLIST_COND[i][6]) && _272_WrathOfAncestors.DROPLIST_COND[i][1] != cond && _272_WrathOfAncestors.DROPLIST_COND[i][1] != 0)
				{
					st.set("cond", String.valueOf(_272_WrathOfAncestors.DROPLIST_COND[i][1]));
					st.setState(2);
				}
		return null;
	}

	static
	{
		DROPLIST_COND = new int[][] { { 1, 2, 20319, 0, 1474, 50, 100, 1 }, { 1, 2, 20320, 0, 1474, 50, 100, 1 } };
	}
}
