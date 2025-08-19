package commands.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.Territory;
import l2s.gameserver.model.World;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.SpawnTable;
import l2s.gameserver.tables.TerritoryTable;

public class AdminZone implements IAdminCommandHandler, ScriptFile
{
	public static final String[] ADMIN_ZONE_COMMANDS;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(activeChar == null)
			return false;
		if(!activeChar.getPlayerAccess().CanTeleport)
			return false;
		final StringTokenizer st = new StringTokenizer(command, " ");
		final String actualCommand = st.nextToken();
		if(actualCommand.equalsIgnoreCase("admin_zone_check"))
		{
			activeChar.sendMessage("===== Active Territories =====");
			final List<Territory> territories = World.getTerritories(activeChar.getX(), activeChar.getY(), activeChar.getZ());
			if(territories != null)
				for(final Territory terr : territories)
				{
					activeChar.sendMessage("Territory: " + terr.getId());
					if(terr.getZone() != null)
						activeChar.sendMessage("Zone: " + terr.getZone().getType().toString() + ", id: " + terr.getZone().getId() + ", state: " + (terr.getZone().isActive() ? "active" : "not active"));
				}
			activeChar.sendMessage("======= Mob Spawns =======");
			for(final Spawn spawn : SpawnTable.getInstance().getSpawnTable())
			{
				final int location = spawn.getLocation();
				if(location == 0)
					continue;
				final Territory terr2 = TerritoryTable.getInstance().getLocation(location);
				if(terr2 == null)
					continue;
				if(!terr2.isInside(activeChar.getX(), activeChar.getY()))
					continue;
				activeChar.sendMessage("Territory: " + terr2.getId());
			}
		}
		else if(actualCommand.equalsIgnoreCase("admin_region"))
		{
			activeChar.sendMessage("Current region: " + activeChar.getCurrentRegion().getName());
			activeChar.sendMessage("Objects list:");
			for(final GameObject o : activeChar.getCurrentRegion().getObjectsList(new ArrayList<GameObject>(activeChar.getCurrentRegion().getObjectsSize()), 0, activeChar.getReflectionId()))
				if(o != null)
					activeChar.sendMessage(o.toString());
		}
		else if(actualCommand.equalsIgnoreCase("admin_loc"))
		{
			String loc_id = "0";
			if(st.hasMoreTokens())
				loc_id = st.nextToken();
			String loc_name;
			if(st.hasMoreTokens())
				loc_name = st.nextToken();
			else
				loc_name = "loc_" + loc_id;
			System.out.println("(" + loc_id + ",'" + loc_name + "'," + activeChar.getX() + "," + activeChar.getY() + "," + (activeChar.getZ() - 10) + "," + (activeChar.getZ() + 100) + "),");
			activeChar.sendMessage("\u0422\u043e\u0447\u043a\u0430 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0430.");
			final ItemInstance temp = new ItemInstance(IdFactory.getInstance().getNextId(), 1060);
			temp.dropMe(activeChar, activeChar.getLoc());
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminZone.ADMIN_ZONE_COMMANDS;
	}

	@Override
	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		ADMIN_ZONE_COMMANDS = new String[] { "admin_zone_check", "admin_region", "admin_loc" };
	}
}
