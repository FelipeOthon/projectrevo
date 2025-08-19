package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.ScriptFile;

public class _261_CollectorsDream extends Quest implements ScriptFile
{
	int GIANT_SPIDER_LEG;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _261_CollectorsDream()
	{
		super(false);
		GIANT_SPIDER_LEG = 1087;
		this.addStartNpc(30222);
		this.addTalkId(new int[] { 30222 });
		this.addKillId(new int[] { 20308 });
		this.addKillId(new int[] { 20460 });
		this.addKillId(new int[] { 20466 });
		addQuestItem(new int[] { GIANT_SPIDER_LEG });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.intern().equalsIgnoreCase("moneylender_alshupes_q0261_03.htm"))
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
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() >= 15)
			{
				htmltext = "moneylender_alshupes_q0261_02.htm";
				return htmltext;
			}
			htmltext = "moneylender_alshupes_q0261_01.htm";
			st.exitCurrentQuest(true);
		}
		else if(cond == 1 || st.getQuestItemsCount(GIANT_SPIDER_LEG) < 8L)
			htmltext = "moneylender_alshupes_q0261_04.htm";
		else if(cond == 2 && st.getQuestItemsCount(GIANT_SPIDER_LEG) >= 8L)
		{
			st.takeItems(GIANT_SPIDER_LEG, -1L);
			st.giveItems(57, 1000L);
			st.addExpAndSp(2000L, 0L);
			if(st.getPlayer().getClassId().getLevel() == 1 && !st.getPlayer().getVarBoolean("p1q4"))
			{
				st.getPlayer().setVar("p1q4", "1");
				st.getPlayer().sendPacket(new ExShowScreenMessage("Now go find the Newbie Guide.", 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
			}
			htmltext = "moneylender_alshupes_q0261_05.htm";
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1 && st.getQuestItemsCount(GIANT_SPIDER_LEG) < 8L)
		{
			st.giveItems(GIANT_SPIDER_LEG, 1L);
			if(st.getQuestItemsCount(GIANT_SPIDER_LEG) == 8L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
