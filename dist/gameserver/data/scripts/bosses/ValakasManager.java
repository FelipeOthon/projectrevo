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
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.BossInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.SocialAction;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.Log;

public class ValakasManager extends Functions implements ScriptFile
{
	private static final Logger _log;
	private static final int[][] _teleportCubeLocation;
	private static List<Spawn> _teleportCubeSpawn;
	private static List<NpcInstance> _teleportCube;
	private static Map<Integer, Spawn> _monsterSpawn;
	private static List<NpcInstance> _monsters;
	private static ScheduledFuture<?> _cubeSpawnTask;
	private static ScheduledFuture<?> _monsterSpawnTask;
	private static ScheduledFuture<?> _intervalEndTask;
	private static ScheduledFuture<?> _socialTask;
	private static ScheduledFuture<?> _mobiliseTask;
	private static ScheduledFuture<?> _moveAtRandomTask;
	private static ScheduledFuture<?> _respawnValakasTask;
	private static ScheduledFuture<?> _sleepCheckTask;
	private static ScheduledFuture<?> _onAnnihilatedTask;
	private static ScheduledFuture<?> _activityTimeEndTask;
	private static final int Valakas = 29028;
	private static final int ValakasDummy = 32123;
	private static final int _teleportCubeId = 31759;
	private static final int VALAKAS_CIRCLET = 8567;
	private static EpicBossState _state;
	private static Zone _zone;
	private static long _lastAttackTime;
	private static final boolean FWV_MOVEATRANDOM = true;
	private static boolean Dying;

	private static void banishForeigners()
	{
		for(final Player player : getPlayersInside())
			player.teleToClosestTown();
	}

	private static synchronized void checkAnnihilated()
	{
		if(_onAnnihilatedTask == null && isPlayersAnnihilated())
			_onAnnihilatedTask = ThreadPoolManager.getInstance().schedule(new onAnnihilated(), 5000L);
	}

	private static List<Player> getPlayersInside()
	{
		return getZone().getInsidePlayers();
	}

	public static Zone getZone()
	{
		return _zone;
	}

	private static void init()
	{
		_state = new EpicBossState(29028);
		_zone = ZoneManager.getInstance().getZoneById(Zone.ZoneType.epic, 702003, false);
		try
		{
			NpcTemplate template1 = NpcTable.getTemplate(29028);
			Spawn tempSpawn = new Spawn(template1);
			tempSpawn.setLocx(212852);
			tempSpawn.setLocy(-114842);
			tempSpawn.setLocz(-1632);
			tempSpawn.setHeading(833);
			tempSpawn.setAmount(1);
			tempSpawn.stopRespawn();
			_monsterSpawn.put(29028, tempSpawn);
			template1 = NpcTable.getTemplate(32123);
			tempSpawn = new Spawn(template1);
			tempSpawn.setLocx(212852);
			tempSpawn.setLocy(-114842);
			tempSpawn.setLocz(-1632);
			tempSpawn.setHeading(833);
			tempSpawn.setAmount(1);
			tempSpawn.stopRespawn();
			_monsterSpawn.put(32123, tempSpawn);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			final NpcTemplate Cube = NpcTable.getTemplate(31759);
			for(final int[] element : _teleportCubeLocation)
			{
				final Spawn spawnDat = new Spawn(Cube);
				spawnDat.setAmount(1);
				spawnDat.setLocx(element[0]);
				spawnDat.setLocy(element[1]);
				spawnDat.setLocz(element[2]);
				spawnDat.setHeading(element[3]);
				spawnDat.setRespawnDelay(60);
				spawnDat.setLocation(0);
				_teleportCubeSpawn.add(spawnDat);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		_log.info("ValakasManager: State of Valakas is " + _state.getState() + ".");
		if(_state.getAliveId() > 0)
		{
			final Spawn valakasSpawn = _monsterSpawn.get(29028);
			valakasSpawn.setLoc(_state.getLoc());
			final BossInstance _valakas = (BossInstance) valakasSpawn.doSpawn(true);
			_valakas.setCurrentHp(_state.getCurrentHp(), true);
			_valakas.setCurrentMp(_state.getCurrentMp());
			_monsters.add(_valakas);
			setLastAttackTime();
			_sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 600000L);
			_activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), Config.VALAKAS_ACTIVITY_TIME);
		}
		else if(!_state.getState().equals(EpicBossState.State.NOTSPAWN))
			setIntervalEndTask();
		final Date dt = new Date(_state.getRespawnDate());
		_log.info("ValakasManager: Next spawn date of Valakas is " + dt + ".");
	}

	private static boolean isPlayersAnnihilated()
	{
		for(final Player pc : getPlayersInside())
			if(!pc.isDead())
				return false;
		return true;
	}

	public static void OnDie(final Creature self, final Creature killer)
	{
		if(self == null)
			return;
		if(Config.VALAKAS_CHECK_ANNIHILATED && self.isPlayer() && _state != null && _state.getState() == EpicBossState.State.ALIVE && _zone != null && _zone.checkIfInZone(self.getX(), self.getY()))
			checkAnnihilated();
		else if(self.isNpc() && self.getNpcId() == 29028)
			onValakasDie(killer);
	}

	private static void onValakasDie(final Creature killer)
	{
		if(Dying)
			return;
		Dying = true;
		_state.setRespawnDate(Config.FWV_FIXINTERVALOFVALAKAS, Config.FWV_RANDOMINTERVALOFVALAKAS, Config.VALAKAS_FIXRESP);
		_state.setState(EpicBossState.State.INTERVAL);
		_state.update();
		Log.addLog("Valakas died", "bosses");
		_cubeSpawnTask = ThreadPoolManager.getInstance().schedule(new CubeSpawn(), 20000L);
		if(killer != null && killer.isPlayable())
		{
			final Player pc = killer.getPlayer();
			if(pc == null)
				return;
			final Party party = pc.getParty();
			if(party != null)
			{
				for(final Player partyMember : party.getPartyMembers())
					if(partyMember != null && pc.isInRange(partyMember, 5000L) && partyMember.getInventory().getItemByItemId(8567) == null)
						partyMember.getInventory().addItem(8567, 1L);
			}
			else if(pc.getInventory().getItemByItemId(8567) == null)
				pc.getInventory().addItem(8567, 1L);
		}
	}

	private static void setIntervalEndTask()
	{
		setUnspawn();
		if(_state.getState().equals(EpicBossState.State.ALIVE))
		{
			_state.setState(EpicBossState.State.NOTSPAWN);
			_state.update();
			return;
		}
		if(!_state.getState().equals(EpicBossState.State.INTERVAL))
		{
			_state.setRespawnDate(Config.FWV_FIXINTERVALOFVALAKAS, Config.FWV_RANDOMINTERVALOFVALAKAS, Config.VALAKAS_FIXRESP);
			_state.setState(EpicBossState.State.INTERVAL);
			_state.update();
		}
		_intervalEndTask = ThreadPoolManager.getInstance().schedule(new IntervalEnd(), _state.getInterval());
	}

	private static void setUnspawn()
	{
		banishForeigners();
		for(final NpcInstance mob : _monsters)
		{
			mob.getSpawn().stopRespawn();
			mob.deleteMe();
		}
		_monsters.clear();
		for(final NpcInstance cube : _teleportCube)
		{
			cube.getSpawn().stopRespawn();
			cube.deleteMe();
		}
		_teleportCube.clear();
		if(_cubeSpawnTask != null)
		{
			_cubeSpawnTask.cancel(false);
			_cubeSpawnTask = null;
		}
		if(_monsterSpawnTask != null)
		{
			_monsterSpawnTask.cancel(false);
			_monsterSpawnTask = null;
		}
		if(_intervalEndTask != null)
		{
			_intervalEndTask.cancel(false);
			_intervalEndTask = null;
		}
		if(_socialTask != null)
		{
			_socialTask.cancel(false);
			_socialTask = null;
		}
		if(_mobiliseTask != null)
		{
			_mobiliseTask.cancel(false);
			_mobiliseTask = null;
		}
		if(_moveAtRandomTask != null)
		{
			_moveAtRandomTask.cancel(false);
			_moveAtRandomTask = null;
		}
		if(_sleepCheckTask != null)
		{
			_sleepCheckTask.cancel(false);
			_sleepCheckTask = null;
		}
		if(_respawnValakasTask != null)
		{
			_respawnValakasTask.cancel(false);
			_respawnValakasTask = null;
		}
		if(_onAnnihilatedTask != null)
		{
			_onAnnihilatedTask.cancel(false);
			_onAnnihilatedTask = null;
		}
		if(_activityTimeEndTask != null)
		{
			_activityTimeEndTask.cancel(true);
			_activityTimeEndTask = null;
		}
	}

	private static void sleep()
	{
		setUnspawn();
		if(_state.getState().equals(EpicBossState.State.ALIVE))
		{
			_state.setState(EpicBossState.State.NOTSPAWN);
			_state.update();
		}
	}

	public static void setLastAttackTime()
	{
		_lastAttackTime = System.currentTimeMillis();
	}

	public static synchronized void setValakasSpawnTask()
	{
		if(_monsterSpawnTask == null)
			_monsterSpawnTask = ThreadPoolManager.getInstance().schedule(new ValakasSpawn(1, null), Config.FWV_APPTIMEOFVALAKAS);
	}

	public static int isEnableEnterToLair()
	{
		return _state.getState() == EpicBossState.State.NOTSPAWN ? 1 : _state.getState() == EpicBossState.State.ALIVE ? 2 : (int) (_state.getRespawnDate() / 1000L);
	}

	@Override
	public void onLoad()
	{
		init();
	}

	@Override
	public void onReload()
	{
		sleep();
	}

	@Override
	public void onShutdown()
	{
		List<NpcInstance> npcs = GameObjectsStorage.getNpcs(true, 29028);
		if(!npcs.isEmpty())
		{
			if(_state.getState() == EpicBossState.State.ALIVE)
				_state.aliveSave(29028, npcs.get(0).getCurrentHp(), npcs.get(0).getCurrentMp(), npcs.get(0).getLoc());
		}
	}

	static
	{
		_log = LoggerFactory.getLogger(ValakasManager.class);
		_teleportCubeLocation = new int[][] {
				{ 214880, -116144, -1644, 0 },
				{ 213696, -116592, -1644, 0 },
				{ 212112, -116688, -1644, 0 },
				{ 211184, -115472, -1664, 0 },
				{ 210336, -114592, -1644, 0 },
				{ 211360, -113904, -1644, 0 },
				{ 213152, -112352, -1644, 0 },
				{ 214032, -113232, -1644, 0 },
				{ 214752, -114592, -1644, 0 },
				{ 209824, -115568, -1421, 0 },
				{ 210528, -112192, -1403, 0 },
				{ 213120, -111136, -1408, 0 },
				{ 215184, -111504, -1392, 0 },
				{ 215456, -117328, -1392, 0 },
				{ 213200, -118160, -1424, 0 } };
		_teleportCubeSpawn = new ArrayList<Spawn>();
		_teleportCube = new ArrayList<NpcInstance>();
		_monsterSpawn = new HashMap<Integer, Spawn>();
		_monsters = new ArrayList<NpcInstance>();
		_cubeSpawnTask = null;
		_monsterSpawnTask = null;
		_intervalEndTask = null;
		_socialTask = null;
		_mobiliseTask = null;
		_moveAtRandomTask = null;
		_respawnValakasTask = null;
		_sleepCheckTask = null;
		_onAnnihilatedTask = null;
		_activityTimeEndTask = null;
		_lastAttackTime = 0L;
		Dying = false;
	}

	public static class CheckLastAttack implements Runnable
	{
		@Override
		public void run()
		{
			if(_state.getState().equals(EpicBossState.State.ALIVE))
				if(_lastAttackTime + Config.VALAKAS_LIMITUNTILSLEEP < System.currentTimeMillis())
					sleep();
				else
					_sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 60000L);
		}
	}

	public static class CubeSpawn implements Runnable
	{
		@Override
		public void run()
		{
			for(final Spawn spawnDat : _teleportCubeSpawn)
				_teleportCube.add(spawnDat.doSpawn(true));
		}
	}

	public static class IntervalEnd implements Runnable
	{
		@Override
		public void run()
		{
			_state.setState(EpicBossState.State.NOTSPAWN);
			_state.update();
		}
	}

	private static class MoveAtRandom implements Runnable
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
			sleep();
		}
	}

	private static class SetMobilised implements Runnable
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

	private static class ValakasSpawn implements Runnable
	{
		private int _distance;
		private int _taskId;
		private BossInstance _valakas;
		private List<Player> _players;

		ValakasSpawn(final int taskId, final BossInstance valakas)
		{
			_distance = 2550;
			_valakas = null;
			_players = getPlayersInside();
			_taskId = taskId;
			_valakas = valakas;
		}

		@Override
		public void run()
		{
			SocialAction sa = null;
			if(_socialTask != null)
			{
				_socialTask.cancel(false);
				_socialTask = null;
			}
			switch(_taskId)
			{
				case 1:
				{
					Dying = false;
					final Spawn valakasSpawn = _monsterSpawn.get(29028);
					_valakas = (BossInstance) valakasSpawn.doSpawn(true);
					_monsters.add(_valakas);
					_valakas.setImmobilized(true);
					_state.setRespawnDate(Config.FWV_FIXINTERVALOFVALAKAS, Config.FWV_RANDOMINTERVALOFVALAKAS, Config.VALAKAS_FIXRESP);
					_state.setState(EpicBossState.State.ALIVE);
					_state.update();
					_socialTask = ThreadPoolManager.getInstance().schedule(new ValakasSpawn(2, _valakas), 16L);
					break;
				}
				case 2:
				{
					sa = new SocialAction(_valakas.getObjectId(), 1);
					_valakas.broadcastPacket(new L2GameServerPacket[] { sa });
					for(final Player pc : _players)
						if(pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1800, 180, -1, 1500, 15000);
						}
						else
							pc.leaveMovieMode();
					_socialTask = ThreadPoolManager.getInstance().schedule(new ValakasSpawn(3, _valakas), 1500L);
					break;
				}
				case 3:
				{
					for(final Player pc : _players)
						if(pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1300, 180, -5, 3000, 15000);
						}
						else
							pc.leaveMovieMode();
					_socialTask = ThreadPoolManager.getInstance().schedule(new ValakasSpawn(4, _valakas), 3300L);
					break;
				}
				case 4:
				{
					for(final Player pc : _players)
						if(pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 500, 180, -8, 600, 15000);
						}
						else
							pc.leaveMovieMode();
					_socialTask = ThreadPoolManager.getInstance().schedule(new ValakasSpawn(5, _valakas), 1300L);
					break;
				}
				case 5:
				{
					for(final Player pc : _players)
						if(pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1200, 180, -5, 300, 15000);
						}
						else
							pc.leaveMovieMode();
					_socialTask = ThreadPoolManager.getInstance().schedule(new ValakasSpawn(6, _valakas), 1600L);
					break;
				}
				case 6:
				{
					for(final Player pc : _players)
						if(pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 2800, 250, 70, 0, 15000);
						}
						else
							pc.leaveMovieMode();
					_socialTask = ThreadPoolManager.getInstance().schedule(new ValakasSpawn(7, _valakas), 200L);
					break;
				}
				case 7:
				{
					for(final Player pc : _players)
						if(pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 2600, 30, 60, 3400, 15000);
						}
						else
							pc.leaveMovieMode();
					_socialTask = ThreadPoolManager.getInstance().schedule(new ValakasSpawn(8, _valakas), 5700L);
					break;
				}
				case 8:
				{
					for(final Player pc : _players)
						if(pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 700, 150, -65, 0, 15000);
						}
						else
							pc.leaveMovieMode();
					_socialTask = ThreadPoolManager.getInstance().schedule(new ValakasSpawn(9, _valakas), 1400L);
					break;
				}
				case 9:
				{
					for(final Player pc : _players)
						if(pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1200, 150, -55, 2900, 15000);
						}
						else
							pc.leaveMovieMode();
					_socialTask = ThreadPoolManager.getInstance().schedule(new ValakasSpawn(10, _valakas), 6700L);
					break;
				}
				case 10:
				{
					for(final Player pc : _players)
						if(pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 750, 170, -10, 1700, 5700);
						}
						else
							pc.leaveMovieMode();
					_socialTask = ThreadPoolManager.getInstance().schedule(new ValakasSpawn(11, _valakas), 3700L);
					break;
				}
				case 11:
				{
					for(final Player pc : _players)
						if(pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 840, 170, -5, 1200, 2000);
						}
						else
							pc.leaveMovieMode();
					_socialTask = ThreadPoolManager.getInstance().schedule(new ValakasSpawn(12, _valakas), 2000L);
					break;
				}
				case 12:
				{
					for(final Player pc : _players)
						pc.leaveMovieMode();
					_mobiliseTask = ThreadPoolManager.getInstance().schedule(new SetMobilised(_valakas), 16L);
					final Location pos = new Location(Rnd.get(211080, 214909), Rnd.get(-115841, -112822), -1662, 0);
					_moveAtRandomTask = ThreadPoolManager.getInstance().schedule(new MoveAtRandom(_valakas, pos), 32L);
					setLastAttackTime();
					_sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 600000L);
					_activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), Config.VALAKAS_ACTIVITY_TIME);
					break;
				}
			}
		}
	}

	public static class ActivityTimeEnd implements Runnable
	{
		@Override
		public void run()
		{
			sleep();
		}
	}
}
