package npc.model;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.templates.npc.NpcTemplate;
import quests._1004_IceFairySirra;

public class StewardInstance extends NpcInstance
{
	public StewardInstance(final int objectID, final NpcTemplate template)
	{
		super(objectID, template);
	}

	@Override
	public void spawnMe()
	{
		super.spawnMe();
		_1004_IceFairySirra.spawnTask();
	}

	@Override
	public Clan getClan()
	{
		return null;
	}
}
