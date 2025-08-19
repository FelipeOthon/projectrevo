package commands.admin;

import java.util.Calendar;
import java.util.StringTokenizer;

import l2s.gameserver.Config;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.model.entity.olympiad.OlympiadDatabase;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Log;
import l2s.gameserver.utils.TimeUtils;

public class AdminOlympiad implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(command.startsWith("admin_oly_start"))
		{
			if(!Olympiad.manualStartOlympiad())
				activeChar.sendMessage("Olympiad already started.");
		}
		else if(command.startsWith("admin_oly_stop"))
		{
			if(!Olympiad.manualStopOlympiad())
				activeChar.sendMessage("Olympiad already stopped.");
		}
		else if(command.startsWith("admin_oly_save"))
		{
			if(!Config.ENABLE_OLYMPIAD)
				return false;
			try
			{
				OlympiadDatabase.save();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			activeChar.sendMessage("Olympiad data saved.");
		}
		else if(command.startsWith("admin_manualhero"))
		{
			if(!Config.ENABLE_OLYMPIAD)
				return false;
			try
			{
				Olympiad.manualSelectHeroes();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			activeChar.sendMessage("Heroes formed.");
		}
		else if(command.startsWith("admin_add_oly_points"))
		{
			final String[] wordList = command.split(" ");
			if(wordList.length < 3)
			{
				activeChar.sendMessage("Command syntax: //add_oly_points <char_name> <point_to_add>");
				activeChar.sendMessage("This command can be applied only for online players.");
				return false;
			}
			final Player player = World.getPlayer(wordList[1]);
			if(player == null)
			{
				activeChar.sendMessage("Character " + wordList[1] + " not found in game.");
				return false;
			}
			int pointToAdd;
			try
			{
				pointToAdd = Integer.parseInt(wordList[2]);
			}
			catch(NumberFormatException e2)
			{
				activeChar.sendMessage("Please specify integer value for olympiad points.");
				return false;
			}
			final int curPoints = Olympiad.getNoblePoints(player.getObjectId());
			Olympiad.manualSetNoblePoints(player.getObjectId(), curPoints + pointToAdd);
			final int newPoints = Olympiad.getNoblePoints(player.getObjectId());
			activeChar.sendMessage("Added " + pointToAdd + " points to character " + player.getName());
			activeChar.sendMessage("Old points: " + curPoints + ", new points: " + newPoints);
			Log.addLog(activeChar.toString() + " add olympiad points to player " + player.getName(), "gm_actions");
		}
		else if(command.startsWith("admin_oly_points"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			try
			{
				st.nextToken();
				final String name = st.nextToken();
				if(name != null)
				{
					final int c = Olympiad.getNoblePointsByName(name);
					if(c != -1)
						activeChar.sendMessage(name + ": " + c);
					else
						activeChar.sendMessage("Player " + name + " not found.");
				}
			}
			catch(Exception e3)
			{
				activeChar.sendMessage("Command syntax: //oly_points <char_name>.");
				return false;
			}
		}
		else if(command.equals("admin_oly_end"))
		{
			if(Olympiad._olympiadEnd > 0L)
				activeChar.sendMessage("Olympiad End: " + TimeUtils.toSimpleFormat(Olympiad._olympiadEnd));
			else
				activeChar.sendMessage("Olympiad End: not defined.");
		}
		else if(command.startsWith("admin_setolyend"))
		{
			final String[] wordList = command.split(" ");
			if(wordList.length < 2)
			{
				activeChar.sendMessage("Command syntax: //setolyend <hour> <day> <month> <year> <minute>");
				return false;
			}
			final Calendar calendar = Calendar.getInstance();
			calendar.set(12, 0);
			calendar.set(13, 1);
			for(int i = 1; i < wordList.length; ++i)
			{
				int val;
				try
				{
					val = Integer.parseInt(wordList[i]);
				}
				catch(Exception e4)
				{
					activeChar.sendMessage("Command syntax: //setolyend <hour> <day> <month> <year> <minute>");
					return false;
				}
				int type = 0;
				switch(i)
				{
					case 1:
					{
						type = 11;
						break;
					}
					case 2:
					{
						type = 5;
						break;
					}
					case 3:
					{
						type = 2;
						--val;
						break;
					}
					case 4:
					{
						type = 1;
						break;
					}
					case 5:
					{
						type = 12;
						break;
					}
					default:
					{
						continue;
					}
				}
				calendar.set(type, val);
			}
			if(calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
			{
				activeChar.sendMessage("Date is lower than current time.");
				return false;
			}
			if(Olympiad.setOlyEnd(calendar.getTimeInMillis()))
				useAdminCommand("admin_oly_end", activeChar);
			else
				activeChar.sendMessage("Incorrect period.");
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminOlympiad._adminCommands;
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
		AdminOlympiad._adminCommands = new String[] {
				"admin_oly_start",
				"admin_oly_stop",
				"admin_oly_save",
				"admin_manualhero",
				"admin_add_oly_points",
				"admin_oly_points",
				"admin_oly_end",
				"admin_setolyend" };
	}
}
