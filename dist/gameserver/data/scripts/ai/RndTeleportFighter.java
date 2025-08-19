package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.tables.TerritoryTable;
import l2s.gameserver.utils.Location;

public class RndTeleportFighter extends Fighter
{
	private long _lastTeleport;

	public RndTeleportFighter(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean maybeMoveToHome()
	{
		final NpcInstance actor = getActor();
		if(actor == null || System.currentTimeMillis() - _lastTeleport < 10000L)
			return false;
		final boolean randomWalk = actor.hasRandomWalk();
		final Location sloc = actor.getSpawnedLoc();
		if(sloc == null)
			return false;
		if(randomWalk && (!Config.RND_WALK || Rnd.chance(Config.RND_WALK_RATE)))
			return false;
		if(!randomWalk && actor.isInRangeZ(sloc, Config.MAX_DRIFT_RANGE))
			return false;
		final int x = sloc.x + Rnd.get(-Config.MAX_DRIFT_RANGE, Config.MAX_DRIFT_RANGE);
		final int y = sloc.y + Rnd.get(-Config.MAX_DRIFT_RANGE, Config.MAX_DRIFT_RANGE);
		final int z = GeoEngine.getLowerHeight(x, y, sloc.z, actor.getGeoIndex());
		if(sloc.z - z > 64)
			return false;
		final Spawn spawn = actor.getSpawn();
		boolean isInside = true;
		if(spawn != null && spawn.getLocation() != 0)
			isInside = TerritoryTable.getInstance().getLocation(spawn.getLocation()).isInside(x, y);
		if(isInside)
		{
			actor.broadcastPacketToOthers(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4671, 1, 500, 0L) });
			ThreadPoolManager.getInstance().schedule(new DefaultAI.Teleport(new Location(x, y, z)), 500L);
			_lastTeleport = System.currentTimeMillis();
		}
		return isInside;
	}
}
