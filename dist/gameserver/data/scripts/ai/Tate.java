package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.utils.Location;

public class Tate extends DefaultAI
{
	static final Location[] points;
	private int current_point;
	private long wait_timeout;
	private boolean wait;

	public Tate(final NpcInstance actor)
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
						wait_timeout = System.currentTimeMillis() + 20000L;
						Functions.npcSay(actor, "Care to go a round?");
						return wait = true;
					}
					case 7:
					{
						wait_timeout = System.currentTimeMillis() + 15000L;
						Functions.npcSay(actor, "Have a nice day, Mr. Garita and Mion!");
						return wait = true;
					}
					case 11:
					{
						wait_timeout = System.currentTimeMillis() + 30000L;
						Functions.npcSay(actor, "Mr. Lid, Murdoc, and Airy! How are you doing?");
						return wait = true;
					}
				}
			wait_timeout = 0L;
			wait = false;
			++current_point;
			if(current_point >= Tate.points.length)
				current_point = 0;
			this.addTaskMove(Tate.points[current_point], true);
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
				new Location(115824, -181564, -1352),
				new Location(116048, -181575, -1352),
				new Location(116521, -181476, -1400),
				new Location(116632, -180022, -1168),
				new Location(115355, -178617, -928),
				new Location(115763, -177585, -896),
				new Location(115795, -177361, -880),
				new Location(115877, -177338, -880),
				new Location(115783, -177493, -880),
				new Location(115112, -179836, -880),
				new Location(115102, -180026, -872),
				new Location(114876, -180045, -872),
				new Location(114840, -179694, -872),
				new Location(116322, -179602, -1096),
				new Location(116792, -180386, -1240),
				new Location(116319, -181573, -1376),
				new Location(115824, -181564, -1352) };
	}
}
