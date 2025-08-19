package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.utils.Location;

public class Leandro extends DefaultAI
{
	static final Location[] points;
	private int current_point;
	private long wait_timeout;
	private boolean wait;

	public Leandro(final NpcInstance actor)
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
						wait_timeout = System.currentTimeMillis() + 30000L;
						Functions.npcSay(actor, "Where has he gone?");
						return wait = true;
					}
					case 10:
					{
						wait_timeout = System.currentTimeMillis() + 60000L;
						Functions.npcSay(actor, "Have you seen Windawood?");
						return wait = true;
					}
				}
			wait_timeout = 0L;
			wait = false;
			++current_point;
			if(current_point >= Leandro.points.length)
				current_point = 0;
			this.addTaskMove(Leandro.points[current_point], true);
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
				new Location(-82428, 245204, -3720),
				new Location(-82422, 245448, -3704),
				new Location(-82080, 245401, -3720),
				new Location(-82108, 244974, -3720),
				new Location(-83595, 244051, -3728),
				new Location(-83898, 242776, -3728),
				new Location(-85966, 241371, -3728),
				new Location(-86079, 240868, -3720),
				new Location(-86076, 240392, -3712),
				new Location(-86519, 240706, -3712),
				new Location(-86343, 241130, -3720),
				new Location(-86519, 240706, -3712),
				new Location(-86076, 240392, -3712),
				new Location(-86079, 240868, -3720),
				new Location(-85966, 241371, -3728),
				new Location(-83898, 242776, -3728),
				new Location(-83595, 244051, -3728),
				new Location(-82108, 244974, -3720) };
	}
}
