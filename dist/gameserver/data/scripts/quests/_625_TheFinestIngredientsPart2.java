package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.listener.MethodInvokeListener;
import l2s.gameserver.listener.events.MethodEvent;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;

public class _625_TheFinestIngredientsPart2 extends Quest implements ScriptFile
{
	private static int Jeremy;
	private static int Yetis_Table;
	private static int RB_Icicle_Emperor_Bumbalump;
	private static short Soy_Sauce_Jar;
	private static short Food_for_Bumbalump;
	private static short Special_Yeti_Meat;
	private static short Reward_First;
	private static short Reward_Last;

	public _625_TheFinestIngredientsPart2()
	{
		super(true);
		this.addStartNpc(_625_TheFinestIngredientsPart2.Jeremy);
		this.addTalkId(new int[] { _625_TheFinestIngredientsPart2.Yetis_Table });
		this.addKillId(new int[] { _625_TheFinestIngredientsPart2.RB_Icicle_Emperor_Bumbalump });
		addQuestItem(new int[] { _625_TheFinestIngredientsPart2.Food_for_Bumbalump, _625_TheFinestIngredientsPart2.Special_Yeti_Meat });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		final int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("jeremy_q0625_0104.htm") && _state == 1)
		{
			if(st.getQuestItemsCount(_625_TheFinestIngredientsPart2.Soy_Sauce_Jar) == 0L)
			{
				st.exitCurrentQuest(true);
				return "jeremy_q0625_0102.htm";
			}
			st.setState(2);
			st.set("cond", "1");
			st.takeItems(_625_TheFinestIngredientsPart2.Soy_Sauce_Jar, 1L);
			st.giveItems(_625_TheFinestIngredientsPart2.Food_for_Bumbalump, 1L);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("jeremy_q0625_0301.htm") && _state == 2 && cond == 3)
		{
			st.exitCurrentQuest(true);
			if(st.getQuestItemsCount(_625_TheFinestIngredientsPart2.Special_Yeti_Meat) == 0L)
				return "jeremy_q0625_0302.htm";
			st.takeItems(_625_TheFinestIngredientsPart2.Special_Yeti_Meat, 1L);
			st.giveItems(Rnd.get(_625_TheFinestIngredientsPart2.Reward_First, _625_TheFinestIngredientsPart2.Reward_Last), 5L, true);
		}
		else if(event.equalsIgnoreCase("yetis_table_q0625_0201.htm") && _state == 2 && cond == 1)
		{
			if(ServerVariables.getLong(_625_TheFinestIngredientsPart2.class.getSimpleName(), 0L) + 10800000L > System.currentTimeMillis())
				return "yetis_table_q0625_0204.htm";
			if(st.getQuestItemsCount(_625_TheFinestIngredientsPart2.Food_for_Bumbalump) == 0L)
				return "yetis_table_q0625_0203.htm";
			if(BumbalumpSpawned())
				return "yetis_table_q0625_0202.htm";
			st.takeItems(_625_TheFinestIngredientsPart2.Food_for_Bumbalump, 1L);
			st.set("cond", "2");
			ThreadPoolManager.getInstance().schedule(new BumbalumpSpawner(), 1000L);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int _state = st.getState();
		final int npcId = npc.getNpcId();
		if(_state == 1)
		{
			if(npcId != _625_TheFinestIngredientsPart2.Jeremy)
				return "noquest";
			if(st.getPlayer().getLevel() < 73)
			{
				st.exitCurrentQuest(true);
				return "jeremy_q0625_0103.htm";
			}
			if(st.getQuestItemsCount(_625_TheFinestIngredientsPart2.Soy_Sauce_Jar) == 0L)
			{
				st.exitCurrentQuest(true);
				return "jeremy_q0625_0102.htm";
			}
			st.set("cond", "0");
			return "jeremy_q0625_0101.htm";
		}
		else
		{
			if(_state != 2)
				return "noquest";
			final int cond = st.getInt("cond");
			if(npcId == _625_TheFinestIngredientsPart2.Jeremy)
			{
				if(cond == 1)
					return "jeremy_q0625_0105.htm";
				if(cond == 2)
					return "jeremy_q0625_0202.htm";
				if(cond == 3)
					return "jeremy_q0625_0201.htm";
			}
			if(npcId == _625_TheFinestIngredientsPart2.Yetis_Table)
			{
				if(ServerVariables.getLong(_625_TheFinestIngredientsPart2.class.getSimpleName(), 0L) + 10800000L > System.currentTimeMillis())
					return "yetis_table_q0625_0204.htm";
				if(cond == 1)
					return "yetis_table_q0625_0101.htm";
				if(cond == 2)
				{
					if(BumbalumpSpawned())
						return "yetis_table_q0625_0202.htm";
					ThreadPoolManager.getInstance().schedule(new BumbalumpSpawner(), 1000L);
					return "yetis_table_q0625_0201.htm";
				}
				else if(cond == 3)
					return "yetis_table_q0625_0204.htm";
			}
			return "noquest";
		}
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1 || st.getInt("cond") == 2)
		{
			if(st.getQuestItemsCount(_625_TheFinestIngredientsPart2.Food_for_Bumbalump) > 0L)
				st.takeItems(_625_TheFinestIngredientsPart2.Food_for_Bumbalump, 1L);
			st.giveItems(_625_TheFinestIngredientsPart2.Special_Yeti_Meat, 1L);
			st.set("cond", "3");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		return null;
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	private static boolean BumbalumpSpawned()
	{
		return !GameObjectsStorage.getNpcs(false, _625_TheFinestIngredientsPart2.RB_Icicle_Emperor_Bumbalump).isEmpty();
	}

	static
	{
		_625_TheFinestIngredientsPart2.Jeremy = 31521;
		_625_TheFinestIngredientsPart2.Yetis_Table = 31542;
		_625_TheFinestIngredientsPart2.RB_Icicle_Emperor_Bumbalump = 25296;
		_625_TheFinestIngredientsPart2.Soy_Sauce_Jar = 7205;
		_625_TheFinestIngredientsPart2.Food_for_Bumbalump = 7209;
		_625_TheFinestIngredientsPart2.Special_Yeti_Meat = 7210;
		_625_TheFinestIngredientsPart2.Reward_First = 4589;
		_625_TheFinestIngredientsPart2.Reward_Last = 4594;
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
			ServerVariables.set(_625_TheFinestIngredientsPart2.class.getSimpleName(), String.valueOf(System.currentTimeMillis()));
		}
	}

	public class BumbalumpSpawner implements Runnable
	{
		private Spawn _spawn;
		private int tiks;

		public BumbalumpSpawner()
		{
			_spawn = null;
			tiks = 0;
			if(BumbalumpSpawned())
				return;
			final NpcTemplate template = NpcTable.getTemplate(_625_TheFinestIngredientsPart2.RB_Icicle_Emperor_Bumbalump);
			if(template == null)
				return;
			try
			{
				_spawn = new Spawn(template);
			}
			catch(Exception E)
			{
				return;
			}
			_spawn.setLocx(158240);
			_spawn.setLocy(-121536);
			_spawn.setLocz(-2253);
			_spawn.setHeading(Rnd.get(0, 65535));
			_spawn.setAmount(1);
			_spawn.doSpawn(true);
			_spawn.stopRespawn();
			for(final NpcInstance _npc : _spawn.getAllSpawned())
				_npc.addMethodInvokeListener("L2Character.doDie", new DieListener());
		}

		public void Say(final String test)
		{
			for(final NpcInstance _npc : _spawn.getAllSpawned())
				Functions.npcSay(_npc, test);
		}

		@Override
		public void run()
		{
			if(_spawn == null)
				return;
			if(tiks == 0)
				Say("I will crush you!");
			if(tiks < 1200 && BumbalumpSpawned())
			{
				++tiks;
				if(tiks == 1200)
					Say("May the gods forever condemn you! Your power weakens!");
				ThreadPoolManager.getInstance().schedule(this, 1000L);
				return;
			}
			_spawn.despawnAll();
		}
	}
}
