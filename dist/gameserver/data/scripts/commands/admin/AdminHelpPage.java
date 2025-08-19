package commands.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import commands.voiced.Security;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.instancemanager.L2TopManager;
import l2s.gameserver.instancemanager.PlayerManager;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.scripts.Scripts;
import l2s.gameserver.utils.TimeUtils;
import services.DonateParse;

public class AdminHelpPage implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().Menu || !activeChar.getPlayerAccess().CanUseGMCommand)
			return false;
		String args = null;
		if(command.startsWith("admin_showhtml"))
		{
			if(command.length() > AdminHelpPage._adminCommands[1].length() + 1)
				args = command.substring(AdminHelpPage._adminCommands[1].length()).trim();
			if(args == null)
			{
				activeChar.sendMessage("Usage: //showhtml <file>");
				return false;
			}
			showHelpPage(activeChar, args);
		}
		else if(command.startsWith("admin_pool"))
		{
			final StringBuilder sb = new StringBuilder();
			sb.append(ThreadPoolManager.getInstance().getStats());
			ScriptFile._log.info(sb.toString());
		}
		else if(command.startsWith("admin_nm"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 1)
			{
				st.nextToken();
				final NpcHtmlMessage msg = new NpcHtmlMessage(1);
				msg.setFile("castle/chamberlain/" + st.nextToken() + "-d.htm");
				activeChar.sendPacket(msg);
			}
		}
		else if(command.startsWith("admin_sid"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			String name = null;
			Player p = null;
			if(st.countTokens() > 1)
			{
				st.nextToken();
				name = st.nextToken();
				p = GameObjectsStorage.getPlayer(name);
			}
			else
			{
				final GameObject t = activeChar.getTarget();
				if(t != null && t.isPlayer())
					p = (Player) t;
			}
			if(p != null)
				activeChar.sendMessage(p.getName() + " objId: " + p.getObjectId());
			else if(name != null)
			{
				final int id = PlayerManager.getObjectIdByName(name);
				if(id <= 0)
					activeChar.sendMessage("Player " + name + " not exist.");
				else
				{
					name = PlayerManager.getNameByObjectId(id);
					activeChar.sendMessage(name + " objId: " + id);
				}
			}
		}
		else if(command.startsWith("admin_namebyid"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 1)
			{
				st.nextToken();
				final int id2 = Integer.parseInt(st.nextToken());
				final Player p = GameObjectsStorage.getPlayer(id2);
				if(p != null)
					activeChar.sendMessage("Name [" + p.getObjectId() + "]: " + p.getName());
			}
		}
		else if(command.startsWith("admin_accbyname"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 1)
			{
				st.nextToken();
				final String name = st.nextToken();
				final String acc = PlayerManager.getAccNameByName(name);
				if(acc.equals(""))
					activeChar.sendMessage("Character " + name + " not exist.");
				else
					activeChar.sendMessage(name + " account: " + acc);
			}
		}
		else if(command.equals("admin_online"))
		{
			if(!activeChar.getPlayerAccess().CanUseOnline)
				return false;
			List<String> hwids = new ArrayList<String>();
			int bots = 0;
			for(final Player player : GameObjectsStorage.getPlayers())
				if(player != null && !player.isInOfflineMode())
				{
					if(player.isGM())
						continue;
					if(player.isFashion)
						++bots;
					else
					{
						if(!player.isConnected() || player.getHWID().isEmpty() || hwids.contains(player.getHWID()))
							continue;
						hwids.add(player.getHWID());
					}
				}
			if(bots > 0)
				activeChar.sendMessage("\u0411\u043e\u0442\u043e\u0432 \u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u043e: " + bots);
			activeChar.sendMessage("\u0420\u0435\u0430\u043b\u044c\u043d\u044b\u0439 \u043e\u043d\u043b\u0430\u0439\u043d: " + hwids.size());
			hwids = null;
		}
		else if(command.startsWith("admin_charkey"))
		{
			if(!Config.GM_CAN_SEE_CHAR_KEY)
			{
				activeChar.sendMessage("Disabled by config.");
				return false;
			}
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 1)
			{
				st.nextToken();
				final String name = st.nextToken();
				final int id3 = PlayerManager.getObjectIdByName(name);
				if(id3 <= 0)
					activeChar.sendMessage("Character " + name + " not exist.");
				else if(Config.CHAR_KEYS.containsKey(id3))
					activeChar.sendMessage(name + " key: " + Config.CHAR_KEYS.get(id3));
				else
					activeChar.sendMessage("Character " + name + " don't have the key.");
			}
		}
		else if(command.equals("admin_savecharkeys"))
		{
			Security.save();
			activeChar.sendMessage("Characters keys saved.");
		}
		else if(command.equals("admin_clearcharkeys"))
		{
			final List<Integer> ids = new ArrayList<Integer>();
			for(final int i : Config.CHAR_KEYS.keySet())
				if(PlayerManager.getNameByObjectId(i).isEmpty())
					ids.add(i);
			if(!ids.isEmpty())
				for(final int i : ids)
					Config.CHAR_KEYS.remove(i);
			activeChar.sendMessage("Characters keys cleared.");
		}
		else if(command.equals("admin_l2top"))
		{
			if(Config.L2TopManagerEnabled)
			{
				if(L2TopManager.started)
				{
					L2TopManager.getInstance().stop();
					activeChar.sendMessage("L2Top disabled.");
				}
				else
				{
					L2TopManager.getInstance();
					activeChar.sendMessage("L2Top loaded.");
				}
			}
			else
				activeChar.sendMessage("L2Top not enabled.");
		}
		else if(command.equals("admin_donate"))
		{
			if(Config.ALLOW_DONATE_PARSE)
			{
				if(DonateParse.started)
				{
					DonateParse.stop();
					activeChar.sendMessage("Donate disabled.");
				}
				else
				{
					DonateParse.start();
					activeChar.sendMessage("Donate loaded.");
				}
			}
			else
				activeChar.sendMessage("Donate not enabled.");
		}
		else if(command.equalsIgnoreCase("admin_startfrintezza"))
			Scripts.getInstance().callScripts(activeChar, "bosses.FrintezzaManager", "startFrintezza");
		return true;
	}

	public static void showHelpPage(final Player targetChar, final String filename)
	{
		final String content = HtmCache.getInstance().getHtml("admin/" + filename, targetChar);
		if(content == null)
		{
			targetChar.sendMessage("Not found filename: " + filename);
			return;
		}
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		adminReply.setHtml(content);
		targetChar.sendPacket(adminReply);
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminHelpPage._adminCommands;
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
		AdminHelpPage._adminCommands = new String[] {
				"admin_showhtml",
				"admin_pool",
				"admin_nm",
				"admin_sid",
				"admin_namebyid",
				"admin_accbyname",
				"admin_online",
				"admin_charkey",
				"admin_savecharkeys",
				"admin_clearcharkeys",
				"admin_l2top",
				"admin_donate",
				"admin_startfrintezza" };
	}
}
