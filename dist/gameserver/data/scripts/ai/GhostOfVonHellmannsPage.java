package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.utils.Location;

public class GhostOfVonHellmannsPage extends DefaultAI
{
	static final Location[] points;
	static final String[] NPCtext;
	private int current_point;
	private long wait_timeout;
	private boolean wait;

	public GhostOfVonHellmannsPage(final NpcInstance actor)
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
		if(System.currentTimeMillis() <= wait_timeout || current_point <= -1 && !Rnd.chance(5))
			return randomAnimation();
		if(!wait)
			switch(current_point)
			{
				case 4:
				{
					wait_timeout = System.currentTimeMillis() + 60000L;
					return wait = true;
				}
			}
		wait_timeout = 0L;
		wait = false;
		++current_point;
		if(current_point >= GhostOfVonHellmannsPage.points.length)
		{
			actor.deleteMe();
			return false;
		}
		if(current_point == 0)
			Functions.npcSay(actor, GhostOfVonHellmannsPage.NPCtext[0]);
		else if(current_point == 3)
			Functions.npcSay(actor, GhostOfVonHellmannsPage.NPCtext[1]);
		else if(current_point == 4)
			Functions.npcSay(actor, GhostOfVonHellmannsPage.NPCtext[2]);
		this.addTaskMove(GhostOfVonHellmannsPage.points[current_point], true);
		doTask();
		return true;
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
				new Location(52373, -54296, -3136),
				new Location(52279, -53064, -3161),
				new Location(51909, -51725, -3125),
				new Location(52438, -51240, -3097),
				new Location(52143, -51418, -3085) };
		NPCtext = new String[] { "Follow me...", "This where that here...", "I want to speak to you..." };
	}
}
