package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.utils.Location;

public class Toma extends DefaultAI
{
	private Location[] _points;
	private static long TELEPORT_PERIOD;
	private long _lastTeleport;

	public Toma(final NpcInstance actor)
	{
		super(actor);
		(_points = new Location[3])[0] = new Location(151680, -174891, -1807, 41400);
		_points[1] = new Location(154153, -220105, -3402);
		_points[2] = new Location(178834, -184336, -352);
		_lastTeleport = System.currentTimeMillis();
	}

	@Override
	protected boolean thinkActive()
	{
		if(System.currentTimeMillis() - _lastTeleport < Toma.TELEPORT_PERIOD)
			return false;
		for(int i = 0; i < _points.length; ++i)
		{
			final Location loc = _points[Rnd.get(_points.length)];
			final NpcInstance _thisActor = getActor();
			if(!_thisActor.getLoc().equals(loc))
			{
				_thisActor.broadcastPacketToOthers(new L2GameServerPacket[] { new MagicSkillUse(_thisActor, _thisActor, 4671, 1, 500, 0L) });
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

	static
	{
		Toma.TELEPORT_PERIOD = 1800000L;
	}
}
