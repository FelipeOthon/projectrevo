package commands.admin;

import java.util.StringTokenizer;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.scripts.ScriptFile;

public class AdminLevel implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(activeChar == null)
			return false;
		if(!activeChar.getPlayerAccess().CanEditChar)
			return false;
		if(activeChar.getTarget() == null || !activeChar.getTarget().isPlayer())
		{
			activeChar.sendPacket(Msg.INCORRECT_TARGET);
			return false;
		}
		final Player target = (Player) activeChar.getTarget();
		final StringTokenizer st = new StringTokenizer(command, " ");
		final String actualCommand = st.nextToken();
		String val = "";
		if(st.countTokens() >= 1)
			val = st.nextToken();
		if(actualCommand.equalsIgnoreCase("admin_addLevel"))
			try
			{
				final Byte level = (byte) (target.getLevel() + Byte.parseByte(val));
				if(level < 1 || level > 80)
				{
					activeChar.sendMessage("You must specify level between 1 and 80.");
					return false;
				}
				final Long exp_add = Experience.LEVEL[level] - target.getExp();
				target.addExpAndSp(exp_add, 0L, false, false);
			}
			catch(NumberFormatException e)
			{
				activeChar.sendMessage("Wrong Number Format");
			}
		else if(actualCommand.equalsIgnoreCase("admin_setLevel"))
			try
			{
				final byte level2 = Byte.parseByte(val);
				if(level2 < 1 || level2 > 80)
				{
					activeChar.sendMessage("You must specify level between 1 and 80.");
					return false;
				}
				final Long exp_add = Experience.LEVEL[level2] - target.getExp();
				target.addExpAndSp(exp_add, 0L, false, false);
			}
			catch(NumberFormatException e)
			{
				activeChar.sendMessage("You must specify level between 1 and 80.");
				return false;
			}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminLevel.ADMIN_COMMANDS;
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
		ADMIN_COMMANDS = new String[] { "admin_addLevel", "admin_setLevel" };
	}
}
