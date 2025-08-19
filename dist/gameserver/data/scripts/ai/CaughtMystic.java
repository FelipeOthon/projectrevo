package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Mystic;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;

public class CaughtMystic extends Mystic
{
	private static final int TIME_TO_LIVE = 60000;
	private final long TIME_TO_DIE;

	public CaughtMystic(final NpcInstance actor)
	{
		super(actor);
		TIME_TO_DIE = System.currentTimeMillis() + 60000L;
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		if(Rnd.chance(75))
			Functions.npcSayCustomMessage(getActor(), "scripts.ai.CaughtMob.spawn", new Object[0]);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		if(Rnd.chance(75))
			Functions.npcSayCustomMessage(getActor(), "scripts.ai.CaughtMob.death", new Object[0]);
		super.onEvtDead(killer);
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if(actor != null && System.currentTimeMillis() >= TIME_TO_DIE)
		{
			actor.deleteMe();
			return false;
		}
		return super.thinkActive();
	}
}
