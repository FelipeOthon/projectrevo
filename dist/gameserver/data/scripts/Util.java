import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import org.apache.commons.lang3.ArrayUtils;

import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.instancemanager.TownManager;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.TeleportLocation;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.entity.SevenSigns;
import l2s.gameserver.model.entity.Town;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.entity.residence.Residence;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.instances.ResidenceManager;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.TeleportTable;
import l2s.gameserver.utils.Location;

public class Util extends Functions implements ScriptFile
{
	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public void GK(String[] param)
	{
		if(param.length < 3)
			throw new IllegalArgumentException();

		final Player player = getSelf();
		if(player == null)
			return;

		final NpcInstance npc = player.getLastNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
			return;

		final int x = Integer.parseInt(param[0]);
		final int y = Integer.parseInt(param[1]);
		final int z = Integer.parseInt(param[2]);
		int price = param.length > 3 ? Integer.parseInt(param[3]) : 0;
		int item_id = param.length > 4 ? Integer.parseInt(param[4]) : 0;

		if(player.getKarma() > 0 && !ArrayUtils.contains(Config.ALT_GAME_KARMA_NPC, npc.getNpcId()))
		{
			player.sendMessage(new CustomMessage("l2s.TeleportKarma"));
			return;
		}

		if(player.getMountType() == 2)
		{
			player.sendMessage(new CustomMessage("l2s.TeleportWyvern"));
			return;
		}

		if(!Config.GATEKEEPER_TELEPORT_SIEGE)
		{
			final Town town = TownManager.getInstance().getClosestTown(x, y);
			if(town != null)
			{
				final Castle castle = town.getCastle();
				if(castle != null) {
					SiegeEvent<?, ?> siegeEvent = castle.getSiegeEvent();
					if(siegeEvent != null && siegeEvent.isInProgress()) {
						player.sendPacket(new SystemMessage(707));
						return;
					}
				}
			}
		}

		if(price > 0)
		{
			if(item_id == 57)
			{
				if(player.getAdena() < price)
				{
					player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					return;
				}
				player.reduceAdena(price, true);
			}
			else
			{
				ItemInstance item = player.getInventory().getItemByItemId(item_id);
				if(item == null || item.getIntegerLimitedCount() < price)
				{
					player.sendPacket(new SystemMessage(701));
					return;
				}

				player.getInventory().destroyItem(item, price, true);

				if(price > 1)
					player.sendPacket(new SystemMessage(301).addItemName(item_id).addNumber(price));
				else
					player.sendPacket(new SystemMessage(302).addItemName(item_id));
			}
		}

		player.teleToLocation(Location.findAroundPosition(x, y, z, 0, 70, player.getGeoIndex()));
	}

	public void SGK(final String[] param)
	{
		if(param.length < 3)
			throw new IllegalArgumentException();
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = player.getLastNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
			return;
		final TeleportTable.TeleportLocation[] list = TeleportTable.getInstance().getTeleportLocationList(npc.getNpcId(), player.teleList);
		player.teleList = 1;
		if(list == null)
			return;
		final int x = Integer.parseInt(param[0]);
		final int y = Integer.parseInt(param[1]);
		final int z = Integer.parseInt(param[2]);
		final String c = x + " " + y + " " + z;
		boolean ok = false;
		int id = 0;
		int price = 0;
		int random = 0;
		for(final TeleportTable.TeleportLocation tl : list)
			if(tl._target.equals(c))
			{
				id = tl._item.getItemId();
				price = tl._price;
				random = tl._random;
				ok = true;
				break;
			}
		if(!ok)
			return;
		if(player.getKarma() > 0 && !ArrayUtils.contains(Config.ALT_GAME_KARMA_NPC, npc.getNpcId()))
		{
			player.sendMessage(new CustomMessage("l2s.TeleportKarma"));
			return;
		}
		if(player.getMountType() == 2)
		{
			player.sendMessage(new CustomMessage("l2s.TeleportWyvern"));
			return;
		}
		if(id > 2)
		{
			if(npc.getNpcId() == 30483 && player.getLevel() >= Config.CRUMA_GATEKEEPER_LVL)
			{
				show("teleporter/30483-no.htm", player);
				return;
			}
			if(!Config.GATEKEEPER_TELEPORT_SIEGE)
			{
				final Town town = TownManager.getInstance().getClosestTown(x, y);
				if(town != null)
				{
					final Castle castle = town.getCastle();
					if(castle != null) {
						SiegeEvent<?, ?> siegeEvent = castle.getSiegeEvent();
						if(siegeEvent != null && siegeEvent.isInProgress()){
							player.sendPacket(new SystemMessage(707));
							return;
						}
					}
				}
			}
			if(price > 0)
				if(id == 57)
				{
					price *= (int) player.teleMod;
					player.teleMod = 1.0f;
					if(player.getAdena() < price)
					{
						player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
						return;
					}
					player.reduceAdena(price, true);
				}
				else
				{
					final ItemInstance ii = player.getInventory().getItemByItemId(id);
					if(ii == null || ii.getIntegerLimitedCount() < price)
					{
						player.sendPacket(new SystemMessage(701));
						return;
					}
					player.getInventory().destroyItem(ii, price, true);
					if(price > 1)
						player.sendPacket(new SystemMessage(301).addItemName(id).addNumber(price));
					else
						player.sendPacket(new SystemMessage(302).addItemName(id));
				}
			final Location pos = random > 0 ? Location.findAroundPosition(x, y, z, 0, random, player.getGeoIndex()) : new Location(x, y, z);
			player.teleToLocation(pos);
		}
		else
		{
			if(Config.ALLOW_SEVEN_SIGNS)
			{
				if(id > 0)
				{
					final int player_cabal = SevenSigns.getInstance().getPlayerCabal(player);
					final int period = SevenSigns.getInstance().getCurrentPeriod();
					if(period == 1 && player_cabal == 0)
					{
						player.sendPacket(new SystemMessage(1303));
						return;
					}
					final int winner;
					if(period == 3 && (winner = SevenSigns.getInstance().getCabalHighestScore()) != 0)
					{
						if(winner != player_cabal)
							return;
						if(id == 1 && SevenSigns.getInstance().getSealOwner(1) != player_cabal)
							return;
						if(id == 2 && SevenSigns.getInstance().getSealOwner(2) != player_cabal)
							return;
					}
					player.setVar("isIn7sDungeon", "1");
				}
				else
					player.unsetVar("isIn7sDungeon");
			}
			player.teleToLocation(x, y, z);
		}
	}

	public void RGK(final String[] param)
	{
		if(param.length < 3)
			throw new IllegalArgumentException();
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = player.getLastNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || !(npc instanceof ResidenceManager))
			return;
		final Clan clan = player.getClan();
		if(clan == null)
			return;
		if (player.getClanHall() == null)
			return;
		final Residence r = ResidenceHolder.getInstance().getResidence(player.getClanHall().getId());
		if(r == null || clan.getHasCastle() != r.getId() && clan.getHasHideout() != r.getId())
			return;
		if(!r.isFunctionActive(1))
			return;
		final int x = Integer.parseInt(param[0]);
		final int y = Integer.parseInt(param[1]);
		final int z = Integer.parseInt(param[2]);
		boolean ok = false;
		int price = 0;
		for(final TeleportLocation loc : r.getFunction(1).getTeleports())
			if(loc.getX() == x && loc.getY() == y && loc.getZ() == z)
			{
				price = loc.getPrice();
				ok = true;
				break;
			}
		if(!ok)
			return;
		if(player.getKarma() > 0 && !ArrayUtils.contains(Config.ALT_GAME_KARMA_NPC, npc.getNpcId()))
		{
			player.sendMessage(new CustomMessage("l2s.TeleportKarma"));
			return;
		}
		if(player.getMountType() == 2)
		{
			player.sendMessage(new CustomMessage("l2s.TeleportWyvern"));
			return;
		}
		if(!Config.GATEKEEPER_TELEPORT_SIEGE)
		{
			final Town town = TownManager.getInstance().getClosestTown(x, y);
			if(town != null)
			{
				final Castle castle = town.getCastle();
				if(castle != null) {
					SiegeEvent<?, ?> siegeEvent = castle.getSiegeEvent();
					if (siegeEvent != null && siegeEvent.isInProgress()) {
						player.sendPacket(new SystemMessage(707));
						return;
					}
				}
			}
		}
		if(price > 0)
		{
			if(player.getAdena() < price)
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			player.reduceAdena(price, true);
		}
		final Location pos = Location.findAroundPosition(x, y, z, 0, 70, player.getGeoIndex());
		player.teleToLocation(pos);
	}

	public void Token()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = player.getLastNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
			return;
		final int id = npc.getNpcId() == 30540 ? 1659 : npc.getNpcId() == 30576 ? 1658 : 0;
		if(id == 0)
			return;
		if(player.getKarma() > 0 && !ArrayUtils.contains(Config.ALT_GAME_KARMA_NPC, npc.getNpcId()))
		{
			player.sendMessage(new CustomMessage("l2s.TeleportKarma"));
			return;
		}
		if(player.getMountType() == 2)
		{
			player.sendMessage(new CustomMessage("l2s.TeleportWyvern"));
			return;
		}
		final ItemInstance ii = player.getInventory().getItemByItemId(id);
		if(ii == null)
		{
			player.sendPacket(new SystemMessage(701));
			return;
		}
		player.getInventory().destroyItem(ii, 1L, false);
		player.sendPacket(new SystemMessage(302).addItemName(id));
		player.teleToLocation(-80684, 149770, -3040);
	}

	public void TokenJump(final String[] param)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(player.getLevel() <= 19)
			SGK(param);
		else
			show("Only for newbies", player);
	}

	public void NoblessTeleport()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(player.isNoble() || Config.ALLOW_NOBLE_TP_TO_ALL)
			show("scripts/noble.htm", player);
		else
			show("scripts/nobleteleporter-no.htm", player);
	}

	public static void LevelLimit(final Zone zone, final GameObject object, final Boolean enter, final int level, final Location loc)
	{
		if(enter)
		{
			final Player player = object.getPlayer();
			if(player != null && player.getLevel() > level && object.isInZone(zone))
			{
				if(player.isTeleporting())
					player.escLoc = true;
				player.teleToLocation(loc);
			}
		}
	}

	public static void LevelMin(final Zone zone, final GameObject object, final Boolean enter, final int level, final Location loc)
	{
		if(enter)
		{
			final Player player = object.getPlayer();
			if(player != null && player.getLevel() < level && object.isInZone(zone))
			{
				if(player.isTeleporting())
					player.escLoc = true;
				player.teleToLocation(loc);
			}
		}
	}

	public static void LevelMinCW(final Zone zone, final GameObject object, final Boolean enter, final int level, final Location loc)
	{
		if(enter)
		{
			final Player player = object.getPlayer();
			if(player != null && player.getLevel() < level && player.isCursedWeaponEquipped() && object.isInZone(zone))
			{
				if(player.isTeleporting())
					player.escLoc = true;
				player.teleToLocation(loc);
			}
		}
	}
}
