package items;

import java.util.Collection;

import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.dao.CastleHiredGuardDAO;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.templates.MerchantGuard;
import l2s.gameserver.utils.Util;

public class MercTicket implements IItemHandler, ScriptFile
{
	private int[] _itemIds;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		if(player.getClan() == null || player.getClan().getHasCastle() == 0 || (player.getClanPrivileges() & 0x200000) != 0x200000)
		{
			player.sendPacket(new IBroadcastPacket[] { Msg.YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_POSITION_MERCENARIES, Msg.ActionFail });
			return false;
		}
		final Castle castle = player.getCastle();
		final MerchantGuard guard = castle.getMerchantGuard(item.getItemId());
		if(guard == null || !castle.checkIfInZone(player.getLoc()) || player.isActionBlocked("drop_merchant_guard"))
		{
			player.sendPacket(new IBroadcastPacket[] { Msg.YOU_CANNOT_POSITION_MERCENARIES_HERE, Msg.ActionFail });
			return false;
		}
		SiegeEvent<?, ?> siegeEvent = castle.getSiegeEvent();
		if(siegeEvent != null && siegeEvent.isInProgress() || !guard.isValidSSQPeriod())
		{
			player.sendPacket(new IBroadcastPacket[] {
					Msg.A_MERCENARY_CAN_BE_ASSIGNED_TO_A_POSITION_FROM_THE_BEGINNING_OF_THE_SEAL_VALIDATION_PERIOD_UNTIL_THE_TIME_WHEN_A_SIEGE_STARTS,
					Msg.ActionFail });
			return false;
		}
		int countOfGuard = 0;
		for(final ItemInstance $item : castle.getSpawnMerchantTickets())
		{
			if(Util.getDistance($item.getLoc(), player.getLoc()) < 200.0)
			{
				player.sendPacket(new IBroadcastPacket[] {
						Msg.POSITIONING_CANNOT_BE_DONE_HERE_BECAUSE_THE_DISTANCE_BETWEEN_MERCENARIES_IS_TOO_SHORT,
						Msg.ActionFail });
				return false;
			}
			if($item.getItemId() != guard.getItemId())
				continue;
			++countOfGuard;
		}
		if(countOfGuard >= guard.getMax())
		{
			player.sendPacket(new IBroadcastPacket[] { Msg.THIS_MERCENARY_CANNOT_BE_POSITIONED_ANYMORE, Msg.ActionFail });
			return false;
		}
		final ItemInstance dropticket = new ItemInstance(IdFactory.getInstance().getNextId(), item.getItemId());
		dropticket.setLocation(ItemInstance.ItemLocation.INVENTORY);
		dropticket.dropMe((Creature) null, player.getLoc());
		dropticket.setDropTime(0L);
		castle.getSpawnMerchantTickets().add(dropticket);
		CastleHiredGuardDAO.getInstance().insert(castle, dropticket.getItemId(), dropticket.getLoc());
		player.getInventory().destroyItem(item, 1L, false);
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
		final IntSet set = new HashIntSet();
		final Collection<Castle> castles = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		for(final Castle c : castles)
			set.addAll(c.getMerchantGuards().keySet());
		_itemIds = set.toArray();
		for(final int id : _itemIds)
			ItemTable.getInstance().getTemplate(id).setIsMercTicket(true);
		ItemHandler.getInstance().registerItemHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
