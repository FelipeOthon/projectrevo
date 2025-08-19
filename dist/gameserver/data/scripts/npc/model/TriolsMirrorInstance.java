package npc.model;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.templates.npc.NpcTemplate;

public class TriolsMirrorInstance extends NpcInstance
{
	public TriolsMirrorInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void showChatWindow(final Player player, final int val, final Object... arg)
	{
		if(getNpcId() == 32040)
			player.teleToLocation(-12766, -35840, -10856);
		else if(getNpcId() == 32039)
			player.teleToLocation(35079, -49758, -760);
	}

	@Override
	public Clan getClan()
	{
		return null;
	}
}
