package commands.admin;

import java.util.StringTokenizer;

import l2s.gameserver.Config;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.SevenSigns;
import l2s.gameserver.scripts.ScriptFile;

public class AdminSS implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanUseGMCommand)
			return false;
		if(!Config.ALLOW_SEVEN_SIGNS)
			return false;
		if(command.startsWith("admin_ssq_change"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 2)
			{
				st.nextToken();
				final int period = Integer.parseInt(st.nextToken());
				final int minutes = Integer.parseInt(st.nextToken());
				SevenSigns.getInstance().changePeriod(period, minutes * 60);
			}
			else if(st.countTokens() > 1)
			{
				st.nextToken();
				final int period = Integer.parseInt(st.nextToken());
				SevenSigns.getInstance().changePeriod(period);
			}
			else
				SevenSigns.getInstance().changePeriod();
		}
		else if(command.startsWith("admin_ssq_time"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 1)
			{
				st.nextToken();
				final int time = Integer.parseInt(st.nextToken());
				SevenSigns.getInstance().setTimeToNextPeriodChange(time);
			}
		}
		else if(command.equals("admin_ssq_status"))
			for(final Integer currSeal : SevenSigns.getInstance().getSealOwners().keySet())
			{
				final int sealOwner = SevenSigns.getInstance().getSealOwners().get(currSeal);
				if(sealOwner != 0)
				{
					if(SevenSigns.getInstance().isSealValidationPeriod())
						activeChar.sendMessage(SevenSigns.getCabalName(sealOwner) + " have won the " + SevenSigns.getSealName(currSeal, false) + ".");
					else
						activeChar.sendMessage(SevenSigns.getSealName(currSeal, false) + " is currently owned by " + SevenSigns.getCabalName(sealOwner) + ".");
				}
				else
					activeChar.sendMessage(SevenSigns.getSealName(currSeal, false) + " remains unclaimed.");
			}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminSS._adminCommands;
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
		AdminSS._adminCommands = new String[] { "admin_ssq_change", "admin_ssq_time", "admin_ssq_status" };
	}
}
