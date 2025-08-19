package commands.admin;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.DoorTable;

public class AdminDoorControl implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().Door)
			return false;
		try
		{
			if(command.startsWith("admin_open "))
			{
				final int doorId = Integer.parseInt(command.split(" ")[1]);
				DoorTable.getInstance().getDoor(doorId).openMe();
			}
			else if(command.startsWith("admin_close "))
			{
				final int doorId = Integer.parseInt(command.split(" ")[1]);
				DoorTable.getInstance().getDoor(doorId).closeMe();
			}
			if(command.equals("admin_closeall"))
				for(final DoorInstance door : DoorTable.getInstance().getDoors())
					door.closeMe();
			if(command.equals("admin_openall"))
				for(final DoorInstance door : DoorTable.getInstance().getDoors())
					door.openMe();
			if(command.equals("admin_open"))
			{
				final GameObject target = activeChar.getTarget();
				if(target instanceof DoorInstance)
					((DoorInstance) target).openMe();
				else
					activeChar.sendPacket(Msg.INCORRECT_TARGET);
			}
			if(command.equals("admin_close"))
			{
				final GameObject target = activeChar.getTarget();
				if(target instanceof DoorInstance)
					((DoorInstance) target).closeMe();
				else
					activeChar.sendPacket(Msg.INCORRECT_TARGET);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminDoorControl._adminCommands;
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
		AdminDoorControl._adminCommands = new String[] { "admin_open", "admin_close", "admin_openall", "admin_closeall" };
	}
}
