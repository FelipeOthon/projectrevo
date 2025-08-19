package quests;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.World;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ItemList;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;

public class _421_LittleWingAdventures extends Quest implements ScriptFile
{
	private static int Cronos;
	private static int Mimyu;
	private static int Hatchling_of_the_Wind;
	private static int Fairy_Tree_of_Wind;
	private static int Fairy_Tree_of_Star;
	private static int Fairy_Tree_of_Twilight;
	private static int Fairy_Tree_of_Abyss;
	private static int Soul_of_Tree_Guardian;
	private static int Dragonflute_of_Wind;
	private static int Dragonflute_of_Star;
	private static int Dragonflute_of_Twilight;
	private static int Dragon_Bugle_of_Wind;
	private static int Dragon_Bugle_of_Star;
	private static int Dragon_Bugle_of_Twilight;
	private static int Fairy_Leaf;
	private static int Min_Fairy_Tree_Attaks;

	public _421_LittleWingAdventures()
	{
		super(false);
		this.addStartNpc(_421_LittleWingAdventures.Cronos);
		this.addTalkId(new int[] { _421_LittleWingAdventures.Mimyu });
		this.addKillId(new int[] { _421_LittleWingAdventures.Fairy_Tree_of_Wind });
		this.addKillId(new int[] { _421_LittleWingAdventures.Fairy_Tree_of_Star });
		this.addKillId(new int[] { _421_LittleWingAdventures.Fairy_Tree_of_Twilight });
		this.addKillId(new int[] { _421_LittleWingAdventures.Fairy_Tree_of_Abyss });
		addAttackId(new int[] { _421_LittleWingAdventures.Fairy_Tree_of_Wind });
		addAttackId(new int[] { _421_LittleWingAdventures.Fairy_Tree_of_Star });
		addAttackId(new int[] { _421_LittleWingAdventures.Fairy_Tree_of_Twilight });
		addAttackId(new int[] { _421_LittleWingAdventures.Fairy_Tree_of_Abyss });
		addQuestItem(new int[] { _421_LittleWingAdventures.Fairy_Leaf });
	}

	private static ItemInstance GetDragonflute(final QuestState st)
	{
		final List<ItemInstance> Dragonflutes = new ArrayList<ItemInstance>();
		for(final ItemInstance item : st.getPlayer().getInventory().getItems())
			if(item != null && (item.getItemId() == _421_LittleWingAdventures.Dragonflute_of_Wind || item.getItemId() == _421_LittleWingAdventures.Dragonflute_of_Star || item.getItemId() == _421_LittleWingAdventures.Dragonflute_of_Twilight))
				Dragonflutes.add(item);
		if(Dragonflutes.isEmpty())
			return null;
		if(Dragonflutes.size() == 1)
			return Dragonflutes.get(0);
		if(st.getState() == 1)
			return null;
		final int dragonflute_id = st.getInt("dragonflute");
		for(final ItemInstance item2 : Dragonflutes)
			if(item2.getObjectId() == dragonflute_id)
				return item2;
		return null;
	}

	private static boolean HatchlingSummoned(final QuestState st, final boolean CheckObjID)
	{
		final Servitor _pet = st.getPlayer().getServitor();
		if(_pet == null)
			return false;
		if(CheckObjID)
		{
			final int dragonflute_id = st.getInt("dragonflute");
			if(dragonflute_id == 0)
				return false;
			if(_pet.getControlItemId() != dragonflute_id)
				return false;
		}
		final ItemInstance dragonflute = GetDragonflute(st);
		return dragonflute != null && _pet.getNpcId() == _421_LittleWingAdventures.Hatchling_of_the_Wind + dragonflute.getItemId() - _421_LittleWingAdventures.Dragonflute_of_Wind;
	}

	private static boolean CheckTree(final QuestState st, final int Fairy_Tree_id)
	{
		return st.getInt(String.valueOf(Fairy_Tree_id)) == 1000000;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		final ItemInstance dragonflute = GetDragonflute(st);
		final int dragonflute_id = st.getInt("dragonflute");
		final int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("30610_05.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if((event.equalsIgnoreCase("30747_03.htm") || event.equalsIgnoreCase("30747_04.htm")) && _state == 2 && cond == 1)
		{
			if(dragonflute == null)
				return "noquest";
			if(dragonflute.getObjectId() != dragonflute_id)
			{
				if(Rnd.chance(10))
				{
					st.takeItems(dragonflute.getItemId(), 1L);
					st.playSound(Quest.SOUND_FINISH);
					st.exitCurrentQuest(true);
				}
				return "30747_00.htm";
			}
			if(!HatchlingSummoned(st, false))
				return event.equalsIgnoreCase("30747_04.htm") ? "30747_04a.htm" : "30747_02.htm";
			if(event.equalsIgnoreCase("30747_04.htm"))
			{
				st.set("cond", "2");
				st.takeItems(_421_LittleWingAdventures.Fairy_Leaf, -1L);
				st.giveItems(_421_LittleWingAdventures.Fairy_Leaf, 4L);
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int _state = st.getState();
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		final ItemInstance dragonflute = GetDragonflute(st);
		final int dragonflute_id = st.getInt("dragonflute");
		if(_state == 1)
		{
			if(npcId != _421_LittleWingAdventures.Cronos)
				return "noquest";
			if(st.getPlayer().getLevel() < 45)
			{
				st.exitCurrentQuest(true);
				return "30610_01.htm";
			}
			if(dragonflute == null)
			{
				st.exitCurrentQuest(true);
				return "30610_02.htm";
			}
			if(dragonflute.getEnchantLevel() < 55)
			{
				st.exitCurrentQuest(true);
				return "30610_03.htm";
			}
			st.set("cond", "0");
			st.set("dragonflute", String.valueOf(dragonflute.getObjectId()));
			return "30610_04.htm";
		}
		else
		{
			if(_state != 2)
				return "noquest";
			if(npcId != _421_LittleWingAdventures.Cronos)
			{
				if(npcId == _421_LittleWingAdventures.Mimyu)
				{
					if(st.getQuestItemsCount(_421_LittleWingAdventures.Dragon_Bugle_of_Wind) + st.getQuestItemsCount(_421_LittleWingAdventures.Dragon_Bugle_of_Star) + st.getQuestItemsCount(_421_LittleWingAdventures.Dragon_Bugle_of_Twilight) > 0L)
						return "30747_00b.htm";
					if(dragonflute == null)
						return "noquest";
					if(cond == 1)
						return "30747_01.htm";
					if(cond == 2)
					{
						if(!HatchlingSummoned(st, false))
							return "30747_09.htm";
						if(st.getQuestItemsCount(_421_LittleWingAdventures.Fairy_Leaf) == 0L)
						{
							st.playSound(Quest.SOUND_FINISH);
							st.exitCurrentQuest(true);
							return "30747_11.htm";
						}
						return "30747_10.htm";
					}
					else if(cond == 3)
					{
						if(dragonflute.getObjectId() != dragonflute_id)
							return "30747_00a.htm";
						if(st.getQuestItemsCount(_421_LittleWingAdventures.Fairy_Leaf) > 0L)
						{
							st.playSound(Quest.SOUND_FINISH);
							st.exitCurrentQuest(true);
							return "30747_11.htm";
						}
						if(!CheckTree(st, _421_LittleWingAdventures.Fairy_Tree_of_Wind) || !CheckTree(st, _421_LittleWingAdventures.Fairy_Tree_of_Star) || !CheckTree(st, _421_LittleWingAdventures.Fairy_Tree_of_Twilight) || !CheckTree(st, _421_LittleWingAdventures.Fairy_Tree_of_Abyss))
						{
							st.playSound(Quest.SOUND_FINISH);
							st.exitCurrentQuest(true);
							return "30747_11.htm";
						}
						if(st.getInt("welldone") == 0)
						{
							if(!HatchlingSummoned(st, false))
								return "30747_09.htm";
							st.set("welldone", "1");
							return "30747_12.htm";
						}
						else
						{
							if(HatchlingSummoned(st, false) || st.getPlayer().getServitor() != null)
								return "30747_13a.htm";
							st.getPlayer().sendPacket(new SystemMessage(302).addItemName(dragonflute.getItemId()));
							dragonflute.setItemId(_421_LittleWingAdventures.Dragon_Bugle_of_Wind + dragonflute.getItemId() - _421_LittleWingAdventures.Dragonflute_of_Wind);
							dragonflute.updateDatabase(true);
							st.getPlayer().sendPacket(new SystemMessage(54).addItemName(dragonflute.getItemId()));
							st.getPlayer().sendPacket(new ItemList(st.getPlayer(), false));
							st.playSound(Quest.SOUND_FINISH);
							st.exitCurrentQuest(true);
							return "30747_13.htm";
						}
					}
				}
				return "noquest";
			}
			if(dragonflute == null)
				return "30610_02.htm";
			return dragonflute.getObjectId() == dragonflute_id ? "30610_07.htm" : "30610_06.htm";
		}
	}

	@Override
	public String onAttack(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2 || st.getInt("cond") != 2 || !HatchlingSummoned(st, true) || st.getQuestItemsCount(_421_LittleWingAdventures.Fairy_Leaf) == 0L)
			return null;
		final String npcID = String.valueOf(npc.getNpcId());
		final Integer attaked_times = st.getInt(npcID);
		if(CheckTree(st, npc.getNpcId()))
			return null;
		if(attaked_times > _421_LittleWingAdventures.Min_Fairy_Tree_Attaks && Rnd.chance(attaked_times - _421_LittleWingAdventures.Min_Fairy_Tree_Attaks))
		{
			st.set(npcID, "1000000");
			Functions.npcSay(npc, "Give me the leaf!");
			st.takeItems(_421_LittleWingAdventures.Fairy_Leaf, 1L);
			if(CheckTree(st, _421_LittleWingAdventures.Fairy_Tree_of_Wind) && CheckTree(st, _421_LittleWingAdventures.Fairy_Tree_of_Star) && CheckTree(st, _421_LittleWingAdventures.Fairy_Tree_of_Twilight) && CheckTree(st, _421_LittleWingAdventures.Fairy_Tree_of_Abyss))
			{
				st.set("cond", "3");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		else
			st.set(npcID, String.valueOf(attaked_times + 1));
		return null;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2 || st.getInt("cond") != 2 || !HatchlingSummoned(st, true) || st.getQuestItemsCount(_421_LittleWingAdventures.Fairy_Leaf) == 0L || CheckTree(st, npc.getNpcId()))
			return null;
		st.set("cond", "3");
		ThreadPoolManager.getInstance().schedule(new GuardiansSpawner(npc, st, Rnd.get(15, 20)), 1000L);
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

	static
	{
		_421_LittleWingAdventures.Cronos = 30610;
		_421_LittleWingAdventures.Mimyu = 30747;
		_421_LittleWingAdventures.Hatchling_of_the_Wind = 12311;
		_421_LittleWingAdventures.Fairy_Tree_of_Wind = 27185;
		_421_LittleWingAdventures.Fairy_Tree_of_Star = 27186;
		_421_LittleWingAdventures.Fairy_Tree_of_Twilight = 27187;
		_421_LittleWingAdventures.Fairy_Tree_of_Abyss = 27188;
		_421_LittleWingAdventures.Soul_of_Tree_Guardian = 27189;
		_421_LittleWingAdventures.Dragonflute_of_Wind = 3500;
		_421_LittleWingAdventures.Dragonflute_of_Star = 3501;
		_421_LittleWingAdventures.Dragonflute_of_Twilight = 3502;
		_421_LittleWingAdventures.Dragon_Bugle_of_Wind = 4422;
		_421_LittleWingAdventures.Dragon_Bugle_of_Star = 4423;
		_421_LittleWingAdventures.Dragon_Bugle_of_Twilight = 4424;
		_421_LittleWingAdventures.Fairy_Leaf = 4325;
		_421_LittleWingAdventures.Min_Fairy_Tree_Attaks = 110;
	}

	public class GuardiansSpawner implements Runnable
	{
		private Spawn _spawn;
		private String agressor;
		private String agressors_pet;
		private List<String> agressors_party;
		private int tiks;

		public GuardiansSpawner(final NpcInstance npc, final QuestState st, final int _count)
		{
			_spawn = null;
			agressors_pet = null;
			agressors_party = null;
			tiks = 0;
			final NpcTemplate template = NpcTable.getTemplate(_421_LittleWingAdventures.Soul_of_Tree_Guardian);
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
			for(int i = 0; i < _count; ++i)
			{
				_spawn.setLoc(Location.findPointToStay(npc.getLoc(), 50, 200, npc.getGeoIndex()));
				_spawn.setHeading(Rnd.get(0, 65535));
				_spawn.setAmount(1);
				_spawn.doSpawn(true);
				agressor = st.getPlayer().getName();
				if(st.getPlayer().getServitor() != null)
					agressors_pet = st.getPlayer().getServitor().getName();
				if(st.getPlayer().getParty() != null)
				{
					agressors_party = new ArrayList<String>();
					for(final Player _member : st.getPlayer().getParty().getPartyMembers())
						if(!_member.equals(st.getPlayer()))
							agressors_party.add(_member.getName());
				}
			}
			_spawn.stopRespawn();
			updateAgression();
		}

		private void AddAgression(final Playable _player, final int aggro)
		{
			if(_player == null)
				return;
			for(final NpcInstance _mob : _spawn.getAllSpawned())
				_mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, _player, aggro);
		}

		private void updateAgression()
		{
			final Player _player = World.getPlayer(agressor);
			if(_player != null)
			{
				if(agressors_pet != null && _player.getServitor() != null && _player.getServitor().getName().equalsIgnoreCase(agressors_pet))
					AddAgression(_player.getServitor(), 10);
				AddAgression(_player, 2);
			}
			if(agressors_party != null)
				for(final String _agressor : agressors_party)
					AddAgression(World.getPlayer(_agressor), 1);
		}

		@Override
		public void run()
		{
			if(_spawn == null)
				return;
			++tiks;
			if(tiks < 600)
			{
				updateAgression();
				ThreadPoolManager.getInstance().schedule(this, 1000L);
				return;
			}
			_spawn.despawnAll();
		}
	}
}
