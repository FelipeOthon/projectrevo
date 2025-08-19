package services;

import l2s.gameserver.Bonus;
import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.authcomm.AuthServerCommunication;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.utils.AutoBan;
import l2s.gameserver.utils.Log;
import l2s.gameserver.utils.TimeUtils;
import l2s.gameserver.utils.Util;

public class RateBonus extends Functions implements ScriptFile
{
	public void list()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final boolean ru = player.isLangRus();
		if(Config.SERVICES_RATE_TYPE < 1)
		{
			player.sendMessage(ru ? "Сервис отключен." : "Service disabled.");
			return;
		}
		String html = null;
		if(player.getNetConnection().getBonus() >= 0.0f)
		{
			final int endtime = player.getNetConnection().getBonusExpire();
			if(endtime >= System.currentTimeMillis() / 1000L)
				html = HtmCache.getInstance().getHtml("scripts/services/RateBonusAlready.htm", player).replaceFirst("endtime", TimeUtils.toSimpleFormat(endtime * 1000L));
			else
			{
				html = HtmCache.getInstance().getHtml("scripts/services/RateBonus.htm", player);
				String add = "";
				for(int i = 0; i < Config.SERVICES_RATE_BONUS_DAYS.length; ++i)
					add = add + "<a action=\"bypass -h scripts_services.RateBonus:get " + i + "\">" + (int) (Config.SERVICES_RATE_BONUS_VALUE[i] * 100.0f - 100.0f) + "% " + (ru ? "\u043d\u0430" : "for") + " " + Config.SERVICES_RATE_BONUS_DAYS[i] + " " + Util.dayFormat(ru, String.valueOf(Config.SERVICES_RATE_BONUS_DAYS[i])) + " - " + Config.SERVICES_RATE_BONUS_PRICE[i] + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_RATE_BONUS_ITEM[i]).getName() + "</a><br>";
				html = html.replaceFirst("%toreplace%", add);
			}
		}
		else
			html = HtmCache.getInstance().getHtml("scripts/services/RateBonusNo.htm", player);
		show(html, player);
	}

	public void get(final String[] param)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_RATE_TYPE < 1)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		if(player.getNetConnection().getBonus() < 0.0f)
		{
			show(HtmCache.getInstance().getHtml("scripts/services/RateBonusNo.htm", player), player);
			return;
		}
		final int endtime = player.getNetConnection().getBonusExpire();
		if(endtime >= System.currentTimeMillis() / 1000L)
		{
			show(HtmCache.getInstance().getHtml("scripts/services/RateBonusAlready.htm", player).replaceFirst("endtime", TimeUtils.toSimpleFormat(endtime * 1000L)), player);
			return;
		}
		final int i = Integer.parseInt(param[0]);
		if(Config.SERVICES_RATE_TYPE == 1 && AuthServerCommunication.getInstance().isShutdown())
		{
			list();
			player.sendMessage(player.isLangRus() ? "\u0421\u0435\u0440\u0432\u0438\u0441 \u0432\u0440\u0435\u043c\u0435\u043d\u043d\u043e \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d." : "Service is temporarily unavailable.");
			return;
		}

		if(getItemCount(player, Config.SERVICES_RATE_BONUS_ITEM[i]) >= Config.SERVICES_RATE_BONUS_PRICE[i]) {
			if (deleteItem(player, Config.SERVICES_RATE_BONUS_ITEM[i], Config.SERVICES_RATE_BONUS_PRICE[i])) {
				Log.addLog("Rate bonus: " + player.toString() + " | value x" + Config.SERVICES_RATE_BONUS_VALUE[i] + " | days " + Config.SERVICES_RATE_BONUS_DAYS[i], "services");
				Bonus.giveBonus(player, Config.SERVICES_RATE_BONUS_VALUE[i], Config.SERVICES_RATE_BONUS_DAYS[i] * 24);
				return;
			}
		}
		if(Config.SERVICES_RATE_BONUS_ITEM[i] == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	public void howtogetcol()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		show("scripts/services/howtogetcol.htm", player);
	}

	public void listPB()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		boardPB(player);
	}

	public static void boardPB(final Player player)
	{
		if(Config.DELUXE_PREMIUM_PRICE.length < 3)
			return;
		if(!Config.DELUXE_PREMIUM_PAGE_PATH.isEmpty())
		{
			show(HtmCache.getInstance().getHtml(Config.DELUXE_PREMIUM_PAGE_PATH, player), player);
			return;
		}
		final boolean ru = player.isLangRus();
		final String text = ru ? "\u041a\u043e\u043c\u0430\u043d\u0434\u0430 <font color=\"LEVEL\">.premiumbuff</font> \u043f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0434\u0430\u0442\u0443 \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f" : "Command <font color=\"LEVEL\">.premiumbuff</font> show end time";
		String html = "<title>Premium Buffer</title><center><br>" + text + "<br><br><br>";
		int n = 0;
		for(int i = 0; i < Config.DELUXE_PREMIUM_PRICE.length; i += 3)
		{
			++n;
			html = html + "<a action=\"bypass -h scripts_services.RateBonus:getPB " + n + "\">" + Config.DELUXE_PREMIUM_PRICE[i + 2] + " " + Util.dayFormat(ru, String.valueOf(Config.DELUXE_PREMIUM_PRICE[i + 2])) + " - " + Config.DELUXE_PREMIUM_PRICE[i + 1] + " " + ItemTable.getInstance().getTemplate(Config.DELUXE_PREMIUM_PRICE[i]).getName() + "</a><br>";
		}
		show(html, player);
	}

	public void getPB(final String[] param)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final int i = Math.min(Math.max(Integer.parseInt(param[0]) * 3, 1), Config.DELUXE_PREMIUM_PRICE.length);
		final int id = Config.DELUXE_PREMIUM_PRICE[i - 3];
		final int count = Config.DELUXE_PREMIUM_PRICE[i - 2];
		final long days = Config.DELUXE_PREMIUM_PRICE[i - 1];
		if(getItemCount(player, id) >= count) {
			if (deleteItem(player, id, count)) {
				final String v = player.getVar("PremiumBuff");
				if (v != null && Long.parseLong(v) > System.currentTimeMillis()) {
					player.setVar("PremiumBuff", String.valueOf(Long.parseLong(v) + 86400000L * days));
					if (player.isLangRus())
						player.sendMessage("\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0440\u043e\u0434\u043b\u0438\u043b\u0438 \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f \u043a \u0431\u0430\u0444\u0444\u0443 \u043d\u0430 " + days + " " + Util.dayFormat(true, String.valueOf(days)) + ".");
					else
						player.sendMessage("You have successfully extended the premium access to buff on " + days + " " + Util.dayFormat(false, String.valueOf(days)) + ".");
					Log.addLog(player.toString() + " extended premium access to buff on " + days + " days.", "services");
				} else {
					player.setVar("PremiumBuff", String.valueOf(System.currentTimeMillis() + 86400000L * days));
					if (player.isLangRus())
						player.sendMessage("\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0440\u0438\u043e\u0431\u0440\u0435\u043b\u0438 \u043f\u0440\u0435\u043c\u0438\u0443\u043c \u0434\u043e\u0441\u0442\u0443\u043f \u043a \u0431\u0430\u0444\u0444\u0443 \u043d\u0430 " + days + " " + Util.dayFormat(true, String.valueOf(days)) + ".");
					else
						player.sendMessage("You have successfully acquired premium access to buff on " + days + " " + Util.dayFormat(false, String.valueOf(days)) + ".");
					Log.addLog(player.toString() + " acquired premium access to buff on " + days + " days.", "services");
				}
				if (Config.DELUXE_NPC_PAGE_AFTER_BUY) {
					final NpcInstance npc = getNpc();
					if (NpcInstance.canBypassCheck(player, npc) && npc.getNpcId() == Config.SERVICES_Buffer_Id)
						show(HtmCache.getInstance().getHtml("scripts/services/NpcBuffer/buffsDeluxe.htm", player), player);
				} else if (!Config.DELUXE_PAGE_AFTER_BUY.isEmpty())
					show(HtmCache.getInstance().getHtml(Config.DELUXE_PAGE_AFTER_BUY, player), player);
				return;
			}
		}
		if(id == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	public void listESB()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final boolean ru = player.isLangRus();
		if(!Config.ALLOW_ES_BONUS)
		{
			player.sendMessage(ru ? "Сервис отключен." : "Service disabled.");
			return;
		}
		final String text = ru ? "\u041a\u043e\u043c\u0430\u043d\u0434\u0430 <font color=\"LEVEL\">.esbonus</font> \u043f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0434\u0430\u0442\u0443 \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f" : "Command <font color=\"LEVEL\">.esbonus</font> show end time";
		String html = "<title>Enchant Skill Bonus</title><center><br>" + text + "<br>";
		html = html + "<font color=\"ffff33\"> +" + Config.ES_BONUS_CHANCE + "% " + (ru ? "\u043a \u0448\u0430\u043d\u0441\u0443 \u0437\u0430\u0442\u043e\u0447\u043a\u0438 \u0441\u043a\u0438\u043b\u043e\u0432" : "chance of skills enchant") + ":</font><br><br>";
		int n = 0;
		for(int i = 0; i < Config.ES_BONUS_PRICE.length; i += 3)
		{
			++n;
			html = html + "<a action=\"bypass -h scripts_services.RateBonus:getESB " + n + "\">" + Config.ES_BONUS_PRICE[i + 2] + " " + Util.dayFormat(ru, String.valueOf(Config.ES_BONUS_PRICE[i + 2])) + " - " + Config.ES_BONUS_PRICE[i + 1] + " " + ItemTable.getInstance().getTemplate(Config.ES_BONUS_PRICE[i]).getName() + "</a><br>";
		}
		show(html, player);
	}

	public void getESB(final String[] param)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.ALLOW_ES_BONUS)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		final int i = Math.min(Math.max(Integer.parseInt(param[0]) * 3, 1), Config.ES_BONUS_PRICE.length);
		final int id = Config.ES_BONUS_PRICE[i - 3];
		final int count = Config.ES_BONUS_PRICE[i - 2];
		final long days = Config.ES_BONUS_PRICE[i - 1];
		if(getItemCount(player, id) >= count) {
			if (deleteItem(player, id, count)) {
				final String v = player.getVar("BonusES");
				if (v != null && Long.parseLong(v) > System.currentTimeMillis()) {
					player.setVar("BonusES", String.valueOf(Long.parseLong(v) + 86400000L * days));
					if (player.isLangRus())
						player.sendMessage("\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0440\u043e\u0434\u043b\u0438\u043b\u0438 \u0431\u043e\u043d\u0443\u0441 \u0437\u0430\u0442\u043e\u0447\u043a\u0438 \u0441\u043a\u0438\u043b\u043e\u0432 \u043d\u0430 " + days + " " + Util.dayFormat(true, String.valueOf(days)) + ".");
					else
						player.sendMessage("You have successfully extended enchant skill bonus on " + days + " " + Util.dayFormat(false, String.valueOf(days)) + ".");
					Log.addLog(player.toString() + " extended enchant skill bonus on " + days + " days.", "services");
				} else {
					player.setVar("BonusES", String.valueOf(System.currentTimeMillis() + 86400000L * days));
					if (player.isLangRus())
						player.sendMessage("\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0440\u0438\u043e\u0431\u0440\u0435\u043b\u0438 \u0431\u043e\u043d\u0443\u0441 \u0437\u0430\u0442\u043e\u0447\u043a\u0438 \u0441\u043a\u0438\u043b\u043e\u0432 \u043d\u0430 " + days + " " + Util.dayFormat(true, String.valueOf(days)) + ".");
					else
						player.sendMessage("You have successfully acquired enchant skill bonus on " + days + " " + Util.dayFormat(false, String.valueOf(days)) + ".");
					Log.addLog(player.toString() + " acquired enchant skill bonus on " + days + " days.", "services");
				}
				return;
			}
		}
		if(id == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	public void keyCheck(final String[] param)
	{
		if(!Config.SERVICES_CHAR_KEY)
			return;
		final Player player = getSelf();
		if(player == null)
			return;
		try
		{
			if(Config.CHAR_KEYS.get(player.getObjectId()).equals(param[0].trim()))
			{
				player.setKeyBlocked(false);
				player.sendMessage(new CustomMessage("l2s.KeyUnFrozen"));
				if(!player.classWindow())
					show(HtmCache.getInstance().getHtml("scripts/commands/voiced/charKey/char_key_ok.html", player), player);
				if(Config.CHAR_KEY_FAIL_BAN > 0 && Config.CHECKS.containsKey(player.getHWID()))
					Config.CHECKS.remove(player.getHWID());
			}
			else
			{
				if(Config.CHAR_KEY_FAIL_BAN > 0)
				{
					final int i = Config.CHECKS.containsKey(player.getHWID()) ? Config.CHECKS.get(player.getHWID()) + 1 : 1;
					if(i >= Config.CHAR_KEY_FAIL_BAN)
					{
						AutoBan.addHwidBan("", player.getHWID(), "\u041f\u043e\u043f\u044b\u0442\u043a\u0430 \u0431\u0440\u0443\u0442\u0430 \u043a\u043b\u044e\u0447\u0430 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 " + player.getName(), 0L, "");
						Config.CHECKS.remove(player.getHWID());
						player.kick(false);
						return;
					}
					Config.CHECKS.put(player.getHWID(), i);
				}
				if(Config.CHAR_KEY_FAIL_KICK > 0)
				{
					final Player l2Player = player;
					++l2Player.CK_FAIL;
					if(player.CK_FAIL >= Config.CHAR_KEY_FAIL_KICK)
						player.kick(false);
				}
			}
		}
		catch(Exception e)
		{
			player.kick(false);
		}
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Rate bonus");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
