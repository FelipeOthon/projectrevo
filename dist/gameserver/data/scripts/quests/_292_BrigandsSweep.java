package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _292_BrigandsSweep extends Quest implements ScriptFile
{
	private static int Spiron;
	private static int Balanki;
	private static int GoblinBrigand;
	private static int GoblinBrigandLeader;
	private static int GoblinBrigandLieutenant;
	private static int GoblinSnooper;
	private static int GoblinLord;
	private static short GoblinNecklace;
	private static short GoblinPendant;
	private static short GoblinLordPendant;
	private static short SuspiciousMemo;
	private static short SuspiciousContract;
	private static int Chance;
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

	public _292_BrigandsSweep()
	{
		super(false);
		this.addStartNpc(_292_BrigandsSweep.Spiron);
		this.addTalkId(new int[] { _292_BrigandsSweep.Balanki });
		for(int i = 0; i < _292_BrigandsSweep.DROPLIST_COND.length; ++i)
			this.addKillId(new int[] { _292_BrigandsSweep.DROPLIST_COND[i][2] });
		addQuestItem(new int[] { _292_BrigandsSweep.SuspiciousMemo });
		addQuestItem(new int[] { _292_BrigandsSweep.SuspiciousContract });
		addQuestItem(new int[] { _292_BrigandsSweep.GoblinNecklace });
		addQuestItem(new int[] { _292_BrigandsSweep.GoblinPendant });
		addQuestItem(new int[] { _292_BrigandsSweep.GoblinLordPendant });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("elder_spiron_q0292_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("elder_spiron_q0292_06.htm"))
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
		if(npcId == _292_BrigandsSweep.Spiron)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getRace() != Race.dwarf)
				{
					htmltext = "elder_spiron_q0292_00.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() < 5)
				{
					htmltext = "elder_spiron_q0292_01.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "elder_spiron_q0292_02.htm";
			}
			else if(cond == 1)
			{
				final long reward = st.getQuestItemsCount(_292_BrigandsSweep.GoblinNecklace) * 12L + st.getQuestItemsCount(_292_BrigandsSweep.GoblinPendant) * 36L + st.getQuestItemsCount(_292_BrigandsSweep.GoblinLordPendant) * 33L + st.getQuestItemsCount(_292_BrigandsSweep.SuspiciousContract) * 100L;
				if(reward == 0L)
					return "elder_spiron_q0292_04.htm";
				if(st.getQuestItemsCount(_292_BrigandsSweep.SuspiciousContract) != 0L)
					htmltext = "elder_spiron_q0292_10.htm";
				else if(st.getQuestItemsCount(_292_BrigandsSweep.SuspiciousMemo) == 0L)
					htmltext = "elder_spiron_q0292_05.htm";
				else if(st.getQuestItemsCount(_292_BrigandsSweep.SuspiciousMemo) == 1L)
					htmltext = "elder_spiron_q0292_08.htm";
				else
					htmltext = "elder_spiron_q0292_09.htm";
				st.takeItems(_292_BrigandsSweep.GoblinNecklace, -1L);
				st.takeItems(_292_BrigandsSweep.GoblinPendant, -1L);
				st.takeItems(_292_BrigandsSweep.GoblinLordPendant, -1L);
				st.takeItems(_292_BrigandsSweep.SuspiciousContract, -1L);
				st.giveItems(57, reward);
			}
		}
		else if(npcId == _292_BrigandsSweep.Balanki && cond == 1)
			if(st.getQuestItemsCount(_292_BrigandsSweep.SuspiciousContract) == 0L)
				htmltext = "balanki_q0292_01.htm";
			else
			{
				st.takeItems(_292_BrigandsSweep.SuspiciousContract, -1L);
				st.giveItems(57, 120L);
				htmltext = "balanki_q0292_02.htm";
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		for(int i = 0; i < _292_BrigandsSweep.DROPLIST_COND.length; ++i)
			if(cond == _292_BrigandsSweep.DROPLIST_COND[i][0] && npcId == _292_BrigandsSweep.DROPLIST_COND[i][2] && (_292_BrigandsSweep.DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(_292_BrigandsSweep.DROPLIST_COND[i][3]) > 0L))
				if(_292_BrigandsSweep.DROPLIST_COND[i][5] == 0)
					st.rollAndGive(_292_BrigandsSweep.DROPLIST_COND[i][4], _292_BrigandsSweep.DROPLIST_COND[i][7], _292_BrigandsSweep.DROPLIST_COND[i][6]);
				else if(st.rollAndGive(_292_BrigandsSweep.DROPLIST_COND[i][4], _292_BrigandsSweep.DROPLIST_COND[i][7], _292_BrigandsSweep.DROPLIST_COND[i][7], _292_BrigandsSweep.DROPLIST_COND[i][5], _292_BrigandsSweep.DROPLIST_COND[i][6]) && _292_BrigandsSweep.DROPLIST_COND[i][1] != cond && _292_BrigandsSweep.DROPLIST_COND[i][1] != 0)
				{
					st.set("cond", String.valueOf(_292_BrigandsSweep.DROPLIST_COND[i][1]));
					st.setState(2);
				}
		if(st.getQuestItemsCount(_292_BrigandsSweep.SuspiciousContract) == 0L && Rnd.chance(_292_BrigandsSweep.Chance))
			if(st.getQuestItemsCount(_292_BrigandsSweep.SuspiciousMemo) < 3L)
			{
				st.giveItems(_292_BrigandsSweep.SuspiciousMemo, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			else
			{
				st.takeItems(_292_BrigandsSweep.SuspiciousMemo, -1L);
				st.giveItems(_292_BrigandsSweep.SuspiciousContract, 1L);
				st.playSound(Quest.SOUND_MIDDLE);
			}
		return null;
	}

	static
	{
		_292_BrigandsSweep.Spiron = 30532;
		_292_BrigandsSweep.Balanki = 30533;
		_292_BrigandsSweep.GoblinBrigand = 20322;
		_292_BrigandsSweep.GoblinBrigandLeader = 20323;
		_292_BrigandsSweep.GoblinBrigandLieutenant = 20324;
		_292_BrigandsSweep.GoblinSnooper = 20327;
		_292_BrigandsSweep.GoblinLord = 20528;
		_292_BrigandsSweep.GoblinNecklace = 1483;
		_292_BrigandsSweep.GoblinPendant = 1484;
		_292_BrigandsSweep.GoblinLordPendant = 1485;
		_292_BrigandsSweep.SuspiciousMemo = 1486;
		_292_BrigandsSweep.SuspiciousContract = 1487;
		_292_BrigandsSweep.Chance = 10;
		DROPLIST_COND = new int[][] {
				{ 1, 0, _292_BrigandsSweep.GoblinBrigand, 0, _292_BrigandsSweep.GoblinNecklace, 0, 40, 1 },
				{ 1, 0, _292_BrigandsSweep.GoblinBrigandLeader, 0, _292_BrigandsSweep.GoblinNecklace, 0, 40, 1 },
				{ 1, 0, _292_BrigandsSweep.GoblinSnooper, 0, _292_BrigandsSweep.GoblinNecklace, 0, 40, 1 },
				{ 1, 0, _292_BrigandsSweep.GoblinBrigandLieutenant, 0, _292_BrigandsSweep.GoblinPendant, 0, 40, 1 },
				{ 1, 0, _292_BrigandsSweep.GoblinLord, 0, _292_BrigandsSweep.GoblinLordPendant, 0, 40, 1 } };
	}
}
