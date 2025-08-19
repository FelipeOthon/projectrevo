package commands.admin;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.ScriptFile;

public class AdminEvents implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().IsEventGm)
			return false;
		if(command.startsWith("admin_events"))
			AdminHelpPage.showHelpPage(activeChar, "events.htm");
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminEvents._adminCommands;
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
		AdminEvents._adminCommands = new String[] { "admin_events" };
	}
}
