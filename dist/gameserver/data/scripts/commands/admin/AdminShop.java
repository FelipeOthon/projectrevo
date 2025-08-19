package commands.admin;

import l2s.gameserver.TradeController;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.BuyList;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Stat;

public class AdminShop implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().UseGMShop)
			return false;
		if(command.startsWith("admin_buy"))
			try
			{
				handleBuyRequest(activeChar, command.substring(10));
			}
			catch(IndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Please specify buylist.");
			}
		else if(command.equals("admin_gmshop"))
			AdminHelpPage.showHelpPage(activeChar, "gmshops.htm");
		else if(command.equals("admin_tax"))
			activeChar.sendMessage("TaxSum: " + Stat.getTaxSum());
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminShop._adminCommands;
	}

	private void handleBuyRequest(final Player activeChar, final String command)
	{
		int val = -1;
		try
		{
			val = Integer.parseInt(command);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		final TradeController.NpcTradeList list = TradeController.getInstance().getBuyList(val);
		if(list != null)
		{
			activeChar.setLastNpcId(-1);
			activeChar.setBuyListId(val);
			final BuyList bl = new BuyList(list, activeChar.getAdena());
			activeChar.sendPacket(bl);
		}
		activeChar.sendActionFailed();
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
		AdminShop._adminCommands = new String[] { "admin_buy", "admin_gmshop", "admin_tax" };
	}
}
