package ai;

import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.SkillTable;

public class MatchTrief extends MatchFighter
{
	public static final Skill HOLD;

	public MatchTrief(final NpcInstance actor)
	{
		super(actor);
	}

	public void hold()
	{
		final NpcInstance actor = getActor();
		addTaskCast(actor, MatchTrief.HOLD);
		doTask();
	}

	static
	{
		HOLD = SkillTable.getInstance().getInfo(4047, 6);
	}
}
