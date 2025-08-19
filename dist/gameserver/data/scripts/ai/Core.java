package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.PlaySound;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.utils.Location;

public class Core extends Fighter
{
	private boolean _firstTimeAttacked;
	private static final int TELEPORTATION_CUBIC_ID = 31842;
	private static final Location CUBIC_1_POSITION;
	private static final Location CUBIC_2_POSITION;
	private static final int CUBIC_DESPAWN_TIME = 900000;

	public Core(final NpcInstance actor)
	{
		super(actor);
		_firstTimeAttacked = true;
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance _thisActor = getActor();
		if(_firstTimeAttacked)
		{
			_firstTimeAttacked = false;
			Functions.npcShout(_thisActor, "A non-permitted target has been discovered.", 0);
			Functions.npcShout(_thisActor, "Starting intruder removal system.", 0);
		}
		else if(Rnd.chance(1))
			Functions.npcShout(_thisActor, "Removing intruders.", 0);
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		final NpcInstance _thisActor = getActor();
		_thisActor.broadcastPacket(new L2GameServerPacket[] { new PlaySound(1, "BS02_D", 1, 0, _thisActor.getLoc()) });
		Functions.npcShout(_thisActor, "A fatal error has occurred", 0);
		Functions.npcShout(_thisActor, "System is being shut down...", 0);
		Functions.npcShout(_thisActor, "......", 0);
		try
		{
			final Spawn spawn1 = new Spawn(NpcTable.getTemplate(31842));
			spawn1.setLoc(Core.CUBIC_1_POSITION);
			spawn1.doSpawn(true);
			spawn1.stopRespawn();
			final Spawn spawn2 = new Spawn(NpcTable.getTemplate(31842));
			spawn2.setLoc(Core.CUBIC_2_POSITION);
			spawn2.doSpawn(true);
			spawn2.stopRespawn();
			ThreadPoolManager.getInstance().schedule(new DeSpawnScheduleTimerTask(spawn1, spawn2), 900000L);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		_firstTimeAttacked = true;
		super.onEvtDead(killer);
	}

	static
	{
		CUBIC_1_POSITION = new Location(16502, 110165, -6394, 0);
		CUBIC_2_POSITION = new Location(18948, 110165, -6394, 0);
	}

	public class DeSpawnScheduleTimerTask implements Runnable
	{
		Spawn _spawn1;
		Spawn _spawn2;

		public DeSpawnScheduleTimerTask(final Spawn spawn1, final Spawn spawn2)
		{
			_spawn1 = null;
			_spawn2 = null;
			_spawn1 = spawn1;
			_spawn2 = spawn2;
		}

		@Override
		public void run()
		{
			try
			{
				_spawn1.getLastSpawn().decayMe();
				_spawn2.getLastSpawn().decayMe();
				_spawn1.getLastSpawn().deleteMe();
				_spawn2.getLastSpawn().deleteMe();
			}
			catch(Throwable t)
			{}
		}
	}
}
