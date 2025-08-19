package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _603_DaimontheWhiteEyedPart1 extends Quest implements ScriptFile
{
	private static final int EYE = 31683;
	private static final int TABLE1 = 31548;
	private static final int TABLE2 = 31549;
	private static final int TABLE3 = 31550;
	private static final int TABLE4 = 31551;
	private static final int TABLE5 = 31552;
	private static final int BUFFALO = 21299;
	private static final int BANDERSNATCH = 21297;
	private static final int GRENDEL = 21304;
	private static final int EVIL_SPIRIT = 7190;
	private static final int BROKEN_CRYSTAL = 7191;
	private static final int U_SUMMON = 7192;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _603_DaimontheWhiteEyedPart1()
	{
		super(true);
		this.addStartNpc(31683);
		this.addTalkId(new int[] { 31548 });
		this.addTalkId(new int[] { 31549 });
		this.addTalkId(new int[] { 31550 });
		this.addTalkId(new int[] { 31551 });
		this.addTalkId(new int[] { 31552 });
		this.addKillId(new int[] { 21299 });
		this.addKillId(new int[] { 21297 });
		this.addKillId(new int[] { 21304 });
		addQuestItem(new int[] { 7190 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("31683-02.htm"))
		{
			if(st.getPlayer().getLevel() < 73)
			{
				htmltext = "31683-01a.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				st.set("cond", "1");
				st.setState(2);
				st.playSound("ItemSound.quest_accept");
			}
		}
		else if(event.equalsIgnoreCase("31548-02.htm"))
		{
			st.set("cond", "2");
			st.setState(2);
			st.playSound("ItemSound.quest_middle");
			st.giveItems(7191, 1L);
		}
		else if(event.equalsIgnoreCase("31549-02.htm"))
		{
			st.set("cond", "3");
			st.setState(2);
			st.playSound("ItemSound.quest_middle");
			st.giveItems(7191, 1L);
		}
		else if(event.equalsIgnoreCase("31550-02.htm"))
		{
			st.set("cond", "4");
			st.setState(2);
			st.playSound("ItemSound.quest_middle");
			st.giveItems(7191, 1L);
		}
		else if(event.equalsIgnoreCase("31551-02.htm"))
		{
			st.set("cond", "5");
			st.setState(2);
			st.playSound("ItemSound.quest_middle");
			st.giveItems(7191, 1L);
		}
		else if(event.equalsIgnoreCase("31552-02.htm"))
		{
			st.set("cond", "6");
			st.setState(2);
			st.playSound("ItemSound.quest_middle");
			st.giveItems(7191, 1L);
		}
		else if(event.equalsIgnoreCase("31683-04.htm"))
		{
			if(st.getQuestItemsCount(7191) < 5L)
				htmltext = "31683-08.htm";
			else
			{
				st.set("cond", "7");
				st.setState(2);
				st.takeItems(7191, -1L);
				st.playSound("ItemSound.quest_middle");
			}
		}
		else if(event.equalsIgnoreCase("31683-07.htm"))
			if(st.getQuestItemsCount(7190) < 200L)
				htmltext = "31683-09.htm";
			else
			{
				st.takeItems(7190, -1L);
				st.giveItems(7192, 1L);
				st.playSound("ItemSound.quest_finish");
				st.exitCurrentQuest(true);
			}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "<html><body>You are either not on a quest that involves this NPC, or you don't meet this NPC's minimum quest requirements.</body></html>";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(npcId == 31683)
				htmltext = "31683-01.htm";
		}
		else if(cond == 1)
		{
			if(npcId == 31683)
				htmltext = "31683-02a.htm";
			else if(npcId == 31548)
				htmltext = "31548-01.htm";
		}
		else if(cond == 2)
		{
			if(npcId == 31683)
				htmltext = "31683-02a.htm";
			else if(npcId == 31549)
				htmltext = "31549-01.htm";
			else
				htmltext = "table-no.htm";
		}
		else if(cond == 3)
		{
			if(npcId == 31683)
				htmltext = "31683-02a.htm";
			else if(npcId == 31550)
				htmltext = "31550-01.htm";
			else
				htmltext = "table-no.htm";
		}
		else if(cond == 4)
		{
			if(npcId == 31683)
				htmltext = "31683-02a.htm";
			else if(npcId == 31551)
				htmltext = "31551-01.htm";
			else
				htmltext = "table-no.htm";
		}
		else if(cond == 5)
		{
			if(npcId == 31683)
				htmltext = "31683-02a.htm";
			else if(npcId == 31552)
				htmltext = "31552-01.htm";
			else
				htmltext = "table-no.htm";
		}
		else if(cond == 6)
		{
			if(npcId == 31683)
				htmltext = "31683-03.htm";
			else
				htmltext = "table-no.htm";
		}
		else if(cond == 7)
		{
			if(npcId == 31683)
				htmltext = "31683-05.htm";
		}
		else if(cond == 8 && npcId == 31683)
			htmltext = "31683-06.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		st.rollAndGive(7190, 1, 1, 200, 100.0);
		if(st.getQuestItemsCount(7190) == 200L)
		{
			st.set("cond", "8");
			st.setState(2);
		}
		return null;
	}
}
