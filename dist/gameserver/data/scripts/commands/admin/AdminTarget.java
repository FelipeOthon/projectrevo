package commands.admin;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.scripts.ScriptFile;

public class AdminTarget implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanViewChar)
			return false;
		if(command.startsWith("admin_target"))
			handleTarget(command, activeChar);
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminTarget._adminCommands;
	}

	private void handleTarget(final String command, final Player activeChar)
	{
		try
		{
			final String targetName = command.substring(13);
			final GameObject obj = World.getPlayer(targetName);
			if(obj != null && obj.isPlayer())
				obj.onAction(activeChar, false);
			else
				activeChar.sendMessage("Player " + targetName + " not found");
		}
		catch(IndexOutOfBoundsException e)
		{
			activeChar.sendMessage("Please specify correct name.");
		}
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
		AdminTarget._adminCommands = new String[] { "admin_target" };
	}
}
