package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.utils.Location;

public class Jaradine extends DefaultAI
{
	static final Location[] points;
	private int current_point;
	private long wait_timeout;
	private boolean wait;

	public Jaradine(final NpcInstance actor)
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
						Functions.npcSay(actor, "The Mother Tree is slowly dying.");
						return wait = true;
					}
					case 4:
					{
						wait_timeout = System.currentTimeMillis() + 15000L;
						Functions.npcSay(actor, "How can we save the Mother Tree?");
						return wait = true;
					}
					case 6:
					{
						wait_timeout = System.currentTimeMillis() + 60000L;
						return wait = true;
					}
				}
			wait_timeout = 0L;
			wait = false;
			++current_point;
			if(current_point >= Jaradine.points.length)
				current_point = 0;
			this.addTaskMove(Jaradine.points[current_point], true);
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
				new Location(44964, 50568, -3056),
				new Location(44435, 50025, -3056),
				new Location(44399, 49078, -3056),
				new Location(45058, 48437, -3056),
				new Location(46132, 48724, -3056),
				new Location(46452, 49743, -3056),
				new Location(45730, 50590, -3056) };
	}
}
