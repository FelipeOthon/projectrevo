package bosses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.SocialAction;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.Log;

public class SailrenManager extends Functions implements ScriptFile
{
	private static final Logger _log;
	private static NpcInstance _velociraptor;
	private static NpcInstance _pterosaur;
	private static NpcInstance _tyranno;
	private static NpcInstance _sailren;
	private static NpcInstance _teleportCube;
	private static ScheduledFuture<?> _cubeSpawnTask;
	private static ScheduledFuture<?> _monsterSpawnTask;
	private static ScheduledFuture<?> _intervalEndTask;
	private static ScheduledFuture<?> _socialTask;
	private static ScheduledFuture<?> _activityTimeEndTask;
	private static ScheduledFuture<?> _onAnnihilatedTask;
	private static final int Sailren = 29065;
	private static final int Velociraptor = 22198;
	private static final int Pterosaur = 22199;
	private static final int Tyrannosaurus = 22217;
	private static final int TeleportCubeId = 32107;
	private static EpicBossState _state;
	private static Zone _zone;
	private static final boolean FWS_ENABLESINGLEPLAYER;
	private static final int FWS_INTERVALOFNEXTMONSTER = 60000;
	private static boolean _isAlreadyEnteredOtherParty;
	private static boolean Dying;

	private static void banishForeigners()
	{
		for(final Player player : getPlayersInside())
			player.teleToClosestTown();
	}

	private static synchronized void checkAnnihilated()
	{
		if(SailrenManager._onAnnihilatedTask == null && isPlayersAnnihilated())
			SailrenManager._onAnnihilatedTask = ThreadPoolManager.getInstance().schedule(new onAnnihilated(), 5000L);
	}

	private static List<Player> getPlayersInside()
	{
		return SailrenManager._zone.getInsidePlayers();
	}

	public static Zone getZone()
	{
		return SailrenManager._zone;
	}

	private static void init()
	{
		SailrenManager._state = new EpicBossState(29065);
		SailrenManager._zone = ZoneManager.getInstance().getZoneById(Zone.ZoneType.epic, 702004, false);
		SailrenManager._log.info("SailrenManager: State of Sailren is " + SailrenManager._state.getState() + ".");
		if(SailrenManager._state.getAliveId() > 0)
		{
			(SailrenManager._sailren = spawn(SailrenManager._state.getLoc(), 29065)).setCurrentHp(SailrenManager._state.getCurrentHp(), true);
			SailrenManager._sailren.setCurrentMp(SailrenManager._state.getCurrentMp());
			SailrenManager._activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), Config.SAILREN_ACTIVITY_TIME);
		}
		else if(!SailrenManager._state.getState().equals(EpicBossState.State.NOTSPAWN))
			setIntervalEndTask();
		final Date dt = new Date(SailrenManager._state.getRespawnDate());
		SailrenManager._log.info("SailrenManager: Next spawn date of Sailren is " + dt + ".");
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
		if(Config.SAILREN_CHECK_ANNIHILATED && self.isPlayer() && SailrenManager._state != null && SailrenManager._state.getState() == EpicBossState.State.ALIVE && SailrenManager._zone != null && SailrenManager._zone.checkIfInZone(self.getX(), self.getY()))
			checkAnnihilated();
		else if(self == SailrenManager._velociraptor)
		{
			if(SailrenManager._monsterSpawnTask != null)
				SailrenManager._monsterSpawnTask.cancel(false);
			SailrenManager._monsterSpawnTask = ThreadPoolManager.getInstance().schedule(new SailrenSpawn(22199), 60000L);
		}
		else if(self == SailrenManager._pterosaur)
		{
			if(SailrenManager._monsterSpawnTask != null)
				SailrenManager._monsterSpawnTask.cancel(false);
			SailrenManager._monsterSpawnTask = ThreadPoolManager.getInstance().schedule(new SailrenSpawn(22217), 60000L);
		}
		else if(self == SailrenManager._tyranno)
		{
			if(SailrenManager._monsterSpawnTask != null)
				SailrenManager._monsterSpawnTask.cancel(false);
			SailrenManager._monsterSpawnTask = ThreadPoolManager.getInstance().schedule(new SailrenSpawn(29065), 60000L);
		}
		else if(self == SailrenManager._sailren)
			onSailrenDie(killer);
	}

	private static void onSailrenDie(final Creature killer)
	{
		if(SailrenManager.Dying)
			return;
		SailrenManager.Dying = true;
		SailrenManager._state.setRespawnDate(Config.FWS_FIXINTERVALOFSAILRENSPAWN, Config.FWS_RANDOMINTERVALOFSAILRENSPAWN, Config.SAILREN_FIXRESP);
		SailrenManager._state.setState(EpicBossState.State.INTERVAL);
		SailrenManager._state.update();
		SailrenManager._isAlreadyEnteredOtherParty = false;
		Log.addLog("Sailren died", "bosses");
		SailrenManager._cubeSpawnTask = ThreadPoolManager.getInstance().schedule(new CubeSpawn(), 20000L);
	}

	private static void setIntervalEndTask()
	{
		setUnspawn();
		if(SailrenManager._state.getState().equals(EpicBossState.State.ALIVE))
		{
			SailrenManager._state.setState(EpicBossState.State.NOTSPAWN);
			SailrenManager._state.update();
			return;
		}
		if(!SailrenManager._state.getState().equals(EpicBossState.State.INTERVAL))
		{
			SailrenManager._state.setRespawnDate(Config.FWS_FIXINTERVALOFSAILRENSPAWN, Config.FWS_RANDOMINTERVALOFSAILRENSPAWN, Config.SAILREN_FIXRESP);
			SailrenManager._state.setState(EpicBossState.State.INTERVAL);
			SailrenManager._state.update();
		}
		SailrenManager._intervalEndTask = ThreadPoolManager.getInstance().schedule(new IntervalEnd(), SailrenManager._state.getInterval());
	}

	private static void setUnspawn()
	{
		banishForeigners();
		if(SailrenManager._velociraptor != null)
		{
			if (SailrenManager._velociraptor.getSpawn() != null)
			{
				SailrenManager._velociraptor.getSpawn().stopRespawn();
				SailrenManager._velociraptor.deleteMe();
				SailrenManager._velociraptor = null;
			}
		}
		if(SailrenManager._pterosaur != null)
		{
			SailrenManager._pterosaur.getSpawn().stopRespawn();
			SailrenManager._pterosaur.deleteMe();
			SailrenManager._pterosaur = null;
		}
		if(SailrenManager._tyranno != null)
		{
			SailrenManager._tyranno.getSpawn().stopRespawn();
			SailrenManager._tyranno.deleteMe();
			SailrenManager._tyranno = null;
		}
		if(SailrenManager._sailren != null)
		{
			SailrenManager._sailren.getSpawn().stopRespawn();
			SailrenManager._sailren.deleteMe();
			SailrenManager._sailren = null;
		}
		if(SailrenManager._teleportCube != null)
		{
			SailrenManager._teleportCube.getSpawn().stopRespawn();
			SailrenManager._teleportCube.deleteMe();
			SailrenManager._teleportCube = null;
		}
		if(SailrenManager._cubeSpawnTask != null)
		{
			SailrenManager._cubeSpawnTask.cancel(false);
			SailrenManager._cubeSpawnTask = null;
		}
		if(SailrenManager._monsterSpawnTask != null)
		{
			SailrenManager._monsterSpawnTask.cancel(false);
			SailrenManager._monsterSpawnTask = null;
		}
		if(SailrenManager._intervalEndTask != null)
		{
			SailrenManager._intervalEndTask.cancel(false);
			SailrenManager._intervalEndTask = null;
		}
		if(SailrenManager._socialTask != null)
		{
			SailrenManager._socialTask.cancel(false);
			SailrenManager._socialTask = null;
		}
		if(SailrenManager._activityTimeEndTask != null)
		{
			SailrenManager._activityTimeEndTask.cancel(true);
			SailrenManager._activityTimeEndTask = null;
		}
		if(SailrenManager._onAnnihilatedTask != null)
		{
			SailrenManager._onAnnihilatedTask.cancel(false);
			SailrenManager._onAnnihilatedTask = null;
		}
	}

	private static void sleep()
	{
		setUnspawn();
		if(SailrenManager._state.getState().equals(EpicBossState.State.ALIVE))
		{
			SailrenManager._state.setState(EpicBossState.State.NOTSPAWN);
			SailrenManager._state.update();
		}
	}

	public static synchronized void setSailrenSpawnTask()
	{
		if(SailrenManager._monsterSpawnTask == null)
			SailrenManager._monsterSpawnTask = ThreadPoolManager.getInstance().schedule(new SailrenSpawn(22198), 60000L);
	}

	public static boolean isEnableEnterToLair()
	{
		return SailrenManager._state.getState() == EpicBossState.State.NOTSPAWN;
	}

	public static int canIntoSailrenLair(final Player pc)
	{
		if(!SailrenManager.FWS_ENABLESINGLEPLAYER && pc.getParty() == null)
			return 4;
		if(SailrenManager._isAlreadyEnteredOtherParty || SailrenManager._state.getState().equals(EpicBossState.State.ALIVE))
			return 2;
		if(SailrenManager._state.getState().equals(EpicBossState.State.INTERVAL) || SailrenManager._state.getState().equals(EpicBossState.State.DEAD))
			return 3;
		return 1;
	}

	public static void entryToSailrenLair(final Player pc)
	{
		SailrenManager._isAlreadyEnteredOtherParty = true;
		if(pc.getParty() == null)
			pc.teleToLocation(new Location(27734, -6938, -1982).rnd(0, 80, true));
		else
		{
			final List<Player> members = new ArrayList<Player>();
			for(final Player mem : pc.getParty().getPartyMembers())
				if(mem != null && !mem.isDead() && mem.isInRange(pc, 1000L))
					members.add(mem);
			for(final Player mem : members)
				mem.teleToLocation(new Location(27734, -6938, -1982).rnd(0, 80, true));
		}
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
		if(SailrenManager._state.getState() == EpicBossState.State.ALIVE && SailrenManager._sailren != null && !SailrenManager._sailren.isDead())
			SailrenManager._state.aliveSave(29065, SailrenManager._sailren.getCurrentHp(), SailrenManager._sailren.getCurrentMp(), SailrenManager._sailren.getLoc());
	}

	static
	{
		_log = LoggerFactory.getLogger(SailrenManager.class);
		SailrenManager._cubeSpawnTask = null;
		SailrenManager._monsterSpawnTask = null;
		SailrenManager._intervalEndTask = null;
		SailrenManager._socialTask = null;
		SailrenManager._activityTimeEndTask = null;
		SailrenManager._onAnnihilatedTask = null;
		FWS_ENABLESINGLEPLAYER = Boolean.FALSE;
		SailrenManager._isAlreadyEnteredOtherParty = false;
		SailrenManager.Dying = false;
	}

	public static class ActivityTimeEnd implements Runnable
	{
		@Override
		public void run()
		{
			sleep();
		}
	}

	public static class CubeSpawn implements Runnable
	{
		@Override
		public void run()
		{
			SailrenManager._teleportCube = new NpcInstance(IdFactory.getInstance().getNextId(), NpcTable.getTemplate(32107));
			SailrenManager._teleportCube.setCurrentHpMp(SailrenManager._teleportCube.getMaxHp(), SailrenManager._teleportCube.getMaxMp(), true);
			SailrenManager._teleportCube.setSpawnedLoc(new Location(27734, -6838, -1982, 0));
			SailrenManager._teleportCube.spawnMe(SailrenManager._teleportCube.getSpawnedLoc());
			SailrenManager.Dying = false;
		}
	}

	public static class IntervalEnd implements Runnable
	{
		@Override
		public void run()
		{
			SailrenManager._state.setState(EpicBossState.State.NOTSPAWN);
			SailrenManager._state.update();
		}
	}

	private static class Social implements Runnable
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
			_npc.broadcastPacket(new L2GameServerPacket[] { new SocialAction(_npc.getObjectId(), _action) });
		}
	}

	public static class onAnnihilated implements Runnable
	{
		@Override
		public void run()
		{
			sleep();
			SailrenManager._isAlreadyEnteredOtherParty = false;
		}
	}

	private static class SailrenSpawn implements Runnable
	{
		private int _npcId;
		private final Location _pos;

		SailrenSpawn(final int npcId)
		{
			_pos = new Location(27628, -6109, -1982, 44732);
			_npcId = npcId;
		}

		@Override
		public void run()
		{
			if(SailrenManager._socialTask != null)
			{
				SailrenManager._socialTask.cancel(false);
				SailrenManager._socialTask = null;
			}
			switch(_npcId)
			{
				case 22198:
				{
					SailrenManager._velociraptor = Functions.spawn(new Location(27852, -5536, -1983, 44732), 22198);
					((DefaultAI) SailrenManager._velociraptor.getAI()).addTaskMove(_pos, false);
					if(SailrenManager._socialTask != null)
					{
						SailrenManager._socialTask.cancel(true);
						SailrenManager._socialTask = null;
					}
					SailrenManager._socialTask = ThreadPoolManager.getInstance().schedule(new Social(SailrenManager._velociraptor, 2), 6000L);
					if(SailrenManager._activityTimeEndTask != null)
					{
						SailrenManager._activityTimeEndTask.cancel(true);
						SailrenManager._activityTimeEndTask = null;
					}
					SailrenManager._activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), Config.SAILREN_ACTIVITY_TIME);
					break;
				}
				case 22199:
				{
					SailrenManager._pterosaur = Functions.spawn(new Location(27852, -5536, -1983, 44732), 22199);
					((DefaultAI) SailrenManager._pterosaur.getAI()).addTaskMove(_pos, false);
					if(SailrenManager._socialTask != null)
					{
						SailrenManager._socialTask.cancel(true);
						SailrenManager._socialTask = null;
					}
					SailrenManager._socialTask = ThreadPoolManager.getInstance().schedule(new Social(SailrenManager._pterosaur, 2), 6000L);
					if(SailrenManager._activityTimeEndTask != null)
					{
						SailrenManager._activityTimeEndTask.cancel(true);
						SailrenManager._activityTimeEndTask = null;
					}
					SailrenManager._activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), Config.SAILREN_ACTIVITY_TIME);
					break;
				}
				case 22217:
				{
					SailrenManager._tyranno = Functions.spawn(new Location(27852, -5536, -1983, 44732), 22217);
					((DefaultAI) SailrenManager._tyranno.getAI()).addTaskMove(_pos, false);
					if(SailrenManager._socialTask != null)
					{
						SailrenManager._socialTask.cancel(true);
						SailrenManager._socialTask = null;
					}
					SailrenManager._socialTask = ThreadPoolManager.getInstance().schedule(new Social(SailrenManager._tyranno, 2), 6000L);
					if(SailrenManager._activityTimeEndTask != null)
					{
						SailrenManager._activityTimeEndTask.cancel(true);
						SailrenManager._activityTimeEndTask = null;
					}
					SailrenManager._activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), Config.SAILREN_ACTIVITY_TIME);
					break;
				}
				case 29065:
				{
					SailrenManager._sailren = Functions.spawn(new Location(27810, -5655, -1983, 44732), 29065);
					SailrenManager._state.setRespawnDate(Config.FWS_FIXINTERVALOFSAILRENSPAWN + Config.SAILREN_ACTIVITY_TIME, Config.FWS_RANDOMINTERVALOFSAILRENSPAWN, Config.SAILREN_FIXRESP);
					SailrenManager._state.setState(EpicBossState.State.ALIVE);
					SailrenManager._state.update();
					SailrenManager._sailren.setRunning();
					((DefaultAI) SailrenManager._sailren.getAI()).addTaskMove(_pos, false);
					if(SailrenManager._socialTask != null)
					{
						SailrenManager._socialTask.cancel(true);
						SailrenManager._socialTask = null;
					}
					SailrenManager._socialTask = ThreadPoolManager.getInstance().schedule(new Social(SailrenManager._sailren, 2), 6000L);
					if(SailrenManager._activityTimeEndTask != null)
					{
						SailrenManager._activityTimeEndTask.cancel(true);
						SailrenManager._activityTimeEndTask = null;
					}
					SailrenManager._activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), Config.SAILREN_ACTIVITY_TIME);
					break;
				}
			}
		}
	}
}
