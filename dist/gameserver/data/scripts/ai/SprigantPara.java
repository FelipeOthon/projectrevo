package ai;

import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.SkillTable;

public class SprigantPara extends DefaultAI
{
	private final Skill SKILL;
	private long _waitTime;
	private static final int TICK_IN_MILISECONDS = 15000;

	public SprigantPara(final NpcInstance actor)
	{
		super(actor);
		SKILL = SkillTable.getInstance().getInfo(5085, 1);
	}

	@Override
	protected boolean thinkActive()
	{
		if(System.currentTimeMillis() > _waitTime)
		{
			final NpcInstance actor = getActor();
			if(actor != null)
				actor.doCast(SKILL, actor, false);
			_waitTime = System.currentTimeMillis() + 15000L;
			return true;
		}
		return false;
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
