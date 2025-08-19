package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _023_LidiasHeart extends Quest implements ScriptFile
{
	int Innocentin;
	int BrokenBookshelf;
	int GhostofvonHellmann;
	int Tombstone;
	int Violet;
	int Box;
	int MapForestofDeadman;
	int SilverKey;
	int LidiaHairPin;
	int LidiaDiary;
	int SilverSpear;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _023_LidiasHeart()
	{
		super(false);
		Innocentin = 31328;
		BrokenBookshelf = 31526;
		GhostofvonHellmann = 31524;
		Tombstone = 31523;
		Violet = 31386;
		Box = 31530;
		MapForestofDeadman = 7063;
		SilverKey = 7149;
		LidiaHairPin = 7148;
		LidiaDiary = 7064;
		SilverSpear = 7150;
		this.addStartNpc(Innocentin);
		this.addTalkId(new int[] { Innocentin });
		this.addTalkId(new int[] { BrokenBookshelf });
		this.addTalkId(new int[] { GhostofvonHellmann });
		this.addTalkId(new int[] { Tombstone });
		this.addTalkId(new int[] { Violet });
		this.addTalkId(new int[] { Box });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("31328-02.htm"))
		{
			st.giveItems(MapForestofDeadman, 1L);
			st.giveItems(SilverKey, 1L);
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("31328-03.htm"))
			st.set("cond", "2");
		else if(event.equals("31526-01.htm"))
			st.set("cond", "3");
		else if(event.equals("31526-05.htm"))
		{
			st.giveItems(LidiaHairPin, 1L);
			if(st.getQuestItemsCount(LidiaDiary) != 0L)
				st.set("cond", "4");
		}
		else if(event.equals("31526-11.htm"))
		{
			st.giveItems(LidiaDiary, 1L);
			if(st.getQuestItemsCount(LidiaHairPin) != 0L)
				st.set("cond", "4");
		}
		else if(event.equals("31328-19.htm"))
			st.set("cond", "6");
		else if(event.equals("31524-04.htm"))
		{
			st.set("cond", "7");
			st.takeItems(LidiaDiary, -1L);
		}
		else if(event.equals("31523-02.htm"))
			st.addSpawn(GhostofvonHellmann, 120000);
		else if(event.equals("31523-05.htm"))
			st.startQuestTimer("viwer_timer", 10000L);
		else if(event.equals("viwer_timer"))
		{
			st.set("cond", "8");
			htmltext = "31523-06.htm";
		}
		else if(event.equals("31530-02.htm"))
		{
			st.set("cond", "10");
			st.takeItems(SilverKey, -1L);
			st.giveItems(SilverSpear, 1L);
		}
		else if(event.equals("i7064-02.htm"))
			htmltext = "i7064-02.htm";
		else if(event.equals("31526-13.htm"))
			st.startQuestTimer("read_book", 120000L);
		else if(event.equals("read_book"))
			htmltext = "i7064.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == Innocentin)
		{
			if(cond == 0)
			{
				final QuestState TragedyInVonHellmannForest = st.getPlayer().getQuestState(22);
				if(TragedyInVonHellmannForest != null)
					if(TragedyInVonHellmannForest.isCompleted())
						htmltext = "31328-01.htm";
					else
						htmltext = "31328-00.htm";
			}
			else if(cond == 1)
				htmltext = "31328-03.htm";
			else if(cond == 2)
				htmltext = "31328-07.htm";
			else if(cond == 4)
				htmltext = "31328-08.htm";
			else if(cond == 6)
				htmltext = "31328-19.htm";
		}
		else if(npcId == BrokenBookshelf)
		{
			if(cond == 2)
			{
				if(st.getQuestItemsCount(SilverKey) != 0L)
					htmltext = "31526-00.htm";
			}
			else if(cond == 3)
			{
				if(st.getQuestItemsCount(LidiaHairPin) == 0L && st.getQuestItemsCount(LidiaDiary) != 0L)
					htmltext = "31526-12.htm";
				else if(st.getQuestItemsCount(LidiaHairPin) != 0L && st.getQuestItemsCount(LidiaDiary) == 0L)
					htmltext = "31526-06.htm";
				else if(st.getQuestItemsCount(LidiaHairPin) == 0L && st.getQuestItemsCount(LidiaDiary) == 0L)
					htmltext = "31526-02.htm";
			}
			else if(cond == 4)
				htmltext = "31526-13.htm";
		}
		else if(npcId == GhostofvonHellmann)
		{
			if(cond == 6)
				htmltext = "31524-01.htm";
			else if(cond == 7)
				htmltext = "31524-05.htm";
		}
		else if(npcId == Tombstone)
		{
			if(cond == 6)
				if(st.getQuestTimer("spawn_timer") != null)
					htmltext = "31523-03.htm";
				else
					htmltext = "31523-01.htm";
			if(cond == 7)
				htmltext = "31523-04.htm";
			else if(cond == 8)
				htmltext = "31523-06.htm";
		}
		else if(npcId == Violet)
		{
			if(cond == 8)
			{
				htmltext = "31386-01.htm";
				st.set("cond", "9");
			}
			else if(cond == 9)
				htmltext = "31386-02.htm";
			else if(cond == 10)
				if(st.getQuestItemsCount(SilverSpear) != 0L)
				{
					htmltext = "31386-03.htm";
					st.takeItems(SilverSpear, -1L);
					st.giveItems(57, 350000L);
					st.addExpAndSp(456893L, 42112L);
					st.playSound(Quest.SOUND_FINISH);
					st.exitCurrentQuest(false);
				}
				else
					htmltext = "You have no Silver Spear...";
		}
		else if(npcId == Box)
			if(cond == 9)
			{
				if(st.getQuestItemsCount(SilverKey) != 0L)
					htmltext = "31530-01.htm";
				else
					htmltext = "You have no key...";
			}
			else if(cond == 10)
				htmltext = "31386-03.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		return null;
	}
}
