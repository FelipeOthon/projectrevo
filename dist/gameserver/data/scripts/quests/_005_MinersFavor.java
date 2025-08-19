package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _005_MinersFavor extends Quest implements ScriptFile
{
	public final int BOLTER = 30554;
	public final int SHARI = 30517;
	public final int GARITA = 30518;
	public final int REED = 30520;
	public final int BRUNON = 30526;
	public final int BOLTERS_LIST = 1547;
	public final int MINING_BOOTS = 1548;
	public final int MINERS_PICK = 1549;
	public final int BOOMBOOM_POWDER = 1550;
	public final int REDSTONE_BEER = 1551;
	public final int BOLTERS_SMELLY_SOCKS = 1552;
	public final int NECKLACE = 906;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _005_MinersFavor()
	{
		super(false);
		this.addStartNpc(30554);
		this.addTalkId(new int[] { 30517 });
		this.addTalkId(new int[] { 30518 });
		this.addTalkId(new int[] { 30520 });
		this.addTalkId(new int[] { 30526 });
		addQuestItem(new int[] { 1547, 1552, 1548, 1549, 1550, 1551 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("miner_bolter_q0005_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.giveItems(1547, 1L, false);
			st.giveItems(1552, 1L, false);
		}
		else if(event.equalsIgnoreCase("blacksmith_bronp_q0005_02.htm"))
		{
			st.takeItems(1552, -1L);
			st.giveItems(1549, 1L, false);
			if(st.getQuestItemsCount(1547) > 0L && st.getQuestItemsCount(1548) + st.getQuestItemsCount(1549) + st.getQuestItemsCount(1550) + st.getQuestItemsCount(1551) == 4L)
			{
				st.set("cond", "2");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30554)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 2)
					htmltext = "miner_bolter_q0005_02.htm";
				else
				{
					htmltext = "miner_bolter_q0005_01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "miner_bolter_q0005_04.htm";
			else if(cond == 2 && st.getQuestItemsCount(1548) + st.getQuestItemsCount(1549) + st.getQuestItemsCount(1550) + st.getQuestItemsCount(1551) == 4L)
			{
				htmltext = "miner_bolter_q0005_06.htm";
				st.takeItems(1548, -1L);
				st.takeItems(1549, -1L);
				st.takeItems(1550, -1L);
				st.takeItems(1551, -1L);
				st.takeItems(1547, -1L);
				st.giveItems(906, 1L, false);
				st.getPlayer().addExpAndSp(5672L, 446L, false, false);
				st.giveItems(57, 2466L);
				st.unset("cond");
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else if(cond == 1 && st.getQuestItemsCount(1547) > 0L)
		{
			if(npcId == 30517)
			{
				if(st.getQuestItemsCount(1550) == 0L)
				{
					htmltext = "trader_chali_q0005_01.htm";
					st.giveItems(1550, 1L, false);
					st.playSound(Quest.SOUND_ITEMGET);
				}
				else
					htmltext = "trader_chali_q0005_02.htm";
			}
			else if(npcId == 30518)
			{
				if(st.getQuestItemsCount(1548) == 0L)
				{
					htmltext = "trader_garita_q0005_01.htm";
					st.giveItems(1548, 1L, false);
					st.playSound(Quest.SOUND_ITEMGET);
				}
				else
					htmltext = "trader_garita_q0005_02.htm";
			}
			else if(npcId == 30520)
			{
				if(st.getQuestItemsCount(1551) == 0L)
				{
					htmltext = "warehouse_chief_reed_q0005_01.htm";
					st.giveItems(1551, 1L, false);
					st.playSound(Quest.SOUND_ITEMGET);
				}
				else
					htmltext = "warehouse_chief_reed_q0005_02.htm";
			}
			else if(npcId == 30526 && st.getQuestItemsCount(1552) > 0L)
				if(st.getQuestItemsCount(1549) == 0L)
					htmltext = "blacksmith_bronp_q0005_01.htm";
				else
					htmltext = "blacksmith_bronp_q0005_03.htm";
			if(st.getQuestItemsCount(1547) > 0L && st.getQuestItemsCount(1548) + st.getQuestItemsCount(1549) + st.getQuestItemsCount(1550) + st.getQuestItemsCount(1551) == 4L)
			{
				st.set("cond", "2");
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		return htmltext;
	}
}
