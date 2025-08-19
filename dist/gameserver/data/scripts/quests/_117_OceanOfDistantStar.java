package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _117_OceanOfDistantStar extends Quest implements ScriptFile
{
	private static final int Abey = 32053;
	private static final int GhostEngineer = 32055;
	private static final int Obi = 32052;
	private static final int GhostEngineer2 = 32054;
	private static final int Box = 32076;
	private static final int BookOfGreyStar = 8495;
	private static final int EngravedHammer = 8488;
	private static final int BanditWarrior = 22023;
	private static final int BanditInspector = 22024;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _117_OceanOfDistantStar()
	{
		super(false);
		this.addStartNpc(32053);
		this.addTalkId(new int[] { 32055 });
		this.addTalkId(new int[] { 32052 });
		this.addTalkId(new int[] { 32076 });
		this.addTalkId(new int[] { 32054 });
		this.addKillId(new int[] { 22023 });
		this.addKillId(new int[] { 22024 });
		addQuestItem(new int[] { 8495, 8488 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("railman_abu_q0117_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("ghost_of_railroadman2_q0117_0201.htm"))
			st.set("cond", "2");
		else if(event.equalsIgnoreCase("railman_obi_q0117_0301.htm"))
			st.set("cond", "3");
		else if(event.equalsIgnoreCase("railman_abu_q0117_0401.htm"))
			st.set("cond", "4");
		else if(event.equalsIgnoreCase("q_box_of_railroad_q0117_0501.htm"))
		{
			st.set("cond", "5");
			st.giveItems(8488, 1L);
		}
		else if(event.equalsIgnoreCase("railman_abu_q0117_0601.htm"))
			st.set("cond", "6");
		else if(event.equalsIgnoreCase("railman_obi_q0117_0701.htm"))
			st.set("cond", "7");
		else if(event.equalsIgnoreCase("railman_obi_q0117_0801.htm"))
		{
			st.takeItems(8495, -1L);
			st.set("cond", "9");
		}
		else if(event.equalsIgnoreCase("ghost_of_railroadman2_q0117_0901.htm"))
		{
			st.takeItems(8488, -1L);
			st.set("cond", "10");
		}
		else if(event.equalsIgnoreCase("ghost_of_railroadman_q0117_1002.htm"))
		{
			st.giveItems(57, 17647L, true);
			st.addExpAndSp(107387L, 7369L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		int cond = 0;
		if(id != 1)
			cond = st.getInt("cond");
		if(npcId == 32053)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 39)
					htmltext = "railman_abu_q0117_0101.htm";
				else
				{
					htmltext = "railman_abu_q0117_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 3)
				htmltext = "railman_abu_q0117_0301.htm";
			else if(cond == 5 && st.getQuestItemsCount(8488) > 0L)
				htmltext = "railman_abu_q0117_0501.htm";
			else if(cond == 6 && st.getQuestItemsCount(8488) > 0L)
				htmltext = "railman_abu_q0117_0601.htm";
		}
		else if(npcId == 32055)
		{
			if(cond == 1)
				htmltext = "ghost_of_railroadman2_q0117_0101.htm";
			else if(cond == 9 && st.getQuestItemsCount(8488) > 0L)
				htmltext = "ghost_of_railroadman2_q0117_0801.htm";
		}
		else if(npcId == 32052)
		{
			if(cond == 2)
				htmltext = "railman_obi_q0117_0201.htm";
			else if(cond == 6 && st.getQuestItemsCount(8488) > 0L)
				htmltext = "railman_obi_q0117_0601.htm";
			else if(cond == 7 && st.getQuestItemsCount(8488) > 0L)
				htmltext = "railman_obi_q0117_0701.htm";
			else if(cond == 8 && st.getQuestItemsCount(8495) > 0L)
				htmltext = "railman_obi_q0117_0704.htm";
		}
		else if(npcId == 32076 && cond == 4)
			htmltext = "q_box_of_railroad_q0117_0401.htm";
		else if(npcId == 32054 && cond == 10)
			htmltext = "ghost_of_railroadman_q0117_0901.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 7 && Rnd.chance(30))
		{
			if(st.getQuestItemsCount(8495) < 1L)
			{
				st.giveItems(8495, 1L);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			st.set("cond", "8");
			st.setState(2);
		}
		return null;
	}
}
