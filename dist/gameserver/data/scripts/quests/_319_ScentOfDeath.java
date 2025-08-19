package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _319_ScentOfDeath extends Quest implements ScriptFile
{
	private static final int MINALESS = 30138;
	private static final int HealingPotion = 1060;
	private static final int ZombieSkin = 1045;
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

	public _319_ScentOfDeath()
	{
		super(false);
		this.addStartNpc(30138);
		this.addTalkId(new int[] { 30138 });
		for(int i = 0; i < _319_ScentOfDeath.DROPLIST_COND.length; ++i)
			this.addKillId(new int[] { _319_ScentOfDeath.DROPLIST_COND[i][2] });
		addQuestItem(new int[] { 1045 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("mina_q0319_04.htm"))
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
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		int cond = 0;
		if(id != 1)
			cond = st.getInt("cond");
		if(npcId == 30138)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 11)
				{
					htmltext = "mina_q0319_02.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "mina_q0319_03.htm";
			}
			else if(cond == 1)
				htmltext = "mina_q0319_05.htm";
			else if(cond == 2 && st.getQuestItemsCount(1045) >= 5L)
			{
				htmltext = "mina_q0319_06.htm";
				st.takeItems(1045, -1L);
				st.giveItems(57, 3350L);
				st.giveItems(1060, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "mina_q0319_05.htm";
				st.set("cond", "1");
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		for(int i = 0; i < _319_ScentOfDeath.DROPLIST_COND.length; ++i)
			if(cond == _319_ScentOfDeath.DROPLIST_COND[i][0] && npcId == _319_ScentOfDeath.DROPLIST_COND[i][2] && (_319_ScentOfDeath.DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(_319_ScentOfDeath.DROPLIST_COND[i][3]) > 0L))
				if(_319_ScentOfDeath.DROPLIST_COND[i][5] == 0)
					st.rollAndGive(_319_ScentOfDeath.DROPLIST_COND[i][4], _319_ScentOfDeath.DROPLIST_COND[i][7], _319_ScentOfDeath.DROPLIST_COND[i][6]);
				else if(st.rollAndGive(_319_ScentOfDeath.DROPLIST_COND[i][4], _319_ScentOfDeath.DROPLIST_COND[i][7], _319_ScentOfDeath.DROPLIST_COND[i][7], _319_ScentOfDeath.DROPLIST_COND[i][5], _319_ScentOfDeath.DROPLIST_COND[i][6]) && _319_ScentOfDeath.DROPLIST_COND[i][1] != cond && _319_ScentOfDeath.DROPLIST_COND[i][1] != 0)
				{
					st.set("cond", String.valueOf(_319_ScentOfDeath.DROPLIST_COND[i][1]));
					st.setState(2);
				}
		return null;
	}

	static
	{
		DROPLIST_COND = new int[][] { { 1, 2, 20015, 0, 1045, 5, 20, 1 }, { 1, 2, 20020, 0, 1045, 5, 25, 1 } };
	}
}
