package ai;

import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.ItemTable;

public class BehemothDragon extends Fighter
{
	public BehemothDragon(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return;
		if(killer != null)
		{
			final Player player = killer.getPlayer();
			if(player != null)
				for(int i = 0; i < 10; ++i)
				{
					ItemTable.getInstance().createItem(8604).dropToTheGround(player, actor);
					ItemTable.getInstance().createItem(8601).dropToTheGround(player, actor);
				}
		}
		super.onEvtDead(killer);
	}
}
