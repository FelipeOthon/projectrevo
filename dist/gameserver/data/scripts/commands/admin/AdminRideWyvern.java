package commands.admin;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.ScriptFile;

public class AdminRideWyvern implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().Rider)
			return false;
		if(command.startsWith("admin_ride_wyvern"))
		{
			if(activeChar.isMounted() || activeChar.getServitor() != null)
			{
				activeChar.sendMessage("Already Have a Pet or Mounted.");
				return false;
			}
			activeChar.setMount(12621, 0, 0);
		}
		else if(command.startsWith("admin_wr"))
		{
			final GameObject t = activeChar.getTarget();
			if(t != null && t.isPlayer())
			{
				final Player p = (Player) t;
				if(p.isMounted() || p.getServitor() != null)
				{
					activeChar.sendMessage("Already Have a Pet or Mounted.");
					return false;
				}
				p.setMount(12621, 0, 0);
			}
		}
		else if(command.startsWith("admin_ride_strider"))
		{
			if(activeChar.isMounted() || activeChar.getServitor() != null)
			{
				activeChar.sendMessage("Already Have a Pet or Mounted.");
				return false;
			}
			activeChar.setMount(12526, 0, 0);
		}
		else if(command.startsWith("admin_sr"))
		{
			final GameObject t = activeChar.getTarget();
			if(t != null && t.isPlayer())
			{
				final Player p = (Player) t;
				if(p.isMounted() || p.getServitor() != null)
				{
					activeChar.sendMessage("Already Have a Pet or Mounted.");
					return false;
				}
				p.setMount(12526, 0, 0);
			}
		}
		else if(command.startsWith("admin_unride"))
			activeChar.setMount(0, 0, 0);
		else if(command.startsWith("admin_ur"))
		{
			final GameObject t = activeChar.getTarget();
			if(t != null && t.isPlayer())
				((Player) t).setMount(0, 0, 0);
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminRideWyvern.ADMIN_COMMANDS;
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
		ADMIN_COMMANDS = new String[] {
				"admin_ride_wyvern",
				"admin_ride_strider",
				"admin_unride_wyvern",
				"admin_unride_strider",
				"admin_unride",
				"admin_wr",
				"admin_sr",
				"admin_ur" };
	}
}
