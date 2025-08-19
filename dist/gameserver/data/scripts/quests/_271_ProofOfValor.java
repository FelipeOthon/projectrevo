package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _271_ProofOfValor extends Quest implements ScriptFile
{
	private static final int RUKAIN = 30577;
	private static final int KASHA_WOLF_FANG_ID = 1473;
	private static final int NECKLACE_OF_VALOR_ID = 1507;
	private static final int NECKLACE_OF_COURAGE_ID = 1506;
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

	public _271_ProofOfValor()
	{
		super(false);
		this.addStartNpc(30577);
		this.addTalkId(new int[] { 30577 });
		for(int i = 0; i < _271_ProofOfValor.DROPLIST_COND.length; ++i)
			this.addKillId(new int[] { _271_ProofOfValor.DROPLIST_COND[i][2] });
		addQuestItem(new int[] { 1473 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("praetorian_rukain_q0271_03.htm"))
		{
			st.playSound(Quest.SOUND_ACCEPT);
			if(st.getQuestItemsCount(1506) > 0L || st.getQuestItemsCount(1507) > 0L)
				htmltext = "praetorian_rukain_q0271_07.htm";
			st.set("cond", "1");
			st.setState(2);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30577)
			if(cond == 0)
			{
				if(st.getPlayer().getRace() != Race.orc)
				{
					htmltext = "praetorian_rukain_q0271_00.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() < 4)
				{
					htmltext = "praetorian_rukain_q0271_01.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getQuestItemsCount(1506) > 0L || st.getQuestItemsCount(1507) > 0L)
				{
					htmltext = "praetorian_rukain_q0271_06.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "praetorian_rukain_q0271_02.htm";
			}
			else if(cond == 1)
				htmltext = "praetorian_rukain_q0271_04.htm";
			else if(cond == 2 && st.getQuestItemsCount(1473) == 50L)
			{
				st.takeItems(1473, -1L);
				if(Rnd.chance(14))
				{
					st.takeItems(1507, -1L);
					st.giveItems(1507, 1L);
				}
				else
				{
					st.takeItems(1506, -1L);
					st.giveItems(1506, 1L);
				}
				htmltext = "praetorian_rukain_q0271_05.htm";
				st.exitCurrentQuest(true);
			}
			else if(cond == 2 && st.getQuestItemsCount(1473) < 50L)
			{
				htmltext = "praetorian_rukain_q0271_04.htm";
				st.set("cond", "1");
				st.setState(2);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		for(int i = 0; i < _271_ProofOfValor.DROPLIST_COND.length; ++i)
			if(cond == _271_ProofOfValor.DROPLIST_COND[i][0] && npcId == _271_ProofOfValor.DROPLIST_COND[i][2] && (_271_ProofOfValor.DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(_271_ProofOfValor.DROPLIST_COND[i][3]) > 0L))
				if(_271_ProofOfValor.DROPLIST_COND[i][5] == 0)
					st.rollAndGive(_271_ProofOfValor.DROPLIST_COND[i][4], _271_ProofOfValor.DROPLIST_COND[i][7], _271_ProofOfValor.DROPLIST_COND[i][6]);
				else if(st.rollAndGive(_271_ProofOfValor.DROPLIST_COND[i][4], _271_ProofOfValor.DROPLIST_COND[i][7], _271_ProofOfValor.DROPLIST_COND[i][7], _271_ProofOfValor.DROPLIST_COND[i][5], _271_ProofOfValor.DROPLIST_COND[i][6]) && _271_ProofOfValor.DROPLIST_COND[i][1] != cond && _271_ProofOfValor.DROPLIST_COND[i][1] != 0)
				{
					st.set("cond", String.valueOf(_271_ProofOfValor.DROPLIST_COND[i][1]));
					st.setState(2);
				}
		return null;
	}

	static
	{
		DROPLIST_COND = new int[][] { { 1, 2, 20475, 0, 1473, 50, 25, 2 } };
	}
}
