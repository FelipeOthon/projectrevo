package ai;

import java.util.List;

import l2s.gameserver.Config;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.CTBBossInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.Location;

public abstract class MatchFighter extends Fighter
{
	public MatchFighter(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if(actor.isActionsDisabled())
			return true;
		if(_def_think)
		{
			if(doTask())
				clearTasks();
			return true;
		}
		final long now = System.currentTimeMillis();
		if(now - _checkAggroTimestamp > Config.AGGRO_CHECK_INTERVAL)
		{
			_checkAggroTimestamp = now;
			int count = 0;
			final List<Creature> chars = World.getAroundCharacters(actor, Config.AGGRO_CHECK_RADIUS, Config.AGGRO_CHECK_HEIGHT);
			final int size = Math.min(chars.size(), 1000);
			while(!chars.isEmpty())
			{
				if(++count > size)
					break;
				final Creature target = getNearestTarget(chars);
				if(target == null)
					break;
				if(checkAggression(target))
				{
					actor.getAggroList().addDamageHate(target, 0, 2);
					if(target.isSummon())
						actor.getAggroList().addDamageHate(target.getPlayer(), 0, 1);
					actor.setRunning();
					this.setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
					return true;
				}
				chars.remove(target);
			}
		}
		return randomWalk();
	}

	@Override
	protected boolean checkAggression(final Creature target)
	{
		final CTBBossInstance actor = getActor();
		return getIntention() == CtrlIntention.AI_INTENTION_ACTIVE && !target.isAlikeDead() && !target.isInvul() && actor.isAttackable(target) && GeoEngine.canSeeTarget(actor, target);
	}

	@Override
	protected boolean canAttackCharacter(final Creature target)
	{
		final NpcInstance actor = getActor();
		return actor.isAttackable(target);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		final CTBBossInstance actor = getActor();
		final int x = (int) (actor.getX() + 800.0 * Math.cos(actor.headingToRadians(actor.getHeading() - 32768)));
		final int y = (int) (actor.getY() + 800.0 * Math.sin(actor.headingToRadians(actor.getHeading() - 32768)));
		actor.setSpawnedLoc(new Location(x, y, actor.getZ()));
		this.addTaskMove(actor.getSpawnedLoc(), true);
		doTask();
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	@Override
	public CTBBossInstance getActor()
	{
		return (CTBBossInstance) super.getActor();
	}
}
