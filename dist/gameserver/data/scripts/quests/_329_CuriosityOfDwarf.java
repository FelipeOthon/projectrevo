package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _329_CuriosityOfDwarf extends Quest implements ScriptFile
{
	private int GOLEM_HEARTSTONE;
	private int BROKEN_HEARTSTONE;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _329_CuriosityOfDwarf()
	{
		super(false);
		GOLEM_HEARTSTONE = 1346;
		BROKEN_HEARTSTONE = 1365;
		this.addStartNpc(30437);
		this.addKillId(new int[] { 20083 });
		this.addKillId(new int[] { 20085 });
		addQuestItem(new int[] { BROKEN_HEARTSTONE });
		addQuestItem(new int[] { GOLEM_HEARTSTONE });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("trader_rolento_q0329_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("trader_rolento_q0329_06.htm"))
		{
			st.exitCurrentQuest(true);
			st.playSound(Quest.SOUND_FINISH);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int id = st.getState();
		if(id == 1)
			st.set("cond", "0");
		String htmltext;
		if(st.getInt("cond") == 0)
		{
			if(st.getPlayer().getLevel() >= 33)
				htmltext = "trader_rolento_q0329_02.htm";
			else
			{
				htmltext = "trader_rolento_q0329_01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else
		{
			final long heart = st.getQuestItemsCount(GOLEM_HEARTSTONE);
			final long broken = st.getQuestItemsCount(BROKEN_HEARTSTONE);
			if(broken + heart > 0L)
			{
				st.giveItems(57, 50L * broken + 1000L * heart);
				st.takeItems(BROKEN_HEARTSTONE, -1L);
				st.takeItems(GOLEM_HEARTSTONE, -1L);
				htmltext = "trader_rolento_q0329_05.htm";
			}
			else
				htmltext = "trader_rolento_q0329_04.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int n = Rnd.get(1, 100);
		if(npcId == 20085)
		{
			if(n < 5)
			{
				st.giveItems(GOLEM_HEARTSTONE, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			else if(n < 58)
			{
				st.giveItems(BROKEN_HEARTSTONE, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		else if(npcId == 20083)
			if(n < 6)
			{
				st.giveItems(GOLEM_HEARTSTONE, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			else if(n < 56)
			{
				st.giveItems(BROKEN_HEARTSTONE, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
		return null;
	}
}
