package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.utils.Location;

public class Alhena extends DefaultAI
{
	static final Location[] points;
	private int current_point;
	private long wait_timeout;
	private boolean wait;

	public Alhena(final NpcInstance actor)
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
					case 4:
					{
						wait_timeout = System.currentTimeMillis() + 15000L;
						Functions.npcSay(actor, "You're a hard worker, Rayla!");
						return wait = true;
					}
					case 9:
					{
						wait_timeout = System.currentTimeMillis() + 15000L;
						Functions.npcSay(actor, "You're a hard worker!");
						return wait = true;
					}
					case 12:
					{
						wait_timeout = System.currentTimeMillis() + 60000L;
						return wait = true;
					}
				}
			wait_timeout = 0L;
			wait = false;
			++current_point;
			if(current_point >= Alhena.points.length)
				current_point = 0;
			this.addTaskMove(Alhena.points[current_point], true);
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
				new Location(10968, 14620, -4248),
				new Location(11308, 15847, -4584),
				new Location(12119, 16441, -4584),
				new Location(15104, 15661, -4376),
				new Location(15265, 16288, -4376),
				new Location(12292, 16934, -4584),
				new Location(11777, 17669, -4584),
				new Location(11229, 17650, -4576),
				new Location(10641, 17282, -4584),
				new Location(7683, 18034, -4376),
				new Location(10551, 16775, -4584),
				new Location(11004, 15942, -4584),
				new Location(10827, 14757, -4248),
				new Location(10968, 14620, -4248) };
	}
}
