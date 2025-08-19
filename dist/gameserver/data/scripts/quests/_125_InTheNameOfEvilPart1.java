package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.AddonsConfig;

public class _125_InTheNameOfEvilPart1 extends Quest implements ScriptFile
{
	private static final int Mushika = 32114;
	private static final int Karakawei = 32117;
	private static final int UluKaimu = 32119;
	private static final int BaluKaimu = 32120;
	private static final int ChutaKaimu = 32121;
	private static final int EPITAPH = 8781;
	private static final int OrClaw = 8779;
	private static final int DienBone = 8780;
	private static final int[] Ornithomimus;
	private static final int[] Deinonychus;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _125_InTheNameOfEvilPart1()
	{
		super(false);
		this.addStartNpc(32114);
		this.addTalkId(new int[] { 32117 });
		this.addTalkId(new int[] { 32119 });
		this.addKillId(_125_InTheNameOfEvilPart1.Ornithomimus);
		this.addKillId(_125_InTheNameOfEvilPart1.Deinonychus);
		this.addTalkId(new int[] { 32120 });
		this.addTalkId(new int[] { 32121 });
	}

	private String getWordText32119(final QuestState st)
	{
		String htmltext = HtmCache.getInstance().getHtml("quests/_125_InTheNameOfEvilPart1/32119.htm", st.getPlayer());
		htmltext = htmltext.replace("%1%", st.getInt("T32119") == 0 ? "_" : "T");
		htmltext = htmltext.replace("%2%", st.getInt("E32119") == 0 ? "_" : "E");
		htmltext = htmltext.replace("%3%", st.getInt("P32119") == 0 ? "_" : "P");
		htmltext = htmltext.replace("%4%", st.getInt("U32119") == 0 ? "_" : "U");
		if(st.getInt("T32119") > 0 && st.getInt("E32119") > 0 && st.getInt("P32119") > 0 && st.getInt("U32119") > 0)
			htmltext = htmltext.replace("%5%", "<a action=\"bypass -h Quest _125_InTheNameOfEvilPart1 OK32119\">OK</a>");
		else
			htmltext = htmltext.replace("%5%", "");
		return htmltext;
	}

	private String getWordText32120(final QuestState st)
	{
		String htmltext = HtmCache.getInstance().getHtml("quests/_125_InTheNameOfEvilPart1/32120.htm", st.getPlayer());
		htmltext = htmltext.replace("%1%", st.getInt("T32120") == 0 ? "_" : "T");
		htmltext = htmltext.replace("%2%", st.getInt("O32120") == 0 ? "_" : "O");
		htmltext = htmltext.replace("%3%", st.getInt("O32120") <= 1 ? "_" : "O");
		htmltext = htmltext.replace("%4%", st.getInt("N32120") == 0 ? "_" : "N");
		if(st.getInt("T32120") > 0 && st.getInt("O32120") > 1 && st.getInt("N32120") > 0)
			htmltext = htmltext.replace("%5%", "<a action=\"bypass -h Quest _125_InTheNameOfEvilPart1 OK32120\">OK</a>");
		else
			htmltext = htmltext.replace("%5%", "");
		return htmltext;
	}

	private String getWordText32121(final QuestState st)
	{
		String htmltext = HtmCache.getInstance().getHtml("quests/_125_InTheNameOfEvilPart1/32121.htm", st.getPlayer());
		htmltext = htmltext.replace("%1%", st.getInt("W32121") == 0 ? "_" : "W");
		htmltext = htmltext.replace("%2%", st.getInt("A32121") == 0 ? "_" : "A");
		htmltext = htmltext.replace("%3%", st.getInt("G32121") == 0 ? "_" : "G");
		htmltext = htmltext.replace("%4%", st.getInt("U32121") == 0 ? "_" : "U");
		if(st.getInt("W32121") > 0 && st.getInt("A32121") > 0 && st.getInt("G32121") > 0 && st.getInt("U32121") > 0)
			htmltext = htmltext.replace("%5%", "<a action=\"bypass -h Quest _125_InTheNameOfEvilPart1 OK32121\">OK</a>");
		else
			htmltext = htmltext.replace("%5%", "");
		return htmltext;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = "";
		if(event.equalsIgnoreCase("OK32119"))
		{
			htmltext = "32119-1.htm";
			st.set("cond", "4");
		}
		if(event.equalsIgnoreCase("T32119"))
		{
			if(st.getInt("T32119") < 1)
				st.set("T32119", "1");
			htmltext = getWordText32119(st);
		}
		else if(event.equalsIgnoreCase("E32119"))
		{
			if(st.getInt("E32119") < 1)
				st.set("E32119", "1");
			htmltext = getWordText32119(st);
		}
		else if(event.equalsIgnoreCase("P32119"))
		{
			if(st.getInt("P32119") < 1)
				st.set("P32119", "1");
			htmltext = getWordText32119(st);
		}
		else if(event.equalsIgnoreCase("U32119"))
		{
			if(st.getInt("U32119") < 1)
				st.set("U32119", "1");
			htmltext = getWordText32119(st);
		}
		else if(event.equalsIgnoreCase("OK32120"))
		{
			htmltext = "32120-1.htm";
			st.set("cond", "5");
		}
		if(event.equalsIgnoreCase("T32120"))
		{
			if(st.getInt("T32120") < 1)
				st.set("T32120", "1");
			htmltext = getWordText32120(st);
		}
		else if(event.equalsIgnoreCase("O32120"))
		{
			if(st.getInt("O32120") < 1)
				st.set("O32120", "1");
			else if(st.getInt("O32120") == 1)
				st.set("O32120", "2");
			htmltext = getWordText32120(st);
		}
		else if(event.equalsIgnoreCase("N32120"))
		{
			if(st.getInt("N32120") < 1)
				st.set("N32120", "1");
			htmltext = getWordText32120(st);
		}
		else if(event.equalsIgnoreCase("OK32121"))
		{
			htmltext = "32121-1.htm";
			st.set("cond", "6");
		}
		if(event.equalsIgnoreCase("W32121"))
		{
			if(st.getInt("W32121") < 1)
				st.set("W32121", "1");
			htmltext = getWordText32121(st);
		}
		else if(event.equalsIgnoreCase("A32121"))
		{
			if(st.getInt("A32121") < 1)
				st.set("A32121", "1");
			htmltext = getWordText32121(st);
		}
		else if(event.equalsIgnoreCase("G32121"))
		{
			if(st.getInt("G32121") < 1)
				st.set("G32121", "1");
			htmltext = getWordText32121(st);
		}
		else if(event.equalsIgnoreCase("U32121"))
		{
			if(st.getInt("U32121") < 1)
				st.set("U32121", "1");
			htmltext = getWordText32121(st);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 32114)
		{
			if(st.getPlayer().getLevel() < 76)
			{
				htmltext = "<html>This quest for 76 level characters.</html>";
				st.exitCurrentQuest(true);
			}
			else if(cond == 0)
			{
				htmltext = "32114.htm";
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else if(cond == 6)
			{
				st.unset("OK32119");
				st.unset("OK32120");
				st.unset("OK32121");
				st.unset("T32119");
				st.unset("E32119");
				st.unset("P32119");
				st.unset("U32119");
				st.unset("T32120");
				st.unset("O32120");
				st.unset("N32120");
				st.unset("W32121");
				st.unset("A32121");
				st.unset("G32121");
				st.unset("U32121");
				st.unset("cond");
				htmltext = "<html>Quest In the Name of Evil Part 1 complete!</html>";
				st.addExpAndSp(859195L, 86603L);
				st.giveItems(8781, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 32117)
		{
			if(cond == 1)
			{
				htmltext = "32117.htm";
				st.set("cond", "2");
			}
			else if(cond == 2 && (st.getQuestItemsCount(8779) < 2L || st.getQuestItemsCount(8780) < 2L))
				htmltext = "32117.htm";
			else
			{
				st.takeItems(8780, -1L);
				st.takeItems(8779, -1L);
				htmltext = "32117-1.htm";
				st.set("cond", "3");
			}
		}
		else if(npcId == 32119)
		{
			if(cond == 3)
				htmltext = "32119.htm";
			else if(cond == 4)
				htmltext = "32119-1.htm";
		}
		else if(npcId == 32120)
		{
			if(cond == 4)
				htmltext = "32120.htm";
			else if(cond == 5)
				htmltext = "32120-1.htm";
		}
		else if(npcId == 32121)
			if(cond == 5)
				htmltext = "32121.htm";
			else if(cond == 6)
				htmltext = "32121-1.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int id = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond == 2)
		{
			for(final int i : _125_InTheNameOfEvilPart1.Ornithomimus)
				if(id == i && st.getQuestItemsCount(8779) < 2L && Rnd.chance(10.0f * AddonsConfig.getQuestDropRates(this)))
				{
					st.giveItems(8779, 1L);
					st.playSound(Quest.SOUND_MIDDLE);
					return null;
				}
			for(final int i : _125_InTheNameOfEvilPart1.Deinonychus)
				if(id == i && st.getQuestItemsCount(8780) < 2L && Rnd.chance(10.0f * AddonsConfig.getQuestDropRates(this)))
				{
					st.giveItems(8780, 1L);
					st.playSound(Quest.SOUND_MIDDLE);
					return null;
				}
		}
		return null;
	}

	static
	{
		Ornithomimus = new int[] { 22200, 22201, 22202, 22219, 22224 };
		Deinonychus = new int[] { 22203, 22204, 22205, 22220, 22225 };
	}
}
