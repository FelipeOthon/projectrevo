package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;

public class RndWalkAndAnim extends DefaultAI
{
	protected static final int PET_WALK_RANGE = 100;

	public RndWalkAndAnim(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if(actor == null || actor.isMoving)
			return false;
		final int val = Rnd.get(100);
		if(val < 20)
			randomWalk();
		else if(val < 40)
			actor.onRandomAnimation();
		return false;
	}

	@Override
	protected boolean randomWalk()
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return false;
		final int spawnX = actor.getSpawnedLoc().getX();
		final int spawnY = actor.getSpawnedLoc().getY();
		final int spawnZ = actor.getSpawnedLoc().getZ();
		final int x = spawnX + Rnd.get(200) - 100;
		final int y = spawnY + Rnd.get(200) - 100;
		final int z = GeoEngine.getLowerHeight(x, y, spawnZ, actor.getGeoIndex());
		actor.setRunning();
		actor.moveToLocation(x, y, z, 0, true);
		return true;
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{}

	@Override
	protected void onEvtAggression(final Creature target, final int aggro)
	{}
}
