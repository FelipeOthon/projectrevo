package items;

import l2s.commons.util.Rnd;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.Dice;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class RollingDice implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds;
	static final SystemMessage YOU_MAY_NOT_THROW_THE_DICE_AT_THIS_TIMETRY_AGAIN_LATER;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		final int itemId = item.getItemId();
		if(player.isInOlympiadMode())
		{
			player.sendPacket(Msg.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
			return false;
		}
		if(player.isSitting())
		{
			player.sendPacket(Msg.YOU_CANNOT_MOVE_WHILE_SITTING);
			return false;
		}
		if(itemId == 4625 || itemId == 4626 || itemId == 4627 || itemId == 4628)
		{
			final int number = rollDice(player);
			if(number == 0)
			{
				player.sendPacket(RollingDice.YOU_MAY_NOT_THROW_THE_DICE_AT_THIS_TIMETRY_AGAIN_LATER);
				return false;
			}
			player.broadcastPacket(new L2GameServerPacket[] {
					new Dice(player.getObjectId(), itemId, number, player.getX() - 30, player.getY() - 30, player.getZ()),
					new SystemMessage(834).addString(player.getName()).addNumber(number) });
		}
		return true;
	}

	private int rollDice(final Player player)
	{
		if(System.currentTimeMillis() <= player.lastDiceThrown)
			return 0;
		player.lastDiceThrown = System.currentTimeMillis() + 4000L;
		return Rnd.get(1, 6);
	}

	@Override
	public final int[] getItemIds()
	{
		return RollingDice._itemIds;
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
		_itemIds = new int[] { 4625, 4626, 4627, 4628 };
		YOU_MAY_NOT_THROW_THE_DICE_AT_THIS_TIMETRY_AGAIN_LATER = new SystemMessage(835);
	}
}
