package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _637_ThroughOnceMore extends Quest implements ScriptFile
{
	public final int CHANCE = 40;
	public final int FLAURON = 32010;
	public final int VISITOR_MARK = 8064;
	public final int FADED_MARK = 8065;
	public final int NECROHEART = 8066;
	public final int PERMANENT_MARK = 8067;
	public final int ANTEROOM_KEY = 8273;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _637_ThroughOnceMore()
	{
		super(false);
		this.addStartNpc(32010);
		this.addKillId(new int[] { 21565, 21566, 21567, 21568 });
		addQuestItem(new int[] { 8066 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("falsepriest_flauron_q0637_11.htm"))
		{
			st.setCond(1);
			st.takeItems(8064, 1L);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("falsepriest_flauron_q0637_09.htm"))
			st.exitCurrentQuest(true);
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getCond();
		String htmltext;
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() > 72 && st.getQuestItemsCount(8065) > 0L)
				htmltext = "falsepriest_flauron_q0637_01.htm";
			else if(st.getPlayer().getLevel() > 72 && st.getQuestItemsCount(8064) < 0L)
			{
				htmltext = "falsepriest_flauron_q0637_03.htm";
				st.exitCurrentQuest(true);
			}
			else if(st.getPlayer().getLevel() > 72 && st.getQuestItemsCount(8067) < 0L)
			{
				htmltext = "falsepriest_flauron_q0637_05.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "falsepriest_flauron_q0637_04.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 2 && st.getQuestItemsCount(8066) >= 10L)
		{
			htmltext = "falsepriest_flauron_q0637_13.htm";
			st.takeItems(8066, -1L);
			st.takeItems(8065, -1L);
			st.takeItems(8064, -1L);
			st.giveItems(8067, 1L);
			st.giveItems(8273, 10L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else
			htmltext = "falsepriest_flauron_q0637_12.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final long count = st.getQuestItemsCount(8066);
		if(st.getCond() == 1 && Rnd.chance(40) && count < 10L)
		{
			st.giveItems(8066, 1L);
			if(count == 9L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.setCond(2);
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}

	@Override
	public void onAbort(final QuestState st)
	{
		if(st.getQuestItemsCount(8064) == 0L)
			st.giveItems(8064, 1L);
	}
}
