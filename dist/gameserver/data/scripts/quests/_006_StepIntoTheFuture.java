package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _006_StepIntoTheFuture extends Quest implements ScriptFile
{
	private static final int Roxxy = 30006;
	private static final int Baulro = 30033;
	private static final int Windawood = 30311;
	private static final int BaulrosLetter = 7571;
	private static final int ScrollOfEscapeGiran = 7126;
	private static final int MarkOfTraveler = 7570;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _006_StepIntoTheFuture()
	{
		super(false);
		this.addStartNpc(30006);
		this.addTalkId(new int[] { 30033 });
		this.addTalkId(new int[] { 30311 });
		addQuestItem(new int[] { 7571 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("rapunzel_q0006_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("baul_q0006_0201.htm"))
		{
			st.giveItems(7571, 1L, false);
			st.set("cond", "2");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("sir_collin_windawood_q0006_0301.htm"))
		{
			st.takeItems(7571, -1L);
			st.set("cond", "3");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("rapunzel_q0006_0401.htm"))
		{
			st.giveItems(7126, 1L, false);
			st.giveItems(7570, 1L, false);
			st.unset("cond");
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30006)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getRace() == Race.human && st.getPlayer().getLevel() >= 3)
					htmltext = "rapunzel_q0006_0101.htm";
				else
				{
					htmltext = "rapunzel_q0006_0102.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "rapunzel_q0006_0105.htm";
			else if(cond == 3)
				htmltext = "rapunzel_q0006_0301.htm";
		}
		else if(npcId == 30033)
		{
			if(cond == 1)
				htmltext = "baul_q0006_0101.htm";
			else if(cond == 2 && st.getQuestItemsCount(7571) > 0L)
				htmltext = "baul_q0006_0202.htm";
		}
		else if(npcId == 30311)
			if(cond == 2 && st.getQuestItemsCount(7571) > 0L)
				htmltext = "sir_collin_windawood_q0006_0201.htm";
			else if(cond == 2 && st.getQuestItemsCount(7571) == 0L)
				htmltext = "sir_collin_windawood_q0006_0302.htm";
			else if(cond == 3)
				htmltext = "sir_collin_windawood_q0006_0303.htm";
		return htmltext;
	}
}
