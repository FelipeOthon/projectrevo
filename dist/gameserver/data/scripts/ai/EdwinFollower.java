package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;

public class EdwinFollower extends DefaultAI
{
	private static final int EDWIN_ID = 32072;
	private static final int DRIFT_DISTANCE = 200;

	private long _wait_timeout;
	private int _edwin;

	public EdwinFollower(final NpcInstance actor)
	{
		super(actor);
		_wait_timeout = 0L;
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	@Override
	protected boolean randomAnimation()
	{
		return false;
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if(actor == null)
			return false;

		GameObject edwin = GameObjectsStorage.findObject(_edwin);
		if(edwin == null)
		{
			if(System.currentTimeMillis() > _wait_timeout)
			{
				_wait_timeout = System.currentTimeMillis() + 15000L;
				for(final NpcInstance npc : World.getAroundNpc(actor))
					if(npc.getNpcId() == 32072)
					{
						_edwin = npc.getObjectId();
						return true;
					}
			}
		}
		else if(!actor.isMoving)
		{
			int x = edwin.getX() + Rnd.get(400) - 200;
			int y = edwin.getY() + Rnd.get(400) - 200;
			int z = edwin.getZ();
			actor.setRunning();
			actor.moveToLocation(x, y, z, 0, true);
			return true;
		}
		return false;
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{}

	@Override
	protected void onEvtAggression(final Creature target, final int aggro)
	{}
}
