package commands.admin;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javolution.text.TextBuilder;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.instancemanager.CastleManorManager;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.ScriptFile;

public class AdminManor implements IAdminCommandHandler, ScriptFile
{
	private static final String[] _adminCommands;

	@Override
	public boolean useAdminCommand(String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().Menu)
			return false;
		final StringTokenizer st = new StringTokenizer(command);
		command = st.nextToken();
		if(command.equals("admin_manor"))
			showMainPage(activeChar);
		else if(command.equals("admin_manor_reset"))
		{
			int castleId = 0;
			try
			{
				castleId = Integer.parseInt(st.nextToken());
			}
			catch(Exception ex)
			{}
			if(castleId > 0)
			{
				final Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, castleId);
				castle.setCropProcure(new ArrayList<CastleManorManager.CropProcure>(), 0);
				castle.setCropProcure(new ArrayList<CastleManorManager.CropProcure>(), 1);
				castle.setSeedProduction(new ArrayList<CastleManorManager.SeedProduction>(), 0);
				castle.setSeedProduction(new ArrayList<CastleManorManager.SeedProduction>(), 1);
				if(Config.MANOR_SAVE_ALL_ACTIONS)
				{
					castle.saveCropData();
					castle.saveSeedData();
				}
				activeChar.sendMessage("Manor data for " + castle.getName() + " was nulled");
			}
			else
			{
				for(final Castle castle2 : ResidenceHolder.getInstance().getResidenceList(Castle.class))
				{
					castle2.setCropProcure(new ArrayList<CastleManorManager.CropProcure>(), 0);
					castle2.setCropProcure(new ArrayList<CastleManorManager.CropProcure>(), 1);
					castle2.setSeedProduction(new ArrayList<CastleManorManager.SeedProduction>(), 0);
					castle2.setSeedProduction(new ArrayList<CastleManorManager.SeedProduction>(), 1);
					if(Config.MANOR_SAVE_ALL_ACTIONS)
					{
						castle2.saveCropData();
						castle2.saveSeedData();
					}
				}
				activeChar.sendMessage("Manor data was nulled");
			}
			showMainPage(activeChar);
		}
		else if(command.equals("admin_manor_save"))
		{
			CastleManorManager.getInstance().save();
			activeChar.sendMessage("Manor System: all data saved");
			showMainPage(activeChar);
		}
		else if(command.equals("admin_manor_disable"))
		{
			final boolean mode = CastleManorManager.getInstance().isDisabled();
			CastleManorManager.getInstance().setDisabled(!mode);
			if(mode)
				activeChar.sendMessage("Manor System: enabled");
			else
				activeChar.sendMessage("Manor System: disabled");
			showMainPage(activeChar);
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminManor._adminCommands;
	}

	private String formatTime(final long millis)
	{
		String s = "";
		int secs = (int) millis / 1000;
		int mins = secs / 60;
		secs -= mins * 60;
		final int hours = mins / 60;
		mins -= hours * 60;
		if(hours > 0)
			s = s + hours + ":";
		s = s + mins + ":";
		s += secs;
		return s;
	}

	private void showMainPage(final Player activeChar)
	{
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final TextBuilder replyMSG = new TextBuilder("<html><body>");
		replyMSG.append("<center><font color=\"LEVEL\"> [Manor System] </font></center><br>");
		replyMSG.append("<table width=\"100%\">");
		replyMSG.append("<tr><td>Disabled: " + (CastleManorManager.getInstance().isDisabled() ? "yes" : "no") + "</td>");
		replyMSG.append("<td>Under Maintenance: " + (CastleManorManager.getInstance().isUnderMaintenance() ? "yes" : "no") + "</td></tr>");
		replyMSG.append("<tr><td>Approved: " + (ServerVariables.getBool("ManorApproved") ? "yes" : "no") + "</td></tr>");
		replyMSG.append("</table>");
		replyMSG.append("<center><table>");
		replyMSG.append("<tr><td><button value=\"" + (CastleManorManager.getInstance().isDisabled() ? "Enable" : "Disable") + "\" action=\"bypass -h admin_manor_disable\" width=110 height=15 back=\"sek.cbui92\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Reset\" action=\"bypass -h admin_manor_reset\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
		replyMSG.append("<tr><td><button value=\"Refresh\" action=\"bypass -h admin_manor\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Back\" action=\"bypass -h admin_admin\" width=110 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
		replyMSG.append("</table></center>");
		replyMSG.append("<br><center>Castle Information:<table width=\"100%\">");
		replyMSG.append("<tr><td></td><td>Current Period</td><td>Next Period</td></tr>");
		for(final Castle c : ResidenceHolder.getInstance().getResidenceList(Castle.class))
			replyMSG.append("<tr><td>" + c.getName() + "</td><td>" + c.getManorCost(0) + "a</td><td>" + c.getManorCost(1) + "a</td></tr>");
		replyMSG.append("</table><br>");
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
		_adminCommands = new String[] { "admin_manor", "admin_manor_reset", "admin_manor_save", "admin_manor_disable" };
	}
}
