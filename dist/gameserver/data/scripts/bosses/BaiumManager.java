package bosses;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.BossInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.Earthquake;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.PlaySound;
import l2s.gameserver.network.l2.s2c.SocialAction;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.EffectType;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.Log;

public class BaiumManager extends Functions implements ScriptFile
{
	private static Logger _log;
	public static List<NpcInstance> _angels;
	private static List<Spawn> _angelSpawns;
	private static ScheduledFuture<?> _callAngelTask;
	private static ScheduledFuture<?> _cubeSpawnTask;
	private static ScheduledFuture<?> _intervalEndTask;
	private static ScheduledFuture<?> _killPcTask;
	private static ScheduledFuture<?> _mobiliseTask;
	private static ScheduledFuture<?> _moveAtRandomTask;
	private static ScheduledFuture<?> _onAnnihilatedTask;
	private static ScheduledFuture<?> _sleepCheckTask;
	private static ScheduledFuture<?> _socialTask;
	private static ScheduledFuture<?> _socialTask2;
	private static ScheduledFuture<?> _activityTimeEndTask;
	private static long _lastAttackTime;
	private static List<NpcInstance> _monsters;
	private static Map<Integer, Spawn> _monsterSpawn;
	private static EpicBossState _state;
	private static Spawn _statueSpawn;
	private static NpcInstance _teleportCube;
	private static Spawn _teleportCubeSpawn;
	private static Zone _zone;
	private static BossInstance BaiumBoss;
	private static NpcInstance BaiumNpc;
	private static final Location[] ANGEL_LOCATION;
	private static final int ARCHANGEL = 29021;
	private static final int BAIUM_ID = 29020;
	private static final int BAIUM_NPC = 29025;
	private static final Location CUBE_LOCATION;
	private static final Location STATUE_LOCATION;
	private static boolean Dying;
	private static final int TELEPORT_CUBE = 31759;

	private static void banishForeigners()
	{
		for(final Player player : getPlayersInside())
			player.teleToClosestTown();
	}

	private static synchronized void checkAnnihilated()
	{
		if(BaiumManager._onAnnihilatedTask == null && isPlayersAnnihilated())
			BaiumManager._onAnnihilatedTask = ThreadPoolManager.getInstance().schedule(new onAnnihilated(), 5000L);
	}

	private static void deleteArchangels()
	{
		for(final NpcInstance angel : BaiumManager._angels)
			if(angel != null && angel.getSpawn() != null)
			{
				angel.getSpawn().stopRespawn();
				angel.deleteMe();
			}
		BaiumManager._angels.clear();
	}

	private static List<Player> getPlayersInside()
	{
		return getZone().getInsidePlayersIncludeZ();
	}

	public static Zone getZone()
	{
		return BaiumManager._zone;
	}

	public static void init()
	{
		BaiumManager._state = new EpicBossState(29020);
		BaiumManager._zone = ZoneManager.getInstance().getZoneById(Zone.ZoneType.epic, 702001, false);
		try
		{
			(BaiumManager._statueSpawn = new Spawn(NpcTable.getTemplate(29025))).setAmount(1);
			BaiumManager._statueSpawn.setLoc(BaiumManager.STATUE_LOCATION);
			BaiumManager._statueSpawn.stopRespawn();
			final Spawn tempSpawn = new Spawn(NpcTable.getTemplate(29020));
			tempSpawn.setAmount(1);
			BaiumManager._monsterSpawn.put(29020, tempSpawn);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			final NpcTemplate angel = NpcTable.getTemplate(29021);
			BaiumManager._angelSpawns.clear();
			final List<Integer> random = new ArrayList<Integer>();
			for(int i = 0; i < 5; ++i)
			{
				int r;
				for(r = -1; r == -1 || random.contains(r); r = Rnd.get(10))
				{}
				random.add(r);
			}
			for(final int j : random)
			{
				final Spawn spawnDat = new Spawn(angel);
				spawnDat.setAmount(1);
				spawnDat.setLoc(BaiumManager.ANGEL_LOCATION[j]);
				spawnDat.setRespawnDelay(300000);
				spawnDat.setLocation(0);
				BaiumManager._angelSpawns.add(spawnDat);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		BaiumManager._log.info("BaiumManager: State of Baium is " + BaiumManager._state.getState() + ".");
		if(BaiumManager._state.getAliveId() > 0)
		{
			final Spawn baiumSpawn = BaiumManager._monsterSpawn.get(29020);
			baiumSpawn.setLoc(BaiumManager._state.getLoc());
			(BaiumManager.BaiumBoss = (BossInstance) baiumSpawn.doSpawn(true)).setCurrentHp(BaiumManager._state.getCurrentHp(), true);
			BaiumManager.BaiumBoss.setCurrentMp(BaiumManager._state.getCurrentMp());
			BaiumManager._monsters.add(BaiumManager.BaiumBoss);
			setLastAttackTime();
			BaiumManager._callAngelTask = ThreadPoolManager.getInstance().schedule(new CallArchAngel(), 1000L);
			if(BaiumManager._sleepCheckTask != null)
				BaiumManager._sleepCheckTask.cancel(false);
			BaiumManager._sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 600000L);
			BaiumManager._activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), Config.BAIUM_ACTIVITY_TIME);
		}
		else if(BaiumManager._state.getState().equals(EpicBossState.State.NOTSPAWN))
			BaiumManager.BaiumNpc = BaiumManager._statueSpawn.doSpawn(true);
		else if(BaiumManager._state.getState().equals(EpicBossState.State.ALIVE))
		{
			BaiumManager._state.setState(EpicBossState.State.NOTSPAWN);
			BaiumManager._state.update();
			BaiumManager.BaiumNpc = BaiumManager._statueSpawn.doSpawn(true);
		}
		else if(BaiumManager._state.getState().equals(EpicBossState.State.INTERVAL) || BaiumManager._state.getState().equals(EpicBossState.State.DEAD))
			setIntervalEndTask();
		final Date dt = new Date(BaiumManager._state.getRespawnDate());
		BaiumManager._log.info("Loaded Boss: Baium. Next spawn date: " + dt);
	}

	private static boolean isPlayersAnnihilated()
	{
		for(final Player pc : getPlayersInside())
			if(!pc.isDead())
				return false;
		return true;
	}

	public static void onBaiumDie(final Creature self)
	{
		if(BaiumManager.Dying)
			return;
		BaiumManager.Dying = true;
		self.broadcastPacket(new L2GameServerPacket[] { new PlaySound(1, "BS02_D", 1, 0, self.getLoc()) });
		BaiumManager._state.setRespawnDate(Config.FWB_FIXINTERVALOFBAIUM, Config.FWB_RANDOMINTERVALOFBAIUM, Config.BAIUM_FIXRESP);
		BaiumManager._state.setState(EpicBossState.State.INTERVAL);
		BaiumManager._state.update();
		if(BaiumManager._intervalEndTask != null)
			BaiumManager._intervalEndTask.cancel(false);
		BaiumManager._intervalEndTask = ThreadPoolManager.getInstance().schedule(new IntervalEnd(), BaiumManager._state.getInterval());
		Log.addLog("Baium died", "bosses");
		deleteArchangels();
		if(BaiumManager._sleepCheckTask != null)
		{
			BaiumManager._sleepCheckTask.cancel(false);
			BaiumManager._sleepCheckTask = null;
		}
		if(BaiumManager._activityTimeEndTask != null)
		{
			BaiumManager._activityTimeEndTask.cancel(true);
			BaiumManager._activityTimeEndTask = null;
		}
		BaiumManager._cubeSpawnTask = ThreadPoolManager.getInstance().schedule(new CubeSpawn(), 20000L);
		BaiumManager.BaiumBoss = null;
	}

	public static void OnDie(final Creature self, final Creature killer)
	{
		if(self == null)
			return;
		if(Config.BAIUM_CHECK_ANNIHILATED && self.isPlayer() && BaiumManager._state != null && BaiumManager._state.getState() == EpicBossState.State.ALIVE && BaiumManager._zone != null && BaiumManager._zone.checkIfInZone(self))
			checkAnnihilated();
		else if(self.isNpc() && self.getNpcId() == 29020)
			onBaiumDie(self);
	}

	private static void setIntervalEndTask()
	{
		setUnspawn();
		if(!BaiumManager._state.getState().equals(EpicBossState.State.INTERVAL))
		{
			BaiumManager._state.setRespawnDate(Config.FWB_FIXINTERVALOFBAIUM, Config.FWB_RANDOMINTERVALOFBAIUM, Config.BAIUM_FIXRESP);
			BaiumManager._state.setState(EpicBossState.State.INTERVAL);
			BaiumManager._state.update();
		}
		BaiumManager._intervalEndTask = ThreadPoolManager.getInstance().schedule(new IntervalEnd(), BaiumManager._state.getInterval());
	}

	public static void setLastAttackTime()
	{
		BaiumManager._lastAttackTime = System.currentTimeMillis();
	}

	private static void setUnspawn()
	{
		banishForeigners();
		deleteArchangels();
		for(final NpcInstance mob : BaiumManager._monsters)
		{
			mob.getSpawn().stopRespawn();
			mob.deleteMe();
		}
		BaiumManager._monsters.clear();
		BaiumManager.BaiumBoss = null;
		if(BaiumManager._teleportCube != null)
		{
			BaiumManager._teleportCube.getSpawn().stopRespawn();
			BaiumManager._teleportCube.deleteMe();
			BaiumManager._teleportCube = null;
		}
		if(BaiumManager._cubeSpawnTask != null)
		{
			BaiumManager._cubeSpawnTask.cancel(false);
			BaiumManager._cubeSpawnTask = null;
		}
		if(BaiumManager._intervalEndTask != null)
		{
			BaiumManager._intervalEndTask.cancel(false);
			BaiumManager._intervalEndTask = null;
		}
		if(BaiumManager._socialTask != null)
		{
			BaiumManager._socialTask.cancel(false);
			BaiumManager._socialTask = null;
		}
		if(BaiumManager._mobiliseTask != null)
		{
			BaiumManager._mobiliseTask.cancel(false);
			BaiumManager._mobiliseTask = null;
		}
		if(BaiumManager._moveAtRandomTask != null)
		{
			BaiumManager._moveAtRandomTask.cancel(false);
			BaiumManager._moveAtRandomTask = null;
		}
		if(BaiumManager._socialTask2 != null)
		{
			BaiumManager._socialTask2.cancel(false);
			BaiumManager._socialTask2 = null;
		}
		if(BaiumManager._killPcTask != null)
		{
			BaiumManager._killPcTask.cancel(false);
			BaiumManager._killPcTask = null;
		}
		if(BaiumManager._callAngelTask != null)
		{
			BaiumManager._callAngelTask.cancel(false);
			BaiumManager._callAngelTask = null;
		}
		if(BaiumManager._sleepCheckTask != null)
		{
			BaiumManager._sleepCheckTask.cancel(false);
			BaiumManager._sleepCheckTask = null;
		}
		if(BaiumManager._onAnnihilatedTask != null)
		{
			BaiumManager._onAnnihilatedTask.cancel(false);
			BaiumManager._onAnnihilatedTask = null;
		}
		if(BaiumManager._activityTimeEndTask != null)
		{
			BaiumManager._activityTimeEndTask.cancel(true);
			BaiumManager._activityTimeEndTask = null;
		}
	}

	private static void sleepBaium()
	{
		setUnspawn();
		if(BaiumManager._state.getState().equals(EpicBossState.State.ALIVE))
		{
			Log.addLog("Baium going to sleep, spawning statue", "bosses");
			BaiumManager._state.setState(EpicBossState.State.NOTSPAWN);
			BaiumManager._state.update();
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					BaiumManager.BaiumNpc = BaiumManager._statueSpawn.doSpawn(true);
				}
			}, Config.BAIUM_STATUE_SPAWN_TIME);
		}
	}

	public static void spawnBaium(final String awake_by, final int oid)
	{
		if(BaiumManager.BaiumBoss != null)
			return;
		BaiumManager.Dying = false;
		final Spawn baiumSpawn = BaiumManager._monsterSpawn.get(29020);
		baiumSpawn.setLoc(BaiumManager.BaiumNpc.getLoc());
		BaiumManager.BaiumNpc.getSpawn().stopRespawn();
		BaiumManager.BaiumNpc.deleteMe();
		BaiumManager.BaiumNpc = null;
		BaiumManager.BaiumBoss = (BossInstance) baiumSpawn.doSpawn(true);
		BaiumManager._monsters.add(BaiumManager.BaiumBoss);
		BaiumManager._state.setRespawnDate(Config.FWB_FIXINTERVALOFBAIUM, Config.FWB_RANDOMINTERVALOFBAIUM, Config.BAIUM_FIXRESP);
		BaiumManager._state.setState(EpicBossState.State.ALIVE);
		BaiumManager._state.update();
		Log.addLog("Spawned Baium, awake by: " + awake_by, "bosses");
		setLastAttackTime();
		BaiumManager.BaiumBoss.setImmobilized(true);
		BaiumManager.BaiumBoss.broadcastPacket(new L2GameServerPacket[] { new PlaySound(1, "BS02_A", 1, 0, BaiumManager.BaiumBoss.getLoc()) });
		BaiumManager.BaiumBoss.broadcastPacket(new L2GameServerPacket[] { new SocialAction(BaiumManager.BaiumBoss.getObjectId(), 2) });
		BaiumManager._socialTask = ThreadPoolManager.getInstance().schedule(new Social(BaiumManager.BaiumBoss, 3), 15000L);
		ThreadPoolManager.getInstance().schedule(new EarthquakeTask(BaiumManager.BaiumBoss), 25000L);
		BaiumManager._socialTask2 = ThreadPoolManager.getInstance().schedule(new Social(BaiumManager.BaiumBoss, 1), 25000L);
		BaiumManager._killPcTask = ThreadPoolManager.getInstance().schedule(new KillPc(oid, BaiumManager.BaiumBoss), 26000L);
		BaiumManager._callAngelTask = ThreadPoolManager.getInstance().schedule(new CallArchAngel(), 35000L);
		BaiumManager._mobiliseTask = ThreadPoolManager.getInstance().schedule(new SetMobilised(BaiumManager.BaiumBoss), 35500L);
		final Location pos = new Location(Rnd.get(112826, 116241), Rnd.get(15575, 16375), 10078, 0);
		BaiumManager._moveAtRandomTask = ThreadPoolManager.getInstance().schedule(new MoveAtRandom(BaiumManager.BaiumBoss, pos), 36000L);
		if(BaiumManager._sleepCheckTask != null)
			BaiumManager._sleepCheckTask.cancel(false);
		BaiumManager._sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 600000L);
		BaiumManager._activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), Config.BAIUM_ACTIVITY_TIME);
	}

	public static int isEnableEnterToLair()
	{
		return BaiumManager.BaiumBoss != null ? 2 : BaiumManager.BaiumNpc != null ? 1 : (int) (BaiumManager._state.getRespawnDate() / 1000L);
	}

	@Override
	public void onLoad()
	{
		init();
	}

	@Override
	public void onReload()
	{
		sleepBaium();
	}

	@Override
	public void onShutdown()
	{
		if(BaiumManager._state.getState() == EpicBossState.State.ALIVE && BaiumManager.BaiumBoss != null && !BaiumManager.BaiumBoss.isDead())
			BaiumManager._state.aliveSave(29020, BaiumManager.BaiumBoss.getCurrentHp(), BaiumManager.BaiumBoss.getCurrentMp(), BaiumManager.BaiumBoss.getLoc());
	}

	static
	{
		BaiumManager._log = LoggerFactory.getLogger(BaiumManager.class);
		BaiumManager._angels = new ArrayList<NpcInstance>();
		BaiumManager._angelSpawns = new ArrayList<Spawn>();
		BaiumManager._callAngelTask = null;
		BaiumManager._cubeSpawnTask = null;
		BaiumManager._intervalEndTask = null;
		BaiumManager._killPcTask = null;
		BaiumManager._mobiliseTask = null;
		BaiumManager._moveAtRandomTask = null;
		BaiumManager._onAnnihilatedTask = null;
		BaiumManager._sleepCheckTask = null;
		BaiumManager._socialTask = null;
		BaiumManager._socialTask2 = null;
		BaiumManager._activityTimeEndTask = null;
		BaiumManager._lastAttackTime = 0L;
		BaiumManager._monsters = new ArrayList<NpcInstance>();
		BaiumManager._monsterSpawn = new HashMap<Integer, Spawn>();
		BaiumManager._statueSpawn = null;
		BaiumManager._teleportCube = null;
		BaiumManager._teleportCubeSpawn = null;
		BaiumManager.BaiumBoss = null;
		BaiumManager.BaiumNpc = null;
		ANGEL_LOCATION = new Location[] {
				new Location(113004, 16209, 10076, 60242),
				new Location(114053, 16642, 10076, 4411),
				new Location(114563, 17184, 10076, 49241),
				new Location(116356, 16402, 10076, 31109),
				new Location(115015, 16393, 10076, 32760),
				new Location(115481, 15335, 10076, 16241),
				new Location(114680, 15407, 10051, 32485),
				new Location(114886, 14437, 10076, 16868),
				new Location(115391, 17593, 10076, 55346),
				new Location(115245, 17558, 10076, 35536) };
		CUBE_LOCATION = new Location(115203, 16620, 10078, 0);
		STATUE_LOCATION = new Location(115996, 17417, 10106, 41740);
		BaiumManager.Dying = false;
	}

	public static class CallArchAngel implements Runnable
	{
		@Override
		public void run()
		{
			for(final Spawn spawn : BaiumManager._angelSpawns)
				BaiumManager._angels.add(spawn.doSpawn(true));
		}
	}

	public static class CheckLastAttack implements Runnable
	{
		@Override
		public void run()
		{
			if(BaiumManager._state.getState().equals(EpicBossState.State.ALIVE))
				if(BaiumManager._lastAttackTime + Config.BAIUM_LIMITUNTILSLEEP < System.currentTimeMillis())
					sleepBaium();
				else
					BaiumManager._sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 60000L);
		}
	}

	public static class CubeSpawn implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				final NpcTemplate Cube = NpcTable.getTemplate(31759);
				final Spawn _teleportCubeSpawn = new Spawn(Cube);
				_teleportCubeSpawn.setAmount(1);
				_teleportCubeSpawn.setLoc(BaiumManager.CUBE_LOCATION);
				_teleportCubeSpawn.setRespawnDelay(60);
				_teleportCubeSpawn.setLocation(0);
				BaiumManager._teleportCube = _teleportCubeSpawn.doSpawn(true);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static class EarthquakeTask implements Runnable
	{
		private final BossInstance baium;

		public EarthquakeTask(final BossInstance _baium)
		{
			baium = _baium;
		}

		@Override
		public void run()
		{
			final Earthquake eq = new Earthquake(baium.getLoc(), 40, 5);
			baium.broadcastPacket(new L2GameServerPacket[] { eq });
		}
	}

	public static class IntervalEnd implements Runnable
	{
		@Override
		public void run()
		{
			BaiumManager._state.setState(EpicBossState.State.NOTSPAWN);
			BaiumManager._state.update();
			BaiumManager.BaiumNpc = BaiumManager._statueSpawn.doSpawn(true);
		}
	}

	public static class KillPc implements Runnable
	{
		private BossInstance _boss;
		private final int _target;

		public KillPc(final int target, final BossInstance boss)
		{
			_target = target;
			_boss = boss;
		}

		@Override
		public void run()
		{
			if(_boss == null || _boss.isDead())
				return;
			final Skill skill = SkillTable.getInstance().getInfo(4136, 1);
			if(skill == null)
				return;
			final Player player = GameObjectsStorage.getPlayer(_target);
			if(player != null && !player.isDead())
			{
				if(!player.isInRange(_boss, 300L))
					player.teleToLocation(115929, 17349, 10077);
				player.getAbnormalList().stop(EffectType.Petrification);
				player.getAbnormalList().stop(EffectType.Invulnerable);
				_boss.setTarget(player);
				_boss.doCast(skill, player, false);
			}
		}
	}

	public static class MoveAtRandom implements Runnable
	{
		private NpcInstance _npc;
		private Location _pos;

		public MoveAtRandom(final NpcInstance npc, final Location pos)
		{
			_npc = npc;
			_pos = pos;
		}

		@Override
		public void run()
		{
			if(_npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
				_npc.moveToLocation(_pos, 0, false);
		}
	}

	public static class onAnnihilated implements Runnable
	{
		@Override
		public void run()
		{
			sleepBaium();
		}
	}

	public static class SetMobilised implements Runnable
	{
		private BossInstance _boss;

		public SetMobilised(final BossInstance boss)
		{
			_boss = boss;
		}

		@Override
		public void run()
		{
			_boss.setImmobilized(false);
		}
	}

	public static class Social implements Runnable
	{
		private int _action;
		private NpcInstance _npc;

		public Social(final NpcInstance npc, final int actionId)
		{
			_npc = npc;
			_action = actionId;
		}

		@Override
		public void run()
		{
			final SocialAction sa = new SocialAction(_npc.getObjectId(), _action);
			_npc.broadcastPacket(new L2GameServerPacket[] { sa });
		}
	}

	public static class ActivityTimeEnd implements Runnable
	{
		@Override
		public void run()
		{
			sleepBaium();
		}
	}
}
