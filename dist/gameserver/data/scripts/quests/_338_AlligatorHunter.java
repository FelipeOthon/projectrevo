package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _338_AlligatorHunter extends Quest implements ScriptFile
{
	private static final int Enverun = 30892;
	private static final int AlligatorLeather = 4337;
	private static final int CrokianLad = 20804;
	private static final int DailaonLad = 20805;
	private static final int CrokianLadWarrior = 20806;
	private static final int FarhiteLad = 20807;
	private static final int NosLad = 20808;
	private static final int SwampTribe = 20991;
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

	public _338_AlligatorHunter()
	{
		super(false);
		DROPLIST_COND = new int[][] {
				{ 1, 0, 20804, 0, 4337, 0, 60, 1 },
				{ 1, 0, 20805, 0, 4337, 0, 60, 1 },
				{ 1, 0, 20806, 0, 4337, 0, 60, 1 },
				{ 1, 0, 20807, 0, 4337, 0, 60, 1 },
				{ 1, 0, 20808, 0, 4337, 0, 60, 1 },
				{ 1, 0, 20991, 0, 4337, 0, 60, 1 } };
		this.addStartNpc(30892);
		for(int i = 0; i < DROPLIST_COND.length; ++i)
			this.addKillId(new int[] { DROPLIST_COND[i][2] });
		addQuestItem(new int[] { 4337 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30892-02.htm"))
		{
			st.playSound(Quest.SOUND_ACCEPT);
			st.set("cond", "1");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("30892-02-afmenu.htm"))
		{
			final long AdenaCount = st.getQuestItemsCount(4337) * 40L;
			st.takeItems(4337, -1L);
			st.giveItems(57, AdenaCount);
		}
		else if(event.equalsIgnoreCase("quit"))
		{
			if(st.getQuestItemsCount(4337) >= 1L)
			{
				final long AdenaCount = st.getQuestItemsCount(4337) * 40L;
				st.takeItems(4337, -1L);
				st.giveItems(57, AdenaCount);
				htmltext = "30892-havequit.htm";
			}
			else
				htmltext = "30892-havent.htm";
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "<html><body>I have nothing to say you</body></html>";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30892)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 40)
					htmltext = "30892-01.htm";
				else
				{
					htmltext = "30892-00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(st.getQuestItemsCount(4337) == 0L)
				htmltext = "30892-02-rep.htm";
			else
				htmltext = "30892-menu.htm";
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
}
