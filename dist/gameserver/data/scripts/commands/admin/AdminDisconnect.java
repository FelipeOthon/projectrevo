package commands.admin;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class AdminDisconnect implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanKick)
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
			case admin_disconnect:
			case admin_kick:
			{
				if(wordList.length == 1)
				{
					final GameObject target = activeChar.getTarget();
					if(target == null)
					{
						activeChar.sendMessage("Select character or specify player name.");
						break;
					}
					if(!target.isPlayer())
					{
						activeChar.sendPacket(Msg.INCORRECT_TARGET);
						break;
					}
					final Player player = (Player) target;
					break;
				}
				else
				{
					final Player player = World.getPlayer(wordList[1]);
					if(player == null)
					{
						try
						{
							final int radius = Math.max(Integer.parseInt(wordList[1]), 100);
							for(final Player pr : World.getAroundPlayers(activeChar, radius, 200))
								if(pr != null)
									kick(pr);
							activeChar.sendMessage("Apply kick within " + radius + " unit radius.");
						}
						catch(NumberFormatException e2)
						{
							activeChar.sendMessage("Enter valid player name or radius!");
						}
						break;
					}
					if(player.getObjectId() == activeChar.getObjectId())
					{
						activeChar.sendMessage("You can't logout your character.");
						break;
					}
					activeChar.sendMessage("Character " + player.getName() + " disconnected from server.");
					kick(player);
					break;
				}
			}
		}
		return true;
	}

	private static void kick(final Player player)
	{
		if(player.isInOfflineMode())
		{
			player.setOfflineMode(false);
			player.kick(false);
			return;
		}
		player.sendMessage(new CustomMessage("scripts.commands.admin.AdminDisconnect.YoureKickedByGM"));
		player.sendPacket(new SystemMessage(127));
		player.startKickTask(500L, true);
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminDisconnect._adminCommands;
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
		AdminDisconnect._adminCommands = new String[] { "admin_disconnect", "admin_kick" };
	}

	private enum CommandEnum
	{
		admin_disconnect,
		admin_kick;
	}
}
