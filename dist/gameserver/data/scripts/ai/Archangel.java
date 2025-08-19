package ai;

import bosses.BaiumManager;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.model.AggroList;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;

public class Archangel extends Fighter
{
	private long _spawnTime;

	public Archangel(final NpcInstance actor)
	{
		super(actor);
		_spawnTime = 0L;
	}

	@Override
	protected boolean checkAggression(final Creature target)
	{
		final NpcInstance actor = getActor();
		if(actor == null || getIntention() != CtrlIntention.AI_INTENTION_ACTIVE || !isGlobalAggro())
			return false;
		if(target.isAlikeDead())
			return false;
		if(target.isNpc() && target.isInvul())
			return false;
		if(target.isPlayable())
		{
			if(!canSeeInSilentMove((Playable) target))
				return false;
			if(target.isPlayer() && target.isInvisible())
				return false;
			if(((Playable) target).getNonAggroTime() > System.currentTimeMillis())
				return false;
		}
		return target.isInRangeZ(actor.getLoc(), (long) actor.getAggroRange()) && System.currentTimeMillis() - _spawnTime >= 7000L && (target.isPlayable() || target.getNpcId() == 29020 && System.currentTimeMillis() - _spawnTime >= 30000L) && GeoEngine.canSeeTarget(actor, target);
	}

	@Override
	protected void onEvtSpawn()
	{
		_spawnTime = System.currentTimeMillis();
		super.onEvtSpawn();
	}

	@Override
	protected void onEvtAggression(final Creature attacker, final int aggro)
	{
		if(attacker == null || !attacker.isPlayable())
			return;
		super.onEvtAggression(attacker, aggro);
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		if(attacker == null || !attacker.isPlayable())
			return;
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected void returnHome()
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return;
		clearTasks();
		actor.stopMove();
		actor.getAggroList().clear(true);
		setAttackTimeout(Long.MAX_VALUE);
		setAttackTarget((Creature) null);
		changeIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
	}

	@Override
	public int getMaxAttackTimeout()
	{
		return 500;
	}

	@Override
	protected boolean checkTarget(final Creature target, final int range)
	{
		final NpcInstance actor = getActor();
		if(actor == null || target == null || target.isAlikeDead() || !actor.isInRangeZ(target, range) || !target.isPlayable() && target.getNpcId() != 29020)
			return false;
		final boolean hidden = target.isPlayable() && target.isInvisible();
		if(!hidden && actor.isConfused())
			return true;
		if(getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
			return target.isPlayable() || target.getNpcId() == 29020;
		final AggroList.AggroInfo ai = actor.getAggroList().get(target);
		if(ai == null)
			return false;
		if(hidden)
		{
			ai.hate = 0;
			return false;
		}
		return ai.hate > 0;
	}

	@Override
	protected void onEvtSeeSpell(final Skill skill, final Creature caster)
	{
		super.onEvtSeeSpell(skill, caster);
		final NpcInstance actor = getActor();
		if(actor == null)
			return;
		if(skill.getSkillType() == Skill.SkillType.AGGRESSION && caster.getTarget() == actor)
			this.setIntention(CtrlIntention.AI_INTENTION_ATTACK, caster);
	}

	@Override
	protected boolean maybeMoveToHome()
	{
		final NpcInstance actor = getActor();
		return actor != null && !BaiumManager.getZone().checkIfInZone(actor);
	}
}
