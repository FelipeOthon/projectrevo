package ai;

import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.NpcInstance;

public class GuardAnt extends Fighter
{
	public GuardAnt(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public boolean checkAggression(final Creature target)
	{
		return target.isInZone(Zone.ZoneType.epic) && super.checkAggression(target);
	}

	@Override
	protected void onEvtAggression(final Creature attacker, final int aggro)
	{
		if(attacker.isParalyzed())
			return;
		super.onEvtAggression(attacker, aggro);
	}

	@Override
	protected boolean tryMoveToTarget(final Creature target, final int offset)
	{
		if(target.isParalyzed())
		{
			clearTasks();
			changeIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
			onEvtThink();
			return false;
		}
		return super.tryMoveToTarget(target, offset);
	}
}
