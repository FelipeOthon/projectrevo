package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _376_GiantsExploration1 extends Quest implements ScriptFile
{
	private static final int DROP_RATE = 5;
	private static final int DROP_RATE_2 = 8;
	private static final int ANC_SCROLL = 5944;
	private static final int DICT1 = 5891;
	private static final int DICT2 = 5892;
	private static final int MST_BK = 5890;
	private static final int[][] EXCHANGE;
	private static final String error_1 = "<html><head><body>Head Researcher Sobling:<br><br>I think it is too early for you to help me. Come back after you have gained some more experience. <br><font color=\"LEVEL\">(Quest for characters level 51 and above.)</font></body></html>";
	private static final String start = "<html><head><body>Head Researcher Sobling:<br><br>As leader of the research team i'm looking for experienced adventurers to join us. Actually we're searching for relics from the ancient Giants Culture. There are four scripts we can't find yet: <font color=\"LEVEL\">Plans for the construction of Golems, Basis of the Giant's Magic, Construction Technology Handbook and Giant's Medical Theory.</font><br><br>Given the value of the items we're looking for, there is no need to tell you how generous shall be my rewards.<br><br><a action=\"bypass -h Quest _376_GiantsExploration1 yes\">I will search for ancient items</a><br><a action=\"bypass -h Quest _376_GiantsExploration1 0\">I won't help you</a><br></body></html>";
	private static final String starting = "Starting.htm";
	private static final String checkout = "<html><head><body>Head Researcher Sobling:<br><br>Excellent! You came back! Was it difficult to collect ancient pieces?<br><br>Let me see what you've found thus far...<br><br><a action=\"bypass -h Quest _376_GiantsExploration1 show\">Show him the books you collected</a><br><a action=\"bypass -h Quest _376_GiantsExploration1 myst\">Show him another items you've found</a><br></body></html>";
	private static final String checkout2 = "<html><head><body>Head Researcher Sobling:<br><br>Excellent! You came back! Was it difficult to collect ancient pieces?<br><br>Hum... what is it? You have some ancient scrolls here, but those are of no use to me until you translate its contents, i have no time to do it on my own and that's why i gave you the dictionary. Anyway i can check any other ancient item you may have...<br><br><a action=\"bypass -h Quest _376_GiantsExploration1 show\">Show him the scrolls you collected</a><br><a action=\"bypass -h Quest _376_GiantsExploration1 myst\">Show him another items you've found</a><br></body></html>";
	private static final String no_items = "<html><head><body>Head Researcher Sobling:<br><br>Hum... I see no valuable items here, you should continue your research. I'm pretty sure you can do it better if you put more effort... what you think?<br><br><a action=\"bypass -h Quest _376_GiantsExploration1 Starting.htm\">I will continue</a><br><a action=\"bypass -h Quest _376_GiantsExploration1 0\">I will quit</a><br></body></html>";
	private static final String tnx4items = "<html><head><body>Head Researcher Sobling:<br><br>Amazing! These are the sort of items i was looking for... Take this rare recipes as a proof of my gratitude. Anyhow, I'm sure there are more ancient relics guarded by those monsters, would you like to search some more?<br><br><a action=\"bypass -h Quest _376_GiantsExploration1 Starting.htm\">I will continue</a><br><a action=\"bypass -h Quest _376_GiantsExploration1 0\">I will quit</a><br></body></html>";
	private static final String go_part2 = "<html><head><body>Head Researcher Sobling:<br><br>Interesting. What could this mysterious book be? I'm afraid we will not be able to figure out it's contents without assistance. But don't worry, i know who can help us, go now and talk with <font color=\"LEVEL\">Warehouse Freightman Cliff in Oren Castle Town</font>, show him the tome and he will probably know what to do with it.<br><br></body></html>";
	private static final String no_part2 = "<html><head><body>Head Researcher Sobling:<br><br>I don't find anything useful here... I'm aware already about some discoveries related to giant's living, but there is no archeological value in vane letters or drawings you may find. As i told you, we are in desperate search for <font color=\"LEVEL\">Plans for the construction of Golems, Basis of the Giant's Magic, Construction Technology and Giant's Medical Theory.</font> You must bring any of those books complete. Few we can do only with fragments.<br><br></body></html>";
	private static final String ok_part2 = "<html><head><body>Warehouse Freightman Cliff:<br><br>What is that book? Sobling told you to bring it to me? Well... It's written in a very ancient language.. yes.. Humm.. Take this \"Ancient Language Dictionary: Intermediate Level\" and meet <font color=\"LEVEL\">Sobling</font> again. With this he will be able to translate it. Leave now.</body></html>";
	private static final String gogogo_2 = "<html><head><body>Head Researcher Sobling:<br><br>Are you still there with the book? There is no way to read it's contents with our current knowledge. Take the book to <font color=\"LEVEL\">Warehouse Freightman Cliff in Oren Castle Town</font>.<br><br></body></html>";
	private static final String ext_msg = "Quest aborted";
	private static final int HR_SOBLING = 31147;
	private static final int WF_CLIFF = 30182;
	private static final int MOBS_START = 20647;
	private static final int MOBS_END = 20650;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _376_GiantsExploration1()
	{
		super(true);
		this.addStartNpc(31147);
		this.addTalkId(new int[] { 30182 });
		for(int i = 20647; i <= 20650; ++i)
			this.addKillId(new int[] { i });
		addQuestItem(new int[] { 5891, 5890, 5944 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("yes"))
		{
			htmltext = "Starting.htm";
			st.setState(2);
			st.set("cond", "1");
			st.giveItems(5891, 1L);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("0"))
		{
			htmltext = "Quest aborted";
			st.playSound(Quest.SOUND_FINISH);
			st.takeItems(5891, -1L);
			st.takeItems(5890, -1L);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("show"))
		{
			htmltext = "<html><head><body>Head Researcher Sobling:<br><br>Hum... I see no valuable items here, you should continue your research. I'm pretty sure you can do it better if you put more effort... what you think?<br><br><a action=\"bypass -h Quest _376_GiantsExploration1 Starting.htm\">I will continue</a><br><a action=\"bypass -h Quest _376_GiantsExploration1 0\">I will quit</a><br></body></html>";
			for(int i = 0; i < _376_GiantsExploration1.EXCHANGE.length; i += 2)
			{
				long count = Long.MAX_VALUE;
				for(final int j : _376_GiantsExploration1.EXCHANGE[i])
					count = Math.min(count, st.getQuestItemsCount(j));
				if(count >= 1L)
				{
					htmltext = "<html><head><body>Head Researcher Sobling:<br><br>Amazing! These are the sort of items i was looking for... Take this rare recipes as a proof of my gratitude. Anyhow, I'm sure there are more ancient relics guarded by those monsters, would you like to search some more?<br><br><a action=\"bypass -h Quest _376_GiantsExploration1 Starting.htm\">I will continue</a><br><a action=\"bypass -h Quest _376_GiantsExploration1 0\">I will quit</a><br></body></html>";
					for(final int j : _376_GiantsExploration1.EXCHANGE[i])
						st.takeItems(j, count);
					for(int l = 0; l < count; ++l)
					{
						int item = _376_GiantsExploration1.EXCHANGE[i + 1][Rnd.get(_376_GiantsExploration1.EXCHANGE[i + 1].length)];
						if(Config.ALT_100_RECIPES_A)
							++item;
						st.giveItems(item, 1L);
					}
				}
			}
		}
		else if(event.equalsIgnoreCase("myst"))
			if(st.getQuestItemsCount(5890) > 0L)
			{
				if(cond == 1)
				{
					st.setState(2);
					st.set("cond", "2");
					htmltext = "<html><head><body>Head Researcher Sobling:<br><br>Interesting. What could this mysterious book be? I'm afraid we will not be able to figure out it's contents without assistance. But don't worry, i know who can help us, go now and talk with <font color=\"LEVEL\">Warehouse Freightman Cliff in Oren Castle Town</font>, show him the tome and he will probably know what to do with it.<br><br></body></html>";
				}
				else if(cond == 2)
					htmltext = "<html><head><body>Head Researcher Sobling:<br><br>Are you still there with the book? There is no way to read it's contents with our current knowledge. Take the book to <font color=\"LEVEL\">Warehouse Freightman Cliff in Oren Castle Town</font>.<br><br></body></html>";
			}
			else
				htmltext = "<html><head><body>Head Researcher Sobling:<br><br>I don't find anything useful here... I'm aware already about some discoveries related to giant's living, but there is no archeological value in vane letters or drawings you may find. As i told you, we are in desperate search for <font color=\"LEVEL\">Plans for the construction of Golems, Basis of the Giant's Magic, Construction Technology and Giant's Medical Theory.</font> You must bring any of those books complete. Few we can do only with fragments.<br><br></body></html>";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		final int cond = st.getInt("cond");
		if(npcId == 31147)
		{
			if(id == 1)
			{
				htmltext = "<html><head><body>Head Researcher Sobling:<br><br>As leader of the research team i'm looking for experienced adventurers to join us. Actually we're searching for relics from the ancient Giants Culture. There are four scripts we can't find yet: <font color=\"LEVEL\">Plans for the construction of Golems, Basis of the Giant's Magic, Construction Technology Handbook and Giant's Medical Theory.</font><br><br>Given the value of the items we're looking for, there is no need to tell you how generous shall be my rewards.<br><br><a action=\"bypass -h Quest _376_GiantsExploration1 yes\">I will search for ancient items</a><br><a action=\"bypass -h Quest _376_GiantsExploration1 0\">I won't help you</a><br></body></html>";
				if(st.getPlayer().getLevel() < 51)
				{
					st.exitCurrentQuest(true);
					htmltext = "<html><head><body>Head Researcher Sobling:<br><br>I think it is too early for you to help me. Come back after you have gained some more experience. <br><font color=\"LEVEL\">(Quest for characters level 51 and above.)</font></body></html>";
				}
			}
			else if(id == 2)
				if(st.getQuestItemsCount(5944) == 0L && st.getQuestItemsCount(5890) == 0L)
					htmltext = "<html><head><body>Head Researcher Sobling:<br><br>Excellent! You came back! Was it difficult to collect ancient pieces?<br><br>Let me see what you've found thus far...<br><br><a action=\"bypass -h Quest _376_GiantsExploration1 show\">Show him the books you collected</a><br><a action=\"bypass -h Quest _376_GiantsExploration1 myst\">Show him another items you've found</a><br></body></html>";
				else
					htmltext = "<html><head><body>Head Researcher Sobling:<br><br>Excellent! You came back! Was it difficult to collect ancient pieces?<br><br>Hum... what is it? You have some ancient scrolls here, but those are of no use to me until you translate its contents, i have no time to do it on my own and that's why i gave you the dictionary. Anyway i can check any other ancient item you may have...<br><br><a action=\"bypass -h Quest _376_GiantsExploration1 show\">Show him the scrolls you collected</a><br><a action=\"bypass -h Quest _376_GiantsExploration1 myst\">Show him another items you've found</a><br></body></html>";
		}
		else if(npcId == 30182 && cond == 2 & st.getQuestItemsCount(5890) > 0L)
		{
			htmltext = "<html><head><body>Warehouse Freightman Cliff:<br><br>What is that book? Sobling told you to bring it to me? Well... It's written in a very ancient language.. yes.. Humm.. Take this \"Ancient Language Dictionary: Intermediate Level\" and meet <font color=\"LEVEL\">Sobling</font> again. With this he will be able to translate it. Leave now.</body></html>";
			st.takeItems(5890, 1L);
			st.giveItems(5892, 1L);
			st.set("cond", "3");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		st.rollAndGive(5944, 1, 1, 5.0);
		if(st.getInt("cond") == 1)
			st.rollAndGive(5890, 1, 1, 1, 8.0);
		return null;
	}

	static
	{
		EXCHANGE = new int[][] {
				{ 5937, 5938, 5939, 5940, 5941 },
				{ 5346, 5354 },
				{ 5932, 5933, 5934, 5935, 5936 },
				{ 5332, 5334 },
				{ 5922, 5923, 5924, 5925, 5926 },
				{ 5416, 5418 },
				{ 5927, 5928, 5929, 5930, 5931 },
				{ 5424, 5340 } };
	}
}
