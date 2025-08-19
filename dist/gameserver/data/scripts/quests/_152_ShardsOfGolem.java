package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _152_ShardsOfGolem extends Quest implements ScriptFile
{
	int HARRYS_RECEIPT1;
	int HARRYS_RECEIPT2;
	int GOLEM_SHARD;
	int TOOL_BOX;
	int WOODEN_BP;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _152_ShardsOfGolem()
	{
		super(false);
		HARRYS_RECEIPT1 = 1008;
		HARRYS_RECEIPT2 = 1009;
		GOLEM_SHARD = 1010;
		TOOL_BOX = 1011;
		WOODEN_BP = 23;
		this.addStartNpc(30035);
		this.addTalkId(new int[] { 30035 });
		this.addTalkId(new int[] { 30035 });
		this.addTalkId(new int[] { 30283 });
		this.addTalkId(new int[] { 30035 });
		this.addKillId(new int[] { 20016 });
		this.addKillId(new int[] { 20101 });
		addQuestItem(new int[] { HARRYS_RECEIPT1, GOLEM_SHARD, TOOL_BOX, HARRYS_RECEIPT2 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("harry_q0152_04.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			if(st.getQuestItemsCount(HARRYS_RECEIPT1) == 0L)
				st.giveItems(HARRYS_RECEIPT1, 1L);
		}
		else if(event.equals("152_2"))
		{
			st.takeItems(HARRYS_RECEIPT1, -1L);
			if(st.getQuestItemsCount(HARRYS_RECEIPT2) == 0L)
			{
				st.giveItems(HARRYS_RECEIPT2, 1L);
				st.set("cond", "2");
			}
			htmltext = "blacksmith_alltran_q0152_02.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30035)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 10)
				{
					htmltext = "harry_q0152_03.htm";
					return htmltext;
				}
				htmltext = "harry_q0152_02.htm";
				st.exitCurrentQuest(true);
			}
			else if(cond == 1 && st.getQuestItemsCount(HARRYS_RECEIPT1) != 0L)
				htmltext = "harry_q0152_05.htm";
			else if(cond == 2 && st.getQuestItemsCount(HARRYS_RECEIPT2) != 0L)
				htmltext = "harry_q0152_05.htm";
			else if(cond == 4 && st.getQuestItemsCount(TOOL_BOX) != 0L)
			{
				st.takeItems(TOOL_BOX, -1L);
				st.takeItems(HARRYS_RECEIPT2, -1L);
				st.set("cond", "0");
				st.playSound(Quest.SOUND_FINISH);
				st.giveItems(WOODEN_BP, 1L);
				st.addExpAndSp(5000L, 0L);
				htmltext = "harry_q0152_06.htm";
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 30283)
		{
			if(cond == 1 && st.getQuestItemsCount(HARRYS_RECEIPT1) != 0L)
				htmltext = "blacksmith_alltran_q0152_01.htm";
			else if(cond == 2 && st.getQuestItemsCount(HARRYS_RECEIPT2) != 0L && st.getQuestItemsCount(GOLEM_SHARD) < 5L)
				htmltext = "blacksmith_alltran_q0152_03.htm";
			else if(cond == 3 && st.getQuestItemsCount(HARRYS_RECEIPT2) != 0L && st.getQuestItemsCount(GOLEM_SHARD) == 5L)
			{
				st.takeItems(GOLEM_SHARD, -1L);
				if(st.getQuestItemsCount(TOOL_BOX) == 0L)
				{
					st.giveItems(TOOL_BOX, 1L);
					st.set("cond", "4");
				}
				htmltext = "blacksmith_alltran_q0152_04.htm";
			}
		}
		else if(cond == 4 && st.getQuestItemsCount(HARRYS_RECEIPT2) != 0L && st.getQuestItemsCount(TOOL_BOX) != 0L)
			htmltext = "blacksmith_alltran_q0152_05.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 2 && Rnd.chance(30) && st.getQuestItemsCount(GOLEM_SHARD) < 5L)
		{
			st.giveItems(GOLEM_SHARD, 1L);
			if(st.getQuestItemsCount(GOLEM_SHARD) == 5L)
			{
				st.set("cond", "3");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
