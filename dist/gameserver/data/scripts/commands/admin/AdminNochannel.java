package commands.admin;

import java.util.StringTokenizer;

import l2s.gameserver.Announcements;
import l2s.gameserver.Config;
import l2s.gameserver.database.mysql;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.instancemanager.PlayerManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Log;

public class AdminNochannel implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;
	private static Boolean announce_;
	private static Boolean announce_nick_;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanBanChat)
			return false;
		if(command.startsWith("admin_n"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 1)
			{
				st.nextToken();
				final String player = st.nextToken();
				final Player plr = World.getPlayer(player);
				if(plr != null)
				{
					int timeval = 30;
					String reason = "\u043d\u0435 \u0443\u043a\u0430\u0437\u0430\u043d\u0430";
					if(st.countTokens() >= 1)
					{
						final String time = st.nextToken();
						timeval = Integer.parseInt(time);
					}
					if(st.countTokens() >= 1)
					{
						reason = st.nextToken();
						while(st.hasMoreTokens())
							reason = reason + " " + st.nextToken();
					}
					final Announcements sys = new Announcements();
					if(timeval == 0)
					{
						activeChar.sendMessage("\u0412\u044b \u0441\u043d\u044f\u043b\u0438 \u0431\u0430\u043d \u0447\u0430\u0442\u0430 \u0441 \u0438\u0433\u0440\u043e\u043a\u0430 " + plr.getName() + ".");
						Log.add(activeChar.getName() + " \u0441\u043d\u044f\u043b(\u0430) \u0431\u0430\u043d \u0447\u0430\u0442\u0430 \u0441 \u0438\u0433\u0440\u043e\u043a\u0430 " + plr.getName() + ".", "banchat", activeChar);
					}
					else if(timeval < 0)
					{
						if(AdminNochannel.announce_)
							if(AdminNochannel.announce_nick_)
								sys.announceToAll(activeChar.getName() + " \u0437\u0430\u0431\u0430\u043d\u0438\u043b(\u0430) \u0447\u0430\u0442 \u0438\u0433\u0440\u043e\u043a\u0443 " + plr.getName() + " \u043d\u0430 \u0431\u0435\u0441\u0441\u0440\u043e\u0447\u043d\u044b\u0439 \u043f\u0435\u0440\u0438\u043e\u0434, \u043f\u0440\u0438\u0447\u0438\u043d\u0430: " + reason + ".");
							else
								sys.announceToAll("\u0417\u0430\u0431\u0430\u043d\u0435\u043d \u0447\u0430\u0442 \u0438\u0433\u0440\u043e\u043a\u0443 " + plr.getName() + " \u043d\u0430 \u0431\u0435\u0441\u0441\u0440\u043e\u0447\u043d\u044b\u0439 \u043f\u0435\u0440\u0438\u043e\u0434, \u043f\u0440\u0438\u0447\u0438\u043d\u0430: " + reason + ".");
						activeChar.sendMessage("\u0412\u044b \u0437\u0430\u0431\u0430\u043d\u0438\u043b\u0438 \u0447\u0430\u0442 \u0438\u0433\u0440\u043e\u043a\u0443 " + plr.getName() + " \u043d\u0430 \u0431\u0435\u0441\u0441\u0440\u043e\u0447\u043d\u044b\u0439 \u043f\u0435\u0440\u0438\u043e\u0434, \u043f\u0440\u0438\u0447\u0438\u043d\u0430: " + reason + ".");
						Log.add(activeChar.getName() + " \u0437\u0430\u0431\u0430\u043d\u0438\u043b(\u0430) \u0447\u0430\u0442 \u0438\u0433\u0440\u043e\u043a\u0443 " + plr.getName() + " \u043d\u0430 \u0431\u0435\u0441\u0441\u0440\u043e\u0447\u043d\u044b\u0439 \u043f\u0435\u0440\u0438\u043e\u0434, \u043f\u0440\u0438\u0447\u0438\u043d\u0430: " + reason + ".", "banchat", activeChar);
					}
					else
					{
						if(AdminNochannel.announce_)
							if(AdminNochannel.announce_nick_)
								sys.announceToAll(activeChar.getName() + " \u0437\u0430\u0431\u0430\u043d\u0438\u043b(\u0430) \u0447\u0430\u0442 \u0438\u0433\u0440\u043e\u043a\u0443 " + plr.getName() + " \u043d\u0430 " + timeval + " \u043c\u0438\u043d, \u043f\u0440\u0438\u0447\u0438\u043d\u0430: " + reason + ".");
							else
								sys.announceToAll("\u0417\u0430\u0431\u0430\u043d\u0435\u043d \u0447\u0430\u0442 \u0438\u0433\u0440\u043e\u043a\u0443 " + plr.getName() + " \u043d\u0430 " + timeval + " \u043c\u0438\u043d, \u043f\u0440\u0438\u0447\u0438\u043d\u0430: " + reason + ".");
						activeChar.sendMessage("\u0412\u044b \u0437\u0430\u0431\u0430\u043d\u0438\u043b\u0438 \u0447\u0430\u0442 \u0438\u0433\u0440\u043e\u043a\u0443 " + plr.getName() + " \u043d\u0430 " + timeval + " \u043c\u0438\u043d, \u043f\u0440\u0438\u0447\u0438\u043d\u0430: " + reason + ".");
						Log.add(activeChar.getName() + " \u0437\u0430\u0431\u0430\u043d\u0438\u043b(\u0430) \u0447\u0430\u0442 \u0438\u0433\u0440\u043e\u043a\u0443 " + plr.getName() + " \u043d\u0430 " + timeval + " \u043c\u0438\u043d, \u043f\u0440\u0438\u0447\u0438\u043d\u0430: " + reason + ".", "banchat", activeChar);
					}
					updateNoChannel(plr, timeval);
				}
				else
				{
					final int id = PlayerManager.getObjectIdByName(player);
					if(id > 0)
					{
						int time2 = 30;
						if(st.hasMoreTokens())
							time2 = Integer.parseInt(st.nextToken());
						mysql.set("UPDATE characters SET nochannel=? WHERE obj_Id=? LIMIT 1", new Object[] { time2 > 0 ? time2 * 60 : time2, id });
						if(time2 == 0)
							activeChar.sendMessage("\u0412\u044b \u0441\u043d\u044f\u043b\u0438 \u0431\u0430\u043d \u0447\u0430\u0442\u0430 \u0441 \u0438\u0433\u0440\u043e\u043a\u0430 " + player + ".");
						else if(time2 < 0)
							activeChar.sendMessage("\u0412\u044b \u0437\u0430\u0431\u0430\u043d\u0438\u043b\u0438 \u0447\u0430\u0442 \u0438\u0433\u0440\u043e\u043a\u0443 " + player + " \u043d\u0430 \u0431\u0435\u0441\u0441\u0440\u043e\u0447\u043d\u044b\u0439 \u043f\u0435\u0440\u0438\u043e\u0434.");
						else
							activeChar.sendMessage("\u0412\u044b \u0437\u0430\u0431\u0430\u043d\u0438\u043b\u0438 \u0447\u0430\u0442 \u0438\u0433\u0440\u043e\u043a\u0443 " + player + " \u043d\u0430 " + time2 + " \u043c\u0438\u043d.");
					}
					else
						activeChar.sendMessage("\u0418\u0433\u0440\u043e\u043a " + player + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d.");
				}
			}
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminNochannel._adminCommands;
	}

	private void updateNoChannel(final Player player, final int time)
	{
		player.updateNoChannel(time * 60000);
		if(time == 0)
			player.sendMessage(new CustomMessage("common.ChatUnBanned"));
		else if(time > 0)
			player.sendMessage(new CustomMessage("common.ChatBanned").addNumber(time));
		else
			player.sendMessage(new CustomMessage("common.ChatBannedPermanently"));
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
		AdminNochannel._adminCommands = new String[] { "admin_n" };
		AdminNochannel.announce_ = Config.MAT_ANNOUNCE;
		AdminNochannel.announce_nick_ = Config.MAT_ANNOUNCE_NICK;
	}
}
