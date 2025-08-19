package bosses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.BossInstance;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.SocialAction;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.Log;

public class AntharasManager extends Functions implements ScriptFile
{
	private static final int _teleportCubeId = 31859;
	private static final int _antharasId = 29019;
	private static final Location _teleportCubeLocation;
	private static final Location _antharasLocation;
	private static BossInstance _antharas;
	private static NpcInstance _teleportCube;
	private static List<NpcInstance> _monsters;
	private static ScheduledFuture<?> _cubeSpawnTask;
	private static ScheduledFuture<?> _monsterSpawnTask;
	private static ScheduledFuture<?> _intervalEndTask;
	private static ScheduledFuture<?> _socialTask;
	private static ScheduledFuture<?> _mobiliseTask;
	private static ScheduledFuture<?> _behemothSpawnTask;
	private static ScheduledFuture<?> _bomberSpawnTask;
	private static ScheduledFuture<?> _selfDestructionTask;
	private static ScheduledFuture<?> _moveAtRandomTask;
	private static ScheduledFuture<?> _sleepCheckTask;
	private static ScheduledFuture<?> _onAnnihilatedTask;
	private static ScheduledFuture<?> _activityTimeEndTask;
	private static final int ANTHARAS_CIRCLET = 8568;
	private static EpicBossState _state;
	private static Zone _zone;
	private static long _lastAttackTime;
	private static final boolean FWA_MOVEATRANDOM = true;
	private static final int FWA_LIMITOFWEAK = 299;
	private static final int FWA_LIMITOFNORMAL = 399;
	private static final int FWA_INTERVALOFBEHEMOTHONWEAK = 480000;
	private static final int FWA_INTERVALOFBEHEMOTHONNORMAL = 300000;
	private static final int FWA_INTERVALOFBEHEMOTHONSTRONG = 180000;
	private static final int FWA_INTERVALOFBOMBERONWEAK = 360000;
	private static final int FWA_INTERVALOFBOMBERONNORMAL = 240000;
	private static final int FWA_INTERVALOFBOMBERONSTRONG = 180000;
	private static final int ANTHARAS_WEAK = 29066;
	private static final int ANTHARAS_NORMAL = 29067;
	private static final int ANTHARAS_STRONG = 29068;
	private static boolean Dying;

	private static void banishForeigners()
	{
		for(final Player player : getPlayersInside())
			player.teleToClosestTown();
	}

	private static synchronized void checkAnnihilated()
	{
		if(AntharasManager._onAnnihilatedTask == null && isPlayersAnnihilated())
			AntharasManager._onAnnihilatedTask = ThreadPoolManager.getInstance().schedule(new onAnnihilated(), 5000L);
	}

	private static List<Player> getPlayersInside()
	{
		return getZone().getInsidePlayers();
	}

	public static Zone getZone()
	{
		return AntharasManager._zone;
	}

	private static boolean isPlayersAnnihilated()
	{
		for(final Player pc : getPlayersInside())
			if(!pc.isDead())
				return false;
		return true;
	}

	private static void onAntharasDie(final Creature killer)
	{
		if(AntharasManager.Dying)
			return;
		AntharasManager.Dying = true;
		AntharasManager._state.setRespawnDate(Config.FWA_FIXINTERVALOFANTHARAS, Config.FWA_RANDOMINTERVALOFANTHARAS, Config.ANTHARAS_FIXRESP);
		AntharasManager._state.setState(EpicBossState.State.INTERVAL);
		AntharasManager._state.update();
		Log.addLog("Antharas died", "bosses");
		AntharasManager._cubeSpawnTask = ThreadPoolManager.getInstance().schedule(new CubeSpawn(), 20000L);
		if(killer != null && killer.isPlayable())
		{
			final Player pc = killer.getPlayer();
			if(pc == null)
				return;
			final Party party = pc.getParty();
			if(party != null)
			{
				for(final Player partyMember : party.getPartyMembers())
					if(partyMember != null && pc.isInRange(partyMember, 5000L) && partyMember.getInventory().getItemByItemId(8568) == null)
						partyMember.getInventory().addItem(8568, 1L);
			}
			else if(pc.getInventory().getItemByItemId(8568) == null)
				pc.getInventory().addItem(8568, 1L);
		}
		for(final NpcInstance mob : AntharasManager._monsters)
			mob.deleteMe();
		AntharasManager._monsters.clear();
	}

	public static void OnDie(final Creature self, final Creature killer)
	{
		if(self == null)
			return;
		if(Config.ANTHARAS_CHECK_ANNIHILATED && self.isPlayer() && AntharasManager._state != null && AntharasManager._state.getState() == EpicBossState.State.ALIVE && AntharasManager._zone != null && AntharasManager._zone.checkIfInZone(self.getX(), self.getY()))
			checkAnnihilated();
		else if(self.isNpc() && (self.getNpcId() == 29066 || self.getNpcId() == 29067 || self.getNpcId() == 29068))
			onAntharasDie(killer);
	}

	private static void setIntervalEndTask()
	{
		setUnspawn();
		if(AntharasManager._state.getState().equals(EpicBossState.State.ALIVE))
		{
			AntharasManager._state.setState(EpicBossState.State.NOTSPAWN);
			AntharasManager._state.update();
			return;
		}
		if(!AntharasManager._state.getState().equals(EpicBossState.State.INTERVAL))
		{
			AntharasManager._state.setRespawnDate(Config.FWA_FIXINTERVALOFANTHARAS, Config.FWA_RANDOMINTERVALOFANTHARAS, Config.ANTHARAS_FIXRESP);
			AntharasManager._state.setState(EpicBossState.State.INTERVAL);
			AntharasManager._state.update();
		}
		AntharasManager._intervalEndTask = ThreadPoolManager.getInstance().schedule(new IntervalEnd(), AntharasManager._state.getInterval());
	}

	private static void setUnspawn()
	{
		banishForeigners();
		if(AntharasManager._antharas != null)
			AntharasManager._antharas.deleteMe();
		AntharasManager._antharas = null;
		if(AntharasManager._teleportCube != null)
			AntharasManager._teleportCube.deleteMe();
		AntharasManager._teleportCube = null;
		for(final NpcInstance mob : AntharasManager._monsters)
			mob.deleteMe();
		AntharasManager._monsters.clear();
		if(AntharasManager._cubeSpawnTask != null)
		{
			AntharasManager._cubeSpawnTask.cancel(false);
			AntharasManager._cubeSpawnTask = null;
		}
		if(AntharasManager._monsterSpawnTask != null)
		{
			AntharasManager._monsterSpawnTask.cancel(false);
			AntharasManager._monsterSpawnTask = null;
		}
		if(AntharasManager._intervalEndTask != null)
		{
			AntharasManager._intervalEndTask.cancel(false);
			AntharasManager._intervalEndTask = null;
		}
		if(AntharasManager._socialTask != null)
		{
			AntharasManager._socialTask.cancel(false);
			AntharasManager._socialTask = null;
		}
		if(AntharasManager._mobiliseTask != null)
		{
			AntharasManager._mobiliseTask.cancel(false);
			AntharasManager._mobiliseTask = null;
		}
		if(AntharasManager._behemothSpawnTask != null)
		{
			AntharasManager._behemothSpawnTask.cancel(false);
			AntharasManager._behemothSpawnTask = null;
		}
		if(AntharasManager._bomberSpawnTask != null)
		{
			AntharasManager._bomberSpawnTask.cancel(false);
			AntharasManager._bomberSpawnTask = null;
		}
		if(AntharasManager._selfDestructionTask != null)
		{
			AntharasManager._selfDestructionTask.cancel(false);
			AntharasManager._selfDestructionTask = null;
		}
		if(AntharasManager._moveAtRandomTask != null)
		{
			AntharasManager._moveAtRandomTask.cancel(false);
			AntharasManager._moveAtRandomTask = null;
		}
		if(AntharasManager._sleepCheckTask != null)
		{
			AntharasManager._sleepCheckTask.cancel(false);
			AntharasManager._sleepCheckTask = null;
		}
		if(AntharasManager._onAnnihilatedTask != null)
		{
			AntharasManager._onAnnihilatedTask.cancel(false);
			AntharasManager._onAnnihilatedTask = null;
		}
		if(AntharasManager._activityTimeEndTask != null)
		{
			AntharasManager._activityTimeEndTask.cancel(true);
			AntharasManager._activityTimeEndTask = null;
		}
	}

	private void init()
	{
		AntharasManager._state = new EpicBossState(29019);
		AntharasManager._zone = ZoneManager.getInstance().getZoneById(Zone.ZoneType.epic, 702002, false);
		ScriptFile._log.info("AntharasManager: State of Antharas is " + AntharasManager._state.getState() + ".");
		if(AntharasManager._state.getAliveId() > 0)
		{
			(AntharasManager._antharas = new BossInstance(IdFactory.getInstance().getNextId(), NpcTable.getTemplate(AntharasManager._state.getAliveId()))).setSpawnedLoc(AntharasManager._state.getLoc());
			AntharasManager._antharas.setCurrentHp(AntharasManager._state.getCurrentHp(), true);
			AntharasManager._antharas.setCurrentMp(AntharasManager._state.getCurrentMp());
			AntharasManager._antharas.spawnMe(AntharasManager._antharas.getSpawnedLoc());
			int intervalOfBehemoth;
			int intervalOfBomber;
			if(AntharasManager._antharas.getNpcId() == 29066)
			{
				intervalOfBehemoth = 480000;
				intervalOfBomber = 360000;
			}
			else if(AntharasManager._antharas.getNpcId() == 29068)
			{
				intervalOfBehemoth = 180000;
				intervalOfBomber = 180000;
			}
			else
			{
				intervalOfBehemoth = 300000;
				intervalOfBomber = 240000;
			}
			AntharasManager._behemothSpawnTask = ThreadPoolManager.getInstance().schedule(new BehemothSpawn(intervalOfBehemoth), 30000L);
			AntharasManager._bomberSpawnTask = ThreadPoolManager.getInstance().schedule(new BomberSpawn(intervalOfBomber), 30000L);
			setLastAttackTime();
			AntharasManager._sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 600000L);
			AntharasManager._activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), Config.ANTHARAS_ACTIVITY_TIME);
		}
		else if(!AntharasManager._state.getState().equals(EpicBossState.State.NOTSPAWN))
			setIntervalEndTask();
		final Date dt = new Date(AntharasManager._state.getRespawnDate());
		ScriptFile._log.info("AntharasManager: Next spawn date of Antharas is " + dt + ".");
	}

	private static void sleep()
	{
		setUnspawn();
		if(AntharasManager._state.getState().equals(EpicBossState.State.ALIVE))
		{
			AntharasManager._state.setState(EpicBossState.State.NOTSPAWN);
			AntharasManager._state.update();
		}
	}

	public static void setLastAttackTime()
	{
		AntharasManager._lastAttackTime = System.currentTimeMillis();
	}

	public static synchronized void setAntharasSpawnTask()
	{
		if(AntharasManager._monsterSpawnTask == null)
			AntharasManager._monsterSpawnTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(1), Config.FWA_APPTIMEOFANTHARAS);
	}

	public static int isEnableEnterToLair()
	{
		return AntharasManager._state.getState() == EpicBossState.State.NOTSPAWN ? 1 : AntharasManager._state.getState() == EpicBossState.State.ALIVE ? 2 : (int) (AntharasManager._state.getRespawnDate() / 1000L);
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
		if(AntharasManager._state.getState() == EpicBossState.State.ALIVE && AntharasManager._antharas != null && !AntharasManager._antharas.isDead())
			AntharasManager._state.aliveSave(AntharasManager._antharas.getNpcId(), AntharasManager._antharas.getCurrentHp(), AntharasManager._antharas.getCurrentMp(), AntharasManager._antharas.getLoc());
	}

	static
	{
		_teleportCubeLocation = new Location(177615, 114941, -7709, 0);
		_antharasLocation = new Location(181323, 114850, -7623, 32542);
		AntharasManager._antharas = null;
		AntharasManager._teleportCube = null;
		AntharasManager._monsters = new ArrayList<NpcInstance>();
		AntharasManager._cubeSpawnTask = null;
		AntharasManager._monsterSpawnTask = null;
		AntharasManager._intervalEndTask = null;
		AntharasManager._socialTask = null;
		AntharasManager._mobiliseTask = null;
		AntharasManager._behemothSpawnTask = null;
		AntharasManager._bomberSpawnTask = null;
		AntharasManager._selfDestructionTask = null;
		AntharasManager._moveAtRandomTask = null;
		AntharasManager._sleepCheckTask = null;
		AntharasManager._onAnnihilatedTask = null;
		AntharasManager._activityTimeEndTask = null;
		AntharasManager._lastAttackTime = 0L;
		AntharasManager.Dying = false;
	}

	private static class AntharasSpawn implements Runnable
	{
		private int _distance;
		private int _taskId;
		private List<Player> _players;

		AntharasSpawn(final int taskId)
		{
			_distance = 2750;
			_taskId = 0;
			_players = getPlayersInside();
			_taskId = taskId;
		}

		@Override
		public void run()
		{
			SocialAction sa = null;
			if(AntharasManager._socialTask != null)
			{
				AntharasManager._socialTask.cancel(false);
				AntharasManager._socialTask = null;
			}
			switch(_taskId)
			{
				case 1:
				{
					if(AntharasManager._antharas != null)
						return;
					int npcId;
					if(_players.size() <= 299)
						npcId = 29066;
					else if(_players.size() > 399)
						npcId = 29068;
					else
						npcId = 29067;
					AntharasManager.Dying = false;
					AntharasManager._antharas = new BossInstance(IdFactory.getInstance().getNextId(), NpcTable.getTemplate(npcId));
					AntharasManager._antharas.setSpawnedLoc(AntharasManager._antharasLocation);
					AntharasManager._antharas.spawnMe(AntharasManager._antharas.getSpawnedLoc());
					AntharasManager._antharas.setImmobilized(true);
					AntharasManager._state.setRespawnDate(Config.FWA_FIXINTERVALOFANTHARAS, Config.FWA_RANDOMINTERVALOFANTHARAS, Config.ANTHARAS_FIXRESP);
					AntharasManager._state.setState(EpicBossState.State.ALIVE);
					AntharasManager._state.update();
					int intervalOfBehemoth;
					int intervalOfBomber;
					if(_players.size() <= 299)
					{
						intervalOfBehemoth = 480000;
						intervalOfBomber = 360000;
					}
					else if(_players.size() > 399)
					{
						intervalOfBehemoth = 180000;
						intervalOfBomber = 180000;
					}
					else
					{
						intervalOfBehemoth = 300000;
						intervalOfBomber = 240000;
					}
					AntharasManager._behemothSpawnTask = ThreadPoolManager.getInstance().schedule(new BehemothSpawn(intervalOfBehemoth), 30000L);
					AntharasManager._bomberSpawnTask = ThreadPoolManager.getInstance().schedule(new BomberSpawn(intervalOfBomber), 30000L);
					AntharasManager._socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(2), 16L);
					break;
				}
				case 2:
				{
					for(final Player pc : _players)
						if(pc.getDistance(AntharasManager._antharas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(AntharasManager._antharas, 700, 13, -19, 0, 10000);
						}
						else
							pc.leaveMovieMode();
					AntharasManager._socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(3), 3000L);
					break;
				}
				case 3:
				{
					sa = new SocialAction(AntharasManager._antharas.getObjectId(), 1);
					AntharasManager._antharas.broadcastPacket(new L2GameServerPacket[] { sa });
					for(final Player pc : _players)
						if(pc.getDistance(AntharasManager._antharas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(AntharasManager._antharas, 700, 13, 0, 6000, 10000);
						}
						else
							pc.leaveMovieMode();
					AntharasManager._socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(4), 10000L);
					break;
				}
				case 4:
				{
					for(final Player pc : _players)
						if(pc.getDistance(AntharasManager._antharas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(AntharasManager._antharas, 3800, 0, -3, 0, 10000);
						}
						else
							pc.leaveMovieMode();
					AntharasManager._socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(5), 200L);
					break;
				}
				case 5:
				{
					sa = new SocialAction(AntharasManager._antharas.getObjectId(), 2);
					AntharasManager._antharas.broadcastPacket(new L2GameServerPacket[] { sa });
					for(final Player pc : _players)
						if(pc.getDistance(AntharasManager._antharas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(AntharasManager._antharas, 1200, 0, -3, 22000, 11000);
						}
						else
							pc.leaveMovieMode();
					AntharasManager._socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(6), 10800L);
					break;
				}
				case 6:
				{
					for(final Player pc : _players)
						if(pc.getDistance(AntharasManager._antharas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(AntharasManager._antharas, 1200, 0, -3, 300, 2000);
						}
						else
							pc.leaveMovieMode();
					AntharasManager._socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(7), 1900L);
					break;
				}
				case 7:
				{
					AntharasManager._antharas.abortCast(true, false);
					for(final Player pc : _players)
						pc.leaveMovieMode();
					//AntharasManager._mobiliseTask = ThreadPoolManager.getInstance().schedule(new SetMobilised(AntharasManager._antharas), 16L);
					AntharasManager._antharas.setImmobilized(false);
					AntharasManager._antharas.setRunning();
					final Location pos = new Location(177615, 114941, -7709);
					AntharasManager._antharas.moveToLocation(pos, 1000, false);
					//AntharasManager._moveAtRandomTask = ThreadPoolManager.getInstance().schedule(new MoveAtRandom(AntharasManager._antharas, pos), 32L);
					AntharasManager.setLastAttackTime();
					AntharasManager._sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 600000L);
					AntharasManager._activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), Config.ANTHARAS_ACTIVITY_TIME);
					break;
				}
			}
		}
	}

	private static class BehemothSpawn implements Runnable
	{
		private int _interval;

		public BehemothSpawn(final int interval)
		{
			_interval = interval;
		}

		@Override
		public void run()
		{
			final MonsterInstance behemoth = new MonsterInstance(IdFactory.getInstance().getNextId(), NpcTable.getTemplate(29069));
			behemoth.setSpawnedLoc(new Location(Rnd.get(175000, 179900), Rnd.get(112400, 116000), -7709));
			behemoth.spawnMe(behemoth.getSpawnedLoc());
			AntharasManager._monsters.add(behemoth);
			if(AntharasManager._behemothSpawnTask != null)
			{
				AntharasManager._behemothSpawnTask.cancel(false);
				AntharasManager._behemothSpawnTask = null;
			}
			AntharasManager._behemothSpawnTask = ThreadPoolManager.getInstance().schedule(new BehemothSpawn(_interval), _interval);
		}
	}

	private static class BomberSpawn implements Runnable
	{
		private int _interval;

		public BomberSpawn(final int interval)
		{
			_interval = interval;
		}

		@Override
		public void run()
		{
			final int npcId = Rnd.get(29070, 29076);
			final MonsterInstance bomber = new MonsterInstance(IdFactory.getInstance().getNextId(), NpcTable.getTemplate(npcId));
			bomber.setSpawnedLoc(new Location(Rnd.get(175000, 179900), Rnd.get(112400, 116000), -7709));
			bomber.spawnMe(bomber.getSpawnedLoc());
			AntharasManager._monsters.add(bomber);
			AntharasManager._selfDestructionTask = ThreadPoolManager.getInstance().schedule(new SelfDestructionOfBomber(bomber), 3000L);
			if(AntharasManager._bomberSpawnTask != null)
			{
				AntharasManager._bomberSpawnTask.cancel(false);
				AntharasManager._bomberSpawnTask = null;
			}
			AntharasManager._bomberSpawnTask = ThreadPoolManager.getInstance().schedule(new BomberSpawn(_interval), _interval);
		}
	}

	private static class CheckLastAttack implements Runnable
	{
		@Override
		public void run()
		{
			if(AntharasManager._state.getState().equals(EpicBossState.State.ALIVE))
				if(AntharasManager._lastAttackTime + Config.ANTHARAS_LIMITUNTILSLEEP < System.currentTimeMillis())
					sleep();
				else
					AntharasManager._sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 60000L);
		}
	}

	private static class CubeSpawn implements Runnable
	{
		@Override
		public void run()
		{
			if(AntharasManager._behemothSpawnTask != null)
			{
				AntharasManager._behemothSpawnTask.cancel(false);
				AntharasManager._behemothSpawnTask = null;
			}
			if(AntharasManager._bomberSpawnTask != null)
			{
				AntharasManager._bomberSpawnTask.cancel(false);
				AntharasManager._bomberSpawnTask = null;
			}
			if(AntharasManager._selfDestructionTask != null)
			{
				AntharasManager._selfDestructionTask.cancel(false);
				AntharasManager._selfDestructionTask = null;
			}
			AntharasManager._teleportCube = new NpcInstance(IdFactory.getInstance().getNextId(), NpcTable.getTemplate(31859));
			AntharasManager._teleportCube.setCurrentHpMp(AntharasManager._teleportCube.getMaxHp(), AntharasManager._teleportCube.getMaxMp(), true);
			AntharasManager._teleportCube.setSpawnedLoc(AntharasManager._teleportCubeLocation);
			AntharasManager._teleportCube.spawnMe(AntharasManager._teleportCube.getSpawnedLoc());
		}
	}

	private static class IntervalEnd implements Runnable
	{
		@Override
		public void run()
		{
			AntharasManager._state.setState(EpicBossState.State.NOTSPAWN);
			AntharasManager._state.update();
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

	private static class onAnnihilated implements Runnable
	{
		@Override
		public void run()
		{
			sleep();
		}
	}

	private static class SelfDestructionOfBomber implements Runnable
	{
		private NpcInstance _bomber;

		public SelfDestructionOfBomber(final NpcInstance bomber)
		{
			_bomber = bomber;
		}

		@Override
		public void run()
		{
			Skill skill = null;
			switch(_bomber.getNpcId())
			{
				case 29070:
				case 29071:
				case 29072:
				case 29073:
				case 29074:
				case 29075:
				{
					skill = SkillTable.getInstance().getInfo(5097, 1);
					break;
				}
				case 29076:
				{
					skill = SkillTable.getInstance().getInfo(5094, 1);
					break;
				}
			}
			_bomber.doCast(skill, (Creature) null, false);
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

	public static class ActivityTimeEnd implements Runnable
	{
		@Override
		public void run()
		{
			sleep();
		}
	}
}
