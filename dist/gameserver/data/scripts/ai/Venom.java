package ai;

import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.utils.NpcUtils;

public class Venom extends Fighter
{
	public Venom(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();
		Functions.npcShout(getActor(), "Who dares to covet the throne of our castle!  Leave immediately or you will pay the price of your audacity with your very own blood!", 0);
	}

	@Override
	public void onEvtDead(final Creature killer)
	{
		super.onEvtDead(killer);
		Functions.npcShout(getActor(), "It's not over yet...  It won't be... over... like this... Never...", 0);
		NpcUtils.spawnSingle(29055, 12589, -49044, -3008, 120000L);
	}
}
