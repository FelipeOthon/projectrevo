package quests;

import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _385_YokeOfThePast extends Quest implements ScriptFile
{
	final short ANCIENT_SCROLL = 5902;
	final short BLANK_SCROLL = 5965;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _385_YokeOfThePast()
	{
		super(true);
		for(int npcId = 31095; npcId <= 31126; ++npcId)
			if(npcId != 31111 && npcId != 31112 && npcId != 31113)
				this.addStartNpc(npcId);
		for(int mobs = 21208; mobs < 21256; ++mobs)
			this.addKillId(new int[] { mobs });
		addQuestItem(new int[] { 5902 });
	}

	public boolean checkNPC(final int npc)
	{
		return npc >= 31095 && npc <= 31126 && npc != 31100 && npc != 31111 && npc != 31112 && npc != 31113;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("enter_necropolis1_q0385_05.htm"))
		{
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.set("cond", "1");
		}
		else if(event.equalsIgnoreCase("enter_necropolis1_q0385_09.htm"))
		{
			htmltext = "enter_necropolis1_q0385_10.htm";
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final double rand = 60.0 * Experience.penaltyModifier(st.calculateLevelDiffForDrop(npc.getLevel(), st.getPlayer().getLevel()), 9.0) * npc.getTemplate().rateHp / 4.0;
		st.rollAndGive(5902, 1, rand);
		return null;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if(checkNPC(npcId) && st.getInt("cond") == 0)
		{
			if(st.getPlayer().getLevel() < 20)
			{
				htmltext = "enter_necropolis1_q0385_02.htm";
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "enter_necropolis1_q0385_01.htm";
		}
		else if(st.getInt("cond") == 1 && st.getQuestItemsCount(5902) == 0L)
			htmltext = "enter_necropolis1_q0385_11.htm";
		else if(st.getInt("cond") == 1 && st.getQuestItemsCount(5902) > 0L)
		{
			htmltext = "enter_necropolis1_q0385_09.htm";
			st.giveItems(5965, st.getQuestItemsCount(5902));
			st.takeItems(5902, -1L);
		}
		else
			st.exitCurrentQuest(true);
		return htmltext;
	}
}
