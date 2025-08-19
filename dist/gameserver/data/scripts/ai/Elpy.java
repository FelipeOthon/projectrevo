package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.Location;

public class Elpy extends Fighter
{
	public Elpy(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = getActor();
		if(attacker != null && Rnd.chance(50))
		{
			final Location pos = Location.findPointToStay(actor.getLoc(), 150, 200, actor.getGeoIndex());
			if(GeoEngine.canMoveToCoord(actor.getX(), actor.getY(), actor.getZ(), pos.x, pos.y, pos.z, actor.getGeoIndex()))
			{
				actor.setRunning();
				this.addTaskMove(pos, false);
			}
		}
	}

	@Override
	public boolean checkAggression(final Creature target)
	{
		return false;
	}

	@Override
	protected void onEvtAggression(final Creature target, final int aggro)
	{}
}
