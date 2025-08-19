package commands.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.Config;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.instancemanager.PlayerManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.PlayerAccess;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Files;

public class AdminChangeAccessLevel implements IAdminCommandHandler, ScriptFile
{
	private static final Logger _log;
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanGmEdit)
			return false;
		final String[] wordList = command.split(" ");
		Commands cmd;
		try
		{
			cmd = Commands.valueOf(wordList[0]);
		}
		catch(Exception e)
		{
			return false;
		}
		switch(cmd)
		{
			case admin_changelvl:
			{
				if(wordList.length == 2)
				{
					final int lvl = Integer.parseInt(wordList[1]);
					if(activeChar.getTarget().isPlayer())
						((Player) activeChar.getTarget()).setAccessLevel(lvl);
					break;
				}
				if(wordList.length == 3)
				{
					final int lvl = Integer.parseInt(wordList[2]);
					final Player player = GameObjectsStorage.getPlayer(wordList[1]);
					if(player != null)
						player.setAccessLevel(lvl);
					break;
				}
				break;
			}
			case admin_moders:
			{
				showModersPannel(activeChar);
				break;
			}
			case admin_moders_add:
			{
				if(activeChar.getTarget() == null || !activeChar.getTarget().isPlayer())
				{
					activeChar.sendMessage("Incorrect target. Please select a player.");
					showModersPannel(activeChar);
					return false;
				}
				final Player modAdd = activeChar.getTarget().getPlayer();
				if(Config.gmlist.containsKey(modAdd.getObjectId()))
				{
					activeChar.sendMessage("Error: Moderator " + modAdd.getName() + " already in server access list.");
					showModersPannel(activeChar);
					return false;
				}
				final String newFName = "m" + modAdd.getObjectId() + ".xml";
				if(!Files.copyFile("./config/GMAccess/template/moderator.xml", "./config/GMAccess/" + newFName))
				{
					activeChar.sendMessage("Error: Failed to copy access-file.");
					showModersPannel(activeChar);
					return false;
				}
				String res = "";
				try
				{
					final BufferedReader in = new BufferedReader(new FileReader("./config/GMAccess/" + newFName));
					String str;
					while((str = in.readLine()) != null)
						res = res + str + "\n";
					in.close();
					res = res.replaceFirst("ObjIdPlayer", "" + modAdd.getObjectId());
					Files.writeFile("./config/GMAccess/" + newFName, res);
				}
				catch(Exception e2)
				{
					activeChar.sendMessage("Error: Failed to modify object ID in access-file.");
					final File fDel = new File("./config/GMAccess/" + newFName);
					if(fDel.exists())
						fDel.delete();
					showModersPannel(activeChar);
					return false;
				}
				final File af = new File("./config/GMAccess/" + newFName);
				if(!af.exists())
				{
					activeChar.sendMessage("Error: Failed to read access-file for " + modAdd.getName());
					showModersPannel(activeChar);
					return false;
				}
				Config.loadGMAccess(af);
				modAdd.setPlayerAccess(Config.gmlist.get(modAdd.getObjectId()));
				activeChar.sendMessage("Moderator " + modAdd.getName() + " added.");
				showModersPannel(activeChar);
				break;
			}
			case admin_moders_del:
			{
				if(wordList.length < 2)
				{
					activeChar.sendMessage("Please specify moderator object ID to delete moderator.");
					showModersPannel(activeChar);
					return false;
				}
				final int oid = Integer.parseInt(wordList[1]);
				if(!Config.gmlist.containsKey(oid))
				{
					activeChar.sendMessage("Error: Moderator with object ID " + oid + " not found in server access lits.");
					showModersPannel(activeChar);
					return false;
				}
				Config.gmlist.remove(oid);
				final Player modDel = GameObjectsStorage.getPlayer(oid);
				if(modDel != null)
					modDel.setPlayerAccess((PlayerAccess) null);
				final String fname = "m" + oid + ".xml";
				final File f = new File("./config/GMAccess/" + fname);
				if(!f.exists() || !f.isFile() || !f.delete())
				{
					activeChar.sendMessage("Error: Can't delete access-file: " + fname);
					showModersPannel(activeChar);
					return false;
				}
				if(modDel != null)
					activeChar.sendMessage("Moderator " + modDel.getName() + " deleted.");
				else
					activeChar.sendMessage("Moderator with object ID " + oid + " deleted.");
				showModersPannel(activeChar);
				break;
			}
		}
		return true;
	}

	private static void showModersPannel(final Player activeChar)
	{
		final NpcHtmlMessage reply = new NpcHtmlMessage(5);
		String html = "Moderators managment panel.<br>";
		final File dir = new File("./config/GMAccess/");
		if(!dir.exists() || !dir.isDirectory())
		{
			html += "Error: Can't open permissions folder.";
			reply.setHtml(html);
			activeChar.sendPacket(reply);
			return;
		}
		html += "<p align=right>";
		html += "<button width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\" action=\"bypass -h admin_moders_add\" value=\"Add moderator\">";
		html += "</p><br>";
		html += "<center><font color=LEVEL>Moderators:</font></center>";
		html += "<table width=285>";
		for(final File f : dir.listFiles())
			if(!f.isDirectory() && f.getName().startsWith("m"))
				if(f.getName().endsWith(".xml"))
				{
					final int oid = Integer.parseInt(f.getName().substring(1, 10));
					String pName = PlayerManager.getNameByObjectId(oid);
					boolean on = false;
					if(pName.isEmpty())
						pName = "" + oid;
					else
						on = GameObjectsStorage.getPlayer(pName) != null;
					html += "<tr>";
					html = html + "<td width=140>" + pName;
					html += on ? " <font color=\"33CC66\">(on)</font>" : "";
					html += "</td>";
					html = html + "<td width=45><button width=50 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\" action=\"bypass -h admin_moders_log " + oid + "\" value=\"Logs\"></td>";
					html = html + "<td width=45><button width=20 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\" action=\"bypass -h admin_moders_del " + oid + "\" value=\"X\"></td>";
					html += "</tr>";
				}
		html += "</table>";
		reply.setHtml(html);
		activeChar.sendPacket(reply);
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminChangeAccessLevel._adminCommands;
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
		_log = LoggerFactory.getLogger(AdminChangeAccessLevel.class);
		AdminChangeAccessLevel._adminCommands = new String[] { "admin_changelvl", "admin_moders", "admin_moders_add", "admin_moders_del" };
	}

	private enum Commands
	{
		admin_changelvl,
		admin_moders,
		admin_moders_add,
		admin_moders_del;
	}
}
