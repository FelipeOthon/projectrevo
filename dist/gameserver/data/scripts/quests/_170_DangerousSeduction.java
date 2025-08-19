package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _170_DangerousSeduction extends Quest implements ScriptFile
{
	private static final int Vellior = 30305;
	private static final int NightmareCrystal = 1046;
	private static final int Merkenis = 27022;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _170_DangerousSeduction()
	{
		super(false);
		this.addStartNpc(30305);
		this.addTalkId(new int[] { 30305 });
		this.addKillId(new int[] { 27022 });
		addQuestItem(new int[] { 1046 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30305-04.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30305)
			if(cond == 0)
			{
				if(st.getPlayer().getRace() != Race.darkelf)
				{
					htmltext = "30305-00.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() < 21)
				{
					htmltext = "30305-02.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "30305-03.htm";
			}
			else if(cond == 1)
				htmltext = "30305-05.htm";
			else if(cond == 2)
			{
				st.takeItems(1046, -1L);
				st.giveItems(57, 102680L, true);
				st.addExpAndSp(38607L, 4018L);
				htmltext = "30305-06.htm";
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond == 1 && npcId == 27022)
		{
			if(st.getQuestItemsCount(1046) == 0L)
				st.giveItems(1046, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			st.set("cond", "2");
			st.setState(2);
		}
		return null;
	}
}
