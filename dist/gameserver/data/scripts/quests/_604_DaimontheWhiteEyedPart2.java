package quests;

import java.util.List;

import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.listener.MethodInvokeListener;
import l2s.gameserver.listener.events.MethodEvent;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class _604_DaimontheWhiteEyedPart2 extends Quest implements ScriptFile
{
	private static final int EYE = 31683;
	private static final int ALTAR = 31541;
	private static final int DAIMON = 25290;
	private static final int U_SUMMON = 7192;
	private static final int S_SUMMON = 7193;
	private static final int ESSENCE = 7194;
	private static final int INT_MEN = 4595;
	private static final int INT_WIT = 4596;
	private static final int MEN_INT = 4597;
	private static final int MEN_WIT = 4598;
	private static final int WIT_INT = 4599;
	private static final int WIT_MEN = 4600;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _604_DaimontheWhiteEyedPart2()
	{
		super(true);
		this.addStartNpc(31683);
		this.addTalkId(new int[] { 31541 });
		this.addKillId(new int[] { 25290 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 25290);
		if(event.equalsIgnoreCase("31683-02.htm"))
		{
			if(st.getPlayer().getLevel() < 73)
			{
				st.exitCurrentQuest(true);
				return "31683-00b.htm";
			}
			st.set("cond", "1");
			st.setState(2);
			st.takeItems(7192, 1L);
			st.giveItems(7193, 1L);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("31541-02.htm"))
		{
			if(st.getQuestItemsCount(7193) == 0L)
				return "31541-04.htm";
			if(!isQuest.isEmpty())
				return "31541-03.htm";
			if(ServerVariables.getLong(_604_DaimontheWhiteEyedPart2.class.getSimpleName(), 0L) + 10800000L > System.currentTimeMillis())
				return "31541-05.htm";
			st.takeItems(7193, 1L);
			isQuest.add(st.addSpawn(25290, 186320, -43904, -3175));
			Functions.npcSay(isQuest.get(0), "Who called me?");
			isQuest.get(0).addMethodInvokeListener("L2Character.doDie", new DieListener());
			st.playSound(Quest.SOUND_MIDDLE);
			st.set("cond", "2");
			st.setState(2);
			st.getPlayer().sendMessage("Daimon the White-Eyed has spawned in 186320, -43904, -3175");
			st.startQuestTimer("DAIMON_Fail", 12000000L);
		}
		else if(event.equalsIgnoreCase("31683-04.htm"))
		{
			if(st.getQuestItemsCount(7194) >= 1L)
				return "list.htm";
			st.exitCurrentQuest(true);
			return "31683-05.htm";
		}
		else
		{
			if(event.equalsIgnoreCase("INT_MEN"))
			{
				st.giveItems(4595, 5L, true);
				st.takeItems(7194, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
				return null;
			}
			if(event.equalsIgnoreCase("INT_WIT"))
			{
				st.giveItems(4596, 5L, true);
				st.takeItems(7194, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
				return null;
			}
			if(event.equalsIgnoreCase("MEN_INT"))
			{
				st.giveItems(4597, 5L, true);
				st.takeItems(7194, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
				return null;
			}
			if(event.equalsIgnoreCase("MEN_WIT"))
			{
				st.giveItems(4598, 5L, true);
				st.takeItems(7194, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
				return null;
			}
			if(event.equalsIgnoreCase("WIT_INT"))
			{
				st.giveItems(4599, 5L, true);
				st.takeItems(7194, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
				return null;
			}
			if(event.equalsIgnoreCase("WIT_MEN"))
			{
				st.giveItems(4600, 5L, true);
				st.takeItems(7194, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
				return null;
			}
			if(event.equalsIgnoreCase("DAIMON_Fail") && !isQuest.isEmpty())
			{
				Functions.npcSay(isQuest.get(0), "Darkness could not have ray?");
				isQuest.get(0).deleteMe();
				return null;
			}
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 25290);
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(npcId == 31683)
				if(st.getQuestItemsCount(7192) >= 1L)
					htmltext = "31683-01.htm";
				else
					htmltext = "31683-00a.htm";
		}
		else if(cond == 1)
		{
			if(npcId == 31683)
				htmltext = "31683-02a.htm";
			else if(npcId == 31541)
				if(ServerVariables.getLong(_604_DaimontheWhiteEyedPart2.class.getSimpleName(), 0L) + 10800000L > System.currentTimeMillis())
					htmltext = "31541-05.htm";
				else
					htmltext = "31541-01.htm";
		}
		else if(cond == 2)
		{
			if(npcId == 31541)
				if(!isQuest.isEmpty())
					htmltext = "31541-03.htm";
				else if(ServerVariables.getLong(_604_DaimontheWhiteEyedPart2.class.getSimpleName(), 0L) + 10800000L > System.currentTimeMillis())
					htmltext = "31541-05.htm";
				else
				{
					isQuest.add(st.addSpawn(25290, 186320, -43904, -3175));
					Functions.npcSay(isQuest.get(0), "Who called me?");
					st.playSound(Quest.SOUND_MIDDLE);
					st.setState(2);
					st.getPlayer().sendMessage("Daimon the White-Eyed has spawned in 186320, -43904, -3175");
					isQuest.get(0).addMethodInvokeListener("L2Character.doDie", new DieListener());
					st.startQuestTimer("DAIMON_Fail", 12000000L);
				}
		}
		else if(cond == 3)
		{
			if(npcId == 31683)
				if(st.getQuestItemsCount(7194) >= 1L)
					htmltext = "31683-03.htm";
				else
					htmltext = "31683-06.htm";
			if(npcId == 31541)
				htmltext = "31541-05.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(7193) > 0L)
		{
			st.takeItems(7193, 1L);
			st.giveItems(7194, 1L);
			st.set("cond", "3");
			st.setState(2);
			st.playSound(Quest.SOUND_MIDDLE);
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
			ServerVariables.set(_604_DaimontheWhiteEyedPart2.class.getSimpleName(), String.valueOf(System.currentTimeMillis()));
		}
	}
}
