package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _365_DevilsLegacy extends Quest implements ScriptFile
{
	private static final int RANDOLF = 30095;
	int[] MOBS;
	private static final int CHANCE_OF_DROP = 25;
	private static final int REWARD_PER_ONE = 5070;
	private static final int TREASURE_CHEST = 5873;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _365_DevilsLegacy()
	{
		super(false);
		MOBS = new int[] { 20836, 29027, 20845, 21629, 21630, 29026 };
		this.addStartNpc(30095);
		this.addKillId(MOBS);
		addQuestItem(new int[] { 5873 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30095-1.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30095-5.htm"))
		{
			final long count = st.getQuestItemsCount(5873);
			if(count > 0L)
			{
				final long reward = count * 5070L;
				st.takeItems(5873, -1L);
				st.giveItems(57, reward);
			}
			else
				htmltext = "You don't have required items";
		}
		else if(event.equalsIgnoreCase("30095-6.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() >= 39)
				htmltext = "30095-0.htm";
			else
			{
				htmltext = "30095-0a.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 1)
			if(st.getQuestItemsCount(5873) == 0L)
				htmltext = "30095-2.htm";
			else
				htmltext = "30095-4.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(Rnd.chance(25))
		{
			st.giveItems(5873, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
