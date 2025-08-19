package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.NpcInstance;

public class Anais extends Fighter
{
	private static Zone _zone;

	public Anais(final NpcInstance actor)
	{
		super(actor);
		Anais._zone = ZoneManager.getInstance().getZoneById(Zone.ZoneType.dummy, 702510, false);
	}

	@Override
	protected boolean maybeMoveToHome()
	{
		final NpcInstance actor = getActor();
		if(actor != null && !Anais._zone.checkIfInZone(actor))
			teleportHome();
		return false;
	}

	@Override
	public boolean canSeeInSilentMove(final Playable target)
	{
		return !target.isSilentMoving() || Rnd.chance(10);
	}
}
