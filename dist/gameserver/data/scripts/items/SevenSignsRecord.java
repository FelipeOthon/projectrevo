package items;

import l2s.gameserver.Config;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.SSQStatus;
import l2s.gameserver.scripts.ScriptFile;

public class SevenSignsRecord implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(!Config.ALLOW_SEVEN_SIGNS || playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		player.sendPacket(new SSQStatus(player, 1));
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return SevenSignsRecord._itemIds;
	}

	@Override
	public void onLoad()
	{
		ItemHandler.getInstance().registerItemHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		_itemIds = new int[] { 5707 };
	}
}
