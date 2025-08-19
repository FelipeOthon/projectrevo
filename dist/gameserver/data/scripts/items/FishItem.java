package items;

import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.reward.DropData;
import l2s.gameserver.network.l2.s2c.ItemList;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.FishTable;
import l2s.gameserver.tables.ItemTable;

public class FishItem implements IItemHandler, ScriptFile
{
	@Override
	public synchronized boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		if(player.getWeightPenalty() >= 3 || player.getInventory().getSize() > player.getInventoryLimit() - 10)
		{
			player.sendPacket(new SystemMessage(129));
			return false;
		}
		final List<DropData> rewards = FishTable.getInstance().getFishReward(item.getItemId());
		int count = 0;
		player.getInventory().destroyItem(item, 1L, false);
		for(final DropData d : rewards)
			if(Rnd.chance(d.getChance()))
			{
				giveItems(player, d.getItemId(), (int) (Rnd.get(d.getMinDrop(), d.getMaxDrop()) * Config.RATE_FISH_DROP_COUNT));
				++count;
			}
		if(count == 0)
			player.sendPacket(new SystemMessage(1669));
		else
			player.sendPacket(new ItemList(player, false));
		return true;
	}

	public void giveItems(final Player activeChar, final int itemId, final long count)
	{
		final ItemInstance item = ItemTable.getInstance().createItem(itemId);
		item.setCount(count);
		activeChar.getInventory().addItem(item);
		if(count > 1L)
			activeChar.sendPacket(new SystemMessage(53).addItemName(itemId).addNumber((int) count));
		else
			activeChar.sendPacket(new SystemMessage(54).addItemName(itemId));
	}

	@Override
	public int[] getItemIds()
	{
		return FishTable.getInstance().getFishIds();
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
}
