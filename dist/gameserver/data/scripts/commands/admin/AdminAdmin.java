package commands.admin;

import java.lang.reflect.Field;
import java.util.StringTokenizer;

import l2s.gameserver.Config;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.PlaySound;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class AdminAdmin implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().Menu)
			return false;
		if(command.equalsIgnoreCase("admin_admin"))
			AdminHelpPage.showHelpPage(activeChar, "admin.htm");
		else if(command.equalsIgnoreCase("admin_play_sounds"))
			AdminHelpPage.showHelpPage(activeChar, "songs/songs.htm");
		else if(command.startsWith("admin_play_sounds"))
			try
			{
				AdminHelpPage.showHelpPage(activeChar, "songs/songs" + command.substring(17) + ".htm");
			}
			catch(StringIndexOutOfBoundsException ex2)
			{}
		else if(command.startsWith("admin_play_sound"))
			try
			{
				playAdminSound(activeChar, command.substring(17));
			}
			catch(StringIndexOutOfBoundsException ex3)
			{}
		else if(command.startsWith("admin_silence"))
		{
			if(activeChar.getMessageRefusal())
			{
				activeChar.unsetVar("gm_silence");
				activeChar.setMessageRefusal(false);
				activeChar.sendPacket(new SystemMessage(178));
			}
			else
			{
				if(Config.SAVE_GM_EFFECTS)
					activeChar.setVar("gm_silence", "true");
				activeChar.setMessageRefusal(true);
				activeChar.sendPacket(new SystemMessage(177));
			}
		}
		else if(command.startsWith("admin_tradeoff"))
			try
			{
				final StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				if(st.nextToken().equalsIgnoreCase("on"))
				{
					activeChar.setTradeRefusal(true);
					activeChar.sendMessage("tradeoff enabled");
				}
				else if(st.nextToken().equalsIgnoreCase("off"))
				{
					activeChar.setTradeRefusal(false);
					activeChar.sendMessage("tradeoff disabled");
				}
			}
			catch(Exception ex)
			{
				if(activeChar.getTradeRefusal())
					activeChar.sendMessage("tradeoff currently enabled");
				else
					activeChar.sendMessage("tradeoff currently disabled");
			}
		else if(command.startsWith("admin_config"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
			String pName = "";
			try
			{
				final String[] parameter = st.nextToken().split("=");
				pName = parameter[0].trim();
				final String pValue = parameter[1].trim();
				final Field field = Config.class.getField(pName);
				if(setField(activeChar, field, pValue))
					activeChar.sendMessage("Config set succesfully");
			}
			catch(NoSuchFieldException e)
			{
				activeChar.sendMessage("Parameter " + pName + " not found");
			}
			catch(Exception e2)
			{
				activeChar.sendMessage("Usage:  //config parameter=value");
			}
		}
		else if(command.startsWith("admin_show_html"))
		{
			final String html = command.substring(16);
			try
			{
				if(html != null)
					AdminHelpPage.showHelpPage(activeChar, html);
				else
					activeChar.sendMessage("Html page not found");
			}
			catch(Exception npe)
			{
				activeChar.sendMessage("Html page not found");
			}
		}
		else if(command.startsWith("admin_gmlist"))
			if(!activeChar.getVarBoolean("NoGMList"))
			{
				activeChar.setVar("NoGMList", "1");
				activeChar.sendMessage("\u0412\u044b \u0443\u0434\u0430\u043b\u0435\u043d\u044b \u0438\u0437 /gmlist");
			}
			else
			{
				activeChar.unsetVar("NoGMList");
				activeChar.sendMessage("\u0412\u044b \u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d\u044b \u0432 /gmlist");
			}
		return true;
	}

	private boolean setField(final Player activeChar, final Field field, final String param)
	{
		try
		{
			field.setBoolean(null, Boolean.parseBoolean(param));
		}
		catch(Exception e)
		{
			try
			{
				field.setInt(null, Integer.parseInt(param));
			}
			catch(Exception e2)
			{
				try
				{
					field.setLong(null, Long.parseLong(param));
				}
				catch(Exception e3)
				{
					try
					{
						field.set(null, param);
					}
					catch(Exception e4)
					{
						activeChar.sendMessage("Error while set field: " + param + " " + e.getMessage());
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminAdmin._adminCommands;
	}

	public void playAdminSound(final Player activeChar, final String sound)
	{
		activeChar.broadcastPacket(new L2GameServerPacket[] { new PlaySound(sound) });
		AdminHelpPage.showHelpPage(activeChar, "admin.htm");
		activeChar.sendMessage("Playing " + sound + ".");
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
		AdminAdmin._adminCommands = new String[] {
				"admin_admin",
				"admin_play_sounds",
				"admin_play_sound",
				"admin_silence",
				"admin_atmosphere",
				"admin_diet",
				"admin_tradeoff",
				"admin_config",
				"admin_show_html",
				"admin_gmlist" };
	}
}
