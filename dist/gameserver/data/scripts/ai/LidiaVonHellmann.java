package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.SiegeGuardFighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Util;

public class LidiaVonHellmann extends SiegeGuardFighter
{
	private static final Skill DRAIN_SKILL;
	private static final Skill DAMAGE_SKILL;

	public LidiaVonHellmann(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();
		Functions.npcShout(getActor(), "Hmm, those who are not of the bloodline are coming this way to take over the castle?!  Humph!  The bitter grudges of the dead.  You must not make light of their power!", 0);
	}

	@Override
	public void onEvtDead(final Creature killer)
	{
		super.onEvtDead(killer);
		Functions.npcShout(getActor(), "Grarr! For the next 2 minutes or so, the game arena are will be cleaned. Throw any items you don't need to the floor now.", 0);
	}

	@Override
	public void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = getActor();
		super.onEvtAttacked(attacker, skill, damage);
		if(Rnd.chance(0.22))
			addTaskCast(attacker, LidiaVonHellmann.DRAIN_SKILL);
		else if(actor.getCurrentHpPercents() < 20.0 && Rnd.chance(0.22))
			addTaskCast(attacker, LidiaVonHellmann.DRAIN_SKILL);
		if(Util.calculateDistance(actor, attacker, false) > 300.0 && Rnd.chance(0.13))
			addTaskCast(attacker, LidiaVonHellmann.DAMAGE_SKILL);
	}

	static
	{
		DRAIN_SKILL = SkillTable.getInstance().getInfo(4999, 1);
		DAMAGE_SKILL = SkillTable.getInstance().getInfo(4998, 1);
	}
}
