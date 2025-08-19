package commands.admin;

import l2s.gameserver.Announcements;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.AutoAnnounces;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.ScriptFile;

public class AdminAnnouncements implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanAnnounce)
			return false;
		final String[] args = command.split(" ");
		if(args[0].equals("admin_list_announcements"))
			Announcements.getInstance().listAnnouncements(activeChar);
		else if(args[0].equals("admin_announce_menu"))
		{
			final Announcements sys = new Announcements();
			sys.handleAnnounce(command, 20);
			Announcements.getInstance().listAnnouncements(activeChar);
		}
		else if(args[0].equals("admin_announce_announcements"))
		{
			Announcements.getInstance().loadAnnouncements();
			for(final Player player : GameObjectsStorage.getPlayers())
				Announcements.getInstance().showAnnouncements(player);
			Announcements.getInstance().listAnnouncements(activeChar);
		}
		else if(args[0].equals("admin_add_announcement"))
		{
			if(args.length < 2)
				return false;
			try
			{
				final String val = command.substring(23);
				Announcements.getInstance().addAnnouncement(val);
				Announcements.getInstance().listAnnouncements(activeChar);
			}
			catch(StringIndexOutOfBoundsException ex)
			{}
		}
		else if(args[0].equals("admin_del_announcement"))
		{
			if(args.length < 2)
				return false;
			try
			{
				final int val2 = new Integer(command.substring(23));
				Announcements.getInstance().delAnnouncement(val2);
				Announcements.getInstance().listAnnouncements(activeChar);
			}
			catch(StringIndexOutOfBoundsException ex2)
			{}
		}
		else if(args[0].equals("admin_announce"))
		{
			final Announcements sys = new Announcements();
			sys.handleAnnounce(command, 15);
		}
		else if(args[0].equals("admin_a"))
		{
			final Announcements sys = new Announcements();
			sys.handleAnnounce(command, 8);
		}
		else if(args[0].equals("admin_crit_announce") || args[0].equals("admin_c"))
		{
			final Announcements sys = new Announcements();
			sys.handleAnnounce(command, 20, ChatType.CRITICAL_ANNOUNCE);
		}
		else if(args[0].equals("admin_toscreen"))
		{
			if(args.length < 2)
				return false;
			final String _text = command.replaceFirst("admin_toscreen ", "");
			final int _time = 3000 + _text.length() * 100;
			final boolean _font_big = _text.length() < 64;
			final ExShowScreenMessage sm = new ExShowScreenMessage(_text, _time, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, _font_big);
			for(final Player player2 : GameObjectsStorage.getPlayers())
				player2.sendPacket(sm);
		}
		else if(args[0].equals("admin_autoannounce_start"))
		{
			ServerVariables.set("AutoAnnounces", "on");
			AutoAnnounces.start();
			activeChar.sendMessage("AutoAnnouncer started.");
		}
		else if(args[0].equals("admin_autoannounce_stop"))
		{
			ServerVariables.unset("AutoAnnounces");
			AutoAnnounces.stop();
			activeChar.sendMessage("AutoAnnouncer stopped.");
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminAnnouncements._adminCommands;
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
		AdminAnnouncements._adminCommands = new String[] {
				"admin_list_announcements",
				"admin_announce_announcements",
				"admin_add_announcement",
				"admin_del_announcement",
				"admin_announce",
				"admin_a",
				"admin_announce_menu",
				"admin_crit_announce",
				"admin_c",
				"admin_toscreen",
				"admin_autoannounce_start",
				"admin_autoannounce_stop" };
	}
}
