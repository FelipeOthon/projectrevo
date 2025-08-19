package events.tournament;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.utils.Location;

public class Tournament_ai extends DefaultAI
{
	private Location[] points;
	private int current_point;
	private long wait_timeout;
	private boolean wait;

	public Tournament_ai(final NpcInstance actor)
	{
		super(actor);
		points = new Location[9];
		current_point = -1;
		wait_timeout = 0L;
		wait = false;
		points[0] = new Location(82545, 148600, -3505, -3395);
		points[1] = new Location(82410, 148277, -3505, -3395);
		points[2] = new Location(82101, 148117, -3505, -3395);
		points[3] = new Location(81673, 148070, -3505, -3395);
		points[4] = new Location(81453, 148378, -3505, -3395);
		points[5] = new Location(81432, 148792, -3505, -3395);
		points[6] = new Location(81702, 149114, -3505, -3395);
		points[7] = new Location(82115, 149111, -3505, -3395);
		points[8] = new Location(82440, 148882, -3505, -3395);
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance _thisActor = getActor();
		if(_thisActor.isDead())
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
						wait_timeout = System.currentTimeMillis() + 60000L;
						Functions.npcShout(_thisActor, "\u0420\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u044f \u043d\u0430 \u0422\u0443\u0440\u043d\u0438\u0440 !!!!", 0);
						return wait = true;
					}
					case 2:
					{
						wait_timeout = System.currentTimeMillis() + 60000L;
						Functions.npcShout(_thisActor, "\u0420\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u044f \u043d\u0430 \u0422\u0443\u0440\u043d\u0438\u0440 !!!!", 0);
						return wait = true;
					}
					case 4:
					{
						wait_timeout = System.currentTimeMillis() + 60000L;
						Functions.npcShout(_thisActor, "\u0420\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u044f \u043d\u0430 \u0422\u0443\u0440\u043d\u0438\u0440 !!!", 0);
						return wait = true;
					}
					case 6:
					{
						wait_timeout = System.currentTimeMillis() + 60000L;
						Functions.npcShout(_thisActor, "\u0420\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u044f \u043d\u0430 \u0422\u0443\u0440\u043d\u0438\u0440 !!!!", 0);
						return wait = true;
					}
					case 8:
					{
						wait_timeout = System.currentTimeMillis() + 60000L;
						Functions.npcShout(_thisActor, "\u0420\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u044f \u043d\u0430 \u0422\u0443\u0440\u043d\u0438\u0440 !!!!", 0);
						return wait = true;
					}
				}
			wait_timeout = 0L;
			wait = false;
			++current_point;
			if(current_point >= points.length)
				current_point = 0;
			this.addTaskMove(points[current_point], true);
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
}
