package bosses;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.model.CommandChannel;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.DoorTable;

public class LastImperialTombManager extends Functions implements ScriptFile
{
	private static Logger _log = LoggerFactory.getLogger(LastImperialTombManager.class);

	private static boolean _isInvaded;
	private static List<NpcInstance> _hallAlarmDevices;
	private static List<NpcInstance> _darkChoirCaptains;
	private static List<NpcInstance> _room1Monsters;
	private static List<NpcInstance> _room2InsideMonsters;
	private static List<NpcInstance> _room2OutsideMonsters;
	private static List<DoorInstance> _room1Doors;
	private static List<DoorInstance> _room2InsideDoors;
	private static DoorInstance _room2OutsideDoor1;
	private static DoorInstance _room2OutsideDoor2;
	private static DoorInstance _room3Door;
	private static Player _commander;
	private static final int SCROLL = 8073;
	private static final int DewdropItem = 8556;
	private static final int[] blockNpcs;
	private static boolean _isReachToHall;
	private static final int[][] _invadeLoc;
	private static ScheduledFuture<?> _InvadeTask;
	private static ScheduledFuture<?> _RegistrationTimeInfoTask;
	private static ScheduledFuture<?> _Room1SpawnTask;
	private static ScheduledFuture<?> _Room2InsideDoorOpenTask;
	private static ScheduledFuture<?> _Room2OutsideSpawnTask;
	private static ScheduledFuture<?> _CheckTimeUpTask;
	private static Zone _zone;
	private static final int ALARM_DEVICE = 18328;
	private static final int CHOIR_PRAYER = 18339;
	private static final int CHOIR_CAPTAIN = 18334;
	private static final int LIT_MAX_PARTY_CNT = 5;
	private static final int LIT_TIME_LIMIT = 35;

	private static void init()
	{
		LastImperialTombManager._zone = ZoneManager.getInstance().getZoneById(Zone.ZoneType.epic, 702121, false);
		LastImperialTombSpawnlist.clear();
		LastImperialTombSpawnlist.fill();
		initDoors();
		_log.info("LastImperialTombManager: Init The Last Imperial Tomb.");
	}

	private static void initDoors()
	{
		LastImperialTombManager._room1Doors.clear();
		LastImperialTombManager._room1Doors.add(DoorTable.getInstance().getDoor(25150042));
		for(int i = 25150051; i <= 25150058; ++i)
			LastImperialTombManager._room1Doors.add(DoorTable.getInstance().getDoor(i));
		LastImperialTombManager._room2InsideDoors.clear();
		for(int i = 25150061; i <= 25150070; ++i)
			LastImperialTombManager._room2InsideDoors.add(DoorTable.getInstance().getDoor(i));
		LastImperialTombManager._room2OutsideDoor1 = DoorTable.getInstance().getDoor(25150043);
		LastImperialTombManager._room2OutsideDoor2 = DoorTable.getInstance().getDoor(25150045);
		LastImperialTombManager._room3Door = DoorTable.getInstance().getDoor(25150046);
		for(final DoorInstance door : LastImperialTombManager._room1Doors)
			door.closeMe();
		for(final DoorInstance door : LastImperialTombManager._room2InsideDoors)
			door.closeMe();
		LastImperialTombManager._room2OutsideDoor1.closeMe();
		LastImperialTombManager._room2OutsideDoor2.closeMe();
		LastImperialTombManager._room3Door.closeMe();
	}

	public void tryRegistration()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
			return;
		if(npc.getNpcId() != 32011 && npc.getNpcId() != 70032)
		{
			player.sendMessage("\u0412\u044b \u043b\u043e\u0445!");
			return;
		}
		if(FrintezzaManager.isEnableEnterToLair() == 2)
		{
			player.sendMessage("Frintezza is already awake! You may not enter now.");
			return;
		}
		if(FrintezzaManager.isEnableEnterToLair() != 1)
		{
			player.sendMessage("Frintezza is still reborning. Currently no entry possible.");
			return;
		}
		if(LastImperialTombManager._isInvaded)
		{
			player.sendMessage("Another group is already fighting inside the Imperial Tomb.");
			return;
		}
		if(player.getParty() == null || !player.getParty().isInCommandChannel())
		{
			player.sendMessage("You cannot enter because you are not in a current command channel.");
			return;
		}
		final CommandChannel cc = player.getParty().getCommandChannel();
		for(final Party party : cc.getParties())
			if(party != null && party.getMemberCount() < Config.LIT_PARTY_MEM)
			{
				if(!player.isLangRus())
					player.sendMessage("Required " + Config.LIT_PARTY_MEM + " or more members in every party.");
				else
					player.sendMessage("\u0412 \u043a\u0430\u0436\u0434\u043e\u0439 \u0433\u0440\u0443\u043f\u043f\u0435 \u0434\u043e\u043b\u0436\u043d\u043e \u0431\u044b\u0442\u044c \u043d\u0435 \u043c\u0435\u043d\u0435\u0435 " + Config.LIT_PARTY_MEM + " \u0447\u043b\u0435\u043d\u043e\u0432.");
				return;
			}
		for(final Player member : cc.getMembers())
			if(member != null)
			{
				if(!player.isInRange(member, 900L))
				{
					if(!member.isLangRus())
						member.sendMessage("It's too far from " + player.getName() + " to work.");
					else
						member.sendMessage("\u0412\u044b \u043d\u0430\u0445\u043e\u0434\u0438\u0442\u0435\u0441\u044c \u0441\u043b\u0438\u0448\u043a\u043e\u043c \u0434\u0430\u043b\u0435\u043a\u043e \u043e\u0442 " + player.getName() + ".");
					if(!player.isLangRus())
						player.sendMessage(member.getName() + " too far from you to work.");
					else
						player.sendMessage(member.getName() + " \u043d\u0430\u0445\u043e\u0434\u0438\u0442\u0441\u044f \u0441\u043b\u0438\u0448\u043a\u043e\u043c \u0434\u0430\u043b\u0435\u043a\u043e \u043e\u0442 \u0412\u0430\u0441.");
					return;
				}
				if(member.getLevel() < 74)
				{
					if(!player.isLangRus())
						player.sendMessage("All members must be 74 or higher level.");
					else
						player.sendMessage("\u0412\u0441\u0435 \u0447\u043b\u0435\u043d\u044b \u0434\u043e\u043b\u0436\u043d\u044b \u0431\u044b\u0442\u044c \u043d\u0435 \u043c\u0435\u043d\u0435\u0435 74-\u0433\u043e \u0443\u0440\u043e\u0432\u043d\u044f.");
					return;
				}
				continue;
			}
		if(cc.getChannelLeader() != player)
		{
			player.sendMessage("You must be leader of the command channel.");
			return;
		}
		if(cc.getParties().size() < Config.LIT_PARTY_MIN)
		{
			player.sendMessage("The command channel must contains at least " + Config.LIT_PARTY_MIN + " parties.");
			return;
		}
		if(cc.getParties().size() > 5)
		{
			player.sendMessage("The command channel must contains not more than 5 parties.");
			return;
		}
		if(player.getInventory().getCountOf(8073) < 1L)
		{
			player.sendMessage("You must possess a \"Frintezza's Magic Force Field Removal Scroll\".");
			return;
		}
		registration(player);
	}

	private static synchronized void registration(final Player pc)
	{
		if(LastImperialTombManager._commander != null)
			return;
		LastImperialTombManager._commander = pc;
		pc.getInventory().destroyItemByItemId(8073, 1L, true);
		if(LastImperialTombManager._InvadeTask != null)
			LastImperialTombManager._InvadeTask.cancel(true);
		LastImperialTombManager._InvadeTask = ThreadPoolManager.getInstance().schedule(new Invade(), 10000L);
	}

	private static void doInvade()
	{
		final Party party = LastImperialTombManager._commander.getParty();
		if(party == null)
		{
			LastImperialTombManager._commander = null;
			return;
		}
		final CommandChannel channel = party.getCommandChannel();
		if(channel == null)
		{
			LastImperialTombManager._commander = null;
			return;
		}
		final List<Party> parties = channel.getParties();
		if(parties == null || parties.size() < Config.LIT_PARTY_MIN || parties.size() > 5)
		{
			LastImperialTombManager._commander = null;
			return;
		}
		int locId = 0;
		for(final Party pt : parties)
		{
			if(locId >= 5)
				locId = 0;
			for(final Player pc : pt.getPartyMembers())
				pc.teleToLocation(LastImperialTombManager._invadeLoc[locId][0] + Rnd.get(50), LastImperialTombManager._invadeLoc[locId][1] + Rnd.get(50), LastImperialTombManager._invadeLoc[locId][2]);
			++locId;
		}
		initDoors();
		LastImperialTombManager._isInvaded = true;
		for(final Spawn spawn : LastImperialTombSpawnlist.getRoom1SpawnList1st())
			if(spawn.getNpcId() == 18328)
			{
				final NpcInstance mob = spawn.doSpawn(true);
				mob.getSpawn().stopRespawn();
				LastImperialTombManager._hallAlarmDevices.add(mob);
			}
		if(LastImperialTombManager._Room1SpawnTask != null)
			LastImperialTombManager._Room1SpawnTask.cancel(true);
		LastImperialTombManager._Room1SpawnTask = ThreadPoolManager.getInstance().schedule(new SpawnRoom1Mobs1st(), 5000L);
		if(LastImperialTombManager._CheckTimeUpTask != null)
			LastImperialTombManager._CheckTimeUpTask.cancel(true);
		LastImperialTombManager._CheckTimeUpTask = ThreadPoolManager.getInstance().schedule(new CheckTimeUp(2100000), 15000L);
	}

	public static void OnDie(final Creature self, final Creature killer)
	{
		if(self == null)
			return;
		if(ArrayUtils.contains(LastImperialTombManager.blockNpcs, self.getNpcId()) && Rnd.chance(10))
			((NpcInstance) self).dropItem(killer.getPlayer(), 8556, 1L);
		switch(self.getNpcId())
		{
			case 18328:
			{
				onKillHallAlarmDevice();
				break;
			}
			case 18339:
			{
				onKillDarkChoirPlayer();
				break;
			}
			case 18334:
			{
				if(Rnd.chance(Config.BA_CHANCE))
					((NpcInstance) self).dropItem(killer.getPlayer(), 8192, Rnd.get(Config.BA_MIN, Config.BA_MAX));
				onKillDarkChoirCaptain();
				break;
			}
		}
	}

	private static void onKillHallAlarmDevice()
	{
		int killCnt = 0;
		for(final NpcInstance HallAlarmDevice : LastImperialTombManager._hallAlarmDevices)
			if(HallAlarmDevice.isDead())
				++killCnt;
		switch(killCnt)
		{
			case 1:
			{
				if(LastImperialTombManager._Room1SpawnTask != null)
					LastImperialTombManager._Room1SpawnTask.cancel(true);
				LastImperialTombManager._Room1SpawnTask = ThreadPoolManager.getInstance().schedule(new SpawnRoom1Mobs2nd(), 3000L);
				break;
			}
			case 2:
			{
				if(LastImperialTombManager._Room1SpawnTask != null)
					LastImperialTombManager._Room1SpawnTask.cancel(true);
				LastImperialTombManager._Room1SpawnTask = ThreadPoolManager.getInstance().schedule(new SpawnRoom1Mobs3rd(), 3000L);
				break;
			}
			case 3:
			{
				if(LastImperialTombManager._Room1SpawnTask != null)
					LastImperialTombManager._Room1SpawnTask.cancel(true);
				LastImperialTombManager._Room1SpawnTask = ThreadPoolManager.getInstance().schedule(new SpawnRoom1Mobs4th(), 3000L);
				break;
			}
			case 4:
			{
				if(LastImperialTombManager._Room1SpawnTask != null)
				{
					LastImperialTombManager._Room1SpawnTask.cancel(true);
					LastImperialTombManager._Room1SpawnTask = null;
				}
				openRoom1Doors();
				LastImperialTombManager._room2OutsideDoor1.openMe();
				spawnRoom2InsideMob();
				break;
			}
		}
	}

	private static void onKillDarkChoirPlayer()
	{
		int killCnt = 0;
		for(final NpcInstance DarkChoirPlayer : LastImperialTombManager._room2InsideMonsters)
			if(DarkChoirPlayer.isDead())
				++killCnt;
		if(LastImperialTombManager._room2InsideMonsters.size() <= killCnt)
		{
			if(LastImperialTombManager._Room2InsideDoorOpenTask != null)
				LastImperialTombManager._Room2InsideDoorOpenTask.cancel(true);
			if(LastImperialTombManager._Room2OutsideSpawnTask != null)
				LastImperialTombManager._Room2OutsideSpawnTask.cancel(true);
			LastImperialTombManager._Room2InsideDoorOpenTask = ThreadPoolManager.getInstance().schedule(new OpenRoom2InsideDoors(), 3000L);
			LastImperialTombManager._Room2OutsideSpawnTask = ThreadPoolManager.getInstance().schedule(new SpawnRoom2OutsideMobs(), 4000L);
		}
	}

	private static void onKillDarkChoirCaptain()
	{
		int killCnt = 0;
		for(final NpcInstance DarkChoirCaptain : LastImperialTombManager._darkChoirCaptains)
			if(DarkChoirCaptain.isDead())
				++killCnt;
		if(LastImperialTombManager._darkChoirCaptains.size() <= killCnt)
		{
			LastImperialTombManager._room2OutsideDoor1.openMe();
			LastImperialTombManager._room2OutsideDoor2.openMe();
			LastImperialTombManager._room3Door.openMe();
			for(final NpcInstance mob : LastImperialTombManager._room2OutsideMonsters)
				mob.deleteMe();
			for(final NpcInstance DarkChoirCaptain : LastImperialTombManager._darkChoirCaptains)
				DarkChoirCaptain.deleteMe();
		}
	}

	private static void openRoom1Doors()
	{
		for(final NpcInstance npc : LastImperialTombManager._hallAlarmDevices)
			npc.deleteMe();
		for(final NpcInstance npc : LastImperialTombManager._room1Monsters)
			npc.deleteMe();
		for(final DoorInstance door : LastImperialTombManager._room1Doors)
			door.openMe();
	}

	private static void spawnRoom2InsideMob()
	{
		for(final Spawn spawn : LastImperialTombSpawnlist.getRoom2InsideSpawnList())
		{
			final NpcInstance mob = spawn.doSpawn(true);
			mob.getSpawn().stopRespawn();
			LastImperialTombManager._room2InsideMonsters.add(mob);
		}
	}

	public static void setReachToHall()
	{
		LastImperialTombManager._isReachToHall = true;
	}

	private static void doCheckTimeUp(int remaining)
	{
		if(LastImperialTombManager._isReachToHall)
			return;
		String text = "";
		int interval;
		if(remaining > 300000)
		{
			final int timeLeft = remaining / 60000;
			interval = 300000;
			text = timeLeft + " minute(s) are remaining.";
			remaining -= 300000;
		}
		else if(remaining > 60000)
		{
			final int timeLeft = remaining / 60000;
			interval = 60000;
			text = timeLeft + " minute(s) are remaining.";
			remaining -= 60000;
		}
		else if(remaining > 30000)
		{
			final int timeLeft = remaining / 1000;
			interval = 30000;
			text = timeLeft + " second(s) are remaining.";
			remaining -= 30000;
		}
		else
		{
			final int timeLeft = remaining / 1000;
			interval = 10000;
			text = timeLeft + " second(s) are remaining.";
			remaining -= 10000;
		}
		final ExShowScreenMessage msg = new ExShowScreenMessage(text, 3000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, false);
		for(final Player player : getPlayersInside())
			player.sendPacket(msg);
		if(LastImperialTombManager._CheckTimeUpTask != null)
			LastImperialTombManager._CheckTimeUpTask.cancel(true);
		if(remaining >= 10000)
			LastImperialTombManager._CheckTimeUpTask = ThreadPoolManager.getInstance().schedule(new CheckTimeUp(remaining), interval);
		else
			LastImperialTombManager._CheckTimeUpTask = ThreadPoolManager.getInstance().schedule(new TimeUp(), interval);
	}

	public static void cleanUpTomb(final boolean banish)
	{
		initDoors();
		cleanUpMobs();
		if(banish)
			banishForeigners();
		LastImperialTombManager._commander = null;
		LastImperialTombManager._isInvaded = false;
		LastImperialTombManager._isReachToHall = false;
		if(LastImperialTombManager._InvadeTask != null)
			LastImperialTombManager._InvadeTask.cancel(true);
		if(LastImperialTombManager._RegistrationTimeInfoTask != null)
			LastImperialTombManager._RegistrationTimeInfoTask.cancel(true);
		if(LastImperialTombManager._Room1SpawnTask != null)
			LastImperialTombManager._Room1SpawnTask.cancel(true);
		if(LastImperialTombManager._Room2InsideDoorOpenTask != null)
			LastImperialTombManager._Room2InsideDoorOpenTask.cancel(true);
		if(LastImperialTombManager._Room2OutsideSpawnTask != null)
			LastImperialTombManager._Room2OutsideSpawnTask.cancel(true);
		if(LastImperialTombManager._CheckTimeUpTask != null)
			LastImperialTombManager._CheckTimeUpTask.cancel(true);
		LastImperialTombManager._InvadeTask = null;
		LastImperialTombManager._RegistrationTimeInfoTask = null;
		LastImperialTombManager._Room1SpawnTask = null;
		LastImperialTombManager._Room2InsideDoorOpenTask = null;
		LastImperialTombManager._Room2OutsideSpawnTask = null;
		LastImperialTombManager._CheckTimeUpTask = null;
	}

	private static void cleanUpMobs()
	{
		for(final NpcInstance mob : LastImperialTombManager._hallAlarmDevices)
			mob.deleteMe();
		for(final NpcInstance mob : LastImperialTombManager._darkChoirCaptains)
			mob.deleteMe();
		for(final NpcInstance mob : LastImperialTombManager._room1Monsters)
			mob.deleteMe();
		for(final NpcInstance mob : LastImperialTombManager._room2InsideMonsters)
			mob.deleteMe();
		for(final NpcInstance mob : LastImperialTombManager._room2OutsideMonsters)
			mob.deleteMe();
		LastImperialTombManager._hallAlarmDevices.clear();
		LastImperialTombManager._darkChoirCaptains.clear();
		LastImperialTombManager._room1Monsters.clear();
		LastImperialTombManager._room2InsideMonsters.clear();
		LastImperialTombManager._room2OutsideMonsters.clear();
	}

	private static void banishForeigners()
	{
		for(final Player player : getPlayersInside())
			if(!player.isGM())
				player.teleToClosestTown();
	}

	private static List<Player> getPlayersInside()
	{
		return LastImperialTombManager._zone.getInsidePlayers();
	}

	public static Zone getZone()
	{
		return LastImperialTombManager._zone;
	}

	@Override
	public void onLoad()
	{
		init();
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		LastImperialTombManager._isInvaded = false;
		LastImperialTombManager._hallAlarmDevices = new ArrayList<NpcInstance>();
		LastImperialTombManager._darkChoirCaptains = new ArrayList<NpcInstance>();
		LastImperialTombManager._room1Monsters = new ArrayList<NpcInstance>();
		LastImperialTombManager._room2InsideMonsters = new ArrayList<NpcInstance>();
		LastImperialTombManager._room2OutsideMonsters = new ArrayList<NpcInstance>();
		LastImperialTombManager._room1Doors = new ArrayList<DoorInstance>();
		LastImperialTombManager._room2InsideDoors = new ArrayList<DoorInstance>();
		LastImperialTombManager._room3Door = null;
		LastImperialTombManager._commander = null;
		blockNpcs = new int[] { 18334, 18335, 18336, 18337, 18338 };
		LastImperialTombManager._isReachToHall = false;
		_invadeLoc = new int[][] {
				{ 173235, -76884, -5107 },
				{ 175003, -76933, -5107 },
				{ 174196, -76190, -5107 },
				{ 174013, -76120, -5107 },
				{ 173263, -75161, -5107 } };
		LastImperialTombManager._InvadeTask = null;
		LastImperialTombManager._RegistrationTimeInfoTask = null;
		LastImperialTombManager._Room1SpawnTask = null;
		LastImperialTombManager._Room2InsideDoorOpenTask = null;
		LastImperialTombManager._Room2OutsideSpawnTask = null;
		LastImperialTombManager._CheckTimeUpTask = null;
	}

	public static class SpawnRoom1Mobs1st implements Runnable
	{
		@Override
		public void run()
		{
			for(final Spawn spawn : LastImperialTombSpawnlist.getRoom1SpawnList1st())
				if(spawn.getNpcId() != 18328)
				{
					final NpcInstance mob = spawn.doSpawn(true);
					mob.getSpawn().stopRespawn();
					LastImperialTombManager._room1Monsters.add(mob);
				}
			if(Rnd.chance(90))
				LastImperialTombManager._Room1SpawnTask = ThreadPoolManager.getInstance().schedule(new SpawnRoom1Mobs1st(), 15000L);
		}
	}

	public static class SpawnRoom1Mobs2nd implements Runnable
	{
		@Override
		public void run()
		{
			for(final Spawn spawn : LastImperialTombSpawnlist.getRoom1SpawnList2nd())
			{
				final NpcInstance mob = spawn.doSpawn(true);
				mob.getSpawn().stopRespawn();
				LastImperialTombManager._room1Monsters.add(mob);
			}
			if(Rnd.chance(90))
				LastImperialTombManager._Room1SpawnTask = ThreadPoolManager.getInstance().schedule(new SpawnRoom1Mobs2nd(), 15000L);
		}
	}

	public static class SpawnRoom1Mobs3rd implements Runnable
	{
		@Override
		public void run()
		{
			for(final Spawn spawn : LastImperialTombSpawnlist.getRoom1SpawnList3rd())
			{
				final NpcInstance mob = spawn.doSpawn(true);
				mob.getSpawn().stopRespawn();
				LastImperialTombManager._room1Monsters.add(mob);
			}
			if(Rnd.chance(75))
				LastImperialTombManager._Room1SpawnTask = ThreadPoolManager.getInstance().schedule(new SpawnRoom1Mobs3rd(), 15000L);
		}
	}

	public static class SpawnRoom1Mobs4th implements Runnable
	{
		@Override
		public void run()
		{
			for(final Spawn spawn : LastImperialTombSpawnlist.getRoom1SpawnList4th())
			{
				final NpcInstance mob = spawn.doSpawn(true);
				mob.getSpawn().stopRespawn();
				LastImperialTombManager._room1Monsters.add(mob);
			}
			if(Rnd.chance(90))
				LastImperialTombManager._Room1SpawnTask = ThreadPoolManager.getInstance().schedule(new SpawnRoom1Mobs4th(), 15000L);
		}
	}

	public static class OpenRoom2InsideDoors implements Runnable
	{
		@Override
		public void run()
		{
			LastImperialTombManager._room2OutsideDoor1.closeMe();
			for(final DoorInstance door : LastImperialTombManager._room2InsideDoors)
				door.openMe();
		}
	}

	public static class SpawnRoom2OutsideMobs implements Runnable
	{
		@Override
		public void run()
		{
			for(final Spawn spawn : LastImperialTombSpawnlist.getRoom2OutsideSpawnList())
				if(spawn.getNpcId() == 18334)
				{
					final NpcInstance mob = spawn.doSpawn(true);
					mob.getSpawn().stopRespawn();
					LastImperialTombManager._darkChoirCaptains.add(mob);
				}
				else
				{
					final NpcInstance mob = spawn.doSpawn(true);
					mob.getSpawn().startRespawn();
					LastImperialTombManager._room2OutsideMonsters.add(mob);
				}
		}
	}

	public static class Invade implements Runnable
	{
		@Override
		public void run()
		{
			doInvade();
		}
	}

	private static class CheckTimeUp implements Runnable
	{
		private int _remaining;

		public CheckTimeUp(final int remaining)
		{
			_remaining = remaining;
		}

		@Override
		public void run()
		{
			doCheckTimeUp(_remaining);
		}
	}

	public static class TimeUp implements Runnable
	{
		@Override
		public void run()
		{
			LastImperialTombManager.cleanUpTomb(true);
		}
	}
}
