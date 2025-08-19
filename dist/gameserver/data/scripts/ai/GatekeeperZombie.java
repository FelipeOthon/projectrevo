package ai;

import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.ai.Mystic;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;

public class GatekeeperZombie extends Mystic
{
	public GatekeeperZombie(final NpcInstance actor)
	{
		super(actor);
		actor.setImmobilized(true);
	}

	@Override
	public boolean checkAggression(final Creature target)
	{
		final NpcInstance actor = getActor();
		if(actor == null || target == null || actor.isDead() || !target.isPlayable() || target.isAlikeDead())
			return false;
		if(getIntention() != CtrlIntention.AI_INTENTION_ACTIVE || !isGlobalAggro())
			return false;
		if(!target.isInRangeZ(actor.getSpawnedLoc(), (long) actor.getAggroRange()))
			return false;
		if(Functions.getItemCount((Playable) target, 8067) != 0L || Functions.getItemCount((Playable) target, 8064) != 0L)
			return false;
		if(!GeoEngine.canSeeTarget(actor, target))
			return false;
		if(getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
		{
			actor.getAggroList().addDamageHate(target, 0, 1);
			this.setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}
		return true;
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
