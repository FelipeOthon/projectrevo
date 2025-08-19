package quests;

import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.listener.MethodInvokeListener;
import l2s.gameserver.listener.events.MethodEvent;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _610_MagicalPowerofWater2 extends Quest implements ScriptFile
{
	private static final int ASEFA = 31372;
	private static final int VARKAS_HOLY_ALTAR = 31560;
	private static final int GREEN_TOTEM = 7238;
	int ICE_HEART_OF_ASHUTAR;
	private static final int Reward_First = 4589;
	private static final int Reward_Last = 4594;
	private static final int SoulOfWaterAshutar = 25316;
	private NpcInstance SoulOfWaterAshutarSpawn;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _610_MagicalPowerofWater2()
	{
		super(true);
		ICE_HEART_OF_ASHUTAR = 7239;
		SoulOfWaterAshutarSpawn = null;
		this.addStartNpc(31372);
		this.addTalkId(new int[] { 31560 });
		this.addKillId(new int[] { 25316 });
		addQuestItem(new int[] { ICE_HEART_OF_ASHUTAR });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 25316);
		String htmltext = event;
		if(event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "shaman_asefa_q0610_0104.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("610_1"))
		{
			if(ServerVariables.getLong(_610_MagicalPowerofWater2.class.getSimpleName(), 0L) + 10800000L > System.currentTimeMillis())
				htmltext = "totem_of_barka_q0610_0204.htm";
			else if(st.getQuestItemsCount(7238) >= 1L && isQuest.isEmpty())
			{
				st.takeItems(7238, 1L);
				(SoulOfWaterAshutarSpawn = st.addSpawn(25316, 104825, -36926, -1136)).addMethodInvokeListener("L2Character.doDie", new DieListener());
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				htmltext = "totem_of_barka_q0610_0203.htm";
		}
		else if(event.equalsIgnoreCase("610_3"))
			if(st.getQuestItemsCount(ICE_HEART_OF_ASHUTAR) >= 1L)
			{
				st.takeItems(ICE_HEART_OF_ASHUTAR, -1L);
				st.giveItems(Rnd.get(4589, 4594), 5L, true);
				st.playSound(Quest.SOUND_FINISH);
				htmltext = "shaman_asefa_q0610_0301.htm";
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "shaman_asefa_q0610_0302.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 25316);
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == 31372)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 75)
				{
					if(st.getQuestItemsCount(7238) >= 1L)
						htmltext = "shaman_asefa_q0610_0101.htm";
					else
					{
						htmltext = "shaman_asefa_q0610_0102.htm";
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "shaman_asefa_q0610_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "shaman_asefa_q0610_0105.htm";
			else if(cond == 2)
				htmltext = "shaman_asefa_q0610_0202.htm";
			else if(cond == 3 && st.getQuestItemsCount(ICE_HEART_OF_ASHUTAR) == 1L)
				htmltext = "shaman_asefa_q0610_0201.htm";
		}
		else if(npcId == 31560)
			if(!npc.isBusy())
			{
				if(ServerVariables.getLong(_610_MagicalPowerofWater2.class.getSimpleName(), 0L) + 10800000L > System.currentTimeMillis())
					htmltext = "totem_of_barka_q0610_0204.htm";
				else if(cond == 1)
					htmltext = "totem_of_barka_q0610_0101.htm";
				else if(cond == 2 && isQuest.isEmpty())
				{
					(SoulOfWaterAshutarSpawn = st.addSpawn(25316, 104825, -36926, -1136)).addMethodInvokeListener("L2Character.doDie", new DieListener());
					htmltext = "totem_of_barka_q0610_0204.htm";
				}
			}
			else
				htmltext = "totem_of_barka_q0610_0202.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(ICE_HEART_OF_ASHUTAR) == 0L && npc.getNpcId() == 25316)
		{
			st.giveItems(ICE_HEART_OF_ASHUTAR, 1L);
			st.set("cond", "3");
			if(SoulOfWaterAshutarSpawn != null)
				SoulOfWaterAshutarSpawn.deleteMe();
			SoulOfWaterAshutarSpawn = null;
		}
		return null;
	}

	public static class DieListener implements MethodInvokeListener
	{
		@Override
		public boolean accept(final MethodEvent event)
		{
			return true;
		}

		@Override
		public void methodInvoked(final MethodEvent e)
		{
			ServerVariables.set(_610_MagicalPowerofWater2.class.getSimpleName(), String.valueOf(System.currentTimeMillis()));
		}
	}
}
