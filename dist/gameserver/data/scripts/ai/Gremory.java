package ai;

import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.utils.Location;

public class Gremory extends DefaultAI
{
	private static final Location[] points;
	private static final long TELEPORT_PERIOD = 1800000L;
	private long _lastTeleport;

	public Gremory(final NpcInstance actor)
	{
		super(actor);
		_lastTeleport = System.currentTimeMillis();
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if(actor == null || System.currentTimeMillis() - _lastTeleport < 1800000L)
			return false;
		for(int i = 0; i < Gremory.points.length; ++i)
		{
			final Location loc = Gremory.points[i];
			if(actor.getX() != loc.x)
			{
				actor.broadcastPacketToOthers(new L2GameServerPacket[] { new MagicSkillUse(actor, actor, 4671, 1, 500, 0L) });
				ThreadPoolManager.getInstance().schedule(new DefaultAI.Teleport(loc), 500L);
				_lastTeleport = System.currentTimeMillis();
				break;
			}
		}
		return true;
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	@Override
	protected void thinkAttack()
	{}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}

	static
	{
		points = new Location[] { new Location(114629, -70818, -544), new Location(110456, -82232, -1615) };
	}
}
