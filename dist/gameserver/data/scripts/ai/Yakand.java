package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.Location;

public class Yakand extends DefaultAI
{
	static final Location[] points;
	private int current_point;
	private long wait_timeout;
	private boolean wait;

	public Yakand(final NpcInstance actor)
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
		if(actor == null || actor.isDead())
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
					case 10:
					{
						wait_timeout = System.currentTimeMillis() + 60000L;
						return wait = true;
					}
				}
			wait_timeout = 0L;
			wait = false;
			++current_point;
			if(current_point >= Yakand.points.length)
				current_point = 0;
			this.addTaskMove(Yakand.points[current_point], true);
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
				new Location(-48820, -113748, -232),
				new Location(-47365, -113618, -232),
				new Location(-45678, -113635, -256),
				new Location(-45168, -114038, -256),
				new Location(-44671, -114185, -256),
				new Location(-44199, -113763, -256),
				new Location(-44312, -113201, -256),
				new Location(-44844, -112958, -256),
				new Location(-45717, -113564, -256),
				new Location(-47370, -113588, -232),
				new Location(-48821, -113496, -232),
				new Location(-48820, -113748, -232) };
	}
}
