package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.utils.Location;

public class Remy extends DefaultAI
{
	static final Location[] points;
	private int current_point;
	private long wait_timeout;
	private boolean wait;

	public Remy(final NpcInstance actor)
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
					case 0:
					{
						wait_timeout = System.currentTimeMillis() + 15000L;
						Functions.npcSay(actor, "A delivery for Mr. Lector? Very good!");
						return wait = true;
					}
					case 3:
					{
						wait_timeout = System.currentTimeMillis() + 15000L;
						Functions.npcSay(actor, "I need a break!");
						return wait = true;
					}
					case 7:
					{
						wait_timeout = System.currentTimeMillis() + 15000L;
						Functions.npcSay(actor, "Hello, Mr. Lector! Long time no see, Mr. Jackson!");
						return wait = true;
					}
					case 12:
					{
						wait_timeout = System.currentTimeMillis() + 15000L;
						Functions.npcSay(actor, "Lulu!");
						return wait = true;
					}
					case 15:
					{
						wait_timeout = System.currentTimeMillis() + 60000L;
						return wait = true;
					}
				}
			wait_timeout = 0L;
			wait = false;
			++current_point;
			if(current_point >= Remy.points.length)
				current_point = 0;
			actor.setRunning();
			this.addTaskMove(Remy.points[current_point], true);
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
				new Location(-81926, 243894, -3712),
				new Location(-82134, 243600, -3728),
				new Location(-83165, 243987, -3728),
				new Location(-84501, 243245, -3728),
				new Location(-85100, 243285, -3728),
				new Location(-86152, 242898, -3728),
				new Location(-86288, 242962, -3720),
				new Location(-86348, 243223, -3720),
				new Location(-86522, 242762, -3720),
				new Location(-86500, 242615, -3728),
				new Location(-86123, 241606, -3728),
				new Location(-85167, 240589, -3728),
				new Location(-84323, 241245, -3728),
				new Location(-83215, 241170, -3728),
				new Location(-82364, 242944, -3728),
				new Location(-81674, 243391, -3712),
				new Location(-81926, 243894, -3712) };
	}
}
