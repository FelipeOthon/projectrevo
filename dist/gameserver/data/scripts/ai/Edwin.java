package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.Location;

public class Edwin extends DefaultAI
{
	static final Location[] points;
	private int current_point;
	private long wait_timeout;
	private boolean wait;

	public Edwin(final NpcInstance actor)
	{
		super(actor);
		current_point = -1;
		wait_timeout = 0L;
		wait = false;
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if(actor.isDead())
			return true;
		if(_def_think)
		{
			doTask();
			return true;
		}
		if(System.currentTimeMillis() > wait_timeout && (current_point > -1 || Rnd.chance(5)))
		{
			if(!wait)
				switch(current_point)
				{
					case 0:
					{
						wait_timeout = System.currentTimeMillis() + 10000L;
						return wait = true;
					}
					case 8:
					{
						wait_timeout = System.currentTimeMillis() + 10000L;
						return wait = true;
					}
				}
			wait_timeout = 0L;
			wait = false;
			++current_point;
			if(current_point >= Edwin.points.length)
				current_point = 0;
			this.addTaskMove(Edwin.points[current_point], true);
			doTask();
			return true;
		}
		return randomAnimation();
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{}

	@Override
	protected void onEvtAggression(final Creature target, final int aggro)
	{}

	static
	{
		points = new Location[] {
				new Location(89991, -144601, -1467),
				new Location(90538, -143470, -1467),
				new Location(90491, -142848, -1467),
				new Location(89563, -141455, -1467),
				new Location(89138, -140621, -1467),
				new Location(87459, -140192, -1467),
				new Location(85625, -140699, -1467),
				new Location(84538, -142382, -1467),
				new Location(84527, -143913, -1467),
				new Location(84538, -142382, -1467),
				new Location(85625, -140699, -1467),
				new Location(87459, -140192, -1467),
				new Location(89138, -140621, -1467),
				new Location(89563, -141455, -1467),
				new Location(90491, -142848, -1467),
				new Location(90538, -143470, -1467),
				new Location(89991, -144601, -1467) };
	}
}
