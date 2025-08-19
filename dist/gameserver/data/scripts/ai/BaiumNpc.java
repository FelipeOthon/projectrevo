package ai;

import java.util.ArrayList;

import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.WorldRegion;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.Earthquake;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class BaiumNpc extends DefaultAI
{
	private long _wait_timeout;
	private static final int BAIUM_EARTHQUAKE_TIMEOUT = 900000;

	public BaiumNpc(final NpcInstance actor)
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
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return true;
		if(_wait_timeout < System.currentTimeMillis())
		{
			_wait_timeout = System.currentTimeMillis() + 900000L;
			final L2GameServerPacket eq = new Earthquake(actor.getLoc(), 40, 10);
			for(final WorldRegion region : World.getNeighborsZ(actor.getX(), actor.getY(), -5000, 10000))
				if(region != null && region.getObjectsSize() > 0)
					for(final Player player : region.getPlayersList(new ArrayList<Player>(50), actor.getObjectId(), actor.getReflectionId()))
						if(player != null)
							player.sendPacket(eq);
		}
		return false;
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
