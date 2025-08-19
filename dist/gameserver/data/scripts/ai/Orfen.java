package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.PrintfFormat;
import npc.model.OrfenInstance;

public class Orfen extends Fighter
{
	public static final PrintfFormat[] MsgOnRecall;
	final Skill _paralyze;
	final Skill _paralyzeDam;

	public Orfen(final NpcInstance actor)
	{
		super(actor);
		_paralyze = SkillTable.getInstance().getInfo(4064, 1);
		_paralyzeDam = SkillTable.getInstance().getInfo(4063, 1);
	}

	@Override
	protected boolean thinkActive()
	{
		if(super.thinkActive())
			return true;
		final OrfenInstance actor = getActor();
		if(actor == null)
			return true;
		if(actor.isTeleported() && actor.getCurrentHpPercents() > 95.0)
		{
			actor.setTeleported(false);
			return true;
		}
		return false;
	}

	@Override
	protected boolean createNewTask()
	{
		return defaultNewTask();
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		super.onEvtAttacked(attacker, skill, damage);
		final OrfenInstance actor = getActor();
		if(actor == null || actor.isCastingNow())
			return;
		final double distance = actor.getDistance(attacker);
		if(distance > 300.0 && distance < 1000.0 && Rnd.chance(5))
		{
			Functions.npcSay(actor, Orfen.MsgOnRecall[Rnd.get(Orfen.MsgOnRecall.length - 1)].sprintf(attacker.getName()));
			teleToLocation(attacker, Location.findFrontPosition(actor, attacker, 0, 50, actor.getGeoIndex()));
			if(this.canUseSkill(_paralyzeDam, attacker, -1.0))
				this.addTaskAttack(attacker, _paralyzeDam, 1000000);
		}
		else if(Rnd.chance(15) && this.canUseSkill(_paralyze, attacker, -1.0))
			this.addTaskAttack(attacker, _paralyze, 1000000);
	}

	@Override
	protected void onEvtSeeSpell(final Skill skill, final Creature caster)
	{
		super.onEvtSeeSpell(skill, caster);
		final OrfenInstance actor = getActor();
		if(actor == null || actor.isCastingNow())
			return;
		final double distance = actor.getDistance(caster);
		if((skill.getSkillType() == Skill.SkillType.HEAL || skill.getSkillType() == Skill.SkillType.HEAL_PERCENT) && skill.getEffectPoint() > 100 && distance < 1000.0 && Rnd.chance(25))
		{
			Functions.npcSay(actor, Orfen.MsgOnRecall[Rnd.get(Orfen.MsgOnRecall.length)].sprintf(caster.getName()));
			teleToLocation(caster, Location.findFrontPosition(actor, caster, 0, 50, actor.getGeoIndex()));
			if(this.canUseSkill(_paralyzeDam, caster, -1.0))
				this.addTaskAttack(caster, _paralyzeDam, 1000000);
		}
	}

	@Override
	public OrfenInstance getActor()
	{
		return (OrfenInstance) super.getActor();
	}

	private void teleToLocation(final Creature attacker, final Location loc)
	{
		attacker.teleToLocation(loc);
	}

	static
	{
		MsgOnRecall = new PrintfFormat[] {
				new PrintfFormat("%s. Stop kidding yourself about your own powerlessness!"),
				new PrintfFormat("%s. I'll make you feel what true fear is!"),
				new PrintfFormat("You're really stupid to have challenged me. %s! Get ready!"),
				new PrintfFormat("%s. Do you think that's going to work?!") };
	}
}
