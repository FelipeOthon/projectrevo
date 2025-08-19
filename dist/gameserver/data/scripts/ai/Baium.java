package ai;

import java.util.HashMap;
import java.util.Map;

import bosses.BaiumManager;
import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.SkillTable;

public class Baium extends DefaultAI
{
	private final Skill baium_normal_attack;
	private final Skill energy_wave;
	private final Skill earth_quake;
	private final Skill thunderbolt;
	private final Skill group_hold;
	private final Skill heal;

	public Baium(final NpcInstance actor)
	{
		super(actor);
		baium_normal_attack = SkillTable.getInstance().getInfo(4127, 1);
		energy_wave = SkillTable.getInstance().getInfo(4128, 1);
		earth_quake = SkillTable.getInstance().getInfo(4129, 1);
		thunderbolt = SkillTable.getInstance().getInfo(4130, 1);
		group_hold = SkillTable.getInstance().getInfo(4131, 1);
		heal = SkillTable.getInstance().getInfo(4135, 1);
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		if(attacker.getPlayer() != null)
			BaiumManager.setLastAttackTime();
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected boolean createNewTask()
	{
		final NpcInstance actor = getActor();
		if(actor == null || actor.isDead())
			return true;
		if(!BaiumManager.getZone().checkIfInZone(actor))
		{
			teleportHome();
			return false;
		}
		clearTasks();
		Creature target;
		if((target = prepareTarget()) == null || !target.isPlayable())
		{
			double dist = actor.getAggroRange();
			for(final Playable cha : World.getAroundPlayables(actor, actor.getAggroRange(), 600))
				if(!cha.isDead() && !cha.isInvisible() && GeoEngine.canSeeTarget(actor, cha) && actor.getDistance(cha) < dist)
				{
					target = cha;
					dist = actor.getDistance(cha);
				}
		}
		if(target == null && !BaiumManager._angels.isEmpty())
		{
			double dist = 1000.0;
			for(final NpcInstance angel : BaiumManager._angels)
				if(angel != null && !angel.isDead() && actor.getDistance(angel) < dist && angel.getAI().getAttackTarget() == actor)
				{
					target = angel;
					dist = actor.getDistance(angel);
				}
			if(target != null)
				actor.getAggroList().addDamageHate(target, 0, 1);
		}
		if(target == null)
			return false;
		if(!BaiumManager.getZone().checkIfInZone(target))
		{
			actor.getAggroList().remove(target, false);
			return false;
		}
		final int s_energy_wave = 20;
		final int s_earth_quake = 20;
		final int s_group_hold = actor.getCurrentHpPercents() > 50.0 ? 0 : 20;
		final int s_thunderbolt = actor.getCurrentHpPercents() > 25.0 ? 0 : 20;
		final int s_heal = actor.getCurrentHpPercents() > 30.0 ? 0 : 25;
		Skill r_skill = null;
		if(actor.isMovementDisabled())
			r_skill = thunderbolt;
		else if(!Rnd.chance(100 - s_thunderbolt - s_group_hold - s_energy_wave - s_earth_quake))
		{
			final Map<Skill, Integer> d_skill = new HashMap<Skill, Integer>();
			final double distance = actor.getDistance(target);
			this.addDesiredSkill(d_skill, target, distance, energy_wave);
			this.addDesiredSkill(d_skill, target, distance, earth_quake);
			if(s_group_hold > 0)
				this.addDesiredSkill(d_skill, target, distance, group_hold);
			if(s_thunderbolt > 0)
				this.addDesiredSkill(d_skill, target, distance, thunderbolt);
			if(s_heal > 0)
				this.addDesiredSkill(d_skill, target, distance, heal);
			r_skill = selectTopSkill(d_skill);
		}
		if(r_skill == null)
			r_skill = baium_normal_attack;
		else if(r_skill.getTargetType() == Skill.SkillTargetType.TARGET_SELF)
			target = actor;
		addTaskCast(target, r_skill);
		r_skill = null;
		return true;
	}

	@Override
	protected boolean maybeMoveToHome()
	{
		final NpcInstance actor = getActor();
		return actor != null && !BaiumManager.getZone().checkIfInZone(actor);
	}
}
