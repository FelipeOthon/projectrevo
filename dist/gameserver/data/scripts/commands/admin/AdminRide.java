package commands.admin;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.ScriptFile;

public class AdminRide implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().Menu)
			return false;
		if(command.startsWith("admin_ride"))
			try
			{
				final String val = command.substring(11);
				showRide(activeChar, val);
			}
			catch(StringIndexOutOfBoundsException ex)
			{}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminRide._adminCommands;
	}

	public static void showRide(final Player targetChar, final String filename)
	{
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		adminReply.setFile("admin/" + filename);
		targetChar.sendPacket(adminReply);
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
		AdminRide._adminCommands = new String[] { "admin_ride" };
	}
}
