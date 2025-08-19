package commands.admin;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.CameraMode;
import l2s.gameserver.network.l2.s2c.SpecialCamera;
import l2s.gameserver.scripts.ScriptFile;

public class AdminCamera implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().Menu)
			return false;
		if(!command.startsWith("admin_freelook"))
		{
			if(command.startsWith("admin_cinematic"))
			{
				command = command.substring(AdminCamera._adminCommands[1].length() + 1);
				final String[] params = command.split(" ");
				final int id = Integer.parseInt(params[0]);
				final int dist = Integer.parseInt(params[1]);
				final int yaw = Integer.parseInt(params[2]);
				final int pitch = Integer.parseInt(params[3]);
				final int time = Integer.parseInt(params[4]);
				final int duration = Integer.parseInt(params[5]);
				activeChar.sendPacket(new SpecialCamera(id, dist, yaw, pitch, time, duration));
			}
			return true;
		}
		if(command.length() > AdminCamera._adminCommands[0].length() + 1)
		{
			command = command.substring(AdminCamera._adminCommands[0].length() + 1);
			final int mode = Integer.parseInt(command);
			if(mode == 1)
			{
				activeChar.setInvisible(true);
				activeChar.setIsInvul(true);
				activeChar.setNoChannel(-1L);
				activeChar.setFlying(true);
			}
			else
			{
				activeChar.setInvisible(false);
				activeChar.setIsInvul(false);
				activeChar.setNoChannel(0L);
				activeChar.setFlying(false);
			}
			activeChar.sendPacket(new CameraMode(mode));
			return true;
		}
		activeChar.sendMessage("Usage: //freelook 1 or //freelook 0");
		return false;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminCamera._adminCommands;
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
		AdminCamera._adminCommands = new String[] { "admin_freelook", "admin_cinematic" };
	}
}
