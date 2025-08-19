package commands.admin;

import java.util.StringTokenizer;

import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.s2c.ItemList;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.utils.Log;

public class AdminCreateItem implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().UseGMShop)
			return false;
		if(command.equals("admin_itemcreate"))
			AdminHelpPage.showHelpPage(activeChar, "itemcreation.htm");
		else if(command.startsWith("admin_create_item"))
		{
			try
			{
				final String val = command.substring(17);
				final StringTokenizer st = new StringTokenizer(val);
				if(st.countTokens() == 2)
				{
					final String id = st.nextToken();
					final int idval = Integer.parseInt(id);
					final String num = st.nextToken();
					final int numval = Integer.parseInt(num);
					final GameObject t = activeChar.getTarget();
					if(t != null && t.isPlayer())
						createItem((Player) t, idval, numval, activeChar);
					else
						createItem(activeChar, idval, numval, activeChar);
				}
				else if(st.countTokens() == 1)
				{
					final String id = st.nextToken();
					final int idval = Integer.parseInt(id);
					final GameObject t2 = activeChar.getTarget();
					if(t2 != null && t2.isPlayer())
						createItem((Player) t2, idval, 1, activeChar);
					else
						createItem(activeChar, idval, 1, activeChar);
				}
			}
			catch(NumberFormatException nfe)
			{
				activeChar.sendMessage("Specify a valid number.");
			}
			catch(StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Can't create this item.");
			}
			AdminHelpPage.showHelpPage(activeChar, "itemcreation.htm");
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminCreateItem._adminCommands;
	}

	private void createItem(final Player player, final int id, int num, final Player gm)
	{
		if(id < 1)
			return;
		try
		{
			ItemInstance createditem = ItemTable.getInstance().createItem(id);
			if(!createditem.isStackable())
				num = Math.min(num, 100);
			createditem.setCount(num);
			player.getInventory().addItem(createditem);
			Log.LogItem(player, "Create", createditem);
			if(!createditem.isStackable())
				for(int i = 0; i < num - 1; ++i)
				{
					createditem = ItemTable.getInstance().createItem(id);
					player.getInventory().addItem(createditem);
					Log.LogItem(player, "Create", createditem);
				}
			player.sendPacket(new IBroadcastPacket[] { new ItemList(player, false), SystemMessage.obtainItems(id, num, 0) });
			Log.addLog(gm.toString() + " spawned " + num + " item(s) '" + id + "' in inventory of player " + player.toString(), "gm_actions");
		}
		catch(Exception e)
		{
			gm.sendMessage("Specify a valid number.");
		}
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
		AdminCreateItem._adminCommands = new String[] { "admin_itemcreate", "admin_create_item" };
	}
}
