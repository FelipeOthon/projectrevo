package commands.admin;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.instancemanager.CursedWeaponsManager;
import l2s.gameserver.model.CursedWeapon;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;

public class AdminCursedWeapons implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().Menu)
			return false;
		final CursedWeaponsManager cwm = CursedWeaponsManager.getInstance();
		int id = 0;
		final StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		if(command.startsWith("admin_cw_info"))
		{
			activeChar.sendMessage("======= Cursed Weapons: =======");
			for(final CursedWeapon cw : cwm.getCursedWeapons())
			{
				activeChar.sendMessage("> " + cw.getName() + " (" + cw.getItemId() + ")");
				if(cw.isActivated())
				{
					final Player pl = cw.getPlayer();
					activeChar.sendMessage("  Player holding: " + pl.getName());
					activeChar.sendMessage("  Player karma: " + cw.getPlayerKarma());
					activeChar.sendMessage("  Time Remaining: " + cw.getTimeLeft() / 60000L + " min.");
					activeChar.sendMessage("  Kills : " + cw.getNbKills());
				}
				else if(cw.isDropped())
				{
					activeChar.sendMessage("  Lying on the ground.");
					activeChar.sendMessage("  Time Remaining: " + cw.getTimeLeft() / 60000L + " min.");
					activeChar.sendMessage("  Kills : " + cw.getNbKills());
				}
				else
					activeChar.sendMessage("  Don't exist in the world.");
			}
		}
		else if(command.startsWith("admin_cw_reload"))
		{
			cwm.reload();
			activeChar.sendMessage("Cursed weapons reloaded.");
		}
		else
		{
			CursedWeapon cw2 = null;
			try
			{
				String parameter = st.nextToken();
				final Pattern pattern = Pattern.compile("[0-9]*");
				final Matcher regexp = pattern.matcher(parameter);
				if(regexp.matches())
					id = Integer.parseInt(parameter);
				else
				{
					parameter = parameter.replace('_', ' ');
					for(final CursedWeapon cwp : cwm.getCursedWeapons())
						if(cwp.getName().toLowerCase().contains(parameter.toLowerCase()))
						{
							id = cwp.getItemId();
							break;
						}
				}
				cw2 = cwm.getCursedWeapon(id);
				if(cw2 == null)
				{
					activeChar.sendMessage("Unknown cursed weapon ID.");
					return false;
				}
			}
			catch(Exception e)
			{
				activeChar.sendMessage("Usage: //cw_remove|//cw_goto|//cw_add <itemid|name>");
			}
			if(cw2 == null)
				return false;
			if(command.startsWith("admin_cw_remove "))
				CursedWeaponsManager.getInstance().endOfLife(cw2);
			else if(command.startsWith("admin_cw_goto "))
				activeChar.teleToLocation(cw2.getLoc());
			else if(command.startsWith("admin_cw_add"))
			{
				if(cw2.isActive())
					activeChar.sendMessage("This cursed weapon is already active.");
				else
				{
					final GameObject target = activeChar.getTarget();
					if(target != null && target.isPlayer())
					{
						final Player player = (Player) target;
						final ItemInstance item = ItemTable.getInstance().createItem(id);
						cwm.activate(player, player.getInventory().addItem(item));
						cwm.showUsageTime(player, cw2);
					}
				}
			}
			else
				activeChar.sendMessage("Unknown command.");
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminCursedWeapons.ADMIN_COMMANDS;
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
		ADMIN_COMMANDS = new String[] { "admin_cw_info", "admin_cw_remove", "admin_cw_goto", "admin_cw_reload", "admin_cw_add" };
	}
}
