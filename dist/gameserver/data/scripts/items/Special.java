package items;

import org.apache.commons.lang3.ArrayUtils;

import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class Special implements IItemHandler, ScriptFile
{
	private static final int[] ITEM_IDS = new int[] { 8060, 8556 };

	@Override
	public int[] getItemIds()
	{
		return ITEM_IDS;
	}

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		final int itemId = item.getItemId();
		switch(itemId)
		{
			case 8060:
			{
				use8060(player);
				break;
			}
			case 8556:
			{
				use8556(player);
				break;
			}
		}
		return true;
	}

	private void use8060(final Player player)
	{
		if(player.getInventory().findItemByItemId(8058) != null)
		{
			Functions.removeItem(player, 8058, 1L);
			Functions.addItem(player, 8059, 1L);
		}
	}

	private boolean use8556(final Player player)
	{
		final int[] npcs = { 29048, 29049 };
		final GameObject t = player.getTarget();
		if(t == null || !t.isNpc() || !ArrayUtils.contains(npcs, ((NpcInstance) t).getNpcId()))
		{
			player.sendPacket(new SystemMessage(113).addItemName(8556));
			return false;
		}
		if(player.getDistance(t) > 200.0)
		{
			player.sendPacket(new SystemMessage(22));
			return false;
		}
		player.sendPacket(new SystemMessage(46).addItemName(8556));
		Functions.removeItem(player, 8556, 1L);
		((NpcInstance) t).doDie(player);
		return true;
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
