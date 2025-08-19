package ai;

import l2s.gameserver.ai.Mystic;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;

public class AndreasMystic extends Mystic
{
	public AndreasMystic(final NpcInstance actor)
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
