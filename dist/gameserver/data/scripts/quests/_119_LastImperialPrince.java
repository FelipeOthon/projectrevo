package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _119_LastImperialPrince extends Quest implements ScriptFile
{
	private static final int SPIRIT = 31453;
	private static final int DEVORIN = 32009;
	private static final int BROOCH = 7262;
	private static final int AMOUNT = 68787;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _119_LastImperialPrince()
	{
		super(false);
		this.addStartNpc(31453);
		this.addTalkId(new int[] { 32009 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("31453-4.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("32009-2.htm"))
		{
			if(st.getQuestItemsCount(7262) < 1L)
			{
				htmltext = "noquest";
				st.exitCurrentQuest(true);
			}
		}
		else if(event.equalsIgnoreCase("32009-3.htm"))
		{
			st.set("cond", "2");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("31453-7.htm"))
		{
			st.giveItems(57, 68787L, true);
			st.addExpAndSp(902439L, 90067L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(st.getPlayer().getLevel() < 74)
		{
			htmltext = "<html><body>Quest for characters level 74 and above.</body></html>";
			st.exitCurrentQuest(true);
			return htmltext;
		}
		if(st.getQuestItemsCount(7262) < 1L)
		{
			htmltext = "noquest";
			st.exitCurrentQuest(true);
			return htmltext;
		}
		if(npcId != 31453)
		{
			if(npcId == 32009 && cond == 1)
				htmltext = "32009-1.htm";
			return htmltext;
		}
		if(cond == 0)
			return "31453-1.htm";
		if(cond == 2)
			return "31453-5.htm";
		return "noquest";
	}
}
