package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.SkillTable;

public class MatchLeader extends MatchFighter
{
	public static final Skill ATTACK_SKILL;

	public MatchLeader(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public void onEvtAttacked(final Creature attacker, final Skill skill, final int dam)
	{
		super.onEvtAttacked(attacker, skill, dam);
		if(Rnd.chance(10))
			addTaskCast(attacker, MatchLeader.ATTACK_SKILL);
	}

	static
	{
		ATTACK_SKILL = SkillTable.getInstance().getInfo(4077, 6);
	}
}
