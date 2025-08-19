package commands.admin;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.s2c.Say2;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.GmListTable;

public class AdminGmChat implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanAnnounce)
			return false;
		if(command.startsWith("admin_gmchat"))
			handleGmChat(command, activeChar);
		else if(command.startsWith("admin_snoop"))
			snoop(command, activeChar);
		return true;
	}

	private void snoop(final String command, final Player activeChar)
	{
		GameObject target = activeChar.getTarget();
		if(command.length() > 12)
		{
			final String val = command.substring(12);
			final Player player = GameObjectsStorage.getPlayer(val);
			if(player != null)
				target = player;
		}
		if(target == null)
		{
			activeChar.sendMessage("You must select a target.");
			return;
		}
		if(!target.isPlayer())
		{
			activeChar.sendMessage("Target must be a player.");
			return;
		}
		final Player player2 = (Player) target;
		player2.addSnooper(activeChar);
		activeChar.addSnooped(player2);
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminGmChat._adminCommands;
	}

	private void handleGmChat(final String command, final Player activeChar)
	{
		try
		{
			final String text = command.substring(13);
			final Say2 cs = new Say2(0, ChatType.ALLIANCE, activeChar.getName(), text);
			GmListTable.broadcastToGMs(cs);
		}
		catch(StringIndexOutOfBoundsException ex)
		{}
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
		AdminGmChat._adminCommands = new String[] { "admin_gmchat", "admin_snoop" };
	}
}
