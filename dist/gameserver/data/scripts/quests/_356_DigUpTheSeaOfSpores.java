package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _356_DigUpTheSeaOfSpores extends Quest implements ScriptFile
{
	private static final int GAUEN = 30717;
	private static final int SPORE_ZOMBIE = 20562;
	private static final int ROTTING_TREE = 20558;
	private static final int CARNIVORE_SPORE = 5865;
	private static final int HERBIBOROUS_SPORE = 5866;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _356_DigUpTheSeaOfSpores()
	{
		super(false);
		this.addStartNpc(30717);
		this.addKillId(new int[] { 20562 });
		this.addKillId(new int[] { 20558 });
		addQuestItem(new int[] { 5865 });
		addQuestItem(new int[] { 5866 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final long carn = st.getQuestItemsCount(5865);
		final long herb = st.getQuestItemsCount(5866);
		if(event.equalsIgnoreCase("magister_gauen_q0356_06.htm"))
		{
			if(st.getPlayer().getLevel() >= 43)
			{
				st.set("cond", "1");
				st.setState(2);
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else
			{
				htmltext = "magister_gauen_q0356_01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if((event.equalsIgnoreCase("magister_gauen_q0356_20.htm") || event.equalsIgnoreCase("magister_gauen_q0356_17.htm")) && carn >= 50L && herb >= 50L)
		{
			st.takeItems(5865, -1L);
			st.takeItems(5866, -1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
			if(event.equalsIgnoreCase("magister_gauen_q0356_17.htm"))
				st.giveItems(57, 44000L);
			else
				st.addExpAndSp(36000L, 2600L);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
			htmltext = "magister_gauen_q0356_02.htm";
		else if(cond != 3)
			htmltext = "magister_gauen_q0356_07.htm";
		else if(cond == 3)
			htmltext = "magister_gauen_q0356_10.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final long carn = st.getQuestItemsCount(5865);
		final long herb = st.getQuestItemsCount(5866);
		if(npcId == 20562)
		{
			if(carn < 50L)
			{
				st.giveItems(5865, 1L);
				if(carn == 49L)
				{
					st.playSound(Quest.SOUND_MIDDLE);
					if(herb >= 50L)
					{
						st.set("cond", "3");
						st.setState(2);
					}
				}
				else
					st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		else if(npcId == 20558 && herb < 50L)
		{
			st.giveItems(5866, 1L);
			if(herb == 49L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				if(carn >= 50L)
				{
					st.set("cond", "3");
					st.setState(2);
				}
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
