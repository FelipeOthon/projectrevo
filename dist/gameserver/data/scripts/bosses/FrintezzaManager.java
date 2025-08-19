package bosses;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.listener.ZoneEnterLeaveListener;
import l2s.gameserver.listener.actor.OnCurrentHpDamageListener;
import l2s.gameserver.listener.actor.OnDeathListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.Earthquake;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillCanceled;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.NpcInfo;
import l2s.gameserver.network.l2.s2c.SocialAction;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.Log;

public class FrintezzaManager extends Functions implements ScriptFile
{
	private static final Logger _log;
	private static Location frintezzaSpawn;
	private static Location scarletSpawnWeak;
	private static Location[] portraitSpawns;
	private static Location[] demonSpawns;
	private static Location cubeSpawn;
	private static NpcInstance frintezza;
	private static NpcInstance strongScarlet;
	private static NpcInstance cube;
	private NpcInstance weakScarlet;
	private static NpcInstance[] portraits;
	private static NpcInstance[] demons;
	private static int _intervalOfFrintezzaSongs;
	private static int _scarletMorph;
	private static ScheduledFuture<?> _monsterSpawnTask;
	private static ScheduledFuture<?> _activityTimeEndTask;
	private static ScheduledFuture<?> _intervalEndTask;
	private static ScheduledFuture<?> _dieTask;
	private static EpicBossState _state;
	private static Zone _zone;
	private DeathListener _deathListener;
	private ZoneListener _zoneListener;
	private CurrentHpListener _currentHpListener;
	private static final int FWF_INTERVALOFNEXTMONSTER = 30000;
	private static final int _frintezzaId = 29045;
	private static final int _strongScarletId = 29047;
	private static final int _frintezzasSwordId = 7903;
	private static NpcInstance _frintezzaDummy;
	private static NpcInstance _overheadDummy;
	private static boolean STARTED;

	public FrintezzaManager()
	{
		_deathListener = new DeathListener();
		_zoneListener = new ZoneListener();
		_currentHpListener = new CurrentHpListener();
	}

	private void init()
	{
		FrintezzaManager._state = new EpicBossState(29045);
		FrintezzaManager._zone = ZoneManager.getInstance().getZoneById(Zone.ZoneType.epic, 702120, false);
		FrintezzaManager._zone.getListenerEngine().addMethodInvokedListener(_zoneListener);
		FrintezzaManager._log.info("FrintezzaManager: State of Frintezza is " + FrintezzaManager._state.getState() + ".");
		if(!FrintezzaManager._state.getState().equals(EpicBossState.State.NOTSPAWN))
			setIntervalEndTask();
		final Date dt = new Date(FrintezzaManager._state.getRespawnDate());
		FrintezzaManager._log.info("FrintezzaManager: Next spawn date of Frintezza is " + dt + ".");
	}

	private NpcInstance spawn(final Location loc)
	{
		final NpcTemplate template = NpcTable.getTemplate(loc.id);
		final NpcInstance npc = template.getNewInstance();
		npc.setSpawnedLoc(loc);
		npc.setHeading(loc.h);
		npc.setXYZInvisible(loc);
		npc.spawnMe();
		return npc;
	}

	private void setScarletSpawnTask(final boolean forced)
	{
		if(forced || FrintezzaManager._state.getState().equals(EpicBossState.State.NOTSPAWN) && !FrintezzaManager.STARTED)
		{
			FrintezzaManager.STARTED = true;
			Log.addLog("Frintezza activated", "bosses");
			LastImperialTombManager.setReachToHall();
			if(FrintezzaManager._monsterSpawnTask != null)
				FrintezzaManager._monsterSpawnTask.cancel(true);
			FrintezzaManager._monsterSpawnTask = ThreadPoolManager.getInstance().schedule(new Spawn(1), forced ? 1000L : 300000L);
		}
	}

	public void startFrintezza()
	{
		final Player player = getSelf();
		if(player == null || !player.isGM())
			return;
		setScarletSpawnTask(true);
	}

	private static void showSocialActionMovie(final NpcInstance target, final int dist, final int yaw, final int pitch, final int time, final int duration, final int socialAction)
	{
		if(target == null)
			return;
		for(final Player pc : getPlayersInside())
			if(pc.getDistance(target) <= 2550.0)
			{
				pc.enterMovieMode();
				pc.specialCamera(target, dist, yaw, pitch, time, duration);
			}
			else
				pc.leaveMovieMode();
		if(socialAction > 0 && socialAction < 5)
			target.broadcastPacket(new L2GameServerPacket[] { new SocialAction(target.getObjectId(), socialAction) });
	}

	private void setUnspawn(final boolean kill)
	{
		banishForeigners();
		if(kill)
		{
			FrintezzaManager._state.setState(EpicBossState.State.DEAD);
			FrintezzaManager._state.update();
			Log.addLog("Frintezza died", "bosses");
		}
		if(FrintezzaManager.frintezza != null)
			FrintezzaManager.frintezza.deleteMe();
		if(weakScarlet != null)
			weakScarlet.deleteMe();
		if(FrintezzaManager.strongScarlet != null)
			FrintezzaManager.strongScarlet.deleteMe();
		if(FrintezzaManager.cube != null)
			FrintezzaManager.cube.deleteMe();
		final NpcInstance l2NpcInstance = FrintezzaManager.strongScarlet = FrintezzaManager.cube = null;
		weakScarlet = l2NpcInstance;
		FrintezzaManager.frintezza = l2NpcInstance;
		deletePortrait();
		if(FrintezzaManager._monsterSpawnTask != null)
		{
			FrintezzaManager._monsterSpawnTask.cancel(true);
			FrintezzaManager._monsterSpawnTask = null;
		}
		if(FrintezzaManager._intervalEndTask != null)
		{
			FrintezzaManager._intervalEndTask.cancel(true);
			FrintezzaManager._intervalEndTask = null;
		}
		if(FrintezzaManager._activityTimeEndTask != null)
		{
			FrintezzaManager._activityTimeEndTask.cancel(true);
			FrintezzaManager._activityTimeEndTask = null;
		}
		if(FrintezzaManager._dieTask != null)
		{
			FrintezzaManager._dieTask.cancel(false);
			FrintezzaManager._dieTask = null;
		}
		if(kill)
			setIntervalEndTask();
		else
		{
			FrintezzaManager._state.setState(EpicBossState.State.NOTSPAWN);
			FrintezzaManager._state.update();
		}
		FrintezzaManager.STARTED = false;
	}

	private static void deletePortrait()
	{
		for(int i = 0; i < 4; ++i)
		{
			if(FrintezzaManager.portraits[i] != null)
			{
				FrintezzaManager.portraits[i].deleteMe();
				FrintezzaManager.portraits[i] = null;
			}
			if(FrintezzaManager.demons[i] != null)
			{
				FrintezzaManager.demons[i].deleteMe();
				FrintezzaManager.demons[i] = null;
			}
		}
	}

	private void blockAll(final boolean flag)
	{
		block(FrintezzaManager.frintezza, flag);
		block(weakScarlet, flag);
		block(FrintezzaManager.strongScarlet, flag);
		for(int i = 0; i < 4; ++i)
		{
			block(FrintezzaManager.portraits[i], flag);
			block(FrintezzaManager.demons[i], flag);
		}
	}

	private static void block(final NpcInstance npc, final boolean flag)
	{
		if(npc == null || npc.isDead())
			return;
		if(flag)
		{
			npc.abortAttack(true, false);
			npc.abortCast(true, false);
			npc.setTarget((GameObject) null);
			if(npc.isMoving)
				npc.stopMove();
			npc.block();
		}
		else
			npc.unblock();
		npc.setIsInvul(flag);
	}

	public static void setIntervalEndTask()
	{
		if(!FrintezzaManager._state.getState().equals(EpicBossState.State.INTERVAL))
		{
			FrintezzaManager._state.setRespawnDate(Config.FWF_FIXINTERVALOFFRINTEZZA, Config.FWF_RANDOMINTERVALOFFRINTEZZA, Config.FRINTEZZA_FIXRESP);
			FrintezzaManager._state.setState(EpicBossState.State.INTERVAL);
			FrintezzaManager._state.update();
		}
		FrintezzaManager._intervalEndTask = ThreadPoolManager.getInstance().schedule(new IntervalEnd(), FrintezzaManager._state.getInterval());
	}

	private static void banishForeigners()
	{
		for(final Player player : getPlayersInside())
			if(!player.isGM())
				player.teleToClosestTown();
	}

	private static Player getRandomPlayer()
	{
		final List<Player> list = FrintezzaManager._zone.getInsidePlayers();
		if(list.isEmpty())
			return null;
		return list.get(Rnd.get(list.size()));
	}

	private static List<Player> getPlayersInside()
	{
		return FrintezzaManager._zone.getInsidePlayers();
	}

	public static Zone getZone()
	{
		return FrintezzaManager._zone;
	}

	public static int isEnableEnterToLair()
	{
		return FrintezzaManager._state.getState() == EpicBossState.State.NOTSPAWN ? 1 : FrintezzaManager._state.getState() == EpicBossState.State.ALIVE ? 2 : (int) (FrintezzaManager._state.getRespawnDate() / 1000L);
	}

	@Override
	public void onLoad()
	{
		init();
	}

	@Override
	public void onReload()
	{
		FrintezzaManager._zone.getListenerEngine().removeMethodInvokedListener(_zoneListener);
	}

	@Override
	public void onShutdown()
	{}

	static
	{
		_log = LoggerFactory.getLogger(ValakasManager.class);
		FrintezzaManager.frintezzaSpawn = new Location(174240, -89805, -5022, 16048, 29045);
		FrintezzaManager.scarletSpawnWeak = new Location(174232, -88020, -5112, 16384, 29046);
		FrintezzaManager.portraitSpawns = new Location[] {
				new Location(175832, -87176, -5104, 35048, 29048),
				new Location(175880, -88696, -5104, 28205, 29049),
				new Location(172600, -88696, -5104, 64817, 29048),
				new Location(172632, -87176, -5104, 57730, 29049) };
		FrintezzaManager.demonSpawns = new Location[] {
				new Location(175832, -87176, -5104, 35048, 29050),
				new Location(175880, -88696, -5104, 28205, 29051),
				new Location(172600, -88696, -5104, 64817, 29051),
				new Location(172632, -87176, -5104, 57730, 29050) };
		FrintezzaManager.cubeSpawn = new Location(174232, -88020, -5114, 16384, 29061);
		FrintezzaManager.portraits = new NpcInstance[4];
		FrintezzaManager.demons = new NpcInstance[4];
		FrintezzaManager._intervalOfFrintezzaSongs = 30000;
		FrintezzaManager._scarletMorph = 0;
		FrintezzaManager._monsterSpawnTask = null;
		FrintezzaManager._activityTimeEndTask = null;
		FrintezzaManager._intervalEndTask = null;
		FrintezzaManager._dieTask = null;
		FrintezzaManager.STARTED = false;
	}

	public class ZoneListener extends ZoneEnterLeaveListener
	{
		@Override
		public void objectEntered(final Zone zone, final GameObject object)
		{
			if(object != null && object.isPlayer() && !object.inObserverMode() && !((Player) object).isGM())
				setScarletSpawnTask(false);
		}

		@Override
		public void objectLeaved(final Zone zone, final GameObject object)
		{}
	}

	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(final Creature actor, final double damage, final Creature attacker, final Skill skill)
		{
			if(actor == null || actor.isDead() || actor != weakScarlet)
				return;
			final double newHp = actor.getCurrentHp() - damage;
			final double maxHp = actor.getMaxHp();
			switch(FrintezzaManager._scarletMorph)
			{
				case 1:
				{
					if(newHp < maxHp * 2.0 / 3.0)
					{
						FrintezzaManager._scarletMorph = 2;
						ThreadPoolManager.getInstance().schedule(new SecondMorph(1), 1100L);
						break;
					}
					break;
				}
				case 2:
				{
					if(newHp < maxHp * 1.0 / 3.0)
					{
						FrintezzaManager._scarletMorph = 3;
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(1), 2000L);
						break;
					}
					break;
				}
			}
		}
	}

	private class Spawn implements Runnable
	{
		private int _taskId;

		public Spawn(final int taskId)
		{
			_taskId = 0;
			_taskId = taskId;
		}

		@Override
		public void run()
		{
			try
			{
				switch(_taskId)
				{
					case 1:
					{
						LastImperialTombManager.cleanUpTomb(false);
						FrintezzaManager._state.setRespawnDate(Config.FWF_FIXINTERVALOFFRINTEZZA, Config.FWF_RANDOMINTERVALOFFRINTEZZA, Config.FRINTEZZA_FIXRESP);
						FrintezzaManager._state.setState(EpicBossState.State.ALIVE);
						FrintezzaManager._state.update();
						FrintezzaManager._frintezzaDummy = FrintezzaManager.this.spawn(new Location(174240, -89805, -5080, 16048, 29059));
						FrintezzaManager._overheadDummy = FrintezzaManager.this.spawn(new Location(174232, -88020, -4000, 16384, 29059));
						FrintezzaManager._overheadDummy.setCollisionHeight(800.0);
						FrintezzaManager._overheadDummy.broadcastPacket(new L2GameServerPacket[] {
								new NpcInfo(FrintezzaManager._overheadDummy, (Creature) null) });
						ThreadPoolManager.getInstance().schedule(new Spawn(2), 1000L);
						break;
					}
					case 2:
					{
						showSocialActionMovie(FrintezzaManager._overheadDummy, 0, 0, -75, 0, 0, 0);
						showSocialActionMovie(FrintezzaManager._overheadDummy, 1000, 90, -10, 6500, 8000, 0);
						FrintezzaManager.frintezza = FrintezzaManager.this.spawn(FrintezzaManager.frintezzaSpawn);
						for(int i = 0; i < 4; ++i)
						{
							(FrintezzaManager.portraits[i] = FrintezzaManager.this.spawn(FrintezzaManager.portraitSpawns[i])).setImmobilized(true);
							FrintezzaManager.demons[i] = FrintezzaManager.this.spawn(FrintezzaManager.demonSpawns[i]);
						}
						blockAll(true);
						ThreadPoolManager.getInstance().schedule(new Spawn(3), 6500L);
						break;
					}
					case 3:
					{
						showSocialActionMovie(FrintezzaManager._frintezzaDummy, 1800, 90, 8, 6500, 7000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(4), 900L);
						break;
					}
					case 4:
					{
						showSocialActionMovie(FrintezzaManager._frintezzaDummy, 140, 90, 10, 2500, 4500, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(5), 4000L);
						break;
					}
					case 5:
					{
						showSocialActionMovie(FrintezzaManager.frintezza, 40, 75, -10, 0, 1000, 0);
						showSocialActionMovie(FrintezzaManager.frintezza, 40, 75, -10, 0, 12000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(6), 1350L);
						break;
					}
					case 6:
					{
						FrintezzaManager.frintezza.broadcastPacket(new L2GameServerPacket[] { new SocialAction(FrintezzaManager.frintezza.getObjectId(), 2) });
						ThreadPoolManager.getInstance().schedule(new Spawn(7), 7000L);
						break;
					}
					case 7:
					{
						FrintezzaManager._overheadDummy.deleteMe();
						FrintezzaManager._overheadDummy = null;
						FrintezzaManager._frintezzaDummy.deleteMe();
						FrintezzaManager._frintezzaDummy = null;
						ThreadPoolManager.getInstance().schedule(new Spawn(8), 1000L);
						break;
					}
					case 8:
					{
						showSocialActionMovie(FrintezzaManager.demons[0], 140, 0, 3, 22000, 3000, 1);
						ThreadPoolManager.getInstance().schedule(new Spawn(9), 2800L);
						break;
					}
					case 9:
					{
						showSocialActionMovie(FrintezzaManager.demons[1], 140, 0, 3, 22000, 3000, 1);
						ThreadPoolManager.getInstance().schedule(new Spawn(10), 2800L);
						break;
					}
					case 10:
					{
						showSocialActionMovie(FrintezzaManager.demons[2], 140, 180, 3, 22000, 3000, 1);
						ThreadPoolManager.getInstance().schedule(new Spawn(11), 2800L);
						break;
					}
					case 11:
					{
						showSocialActionMovie(FrintezzaManager.demons[3], 140, 180, 3, 22000, 3000, 1);
						ThreadPoolManager.getInstance().schedule(new Spawn(12), 3000L);
						break;
					}
					case 12:
					{
						showSocialActionMovie(FrintezzaManager.frintezza, 240, 90, 0, 0, 1000, 0);
						showSocialActionMovie(FrintezzaManager.frintezza, 240, 90, 25, 5500, 10000, 3);
						ThreadPoolManager.getInstance().schedule(new Spawn(13), 3000L);
						break;
					}
					case 13:
					{
						showSocialActionMovie(FrintezzaManager.frintezza, 100, 195, 35, 0, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(14), 700L);
						break;
					}
					case 14:
					{
						showSocialActionMovie(FrintezzaManager.frintezza, 100, 195, 35, 0, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(15), 1300L);
						break;
					}
					case 15:
					{
						showSocialActionMovie(FrintezzaManager.frintezza, 120, 180, 45, 1500, 10000, 0);
						FrintezzaManager.frintezza.broadcastPacket(new L2GameServerPacket[] {
								new MagicSkillUse(FrintezzaManager.frintezza, FrintezzaManager.frintezza, 5006, 1, 34000, 0L) });
						ThreadPoolManager.getInstance().schedule(new Spawn(16), 1500L);
						break;
					}
					case 16:
					{
						showSocialActionMovie(FrintezzaManager.frintezza, 520, 135, 45, 8000, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(17), 7500L);
						break;
					}
					case 17:
					{
						showSocialActionMovie(FrintezzaManager.frintezza, 1500, 110, 25, 10000, 13000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(18), 9500L);
						break;
					}
					case 18:
					{
						weakScarlet = FrintezzaManager.this.spawn(FrintezzaManager.scarletSpawnWeak);
						block(weakScarlet, true);
						weakScarlet.addListener(_currentHpListener);
						weakScarlet.doCast(SkillTable.getInstance().getInfo(5004, 1), (Creature) null, false);
						final Earthquake eq = new Earthquake(weakScarlet.getLoc(), 50, 6);
						for(final Player pc : getPlayersInside())
							pc.broadcastPacket(new L2GameServerPacket[] { eq });
						showSocialActionMovie(weakScarlet, 1000, 160, 20, 6000, 6000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(19), 5500L);
						break;
					}
					case 19:
					{
						showSocialActionMovie(weakScarlet, 800, 160, 5, 1000, 10000, 2);
						ThreadPoolManager.getInstance().schedule(new Spawn(20), 2100L);
						break;
					}
					case 20:
					{
						showSocialActionMovie(weakScarlet, 300, 60, 8, 0, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(21), 2000L);
						break;
					}
					case 21:
					{
						showSocialActionMovie(weakScarlet, 1000, 90, 10, 3000, 5000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(22), 3000L);
						break;
					}
					case 22:
					{
						for(final Player pc : getPlayersInside())
							pc.leaveMovieMode();
						ThreadPoolManager.getInstance().schedule(new Spawn(23), 2000L);
						break;
					}
					case 23:
					{
						blockAll(false);
						FrintezzaManager._scarletMorph = 1;
						FrintezzaManager._activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), Config.FRINTEZZA_ACTIVITY_TIME);
						ThreadPoolManager.getInstance().schedule(new respawnDemons(), 30000L);
						ThreadPoolManager.getInstance().schedule(new Music(), Rnd.get(FrintezzaManager._intervalOfFrintezzaSongs));
						break;
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public class Music implements Runnable
	{
		@Override
		public void run()
		{
			if(FrintezzaManager.frintezza == null)
				return;
			final int song = Math.max(1, Math.min(5, getSong()));
			String song_name = "";
			switch(song)
			{
				case 1:
				{
					song_name = "Concert Hall Melody";
					break;
				}
				case 2:
				{
					song_name = "Rampaging Opus en masse";
					break;
				}
				case 3:
				{
					song_name = "Power Encore";
					break;
				}
				case 4:
				{
					song_name = "Mournful Chorale Prelude";
					break;
				}
				case 5:
				{
					song_name = "Hypnotic Mazurka";
					break;
				}
			}
			if(!FrintezzaManager.frintezza.isBlocked())
			{
				FrintezzaManager.frintezza.broadcastPacket(new L2GameServerPacket[] {
						new ExShowScreenMessage(song_name, 3000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true) });
				FrintezzaManager.frintezza.broadcastPacket(new L2GameServerPacket[] {
						new MagicSkillUse(FrintezzaManager.frintezza, FrintezzaManager.frintezza, 5007, song, FrintezzaManager._intervalOfFrintezzaSongs, 0L) });
				ThreadPoolManager.getInstance().schedule(new SongEffectLaunched(getSongTargets(song), song, 10000), 10000L);
			}
			ThreadPoolManager.getInstance().schedule(new Music(), FrintezzaManager._intervalOfFrintezzaSongs + Rnd.get(10000));
		}

		private Set<Creature> getSongTargets(final int songId)
		{
			final Set<Creature> targets = new HashSet<>();
			if(songId < 4)
			{
				if(weakScarlet != null && !weakScarlet.isDead())
					targets.add(weakScarlet);
				if(FrintezzaManager.strongScarlet != null && !FrintezzaManager.strongScarlet.isDead())
					targets.add(FrintezzaManager.strongScarlet);
				for(int i = 0; i < 4; ++i)
				{
					if(FrintezzaManager.portraits[i] != null && !FrintezzaManager.portraits[i].isDead())
						targets.add(FrintezzaManager.portraits[i]);
					if(FrintezzaManager.demons[i] != null && !FrintezzaManager.demons[i].isDead())
						targets.add(FrintezzaManager.demons[i]);
				}
			}
			else
				for(final Player pc : getPlayersInside())
					if(!pc.isDead())
						targets.add(pc);
			return targets;
		}

		private int getSong()
		{
			if(minionsNeedHeal())
				return 1;
			return Rnd.get(2, 6);
		}

		private boolean minionsNeedHeal()
		{
			if(Rnd.get(100) > 40)
				return false;
			if(weakScarlet != null && !weakScarlet.isAlikeDead() && weakScarlet.getCurrentHp() < weakScarlet.getMaxHp() * 2 / 3)
				return true;
			if(FrintezzaManager.strongScarlet != null && !FrintezzaManager.strongScarlet.isAlikeDead() && FrintezzaManager.strongScarlet.getCurrentHp() < FrintezzaManager.strongScarlet.getMaxHp() * 2 / 3)
				return true;
			for(int i = 0; i < 4; ++i)
			{
				if(FrintezzaManager.portraits[i] != null && !FrintezzaManager.portraits[i].isDead() && FrintezzaManager.portraits[i].getCurrentHp() < FrintezzaManager.portraits[i].getMaxHp() / 3)
					return true;
				if(FrintezzaManager.demons[i] != null && !FrintezzaManager.demons[i].isDead() && FrintezzaManager.demons[i].getCurrentHp() < FrintezzaManager.demons[i].getMaxHp() / 3)
					return true;
			}
			return false;
		}
	}

	private static class SongEffectLaunched implements Runnable
	{
		private final Set<Creature> _targets;
		private final int _song;
		private final int _currentTime;

		public SongEffectLaunched(final Set<Creature> targets, final int song, final int currentTimeOfSong)
		{
			_targets = targets;
			_song = song;
			_currentTime = currentTimeOfSong;
		}

		@Override
		public void run()
		{
			if(FrintezzaManager.frintezza == null || FrintezzaManager.frintezza.isBlocked())
				return;
			if(_currentTime > FrintezzaManager._intervalOfFrintezzaSongs)
				return;
			final SongEffectLaunched songLaunched = new SongEffectLaunched(_targets, _song, _currentTime + FrintezzaManager._intervalOfFrintezzaSongs / 10);
			ThreadPoolManager.getInstance().schedule(songLaunched, FrintezzaManager._intervalOfFrintezzaSongs / 10);
			FrintezzaManager.frintezza.callSkill(null, SkillTable.getInstance().getInfo(5008, _song), _targets, false);
		}
	}

	private class DeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(final Creature self, final Creature killer)
		{
			if(self == null || FrintezzaManager._state == null || FrintezzaManager._state.getState() != EpicBossState.State.ALIVE)
				return;
			if(self.isNpc() && self.getNpcId() == 29047 && FrintezzaManager._dieTask == null)
				FrintezzaManager._dieTask = ThreadPoolManager.getInstance().schedule(new Die(1), 50L);
		}
	}

	public class respawnDemons implements Runnable
	{
		@Override
		public void run()
		{
			boolean isAllDead = true;
			for(int i = 0; i < 4; ++i)
				if(FrintezzaManager.portraits[i] != null && !FrintezzaManager.portraits[i].isDead())
				{
					isAllDead = false;
					if(FrintezzaManager.demons[i] == null || FrintezzaManager.demons[i].isDead())
					{
						FrintezzaManager.demons[i] = FrintezzaManager.this.spawn(FrintezzaManager.demonSpawns[i]);
						final Creature target = getRandomPlayer();
						if(target != null)
							FrintezzaManager.demons[i].getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, target, 10000);
					}
				}
			if(!isAllDead)
				ThreadPoolManager.getInstance().schedule(new respawnDemons(), 30000L);
		}
	}

	private class ThirdMorph implements Runnable
	{
		private int _taskId;
		private int _angle;
		private Location loc;

		public ThirdMorph(final int taskId)
		{
			_taskId = 0;
			_angle = 0;
			loc = null;
			_taskId = taskId;
		}

		@Override
		public void run()
		{
			try
			{
				switch(_taskId)
				{
					case 1:
					{
						_angle = Math.abs((weakScarlet.getHeading() < 32768 ? 180 : 540) - (int) (weakScarlet.getHeading() / 182.044444444));
						for(final Player pc : getPlayersInside())
							pc.enterMovieMode();
						blockAll(true);
						FrintezzaManager.frintezza.broadcastPacket(new L2GameServerPacket[] { new MagicSkillCanceled(FrintezzaManager.frintezza.getObjectId()) });
						FrintezzaManager.frintezza.broadcastPacket(new L2GameServerPacket[] { new SocialAction(FrintezzaManager.frintezza.getObjectId(), 4) });
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(2), 100L);
						break;
					}
					case 2:
					{
						showSocialActionMovie(FrintezzaManager.frintezza, 250, 120, 15, 0, 1000, 0);
						showSocialActionMovie(FrintezzaManager.frintezza, 250, 120, 15, 0, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(3), 6500L);
						break;
					}
					case 3:
					{
						FrintezzaManager.frintezza.broadcastPacket(new L2GameServerPacket[] {
								new MagicSkillUse(FrintezzaManager.frintezza, FrintezzaManager.frintezza, 5006, 1, 34000, 0L) });
						showSocialActionMovie(FrintezzaManager.frintezza, 500, 70, 15, 3000, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(4), 3000L);
						break;
					}
					case 4:
					{
						showSocialActionMovie(FrintezzaManager.frintezza, 2500, 90, 12, 6000, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(5), 3000L);
						break;
					}
					case 5:
					{
						showSocialActionMovie(weakScarlet, 250, _angle, 12, 0, 1000, 0);
						showSocialActionMovie(weakScarlet, 250, _angle, 12, 0, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(6), 500L);
						break;
					}
					case 6:
					{
						weakScarlet.doDie(weakScarlet);
						showSocialActionMovie(weakScarlet, 450, _angle, 14, 8000, 8000, 0);
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(7), 6250L);
						break;
					}
					case 7:
					{
						loc = weakScarlet.getLoc();
						weakScarlet.deleteMe();
						weakScarlet = null;
						loc.setId(29047);
						FrintezzaManager.strongScarlet = FrintezzaManager.this.spawn(loc);
						FrintezzaManager.strongScarlet.addListener(_deathListener);
						block(FrintezzaManager.strongScarlet, true);
						showSocialActionMovie(FrintezzaManager.strongScarlet, 450, _angle, 12, 500, 14000, 2);
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(9), 5000L);
						break;
					}
					case 9:
					{
						blockAll(false);
						for(final Player pc : getPlayersInside())
							pc.leaveMovieMode();
						final Skill skill = SkillTable.getInstance().getInfo(5017, 1);
						skill.getEffects(FrintezzaManager.strongScarlet, FrintezzaManager.strongScarlet, false, false);
						break;
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private class SecondMorph implements Runnable
	{
		private int _taskId;

		public SecondMorph(final int taskId)
		{
			_taskId = 0;
			_taskId = taskId;
		}

		@Override
		public void run()
		{
			try
			{
				switch(_taskId)
				{
					case 1:
					{
						final int angle = Math.abs((weakScarlet.getHeading() < 32768 ? 180 : 540) - (int) (weakScarlet.getHeading() / 182.044444444));
						for(final Player pc : getPlayersInside())
							pc.enterMovieMode();
						blockAll(true);
						showSocialActionMovie(weakScarlet, 500, angle, 5, 500, 15000, 0);
						ThreadPoolManager.getInstance().schedule(new SecondMorph(2), 2000L);
						break;
					}
					case 2:
					{
						weakScarlet.broadcastPacket(new L2GameServerPacket[] { new SocialAction(weakScarlet.getObjectId(), 1) });
						weakScarlet.setCurrentHp(weakScarlet.getMaxHp() * 3 / 4, false);
						weakScarlet.setRHandId(7903);
						for(final Player pc : getPlayersInside())
							pc.sendPacket(new NpcInfo(weakScarlet, pc));
						ThreadPoolManager.getInstance().schedule(new SecondMorph(3), 5500L);
						break;
					}
					case 3:
					{
						weakScarlet.broadcastPacket(new L2GameServerPacket[] { new SocialAction(weakScarlet.getObjectId(), 4) });
						blockAll(false);
						final Skill skill = SkillTable.getInstance().getInfo(5017, 1);
						skill.getEffects(weakScarlet, weakScarlet, false, false);
						for(final Player pc2 : getPlayersInside())
							pc2.leaveMovieMode();
						break;
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private class Die implements Runnable
	{
		private int _taskId;

		public Die(final int taskId)
		{
			_taskId = 0;
			_taskId = taskId;
		}

		@Override
		public void run()
		{
			try
			{
				switch(_taskId)
				{
					case 1:
					{
						blockAll(true);
						deletePortrait();
						final int _angle = Math.abs((FrintezzaManager.strongScarlet.getHeading() < 32768 ? 180 : 540) - (int) (FrintezzaManager.strongScarlet.getHeading() / 182.044444444));
						showSocialActionMovie(FrintezzaManager.strongScarlet, 300, _angle - 180, 5, 0, 7000, 0);
						showSocialActionMovie(FrintezzaManager.strongScarlet, 200, _angle, 85, 4000, 10000, 0);
						FrintezzaManager._dieTask = ThreadPoolManager.getInstance().schedule(new Die(2), 7500L);
						break;
					}
					case 2:
					{
						showSocialActionMovie(FrintezzaManager.frintezza, 100, 120, 5, 0, 7000, 0);
						showSocialActionMovie(FrintezzaManager.frintezza, 100, 90, 5, 5000, 15000, 0);
						FrintezzaManager._dieTask = ThreadPoolManager.getInstance().schedule(new Die(3), 6000L);
						break;
					}
					case 3:
					{
						showSocialActionMovie(FrintezzaManager.frintezza, 900, 90, 25, 7000, 10000, 0);
						FrintezzaManager.frintezza.doDie(FrintezzaManager.frintezza);
						FrintezzaManager.frintezza = null;
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
							public void run()
							{
								FrintezzaManager.cube = FrintezzaManager.this.spawn(FrintezzaManager.cubeSpawn);
							}
						}, 20000L);
						FrintezzaManager._dieTask = ThreadPoolManager.getInstance().schedule(new Die(4), 7000L);
						break;
					}
					case 4:
					{
						for(final Player pc : getPlayersInside())
							pc.leaveMovieMode();
						FrintezzaManager._dieTask = ThreadPoolManager.getInstance().schedule(new Die(5), 600000L);
						break;
					}
					case 5:
					{
						setUnspawn(true);
						break;
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public class ActivityTimeEnd implements Runnable
	{
		@Override
		public void run()
		{
			if(FrintezzaManager._dieTask == null)
				setUnspawn(false);
		}
	}

	public static class IntervalEnd implements Runnable
	{
		@Override
		public void run()
		{
			FrintezzaManager._state.setState(EpicBossState.State.NOTSPAWN);
			FrintezzaManager._state.update();
		}
	}
}
