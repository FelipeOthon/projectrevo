package ai;

import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.SkillTable;

public class MatchCleric extends MatchFighter
{
	public static final Skill HEAL;

	public MatchCleric(final NpcInstance actor)
	{
		super(actor);
	}

	public void heal()
	{
		final NpcInstance actor = getActor();
		addTaskCast(actor, MatchCleric.HEAL);
		doTask();
	}

	static
	{
		HEAL = SkillTable.getInstance().getInfo(4065, 6);
	}
}
