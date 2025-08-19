package commands.admin;

import java.util.StringTokenizer;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.ScriptFile;

public class AdminServer implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().Menu)
			return false;
		if(command.startsWith("admin_server"))
			try
			{
				final String val = command.substring(13);
				showHelpPage(activeChar, val);
			}
			catch(StringIndexOutOfBoundsException ex)
			{}
		else if(command.startsWith("admin_gc"))
		{
			try
			{
				System.gc();
				Thread.sleep(1000L);
				System.gc();
				Thread.sleep(1000L);
				System.gc();
			}
			catch(Exception ex2)
			{}
			activeChar.sendMessage("OK! - garbage collector called.");
		}
		else if(command.startsWith("admin_test"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
			String val2 = null;
			if(st.hasMoreTokens())
				val2 = st.nextToken();
			String val3 = null;
			if(st.hasMoreTokens())
				val3 = st.nextToken();
			activeChar.sendMessage("Test.");
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminServer._adminCommands;
	}

	public static void showHelpPage(final Player targetChar, final String filename)
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
		AdminServer._adminCommands = new String[] { "admin_server", "admin_gc", "admin_test" };
	}
}
