package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _347_GoGetTheCalculator extends Quest implements ScriptFile
{
	public final int BRUNON = 30526;
	public final int SILVERA = 30527;
	public final int SPIRON = 30532;
	public final int BALANKI = 30533;
	public final int GEMSTONE_BEAST = 20540;
	public final int GEMSTONE_BEAST_CRYSTAL = 4286;
	public final int CALCULATOR_Q = 4285;
	public final int CALCULATOR = 4393;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _347_GoGetTheCalculator()
	{
		super(false);
		this.addStartNpc(30526);
		this.addTalkId(new int[] { 30527 });
		this.addTalkId(new int[] { 30532 });
		this.addTalkId(new int[] { 30533 });
		this.addKillId(new int[] { 20540 });
		addQuestItem(new int[] { 4286 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("1"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			htmltext = "30526-02.htm";
		}
		else if(event.equalsIgnoreCase("30533_1"))
		{
			if(st.getQuestItemsCount(57) > 100L)
			{
				st.takeItems(57, 100L);
				if(st.getInt("cond") == 1)
					st.set("cond", "2");
				else
					st.set("cond", "4");
				st.setState(2);
				htmltext = "30533-02.htm";
			}
			else
				htmltext = "30533-03.htm";
		}
		else if(event.equalsIgnoreCase("30532_1"))
		{
			htmltext = "30532-02a.htm";
			if(st.getInt("cond") == 1)
				st.set("cond", "3");
			else
				st.set("cond", "4");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("30532_2"))
			htmltext = "30532-02b.htm";
		else if(event.equalsIgnoreCase("30532_3"))
			htmltext = "30532-02c.htm";
		else if(event.equalsIgnoreCase("30526_1"))
		{
			st.giveItems(4393, 1L);
			st.takeItems(4285, 1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
			htmltext = "30526-05.htm";
		}
		else if(event.equalsIgnoreCase("30526_2"))
		{
			st.giveItems(57, 1000L, true);
			st.takeItems(4285, 1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
			htmltext = "30526-06.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		String htmltext = "noquest";
		if(npcId == 30526 && cond == 0 && st.getPlayer().getLevel() >= 12)
			htmltext = "30526-01.htm";
		else if(npcId == 30526 && cond > 0 && st.getQuestItemsCount(4285) == 0L)
			htmltext = "30526-03.htm";
		else if(npcId == 30526 && cond == 6 && st.getQuestItemsCount(4285) >= 1L)
			htmltext = "30526-04.htm";
		else if(npcId == 30533 && (cond == 1 || cond == 3))
			htmltext = "30533-01.htm";
		else if(npcId == 30532 && (cond == 1 || cond == 2))
			htmltext = "30532-01.htm";
		else if(npcId == 30527 && cond == 4)
		{
			st.set("cond", "5");
			st.setState(2);
			htmltext = "30527-01.htm";
		}
		else if(npcId == 30527 && cond == 5 && st.getQuestItemsCount(4286) < 10L)
			htmltext = "30527-02.htm";
		else if(npcId == 30527 && cond == 5 && st.getQuestItemsCount(4286) >= 10L)
		{
			htmltext = "30527-03.htm";
			st.takeItems(4286, 10L);
			st.giveItems(4285, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
			st.set("cond", "6");
			st.setState(2);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(npcId == 20540 && st.getInt("cond") == 5 && Rnd.chance(50) && st.getQuestItemsCount(4286) < 10L)
		{
			st.giveItems(4286, 1L);
			if(st.getQuestItemsCount(4286) >= 10L)
				st.playSound(Quest.SOUND_MIDDLE);
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
