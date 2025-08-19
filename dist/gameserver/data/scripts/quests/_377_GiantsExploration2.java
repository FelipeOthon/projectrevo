package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _377_GiantsExploration2 extends Quest implements ScriptFile
{
	private static final int DROP_RATE = 20;
	private static final int ANC_BOOK = 5955;
	private static final int DICT2 = 5892;
	private static final int[][] EXCHANGE;
	private static final String error_1 = "<html><head><body>Head Researcher Sobling:<br><br>I think it is too early for you to help me. Come back after you have gained some more experience. <br><font color=\"LEVEL\">(Quest for characters level 57 and above.)</font></body></html>";
	private static final String start = "<html><head><body>Head Researcher Sobling:<br><br>So Cliff sent us this dictionary, i can see clearly now. It's very impressive... There are more relics for we to find out and maybe you will help us as a future member of our excavation team. We should look for <font color=\"LEVEL\">The book of the Titan's science, and the Book of the Titan's Culture.</font><br><br>Our payment for such a discovery cannot be rejected so easily, <font color=\"LEVEL\">A grade recipes</font> used in the manufacture of top level armors... Of course i won't give you anything just for fragments, you will have to gather every piece of a given book.<br><br><a action=\"bypass -h Quest _377_GiantsExploration2 yes\">I will search for ancient books</a><br><a action=\"bypass -h Quest _377_GiantsExploration2 0\">I won't help you this time</a><br></body></html>";
	private static final String starting = "Starting.htm";
	private static final String checkout = "<html><head><body>Head Researcher Sobling:<br><br>Excellent! You came back! Was it difficult to collect ancient books?<br><br>Let me see what you've found thus far...<br><br><a action=\"bypass -h Quest _377_GiantsExploration2 show\">Show him the books you collected</a></body></html>";
	private static final String checkout2 = "<html><head><body>Head Researcher Sobling:<br><br>Excellent! You came back! Was it difficult to collect ancient books?<br><br>Hum... what is it? You have some untranslated book fragments here, but those are of no use to me until you translate its contents, i have no time to do it on my own and that's why i gave you the dictionary Cliff sent to me. Anyway i can check any other translated fragments you may have...<br><br><a action=\"bypass -h Quest _377_GiantsExploration2 show\">Show him the fragments</a></body></html>";
	private static final String no_items = "<html><head><body>Head Researcher Sobling:<br><br>Hum... I don't see any valuable or complete book here, you should continue your research. I'm pretty sure you can do it better if you put more effort... what you think?<br><br><a action=\"bypass -h Quest _377_GiantsExploration2 Starting.htm\">I will continue</a><br><a action=\"bypass -h Quest _377_GiantsExploration2 0\">I will quit</a><br></body></html>";
	private static final String tnx4items = "<html><head><body>Head Researcher Sobling:<br><br>Amazing! These are the sort of items i was looking for... Take this rare recipes as a proof of my gratitude. Anyhow, I'm sure there are more ancient relics guarded by those monsters, would you like to search some more?<br><br><a action=\"bypass -h Quest _377_GiantsExploration2 Starting.htm\">I will continue</a><br><a action=\"bypass -h Quest _377_GiantsExploration2 0\">I will quit</a><br></body></html>";
	private static final String ext_msg = "Quest aborted";
	private static final int HR_SOBLING = 31147;
	private static final int[] MOBS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _377_GiantsExploration2()
	{
		super(true);
		this.addStartNpc(31147);
		this.addKillId(_377_GiantsExploration2.MOBS);
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("yes"))
		{
			htmltext = "Starting.htm";
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("0"))
		{
			htmltext = "Quest aborted";
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("show"))
		{
			htmltext = "<html><head><body>Head Researcher Sobling:<br><br>Hum... I don't see any valuable or complete book here, you should continue your research. I'm pretty sure you can do it better if you put more effort... what you think?<br><br><a action=\"bypass -h Quest _377_GiantsExploration2 Starting.htm\">I will continue</a><br><a action=\"bypass -h Quest _377_GiantsExploration2 0\">I will quit</a><br></body></html>";
			for(final int[] i : _377_GiantsExploration2.EXCHANGE)
			{
				long count = Long.MAX_VALUE;
				for(final int j : i)
					count = Math.min(count, st.getQuestItemsCount(j));
				if(count > 0L)
				{
					htmltext = "<html><head><body>Head Researcher Sobling:<br><br>Amazing! These are the sort of items i was looking for... Take this rare recipes as a proof of my gratitude. Anyhow, I'm sure there are more ancient relics guarded by those monsters, would you like to search some more?<br><br><a action=\"bypass -h Quest _377_GiantsExploration2 Starting.htm\">I will continue</a><br><a action=\"bypass -h Quest _377_GiantsExploration2 0\">I will quit</a><br></body></html>";
					for(final int j : i)
						st.takeItems(j, count);
					for(int n = 0; n < count; ++n)
					{
						final int luck = Rnd.get(100);
						int item = 0;
						if(luck > 75)
							item = 5420;
						else if(luck > 50)
							item = 5422;
						else if(luck > 25)
							item = 5336;
						else
							item = 5338;
						if(Config.ALT_100_RECIPES_A)
							++item;
						st.giveItems(item, 1L);
					}
				}
			}
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int id = st.getState();
		if(st.getQuestItemsCount(5892) == 0L)
			st.exitCurrentQuest(true);
		else if(id == 1)
		{
			htmltext = "<html><head><body>Head Researcher Sobling:<br><br>So Cliff sent us this dictionary, i can see clearly now. It's very impressive... There are more relics for we to find out and maybe you will help us as a future member of our excavation team. We should look for <font color=\"LEVEL\">The book of the Titan's science, and the Book of the Titan's Culture.</font><br><br>Our payment for such a discovery cannot be rejected so easily, <font color=\"LEVEL\">A grade recipes</font> used in the manufacture of top level armors... Of course i won't give you anything just for fragments, you will have to gather every piece of a given book.<br><br><a action=\"bypass -h Quest _377_GiantsExploration2 yes\">I will search for ancient books</a><br><a action=\"bypass -h Quest _377_GiantsExploration2 0\">I won't help you this time</a><br></body></html>";
			if(st.getPlayer().getLevel() < 57)
			{
				st.exitCurrentQuest(true);
				htmltext = "<html><head><body>Head Researcher Sobling:<br><br>I think it is too early for you to help me. Come back after you have gained some more experience. <br><font color=\"LEVEL\">(Quest for characters level 57 and above.)</font></body></html>";
			}
		}
		else if(id == 2)
			if(st.getQuestItemsCount(5955) == 0L)
				htmltext = "<html><head><body>Head Researcher Sobling:<br><br>Excellent! You came back! Was it difficult to collect ancient books?<br><br>Let me see what you've found thus far...<br><br><a action=\"bypass -h Quest _377_GiantsExploration2 show\">Show him the books you collected</a></body></html>";
			else
				htmltext = "<html><head><body>Head Researcher Sobling:<br><br>Excellent! You came back! Was it difficult to collect ancient books?<br><br>Hum... what is it? You have some untranslated book fragments here, but those are of no use to me until you translate its contents, i have no time to do it on my own and that's why i gave you the dictionary Cliff sent to me. Anyway i can check any other translated fragments you may have...<br><br><a action=\"bypass -h Quest _377_GiantsExploration2 show\">Show him the fragments</a></body></html>";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		st.rollAndGive(5955, 1, 20.0);
		return null;
	}

	static
	{
		EXCHANGE = new int[][] { { 5945, 5946, 5947, 5948, 5949 }, { 5950, 5951, 5952, 5953, 5954 } };
		MOBS = new int[] { 20654, 20656, 20657, 20658 };
	}
}
