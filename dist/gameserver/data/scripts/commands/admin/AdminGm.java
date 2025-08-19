package commands.admin;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Log;

public class AdminGm implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanEditChar)
			return false;
		if(command.equals("admin_gm"))
			handleGm(activeChar);
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminGm._adminCommands;
	}

	private void handleGm(final Player activeChar)
	{
		if(activeChar.getPlayerAccess().IsGM)
		{
			activeChar.getPlayerAccess().IsGM = false;
			activeChar.setVar("NoGMList", "1");
			activeChar.sendMessage("You no longer have GM status.");
			Log.addLog(activeChar.toString() + " turned his GM status off", "gm_actions");
		}
		else
		{
			activeChar.getPlayerAccess().IsGM = true;
			activeChar.unsetVar("NoGMList");
			activeChar.sendMessage("You have GM status now.");
			Log.addLog(activeChar.toString() + " turned his GM status on", "gm_actions");
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
		AdminGm._adminCommands = new String[] { "admin_gm" };
	}
}
