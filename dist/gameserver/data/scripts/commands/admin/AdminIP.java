package commands.admin;

import java.util.List;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.BannedIp;
import l2s.gameserver.utils.IpManager;
import l2s.gameserver.utils.Log;
import l2s.gameserver.utils.Util;

public class AdminIP implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanBan)
			return false;
		final String[] wordList = command.split(" ");
		CommandEnum cmd;
		try
		{
			cmd = CommandEnum.valueOf(wordList[0]);
		}
		catch(Exception e)
		{
			return false;
		}
		switch(cmd)
		{
			case admin_ipbanlist:
			{
				try
				{
					final List<BannedIp> baniplist = IpManager.getBanList();
					if(baniplist != null && baniplist.size() > 0)
					{
						final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
						final StringBuffer replyMSG = new StringBuffer("<html><body>");
						replyMSG.append("<center><font color=\"LEVEL\">Ban IP List</font></center><br>");
						replyMSG.append("<center><table width=300><tr><td>");
						replyMSG.append("<center><font color=\"FF0000\">IP</font></center></td><td><font color=\"FF0000\">Banned by</font></td></tr>");
						for(final BannedIp temp : baniplist)
							replyMSG.append("<tr><td>" + temp.ip + "</td><td>" + temp.admin + "</td><td><button value=\"Unban IP\" action=\"bypass -h admin_ipunban " + temp.ip + " 0\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
						replyMSG.append("</table></center>");
						replyMSG.append("</body></html>");
						adminReply.setHtml(replyMSG.toString());
						activeChar.sendPacket(adminReply);
					}
				}
				catch(StringIndexOutOfBoundsException e2)
				{
					activeChar.sendMessage(new CustomMessage("common.Error"));
				}
				break;
			}
			case admin_ipban:
			case admin_ipblock:
			{
				if(wordList.length < 2)
				{
					activeChar.sendMessage("Command syntax: //ipban <ip>");
					break;
				}
				if(!validateIP(wordList[1]))
				{
					activeChar.sendMessage("Error: Invalid IP adress: " + wordList[1]);
					break;
				}
				if(IpManager.CheckIp(wordList[1]))
				{
					activeChar.sendMessage("Already banned IP: " + wordList[1]);
					break;
				}
				int time = 0;
				if(wordList.length > 2)
					time = Integer.parseInt(wordList[2]) * 60 * 60 * 24;
				String reason = "";
				if(wordList.length > 3)
				{
					reason = wordList[3];
					if(wordList.length > 4)
						for(int i = 4; i < wordList.length; ++i)
							reason = reason + " " + wordList[i];
				}
				IpManager.BanIp(wordList[1], activeChar.getName(), time, reason);
				activeChar.sendMessage("Banned IP: " + wordList[1]);
				Log.addLog(activeChar.toString() + " IP " + wordList[1] + ", ban attempt", "gm_actions");
				for(final Player p : GameObjectsStorage.getPlayers())
					if(p != null && p.getIP().equals(wordList[1]))
						p.kick(true);
				break;
			}
			case admin_ipcharban:
			{
				if(wordList.length < 2)
				{
					activeChar.sendMessage("Command syntax: //ipcharban <char_name>");
					break;
				}
				final Player plr = GameObjectsStorage.getPlayer(wordList[1]);
				if(plr == null)
				{
					activeChar.sendMessage("Character " + wordList[1] + " not found.");
					break;
				}
				final String ip = plr.getIP();
				if(ip.equalsIgnoreCase("<not connected>") || ip.equalsIgnoreCase("?.?.?.?"))
				{
					activeChar.sendMessage("Character " + wordList[1] + " not connected.");
					break;
				}
				if(IpManager.CheckIp(ip))
				{
					activeChar.sendMessage("Already banned IP: " + ip);
					break;
				}
				int t = 0;
				if(wordList.length > 2)
					t = Integer.parseInt(wordList[2]) * 60 * 60 * 24;
				String reas = "";
				if(wordList.length > 3)
				{
					reas = wordList[3];
					if(wordList.length > 4)
						for(int j = 4; j < wordList.length; ++j)
							reas = reas + " " + wordList[j];
				}
				IpManager.BanIp(ip, activeChar.getName(), t, reas);
				activeChar.sendMessage("Banned IP: " + ip);
				Log.addLog(activeChar.toString() + " IP " + ip + ", ban attempt", "gm_actions");
				plr.kick(true);
				break;
			}
			case admin_ipchar:
			case admin_charip:
			{
				if(wordList.length != 2)
				{
					activeChar.sendMessage("Command syntax: //charip <char_name>");
					activeChar.sendMessage(" Gets character's IP.");
					break;
				}
				final Player pl = GameObjectsStorage.getPlayer(wordList[1]);
				if(pl == null)
				{
					activeChar.sendMessage("Character " + wordList[1] + " not found.");
					break;
				}
				final String ip_adr = pl.getIP();
				if(ip_adr.equalsIgnoreCase("<not connected>") || ip_adr.equalsIgnoreCase("?.?.?.?"))
				{
					activeChar.sendMessage("Character " + wordList[1] + " not connected.");
					break;
				}
				activeChar.sendMessage("Character's IP: " + ip_adr);
				break;
			}
			case admin_ipunban:
			case admin_ipunblock:
			{
				if(wordList.length < 2)
				{
					activeChar.sendMessage("Command syntax: //ipunban <ip>");
					break;
				}
				if(!validateIP(wordList[1]))
				{
					activeChar.sendMessage("Error: Invalid IP adress: " + wordList[1]);
					break;
				}
				if(!IpManager.CheckIp(wordList[1]))
				{
					activeChar.sendMessage("Not banned IP: " + wordList[1]);
					break;
				}
				IpManager.UnbanIp(wordList[1]);
				activeChar.sendMessage("Unbanned IP: " + wordList[1]);
				Log.addLog(activeChar.toString() + " IP " + wordList[1] + ", ban attempt", "gm_actions");
				if(wordList.length > 2 && wordList[2].equals("0"))
				{
					useAdminCommand("admin_ipbanlist", activeChar);
					break;
				}
				break;
			}
		}
		return true;
	}

	public boolean validateIP(String IP)
	{
		if(!Util.isMatchingRegexp(IP, "[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}"))
			return false;
		IP = IP.replace(".", ",");
		final String[] split;
		final String[] IP_octets = split = IP.split(",");
		for(final String element : split)
			if(Integer.parseInt(element) > 255)
				return false;
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminIP._adminCommands;
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
		AdminIP._adminCommands = new String[] {
				"admin_ipbanlist",
				"admin_ipban",
				"admin_ipblock",
				"admin_ipunban",
				"admin_ipunblock",
				"admin_ipcharban",
				"admin_ipchar",
				"admin_charip" };
	}

	private enum CommandEnum
	{
		admin_ipbanlist,
		admin_ipban,
		admin_ipblock,
		admin_ipunban,
		admin_ipunblock,
		admin_ipcharban,
		admin_ipchar,
		admin_charip;
	}
}
