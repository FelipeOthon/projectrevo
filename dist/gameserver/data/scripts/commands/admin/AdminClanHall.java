package commands.admin;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.entity.residence.ClanHall;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ClanTable;

public class AdminClanHall implements IAdminCommandHandler, ScriptFile
{
	private static Logger _log = LoggerFactory.getLogger(AdminClanHall.class);

	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanEditNPC)
			return false;
		final StringTokenizer st = new StringTokenizer(command, " ");
		command = st.nextToken();
		ClanHall clanhall = null;
		if(command.startsWith("admin_clanhall") && st.hasMoreTokens())
			clanhall = ResidenceHolder.getInstance().getResidence(ClanHall.class, Integer.parseInt(st.nextToken()));
		if(clanhall == null)
			showClanHallSelectPage(activeChar);
		else
		{
			final GameObject target = activeChar.getTarget();
			Player player = null;
			if(target != null)
			{
				if(target.isPlayer())
					player = (Player) target;
			}
			else
				player = activeChar;
			if(command.equalsIgnoreCase("admin_clanhallset"))
			{
				if(player == null || player.getClan() == null)
					activeChar.sendPacket(Msg.TARGET_IS_INCORRECT);
				else
				{
					clanhall.changeOwner(player.getClan());
					_log.info("ClanHall " + clanhall.getName() + "(id: " + clanhall.getId() + ") owned by clan " + player.getClan().getName());
				}
			}
			else if(command.equalsIgnoreCase("admin_clanhalldel"))
				clanhall.changeOwner((Clan) null);
			else if(command.equalsIgnoreCase("admin_clanhallteleportself"))
			{
				final Zone zone = clanhall.getZone();
				if(zone != null)
					activeChar.teleToLocation(zone.getSpawn());
			}
			showClanHallPage(activeChar, clanhall);
		}
		return true;
	}

	public void showClanHallSelectPage(final Player activeChar)
	{
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final StringBuffer replyMSG = new StringBuffer("<html><body>");
		replyMSG.append("<table width=268><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td width=180><center><font color=\"LEVEL\">Clan Halls:</font></center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("</tr></table><br>");
		replyMSG.append("<table width=268>");
		replyMSG.append("<tr><td width=130>ClanHall Name</td><td width=58>Town</td><td width=80>Owner</td></tr>");
		for(final ClanHall clanhall : ResidenceHolder.getInstance().getResidenceList(ClanHall.class))
			if(clanhall != null)
			{
				replyMSG.append("<tr><td>");
				replyMSG.append("<a action=\"bypass -h admin_clanhall " + clanhall.getId() + "\">" + clanhall.getName() + "</a>");
				replyMSG.append("</td><td>" + clanhall.getLocation() + "</td><td>");
				final Clan owner = clanhall.getOwnerId() == 0 ? null : ClanTable.getInstance().getClan(clanhall.getOwnerId());
				if(owner == null)
					replyMSG.append("none");
				else
					replyMSG.append(owner.getName());
				replyMSG.append("</td></tr>");
			}
		replyMSG.append("</table>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	public void showClanHallPage(final Player activeChar, final ClanHall clanhall)
	{
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final StringBuffer replyMSG = new StringBuffer("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td width=180><center>ClanHall Name</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_clanhall\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<center>");
		replyMSG.append("<br><br><br>ClanHall: " + clanhall.getName() + "<br>");
		replyMSG.append("Location: &^" + clanhall.getId() + ";<br>");
		replyMSG.append("ClanHall Owner: ");
		final Clan owner = clanhall.getOwnerId() == 0 ? null : ClanTable.getInstance().getClan(clanhall.getOwnerId());
		if(owner == null)
			replyMSG.append("none");
		else
			replyMSG.append(owner.getName());
		replyMSG.append("<br><br><br>");
		replyMSG.append("<table>");
		replyMSG.append("<tr><td><button value=\"Give ClanHall\" action=\"bypass -h admin_clanhallset " + clanhall.getId() + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Take ClanHall\" action=\"bypass -h admin_clanhalldel " + clanhall.getId() + "\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
		replyMSG.append("</table>");
		replyMSG.append("<br>");
		replyMSG.append("<table><tr>");
		replyMSG.append("<td><button value=\"Teleport self\" action=\"bypass -h admin_clanhallteleportself " + clanhall.getId() + " \" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
		replyMSG.append("</table>");
		replyMSG.append("</center>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminClanHall._adminCommands;
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
		AdminClanHall._adminCommands = new String[] { "admin_clanhall", "admin_clanhallset", "admin_clanhalldel", "admin_clanhallteleportself" };
	}
}
