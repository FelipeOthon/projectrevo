package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _153_DeliverGoods extends Quest implements ScriptFile
{
	int DELIVERY_LIST;
	int HEAVY_WOOD_BOX;
	int CLOTH_BUNDLE;
	int CLAY_POT;
	int JACKSONS_RECEIPT;
	int SILVIAS_RECEIPT;
	int RANTS_RECEIPT;
	int RING_OF_KNOWLEDGE;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _153_DeliverGoods()
	{
		super(false);
		DELIVERY_LIST = 1012;
		HEAVY_WOOD_BOX = 1013;
		CLOTH_BUNDLE = 1014;
		CLAY_POT = 1015;
		JACKSONS_RECEIPT = 1016;
		SILVIAS_RECEIPT = 1017;
		RANTS_RECEIPT = 1018;
		RING_OF_KNOWLEDGE = 875;
		this.addStartNpc(30041);
		this.addTalkId(new int[] { 30002 });
		this.addTalkId(new int[] { 30003 });
		this.addTalkId(new int[] { 30054 });
		addQuestItem(new int[] { HEAVY_WOOD_BOX, CLOTH_BUNDLE, CLAY_POT, DELIVERY_LIST, JACKSONS_RECEIPT, SILVIAS_RECEIPT, RANTS_RECEIPT });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("arnold_q0153_04.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			if(st.getQuestItemsCount(DELIVERY_LIST) == 0L)
				st.giveItems(DELIVERY_LIST, 1L);
			if(st.getQuestItemsCount(HEAVY_WOOD_BOX) == 0L)
				st.giveItems(HEAVY_WOOD_BOX, 1L);
			if(st.getQuestItemsCount(CLOTH_BUNDLE) == 0L)
				st.giveItems(CLOTH_BUNDLE, 1L);
			if(st.getQuestItemsCount(CLAY_POT) == 0L)
				st.giveItems(CLAY_POT, 1L);
			htmltext = "arnold_q0153_04.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30041)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 2)
				{
					htmltext = "arnold_q0153_03.htm";
					return htmltext;
				}
				htmltext = "arnold_q0153_02.htm";
				st.exitCurrentQuest(true);
			}
			else if(cond == 1 && st.getQuestItemsCount(JACKSONS_RECEIPT) + st.getQuestItemsCount(SILVIAS_RECEIPT) + st.getQuestItemsCount(RANTS_RECEIPT) == 0L)
				htmltext = "arnold_q0153_05.htm";
			else if(cond == 1 && st.getQuestItemsCount(JACKSONS_RECEIPT) + st.getQuestItemsCount(SILVIAS_RECEIPT) + st.getQuestItemsCount(RANTS_RECEIPT) == 3L)
			{
				st.giveItems(RING_OF_KNOWLEDGE, 2L);
				st.takeItems(DELIVERY_LIST, -1L);
				st.takeItems(JACKSONS_RECEIPT, -1L);
				st.takeItems(SILVIAS_RECEIPT, -1L);
				st.takeItems(RANTS_RECEIPT, -1L);
				st.addExpAndSp(600L, 0L);
				st.playSound(Quest.SOUND_FINISH);
				htmltext = "arnold_q0153_06.htm";
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 30002)
		{
			if(cond == 1 && st.getQuestItemsCount(HEAVY_WOOD_BOX) == 1L)
			{
				st.takeItems(HEAVY_WOOD_BOX, -1L);
				if(st.getQuestItemsCount(JACKSONS_RECEIPT) == 0L)
					st.giveItems(JACKSONS_RECEIPT, 1L);
				htmltext = "jackson_q0153_01.htm";
			}
			else if(cond == 1 && st.getQuestItemsCount(JACKSONS_RECEIPT) > 0L)
				htmltext = "jackson_q0153_02.htm";
		}
		else if(npcId == 30003)
		{
			if(cond == 1 && st.getQuestItemsCount(CLOTH_BUNDLE) == 1L)
			{
				st.takeItems(CLOTH_BUNDLE, -1L);
				if(st.getQuestItemsCount(SILVIAS_RECEIPT) == 0L)
				{
					st.giveItems(SILVIAS_RECEIPT, 1L);
					if(st.getPlayer().isMageClass())
						st.giveItems(2509, 3L);
					else
						st.giveItems(1835, 6L);
				}
				htmltext = "silvia_q0153_01.htm";
			}
			else if(cond == 1 && st.getQuestItemsCount(SILVIAS_RECEIPT) > 0L)
				htmltext = "silvia_q0153_02.htm";
		}
		else if(npcId == 30054)
			if(cond == 1 && st.getQuestItemsCount(CLAY_POT) == 1L)
			{
				st.takeItems(CLAY_POT, -1L);
				if(st.getQuestItemsCount(RANTS_RECEIPT) == 0L)
					st.giveItems(RANTS_RECEIPT, 1L);
				htmltext = "rant_q0153_01.htm";
			}
			else if(cond == 1 && st.getQuestItemsCount(RANTS_RECEIPT) > 0L)
				htmltext = "rant_q0153_02.htm";
		return htmltext;
	}
}
