package npc.model;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.MerchantInstance;
import l2s.gameserver.templates.npc.NpcTemplate;

/**
 * @author Bonux
**/
public final class BufferInstance extends MerchantInstance
{
	private static final long serialVersionUID = 1L;

	public BufferInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void showChatWindow(Player player, int val, Object... replace)
	{
		if(val == 0)
		{
			showChatWindow(player, "scripts/services/NpcBuffer/buffs.htm");
		}
		else
			showChatWindow(player, "scripts/services/NpcBuffer/buffs-" + val + ".htm");
	}
}
