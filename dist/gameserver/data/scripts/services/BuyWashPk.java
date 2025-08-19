package services;

import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.ItemTable;

public class BuyWashPk extends Functions
{
	public void list()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_WASH_PK_ITEM < 1)
		{
			if(player.isLangRus())
				player.sendMessage("Сервис отключен.");
			else
				player.sendMessage("Service is disabled.");
			return;
		}
		String html = null;
		html = HtmCache.getInstance().getHtml("scripts/services/BuyWashPk.htm", player);
		String add = "";
		final int pkc = player.getPkKills();
		int n = pkc / Config.SERVICES_WASH_PK_COUNT;
		final int rest = pkc % Config.SERVICES_WASH_PK_COUNT;
		if(n > 0 && n <= 100)
		{
			for(int i = 1; i <= n; ++i)
				add = add + "<a action=\"bypass -h scripts_services.BuyWashPk:get " + i * Config.SERVICES_WASH_PK_COUNT + "\">" + i * Config.SERVICES_WASH_PK_COUNT + " PK - " + Config.SERVICES_WASH_PK_PRICE * i + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_WASH_PK_ITEM).getName() + "</a><br1>";
			if(rest > 0)
				add = add + "<a action=\"bypass -h scripts_services.BuyWashPk:get " + (n * Config.SERVICES_WASH_PK_COUNT + rest) + "\">" + (n * Config.SERVICES_WASH_PK_COUNT + rest) + " PK - " + Config.SERVICES_WASH_PK_PRICE * (n + 1) + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_WASH_PK_ITEM).getName() + "</a><br1>";
		}
		else if(pkc > 0)
		{
			if(rest > 0)
				++n;
			add = add + "<a action=\"bypass -h scripts_services.BuyWashPk:get " + pkc + "\">" + pkc + " PK - " + Config.SERVICES_WASH_PK_PRICE * n + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_WASH_PK_ITEM).getName() + "</a><br>";
		}
		else if(player.isLangRus())
			add += "\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442 \u043d\u0438 \u0435\u0434\u0438\u043d\u043e\u0433\u043e \u041f\u041a. \u0412\u043e\u0437\u0432\u0440\u0430\u0449\u0430\u0439\u0442\u0435\u0441\u044c \u043a\u043e\u0433\u0434\u0430 \u0438\u0437\u0431\u0430\u0432\u0438\u0442\u0435\u0441\u044c \u043e\u0442 \u0431\u0440\u0435\u043c\u0435\u043d\u0438 \u0441\u0432\u044f\u0449\u0435\u043d\u043d\u043e\u0441\u0442\u0438 \u0438\u043b\u0438 \u043d\u0430\u0440\u0443\u0448\u0438\u0442\u0435 \u0437\u0430\u043a\u043e\u043d.<br>";
		else
			add += "You don't have PK. Come back later.<br>";
		html = html.replaceFirst("%toreplace%", add);
		show(html, player);
	}

	public void get(final String[] param)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_WASH_PK_ITEM < 1)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service is disabled.");
			return;
		}
		if(player.isCursedWeaponEquipped())
		{
			player.sendMessage(player.isLangRus() ? "\u0421 \u043f\u0440\u043e\u043a\u043b\u044f\u0442\u044b\u043c \u043e\u0440\u0443\u0436\u0438\u0435\u043c \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e." : "You can't do it with cursed weapon.");
			return;
		}
		final int pkc = Integer.parseInt(param[0]);
		int n = pkc / Config.SERVICES_WASH_PK_COUNT;
		if(pkc % Config.SERVICES_WASH_PK_COUNT > 0)
			++n;
		final int c = Config.SERVICES_WASH_PK_PRICE * n;
		if(deleteItem(player, Config.SERVICES_WASH_PK_ITEM, c))
		{
			player.setPkKills(player.getPkKills() - pkc);
			if(player.getPkKills() < 1)
				player.setKarma(0);
			player.sendChanges();
			if(player.isLangRus())
				player.sendMessage("\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043e\u0442\u043c\u044b\u043b\u0438 " + pkc + " \u041f\u041a.");
			else
				player.sendMessage("You have washed " + pkc + " PK successfully.");
		}
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}
}
