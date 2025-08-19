package npc.model;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.templates.npc.NpcTemplate;

public class CtFInstance extends NpcInstance
{
	private static final long serialVersionUID = 1L;

	public CtFInstance(final int objectID, final NpcTemplate template)
	{
		super(objectID, template);
	}

	@Override
	public Clan getClan()
	{
		return null;
	}
}
