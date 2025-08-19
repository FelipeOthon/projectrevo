package events.TheFlowOfTheHorror;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.Location;

public class MonstersAI extends Fighter
{
	private ArrayList<Location> _points;
	private int current_point;

	public void setPoints(final ArrayList<Location> points)
	{
		_points = points;
	}

	public MonstersAI(final NpcInstance actor)
	{
		super(actor);
		_points = new ArrayList<Location>();
		current_point = -1;
		AI_TASK_ATTACK_DELAY = 500L;
		MAX_PURSUE_RANGE = 30000;
	}

	@Override
	public int getMaxAttackTimeout()
	{
		return Integer.MAX_VALUE;
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
		if(current_point <= -1 && !Rnd.chance(5))
			return randomAnimation();
		if(current_point < _points.size() - 1)
		{
			++current_point;
			actor.setRunning();
			clearTasks();
			this.addTaskMove(_points.get(current_point), true);
			doTask();
			return true;
		}
		List<NpcInstance> npcs = GameObjectsStorage.getNpcs(true, 30754);
		if(!npcs.isEmpty())
		{
			clearTasks();
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, npcs.get(0));
			return true;
		}
		return true;
	}
}
