package items;

import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.ShowXMasSeal;
import l2s.gameserver.scripts.ScriptFile;

public class SpecialXMas implements IItemHandler, ScriptFile
{
	private static int[] _itemIds;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(!playable.isPlayer())
			return false;
		final Player activeChar = (Player) playable;
		final int itemId = item.getItemId();
		if(itemId == 5555)
		{
			final ShowXMasSeal SXS = new ShowXMasSeal(5555);
			activeChar.broadcastPacket(new L2GameServerPacket[] { SXS });
		}
		return true;
	}

	@Override
	public int[] getItemIds()
	{
		return SpecialXMas._itemIds;
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
		SpecialXMas._itemIds = new int[] { 5555 };
	}
}
