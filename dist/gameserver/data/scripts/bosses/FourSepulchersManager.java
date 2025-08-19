package bosses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Location;
import npc.model.SepulcherNpcInstance;

public class FourSepulchersManager extends Functions implements ScriptFile
{
	private static Logger _log;
	public static final String QUEST_ID = "_620_FourGoblets";
	private static Zone[] _zone;
	private static final int ENTRANCE_PASS = 7075;
	private static final int USED_PASS = 7261;
	private static final int CHAPEL_KEY = 7260;
	private static final int ANTIQUE_BROOCH = 7262;
	private static boolean _inEntryTime;
	private static boolean _inAttackTime;
	private static ScheduledFuture<?> _changeCoolDownTimeTask;
	private static ScheduledFuture<?> _changeEntryTimeTask;
	private static ScheduledFuture<?> _changeWarmUpTimeTask;
	private static ScheduledFuture<?> _changeAttackTimeTask;
	private static long _coolDownTimeEnd;
	private static long _entryTimeEnd;
	private static long _warmUpTimeEnd;
	private static long _attackTimeEnd;
	private static byte _newCycleMin;
	private static boolean _firstTimeRun;

	public static void init()
	{
		FourSepulchersManager._zone[0] = ZoneManager.getInstance().getZoneById(Zone.ZoneType.epic, 702110, false);
		FourSepulchersManager._zone[1] = ZoneManager.getInstance().getZoneById(Zone.ZoneType.epic, 702111, false);
		FourSepulchersManager._zone[2] = ZoneManager.getInstance().getZoneById(Zone.ZoneType.epic, 702112, false);
		FourSepulchersManager._zone[3] = ZoneManager.getInstance().getZoneById(Zone.ZoneType.epic, 702113, false);
		if(FourSepulchersManager._changeCoolDownTimeTask != null)
			FourSepulchersManager._changeCoolDownTimeTask.cancel(false);
		if(FourSepulchersManager._changeEntryTimeTask != null)
			FourSepulchersManager._changeEntryTimeTask.cancel(false);
		if(FourSepulchersManager._changeWarmUpTimeTask != null)
			FourSepulchersManager._changeWarmUpTimeTask.cancel(false);
		if(FourSepulchersManager._changeAttackTimeTask != null)
			FourSepulchersManager._changeAttackTimeTask.cancel(false);
		FourSepulchersManager._changeCoolDownTimeTask = null;
		FourSepulchersManager._changeEntryTimeTask = null;
		FourSepulchersManager._changeWarmUpTimeTask = null;
		FourSepulchersManager._changeAttackTimeTask = null;
		FourSepulchersManager._inEntryTime = false;
		FourSepulchersManager._inAttackTime = false;
		FourSepulchersManager._firstTimeRun = true;
		FourSepulchersSpawn.init();
		timeSelector();
	}

	private static void timeSelector()
	{
		timeCalculator();
		final long currentTime = System.currentTimeMillis();
		if(currentTime >= FourSepulchersManager._coolDownTimeEnd && currentTime < FourSepulchersManager._entryTimeEnd)
		{
			cleanUp();
			FourSepulchersManager._changeEntryTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeEntryTime(), 0L);
		}
		else if(currentTime >= FourSepulchersManager._entryTimeEnd && currentTime < FourSepulchersManager._warmUpTimeEnd)
		{
			cleanUp();
			FourSepulchersManager._changeWarmUpTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeWarmUpTime(), 0L);
		}
		else if(currentTime >= FourSepulchersManager._warmUpTimeEnd && currentTime < FourSepulchersManager._attackTimeEnd)
		{
			cleanUp();
			FourSepulchersManager._changeAttackTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeAttackTime(), 0L);
		}
		else
			FourSepulchersManager._changeCoolDownTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeCoolDownTime(), 0L);
	}

	private static void timeCalculator()
	{
		final Calendar tmp = Calendar.getInstance();
		if(tmp.get(12) < FourSepulchersManager._newCycleMin)
			tmp.set(10, Calendar.getInstance().get(10) - 1);
		tmp.set(12, FourSepulchersManager._newCycleMin);
		FourSepulchersManager._coolDownTimeEnd = tmp.getTimeInMillis();
		FourSepulchersManager._entryTimeEnd = FourSepulchersManager._coolDownTimeEnd + 180000L;
		FourSepulchersManager._warmUpTimeEnd = FourSepulchersManager._entryTimeEnd + 120000L;
		FourSepulchersManager._attackTimeEnd = FourSepulchersManager._warmUpTimeEnd + 3000000L;
	}

	private static void cleanUp()
	{
		FourSepulchersSpawn._shadows.clear();
		for(final Player player : getPlayersInside())
			player.teleToClosestTown();
		FourSepulchersSpawn.deleteAllMobs();
		FourSepulchersSpawn.closeAllDoors();
		FourSepulchersSpawn._hallInUse.clear();
		FourSepulchersSpawn._hallInUse.put(31921, false);
		FourSepulchersSpawn._hallInUse.put(31922, false);
		FourSepulchersSpawn._hallInUse.put(31923, false);
		FourSepulchersSpawn._hallInUse.put(31924, false);
		if(!FourSepulchersSpawn._archonSpawned.isEmpty())
		{
			final Set<Integer> npcIdSet = FourSepulchersSpawn._archonSpawned.keySet();
			for(final int npcId : npcIdSet)
				FourSepulchersSpawn._archonSpawned.put(npcId, false);
		}
	}

	public static boolean isEntryTime()
	{
		return FourSepulchersManager._inEntryTime;
	}

	public static boolean isAttackTime()
	{
		return FourSepulchersManager._inAttackTime;
	}

	public static synchronized void tryEntry(final NpcInstance npc, final Player player)
	{
		final int npcId = npc.getNpcId();
		switch(npcId)
		{
			case 31921:
			case 31922:
			case 31923:
			case 31924:
			{
				if(FourSepulchersSpawn._hallInUse.get(npcId))
				{
					showHtmlFile(player, npcId + "-FULL.htm", npc, null);
					return;
				}
				if(!player.isInParty() || player.getParty().getMemberCount() < Config.FS_PARTY_MEM_COUNT)
				{
					showHtmlFile(player, npcId + "-SP.htm", npc, null);
					return;
				}
				if(!player.getParty().isLeader(player))
				{
					showHtmlFile(player, npcId + "-NL.htm", npc, null);
					return;
				}
				for(final Player mem : player.getParty().getPartyMembers())
				{
					final QuestState qs = mem.getQuestState(620);
					if(qs == null || !qs.isStarted() && !qs.isCompleted())
					{
						showHtmlFile(player, npcId + "-NS.htm", npc, mem);
						return;
					}
					if(mem.getInventory().getItemByItemId(7075) == null)
					{
						showHtmlFile(player, npcId + "-SE.htm", npc, mem);
						return;
					}
					if(!mem.isQuestContinuationPossible(true))
						return;
					if(mem.isDead() || !mem.isInRange(player, 700L))
						return;
				}
				if(!FourSepulchersManager._inEntryTime)
				{
					showHtmlFile(player, npcId + "-NE.htm", npc, null);
					return;
				}
				showHtmlFile(player, npcId + "-OK.htm", npc, null);
				entry(npcId, player);
				break;
			}
		}
	}

	private static void entry(final int npcId, final Player player)
	{
		final Location loc = FourSepulchersSpawn._startHallSpawns.get(npcId);
		for(final Player mem : player.getParty().getPartyMembers())
			try
			{
				mem.teleToLocation(loc.rnd(0, 80, false));
				Functions.removeItem(mem, 7075, 1L);
				if(mem.getInventory().getItemByItemId(7262) == null)
					Functions.addItem(mem, 7261, 1L);
				Functions.removeItem(mem, 7260, Integer.MAX_VALUE);
			}
			catch(Exception e)
			{
				FourSepulchersManager._log.error("Can't teleport player to sepulcher room: ", e);
			}
		FourSepulchersSpawn._hallInUse.put(npcId, true);
	}

	public static void OnDie(final Creature self, final Creature killer)
	{
		if(self != null && self.isPlayer() && self.getZ() >= -7250 && self.getZ() <= -6841 && checkIfInZone(self))
			checkAnnihilated(self.getObjectId());
	}

	public static void checkAnnihilated(final int id)
	{
		if(isPlayersAnnihilated())
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					final Player player = GameObjectsStorage.getPlayer(id);
					if(player == null)
						return;
					final Party party = player.getParty();
					if(party != null)
					{
						boolean clear = true;
						for(final Player mem : party.getPartyMembers())
							if(!mem.isDead() && checkIfInZone(mem))
							{
								clear = false;
								break;
							}
						if(clear)
							for(final Player mem : party.getPartyMembers())
								mem.teleToLocation(169589 + Rnd.get(-80, 80), -90493 + Rnd.get(-80, 80), -2914);
					}
					else
						player.teleToLocation(169589 + Rnd.get(-80, 80), -90493 + Rnd.get(-80, 80), -2914);
				}
			}, 5000L);
	}

	private static byte minuteSelect(final byte min)
	{
		switch(min % 5)
		{
			case 0:
			{
				return min;
			}
			case 1:
			{
				return (byte) (min - 1);
			}
			case 2:
			{
				return (byte) (min - 2);
			}
			case 3:
			{
				return (byte) (min + 2);
			}
			default:
			{
				return (byte) (min + 1);
			}
		}
	}

	public static void managerSay(byte min)
	{
		if(FourSepulchersManager._inAttackTime)
		{
			if(min < 5)
				return;
			min = minuteSelect(min);
			String msg = "scripts.bosses.FourSepulchersManager.managerSay1";
			if(min == 90)
				msg = "scripts.bosses.FourSepulchersManager.managerSay2";
			for(final SepulcherNpcInstance npc : FourSepulchersSpawn._managers)
			{
				if(!FourSepulchersSpawn._hallInUse.get(npc.getNpcId()))
					continue;
				npc.sayInShout(msg, String.valueOf(min));
			}
		}
		else if(FourSepulchersManager._inEntryTime)
		{
			final String msg2 = "scripts.bosses.FourSepulchersManager.managerSay3";
			final String msg3 = "scripts.bosses.FourSepulchersManager.managerSay4";
			for(final SepulcherNpcInstance npc2 : FourSepulchersSpawn._managers)
			{
				npc2.sayInShout(msg2, "");
				npc2.sayInShout(msg3, "");
			}
		}
	}

	public static FourSepulchersSpawn.GateKeeper getHallGateKeeper(final int npcId)
	{
		for(final FourSepulchersSpawn.GateKeeper gk : FourSepulchersSpawn._GateKeepers)
			if(gk.template.npcId == npcId)
				return gk;
		return null;
	}

	public static void showHtmlFile(final Player player, final String file, final NpcInstance npc, final Player member)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
		html.setFile("SepulcherNpc/" + file);
		if(member != null)
			html.replace("%member%", member.getName());
		player.sendPacket(html);
	}

	private static boolean isPlayersAnnihilated()
	{
		for(final Player pc : getPlayersInside())
			if(!pc.isDead())
				return false;
		return true;
	}

	private static List<Player> getPlayersInside()
	{
		final List<Player> result = new ArrayList<Player>();
		for(final Zone zone : FourSepulchersManager._zone)
			result.addAll(zone.getInsidePlayers());
		return result;
	}

	private static boolean checkIfInZone(final GameObject obj)
	{
		for(final Zone zone : FourSepulchersManager._zone)
			if(zone.checkIfInZone(obj))
				return true;
		return false;
	}

	public static Zone[] getZones()
	{
		return FourSepulchersManager._zone;
	}

	public static Zone getZone(final GameObject obj)
	{
		if(obj != null)
			for(final Zone zone : FourSepulchersManager._zone)
				if(zone.checkIfInZone(obj))
					return zone;
		return null;
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
		FourSepulchersManager._log = LoggerFactory.getLogger(FourSepulchersManager.class);
		FourSepulchersManager._zone = new Zone[4];
		FourSepulchersManager._inEntryTime = false;
		FourSepulchersManager._inAttackTime = false;
		FourSepulchersManager._changeCoolDownTimeTask = null;
		FourSepulchersManager._changeEntryTimeTask = null;
		FourSepulchersManager._changeWarmUpTimeTask = null;
		FourSepulchersManager._changeAttackTimeTask = null;
		FourSepulchersManager._coolDownTimeEnd = 0L;
		FourSepulchersManager._entryTimeEnd = 0L;
		FourSepulchersManager._warmUpTimeEnd = 0L;
		FourSepulchersManager._attackTimeEnd = 0L;
		FourSepulchersManager._newCycleMin = 55;
	}

	public static class ManagerSay implements Runnable
	{
		@Override
		public void run()
		{
			if(FourSepulchersManager._inAttackTime)
			{
				final Calendar tmp = Calendar.getInstance();
				tmp.setTimeInMillis(System.currentTimeMillis() - FourSepulchersManager._warmUpTimeEnd);
				if(tmp.get(12) + 5 < 50)
				{
					FourSepulchersManager.managerSay((byte) tmp.get(12));
					ThreadPoolManager.getInstance().schedule(new ManagerSay(), 300000L);
				}
				else if(tmp.get(12) + 5 >= 50)
					FourSepulchersManager.managerSay((byte) 90);
			}
			else if(FourSepulchersManager._inEntryTime)
				FourSepulchersManager.managerSay((byte) 0);
		}
	}

	public static class ChangeEntryTime implements Runnable
	{
		@Override
		public void run()
		{
			FourSepulchersManager._inEntryTime = true;
			FourSepulchersManager._inAttackTime = false;
			long interval = 0L;
			if(FourSepulchersManager._firstTimeRun)
				interval = FourSepulchersManager._entryTimeEnd - System.currentTimeMillis();
			else
				interval = 180000L;
			ThreadPoolManager.getInstance().execute(new ManagerSay());
			FourSepulchersManager._changeWarmUpTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeWarmUpTime(), interval);
			if(FourSepulchersManager._changeEntryTimeTask != null)
			{
				FourSepulchersManager._changeEntryTimeTask.cancel(false);
				FourSepulchersManager._changeEntryTimeTask = null;
			}
		}
	}

	public static class ChangeWarmUpTime implements Runnable
	{
		@Override
		public void run()
		{
			FourSepulchersManager._inEntryTime = true;
			FourSepulchersManager._inAttackTime = false;
			long interval = 0L;
			if(FourSepulchersManager._firstTimeRun)
				interval = FourSepulchersManager._warmUpTimeEnd - System.currentTimeMillis();
			else
				interval = 120000L;
			FourSepulchersManager._changeAttackTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeAttackTime(), interval);
			if(FourSepulchersManager._changeWarmUpTimeTask != null)
			{
				FourSepulchersManager._changeWarmUpTimeTask.cancel(false);
				FourSepulchersManager._changeWarmUpTimeTask = null;
			}
		}
	}

	public static class ChangeAttackTime implements Runnable
	{
		@Override
		public void run()
		{
			FourSepulchersManager._inEntryTime = false;
			FourSepulchersManager._inAttackTime = true;
			for(final FourSepulchersSpawn.GateKeeper gk : FourSepulchersSpawn._GateKeepers)
			{
				final SepulcherNpcInstance npc = new SepulcherNpcInstance(IdFactory.getInstance().getNextId(), gk.template);
				npc.spawnMe(gk);
				FourSepulchersSpawn._allMobs.add(npc);
			}
			FourSepulchersSpawn.locationShadowSpawns();
			FourSepulchersSpawn.spawnMysteriousBox(31921);
			FourSepulchersSpawn.spawnMysteriousBox(31922);
			FourSepulchersSpawn.spawnMysteriousBox(31923);
			FourSepulchersSpawn.spawnMysteriousBox(31924);
			if(!FourSepulchersManager._firstTimeRun)
				FourSepulchersManager._warmUpTimeEnd = System.currentTimeMillis();
			long interval = 0L;
			if(FourSepulchersManager._firstTimeRun)
			{
				for(double min = Calendar.getInstance().get(12); min < FourSepulchersManager._newCycleMin; ++min)
					if(min % 5.0 == 0.0)
					{
						final Calendar inter = Calendar.getInstance();
						inter.set(12, (int) min);
						ThreadPoolManager.getInstance().schedule(new ManagerSay(), inter.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
						break;
					}
			}
			else
				ThreadPoolManager.getInstance().schedule(new ManagerSay(), 302000L);
			if(FourSepulchersManager._firstTimeRun)
				interval = FourSepulchersManager._attackTimeEnd - System.currentTimeMillis();
			else
				interval = 3000000L;
			FourSepulchersManager._changeCoolDownTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeCoolDownTime(), interval);
			if(FourSepulchersManager._changeAttackTimeTask != null)
			{
				FourSepulchersManager._changeAttackTimeTask.cancel(false);
				FourSepulchersManager._changeAttackTimeTask = null;
			}
		}
	}

	public static class ChangeCoolDownTime implements Runnable
	{
		@Override
		public void run()
		{
			FourSepulchersManager._inEntryTime = false;
			FourSepulchersManager._inAttackTime = false;
			cleanUp();
			final Calendar time = Calendar.getInstance();
			if(Calendar.getInstance().get(12) > FourSepulchersManager._newCycleMin && !FourSepulchersManager._firstTimeRun)
				time.set(10, Calendar.getInstance().get(10) + 1);
			time.set(12, FourSepulchersManager._newCycleMin);
			if(FourSepulchersManager._firstTimeRun)
				FourSepulchersManager._firstTimeRun = false;
			final long interval = time.getTimeInMillis() - System.currentTimeMillis();
			FourSepulchersManager._changeEntryTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeEntryTime(), interval);
			if(FourSepulchersManager._changeCoolDownTimeTask != null)
			{
				FourSepulchersManager._changeCoolDownTimeTask.cancel(false);
				FourSepulchersManager._changeCoolDownTimeTask = null;
			}
		}
	}
}
