package ai;

import java.util.concurrent.ScheduledFuture;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.Location;

public class FollowNpc extends DefaultAI
{
	private boolean _thinking;
	private ScheduledFuture<?> _followTask;

	public FollowNpc(final NpcInstance actor)
	{
		super(actor);
		_thinking = false;
	}

	@Override
	protected boolean randomWalk()
	{
		return getActor() instanceof MonsterInstance;
	}

	@Override
	protected void onEvtThink()
	{
		final NpcInstance actor = getActor();
		if(_thinking || actor.isActionsDisabled() || actor.isAfraid() || actor.isDead() || actor.isMovementDisabled())
			return;
		_thinking = true;
		try
		{
			if(!Config.BLOCK_ACTIVE_TASKS && (getIntention() == CtrlIntention.AI_INTENTION_ACTIVE || getIntention() == CtrlIntention.AI_INTENTION_IDLE))
				thinkActive();
			else if(getIntention() == CtrlIntention.AI_INTENTION_FOLLOW)
				thinkFollow();
		}
		finally
		{
			_thinking = false;
		}
	}

	protected void thinkFollow()
	{
		final NpcInstance actor = getActor();
		final Creature target = actor.getFollowTarget();
		if(target == null || target.isAlikeDead() || actor.getDistance(target) > 4000.0 || actor.isMovementDisabled())
		{
			clientActionFailed();
			return;
		}
		if(actor.isFollow && actor.getFollowTarget() == target)
		{
			clientActionFailed();
			return;
		}
		if(actor.isInRange(target, 120L))
			clientActionFailed();
		if(_followTask != null)
		{
			_followTask.cancel(false);
			_followTask = null;
		}
		_followTask = ThreadPoolManager.getInstance().schedule(new ThinkFollow(), 25L);
	}

	protected class ThinkFollow implements Runnable
	{
		public NpcInstance getActor()
		{
			return FollowNpc.this.getActor();
		}

		@Override
		public void run()
		{
			final NpcInstance actor = getActor();
			if(actor == null)
				return;
			final Creature target = actor.getFollowTarget();
			if(target == null || target.isAlikeDead() || actor.getDistance(target) > 4000.0)
			{
				FollowNpc.this.setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
				return;
			}
			if(!actor.isInRange(target, 120L) && (!actor.isFollow || actor.getFollowTarget() != target))
			{
				final Location loc = new Location(target.getX() + Rnd.get(-60, 60), target.getY() + Rnd.get(-60, 60), target.getZ());
				actor.followToCharacter(loc, target, 100, false);
			}
			_followTask = ThreadPoolManager.getInstance().schedule(this, 250L);
		}
	}
}
