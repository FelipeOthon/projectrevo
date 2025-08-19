package items;

import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.RadarControl;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Location;

public class Book implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(!playable.isPlayer())
			return false;
		final Player activeChar = (Player) playable;
		Functions.show("help/" + item.getItemId() + ".htm", activeChar);
		if(item.getItemId() == 7063)
			activeChar.sendPacket(new RadarControl(0, 2, new Location(51995, -51265, -3104)));
		activeChar.sendActionFailed();
		return true;
	}

	@Override
	public int[] getItemIds()
	{
		return Book._itemIds;
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
		_itemIds = new int[] {
				5588,
				6317,
				7561,
				7063,
				7064,
				7065,
				7066,
				7082,
				7083,
				7084,
				7085,
				7086,
				7087,
				7088,
				7089,
				7090,
				7091,
				7092,
				7093,
				7094,
				7095,
				7096,
				7097,
				7098,
				7099,
				7100,
				7101,
				7102,
				7103,
				7104,
				7105,
				7106,
				7107,
				7108,
				7109,
				7110,
				7111,
				7112 };
	}
}
