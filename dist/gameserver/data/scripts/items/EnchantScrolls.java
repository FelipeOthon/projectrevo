package items;

import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.ChooseInventoryItem;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class EnchantScrolls implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds;
	private static final SystemMessage SELECT_ITEM_TO_ENCHANT;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		if(player.getEnchantScroll() != null)
		{
			player.sendActionFailed();
			return false;
		}
		if(player.isInOlympiadMode())
		{
			player.sendPacket(new SystemMessage(1508));
			return false;
		}
		player.setEnchantScroll(item);
		player.sendPacket(new IBroadcastPacket[] { EnchantScrolls.SELECT_ITEM_TO_ENCHANT, new ChooseInventoryItem(item.getItemId()) });
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return EnchantScrolls._itemIds;
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
				729,
				730,
				731,
				732,
				947,
				948,
				949,
				950,
				951,
				952,
				953,
				954,
				955,
				956,
				957,
				958,
				959,
				960,
				961,
				962,
				6569,
				6570,
				6571,
				6572,
				6573,
				6574,
				6575,
				6576,
				6577,
				6578 };
		SELECT_ITEM_TO_ENCHANT = new SystemMessage(303);
	}
}
