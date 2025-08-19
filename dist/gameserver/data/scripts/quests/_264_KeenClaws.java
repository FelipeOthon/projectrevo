package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _264_KeenClaws extends Quest implements ScriptFile
{
	private static final int Payne = 30136;
	private static final int WolfClaw = 1367;
	private static final int LeatherSandals = 36;
	private static final int WoodenHelmet = 43;
	private static final int Stockings = 462;
	private static final int HealingPotion = 1061;
	private static final int ShortGloves = 48;
	private static final int ClothShoes = 35;
	private static final int Goblin = 20003;
	private static final int AshenWolf = 20456;
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

	public _264_KeenClaws()
	{
		super(false);
		this.addStartNpc(30136);
		this.addKillId(new int[] { 20003 });
		this.addKillId(new int[] { 20456 });
		addQuestItem(new int[] { 1367 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("paint_q0264_03.htm"))
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
		if(npcId == 30136)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 3)
				{
					st.exitCurrentQuest(true);
					return "paint_q0264_01.htm";
				}
				htmltext = "paint_q0264_02.htm";
			}
			else if(cond == 1)
				htmltext = "paint_q0264_04.htm";
			else if(cond == 2)
			{
				st.takeItems(1367, -1L);
				final int n = Rnd.get(17);
				if(n == 0)
				{
					st.giveItems(43, 1L);
					st.playSound(Quest.SOUND_JACKPOT);
				}
				else if(n < 2)
					st.giveItems(57, 1000L);
				else if(n < 5)
					st.giveItems(36, 1L);
				else if(n < 8)
				{
					st.giveItems(462, 1L);
					st.giveItems(57, 50L);
				}
				else if(n < 11)
					st.giveItems(1061, 1L);
				else if(n < 14)
					st.giveItems(48, 1L);
				else
					st.giveItems(35, 1L);
				htmltext = "paint_q0264_05.htm";
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
		for(int i = 0; i < _264_KeenClaws.DROPLIST_COND.length; ++i)
			if(cond == _264_KeenClaws.DROPLIST_COND[i][0] && npcId == _264_KeenClaws.DROPLIST_COND[i][2] && (_264_KeenClaws.DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(_264_KeenClaws.DROPLIST_COND[i][3]) > 0L))
				if(_264_KeenClaws.DROPLIST_COND[i][5] == 0)
					st.rollAndGive(_264_KeenClaws.DROPLIST_COND[i][4], _264_KeenClaws.DROPLIST_COND[i][7], _264_KeenClaws.DROPLIST_COND[i][6]);
				else if(st.rollAndGive(_264_KeenClaws.DROPLIST_COND[i][4], _264_KeenClaws.DROPLIST_COND[i][7], _264_KeenClaws.DROPLIST_COND[i][7], _264_KeenClaws.DROPLIST_COND[i][5], _264_KeenClaws.DROPLIST_COND[i][6]) && _264_KeenClaws.DROPLIST_COND[i][1] != cond && _264_KeenClaws.DROPLIST_COND[i][1] != 0)
				{
					st.set("cond", String.valueOf(_264_KeenClaws.DROPLIST_COND[i][1]));
					st.setState(2);
				}
		return null;
	}

	static
	{
		DROPLIST_COND = new int[][] { { 1, 2, 20003, 0, 1367, 50, 50, 2 }, { 1, 2, 20456, 0, 1367, 50, 50, 2 } };
	}
}
