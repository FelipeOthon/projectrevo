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

public class _616_MagicalPowerofFire2 extends Quest implements ScriptFile
{
	private static final int KETRAS_HOLY_ALTAR = 31558;
	private static final int UDAN = 31379;
	private static final short FIRE_HEART_OF_NASTRON = 7244;
	private static final short RED_TOTEM = 7243;
	private static short Reward_First;
	private static short Reward_Last;
	private static final int SoulOfFireNastron = 25306;
	private NpcInstance SoulOfFireNastronSpawn;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _616_MagicalPowerofFire2()
	{
		super(true);
		SoulOfFireNastronSpawn = null;
		this.addStartNpc(31379);
		this.addTalkId(new int[] { 31558 });
		this.addKillId(new int[] { 25306 });
		addQuestItem(new int[] { 7244 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 25306);
		String htmltext = event;
		if(event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "shaman_udan_q0616_0104.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("616_1"))
		{
			if(ServerVariables.getLong(_616_MagicalPowerofFire2.class.getSimpleName(), 0L) + 10800000L > System.currentTimeMillis())
				htmltext = "totem_of_ketra_q0616_0204.htm";
			else if(st.getQuestItemsCount(7243) >= 1L && isQuest.isEmpty())
			{
				st.takeItems(7243, 1L);
				(SoulOfFireNastronSpawn = st.addSpawn(25306, 142528, -82528, -6496)).addMethodInvokeListener("L2Character.doDie", new DieListener());
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				htmltext = "totem_of_ketra_q0616_0203.htm";
		}
		else if(event.equalsIgnoreCase("616_3"))
			if(st.getQuestItemsCount(7244) >= 1L)
			{
				st.takeItems(7244, -1L);
				st.giveItems(Rnd.get(_616_MagicalPowerofFire2.Reward_First, _616_MagicalPowerofFire2.Reward_Last), 5L, true);
				st.playSound(Quest.SOUND_FINISH);
				htmltext = "shaman_udan_q0616_0301.htm";
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "shaman_udan_q0616_0302.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 25306);
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		switch(npcId)
		{
			case 31379:
			{
				if(cond == 0)
				{
					if(st.getPlayer().getLevel() < 75)
					{
						htmltext = "shaman_udan_q0616_0103.htm";
						st.exitCurrentQuest(true);
						break;
					}
					if(st.getQuestItemsCount(7243) >= 1L)
					{
						htmltext = "shaman_udan_q0616_0101.htm";
						break;
					}
					htmltext = "shaman_udan_q0616_0102.htm";
					st.exitCurrentQuest(true);
					break;
				}
				else
				{
					if(cond == 1)
					{
						htmltext = "shaman_udan_q0616_0105.htm";
						break;
					}
					if(cond == 2)
					{
						htmltext = "shaman_udan_q0616_0202.htm";
						break;
					}
					if(cond == 3 && st.getQuestItemsCount(7244) == 1L)
					{
						htmltext = "shaman_udan_q0616_0201.htm";
						break;
					}
					break;
				}
			}
			case 31558:
			{
				if(ServerVariables.getLong(_616_MagicalPowerofFire2.class.getSimpleName(), 0L) + 10800000L > System.currentTimeMillis())
				{
					htmltext = "totem_of_ketra_q0616_0204.htm";
					break;
				}
				if(npc.isBusy())
				{
					htmltext = "totem_of_ketra_q0616_0202.htm";
					break;
				}
				if(cond == 1)
				{
					htmltext = "totem_of_ketra_q0616_0101.htm";
					break;
				}
				if(cond != 2)
					break;
				if(isQuest.isEmpty())
				{
					(SoulOfFireNastronSpawn = st.addSpawn(25306, 142528, -82528, -6496)).addMethodInvokeListener("L2Character.doDie", new DieListener());
					htmltext = "totem_of_ketra_q0616_0204.htm";
					break;
				}
				htmltext = "<html><body>Already in spawn.</body></html>";
				break;
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(7244) == 0L)
		{
			st.giveItems(7244, 1L);
			st.set("cond", "3");
			if(SoulOfFireNastronSpawn != null)
				SoulOfFireNastronSpawn.deleteMe();
			SoulOfFireNastronSpawn = null;
		}
		return null;
	}

	static
	{
		_616_MagicalPowerofFire2.Reward_First = 4589;
		_616_MagicalPowerofFire2.Reward_Last = 4594;
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
			ServerVariables.set(_616_MagicalPowerofFire2.class.getSimpleName(), String.valueOf(System.currentTimeMillis()));
		}
	}
}
