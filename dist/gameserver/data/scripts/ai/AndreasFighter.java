package ai;

import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;

public class AndreasFighter extends Fighter
{
	public AndreasFighter(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		AndreasVanHalter.action();
		super.onEvtDead(killer);
	}
}
