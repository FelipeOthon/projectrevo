package commands.admin;

import java.util.StringTokenizer;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.World;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Location;

public class AdminMenu implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().Menu)
			return false;
		if(command.equals("admin_char_manage"))
			AdminHelpPage.showHelpPage(activeChar, "charmanage.htm");
		else if(command.startsWith("admin_teleport_character_to_menu"))
		{
			final String[] data = command.split(" ");
			if(data.length == 5)
			{
				final String playerName = data[1];
				final Player player = World.getPlayer(playerName);
				if(player != null)
					teleportCharacter(player, new Location(Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4])), activeChar);
			}
			AdminHelpPage.showHelpPage(activeChar, "charmanage.htm");
		}
		else if(command.startsWith("admin_recall_char_menu"))
			try
			{
				final String targetName = command.substring(23);
				final Player player2 = World.getPlayer(targetName);
				teleportCharacter(player2, activeChar.getLoc(), activeChar);
			}
			catch(StringIndexOutOfBoundsException ex)
			{}
		else if(command.startsWith("admin_goto_char_menu"))
			try
			{
				final String targetName = command.substring(21);
				final Player player2 = World.getPlayer(targetName);
				teleportToCharacter(activeChar, player2);
			}
			catch(StringIndexOutOfBoundsException ex2)
			{}
		else if(command.equals("admin_kill_menu"))
		{
			GameObject obj = activeChar.getTarget();
			final StringTokenizer st = new StringTokenizer(command);
			if(st.countTokens() > 1)
			{
				st.nextToken();
				final String player3 = st.nextToken();
				final Player plyr = World.getPlayer(player3);
				if(plyr != null)
					activeChar.sendMessage("You kicked " + plyr.getName() + " from the game.");
				else
					activeChar.sendMessage("Player " + player3 + " not found in game.");
				obj = plyr;
			}
			if(obj != null && obj.isCreature())
			{
				final Creature target = (Creature) obj;
				target.reduceCurrentHp(target.getMaxHp() + 1, activeChar, (Skill) null, 0, false, true, true, true, false, false, false, false);
			}
			else
				activeChar.sendPacket(Msg.INCORRECT_TARGET);
			AdminHelpPage.showHelpPage(activeChar, "charmanage.htm");
		}
		else if(command.startsWith("admin_kick_menu"))
		{
			final StringTokenizer st2 = new StringTokenizer(command);
			if(st2.countTokens() > 1)
			{
				st2.nextToken();
				final String player4 = st2.nextToken();
				final Player plyr2 = World.getPlayer(player4);
				if(plyr2 != null)
					plyr2.kick(true);
				if(plyr2 != null)
					activeChar.sendMessage("You kicked " + plyr2.getName() + " from the game.");
				else
					activeChar.sendMessage("Player " + player4 + " not found in game.");
			}
			AdminHelpPage.showHelpPage(activeChar, "charmanage.htm");
		}
		else if(command.startsWith("admin_ban_menu"))
		{
			final StringTokenizer st2 = new StringTokenizer(command);
			if(st2.countTokens() > 1)
			{
				st2.nextToken();
				final String player4 = st2.nextToken();
				final Player plyr2 = World.getPlayer(player4);
				if(plyr2 != null)
				{
					plyr2.setAccountAccesslevel(-100, -1);
					plyr2.kick(true);
				}
			}
			AdminHelpPage.showHelpPage(activeChar, "cban.htm");
		}
		else if(command.startsWith("admin_unban_menu"))
		{
			final StringTokenizer st2 = new StringTokenizer(command);
			if(st2.countTokens() > 1)
			{
				st2.nextToken();
				final String player4 = st2.nextToken();
				final Player plyr2 = World.getPlayer(player4);
				if(plyr2 != null)
					plyr2.setAccountAccesslevel(0, 0);
			}
			AdminHelpPage.showHelpPage(activeChar, "cban.htm");
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminMenu._adminCommands;
	}

	private void teleportCharacter(final Player player, final Location loc, final Player activeChar)
	{
		if(player != null)
		{
			player.sendMessage("Admin is teleporting you.");
			player.teleToLocation(loc);
		}
		AdminHelpPage.showHelpPage(activeChar, "charmanage.htm");
	}

	private void teleportToCharacter(final Player activeChar, final GameObject target)
	{
		if(target != null && target.isPlayer())
		{
			final Player player = (Player) target;
			if(player.getObjectId() == activeChar.getObjectId())
				activeChar.sendMessage("You can't teleport to self.");
			else
			{
				activeChar.teleToLocation(player.getLoc());
				activeChar.sendMessage("You have teleported to character " + player.getName());
			}
			AdminHelpPage.showHelpPage(activeChar, "charmanage.htm");
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
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
		AdminMenu._adminCommands = new String[] {
				"admin_char_manage",
				"admin_teleport_character_to_menu",
				"admin_recall_char_menu",
				"admin_goto_char_menu",
				"admin_kick_menu",
				"admin_kill_menu",
				"admin_ban_menu",
				"admin_unban_menu" };
	}
}
