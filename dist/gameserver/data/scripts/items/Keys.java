package items;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.DoorTable;

public class Keys implements IItemHandler, ScriptFile
{
	private static int[] _itemIds;

	public Keys()
	{
		final List<Integer> keys = new ArrayList<Integer>();
		for(final DoorInstance door : DoorTable.getInstance().getDoors())
			if(door != null && door.getKey() > 0)
				keys.add(door.getKey());
		Keys._itemIds = new int[keys.size()];
		int i = 0;
		for(final int id : keys)
		{
			Keys._itemIds[i] = id;
			++i;
		}
	}

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		if(item == null || item.getCount() < 1L)
		{
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
			return false;
		}
		final GameObject target = player.getTarget();
		if(target == null || !target.isDoor())
		{
			player.sendPacket(Msg.INCORRECT_TARGET);
			return false;
		}
		final DoorInstance door = (DoorInstance) target;
		if(!player.isInRange(door, 150L))
		{
			player.sendPacket(Msg.YOUR_TARGET_IS_OUT_OF_RANGE);
			return false;
		}
		player.turn(door, 3000);
		if(door.isOpen())
		{
			player.sendPacket(Msg.IT_IS_NOT_LOCKED);
			return false;
		}
		if(door.getKey() <= 0 || item.getItemId() != door.getKey())
		{
			player.sendPacket(Msg.YOU_ARE_UNABLE_TO_UNLOCK_THE_DOOR);
			return false;
		}
		final ItemInstance ri = player.getInventory().destroyItem(item, 1L, true);
		player.sendPacket(SystemMessage.removeItems(ri.getItemId(), 1L));
		player.sendMessage(new CustomMessage("l2s.gameserver.skills.skillclasses.Unlock.Success"));
		door.openMe();
		return true;
	}

	@Override
	public int[] getItemIds()
	{
		return Keys._itemIds;
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
		Keys._itemIds = null;
	}
}
