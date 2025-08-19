package ai;

import l2s.gameserver.ai.Guard;
import l2s.gameserver.model.instances.NpcInstance;

public class GuardRndWalkAndAnim extends Guard
{
	public GuardRndWalkAndAnim(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		return super.thinkActive() || randomAnimation() || randomWalk();
	}
}
