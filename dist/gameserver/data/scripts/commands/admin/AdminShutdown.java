package commands.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import l2s.commons.lang.StatsUtils;
import l2s.gameserver.Config;
import l2s.gameserver.GameTimeController;
import l2s.gameserver.Shutdown;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.GmListTable;
import l2s.gameserver.utils.Log;

public class AdminShutdown implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanRestart)
			return false;
		try
		{
			if(command.startsWith("admin_server_shutdown"))
			{
				final int val = Integer.parseInt(command.substring(22));
				Shutdown.getInstance().schedule(val, 0);
				GmListTable.broadcastMessageToGMs("This server will be shutdown in " + val + " seconds!");
				Log.addLog(activeChar.toString() + " shutdown server in " + val + " seconds", "gm_actions");
			}
			else if(command.startsWith("admin_server_restart"))
			{
				final int val = Integer.parseInt(command.substring(21));
				Shutdown.getInstance().schedule(val, 2);
				GmListTable.broadcastMessageToGMs("This server will be restart in " + val + " seconds!");
				Log.addLog(activeChar.toString() + " restart server in " + val + " seconds", "gm_actions");
			}
			else if(command.startsWith("admin_server_abort"))
			{
				Shutdown.getInstance().cancel();
				GmListTable.broadcastMessageToGMs("Server shutdown/restart aborted!");
				Log.addLog(activeChar.toString() + " abort shutdown/restart", "gm_actions");
			}
		}
		catch(Exception e)
		{
			sendHtmlForm(activeChar);
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminShutdown._adminCommands;
	}

	private void sendHtmlForm(final Player activeChar)
	{
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final int t = GameTimeController.getInstance().getGameTime();
		final int h = t / 60;
		final int m = t % 60;
		final SimpleDateFormat format = new SimpleDateFormat("h:mm a");
		final Calendar cal = Calendar.getInstance();
		cal.set(11, h);
		cal.set(12, m);
		final StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td width=180><center>Server Management Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<br><br>");
		replyMSG.append("<table>");
		if(activeChar.getPlayerAccess().CanUseOnline)
		{
			final List<String> hwids = new ArrayList<String>();
			int bots = 0;
			int traders = 0;
			for(final Player player : GameObjectsStorage.getPlayers())
				if(player != null)
				{
					if(player.isGM())
						continue;
					if(player.isFashion)
						++bots;
					else if(player.isInOfflineMode())
						++traders;
					else
					{
						if(!player.isConnected() || player.getHWID().isEmpty() || hwids.contains(player.getHWID()))
							continue;
						hwids.add(player.getHWID());
					}
				}
			replyMSG.append("<tr><td>Players Online: " + (GameObjectsStorage.getPlayers().size() - bots) + "</td></tr>");
			replyMSG.append("<tr><td>Offline Traders: " + traders + "</td></tr>");
			replyMSG.append("<tr><td>Bots Online: " + bots + "</td></tr>");
			replyMSG.append("<tr><td>Real Online: " + hwids.size() + "</td></tr>");
		}
		else
			replyMSG.append("<tr><td>Players Online: " + GameObjectsStorage.getPlayers().size() + "</td></tr>");
		replyMSG.append("<tr><td>Used Memory: " + StatsUtils.getMemUsedMb() + "</td></tr>");
		replyMSG.append("<tr><td>Server Rates: " + Config.RATE_XP + "x, " + Config.RATE_SP + "x, " + Config.RATE_DROP_ADENA + "x, " + Config.RATE_DROP_ITEMS + "x</td></tr>");
		replyMSG.append("<tr><td>Server Time: " + new SimpleDateFormat("HH:mm   dd.MM.yyyy").format(new Date(System.currentTimeMillis())) + "</td></tr>");
		replyMSG.append("<tr><td>Game Time: " + format.format(cal.getTime()) + "</td></tr>");
		replyMSG.append("<tr><td>OS: " + System.getenv("OS") + "</td></tr>");
		replyMSG.append("</table><br>");
		replyMSG.append("<table width=270>");
		replyMSG.append("<tr><td>Enter in seconds the time till the server shutdowns bellow:</td></tr>");
		replyMSG.append("<br>");
		replyMSG.append("<tr><td><center>Seconds till: <edit var=\"shutdown_time\" width=60></center></td></tr>");
		replyMSG.append("</table><br>");
		replyMSG.append("<center><table><tr><td>");
		replyMSG.append("<button value=\"Shutdown\" action=\"bypass -h admin_server_shutdown $shutdown_time\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Restart\" action=\"bypass -h admin_server_restart $shutdown_time\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Abort\" action=\"bypass -h admin_server_abort\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
		replyMSG.append("</td></tr></table></center>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
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
		AdminShutdown._adminCommands = new String[] { "admin_server_shutdown", "admin_server_restart", "admin_server_abort" };
	}
}
