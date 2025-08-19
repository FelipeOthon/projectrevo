package ai;

import l2s.gameserver.ai.Mystic;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;

public class PaganGuard extends Mystic
{
	public PaganGuard(final NpcInstance actor)
	{
		super(actor);
		actor.setImmobilized(true);
	}

	@Override
	protected boolean checkTarget(final Creature target, final int range)
	{
		final NpcInstance actor = getActor();
		if(actor != null && target != null && !actor.isInRange(target, actor.getAggroRange()))
		{
			actor.getAggroList().remove(target, true);
			return false;
		}
		return super.checkTarget(target, range);
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
