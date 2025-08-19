package ai;

import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.instances.NpcInstance;

public class WaterFighter extends Fighter
{
	public WaterFighter(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if(actor == null || actor.isDead())
			return true;
		if(!actor.isInWater())
		{
			teleportHome();
			return true;
		}
		return super.thinkActive();
	}
}
