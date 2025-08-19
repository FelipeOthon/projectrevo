package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _110_ToThePrimevalIsle extends Quest implements ScriptFile
{
	int ANTON;
	int MARQUEZ;
	int ANCIENT_BOOK;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _110_ToThePrimevalIsle()
	{
		super(false);
		ANTON = 31338;
		MARQUEZ = 32113;
		ANCIENT_BOOK = 8777;
		this.addStartNpc(ANTON);
		this.addTalkId(new int[] { ANTON });
		this.addTalkId(new int[] { MARQUEZ });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("1"))
		{
			htmltext = "scroll_seller_anton_q0110_05.htm";
			st.set("cond", "1");
			st.giveItems(ANCIENT_BOOK, 1L);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("2") && st.getQuestItemsCount(ANCIENT_BOOK) > 0L)
		{
			htmltext = "marquez_q0110_05.htm";
			st.playSound(Quest.SOUND_FINISH);
			st.giveItems(57, 191678L);
			st.addExpAndSp(251602L, 25242L);
			st.takeItems(ANCIENT_BOOK, -1L);
			st.exitCurrentQuest(false);
		}
		else if(event.equals("3"))
		{
			htmltext = "marquez_q0110_06.htm";
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		final int cond = st.getInt("cond");
		if(id == 1)
		{
			if(st.getPlayer().getLevel() >= 75)
				htmltext = "scroll_seller_anton_q0110_01.htm";
			else
			{
				st.exitCurrentQuest(true);
				htmltext = "scroll_seller_anton_q0110_02.htm";
			}
		}
		else if(npcId == ANTON)
		{
			if(cond == 1)
				htmltext = "scroll_seller_anton_q0110_07.htm";
		}
		else if(id == 2 && npcId == MARQUEZ && cond == 1)
			if(st.getQuestItemsCount(ANCIENT_BOOK) == 0L)
				htmltext = "marquez_q0110_07.htm";
			else
				htmltext = "marquez_q0110_01.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		return null;
	}
}
