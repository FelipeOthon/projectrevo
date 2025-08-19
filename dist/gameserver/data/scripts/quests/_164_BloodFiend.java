package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _164_BloodFiend extends Quest implements ScriptFile
{
	private static final int Creamees = 30149;
	private static final int KirunakSkull = 1044;
	private static final int Kirunak = 27021;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _164_BloodFiend()
	{
		super(false);
		this.addStartNpc(30149);
		this.addTalkId(new int[] { 30149 });
		this.addKillId(new int[] { 27021 });
		addQuestItem(new int[] { 1044 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30149-04.htm"))
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
		if(npcId == 30149)
			if(cond == 0)
			{
				if(st.getPlayer().getRace() == Race.darkelf)
				{
					htmltext = "30149-00.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() < 21)
				{
					htmltext = "30149-02.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "30149-03.htm";
			}
			else if(cond == 1)
				htmltext = "30149-05.htm";
			else if(cond == 2)
			{
				st.takeItems(1044, -1L);
				st.giveItems(57, 42130L, true);
				st.addExpAndSp(35637L, 1854L);
				htmltext = "30149-06.htm";
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
		if(cond == 1 && npcId == 27021)
		{
			if(st.getQuestItemsCount(1044) == 0L)
				st.giveItems(1044, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
			st.set("cond", "2");
			st.setState(2);
		}
		return null;
	}
}
