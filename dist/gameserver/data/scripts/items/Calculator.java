package items;

import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.ShowCalculator;
import l2s.gameserver.scripts.ScriptFile;

/**
 * @author Bonux
**/
public class Calculator implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds = { 4393 };

	@Override
	public boolean useItem(Playable playable, ItemInstance item, Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;

		playable.sendPacket(new ShowCalculator(item.getItemId()));
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return _itemIds;
	}

	@Override
	public void onLoad()
	{
		ItemHandler.getInstance().registerItemHandler(this);
	}

	@Override
	public void onReload()
	{
		//
	}

	@Override
	public void onShutdown()
	{
		//
	}
}
