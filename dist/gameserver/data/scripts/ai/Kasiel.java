package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.utils.Location;

public class Kasiel extends DefaultAI
{
	static final Location[] points;
	private int current_point;
	private long wait_timeout;
	private boolean wait;

	public Kasiel(final NpcInstance actor)
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
					case 5:
					{
						wait_timeout = System.currentTimeMillis() + 15000L;
						Functions.npcSay(actor, "The Mother Tree is always so gorgeous!");
						return wait = true;
					}
					case 9:
					{
						wait_timeout = System.currentTimeMillis() + 60000L;
						Functions.npcSay(actor, "Lady Mirabel, may the peace of the lake be with you!");
						return wait = true;
					}
				}
			wait_timeout = 0L;
			wait = false;
			++current_point;
			if(current_point >= Kasiel.points.length)
				current_point = 0;
			this.addTaskMove(Kasiel.points[current_point], true);
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
				new Location(43932, 51096, -2992),
				new Location(43304, 50364, -2992),
				new Location(43041, 49312, -2992),
				new Location(43612, 48322, -2992),
				new Location(44009, 47645, -2992),
				new Location(45309, 47341, -2992),
				new Location(46726, 47762, -2992),
				new Location(47509, 49004, -2992),
				new Location(47443, 50456, -2992),
				new Location(47013, 51287, -2992),
				new Location(46380, 51254, -2900),
				new Location(46389, 51584, -2800),
				new Location(46009, 51593, -2800),
				new Location(46027, 52156, -2800),
				new Location(44692, 52141, -2800),
				new Location(44692, 51595, -2800),
				new Location(44346, 51564, -2850),
				new Location(44357, 51259, -2900),
				new Location(44111, 51252, -2992) };
	}
}
