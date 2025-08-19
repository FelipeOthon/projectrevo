package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.ai.Priest;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.utils.Location;
import npc.model.QueenAntInstance;

public class QueenAntNurse extends Priest
{
	public QueenAntNurse(final NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = 10000;
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if(actor == null || actor.isDead())
			return true;
		if(_def_think)
		{
			if(doTask())
				clearTasks();
			return true;
		}
		final Creature top_desire_target = getTopDesireTarget();
		if(top_desire_target == null)
			return false;
		if(actor.getDistance(top_desire_target) - top_desire_target.getCollisionRadius() - actor.getCollisionRadius() > 200.0)
		{
			moveOrTeleportToLocation(Location.findFrontPosition(top_desire_target, actor, 100, 150, actor.getGeoIndex()));
			return false;
		}
		return !top_desire_target.isCurrentHpFull() && doTask() && createNewTask();
	}

	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		final NpcInstance actor = getActor();
		if(actor == null)
			return false;
		final Creature top_desire_target = getTopDesireTarget();
		if(actor.isDead() || top_desire_target == null)
			return false;
		if(!top_desire_target.isCurrentHpFull())
		{
			final Skill skill = _healSkills[Rnd.get(_healSkills.length)];
			if(skill.getAOECastRange() < actor.getDistance(top_desire_target))
				moveOrTeleportToLocation(Location.findFrontPosition(top_desire_target, actor, skill.getAOECastRange() - 30, skill.getAOECastRange() - 10, actor.getGeoIndex()));
			addTaskBuff(top_desire_target, skill);
			return true;
		}
		return false;
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	private void moveOrTeleportToLocation(final Location loc)
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return;
		actor.setRunning();
		if(actor.moveToLocation(loc, 0, true))
			return;
		this.clientStopMoving();
		_pathfindFails = 0;
		actor.broadcastPacketToOthers(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 2036, 1, 500, 600000L) });
		ThreadPoolManager.getInstance().schedule(new DefaultAI.Teleport(loc), 500L);
	}

	private Creature getTopDesireTarget()
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return null;
		final QueenAntInstance queen_ant = (QueenAntInstance) ((MonsterInstance) actor).getLeader();
		if(queen_ant == null)
			return null;
		final Creature Larva = queen_ant.getLarva();
		if(Larva != null && Larva.getCurrentHpPercents() < 5.0)
			return Larva;
		return queen_ant;
	}

	@Override
	protected void onIntentionAttack(final Creature target)
	{}

	@Override
	protected void onEvtClanAttacked(final Creature attacked_member, final Creature attacker, final int damage)
	{
		if(doTask())
			createNewTask();
	}
}
