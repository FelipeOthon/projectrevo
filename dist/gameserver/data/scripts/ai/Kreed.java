package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.utils.Location;

public class Kreed extends DefaultAI
{
	static final Location[] points;
	private int current_point;
	private long wait_timeout;
	private boolean wait;

	public Kreed(final NpcInstance actor)
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
					case 3:
					{
						wait_timeout = System.currentTimeMillis() + 15000L;
						return wait = true;
					}
					case 7:
					{
						wait_timeout = System.currentTimeMillis() + 60000L;
						Functions.npcSay(actor, "The Mass of Darkness will start in a couple of days. Pay more attention to the guard!");
						return wait = true;
					}
				}
			wait_timeout = 0L;
			wait = false;
			++current_point;
			if(current_point >= Kreed.points.length)
				current_point = 0;
			this.addTaskMove(Kreed.points[current_point], true);
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
				new Location(23436, 11164, -3728),
				new Location(20256, 11104, -3728),
				new Location(17330, 13579, -3720),
				new Location(17415, 13044, -3736),
				new Location(20153, 12880, -3728),
				new Location(21621, 13349, -3648),
				new Location(20686, 10432, -3720),
				new Location(22426, 10260, -3648),
				new Location(23436, 11164, -3728) };
	}
}
