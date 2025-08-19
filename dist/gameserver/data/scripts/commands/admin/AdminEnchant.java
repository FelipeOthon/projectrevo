package commands.admin;

import javolution.text.TextBuilder;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.InventoryUpdate;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Log;

public class AdminEnchant implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanEditChar)
			return false;
		if(command.equals("admin_enchant"))
			showMainPage(activeChar);
		else
		{
			int armorType = -1;
			if(command.startsWith("admin_seteh"))
				armorType = 6;
			else if(command.startsWith("admin_setec"))
				armorType = 10;
			else if(command.startsWith("admin_seteg"))
				armorType = 9;
			else if(command.startsWith("admin_seteb"))
				armorType = 12;
			else if(command.startsWith("admin_setel"))
				armorType = 11;
			else if(command.startsWith("admin_setew"))
				armorType = 7;
			else if(command.startsWith("admin_setes"))
				armorType = 8;
			else if(command.startsWith("admin_setle"))
				armorType = 1;
			else if(command.startsWith("admin_setre"))
				armorType = 2;
			else if(command.startsWith("admin_setlf"))
				armorType = 4;
			else if(command.startsWith("admin_setrf"))
				armorType = 5;
			else if(command.startsWith("admin_seten"))
				armorType = 3;
			else if(command.startsWith("admin_setun"))
				armorType = 0;
			if(armorType != -1)
				try
				{
					final int ench = Integer.parseInt(command.substring(12));
					if(ench < 0 || ench > 65535)
						activeChar.sendMessage("You must set the enchant level to be between 0-65535.");
					else
						setEnchant(activeChar, ench, armorType);
				}
				catch(StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("Please specify a new enchant value.");
				}
				catch(NumberFormatException e2)
				{
					activeChar.sendMessage("Please specify a valid new enchant value.");
				}
			showMainPage(activeChar);
		}
		return true;
	}

	private void setEnchant(final Player activeChar, final int ench, final int armorType)
	{
		GameObject target = activeChar.getTarget();
		if(target == null)
			target = activeChar;
		if(!target.isPlayer())
		{
			activeChar.sendMessage("Wrong target type.");
			return;
		}
		final Player player = (Player) target;
		int curEnchant = 0;
		final ItemInstance itemInstance = player.getInventory().getPaperdollItem(armorType);
		if(itemInstance != null)
		{
			curEnchant = itemInstance.getEnchantLevel();
			itemInstance.setEnchantLevel(ench);
			player.sendPacket(new InventoryUpdate().addModifiedItem(itemInstance));
			player.broadcastUserInfo(true);
			activeChar.sendMessage("Changed enchantment of " + player.getName() + "'s " + itemInstance.getTemplate().getName() + " from " + curEnchant + " to " + ench + ".");
			if(activeChar.getObjectId() != player.getObjectId())
				player.sendMessage("Admin has changed the enchantment of your " + itemInstance.getTemplate().getName() + " from " + curEnchant + " to " + ench + ".");
			Log.addLog(activeChar.toString() + " change the enchantment of " + player.toString() + " " + itemInstance.getTemplate().getName() + " from " + curEnchant + " to " + ench + ".", "gm_actions");
		}
	}

	public void showMainPage(final Player activeChar)
	{
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final TextBuilder replyMSG = new TextBuilder("<html><body>");
		replyMSG.append("<center><table width=260><tr><td width=40>");
		replyMSG.append("<button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
		replyMSG.append("</td><td width=180>");
		replyMSG.append("<center>Enchant Equip</center>");
		replyMSG.append("</td><td width=40>");
		replyMSG.append("</td></tr></table></center><br>");
		replyMSG.append("<center><table width=270><tr><td>");
		replyMSG.append("<button value=\"Underwear\" action=\"bypass -h admin_setun $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Helmet\" action=\"bypass -h admin_seteh $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Cloak\" action=\"bypass -h admin_setba $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Mask\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Necklace\" action=\"bypass -h admin_seten $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table>");
		replyMSG.append("</center><center><table width=270><tr><td>");
		replyMSG.append("<button value=\"Weapon\" action=\"bypass -h admin_setew $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Chest\" action=\"bypass -h admin_setec $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Shield\" action=\"bypass -h admin_setes $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Earring\" action=\"bypass -h admin_setre $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Earring\" action=\"bypass -h admin_setle $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table>");
		replyMSG.append("</center><center><table width=270><tr><td>");
		replyMSG.append("<button value=\"Gloves\" action=\"bypass -h admin_seteg $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Leggings\" action=\"bypass -h admin_setel $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Boots\" action=\"bypass -h admin_seteb $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Ring\" action=\"bypass -h admin_setrf $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td>");
		replyMSG.append("<button value=\"Ring\" action=\"bypass -h admin_setlf $menu_command\" width=50 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table>");
		replyMSG.append("</center><br>");
		replyMSG.append("<center>[Enchant 0-65535]</center>");
		replyMSG.append("<center><edit var=\"menu_command\" width=100 height=15></center><br>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminEnchant.ADMIN_COMMANDS;
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
		ADMIN_COMMANDS = new String[] {
				"admin_seteh",
				"admin_setec",
				"admin_seteg",
				"admin_setel",
				"admin_seteb",
				"admin_setew",
				"admin_setes",
				"admin_setle",
				"admin_setre",
				"admin_setlf",
				"admin_setrf",
				"admin_seten",
				"admin_setun",
				"admin_enchant" };
	}
}
