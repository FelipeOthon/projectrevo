package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.ScriptFile;

public class _296_SilkOfTarantula extends Quest implements ScriptFile
{
	private static final int TARANTULA_SPIDER_SILK = 1493;
	private static final int TARANTULA_SPINNERETTE = 1494;
	private static final int RING_OF_RACCOON = 1508;
	private static final int RING_OF_FIREFLY = 1509;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _296_SilkOfTarantula()
	{
		super(false);
		this.addStartNpc(30519);
		this.addTalkId(new int[] { 30548 });
		this.addKillId(new int[] { 20394 });
		this.addKillId(new int[] { 20403 });
		this.addKillId(new int[] { 20508 });
		addQuestItem(new int[] { 1493 });
		addQuestItem(new int[] { 1494 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("trader_mion_q0296_03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("quit"))
		{
			htmltext = "trader_mion_q0296_06.htm";
			st.takeItems(1494, -1L);
			st.exitCurrentQuest(true);
			st.playSound(Quest.SOUND_FINISH);
		}
		else if(event.equalsIgnoreCase("exchange"))
			if(st.getQuestItemsCount(1494) >= 1L)
			{
				final long count = st.getQuestItemsCount(1494);
				htmltext = "defender_nathan_q0296_03.htm";
				st.giveItems(1493, 20L * count);
				st.takeItems(1494, count);
			}
			else
				htmltext = "defender_nathan_q0296_02.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 30519)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 15)
				{
					if(st.getQuestItemsCount(1508) <= 0L && st.getQuestItemsCount(1509) <= 0L)
					{
						htmltext = "trader_mion_q0296_08.htm";
						return htmltext;
					}
					htmltext = "trader_mion_q0296_02.htm";
				}
				else
				{
					htmltext = "trader_mion_q0296_01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				if(st.getQuestItemsCount(1493) < 1L)
					htmltext = "trader_mion_q0296_04.htm";
				else if(st.getQuestItemsCount(1493) >= 1L)
				{
					htmltext = "trader_mion_q0296_05.htm";
					st.giveItems(57, st.getQuestItemsCount(1493) * 23L);
					st.takeItems(1493, -1L);
					if(st.getPlayer().getClassId().getLevel() == 1 && !st.getPlayer().getVarBoolean("p1q4"))
					{
						st.getPlayer().setVar("p1q4", "1");
						st.getPlayer().sendPacket(new ExShowScreenMessage("Now go find the Newbie Guide.", 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
					}
				}
		}
		else if(npcId == 30548 && cond == 1)
			htmltext = "defender_nathan_q0296_01.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1)
			if(Rnd.chance(50))
				st.rollAndGive(1494, 1, 45.0);
			else
				st.rollAndGive(1493, 1, 45.0);
		return null;
	}
}
