package quests;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.DoorTable;
import l2s.gameserver.tables.SpawnTable;
import l2s.gameserver.utils.Location;

public class _1004_IceFairySirra extends Quest implements ScriptFile
{
	private static final int STEWARD = 32029;
	private static final int SILVER_HEMOCYTE = 8057;
	private static List<NpcInstance> _allMobs;
	private static ScheduledFuture<?> _startTask;
	private static ScheduledFuture<?> _partyPortTask;
	private static ScheduledFuture<?> _30MinutesRemainingTask;
	private static ScheduledFuture<?> _20MinutesRemainingTask;
	private static ScheduledFuture<?> _10MinutesRemainingTask;
	private static ScheduledFuture<?> _endTask;
	private static ScheduledFuture<?> _respawnTask;
	private static Location[] _spawns;

	public _1004_IceFairySirra()
	{
		super("Ice Fairy Sirra", false);
		addFirstTalkId(new int[] { 32029 });
		this.addKillId(new int[] { 32029, 22100, 22102, 22104, 29056 });
		openGates();
	}

	public static void spawnTask()
	{
		final long remain = ServerVariables.getLong("Sirra_Respawn", 0L) - System.currentTimeMillis();
		if(remain <= 0L)
			setBusy(false);
		else
		{
			setBusy(true);
			_1004_IceFairySirra._respawnTask = ThreadPoolManager.getInstance().schedule(new EventTask("respawn", null), remain);
		}
	}

	@Override
	public String onFirstTalk(final NpcInstance npc, final Player player)
	{
		final QuestState hostQuest = player.getQuestState(10285);
		if(hostQuest != null && hostQuest.getInt("cond") == 7)
			return "";
		if(player.getQuestState(getId()) == null)
			newQuestState(player, 2);
		if(npc.isBusy())
			return "32029-10.htm";
		return "32029.htm";
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final Player player = st.getPlayer();
		if(!event.equalsIgnoreCase("check_condition"))
			return event;
		if(player.getLevel() >= 82)
		{
			player.teleToLocation(103045, -124361, -2768);
			return "";
		}
		if(!player.isInParty())
			return "32029-1a.htm";
		if(player.getParty().getPartyLeader().getObjectId() != player.getObjectId())
			return "32029-1.htm";
		if(checkItems(player))
		{
			cleanUp();
			setBusy(true);
			_1004_IceFairySirra._startTask = ThreadPoolManager.getInstance().schedule(new EventTask("start", player), 100000L);
			destroyItems(player);
			st.giveItems(8379, 3L);
			screenMessage(player, "Steward: Please wait a moment.", 100000);
			return "32029-3.htm";
		}
		return "32029-2.htm";
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(npc.getNpcId() == 29056)
		{
			cleanUp();
			setBusy(true);
			final int respawn_delay = Rnd.get(43200000, 129600000);
			ServerVariables.set("Sirra_Respawn", System.currentTimeMillis() + respawn_delay);
			_1004_IceFairySirra._respawnTask = ThreadPoolManager.getInstance().schedule(new EventTask("respawn", null), respawn_delay);
			screenMessage(st.getPlayer(), "Steward: Thank you for restoring the Queen's appearance!", 10000);
		}
		return null;
	}

	public static void cleanUp()
	{
		if(_1004_IceFairySirra._startTask != null)
		{
			_1004_IceFairySirra._startTask.cancel(false);
			_1004_IceFairySirra._startTask = null;
		}
		if(_1004_IceFairySirra._partyPortTask != null)
		{
			_1004_IceFairySirra._partyPortTask.cancel(false);
			_1004_IceFairySirra._partyPortTask = null;
		}
		if(_1004_IceFairySirra._30MinutesRemainingTask != null)
		{
			_1004_IceFairySirra._30MinutesRemainingTask.cancel(false);
			_1004_IceFairySirra._30MinutesRemainingTask = null;
		}
		if(_1004_IceFairySirra._20MinutesRemainingTask != null)
		{
			_1004_IceFairySirra._20MinutesRemainingTask.cancel(false);
			_1004_IceFairySirra._20MinutesRemainingTask = null;
		}
		if(_1004_IceFairySirra._10MinutesRemainingTask != null)
		{
			_1004_IceFairySirra._10MinutesRemainingTask.cancel(false);
			_1004_IceFairySirra._10MinutesRemainingTask = null;
		}
		if(_1004_IceFairySirra._endTask != null)
		{
			_1004_IceFairySirra._endTask.cancel(false);
			_1004_IceFairySirra._endTask = null;
		}
		if(_1004_IceFairySirra._respawnTask != null)
		{
			_1004_IceFairySirra._respawnTask.cancel(false);
			_1004_IceFairySirra._respawnTask = null;
		}
		for(final NpcInstance mob : _1004_IceFairySirra._allMobs)
			mob.deleteMe();
		_1004_IceFairySirra._allMobs.clear();
		setBusy(false);
	}

	public static void setBusy(final boolean value)
	{
		final NpcInstance steward = findTemplate(32029);
		if(steward != null)
			steward.setBusy(value);
	}

	public static NpcInstance findTemplate(final int npcId)
	{
		for(final Spawn spawn : SpawnTable.getInstance().getSpawnTable())
			if(spawn.getNpcId() == npcId)
				return spawn.getLastSpawn();
		return null;
	}

	public static void openGates()
	{
		for(int i = 23140001; i < 23140003; ++i)
			DoorTable.getInstance().getDoor(i).openMe();
	}

	public static void closeGates()
	{
		for(int i = 23140001; i < 23140003; ++i)
			DoorTable.getInstance().getDoor(i).closeMe();
	}

	public static boolean checkItems(final Player player)
	{
		if(player.getParty() == null)
			return false;
		for(final Player pc : player.getParty().getPartyMembers())
		{
			final ItemInstance i = pc.getInventory().getItemByItemId(8057);
			if(i == null || i.getCount() < 10L)
				return false;
		}
		return true;
	}

	public static void destroyItems(final Player player)
	{
		final Party party = player.getParty();
		if(party != null)
			for(final Player pc : party.getPartyMembers())
				pc.getInventory().destroyItemByItemId(8057, 10L, true);
		else
			cleanUp();
	}

	public static void teleportInside(final Player player)
	{
		if(player.getParty() != null)
			for(final Player pc : player.getParty().getPartyMembers())
				pc.teleToLocation(113533, -126159, -3488);
		else
			cleanUp();
	}

	public static void screenMessage(final Player player, final String text, final int time)
	{
		if(player.getParty() != null)
			for(final Player pc : player.getParty().getPartyMembers())
				pc.sendPacket(new ExShowScreenMessage(text, time));
		else
			cleanUp();
	}

	public static void doSpawns()
	{
		for(final Location spawn : _1004_IceFairySirra._spawns)
			_1004_IceFairySirra._allMobs.add(Functions.spawn(spawn, spawn.id));
	}

	@Override
	public void onLoad()
	{
		//
	}

	@Override
	public void onReload()
	{
		//
	}

	@Override
	public void onShutdown()
	{
		//
	}

	@Override
	public boolean isVisible(Player player)
	{
		return false;
	}

	static
	{
		_1004_IceFairySirra._allMobs = new ArrayList<NpcInstance>();
		_1004_IceFairySirra._startTask = null;
		_1004_IceFairySirra._partyPortTask = null;
		_1004_IceFairySirra._30MinutesRemainingTask = null;
		_1004_IceFairySirra._20MinutesRemainingTask = null;
		_1004_IceFairySirra._10MinutesRemainingTask = null;
		_1004_IceFairySirra._endTask = null;
		_1004_IceFairySirra._respawnTask = null;
		_1004_IceFairySirra._spawns = new Location[] {
				new Location(105546, -127892, -2768, 0, 29060),
				new Location(102779, -125920, -2840, 0, 29056),
				new Location(111719, -126646, -2992, 0, 22100),
				new Location(109509, -128946, -3216, 0, 22102),
				new Location(109680, -125756, -3136, 0, 22104) };
	}

	private static class EventTask implements Runnable
	{
		String event;
		Player player;

		public EventTask(final String event, final Player player)
		{
			this.event = event;
			this.player = player;
		}

		@Override
		public void run()
		{
			if(event.equalsIgnoreCase("start"))
			{
				_1004_IceFairySirra.closeGates();
				_1004_IceFairySirra.doSpawns();
				_1004_IceFairySirra._partyPortTask = ThreadPoolManager.getInstance().schedule(new EventTask("Party_Port", player), 2000L);
				_1004_IceFairySirra._endTask = ThreadPoolManager.getInstance().schedule(new EventTask("End", player), 1802000L);
				_1004_IceFairySirra._startTask = null;
			}
			else if(event.equalsIgnoreCase("Party_Port"))
			{
				_1004_IceFairySirra.teleportInside(player);
				_1004_IceFairySirra.screenMessage(player, "Steward: Please restore the Queen's appearance!", 10000);
				_1004_IceFairySirra._30MinutesRemainingTask = ThreadPoolManager.getInstance().schedule(new EventTask("30MinutesRemaining", player), 300000L);
				_1004_IceFairySirra._partyPortTask = null;
			}
			else if(event.equalsIgnoreCase("30MinutesRemaining"))
			{
				_1004_IceFairySirra.screenMessage(player, "30 minute(s) are remaining.", 10000);
				_1004_IceFairySirra._20MinutesRemainingTask = ThreadPoolManager.getInstance().schedule(new EventTask("20MinutesRemaining", player), 600000L);
				_1004_IceFairySirra._30MinutesRemainingTask = null;
			}
			else if(event.equalsIgnoreCase("20MinutesRemaining"))
			{
				_1004_IceFairySirra.screenMessage(player, "20 minute(s) are remaining.", 10000);
				_1004_IceFairySirra._10MinutesRemainingTask = ThreadPoolManager.getInstance().schedule(new EventTask("10MinutesRemaining", player), 600000L);
				_1004_IceFairySirra._20MinutesRemainingTask = null;
			}
			else if(event.equalsIgnoreCase("10MinutesRemaining"))
			{
				_1004_IceFairySirra.screenMessage(player, "Steward: Waste no time! Please hurry!", 10000);
				_1004_IceFairySirra._10MinutesRemainingTask = null;
			}
			else if(event.equalsIgnoreCase("End"))
			{
				_1004_IceFairySirra.screenMessage(player, "Steward: Was it indeed too much to ask.", 10000);
				_1004_IceFairySirra.cleanUp();
			}
			else if(event.equalsIgnoreCase("respawn"))
				_1004_IceFairySirra.cleanUp();
		}
	}
}
