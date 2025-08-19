package items;

import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.SocialAction;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.DoorTable;

public class KeyMoS implements IItemHandler, ScriptFile
{
	private static final int[] ITEM_IDS;
	private static long LAST_OPEN;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player activeChar = (Player) playable;
		final GameObject target = activeChar.getTarget();
		if(target == null || !target.isDoor())
		{
			activeChar.sendPacket(new SystemMessage(109));
			activeChar.sendActionFailed();
			return false;
		}
		final DoorInstance door = (DoorInstance) target;
		if(!activeChar.isInRange(door, 150L))
		{
			activeChar.sendPacket(new SystemMessage(22));
			activeChar.sendActionFailed();
			return false;
		}
		activeChar.turn(door, 3000);
		if(KeyMoS.LAST_OPEN + 1800000L > System.currentTimeMillis())
		{
			activeChar.sendMessage("You can not use the key right now.");
			activeChar.sendActionFailed();
			return false;
		}
		if(door.getDoorId() != 23150003 && door.getDoorId() != 23150004)
		{
			activeChar.sendMessage("You can not use the key in these terms.");
			activeChar.sendActionFailed();
			return false;
		}
		if(door.isOpen())
		{
			activeChar.sendPacket(new SystemMessage(321));
			activeChar.sendActionFailed();
			return false;
		}
		DoorTable.getInstance().getDoor(23150003).openMe();
		DoorTable.getInstance().getDoor(23150004).openMe();
		KeyMoS.LAST_OPEN = System.currentTimeMillis() + 60000L;
		activeChar.broadcastPacket(new L2GameServerPacket[] { new SocialAction(activeChar.getObjectId(), 3) });
		final ItemInstance ri = activeChar.getInventory().destroyItem(item, 1L, false);
		activeChar.sendPacket(SystemMessage.removeItems(ri.getItemId(), 1L));
		activeChar.sendMessage("Doors will be closed in 60 seconds!");
		return true;
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

	@Override
	public int[] getItemIds()
	{
		return KeyMoS.ITEM_IDS;
	}

	static
	{
		ITEM_IDS = new int[] { 8056 };
		KeyMoS.LAST_OPEN = 0L;
	}
}
