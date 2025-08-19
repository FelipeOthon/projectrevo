package ai;

import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;

public class RainbowEnragedYeti extends Fighter
{
	public RainbowEnragedYeti(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();
		Functions.npcShout(getActor(), "Oooh! Who poured nectar on my head while I was sleeping?", 0);
	}
}
