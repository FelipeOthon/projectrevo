package services;

import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;

public class NickColor extends Functions implements ScriptFile
{
	public void list()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final StringBuilder append = new StringBuilder();
		final String itemName = ItemTable.getInstance().getTemplate(Config.SERVICES_CHANGE_NICK_COLOR_ITEM).getName();
		final String itemName2 = ItemTable.getInstance().getTemplate(Config.SERVICES_CHANGE_TITLE_COLOR_ITEM).getName();
		if(player.isLangRus())
			append.append("<br><font color=\"LEVEL\">\u0421\u043c\u0435\u043d\u0438\u0442\u044c \u0446\u0432\u0435\u0442 \u043d\u0438\u043a\u0430 \u0437\u0430 ").append(Config.SERVICES_CHANGE_NICK_COLOR_PRICE).append(" ").append(itemName).append("</font><br>");
		else
			append.append("<br><font color=\"LEVEL\">You can change nick color for ").append(Config.SERVICES_CHANGE_NICK_COLOR_PRICE).append(" ").append(itemName).append("</font><br>");
		for(final String color : Config.SERVICES_CHANGE_NICK_COLOR_LIST)
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:change ").append(color).append("\"><font color=\"").append(color.substring(4, 6) + color.substring(2, 4) + color.substring(0, 2)).append("\">").append(color.substring(4, 6) + color.substring(2, 4) + color.substring(0, 2)).append("</font></a>");
		if(Config.SERVICES_CHANGE_NICK_COLOR_BLACK > 0)
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:change 111122\"><font color=\"000000\">000000</font></a>").append("<font color=\"LEVEL\"> (").append(Config.SERVICES_CHANGE_NICK_COLOR_BLACK).append(" ").append(itemName).append(")</font>");
		if(player.isLangRus())
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:change FFFFFF\"><font color=\"FFFFFF\">\u0412\u0435\u0440\u043d\u0443\u0442\u044c \u0441\u0442\u0430\u043d\u0434\u0430\u0440\u0442\u043d\u044b\u0439 \u0446\u0432\u0435\u0442 \u043d\u0438\u043a\u0430 (\u0431\u0435\u0441\u043f\u043b\u0430\u0442\u043d\u043e)</font></a><br><br><br>");
		else
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:change FFFFFF\"><font color=\"FFFFFF\">Revert nick color to default (free)</font></a><br><br><br>");
		if(player.isLangRus())
			append.append("<font color=\"LEVEL\">\u0421\u043c\u0435\u043d\u0438\u0442\u044c \u0446\u0432\u0435\u0442 \u0442\u0438\u0442\u0443\u043b\u0430 \u0437\u0430 ").append(Config.SERVICES_CHANGE_TITLE_COLOR_PRICE).append(" ").append(itemName2).append("</font><br>");
		else
			append.append("<font color=\"LEVEL\">You can change title color for ").append(Config.SERVICES_CHANGE_TITLE_COLOR_PRICE).append(" ").append(itemName2).append("</font><br>");
		for(final String color : Config.SERVICES_CHANGE_TITLE_COLOR_LIST)
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:changeTitle ").append(color).append("\"><font color=\"").append(color.substring(4, 6) + color.substring(2, 4) + color.substring(0, 2)).append("\">").append(color.substring(4, 6) + color.substring(2, 4) + color.substring(0, 2)).append("</font></a>");
		if(Config.SERVICES_CHANGE_TITLE_COLOR_BLACK > 0)
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:changeTitle 111122\"><font color=\"000000\">000000</font></a>").append("<font color=\"LEVEL\"> (").append(Config.SERVICES_CHANGE_TITLE_COLOR_BLACK).append(" ").append(itemName2).append(")</font>");
		if(player.isLangRus())
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:changeTitle FFFF77\"><font color=\"FFFF77\">\u0412\u0435\u0440\u043d\u0443\u0442\u044c \u0441\u0442\u0430\u043d\u0434\u0430\u0440\u0442\u043d\u044b\u0439 \u0446\u0432\u0435\u0442 \u0442\u0438\u0442\u0443\u043b\u0430 (\u0431\u0435\u0441\u043f\u043b\u0430\u0442\u043d\u043e)</font></a>");
		else
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:changeTitle FFFF77\"><font color=\"FFFF77\">Revert title color to default (free)</font></a>");
		show(append.toString(), player);
	}

	public void listTitle()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final StringBuilder append = new StringBuilder();
		final String itemName2 = ItemTable.getInstance().getTemplate(Config.SERVICES_CHANGE_TITLE_COLOR_ITEM).getName();
		if(player.isLangRus())
			append.append("<font color=\"LEVEL\">\u0421\u043c\u0435\u043d\u0438\u0442\u044c \u0446\u0432\u0435\u0442 \u0442\u0438\u0442\u0443\u043b\u0430 \u0437\u0430 ").append(Config.SERVICES_CHANGE_TITLE_COLOR_PRICE).append(" ").append(itemName2).append("</font><br>");
		else
			append.append("<font color=\"LEVEL\">You can change title color for ").append(Config.SERVICES_CHANGE_TITLE_COLOR_PRICE).append(" ").append(itemName2).append("</font><br>");
		for(final String color : Config.SERVICES_CHANGE_TITLE_COLOR_LIST)
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:changeTitle ").append(color).append("\"><font color=\"").append(color.substring(4, 6) + color.substring(2, 4) + color.substring(0, 2)).append("\">").append(color.substring(4, 6) + color.substring(2, 4) + color.substring(0, 2)).append("</font></a>");
		if(Config.SERVICES_CHANGE_TITLE_COLOR_BLACK > 0)
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:changeTitle 111122\"><font color=\"000000\">000000</font></a>").append("<font color=\"LEVEL\"> (").append(Config.SERVICES_CHANGE_TITLE_COLOR_BLACK).append(" ").append(itemName2).append(")</font>");
		if(player.isLangRus())
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:changeTitle FFFF77\"><font color=\"FFFF77\">\u0412\u0435\u0440\u043d\u0443\u0442\u044c \u0441\u0442\u0430\u043d\u0434\u0430\u0440\u0442\u043d\u044b\u0439 \u0446\u0432\u0435\u0442 \u0442\u0438\u0442\u0443\u043b\u0430 (\u0431\u0435\u0441\u043f\u043b\u0430\u0442\u043d\u043e)</font></a>");
		else
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:changeTitle FFFF77\"><font color=\"FFFF77\">Revert title color to default (free)</font></a>");
		show(append.toString(), player);
	}

	public void change(final String[] param)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(param[0].equalsIgnoreCase("FFFFFF"))
		{
			player.setNameColor(Integer.decode("0xFFFFFF"), true);
			player.broadcastUserInfo(true);
			return;
		}
		boolean isinlist = false;
		for(final String color : Config.SERVICES_CHANGE_NICK_COLOR_LIST)
			if(color.equalsIgnoreCase(param[0]))
			{
				isinlist = true;
				break;
			}
		if(!isinlist && !param[0].equalsIgnoreCase("111122"))
			return;
		final int count = param[0].equalsIgnoreCase("111122") ? Config.SERVICES_CHANGE_NICK_COLOR_BLACK : Config.SERVICES_CHANGE_NICK_COLOR_PRICE;
		if(getItemCount(player, Config.SERVICES_CHANGE_NICK_COLOR_ITEM) >= Config.SERVICES_CHANGE_NICK_COLOR_PRICE){
			if(deleteItem(player, Config.SERVICES_CHANGE_NICK_COLOR_ITEM, count))
			{
				player.setNameColor(Integer.decode("0x" + param[0]), true);
				player.broadcastUserInfo(true);
			}}
		else if(Config.SERVICES_CHANGE_NICK_COLOR_ITEM == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);

	}

	public void changeTitle(final String[] param)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(param[0].equalsIgnoreCase("FFFF77"))
		{
			player.setTitleColor(Integer.decode("0xFFFF77"));
			player.broadcastUserInfo(true);
			return;
		}
		boolean isinlist = false;
		for(final String color : Config.SERVICES_CHANGE_TITLE_COLOR_LIST)
			if(color.equalsIgnoreCase(param[0]))
			{
				isinlist = true;
				break;
			}
		if(!isinlist && !param[0].equalsIgnoreCase("111122"))
			return;
		final int count = param[0].equalsIgnoreCase("111122") ? Config.SERVICES_CHANGE_TITLE_COLOR_BLACK : Config.SERVICES_CHANGE_TITLE_COLOR_PRICE;
		if(getItemCount(player, Config.SERVICES_CHANGE_TITLE_COLOR_ITEM) >= Config.SERVICES_CHANGE_TITLE_COLOR_PRICE){
			if(deleteItem(player, Config.SERVICES_CHANGE_TITLE_COLOR_ITEM, count))
			{
				player.setTitleColor(Integer.decode("0x" + param[0]));
				player.broadcastUserInfo(true);
			}
		}
		else if(Config.SERVICES_CHANGE_NICK_COLOR_ITEM == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Nick color change");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
