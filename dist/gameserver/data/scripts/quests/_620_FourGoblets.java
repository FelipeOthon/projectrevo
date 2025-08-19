package quests;

import bosses.FourSepulchersManager;
import l2s.commons.util.Rnd;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _620_FourGoblets extends Quest implements ScriptFile
{
	private static int NAMELESS_SPIRIT;
	private static int GHOST_OF_WIGOTH_1;
	private static int GHOST_OF_WIGOTH_2;
	private static int CONQ_SM;
	private static int EMPER_SM;
	private static int SAGES_SM;
	private static int JUDGE_SM;
	private static int GHOST_CHAMBERLAIN_1;
	private static int GHOST_CHAMBERLAIN_2;
	private static int GRAVE_PASS;
	private static int[] GOBLETS;
	private static int RELIC;
	public static final int Sealed_Box = 7255;
	private static int ANTIQUE_BROOCH;
	private static int[] RCP_REWARDS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _620_FourGoblets()
	{
		super(false);
		this.addStartNpc(new int[] {
				_620_FourGoblets.NAMELESS_SPIRIT,
				_620_FourGoblets.CONQ_SM,
				_620_FourGoblets.EMPER_SM,
				_620_FourGoblets.SAGES_SM,
				_620_FourGoblets.JUDGE_SM,
				_620_FourGoblets.GHOST_CHAMBERLAIN_1,
				_620_FourGoblets.GHOST_CHAMBERLAIN_2 });
		this.addTalkId(new int[] { _620_FourGoblets.GHOST_OF_WIGOTH_1, _620_FourGoblets.GHOST_OF_WIGOTH_2 });
		addQuestItem(new int[] { 7255, _620_FourGoblets.GRAVE_PASS });
		addQuestItem(_620_FourGoblets.GOBLETS);
		for(int id = 18120; id <= 18256; ++id)
			this.addKillId(new int[] { id });
	}

	private static String onOpenBoxes(final QuestState st, final String count)
	{
		try
		{
			return new OpenSealedBox(st, Integer.parseInt(count)).apply();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Dont try to cheat with me!";
		}
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final Player player = st.getPlayer();
		final int cond = st.getCond();
		if(event.equalsIgnoreCase("Enter"))
		{
			FourSepulchersManager.tryEntry(npc, player);
			return null;
		}
		if(event.equalsIgnoreCase("accept"))
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 74)
				{
					st.setState(2);
					st.playSound(Quest.SOUND_ACCEPT);
					st.set("cond", "1");
					return "31453-13.htm";
				}
				st.exitCurrentQuest(true);
				return "31453-12.htm";
			}
		}
		else
		{
			if(event.startsWith("openBoxes "))
				return onOpenBoxes(st, event.replace("openBoxes ", "").trim());
			if(event.equalsIgnoreCase("12"))
			{
				if(!st.checkQuestItemsCount(_620_FourGoblets.GOBLETS))
					return "31453-14.htm";
				st.takeAllItems(_620_FourGoblets.GOBLETS);
				st.giveItems(_620_FourGoblets.ANTIQUE_BROOCH, 1L);
				st.set("cond", "2");
				st.playSound(Quest.SOUND_FINISH);
				return "31453-16.htm";
			}
			else
			{
				if(event.equalsIgnoreCase("13"))
				{
					st.playSound(Quest.SOUND_FINISH);
					st.exitCurrentQuest(true);
					return "31453-18.htm";
				}
				if(event.equalsIgnoreCase("14"))
				{
					if(cond == 2)
						return "31453-19.htm";
					return "31453-13.htm";
				}
				else if(event.equalsIgnoreCase("15"))
				{
					if(st.getQuestItemsCount(_620_FourGoblets.ANTIQUE_BROOCH) >= 1L)
					{
						st.getPlayer().teleToLocation(178298, -84574, -7216);
						return null;
					}
					if(st.getQuestItemsCount(_620_FourGoblets.GRAVE_PASS) >= 1L)
					{
						st.takeItems(_620_FourGoblets.GRAVE_PASS, 1L);
						st.getPlayer().teleToLocation(178298, -84574, -7216);
						return null;
					}
					return "" + str(npc.getNpcId()) + "-0.htm";
				}
				else if(event.equalsIgnoreCase("16"))
				{
					if(st.getQuestItemsCount(_620_FourGoblets.ANTIQUE_BROOCH) >= 1L)
					{
						st.getPlayer().teleToLocation(186942, -75602, -2834);
						return null;
					}
					if(st.getQuestItemsCount(_620_FourGoblets.GRAVE_PASS) >= 1L)
					{
						st.takeItems(_620_FourGoblets.GRAVE_PASS, 1L);
						st.getPlayer().teleToLocation(186942, -75602, -2834);
						return null;
					}
					return "" + str(npc.getNpcId()) + "-0.htm";
				}
				else
				{
					if(event.equalsIgnoreCase("17"))
					{
						if(st.getQuestItemsCount(_620_FourGoblets.ANTIQUE_BROOCH) >= 1L)
							st.getPlayer().teleToLocation(169590, -90218, -2914);
						else
						{
							st.takeItems(_620_FourGoblets.GRAVE_PASS, 1L);
							st.getPlayer().teleToLocation(169590, -90218, -2914);
						}
						return "31452-6.htm";
					}
					if(event.equalsIgnoreCase("18"))
					{
						if(st.getSumQuestItemsCount(_620_FourGoblets.GOBLETS) < 3L)
							return "31452-3.htm";
						if(st.getSumQuestItemsCount(_620_FourGoblets.GOBLETS) == 3L)
							return "31452-4.htm";
						if(st.getSumQuestItemsCount(_620_FourGoblets.GOBLETS) >= 4L)
							return "31452-5.htm";
					}
					else
					{
						if(event.equalsIgnoreCase("19"))
							return new OpenSealedBox(st, 1).apply();
						if(event.startsWith("19 "))
							return onOpenBoxes(st, event.replaceFirst("19 ", ""));
						if(event.equalsIgnoreCase("11"))
							return "<html><body><a action=\"bypass -h Quest _620_FourGoblets 19\">\"Please open a box.\"</a><br><a action=\"bypass -h Quest _620_FourGoblets 19 5\">\"Please open 5 boxes.\"</a><br><a action=\"bypass -h Quest _620_FourGoblets 19 10\">\"Please open 10 boxes.\"</a><br><a action=\"bypass -h Quest _620_FourGoblets 19 50\">\"Please open 50 boxes.\"</a><br></body></html>";
						int id = 0;
						try
						{
							id = Integer.parseInt(event);
						}
						catch(Exception ex)
						{}
						if(contains(_620_FourGoblets.RCP_REWARDS, id))
						{
							st.takeItems(_620_FourGoblets.RELIC, 1000L);
							st.giveItems(id, 1L);
							return "31454-17.htm";
						}
					}
				}
			}
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		final int cond = st.getCond();
		if(id == 1)
			st.set("cond", "0");
		if(npcId == _620_FourGoblets.NAMELESS_SPIRIT)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 74)
					htmltext = "31453-1.htm";
				else
				{
					htmltext = "31453-12.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
			{
				if(st.checkQuestItemsCount(_620_FourGoblets.GOBLETS))
					htmltext = "31453-15.htm";
				else
					htmltext = "31453-14.htm";
			}
			else if(cond == 2)
				htmltext = "31453-17.htm";
		}
		else if(npcId == _620_FourGoblets.GHOST_OF_WIGOTH_1)
		{
			if(cond == 2)
				htmltext = "31452-2.htm";
			else if(cond == 1)
				if(st.getSumQuestItemsCount(_620_FourGoblets.GOBLETS) == 1L)
					htmltext = "31452-1.htm";
				else if(st.getSumQuestItemsCount(_620_FourGoblets.GOBLETS) > 1L)
					htmltext = "31452-2.htm";
		}
		else if(npcId == _620_FourGoblets.GHOST_OF_WIGOTH_2)
		{
			if(st.getQuestItemsCount(_620_FourGoblets.RELIC) >= 1000L)
			{
				if(st.getQuestItemsCount(7255) >= 1L)
				{
					if(st.checkQuestItemsCount(_620_FourGoblets.GOBLETS))
						htmltext = "31454-4.htm";
					else if(st.checkQuestItemsCount(_620_FourGoblets.GOBLETS))
						htmltext = "31454-8.htm";
					else
						htmltext = "31454-12.htm";
				}
				else if(st.checkQuestItemsCount(_620_FourGoblets.GOBLETS))
					htmltext = "31454-3.htm";
				else if(st.getSumQuestItemsCount(_620_FourGoblets.GOBLETS) > 1L)
					htmltext = "31454-7.htm";
				else
					htmltext = "31454-11.htm";
			}
			else if(st.getQuestItemsCount(7255) >= 1L)
			{
				if(st.checkQuestItemsCount(_620_FourGoblets.GOBLETS))
					htmltext = "31454-2.htm";
				else if(st.getSumQuestItemsCount(_620_FourGoblets.GOBLETS) > 1L)
					htmltext = "31454-6.htm";
				else
					htmltext = "31454-10.htm";
			}
			else if(st.checkQuestItemsCount(_620_FourGoblets.GOBLETS))
				htmltext = "31454-1.htm";
			else if(st.getSumQuestItemsCount(_620_FourGoblets.GOBLETS) > 1L)
				htmltext = "31454-5.htm";
			else
				htmltext = "31454-9.htm";
		}
		else if(npcId == _620_FourGoblets.CONQ_SM)
			htmltext = "31921-E.htm";
		else if(npcId == _620_FourGoblets.EMPER_SM)
			htmltext = "31922-E.htm";
		else if(npcId == _620_FourGoblets.SAGES_SM)
			htmltext = "31923-E.htm";
		else if(npcId == _620_FourGoblets.JUDGE_SM)
			htmltext = "31924-E.htm";
		else if(npcId == _620_FourGoblets.GHOST_CHAMBERLAIN_1)
			htmltext = "31919-1.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getCond();
		if((cond == 1 || cond == 2) && npcId >= 18120 && npcId <= 18256 && Rnd.chance(30))
		{
			st.giveItems(7255, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}

	static
	{
		_620_FourGoblets.NAMELESS_SPIRIT = 31453;
		_620_FourGoblets.GHOST_OF_WIGOTH_1 = 31452;
		_620_FourGoblets.GHOST_OF_WIGOTH_2 = 31454;
		_620_FourGoblets.CONQ_SM = 31921;
		_620_FourGoblets.EMPER_SM = 31922;
		_620_FourGoblets.SAGES_SM = 31923;
		_620_FourGoblets.JUDGE_SM = 31924;
		_620_FourGoblets.GHOST_CHAMBERLAIN_1 = 31919;
		_620_FourGoblets.GHOST_CHAMBERLAIN_2 = 31920;
		_620_FourGoblets.GRAVE_PASS = 7261;
		_620_FourGoblets.GOBLETS = new int[] { 7256, 7257, 7258, 7259 };
		_620_FourGoblets.RELIC = 7254;
		_620_FourGoblets.ANTIQUE_BROOCH = 7262;
		_620_FourGoblets.RCP_REWARDS = new int[] { 6881, 6883, 6885, 6887, 6891, 6893, 6895, 6897, 6899, 7580 };
	}
}
