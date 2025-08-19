package services;

import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.templates.item.ItemTemplate;

public class ExpandInventory extends Functions implements ScriptFile
{
	public void get()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.SERVICES_EXPAND_INVENTORY_ENABLED)
		{
			show("Сервис отключен.", player);
			return;
		}
		if(player.getInventoryLimit() >= Config.SERVICES_EXPAND_INVENTORY_MAX)
		{
			player.sendMessage("Already max count.");
			return;
		}

		if(deleteItem(player, Config.SERVICES_EXPAND_INVENTORY_ITEM, Config.SERVICES_EXPAND_INVENTORY_PRICE))
		{
			player.setExpandInventory(player.getExpandInventory() + 1);
			player.setVar("ExpandInventory", String.valueOf(player.getExpandInventory()));
			player.sendMessage("Inventory capacity is now " + player.getInventoryLimit());
		}
		else if(Config.SERVICES_EXPAND_INVENTORY_ITEM == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
		this.show();
	}

	public void show()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.SERVICES_EXPAND_INVENTORY_ENABLED)
		{
			show("Сервис отключен.", player);
			return;
		}
		final ItemTemplate item = ItemTable.getInstance().getTemplate(Config.SERVICES_EXPAND_INVENTORY_ITEM);
		String out = "";
		out += "<html><body>\u0420\u0430\u0441\u0448\u0438\u0440\u0435\u043d\u0438\u0435 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044f";
		out += "<br><br><table>";
		out = out + "<tr><td>\u0422\u0435\u043a\u0443\u0449\u0438\u0439 \u0440\u0430\u0437\u043c\u0435\u0440:</td><td>" + player.getInventoryLimit() + "</td></tr>";
		out = out + "<tr><td>\u041c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u044c\u043d\u044b\u0439 \u0440\u0430\u0437\u043c\u0435\u0440:</td><td>" + Config.SERVICES_EXPAND_INVENTORY_MAX + "</td></tr>";
		out = out + "<tr><td>\u0421\u0442\u043e\u0438\u043c\u043e\u0441\u0442\u044c \u0441\u043b\u043e\u0442\u0430:</td><td>" + Config.SERVICES_EXPAND_INVENTORY_PRICE + " " + item.getName() + "</td></tr>";
		out += "</table><br><br>";
		out += "<button width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\" action=\"bypass -h scripts_services.ExpandInventory:get\" value=\"\u0420\u0430\u0441\u0448\u0438\u0440\u0438\u0442\u044c\">";
		out += "</body></html>";
		show(out, player);
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Expand Inventory");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
