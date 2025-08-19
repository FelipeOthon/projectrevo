package ai;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.PlaySound;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.DoorTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.NpcUtils;

public class AndreasVanHalter extends Fighter
{
	private boolean _firstTimeMove;
	private static ScheduledFuture<?> _movieTask;
	private static ScheduledFuture<?> _checkTask;
	private static final int _distance = 4000;
	private static final int _height = 400;
	private int _stage;
	private static List<Integer> npcs1;
	private static List<Integer> npcs2;
	private static List<Integer> altars;
	private static int Ritual_Offering;
	private static int Ritual_Sacrifice;
	private static int COUNTER;
	private static boolean _action;
	private static int TriolsRevelation1;
	private static int TriolsRevelation2;
	private static int TriolsRevelation3;
	private static int TriolsRevelation4;
	private static int TriolsRevelation5;
	private static int TriolsRevelation6;
	private static int TriolsRevelation7;
	private static int TriolsRevelation8;
	private static int TriolsRevelation9;
	private static int TriolsRevelation10;
	private static int TriolsRevelation11;
	private static int RitualOffering;
	private static int RitualSacrifice;
	private static int AltarGatekeeper;
	private static int AndreasCaptainRoyalGuard1;
	private static int AndreasCaptainRoyalGuard2;
	private static int AndreasCaptainRoyalGuard3;
	private static int AndreasRoyalGuards1;
	private static int AndreasRoyalGuards2;
	private static int AndreasRoyalGuards3;

	public AndreasVanHalter(final NpcInstance actor)
	{
		super(actor);
		_firstTimeMove = true;
		_stage = 0;
	}

	@Override
	protected void onEvtSpawn()
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return;
		clearZone();
		SpawnNpc1();
		super.onEvtSpawn();
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if(actor == null || actor.isDead())
			return true;
		if(_firstTimeMove && _action)
		{
			_firstTimeMove = false;
			final DoorInstance door1 = DoorTable.getInstance().getDoor(19160014);
			final DoorInstance door2 = DoorTable.getInstance().getDoor(19160015);
			if(door1.isOpen())
				door1.closeMe();
			if(door2.isOpen())
				door2.closeMe();
			_movieTask = ThreadPoolManager.getInstance().schedule(new Movie(1), 3000L);
			actor.broadcastPacket(new L2GameServerPacket[] { new PlaySound("BS04_A") });
			_checkTask = ThreadPoolManager.getInstance().schedule(new CheckAttack(), 3600000L);
		}
		return true;
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return;
		if(actor.getZ() < -10530)
			teleportHome();
		else if(actor.getDistance(-15363, -54862) <= 200.0)
			teleportHome();
		else if(actor.getDistance(-17424, -54862) <= 200.0)
			teleportHome();
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected boolean createNewTask()
	{
		if(_stage < 2)
		{
			final NpcInstance actor = getActor();
			if(actor != null && !actor.isDead())
			{
				final double c = actor.getCurrentHpPercents();
				if(_stage == 0 && c <= 60.0 || _stage == 1 && c <= 30.0)
				{
					SpawnNpc2();
					++_stage;
				}
			}
		}
		return super.createNewTask();
	}

	private void clearZone()
	{
		final DoorInstance door1 = DoorTable.getInstance().getDoor(19160016);
		final DoorInstance door2 = DoorTable.getInstance().getDoor(19160017);
		if(door1.isOpen())
			door1.closeMe();
		if(door2.isOpen())
			door2.closeMe();
		final NpcInstance actor = getActor();
		for(final Player player : World.getAroundPlayers(actor, 5000, 1000))
			if(player != null && player.getZ() >= -10580 && player.getZ() <= -10420)
				player.teleToLocation(Rnd.get(-16744, 16104), Rnd.get(-51128, -50840), -11008);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		DeleteNpc();
		super.onEvtDead(killer);
	}

	private void SpawnNpc1()
	{
		if(!npcs1.isEmpty())
		{
			for(final Integer id : npcs1)
			{
				final NpcInstance npc = GameObjectsStorage.getNpc(id);
				if(npc != null)
					npc.deleteMe();
			}
			npcs1.clear();
		}
		if(!altars.isEmpty())
		{
			for(final Integer id : altars)
			{
				final NpcInstance npc = GameObjectsStorage.getNpc(id);
				if(npc != null)
					npc.deleteMe();
			}
			altars.clear();
		}
		npcs1.add(NpcUtils.spawnSingle(TriolsRevelation1, new Location(-20117, -52683, -10974)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(TriolsRevelation2, new Location(-20137, -54371, -11170)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(TriolsRevelation3, new Location(-12710, -52677, -10974)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(TriolsRevelation4, new Location(-12660, -54379, -11170)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(TriolsRevelation5, new Location(-17826, -53426, -11624)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(TriolsRevelation6, new Location(-17068, -53440, -11624)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(TriolsRevelation7, new Location(-16353, -53549, -11624)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(TriolsRevelation8, new Location(-15655, -53869, -11624)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(TriolsRevelation9, new Location(-15005, -53132, -11624)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(TriolsRevelation10, new Location(-16316, -56842, -10900)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(TriolsRevelation11, new Location(-16395, -54055, -10439, 15992)).getObjectId());
		Ritual_Offering = NpcUtils.spawnSingle(RitualOffering, new Location(-16384, -53197, -10439, 15992)).getObjectId();
		altars.add(NpcUtils.spawnSingle(AltarGatekeeper, new Location(-17248, -54832, -10424, 16384)).getObjectId());
		altars.add(NpcUtils.spawnSingle(AltarGatekeeper, new Location(-15547, -54835, -10424, 16384)).getObjectId());
		altars.add(NpcUtils.spawnSingle(AltarGatekeeper, new Location(-18116, -54831, -10579, 16384)).getObjectId());
		altars.add(NpcUtils.spawnSingle(AltarGatekeeper, new Location(-14645, -54836, -10577, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-18008, -53394, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17653, -53399, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17827, -53575, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-18008, -53749, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17653, -53754, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17827, -53930, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-18008, -54100, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17653, -54105, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17275, -52577, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-16917, -52577, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-16738, -52577, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17003, -52404, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17353, -52404, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17362, -52752, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17006, -52752, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17721, -52752, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17648, -52968, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-17292, -52968, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-16374, -52577, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-16648, -52404, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-16284, -52404, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-16013, -52577, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-15658, -52577, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-15306, -52577, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-15923, -52404, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-15568, -52404, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-15216, -52404, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-15745, -52752, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-15394, -52752, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-15475, -52969, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-15119, -52969, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-15149, -53411, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-14794, -53416, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-14968, -53592, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-15149, -53766, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-14794, -53771, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-14968, -53947, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-15149, -54117, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard1, new Location(-14794, -54122, -10594, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard2, new Location(-16392, -52124, -10592)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(22189, new Location(Rnd.get(-16402, -16382), Rnd.get(-52134, -52114), -10592)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(22189, new Location(Rnd.get(-16402, -16382), Rnd.get(-52134, -52114), -10592)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(22190, new Location(Rnd.get(-16402, -16382), Rnd.get(-52134, -52114), -10592)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16380, -45796, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16290, -45796, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16471, -45796, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16380, -45514, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16290, -45514, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16471, -45514, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16380, -45243, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16290, -45243, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16471, -45243, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16380, -44973, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16290, -44973, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16471, -44973, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16380, -44703, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16290, -44703, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16471, -44703, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16471, -44443, -10726, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16382, -47685, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16292, -47685, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16474, -47685, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16382, -47404, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16292, -47404, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16474, -47404, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16382, -47133, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16292, -47133, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16474, -47133, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16382, -46862, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16292, -46862, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16474, -46862, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16382, -46593, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16292, -46593, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16474, -46593, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16382, -46333, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16292, -46333, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16474, -46333, -10822, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16381, -49743, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16291, -49743, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16473, -49743, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16381, -49461, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16291, -49461, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16473, -49461, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16381, -49191, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16291, -49191, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16473, -49191, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16381, -48920, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16291, -48920, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16473, -48920, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16381, -48650, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16291, -48650, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16473, -48650, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16381, -48391, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16291, -48391, -10918, 16384)).getObjectId());
		npcs1.add(NpcUtils.spawnSingle(AndreasRoyalGuards3, new Location(-16473, -48391, -10918, 16384)).getObjectId());
	}

	private void SpawnNpc2()
	{
		npcs2.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard3, new Location(-16385, -53268, -10439, 15992)).getObjectId());
		npcs2.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard3, new Location(-17150, -54046, -10439, 15992)).getObjectId());
		npcs2.add(NpcUtils.spawnSingle(AndreasCaptainRoyalGuard3, new Location(-15690, -54030, -10439, 15992)).getObjectId());
		npcs2.add(NpcUtils.spawnSingle(AndreasRoyalGuards1, new Location(-16385, -53268, -10439, 15992)).getObjectId());
		npcs2.add(NpcUtils.spawnSingle(AndreasRoyalGuards1, new Location(-17150, -54046, -10439, 15992)).getObjectId());
		npcs2.add(NpcUtils.spawnSingle(AndreasRoyalGuards1, new Location(-15690, -54030, -10439, 15992)).getObjectId());
		npcs2.add(NpcUtils.spawnSingle(AndreasRoyalGuards2, new Location(-16385, -53268, -10439, 15992)).getObjectId());
		npcs2.add(NpcUtils.spawnSingle(AndreasRoyalGuards2, new Location(-17150, -54046, -10439, 15992)).getObjectId());
		npcs2.add(NpcUtils.spawnSingle(AndreasRoyalGuards2, new Location(-15690, -54030, -10439, 15992)).getObjectId());
	}

	private void DeleteNpc()
	{
		if(_checkTask != null)
		{
			_checkTask.cancel(false);
			_checkTask = null;
		}
		_action = false;
		_firstTimeMove = true;
		if(!npcs2.isEmpty())
		{
			for(final Integer id : npcs2)
			{
				final NpcInstance npc = GameObjectsStorage.getNpc(id);
				if(npc != null)
					npc.deleteMe();
			}
			npcs2.clear();
		}
		if(!npcs1.isEmpty())
		{
			for(final Integer id : npcs1)
			{
				final NpcInstance npc = GameObjectsStorage.getNpc(id);
				if(npc != null)
					npc.deleteMe();
			}
			npcs1.clear();
		}
		if(!altars.isEmpty())
		{
			for(final Integer id : altars)
			{
				final NpcInstance npc = GameObjectsStorage.getNpc(id);
				if(npc != null)
					npc.deleteMe();
			}
			altars.clear();
		}
		COUNTER = 0;
		_stage = 0;
		final DoorInstance door1 = DoorTable.getInstance().getDoor(19160014);
		final DoorInstance door2 = DoorTable.getInstance().getDoor(19160015);
		if(!door1.isOpen())
			door1.openMe();
		if(!door2.isOpen())
			door2.openMe();
	}

	public static void action()
	{
		if(!_action)
		{
			++COUNTER;
			if(COUNTER >= 43)
			{
				final DoorInstance door1 = DoorTable.getInstance().getDoor(19160016);
				final DoorInstance door2 = DoorTable.getInstance().getDoor(19160017);
				if(!door1.isOpen())
					door1.openMe();
				if(!door2.isOpen())
					door2.openMe();
				if(!altars.isEmpty())
					for(final Integer id : altars)
					{
						final NpcInstance npc = GameObjectsStorage.getNpc(id);
						if(npc != null)
							Functions.npcShout(npc, "The door to the 3rd floor of the altar is now open.", 0);
					}
				_action = true;
			}
		}
	}

	static
	{
		_movieTask = null;
		_checkTask = null;
		npcs1 = new CopyOnWriteArrayList<Integer>();
		npcs2 = new CopyOnWriteArrayList<Integer>();
		altars = new CopyOnWriteArrayList<Integer>();
		Ritual_Offering = 0;
		Ritual_Sacrifice = 0;
		COUNTER = 0;
		_action = false;
		TriolsRevelation1 = 32058;
		TriolsRevelation2 = 32059;
		TriolsRevelation3 = 32060;
		TriolsRevelation4 = 32061;
		TriolsRevelation5 = 32062;
		TriolsRevelation6 = 32063;
		TriolsRevelation7 = 32064;
		TriolsRevelation8 = 32065;
		TriolsRevelation9 = 32066;
		TriolsRevelation10 = 32067;
		TriolsRevelation11 = 32068;
		RitualOffering = 32038;
		RitualSacrifice = 22195;
		AltarGatekeeper = 32051;
		AndreasCaptainRoyalGuard1 = 22175;
		AndreasCaptainRoyalGuard2 = 22188;
		AndreasCaptainRoyalGuard3 = 22191;
		AndreasRoyalGuards1 = 22192;
		AndreasRoyalGuards2 = 22193;
		AndreasRoyalGuards3 = 22176;
	}

	private class Movie implements Runnable
	{
		private int _taskId;

		public Movie(final int taskId)
		{
			_taskId = taskId;
		}

		@Override
		public void run()
		{
			final NpcInstance actor = getActor();
			actor.setHeading(16384);
			final NpcInstance camera = NpcUtils.spawnSingle(13014, new Location(-16362, -53754, -10439, 15992));
			switch(_taskId)
			{
				case 1:
				{
					actor.setImmobilized(true);
					for(final Player player : World.getAroundPlayers(actor, 4000, 400))
						if(player != null)
						{
							player.enterMovieMode();
							player.specialCamera(camera, 1500, 88, 89, 0, 5000);
						}
					if(_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().schedule(new Movie(2), 3000L);
					break;
				}
				case 2:
				{
					for(final Player player : World.getAroundPlayers(actor, 4000, 400))
						if(player != null)
						{
							player.enterMovieMode();
							player.specialCamera(camera, 450, 88, 3, 0, 5000);
						}
					if(_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().schedule(new Movie(3), 2500L);
					break;
				}
				case 3:
				{
					final NpcInstance npc1 = GameObjectsStorage.getNpc(Ritual_Offering);
					if(npc1 != null)
					{
						actor.setTarget(npc1);
						actor.doCast(SkillTable.getInstance().getInfo(1168, 7), npc1, false);
					}
					if(_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().schedule(new Movie(4), 1500L);
					break;
				}
				case 4:
				{
					for(final Player player2 : World.getAroundPlayers(actor, 4000, 400))
						if(player2 != null)
						{
							player2.enterMovieMode();
							player2.specialCamera(camera, 500, 88, 3, 5000, 5000);
						}
					if(_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().schedule(new Movie(5), 1000L);
					break;
				}
				case 5:
				{
					final NpcInstance npc2 = GameObjectsStorage.getNpc(Ritual_Offering);
					if(npc2 != null)
						npc2.doDie(actor);
					if(_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().schedule(new Movie(6), 3000L);
					break;
				}
				case 6:
				{
					for(final Player player3 : World.getAroundPlayers(actor, 4000, 400))
						if(player3 != null)
						{
							player3.enterMovieMode();
							player3.specialCamera(camera, 3000, 88, 4, 6000, 5000);
						}
					final NpcInstance npc3 = GameObjectsStorage.getNpc(Ritual_Offering);
					if(npc3 != null)
						npc3.deleteMe();
					Ritual_Sacrifice = NpcUtils.spawnSingle(RitualSacrifice, new Location(-16397, -53199, -10448, 16384)).getObjectId();
					if(_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					_movieTask = ThreadPoolManager.getInstance().schedule(new Movie(7), 6000L);
					break;
				}
				case 7:
				{
					for(final Player player4 : World.getAroundPlayers(actor, 4000, 400))
						if(player4 != null)
							player4.leaveMovieMode();
					final NpcInstance npc4 = GameObjectsStorage.getNpc(Ritual_Sacrifice);
					if(npc4 != null)
						npc4.deleteMe();
					camera.deleteMe();
					actor.setImmobilized(false);
					if(_movieTask != null)
						_movieTask.cancel(false);
					_movieTask = null;
					break;
				}
			}
		}
	}

	private class CheckAttack implements Runnable
	{
		@Override
		public void run()
		{
			DeleteNpc();
			clearZone();
			final NpcInstance actor = getActor();
			if(actor != null)
				actor.setCurrentHpMp(actor.getMaxHp(), actor.getMaxMp(), false);
			ThreadPoolManager.getInstance().schedule(new NewSpawn(), 10000L);
		}
	}

	private class NewSpawn implements Runnable
	{
		@Override
		public void run()
		{
			SpawnNpc1();
		}
	}
}
