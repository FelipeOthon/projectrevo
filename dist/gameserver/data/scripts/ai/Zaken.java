package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.GameTimeController;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.listener.ZoneEnterLeaveListener;
import l2s.gameserver.listener.game.OnDayNightChangeListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.World;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.PlaySound;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Location;

public class Zaken extends DefaultAI
{
	private static final int doll_blader_b = 29023;
	private static final int vale_master_b = 29024;
	private static final int pirates_zombie_captain_b = 29026;
	private static final int pirates_zombie_b = 29027;
	private static final Location[] locations;
	private List<NpcInstance> spawns;
	private static final Zone _zone;
	private ZoneListener _zoneListener;
	private NightInvulDayNightListener _timeListener;
	private final double _baseHpReg;
	private final double _baseMpReg;
	private boolean _isInLightRoom;
	private int _stage;
	private int _stage2;
	private final int ScatterEnemy = 4216;
	private final int MassTeleport = 4217;
	private final int InstantMove = 4222;
	private final int FaceChanceNightToDay = 4223;
	private final int FaceChanceDayToNight = 4224;
	private final Skill AbsorbHPMP;
	private final Skill Hold;
	private final Skill DeadlyDualSwordWeapon;
	private final Skill DeadlyDualSwordWeaponRangeAttack;
	private long _lastTP;

	public Zaken(final NpcInstance actor)
	{
		super(actor);
		spawns = new ArrayList<NpcInstance>();
		_zoneListener = new ZoneListener();
		_timeListener = new NightInvulDayNightListener();
		_isInLightRoom = false;
		_stage = 0;
		_stage2 = 0;
		_lastTP = 0L;
		AbsorbHPMP = SkillTable.getInstance().getInfo(4218, 1);
		Hold = SkillTable.getInstance().getInfo(4219, 1);
		DeadlyDualSwordWeapon = SkillTable.getInstance().getInfo(4220, 1);
		DeadlyDualSwordWeaponRangeAttack = SkillTable.getInstance().getInfo(4221, 1);
		_baseHpReg = actor.getTemplate().baseHpReg;
		_baseMpReg = actor.getTemplate().baseMpReg;
	}

	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		final Creature target;
		if((target = prepareTarget()) == null)
			return false;
		final NpcInstance actor = getActor();
		if(actor == null || actor.isDead())
			return false;
		teleportSelf();
		spawnMinions();
		final int rnd_per = Rnd.get(100);
		final boolean can_teleport = actor.getCurrentMp() >= Config.ZAKEN_TELEPORT_MIN_MP;
		if(can_teleport && rnd_per < 2)
		{
			actor.broadcastPacketToOthers(new MagicSkillUse(actor, target, 4216, 1, 2900, 0L));
			ThreadPoolManager.getInstance().schedule(new TeleportTask(target, false), 2900L);
			return true;
		}
		if(can_teleport && rnd_per < 4)
		{
			actor.broadcastPacketToOthers(new L2GameServerPacket[] { new MagicSkillUse(actor, target, 4217, 1, 2900, 0L) });
			ThreadPoolManager.getInstance().schedule(new TeleportTask(target, false), 2900L);
			for(final Playable playable : World.getAroundPlayables(target, 200, 200))
				if(playable != target)
					ThreadPoolManager.getInstance().schedule(new TeleportTask(playable, false), 2900L);
			return true;
		}
		final double distance = actor.getDistance(target);
		if(rnd_per < 75)
			return chooseTaskAndTargets((Skill) null, target, distance);
		final Map<Skill, Integer> d_skill = new HashMap<Skill, Integer>();
		this.addDesiredSkill(d_skill, target, distance, DeadlyDualSwordWeapon);
		this.addDesiredSkill(d_skill, target, distance, DeadlyDualSwordWeaponRangeAttack);
		this.addDesiredSkill(d_skill, target, distance, Hold);
		this.addDesiredSkill(d_skill, target, distance, AbsorbHPMP);
		final Skill r_skill = selectTopSkill(d_skill);
		return chooseTaskAndTargets(r_skill, target, distance);
	}

	private void teleportSelf()
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return;
		final double actor_hp_precent = actor.getCurrentHpPercents();
		final boolean can_teleport = actor.getCurrentMp() >= Config.ZAKEN_TELEPORT_MIN_MP;
		switch(_stage2)
		{
			case 0:
			{
				if(actor_hp_precent < 85.0)
				{
					if(can_teleport)
					{
						actor.broadcastPacketToOthers(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4222, 1, 500, 0L) });
						ThreadPoolManager.getInstance().schedule(new TeleportTask(actor, true), 500L);
					}
					++_stage2;
					break;
				}
				break;
			}
			case 1:
			{
				if(actor_hp_precent < 50.0)
				{
					if(can_teleport)
					{
						actor.broadcastPacketToOthers(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4222, 1, 500, 0L) });
						ThreadPoolManager.getInstance().schedule(new TeleportTask(actor, true), 500L);
					}
					++_stage2;
					break;
				}
				break;
			}
			case 2:
			{
				if(actor_hp_precent < 25.0)
				{
					if(can_teleport)
					{
						actor.broadcastPacketToOthers(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4222, 1, 500, 0L) });
						ThreadPoolManager.getInstance().schedule(new TeleportTask(actor, true), 500L);
					}
					++_stage2;
					break;
				}
				break;
			}
		}
	}

	private void spawnMinions()
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return;
		final double actor_hp_precent = actor.getCurrentHpPercents();
		switch(_stage)
		{
			case 0:
			{
				if(actor_hp_precent < 90.0)
				{
					addSpawn(29026, getRndLoc());
					++_stage;
					break;
				}
				break;
			}
			case 1:
			{
				if(actor_hp_precent < 80.0)
				{
					addSpawn(29023, getRndLoc());
					++_stage;
					break;
				}
				break;
			}
			case 2:
			{
				if(actor_hp_precent < 70.0)
				{
					addSpawn(29024, getRndLoc());
					addSpawn(29024, getRndLoc());
					++_stage;
					break;
				}
				break;
			}
			case 3:
			{
				if(actor_hp_precent < 60.0)
				{
					addSpawn(29027, getRndLoc());
					addSpawn(29027, getRndLoc());
					addSpawn(29027, getRndLoc());
					addSpawn(29027, getRndLoc());
					addSpawn(29027, getRndLoc());
					++_stage;
					break;
				}
				break;
			}
			case 4:
			{
				if(actor_hp_precent < 50.0)
				{
					addSpawn(29023, new Location(52675, 219371, -3290, Rnd.get(65536)));
					addSpawn(29023, new Location(52687, 219596, -3368, Rnd.get(65536)));
					addSpawn(29023, new Location(52672, 219740, -3418, Rnd.get(65536)));
					addSpawn(29027, new Location(52857, 219992, -3488, Rnd.get(65536)));
					addSpawn(29026, new Location(52959, 219997, -3488, Rnd.get(65536)));
					addSpawn(29024, new Location(53381, 220151, -3488, Rnd.get(65536)));
					addSpawn(29026, new Location(54236, 220948, -3488, Rnd.get(65536)));
					addSpawn(29027, new Location(54885, 220144, -3488, Rnd.get(65536)));
					addSpawn(29027, new Location(55264, 219860, -3488, Rnd.get(65536)));
					addSpawn(29026, new Location(55399, 220263, -3488, Rnd.get(65536)));
					addSpawn(29027, new Location(55679, 220129, -3488, Rnd.get(65536)));
					addSpawn(29024, new Location(56276, 220783, -3488, Rnd.get(65536)));
					addSpawn(29024, new Location(57173, 220234, -3488, Rnd.get(65536)));
					addSpawn(29027, new Location(56267, 218826, -3488, Rnd.get(65536)));
					addSpawn(29023, new Location(56294, 219482, -3488, Rnd.get(65536)));
					addSpawn(29026, new Location(56094, 219113, -3488, Rnd.get(65536)));
					addSpawn(29023, new Location(56364, 218967, -3488, Rnd.get(65536)));
					addSpawn(29027, new Location(57113, 218079, -3488, Rnd.get(65536)));
					addSpawn(29023, new Location(56186, 217153, -3488, Rnd.get(65536)));
					addSpawn(29027, new Location(55440, 218081, -3488, Rnd.get(65536)));
					addSpawn(29026, new Location(55202, 217940, -3488, Rnd.get(65536)));
					addSpawn(29027, new Location(55225, 218236, -3488, Rnd.get(65536)));
					addSpawn(29027, new Location(54973, 218075, -3488, Rnd.get(65536)));
					addSpawn(29026, new Location(53412, 218077, -3488, Rnd.get(65536)));
					addSpawn(29024, new Location(54226, 218797, -3488, Rnd.get(65536)));
					addSpawn(29024, new Location(54394, 219067, -3488, Rnd.get(65536)));
					addSpawn(29027, new Location(54139, 219253, -3488, Rnd.get(65536)));
					addSpawn(29023, new Location(54262, 219480, -3488, Rnd.get(65536)));
					++_stage;
					break;
				}
				break;
			}
			case 5:
			{
				if(actor_hp_precent < 40.0)
				{
					addSpawn(29027, new Location(53412, 218077, -3488, Rnd.get(65536)));
					addSpawn(29024, new Location(54413, 217132, -3488, Rnd.get(65536)));
					addSpawn(29023, new Location(54841, 217132, -3488, Rnd.get(65536)));
					addSpawn(29023, new Location(55372, 217128, -3343, Rnd.get(65536)));
					addSpawn(29023, new Location(55893, 217122, -3488, Rnd.get(65536)));
					addSpawn(29026, new Location(56282, 217237, -3216, Rnd.get(65536)));
					addSpawn(29024, new Location(56963, 218080, -3216, Rnd.get(65536)));
					addSpawn(29027, new Location(56267, 218826, -3216, Rnd.get(65536)));
					addSpawn(29023, new Location(56294, 219482, -3216, Rnd.get(65536)));
					addSpawn(29026, new Location(56094, 219113, -3216, Rnd.get(65536)));
					addSpawn(29023, new Location(56364, 218967, -3216, Rnd.get(65536)));
					addSpawn(29024, new Location(56276, 220783, -3216, Rnd.get(65536)));
					addSpawn(29024, new Location(57173, 220234, -3216, Rnd.get(65536)));
					addSpawn(29027, new Location(54885, 220144, -3216, Rnd.get(65536)));
					addSpawn(29027, new Location(55264, 219860, -3216, Rnd.get(65536)));
					addSpawn(29026, new Location(55399, 220263, -3216, Rnd.get(65536)));
					addSpawn(29027, new Location(55679, 220129, -3216, Rnd.get(65536)));
					addSpawn(29026, new Location(54236, 220948, -3216, Rnd.get(65536)));
					addSpawn(29026, new Location(54464, 219095, -3216, Rnd.get(65536)));
					addSpawn(29024, new Location(54226, 218797, -3216, Rnd.get(65536)));
					addSpawn(29024, new Location(54394, 219067, -3216, Rnd.get(65536)));
					addSpawn(29027, new Location(54139, 219253, -3216, Rnd.get(65536)));
					addSpawn(29023, new Location(54262, 219480, -3216, Rnd.get(65536)));
					addSpawn(29026, new Location(53412, 218077, -3216, Rnd.get(65536)));
					addSpawn(29027, new Location(55440, 218081, -3216, Rnd.get(65536)));
					addSpawn(29026, new Location(55202, 217940, -3216, Rnd.get(65536)));
					addSpawn(29027, new Location(55225, 218236, -3216, Rnd.get(65536)));
					addSpawn(29027, new Location(54973, 218075, -3216, Rnd.get(65536)));
					++_stage;
					break;
				}
				break;
			}
			case 6:
			{
				if(actor_hp_precent < 30.0)
				{
					addSpawn(29027, new Location(54228, 217504, -3216, Rnd.get(65536)));
					addSpawn(29024, new Location(54181, 217168, -3216, Rnd.get(65536)));
					addSpawn(29023, new Location(54714, 217123, -3168, Rnd.get(65536)));
					addSpawn(29023, new Location(55298, 217127, -3073, Rnd.get(65536)));
					addSpawn(29023, new Location(55787, 217130, -2993, Rnd.get(65536)));
					addSpawn(29026, new Location(56284, 217216, -2944, Rnd.get(65536)));
					addSpawn(29024, new Location(56963, 218080, -2944, Rnd.get(65536)));
					addSpawn(29027, new Location(56267, 218826, -2944, Rnd.get(65536)));
					addSpawn(29023, new Location(56294, 219482, -2944, Rnd.get(65536)));
					addSpawn(29026, new Location(56094, 219113, -2944, Rnd.get(65536)));
					addSpawn(29023, new Location(56364, 218967, -2944, Rnd.get(65536)));
					addSpawn(29024, new Location(56276, 220783, -2944, Rnd.get(65536)));
					addSpawn(29024, new Location(57173, 220234, -2944, Rnd.get(65536)));
					addSpawn(29027, new Location(54885, 220144, -2944, Rnd.get(65536)));
					addSpawn(29027, new Location(55264, 219860, -2944, Rnd.get(65536)));
					addSpawn(29026, new Location(55399, 220263, -2944, Rnd.get(65536)));
					addSpawn(29027, new Location(55679, 220129, -2944, Rnd.get(65536)));
					addSpawn(29026, new Location(54236, 220948, -2944, Rnd.get(65536)));
					addSpawn(29026, new Location(54464, 219095, -2944, Rnd.get(65536)));
					addSpawn(29024, new Location(54226, 218797, -2944, Rnd.get(65536)));
					addSpawn(29024, new Location(54394, 219067, -2944, Rnd.get(65536)));
					addSpawn(29027, new Location(54139, 219253, -2944, Rnd.get(65536)));
					addSpawn(29023, new Location(54262, 219480, -2944, Rnd.get(65536)));
					addSpawn(29026, new Location(53412, 218077, -2944, Rnd.get(65536)));
					addSpawn(29026, new Location(54280, 217200, -2944, Rnd.get(65536)));
					addSpawn(29027, new Location(55440, 218081, -2944, Rnd.get(65536)));
					addSpawn(29026, new Location(55202, 217940, -2944, Rnd.get(65536)));
					addSpawn(29027, new Location(55225, 218236, -2944, Rnd.get(65536)));
					addSpawn(29027, new Location(54973, 218075, -2944, Rnd.get(65536)));
					++_stage;
					break;
				}
				break;
			}
		}
	}

	private Location getRndLoc()
	{
		return Zaken.locations[Rnd.get(Zaken.locations.length)].rnd(0, 500, false).setH(Rnd.get(65536));
	}

	private void addSpawn(final int id, final Location loc)
	{
		final NpcInstance actor = getActor();
		if(actor != null)
			try
			{
				final Spawn sp = new Spawn(NpcTable.getTemplate(id));
				sp.setLoc(loc);
				spawns.add(sp.doSpawn(true));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		final NpcInstance actor = getActor();
		if(actor != null)
			actor.broadcastPacket(new L2GameServerPacket[] { new PlaySound(1, "BS02_D", 1, actor.getObjectId(), actor.getLoc()) });
		super.onEvtDead(killer);
	}

	@Override
	public synchronized void startAITask()
	{
		if(_aiTask == null)
		{
			spawns.clear();
			GameTimeController.getInstance().addListener(_timeListener);
			Zaken._zone.getListenerEngine().addMethodInvokedListener(_zoneListener);
			_stage = 0;
			_stage2 = 0;
		}
		super.startAITask();
	}

	@Override
	public synchronized void stopAITask()
	{
		if(_aiTask != null)
		{
			spawns.clear();
			GameTimeController.getInstance().removeListener(_timeListener);
			Zaken._zone.getListenerEngine().removeMethodInvokedListener(_zoneListener);
		}
		super.stopAITask();
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		if(Config.ZAKEN_CLEAR_ZONE)
		{
			Zone zone = ZoneManager.getInstance().getZoneById(Zone.ZoneType.no_restart, 335, false);
			for(final Player player : zone.getInsidePlayers())
				player.teleToClosestTown();
			zone = ZoneManager.getInstance().getZoneById(Zone.ZoneType.no_restart, 1335, false);
			for(final Player player : zone.getInsidePlayers())
				if(!player.isTeleporting())
					player.teleToClosestTown();
		}
	}

	static
	{
		locations = new Location[] {
				new Location(55272, 219112, -3496),
				new Location(56296, 218072, -3496),
				new Location(54232, 218072, -3496),
				new Location(54248, 220136, -3496),
				new Location(56296, 220136, -3496),
				new Location(55272, 219112, -3224),
				new Location(56296, 218072, -3224),
				new Location(54232, 218072, -3224),
				new Location(54248, 220136, -3224),
				new Location(56296, 220136, -3224),
				new Location(55272, 219112, -2952),
				new Location(56296, 218072, -2952),
				new Location(54232, 218072, -2952),
				new Location(54248, 220136, -2952),
				new Location(56296, 220136, -2952) };
		_zone = ZoneManager.getInstance().getZoneById(Zone.ZoneType.no_restart, 1335, true);
	}

	private class TeleportTask implements Runnable
	{
		Creature _target;
		boolean _self;

		public TeleportTask(final Creature target, final boolean self)
		{
			_target = target;
			_self = self;
		}

		@Override
		public void run()
		{
			if(_target != null && (System.currentTimeMillis() - _lastTP >= 10000L || _self))
			{
				final NpcInstance actor = getActor();
				if(actor != null && !actor.isDead())
				{
					_lastTP = System.currentTimeMillis();
					_target.teleToLocation(getRndLoc());
					if(!_self)
						actor.getAggroList().remove(_target, false);
					else
						actor.getAggroList().clear(true);
				}
			}
			_target = null;
		}
	}

	private class NightInvulDayNightListener implements OnDayNightChangeListener
	{
		private NightInvulDayNightListener()
		{
			if(GameTimeController.getInstance().isNowNight())
				onNight();
			else
				onDay();
		}

		@Override
		public void onNight()
		{
			final NpcInstance actor = getActor();
			if(actor != null)
			{
				if(_isInLightRoom)
				{
					actor.getTemplate().baseHpReg = _baseHpReg * 15.0;
					actor.getTemplate().baseMpReg = _baseMpReg * 15.0;
				}
				else
				{
					actor.getTemplate().baseHpReg = _baseHpReg * 20.0;
					actor.getTemplate().baseMpReg = _baseMpReg * 20.0;
				}
				actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4224, 1, 1100, 0L) });
			}
		}

		@Override
		public void onDay()
		{
			final NpcInstance actor = getActor();
			if(actor != null)
			{
				actor.getTemplate().baseHpReg = _baseHpReg;
				actor.getTemplate().baseMpReg = _baseMpReg;
				actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4223, 1, 1100, 0L) });
			}
		}
	}

	private class ZoneListener extends ZoneEnterLeaveListener
	{
		@Override
		public void objectEntered(final Zone zone, final GameObject object)
		{
			final NpcInstance actor = getActor();
			if(actor == null)
				return;
			actor.getTemplate().baseHpReg = _baseHpReg * 0.75;
			actor.getTemplate().baseMpReg = _baseMpReg * 0.75;
			_isInLightRoom = true;
		}

		@Override
		public void objectLeaved(final Zone zone, final GameObject object)
		{
			final NpcInstance actor = getActor();
			if(actor == null)
				return;
			actor.getTemplate().baseHpReg = _baseHpReg;
			actor.getTemplate().baseMpReg = _baseMpReg;
			_isInLightRoom = false;
		}
	}
}
